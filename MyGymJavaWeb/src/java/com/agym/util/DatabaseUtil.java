package com.agym.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String BD = "mygym_db";
    private static final String LOGIN = "root";
    private static final String PASSWORD = "admin"; // Cambia esto por tu contraseña
    private static final String URL = "jdbc:mysql://localhost/" + BD;
    private static String mensaje;

    private Connection conexion;

    public DatabaseUtil() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, LOGIN, PASSWORD);
            if (conexion != null) {
                System.out.println("Conexión a base de datos " + BD + " OK");
            }
        } catch (SQLException e) {
            mensaje = "Error de SQL al conectar: " + e.getMessage();
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            mensaje = "No se encontró el driver de la base de datos: " + e.getMessage();
            e.printStackTrace();
        }
    }

    public Connection getConexion() {
        return conexion;
    }

    public void desconectar() {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexión a base de datos " + BD + " cerrada.");
            } catch (SQLException e) {
                mensaje = "Error al cerrar la conexión: " + e.getMessage();
                e.printStackTrace();
            }
        }
    }

    public static String getMensaje() {
        return mensaje;
    }
}
