package etu1979.framework.servlet;

import java.io.*;
import java.util.HashMap;

import etu1979.framework.Mapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FrontServlet extends HttpServlet {
    HashMap<String, Mapping> mappingURLS;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getServletPath();
        String requete = request.getQueryString();
        if (requete != null) {
            url = url + "?" + requete;
        }
        request.setAttribute("url", url);
        RequestDispatcher dispat = request.getRequestDispatcher("url.jsp");
        dispat.forward(request, response);
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
}