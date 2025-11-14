<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.agym.util.DatabaseUtil" %>
<%@ page import="com.agym.util.UsuarioDAO" %>
<%@ page import="com.agym.modelo.Usuario" %>
<%@ page import="java.sql.Connection" %>

<html>
<head>
    <title>Prueba de Base de Datos</title>
</head>
<body>
    <h1>Realizando prueba de conexión y registro...</h1>
    <%
        String status = "ERROR";
        String mensaje = "";

        // 1. Probar la conexión
        DatabaseUtil dbUtil = new DatabaseUtil();
        Connection conn = dbUtil.getConexion();

        if (conn == null) {
            mensaje = "Fallo la conexión a la base de datos: " + DatabaseUtil.getMensaje();
        } else {
            mensaje = "Conexión a la base de datos exitosa.";

            try {
                // 2. Probar el registro de un usuario de prueba
                UsuarioDAO usuarioDAO = new UsuarioDAO(conn);
                Usuario testUser = new Usuario();
                String testEmail = "testuser@" + System.currentTimeMillis() + ".com";
                testUser.setNombre("Test User");
                testUser.setEmail(testEmail);
                testUser.setPassword("password123");
                testUser.setFechaNacimiento("2000-01-01");
                testUser.setGenero("Otro");

                usuarioDAO.crearUsuario(testUser);
                mensaje += "<br>Usuario de prueba creado con email: " + testEmail;

                // 3. Probar el login (recuperar el usuario)
                Usuario retrievedUser = usuarioDAO.getUsuarioPorEmail(testEmail);
                if (retrievedUser != null && retrievedUser.getPassword().equals("password123")) {
                    status = "ÉXITO";
                    mensaje += "<br>¡Login exitoso! El usuario fue recuperado correctamente.";
                } else {
                    mensaje += "<br>Fallo el login: no se pudo recuperar el usuario o la contraseña no coincide.";
                }

            } catch (Exception e) {
                mensaje += "<br>Ocurrió una excepción durante la prueba: " + e.getMessage();
                e.printStackTrace(response.getWriter());
            } finally {
                // 4. Cerrar la conexión
                dbUtil.desconectar();
                mensaje += "<br>Conexión cerrada.";
            }
        }
    %>

    <h2>Resultado de la prueba: <span style="color: <%= status.equals("ÉXITO") ? "green" : "red" %>;"><%= status %></span></h2>
    <p><%= mensaje %></p>

</body>
</html>
