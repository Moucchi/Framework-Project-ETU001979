<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>

<body>
    <%
        String nom=(String)request.getAttribute("nom") ;
        String prenom=(String)request.getAttribute("prenom") ;
    %>

    <h4>Wassuuuuup <%= nom %> <%= prenom %></h4>
</body>

</html>