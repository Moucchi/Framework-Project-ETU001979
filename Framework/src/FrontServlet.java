package etu1979.framework.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import etu1979.framework.Mapping;
import etu1979.framework.Annotation.URL;
import etu1979.framework.ModelView;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletConfig;

public class FrontServlet extends HttpServlet {
    HashMap<String, Mapping> mappingURLS;

    @Override
    public void init() throws ServletException {
        super.init();
        HashMap<String, Mapping> toBeUsed = map();

        this.setMappingURLS(toBeUsed);
    }

    public HashMap<String, Mapping> map() {
        HashMap<String, Mapping> result = new HashMap<>();

        String fullPath = getPath();

        ArrayList<String> classNames = getAllClassNames(fullPath);

        for (String classs : classNames) {
            try {
                Class temp = Class.forName(classs);

                for (Method method : temp.getDeclaredMethods()) {
                    Mapping mapping = new Mapping();
                    URL annotation = method.getAnnotation(URL.class);

                    if (annotation != null) {
                        mapping.setClassName(temp.getCanonicalName());
                        mapping.setMethod(method.getName());

                        result.put(annotation.value(), mapping);
                    }
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public ArrayList<String> getAllClassNames(String path) {
        ArrayList<String> result = new ArrayList<>();
        result.add(path);
        File directory = new File(path);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (!file.isDirectory()) {
                    String fileName = file.getName();

                    if (fileName.contains(".class")) {
                        String toBeAdded = file.getPath();
                        toBeAdded = toBeAdded.replace(".class", "");
                        toBeAdded = toBeAdded.replace("\\", ".");

                        String temp = getPath();
                        temp = temp.replace("\\", ".");

                        toBeAdded = toBeAdded.replace(temp, "");
                        result.add(toBeAdded);
                    }

                } else {
                    String subPath = file.getPath();
                    result.addAll(getAllClassNames(subPath));
                }
            }

        }

        return result;
    }

    public String getPath() {
        String relativePath = "\\WEB-INF\\classes\\";
        String contextPath = getServletContext().getContextPath();

        contextPath = contextPath.substring(1);

        String fullPath = "webapps\\" + contextPath + relativePath;

        return fullPath;
    }

    public String getURL(HttpServletRequest request) {
        String value = new String();
        String URI = request.getRequestURI();
        String context_path = request.getContextPath();
        value = URI.substring(context_path.length() + 1);
        return value;
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpServletMapping mapping = req.getHttpServletMapping();
        PrintWriter out = resp.getWriter();
        String k = getURL(req);
        Mapping map = getMappingURLS().get(k);
        ModelView modelview = null;

        try {
            Class<?> process_class = Class.forName(map.getClassName());
            Object objet = process_class.newInstance();
            Method method = objet.getClass().getDeclaredMethod(map.getMethod());
            HashMap<String, Object> fetchedData = new HashMap<>();

            out.println("k : " + k);
            out.println("Class name : " + map.getClassName());
            out.println("Method : " + map.getMethod());

            if (method.getReturnType().equals(ModelView.class)) {
                modelview = (ModelView) method.invoke(objet);
                fetchedData = modelview.getData();

                for (String key : fetchedData.keySet()) {
                }

            }

            out.println("Type de retour : " + method.getReturnType().getCanonicalName());
            out.println("Modelview : " + ModelView.class.getCanonicalName());

            RequestDispatcher dispatcher = req.getRequestDispatcher(modelview.getView());
            dispatcher.forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
        }

        out.println("URI : " + req.getRequestURI());
        out.println("Query : " + req.getQueryString());
        out.println("URL : " + req.getRequestURL());
        out.println("Pattern : " + mapping.getPattern());
        out.println("Match value : " + mapping.getMatchValue());
        out.println("Get URL : " + getURL(req));

        for (String key : this.getMappingURLS().keySet()) {
            out.println("\nClass name : " +
                    this.getMappingURLS().get(key).getClassName());
            out.println("Mathod : " + this.getMappingURLS().get(key).getMethod());
            out.println("Annotation value : " + key);
        }
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