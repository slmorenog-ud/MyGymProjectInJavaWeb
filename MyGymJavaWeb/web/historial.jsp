<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.agym.modelo.RutinaGuardada" %>
<%@ page import="com.agym.modelo.Ejercicio" %>
<%@ page import="com.agym.modelo.Rutina.DiaRutina" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%
    List<RutinaGuardada> historial = (List<RutinaGuardada>) request.getAttribute("historialRutinas");
    SimpleDateFormat sdf = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy 'a las' HH:mm");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mi Historial de Rutinas - MyGymJW</title>
    <link rel="stylesheet" href="css/styles.css">
    <style>
        .rutina-guardada {
            border: 1px solid #ddd;
            border-radius: 8px;
            margin-bottom: 25px;
            padding: 20px;
        }
        .rutina-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 15px;
        }
        .rutina-header h3 {
            margin: 0;
        }
        .estado {
            padding: 5px 10px;
            border-radius: 15px;
            font-weight: bold;
            color: white;
        }
        .estado.guardada { background-color: #FFC857; }
        .estado.completada { background-color: #BDD9BF; color: #2E4052; }
        .ejercicio-simple {
            padding-left: 20px;
            border-left: 3px solid #eee;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
    <header>
        <h1><a href="dashboard.jsp">MyGymJW</a></h1>
        <nav>
            <form action="logout" method="post" style="display:inline;">
                <button type="submit" class="link-button">Cerrar Sesión</button>
            </form>
        </nav>
    </header>
    <main>
        <div class="container">
            <h2>Mi Historial de Rutinas</h2>

            <% if (historial == null || historial.isEmpty()) { %>
                <p>Aún no has guardado ninguna rutina. ¡Genera una y guárdala para verla aquí!</p>
            <% } else { %>
                <% for (RutinaGuardada rg : historial) { %>
                    <div class="rutina-guardada">
                        <div class="rutina-header">
                            <h3>Rutina del <%= sdf.format(rg.getFechaGuardada()) %></h3>
                            <span class="estado <%= rg.getEstado().equalsIgnoreCase("Guardada") ? "guardada" : "completada" %>">
                                <%= rg.getEstado() %>
                            </span>
                        </div>

                        <% for (DiaRutina dia : rg.getRutina().getDias()) { %>
                            <h4><%= dia.getNombre() %></h4>
                            <% for (Ejercicio ej : dia.getEjercicios()) { %>
                                <div class="ejercicio-simple">
                                    <p><strong><%= ej.getNombre() %>:</strong> <%= ej.getSeriesYRepeticiones() %></p>
                                </div>
                            <% } %>
                        <% } %>

                        <% if (rg.getEstado().equalsIgnoreCase("Guardada")) { %>
                            <form action="marcarCompletada" method="post" style="text-align: right; margin-top: 15px;">
                                <input type="hidden" name="rutinaId" value="<%= rg.getId() %>">
                                <button type="submit" class="btn">Marcar como Completada</button>
                            </form>
                        <% } %>
                    </div>
                <% } %>
            <% } %>

            <a href="dashboard.jsp" class="btn btn-secondary" style="margin-top: 20px;">Volver al Panel</a>
        </div>
    </main>
</body>
</html>
