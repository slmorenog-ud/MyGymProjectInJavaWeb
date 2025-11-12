package com.agym.util;

import com.agym.modelo.Ejercicio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EjercicioDAO {

    public List<Ejercicio> obtenerTodosLosEjercicios() throws SQLException {
        List<Ejercicio> ejercicios = new ArrayList<>();
        String sql = "SELECT * FROM ejercicios";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ejercicios.add(mapRowToEjercicio(rs));
            }
        }
        return ejercicios;
    }

    private Ejercicio mapRowToEjercicio(ResultSet rs) throws SQLException {
        Ejercicio ejercicio = new Ejercicio();
        ejercicio.setId(rs.getInt("id"));
        ejercicio.setNombre(rs.getString("nombre"));
        ejercicio.setDescripcion(rs.getString("descripcion"));
        ejercicio.setGrupoMuscular(rs.getString("grupo_muscular"));
        ejercicio.setDificultad(rs.getString("dificultad"));
        ejercicio.setImagenUrl(rs.getString("imagen_url"));
        return ejercicio;
    }
}
