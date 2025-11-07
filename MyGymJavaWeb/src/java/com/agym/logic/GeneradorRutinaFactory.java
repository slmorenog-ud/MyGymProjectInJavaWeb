package com.agym.logic;

/**
 * Factory para crear instancias de generadores de rutinas.
 * Esta clase desacopla al cliente (el servlet) de la implementación concreta
 * de los generadores de rutinas, facilitando la adición de nuevos tipos de
 * rutinas en el futuro.
 *
 * Principios de diseño aplicados:
 * - Patrón de diseño Factory: Centraliza la lógica de creación de objetos.
 * - Principio de Abierto/Cerrado (OCP): Para añadir un nuevo generador de rutinas
 *   (ej. de 5 días), solo es necesario modificar esta clase, sin alterar
 *   el código del cliente que la utiliza.
 * - Principio de Responsabilidad Única (SRP): La única responsabilidad de esta clase
 *   es crear el generador de rutinas adecuado.
 */
public class GeneradorRutinaFactory {

    /**
     * Obtiene la instancia del generador de rutinas apropiado según el número
     * de días disponibles del usuario.
     *
     * @param diasDisponibles El número de días que el usuario tiene para entrenar.
     * @return Una instancia de una subclase de {@link GeneradorRutinaBase}.
     * @throws IllegalArgumentException si el número de días no está soportado.
     */
    public static GeneradorRutinaBase getGenerador(int diasDisponibles) {
        switch (diasDisponibles) {
            case 2:
                return new GeneradorRutina2Dias();
            case 3:
                return new GeneradorRutina3Dias();
            case 4:
                return new GeneradorRutina4Dias();
            default:
                throw new IllegalArgumentException("Número de días no soportado: " + diasDisponibles);
        }
    }
}
