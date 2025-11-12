package com.agym.util;

import com.agym.modelo.Ejercicio;
import com.agym.modelo.Rutina;
import com.agym.modelo.RutinaGuardada;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class RutinaDAO {

    public void guardarRutina(RutinaGuardada rutinaGuardada) throws SQLException {
        String sql = "INSERT INTO rutinas_guardadas (usuario_id, fecha_guardada, estado, rutina_json) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, rutinaGuardada.getUsuarioId());
            stmt.setDate(2, new java.sql.Date(rutinaGuardada.getFechaGuardada().getTime()));
            stmt.setString(3, rutinaGuardada.getEstado());
            stmt.setString(4, serializarRutinaAJson(rutinaGuardada.getRutina()));
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    rutinaGuardada.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    public List<RutinaGuardada> obtenerRutinasPorUsuario(int usuarioId) throws SQLException {
        List<RutinaGuardada> rutinas = new ArrayList<>();
        String sql = "SELECT * FROM rutinas_guardadas WHERE usuario_id = ? ORDER BY fecha_guardada DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rutinas.add(mapRowToRutinaGuardada(rs));
                }
            }
        }
        return rutinas;
    }

    public void actualizarEstadoRutina(long id, String estado) throws SQLException {
        String sql = "UPDATE rutinas_guardadas SET estado = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, estado);
            stmt.setLong(2, id);
            stmt.executeUpdate();
        }
    }

    private RutinaGuardada mapRowToRutinaGuardada(ResultSet rs) throws SQLException {
        RutinaGuardada rg = new RutinaGuardada();
        rg.setId(rs.getLong("id"));
        rg.setUsuarioId(rs.getInt("usuario_id"));
        rg.setFechaGuardada(new Date(rs.getDate("fecha_guardada").getTime()));
        rg.setEstado(rs.getString("estado"));
        rg.setRutina(deserializarJsonARutina(rs.getString("rutina_json")));
        return rg;
    }

    private String serializarRutinaAJson(Rutina rutina) {
        JSONObject rutinaJson = new JSONObject();
        JSONArray diasJson = new JSONArray();
        for (Rutina.DiaRutina dia : rutina.getDias()) {
            JSONObject diaJson = new JSONObject();
            diaJson.put("nombre", dia.getNombre());
            JSONArray ejerciciosJson = new JSONArray();
            for (Ejercicio ej : dia.getEjercicios()) {
                JSONObject ejercicioJson = new JSONObject();
                ejercicioJson.put("nombre", ej.getNombre());
                ejercicioJson.put("descripcion", ej.getDescripcion());
                ejercicioJson.put("seriesYRepeticiones", ej.getSeriesYRepeticiones());
                ejercicioJson.put("imagenUrl", ej.getImagenUrl());
                ejerciciosJson.put(ejercicioJson);
            }
            diaJson.put("ejercicios", ejerciciosJson);
            diasJson.put(diaJson);
        }
        rutinaJson.put("dias", diasJson);
        return rutinaJson.toString(4);
    }

    private Rutina deserializarJsonARutina(String json) {
        Rutina rutina = new Rutina();
        JSONObject rutinaJson = new JSONObject(json);
        JSONArray diasJson = rutinaJson.getJSONArray("dias");
        for (int i = 0; i < diasJson.length(); i++) {
            JSONObject diaJson = diasJson.getJSONObject(i);
            Rutina.DiaRutina dia = new Rutina.DiaRutina(diaJson.getString("nombre"));
            JSONArray ejerciciosJson = diaJson.getJSONArray("ejercicios");
            for (int j = 0; j < ejerciciosJson.length(); j++) {
                JSONObject ejercicioJson = ejerciciosJson.getJSONObject(j);
                Ejercicio ej = new Ejercicio();
                ej.setNombre(ejercicioJson.getString("nombre"));
                ej.setDescripcion(ejercicioJson.getString("descripcion"));
                ej.setSeriesYRepeticiones(ejercicioJson.getString("seriesYRepeticiones"));
                ej.setImagenUrl(ejercicioJson.getString("imagenUrl"));
                dia.agregarEjercicio(ej);
            }
            rutina.agregarDia(dia);
        }
        return rutina;
    }
}
