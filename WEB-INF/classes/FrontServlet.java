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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        for (String key : this.getMappingURLS().keySet()) {
            out.println(this.getMappingURLS().get(key).getClassName());
            out.println(this.getMappingURLS().get(key).getMethod());
        }

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