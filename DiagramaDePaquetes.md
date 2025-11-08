```mermaid
graph TD
    direction LR

    subgraph "Capa de Vista (Interfaz de Usuario)"
        Vista[
            <b>Contenido Web</b><br/>
            JSPs<br/>
            HTML<br/>
            CSS
        ]
    end

    subgraph "Capa de Controlador (com.agym.controlador)"
        Controlador[
            <b>Servlets Principales</b><br/>
            LoginServlet<br/>
            RegistroServlet<br/>
            GenerarRutinaServlet<br/>
            GuardarRutinaServlet<br/>
            HistorialServlet<br/>
            LogoutServlet
        ]
    end

    subgraph "Capa de Lógica de Negocio (com.agym.logic)"
        Logica[
            <b>Generación de Rutinas</b><br/>
            GeneradorRutinaFactory<br/>
            GeneradorRutinaBase<br/>
            GeneradorRutina2Dias<br/>
            GeneradorRutina3Dias<br/>
            GeneradorRutina4Dias
        ]
    end

    subgraph "Capa de Utilidades (com.agym.util)"
        Utilidades[
            <b>Persistencia</b><br/>
            JsonUtil
        ]
    end

    subgraph "Capa de Modelo (com.agym.modelo)"
        Modelo[
            <b>Entidades (POJOs)</b><br/>
            Usuario<br/>
            Rutina<br/>
            Ejercicio<br/>
            RutinaGuardada
        ]
    end

    %% --- Dependencias entre Capas ---
    Controlador -- "Usa" --> Logica
    Controlador -- "Usa" --> Utilidades
    Controlador -- "Usa y Manipula" --> Modelo
    Controlador -- "Redirige / Despacha a" --> Vista

    Logica -- "Usa y Crea" --> Modelo
    Utilidades -- "Lee / Escribe" --> Modelo
```
