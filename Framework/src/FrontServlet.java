package etu1979.framework.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import etu1979.framework.util.Inc;
import etu1979.framework.Mapping;
import etu1979.framework.Annotation.URL;
import etu1979.framework.ModelView;
import etu1979.framework.FileUpload;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import jakarta.servlet.ServletConfig;

@MultipartConfig
public class FrontServlet extends HttpServlet {
    HashMap<String, Mapping> mappingURLS;

    @Override
    public void init() throws ServletException {
        super.init();
        HashMap<String, Mapping> toBeUsed = Inc.map(this);

        this.setMappingURLS(toBeUsed);
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        String k = Inc.getURL(req);
        Mapping map = getMappingURLS().get(k);
        ModelView modelview = null;

        for (Entry<String, Mapping> entry : getMappingURLS().entrySet()) {
            out.println("Key of entry " + entry.getKey());
            out.println("ClassName : " + entry.getValue().getClassName());
            out.println("MethodName : " + entry.getValue().getMethod() + "\n");
        }

        out.println("Key : " + k);
        out.println("Class name : " + map.getClassName());

        try {
            Class process_class = Class.forName(map.getClassName());
            Object objet = process_class.getConstructor().newInstance();
            Method method = Inc.getMethode(process_class, map.getMethod(), k);
            HashMap<String, Object> fetchedData = new HashMap<>();
            ArrayList<String> inputFiedlsdNames = Inc.getInputFiedlsdNames(req);

            out.println("Method's details : " + method);
            out.println("Return type : " + method.getReturnType().getSimpleName() + "\n");

            if (method.getReturnType().equals(ModelView.class)) {
                for (String inputName : inputFiedlsdNames) {
                    Field temp = process_class.getDeclaredField(inputName);
                    if (temp.getType() != FileUpload.class) {
                        if (Inc.isIn(process_class, inputName)) {
                            Method setter = Inc.getSetter(process_class, inputName);
                            out.println("setter method's details " + setter);
                            setter.invoke(objet, req.getParameter(inputName));
                        }
                    }
                }

                req.setAttribute(process_class.getName(), objet);

                modelview = (ModelView) method.invoke(objet, Inc.getNecessaryParametersValue(req, method).toArray());
                out.println("View : " + modelview.getView());

                for (String name : inputFiedlsdNames) {
                    modelview.addItem(name, req.getParameter(name));
                }

                fetchedData = modelview.getData();

                for (String key : fetchedData.keySet()) {
                    req.setAttribute(key, fetchedData.get(key));
                    out.println(key + "," + fetchedData.get(key));
                }

                try {
                    Collection<Part> parts = req.getParts();
                    if (parts.size() > 0) {
                        for (Field field : process_class.getDeclaredFields()) {
                            if (field.getType() == FileUpload.class) {
                                Method function = objet.getClass().getDeclaredMethod("setProfilPic", field.getType());
                                Object value = fileTraitement(parts, field);
                                function.invoke(objet, value);
                            }
                        }
                    }
                } catch (Exception e) {
                    out.println(e);
                }

                RequestDispatcher dispatcher = req.getRequestDispatcher(modelview.getView());
                dispatcher.forward(req, resp);
            }

        } catch (Exception e) {
            out.println(e);
        }
    }

    protected FileUpload fileTraitement(Collection<Part> files, Field field) {
        FileUpload file = new FileUpload();
        String name = field.getName();
        boolean exists = false;
        Part filepart = null;
        for (Part part : files) {
            String contentDisposition = part.getHeader("content-disposition");
            System.out.println(contentDisposition);
            if (part.getName().equals(name)) {
                filepart = part;
                exists = true;
                break;
            }
        }
        try (InputStream io = filepart.getInputStream()) {
            ByteArrayOutputStream buffers = new ByteArrayOutputStream();
            byte[] buffer = new byte[(int) filepart.getSize()];
            int read;
            while ((read = io.read(buffer, 0, buffer.length)) != -1) {
                buffers.write(buffer, 0, read);
            }
            file.setFileName(this.fileName(filepart));
            file.setBytes(buffers.toByteArray());
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String fileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        System.out.println(contentDisposition);
        String[] parts = contentDisposition.split(";");
        for (String partStr : parts) {
            if (partStr.trim().startsWith("filename"))
                return partStr.substring(partStr.indexOf('=') + 1).trim().replace("\"", "");
        }
        return null;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    public HashMap<String, Mapping> getMappingURLS() {
        return mappingURLS;
    }

    public void setMappingURLS(HashMap<String, Mapping> mappingURLS) {
        this.mappingURLS = mappingURLS;
    }
}