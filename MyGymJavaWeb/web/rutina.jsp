<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.agym.modelo.Rutina" %>
<%@ page import="com.agym.modelo.Ejercicio" %>
<%@ page import="java.util.List" %>
<%@ page import="com.agym.modelo.Rutina.DiaRutina" %>

<%@ page import="java.util.Map" %>

<%
    Rutina rutina = (Rutina) request.getAttribute("rutina");
    String objetivo = (String) request.getAttribute("objetivoUsuario");

    // Mapa para traducir los valores del objetivo a texto legible
    Map<String, String> traduccionObjetivos = Map.of(
        "bajar_peso", "Bajar de Peso",
        "subir_peso", "Subir de Peso (Hipertrofia)",
        "fortalecer", "Ganar Fuerza"
    );
    String objetivoLegible = traduccionObjetivos.getOrDefault(objetivo, "General");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Tu Rutina Personalizada - MyGymJW</title>
    <link rel="stylesheet" href="css/styles.css">
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
        <h1><a href="dashboard.jsp">MyGymJW</a></h1>
        <nav>
            <a href="logout">Cerrar Sesión</a>
        </nav>
    </header>
    <main>
        <div class="container">
            <h2>Aquí está tu rutina:</h2>
            <% if (objetivo != null && !objetivo.isEmpty()) { %>
                <h4 style="text-align: center; color: #2E4052; margin-top: -10px; margin-bottom: 25px;">Tu objetivo principal: <strong><%= objetivoLegible %></strong></h4>
            <% } %>

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
