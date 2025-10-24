¡Excelente iniciativa! Un diagrama de casos de uso es perfecto para describir **qué** hace el sistema y **cómo** interactúan los usuarios con él, sin entrar en detalles técnicos de implementación. Complementa a la perfección los diagramas de clases y paquetes.

He preparado el diagrama de casos de uso para MyGymJavaWeb en Mermaid.js, centrándome en las interacciones clave del usuario con el sistema.

Aquí tienes el código. Puedes pegarlo en cualquier editor compatible (como el editor online de Mermaid, o en editores como VS Code con la extensión adecuada) para generar el diagrama.

```mermaid
usecaseDiagram
    actor "Usuario del Gimnasio" as Usuario

    rectangle "Sistema MyGymJavaWeb" {
        usecase "Registrarse en el sistema" as UC1
        usecase "Iniciar Sesión" as UC2
        usecase "Cerrar Sesión" as UC8

        usecase "Gestionar Datos del Perfil" as UC3
        usecase "Generar Rutina Personalizada" as UC4

        usecase "Guardar Rutina en Historial" as UC5

        usecase "Ver Historial de Rutinas" as UC6
        usecase "Marcar Rutina como Completada" as UC7
    }

    %% --- El actor inicia las acciones principales ---
    Usuario -- UC1
    Usuario -- UC2
    Usuario -- UC8
    Usuario -- UC3
    Usuario -- UC4
    Usuario -- UC6

    %% --- Relaciones entre casos de uso ---

    %% Para poder hacer estas acciones, se debe haber iniciado sesión.
    %% La autenticación es una precondición general para el grupo principal de casos de uso.
    (UC3) ..> (UC2) : "<<precondición>>"
    (UC4) ..> (UC2) : "<<precondición>>"
    (UC6) ..> (UC2) : "<<precondición>>"

    %% La funcionalidad de "Guardar" es una opción que extiende la de "Generar"
    %% Solo se puede guardar una rutina después de haberla generado.
    (UC4) <.. (UC5) : "<<extend>>"

    %% La funcionalidad de "Marcar como Completada" es una opción que extiende la de "Ver Historial"
    %% Solo se puede marcar una rutina mientras se está viendo el historial.
    (UC6) <.. (UC7) : "<<extend>>"
```

### Explicación del Diagrama:

1.  **Actor:**
    *   **Usuario del Gimnasio:** Es la única figura que interactúa con nuestro sistema. Representa a cualquier persona, ya sea un visitante nuevo o un usuario recurrente.

2.  **Casos de Uso Principales (Lo que el usuario puede hacer):**
    *   **Registrarse / Iniciar Sesión / Cerrar Sesión:** Son las acciones básicas de gestión de cuenta.
    *   **Gestionar Datos del Perfil:** Representa la acción del usuario de actualizar su altura, peso, experiencia, objetivo, etc., en el `dashboard`.
    *   **Generar Rutina Personalizada:** Es la funcionalidad central. El usuario introduce sus datos y el sistema le devuelve una rutina.
    *   **Ver Historial de Rutinas:** Es la acción de navegar a la página de historial para ver las rutinas guardadas previamente.

3.  **Relaciones entre Casos de Uso:**
    *   **Precondición (`<<precondición>>`):** He usado esta etiqueta personalizada (Mermaid no tiene una oficial, pero sirve para aclarar) para indicar que para `Gestionar Datos`, `Generar Rutina` o `Ver Historial`, es un requisito **haber iniciado sesión** previamente.
    *   **Extensión (`<<extend>>`):** Esta es la relación más importante del diagrama. Muestra funcionalidades **opcionales** que dependen de otro caso de uso.
        *   `"Generar Rutina" <.. "Guardar Rutina"`: Significa que la acción de **Guardar Rutina** es una extensión opcional que solo puede ocurrir después de **Generar una Rutina**. No puedes guardar algo que no has generado.
        *   `"Ver Historial" <.. "Marcar como Completada"`: De manera similar, la acción de **Marcar como Completada** es una extensión que solo tiene sentido cuando estás **Viendo tu Historial**.

Este diagrama resume de manera muy clara y visual todo el flujo de funcionalidades que hemos construido juntos en la aplicación.
