package com.agym.util;

import com.agym.modelo.Ejercicio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EjercicioDAO {

    private Connection conn;

    public EjercicioDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Ejercicio> getTodosLosEjercicios() {
        if (conn == null) return new ArrayList<>();

        List<Ejercicio> ejercicios = new ArrayList<>();
        String sql = "SELECT * FROM ejercicios";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Ejercicio ejercicio = new Ejercicio();
                ejercicio.setId(rs.getInt("id"));
                ejercicio.setNombre(rs.getString("nombre"));
                ejercicio.setDescripcion(rs.getString("descripcion"));
                ejercicio.setGrupoMuscular(rs.getString("grupoMuscular"));
                ejercicio.setDificultad(rs.getString("dificultad"));
                ejercicio.setImagenUrl(rs.getString("imagenUrl"));
                ejercicios.add(ejercicio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ejercicios;
    }
}
