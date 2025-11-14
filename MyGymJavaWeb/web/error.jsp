<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<html>
<head>
    <title>Error</title>
</head>
<body>
    <h1>Ha ocurrido un error en la aplicación</h1>
    <p>Lo sentimos, algo ha salido mal. Por favor, inténtalo de nuevo más tarde.</p>

    <h2>Detalles del error:</h2>
    <p style="color:red;">
        <%
            String msg = request.getParameter("msg");
            if (msg != null && !msg.isEmpty()) {
                out.println(msg);
            } else {
                out.println("No se proporcionaron detalles del error.");
            }
        %>
    </p>
</body>
</html>
