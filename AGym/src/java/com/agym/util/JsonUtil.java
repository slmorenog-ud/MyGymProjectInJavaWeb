package com.agym.util;

import com.agym.modelo.Usuario;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtil {

    private static final String USUARIOS_FILE_PATH = "WEB-INF/usuarios.json";
    private static final String EJERCICIOS_FILE_PATH = "WEB-INF/ejercicios.json";

    // --- Métodos para Usuarios ---

    public static List<Usuario> leerUsuarios(String realPath) throws IOException {
        List<Usuario> usuarios = new ArrayList<>();
        String jsonContent = new String(Files.readAllBytes(Paths.get(realPath, USUARIOS_FILE_PATH)));

        if (!jsonContent.isEmpty()) {
            JSONArray jsonArray = new JSONArray(jsonContent);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Usuario usuario = new Usuario();
                usuario.setId(jsonObject.optInt("id"));
                usuario.setNombre(jsonObject.optString("nombre"));
                usuario.setEmail(jsonObject.optString("email"));
                usuario.setPassword(jsonObject.optString("password"));
                usuario.setFechaNacimiento(jsonObject.optString("fechaNacimiento"));
                usuario.setGenero(jsonObject.optString("genero"));
                usuarios.add(usuario);
            }
        }
        return usuarios;
    }

    public static void escribirUsuarios(List<Usuario> usuarios, String realPath) throws IOException {
        JSONArray jsonArray = new JSONArray();
        for (Usuario usuario : usuarios) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", usuario.getId());
            jsonObject.put("nombre", usuario.getNombre());
            jsonObject.put("email", usuario.getEmail());
            jsonObject.put("password", usuario.getPassword());
            jsonObject.put("fechaNacimiento", usuario.getFechaNacimiento());
            jsonObject.put("genero", usuario.getGenero());
            jsonArray.put(jsonObject);
        }

        try (FileWriter file = new FileWriter(new File(realPath, USUARIOS_FILE_PATH))) {
            file.write(jsonArray.toString(4)); // El 4 es para indentar el JSON y hacerlo legible
        }
    }

    // --- Métodos para Ejercicios ---

    public static List<com.agym.modelo.Ejercicio> leerEjercicios(String realPath) throws IOException {
        List<com.agym.modelo.Ejercicio> ejercicios = new ArrayList<>();
        String jsonContent = new String(Files.readAllBytes(Paths.get(realPath, EJERCICIOS_FILE_PATH)));

        if (!jsonContent.isEmpty()) {
            JSONArray jsonArray = new JSONArray(jsonContent);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                com.agym.modelo.Ejercicio ejercicio = new com.agym.modelo.Ejercicio();
                ejercicio.setId(jsonObject.optInt("id"));
                ejercicio.setNombre(jsonObject.optString("nombre"));
                ejercicio.setDescripcion(jsonObject.optString("descripcion"));
                ejercicio.setGrupoMuscular(jsonObject.optString("grupoMuscular"));
                ejercicio.setDificultad(jsonObject.optString("dificultad"));
                ejercicio.setImagenUrl(jsonObject.optString("imagenUrl"));
                ejercicios.add(ejercicio);
            }
        }
        return ejercicios;
    }
}
