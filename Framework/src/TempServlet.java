package etu2075.framework.servlet;
import javax.servlet.*;
import javax.servlet.http.*;
import etu2075.annotation.Url;
import etu2075.framework.Mapping;
import etu2075.framework.ModelView;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.PackageTool;
import java.io.*;
import java.lang.reflect.Method;
import java.util.HashMap;

public class TempServlet extends HttpServlet{
    HashMap<String,Mapping> urlMapping = new HashMap<>();

    @Override
    public void init() throws ServletException {
        try {
            for (Class c : PackageTool.inPackage(getServletConfig().getInitParameter("model"))){
                for (Method m : c.getDeclaredMethods()){
                    if(m.isAnnotationPresent(Url.class)){
                        urlMapping.put(m.getAnnotation(Url.class).url(), new Mapping(c.getName(), m.getAnnotation(Url.class).url().split("-")[1]));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void processRequest(HttpServletRequest req,HttpServletResponse res) throws IOException{
        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
        String url = req.getRequestURI();
        url = url.split("/")[url.split("/").length - 1];
        out.println(url);
        if(urlMapping.containsKey(url)){
            try {
                Object act = Class.forName(urlMapping.get(url).getClassName()).newInstance();
                ModelView mv = (ModelView)act.getClass().getDeclaredMethod(urlMapping.get(url).getMethod()).invoke(act);
                RequestDispatcher requestDispatcher = req.getRequestDispatcher(mv.getView()) ;    
                requestDispatcher.forward(req,res);
            } catch (Exception e) {
                    e.printStackTrace();
            }
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        processRequest(req, res);
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        processRequest(req, res);
    }
}