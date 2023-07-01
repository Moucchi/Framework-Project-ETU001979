package etu1979.framework.util;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import etu1979.framework.Mapping;
import etu1979.framework.Annotation.URL;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;

public class Inc {
    public static HashMap<String, Mapping> map(HttpServlet servlet) {
        HashMap<String, Mapping> result = new HashMap<>();
        String fullPath = getPath(servlet);
        ArrayList<String> classNames = getAllClassNames(fullPath, servlet);

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

    public static ArrayList<String> getAllClassNames(String path, HttpServlet servlet) {
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

                        String temp = getPath(servlet);
                        temp = temp.replace("\\", ".");

                        toBeAdded = toBeAdded.replace(temp, "");
                        result.add(toBeAdded);
                    }

                } else {
                    String subPath = file.getPath();
                    result.addAll(getAllClassNames(subPath, servlet));
                }
            }

        }

        return result;
    }

    public static String getPath(HttpServlet servlet) {
        String relativePath = "\\WEB-INF\\classes\\";
        String contextPath = servlet.getServletContext().getContextPath();

        contextPath = contextPath.substring(1);

        String fullPath = "webapps\\" + contextPath + relativePath;

        return fullPath;
    }

    public static String getURL(HttpServletRequest request) {
        String value = new String();
        String URI = request.getRequestURI();
        String context_path = request.getContextPath();
        value = URI.substring(context_path.length() + 1);
        return value;
    }

    public static Method getSetter(Class modelClass, String attributeName) {
        Field[] fiels = modelClass.getDeclaredFields();
        String methodName = "";

        for (Field field : fiels) {
            if (field.getName().equalsIgnoreCase(attributeName)) {
                methodName = "set" + attributeName;
                break;
            }
        }

        Method[] methods = modelClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equalsIgnoreCase(methodName)) {
                if (doesMatch(method.getParameterTypes())) {
                    return method;
                }
            }
        }

        return null;
    }

    public static boolean doesMatch(Class[] methodParameterTypes) {
        if (methodParameterTypes.length == 0) {
            return false;
        } else {
            if (methodParameterTypes.length == 1) {
                if (methodParameterTypes[0] == String.class) {
                    return true;
                }
            }
        }

        return false;
    }

    public static String capitalize(String toBeCapitalized) {
        String result = toBeCapitalized.toUpperCase().charAt(0)
                + toBeCapitalized.substring(1, toBeCapitalized.length());

        return result;
    }

    public static void set( Class modelClass ,String attributeName , String value ) {
        Method toBeInvokedMethod = getSetter(modelClass, attributeName);
    }
}