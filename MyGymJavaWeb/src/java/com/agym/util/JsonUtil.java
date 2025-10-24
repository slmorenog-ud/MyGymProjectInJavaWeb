package com.agym.util;

import com.agym.modelo.Ejercicio;
import com.agym.modelo.Rutina;
import com.agym.modelo.RutinaGuardada;
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
    private static final String RUTINAS_GUARDADAS_FILE_PATH = "WEB-INF/rutinas_guardadas.json";

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

    // --- Métodos para Rutinas Guardadas ---

    public static List<RutinaGuardada> leerRutinasGuardadas(String realPath) throws IOException {
        List<RutinaGuardada> rutinasGuardadas = new ArrayList<>();
        String jsonContent = new String(Files.readAllBytes(Paths.get(realPath, RUTINAS_GUARDADAS_FILE_PATH)));

        if (!jsonContent.isEmpty()) {
            JSONArray jsonArray = new JSONArray(jsonContent);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                RutinaGuardada rg = new RutinaGuardada();
                rg.setId(jsonObject.getLong("id"));
                rg.setUsuarioId(jsonObject.getInt("usuarioId"));
                rg.setFechaGuardada(new java.util.Date(jsonObject.getLong("fechaGuardada")));
                rg.setEstado(jsonObject.getString("estado"));

                // Deserializar la rutina anidada
                JSONObject rutinaJson = jsonObject.getJSONObject("rutina");
                Rutina rutina = new Rutina();
                JSONArray diasJson = rutinaJson.getJSONArray("dias");
                for (int j = 0; j < diasJson.length(); j++) {
                    JSONObject diaJson = diasJson.getJSONObject(j);
                    Rutina.DiaRutina dia = new Rutina.DiaRutina(diaJson.getString("nombre"));
                    JSONArray ejerciciosJson = diaJson.getJSONArray("ejercicios");
                    for (int k = 0; k < ejerciciosJson.length(); k++) {
                        JSONObject ejercicioJson = ejerciciosJson.getJSONObject(k);
                        Ejercicio ej = new Ejercicio();
                        ej.setNombre(ejercicioJson.getString("nombre"));
                        ej.setDescripcion(ejercicioJson.getString("descripcion"));
                        ej.setSeriesYRepeticiones(ejercicioJson.getString("seriesYRepeticiones"));
                        ej.setImagenUrl(ejercicioJson.getString("imagenUrl"));
                        dia.agregarEjercicio(ej);
                    }
                    rutina.agregarDia(dia);
                }
                rg.setRutina(rutina);
                rutinasGuardadas.add(rg);
            }
        }
        return rutinasGuardadas;
    }

    public static void escribirRutinasGuardadas(List<RutinaGuardada> rutinasGuardadas, String realPath) throws IOException {
        JSONArray jsonArray = new JSONArray();
        for (RutinaGuardada rg : rutinasGuardadas) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", rg.getId());
            jsonObject.put("usuarioId", rg.getUsuarioId());
            jsonObject.put("fechaGuardada", rg.getFechaGuardada().getTime());
            jsonObject.put("estado", rg.getEstado());

            // Serializar la rutina anidada
            JSONObject rutinaJson = new JSONObject();
            JSONArray diasJson = new JSONArray();
            for (Rutina.DiaRutina dia : rg.getRutina().getDias()) {
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
            jsonObject.put("rutina", rutinaJson);

            jsonArray.put(jsonObject);
        }

        try (FileWriter file = new FileWriter(new File(realPath, RUTINAS_GUARDADAS_FILE_PATH))) {
            file.write(jsonArray.toString(4));
        }
    }
}
