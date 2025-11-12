package com.agym.util;

import com.agym.modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UsuarioDAO {

    public Usuario crearUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, email, password, fecha_nacimiento, genero) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getPassword());
            stmt.setString(4, usuario.getFechaNacimiento());
            stmt.setString(5, usuario.getGenero());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    usuario.setId(generatedKeys.getInt(1));
                }
            }
        }
        return usuario;
    }

    public Usuario buscarUsuarioPorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToUsuario(rs);
                }
            }
        }
        return null;
    }

    public Usuario buscarUsuarioPorId(int id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToUsuario(rs);
                }
            }
        }
        return null;
    }

    private Usuario mapRowToUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setEmail(rs.getString("email"));
        usuario.setPassword(rs.getString("password"));
        usuario.setFechaNacimiento(rs.getString("fecha_nacimiento"));
        usuario.setGenero(rs.getString("genero"));
        // Asumiendo que estas columnas pueden ser nulas o no existir en todas las consultas
        usuario.setAltura(rs.getDouble("altura"));
        usuario.setPeso(rs.getDouble("peso"));
        usuario.setExperiencia(rs.getString("experiencia"));
        usuario.setObjetivo(rs.getString("objetivo"));
        usuario.setDiasDisponibles(rs.getInt("dias_disponibles"));
        usuario.setPrioridadMuscular(rs.getString("prioridad_muscular"));
        return usuario;
    }

    public void actualizarUsuario(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET altura = ?, peso = ?, experiencia = ?, objetivo = ?, dias_disponibles = ?, prioridad_muscular = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, usuario.getAltura());
            stmt.setDouble(2, usuario.getPeso());
            stmt.setString(3, usuario.getExperiencia());
            stmt.setString(4, usuario.getObjetivo());
            stmt.setInt(5, usuario.getDiasDisponibles());
            stmt.setString(6, usuario.getPrioridadMuscular());
            stmt.setInt(7, usuario.getId());
            stmt.executeUpdate();
        }
    }
}
