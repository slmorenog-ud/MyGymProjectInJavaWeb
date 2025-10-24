<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.agym.modelo.Rutina" %>
<%@ page import="com.agym.modelo.Ejercicio" %>
<%@ page import="java.util.List" %>

<%
    Rutina rutina = (Rutina) request.getAttribute("rutina");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Tu Rutina Personalizada - AGym</title>
    <link rel="stylesheet" href="css/styles.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;700&display=swap" rel="stylesheet">
    <style>
        .rutina-dia { margin-bottom: 30px; }
        .ejercicio { display: flex; align-items: center; margin-bottom: 20px; border-bottom: 1px solid #eee; padding-bottom: 20px; }
        .ejercicio img { width: 150px; height: 100px; object-fit: cover; margin-right: 20px; border-radius: 4px; }
        .ejercicio-info { flex-grow: 1; }
        .ejercicio-info h4 { margin: 0 0 5px 0; }
        .ejercicio-info p { margin: 0; }
    </style>
</head>
<body>
    <header>
        <h1><a href="dashboard.jsp">AGym</a></h1>
        <nav>
            <a href="logout">Cerrar Sesión</a>
        </nav>
    </header>
    <main>
        <div class="container">
            <h2>Aquí está tu rutina:</h2>

            <% if (rutina != null && !rutina.getDias().isEmpty()) { %>
                <% for (Rutina.DiaRutina dia : rutina.getDias()) { %>
                    <div class="rutina-dia">
                        <h3><%= dia.getNombre() %></h3>
                        <% for (Ejercicio ejercicio : dia.getEjercicios()) { %>
                            <div class="ejercicio">
                                <img src="<%= ejercicio.getImagenUrl() %>" alt="Demostración de <%= ejercicio.getNombre() %>">
                                <div class="ejercicio-info">
                                    <h4><%= ejercicio.getNombre() %></h4>
                                    <p><strong>Instrucciones:</strong> <%= ejercicio.getDescripcion() %></p>
                                    <p><strong>Series y Repeticiones:</strong> <%= ejercicio.getSeriesYRepeticiones() %></p>
                                </div>
                            </div>
                        <% } %>
                    </div>
                <% } %>
            <% } else { %>
                <p>No se pudo generar una rutina. Por favor, intenta de nuevo.</p>
            <% } %>

            <a href="dashboard.jsp" class="btn">Volver al Panel</a>
        </div>
    </main>
</body>
</html>
