package etu1979.framework.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import etu1979.framework.Mapping;
import etu1979.framework.Annotation.URL;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FrontServlet extends HttpServlet {
    HashMap<String, Mapping> mappingURLS;

    @Override
    public void init() throws ServletException {
        super.init();
        ArrayList<String> classNames = getAllClassNames(".\\webapps\\Framework\\WEB-INF\\classes\\");
        HashMap<String, Mapping> toBeUsed = new HashMap<>();

        for (String classs : classNames) {
            try {
                Class temp = Class.forName(classs);

                for (Method method : temp.getDeclaredMethods()) {
                    Mapping mapping = new Mapping();
                    URL annotation = method.getAnnotation(URL.class);

                    if (annotation != null) {
                        mapping.setClassName(temp.getCanonicalName());
                        mapping.setMethod(method.getName());

                        toBeUsed.put(annotation.value(), mapping);
                    }
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        this.setMappingURLS(toBeUsed);
    }

    protected ArrayList<String> getAllClassNames(String path) {
        ArrayList<String> result = new ArrayList<>();
        File directory = new File(path);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                if (fileName.contains(".")) {
                    if (fileName.contains(".class")) {
                        String toBeAdded = "";
                        toBeAdded = file.getPath();
                        toBeAdded = toBeAdded.replace(".\\", "");
                        toBeAdded = toBeAdded.replace(".class", "");
                        toBeAdded = toBeAdded.replace("\\", ".");
                        toBeAdded = toBeAdded.replace("webapps.Framework.WEB-INF.classes.", "");
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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /*
         * String url = request.getServletPath();
         * String requete = request.getQueryString();
         * if (requete != null) {
         * url = url + "?" + requete;
         * }
         * request.setAttribute("url", url);
         * RequestDispatcher dispat = request.getRequestDispatcher("url.jsp");
         * dispat.forward(request, response);
         */

        PrintWriter out = response.getWriter();

        out.println("Tonga eh");
        for (String key : this.getMappingURLS().keySet()) {
            out.println(this.getMappingURLS().get(key).getClassName());
            out.println(this.getMappingURLS().get(key).getMethod());
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