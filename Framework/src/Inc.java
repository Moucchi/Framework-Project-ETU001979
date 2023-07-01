package etu1979.framework.util;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import etu1979.framework.Mapping;
import etu1979.framework.Annotation.URL;
import etu1979.framework.ModelView;

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

    public static void set(Class modelClass, String attributeName, String value) {
        Method toBeInvokedMethod = getSetter(modelClass, attributeName);
    }

    public static ArrayList<String> getInputFiedlsdNames(HttpServletRequest req) {
        Enumeration<String> parameterNames = req.getParameterNames();
        ArrayList<String> inputFiedlsdNames = new ArrayList<>();

        while (parameterNames.hasMoreElements()) {
            String fieldName = parameterNames.nextElement();
            inputFiedlsdNames.add(fieldName);
        }
        return inputFiedlsdNames;
    }

    public static boolean checkParameters(HttpServletRequest req, Method method) {
        String[] necessaryParameters = method.getAnnotation(URL.class).parameters();

        if (necessaryParameters.length > 0) {
            for (String necessaryParameter : necessaryParameters) {
                if (!getInputFiedlsdNames(req).contains(necessaryParameter)) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static ArrayList<String> getNecessaryParametersValue(HttpServletRequest req, Method method) {
        ArrayList<String> result = new ArrayList<>();

        if (checkParameters(req, method)) {
            for (String inputFieldName : getInputFiedlsdNames(req)) {
                result.add(req.getParameter(inputFieldName));
            }
        }

        return result;
    }

    public static ModelView call(HttpServletRequest req, Method method , Object caller){
        ModelView result = new ModelView();
        try {
            ArrayList<String> parameters = getNecessaryParametersValue(req, method);
            if( parameters.size() > 0 ){
                result = (ModelView) method.invoke(caller , parameters.toArray() );
            }else{
                result = (ModelView) method.invoke(caller);
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static boolean isIn(Class modelClass , String inputName){
        Field[] fields = modelClass.getDeclaredFields();
        
        for (Field field : fields) {
            String fieldName = field.getName();

            if( fieldName.equalsIgnoreCase(inputName) ){
                return true;
            }
        }

        return false;
    }
}