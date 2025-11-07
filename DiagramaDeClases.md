classDiagram
    direction TB

    subgraph Presentation_Layer
        class HttpServlet {
            <<Abstract>>
        }
        class RegistroServlet {
            +doPost()
        }
        class LoginServlet {
            +doPost()
        }
        class GenerarRutinaServlet {
            +doPost()
        }
        class GuardarRutinaServlet {
            +doPost()
        }
        class HistorialServlet {
            +doGet()
        }
        class MarcarCompletadaServlet {
            +doPost()
        }
        class LogoutServlet {
            +doPost()
        }

        HttpServlet <|-- RegistroServlet
        HttpServlet <|-- LoginServlet
        HttpServlet <|-- GenerarRutinaServlet
        HttpServlet <|-- GuardarRutinaServlet
        HttpServlet <|-- HistorialServlet
        HttpServlet <|-- MarcarCompletadaServlet
        HttpServlet <|-- LogoutServlet
    end

    subgraph Business_Logic_Layer
        class GeneradorRutinaFactory {
            <<Factory>>
            +getGenerador(int) GeneradorRutinaBase
        }
        class GeneradorRutinaBase {
            <<Abstract>>
            +generar(Usuario, List~Ejercicio~) Rutina
            #construirRutina(...)
        }
        class GeneradorRutina2Dias
        class GeneradorRutina3Dias
        class GeneradorRutina4Dias

        GeneradorRutinaBase <|-- GeneradorRutina2Dias
        GeneradorRutinaBase <|-- GeneradorRutina3Dias
        GeneradorRutinaBase <|-- GeneradorRutina4Dias
    end

    subgraph Data_Access_Utility
        class JsonUtil {
            <<Utility>>
            +leerUsuarios() List~Usuario~
            +escribirUsuarios(List~Usuario~)
            +leerEjercicios() List~Ejercicio~
            +leerRutinasGuardadas() List~RutinaGuardada~
            +escribirRutinasGuardadas(List~RutinaGuardada~)
        }
    end

    subgraph Model_Layer
        class Usuario {
            -int id
            -String nombre
            -String email
            -String password
            -int diasDisponibles
            -String objetivo
        }
        class Rutina {
            -List~DiaRutina~ dias
        }
        class DiaRutina {
            <<ValueObject>>
            -String nombre
            -List~Ejercicio~ ejercicios
        }
        class Ejercicio {
            -int id
            -String nombre
            -String grupoMuscular
            -String dificultad
        }
        class RutinaGuardada {
            -long id
            -int usuarioId
            -Date fechaGuardada
            -Rutina rutina
        }
    end

    %% --- Relationships ---

    %% Presentation -> Logic
    GenerarRutinaServlet --> GeneradorRutinaFactory

    %% Logic -> Logic
    GeneradorRutinaFactory --> GeneradorRutinaBase

    %% Presentation -> Data/Utility
    RegistroServlet --> JsonUtil
    LoginServlet --> JsonUtil
    GenerarRutinaServlet --> JsonUtil
    GuardarRutinaServlet --> JsonUtil
    HistorialServlet --> JsonUtil
    MarcarCompletadaServlet --> JsonUtil

    %% Presentation -> Model
    GenerarRutinaServlet --> Usuario
    GuardarRutinaServlet --> RutinaGuardada
    HistorialServlet --> RutinaGuardada
    MarcarCompletadaServlet --> RutinaGuardada

    %% Logic -> Model
    GeneradorRutinaBase --> Usuario
    GeneradorRutinaBase --> Ejercicio
    GeneradorRutinaBase --> Rutina

    %% Model -> Model (Composition & Aggregation)
    Rutina "1" *-- "1..*" DiaRutina
    DiaRutina "1" *-- "1..*" Ejercicio
    RutinaGuardada o-- "1" Rutina
    RutinaGuardada --> Usuario
