package com.agym.util;

import com.agym.modelo.RutinaGuardada;
import com.agym.modelo.Rutina;
import com.google.gson.Gson; // Necesitaremos GSON para serializar
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RutinaDAO {

    private final Gson gson = new Gson();
    private Connection conn;

    public RutinaDAO(Connection conn) {
        this.conn = conn;
    }

    public void guardarRutina(RutinaGuardada rutinaGuardada) {
        if (conn == null) return;

        String sql = "INSERT INTO rutinas_guardadas (usuario_id, rutina_json) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, rutinaGuardada.getUsuarioId());
            stmt.setString(2, gson.toJson(rutinaGuardada.getRutina()));
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    rutinaGuardada.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<RutinaGuardada> getRutinasPorUsuario(int usuarioId) {
        if (conn == null) return new ArrayList<>();

        List<RutinaGuardada> rutinas = new ArrayList<>();
        String sql = "SELECT * FROM rutinas_guardadas WHERE usuario_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RutinaGuardada rutinaGuardada = new RutinaGuardada();
                    rutinaGuardada.setId(rs.getLong("id"));
                    rutinaGuardada.setUsuarioId(rs.getInt("usuario_id"));

                    String rutinaJson = rs.getString("rutina_json");
                    Rutina rutina = gson.fromJson(rutinaJson, Rutina.class);
                    rutinaGuardada.setRutina(rutina);

                    // Aquí podrías cargar también la fecha y el estado 'completada'

                    rutinas.add(rutinaGuardada);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rutinas;
    }

    public void marcarComoCompletada(long rutinaId) {
        if (conn == null) return;

        String sql = "UPDATE rutinas_guardadas SET completada = TRUE WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, rutinaId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
