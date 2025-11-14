package com.agym.controlador;

import com.agym.modelo.Ejercicio;
import com.agym.util.DatabaseUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/migrate")
public class MigrationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String realPath = getServletContext().getRealPath("/WEB-INF/ejercicios.json");
        Gson gson = new Gson();
        Type tipoListaEjercicios = new TypeToken<List<Ejercicio>>(){}.getType();

        try (FileReader reader = new FileReader(realPath)) {
            List<Ejercicio> ejercicios = gson.fromJson(reader, tipoListaEjercicios);

            Connection conn = DatabaseUtil.getConnection();
            if (conn == null) {
                response.getWriter().println("Error: No se pudo conectar a la base de datos.");
                return;
            }

            String sql = "INSERT INTO ejercicios (nombre, descripcion, grupoMuscular, dificultad, imagenUrl) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (Ejercicio ejercicio : ejercicios) {
                    stmt.setString(1, ejercicio.getNombre());
                    stmt.setString(2, ejercicio.getDescripcion());
                    stmt.setString(3, ejercicio.getGrupoMuscular());
                    stmt.setString(4, ejercicio.getDificultad());
                    stmt.setString(5, ejercicio.getImagenUrl());
                    stmt.addBatch();
                }
                stmt.executeBatch();
                response.getWriter().println("¡Migración de ejercicios completada con éxito!");
            } catch (SQLException e) {
                e.printStackTrace();
                response.getWriter().println("Error durante la migración: " + e.getMessage());
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error al leer o migrar los ejercicios", e);
        }
    }
}
