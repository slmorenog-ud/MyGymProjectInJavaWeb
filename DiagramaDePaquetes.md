```mermaid
graph TD
    subgraph "Arquitectura de Paquetes de MyGymJavaWeb"
        direction LR

        package "Vista (web)" {
            [JSPs, HTML, CSS]
        }

        package "Controlador (com.agym.controlador)" {
            [Servlets]
        }

        package "Lógica (com.agym.logic)" {
            [Generadores de Rutina]
        }

        package "Utilidades (com.agym.util)" {
            [JsonUtil]
        }

        package "Modelo (com.agym.modelo)" {
            [POJOs: Usuario, Rutina, etc.]
        }
    end

    %% --- Dependencias entre Paquetes ---
    %% El controlador recibe la petición, usa la lógica y los datos,
    %% y finalmente despacha a la vista.
    Controlador --> Lógica
    Controlador --> Utilidades
    Controlador --> Modelo
    Controlador --> Vista

    %% La lógica de negocio utiliza los objetos del modelo para
    %% construir las rutinas.
    Lógica --> Modelo

    %% Las utilidades leen y escriben los objetos del modelo
    %% desde y hacia los archivos JSON.
    Utilidades --> Modelo

```
