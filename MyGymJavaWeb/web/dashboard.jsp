<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.agym.modelo.Usuario" %>

<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect("login.html");
        return;
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mi Panel - MyGymJW</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
    <header>
        <h1><a href="index.html">MyGymJW</a></h1>
        <nav>
            <a href="logout">Cerrar Sesión</a>
        </nav>
    </header>
    <main>
        <div class="container">
            <h2>Hola, <%= usuario.getNombre() %>!</h2>
            <p>Completa o actualiza tus datos para generar una rutina personalizada o revisa tu historial.</p>

            <div class="actions-container" style="text-align: center; margin-bottom: 25px;">
                <a href="historial" class="btn btn-secondary">Mi Historial de Rutinas</a>
            </div>

            <form action="generarRutina" method="post">
                <label for="altura">Altura (cm):</label>
                <input type="number" id="altura" name="altura" value="<%= usuario.getAltura() > 0 ? usuario.getAltura() : "" %>" required>

                <label for="peso">Peso (kg):</label>
                <input type="number" step="0.1" id="peso" name="peso" value="<%= usuario.getPeso() > 0 ? usuario.getPeso() : "" %>" required>

                <label for="experiencia">Nivel de Experiencia:</label>
                <select id="experiencia" name="experiencia" required>
                    <option value="principiante" <%= "principiante".equals(usuario.getExperiencia()) ? "selected" : "" %>>Principiante</option>
                    <option value="intermedio" <%= "intermedio".equals(usuario.getExperiencia()) ? "selected" : "" %>>Intermedio</option>
                    <option value="avanzado" <%= "avanzado".equals(usuario.getExperiencia()) ? "selected" : "" %>>Avanzado</option>
                </select>

                <label for="dias">Días disponibles por semana:</label>
                <select id="dias" name="dias" required>
                    <option value="2" <%= usuario.getDiasDisponibles() == 2 ? "selected" : "" %>>2 días</option>
                    <option value="3" <%= usuario.getDiasDisponibles() == 3 ? "selected" : "" %>>3 días</option>
                    <option value="4" <%= usuario.getDiasDisponibles() == 4 ? "selected" : "" %>>4 días</option>
                </select>

                <label for="objetivo">Objetivo Principal:</label>
                <select id="objetivo" name="objetivo" required>
                    <option value="bajar_peso" <%= "bajar_peso".equals(usuario.getObjetivo()) ? "selected" : "" %>>Bajar de peso</option>
                    <option value="subir_peso" <%= "subir_peso".equals(usuario.getObjetivo()) ? "selected" : "" %>>Subir de peso (Hipertrofia)</option>
                    <option value="fortalecer" <%= "fortalecer".equals(usuario.getObjetivo()) ? "selected" : "" %>>Fortalecer (Fuerza)</option>
                </select>

                <label for="prioridadMuscular">¿Qué grupo muscular te gustaría priorizar? (Opcional)</label>
                <select id="prioridadMuscular" name="prioridadMuscular">
                    <option value="sin_preferencia" <%= "sin_preferencia".equals(usuario.getPrioridadMuscular()) ? "selected" : "" %>>Sin preferencia</option>
                    <option value="tren_superior" <%= "tren_superior".equals(usuario.getPrioridadMuscular()) ? "selected" : "" %>>Tren Superior</option>
                    <option value="tren_inferior" <%= "tren_inferior".equals(usuario.getPrioridadMuscular()) ? "selected" : "" %>>Tren Inferior</option>
                    <option value="brazos" <%= "brazos".equals(usuario.getPrioridadMuscular()) ? "selected" : "" %>>Brazos</option>
                </select>

                <button type="submit" class="btn">Generar mi Rutina</button>
            </form>
        </div>
    </main>
</body>
</html>
