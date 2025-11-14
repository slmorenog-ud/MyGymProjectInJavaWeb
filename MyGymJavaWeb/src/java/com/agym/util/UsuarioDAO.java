package com.agym.util;

import com.agym.modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UsuarioDAO {

    private Connection conn;

    public UsuarioDAO(Connection conn) {
        this.conn = conn;
    }

    public Usuario getUsuarioPorEmail(String email) {
        if (conn == null) return null;

        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE email = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setPassword(rs.getString("password"));
                    usuario.setFechaNacimiento(rs.getString("fechaNacimiento"));
                    usuario.setGenero(rs.getString("genero"));
                    // Cargar otros campos si existen en la tabla
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    public void crearUsuario(Usuario usuario) {
        if (conn == null) return;

        String sql = "INSERT INTO usuarios (nombre, email, password, fechaNacimiento, genero) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizarUsuario(Usuario usuario) {
        if (conn == null) return;

        String sql = "UPDATE usuarios SET nombre = ?, email = ?, password = ?, fechaNacimiento = ?, genero = ?, altura = ?, peso = ?, experiencia = ?, diasDisponibles = ?, objetivo = ?, prioridadMuscular = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getPassword());
            stmt.setString(4, usuario.getFechaNacimiento());
            stmt.setString(5, usuario.getGenero());
            stmt.setDouble(6, usuario.getAltura());
            stmt.setDouble(7, usuario.getPeso());
            stmt.setString(8, usuario.getExperiencia());
            stmt.setInt(9, usuario.getDiasDisponibles());
            stmt.setString(10, usuario.getObjetivo());
            stmt.setString(11, usuario.getPrioridadMuscular());
            stmt.setInt(12, usuario.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
