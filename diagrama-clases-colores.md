```mermaid
classDiagram
    direction TB

    %% --------------------------------------------------
    %% ESTILOS Y COLORES POR PAQUETE
    %% --------------------------------------------------
    classDef modelStyle fill:#D9EAD3,stroke:#5A7D4E,stroke-width:2px,color:#333
    classDef controllerStyle fill:#FCE5CD,stroke:#B45F06,stroke-width:2px,color:#333
    classDef utilStyle fill:#EAD1DC,stroke:#674379,stroke-width:2px,color:#333
    classDef abstractStyle fill:#CFE2F3,stroke:#29527A,stroke-width:2px,color:#333

    %% --------------------------------------------------
    %% PAQUETE DE MODELO (Clases de Datos - POJOs)
    %% --------------------------------------------------
    class Usuario {
        -int id
        -String nombre
        -String email
        -String password
        -String fechaNacimiento
        -String genero
        -double altura
        -double peso
        -String experiencia
        -String objetivo
        -int diasDisponibles
        -String prioridadMuscular
        +getters/setters()
    }

    class Ejercicio {
        -int id
        -String nombre
        -String descripcion
        -String grupoMuscular
        -String dificultad
        -String imagenUrl
        -String seriesYRepeticiones
        +getters/setters()
    }

    class Rutina {
        -List~DiaRutina~ dias
        +agregarDia(DiaRutina dia) void
        +getDias() List~DiaRutina~
    }

    class DiaRutina {
        <<ValueObject>>
        -String nombre
        -List~Ejercicio~ ejercicios
        +agregarEjercicio(Ejercicio ej) void
        +getNombre() String
        +getEjercicios() List~Ejercicio~
    }

    class RutinaGuardada {
        -long id
        -int usuarioId
        -Date fechaGuardada
        -String estado
        -Rutina rutina
        +getters/setters()
    }

    %% Aplicar estilo al paquete de Modelo
    class Usuario,Ejercicio,Rutina,DiaRutina,RutinaGuardada modelStyle

    %% --------------------------------------------------
    %% PAQUETE DE CONTROLADORES (Servlets)
    %% --------------------------------------------------
    class HttpServlet {
        <<Abstract>>
        #doGet(req, res) void
        #doPost(req, res) void
    }

    class RegistroServlet {
        +doPost(req, res) void
    }

    class LoginServlet {
        +doPost(req, res) void
    }

    class GenerarRutinaServlet {
        +doPost(req, res) void
        -generarRutinaLogica() Rutina
        -calcularEdad() int
        -agregarEjercicioPrioritario() void
    }

    class GuardarRutinaServlet {
        +doPost(req, res) void
    }

    class HistorialServlet {
        +doGet(req, res) void
    }

    class MarcarCompletadaServlet {
        +doPost(req, res) void
    }

    %% Aplicar estilo al paquete de Controladores
    class HttpServlet abstractStyle
    class RegistroServlet,LoginServlet,GenerarRutinaServlet,GuardarRutinaServlet,HistorialServlet,MarcarCompletadaServlet controllerStyle

    %% --------------------------------------------------
    %% PAQUETE DE UTILIDADES
    %% --------------------------------------------------
    class JsonUtil {
        <<Utility>>
        +leerUsuarios(String) List~Usuario~
        +escribirUsuarios(List~Usuario~, String) void
        +leerEjercicios(String) List~Ejercicio~
        +leerRutinasGuardadas(String) List~RutinaGuardada~
        +escribirRutinasGuardadas(List~RutinaGuardada~, String) void
    }

    %% Aplicar estilo al paquete de Utilidades
    class JsonUtil utilStyle

    %% --------------------------------------------------
    %% RELACIONES ESTRUCTURALES (más importantes)
    %% --------------------------------------------------

    %% Herencia - Relaciones fuertes
    HttpServlet <|-- RegistroServlet
    HttpServlet <|-- LoginServlet
    HttpServlet <|-- GenerarRutinaServlet
    HttpServlet <|-- GuardarRutinaServlet
    HttpServlet <|-- HistorialServlet
    HttpServlet <|-- MarcarCompletadaServlet

    %% Composición - Relaciones fuertes
    Rutina "1" *-- "*" DiaRutina : contiene
    DiaRutina "1" *-- "*" Ejercicio : contiene
    RutinaGuardada "1" *-- "1" Rutina : encapsula

    %% --------------------------------------------------
    %% DEPENDENCIAS PRINCIPALES SOLAMENTE
    %% --------------------------------------------------

    %% Controladores que dependen de JsonUtil (agrupado)
    RegistroServlet ..> JsonUtil : usa para
    LoginServlet ..> JsonUtil : persistencia
    GenerarRutinaServlet ..> JsonUtil : de datos

    %% Dependencias clave del modelo
    JsonUtil ..> Usuario : serializa
    JsonUtil ..> RutinaGuardada : deserializa

    %% Dependencias principales de lógica de negocio
    GenerarRutinaServlet ..> Usuario : lee datos
    GenerarRutinaServlet ..> Ejercicio : consulta
    GenerarRutinaServlet ..> Rutina : crea
```
