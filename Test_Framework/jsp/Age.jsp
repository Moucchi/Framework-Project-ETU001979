<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>

<body>
    <%@ page import="etu1979.framework.model.Emp" %>
    <%
        String nom=(String)request.getAttribute("nom") ;
        String prenom=(String)request.getAttribute("age") ;
        Emp object = (Emp)request.getAttribute("etu1979.framework.model.Emp");
        String age = (String)request.getAttribute("difference");
    %>

    <p>
        <h4>Wassuuuuup <%= object.getNom() %> <%= object.getAge() %> age Hehe</h4>
    </p>
    <p>
        <h4>Difference  <%= age %> ans</h4>
    </p>
</body>

</html>