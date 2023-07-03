<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>

<body>
    <%@ page import="etu1979.framework.model.Emp" %>
    <%@ page import="etu1979.framework.FileUpload" %>

    <%
        Emp object = (Emp)request.getAttribute("etu1979.framework.model.Emp");
    %>

    <h4>Nom fichier <%= object.getProfilPic().getFileName() %></h4>
</body>

</html>