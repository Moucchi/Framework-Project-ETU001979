package etu1979.framework.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import etu1979.framework.util.Inc;
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
                    if (Inc.isIn(process_class, inputName)) {
                        Method setter = Inc.getSetter(process_class, inputName);
                        out.println("setter method's details " + setter);
                        setter.invoke(objet, req.getParameter(inputName));
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

                RequestDispatcher dispatcher = req.getRequestDispatcher(modelview.getView());
                dispatcher.forward(req, resp);
            }

        } catch (Exception e) {
            out.println(e);
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