// File: src/main/java/model/Card.java
package main.java.model;

/**
 * Representa una carta del juego UNO.
 */
public class Carta {
    // Enumeración para los colores de las cartas
    public enum Color {
        ROJO, AMARILLO, VERDE, AZUL, COMODIN
    }

    // Enumeración para los tipos de cartas
    public enum Tipo {
        NUMERICO, SALTA, REVERSA, ROBA_DOS, COMODIN, COMODIN_MAS4
    }

    private final Color color;
    private final Tipo Tipo;
    private final int number;

    /**
     * Constructor para crear una nueva carta.
     * @param color El color de la carta.
     * @param Tipo El tipo de la carta.
     * @param numero El número de la carta (para cartas numéricas).
     */
    public Carta(Color color, Tipo Tipo, int numero) {
        this.color = color;
        this.Tipo = Tipo;
        this.number = numero;
    }

    // Getters
    public Color getColor() { return color; }
    public Tipo getTipo() { return Tipo; }
    public int getNumero() { return number; }

    @Override
    public String toString() {
        if (Tipo == Tipo.NUMERICO) {
            return color + " " + number;
        } else {
            return color + " " + Tipo;
        }
    }
}