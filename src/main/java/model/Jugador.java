// File: src/main/java/model/Player.java
package main.java.model;

// Importacion de la Clase Lista_doble_enlazada...
import main.java.util.ListaD_Enlazada;

/**
 * Representa un jugador en el juego UNO.
 */
public class Jugador {

    //Atributos requeridos por la clase Jugador...
    private String Nombre;
    private ListaD_Enlazada<Carta> Cabeza;
    private boolean Cantar_Uno;

    // Constructor para crear un nuevo jugador.
    public Jugador(String nombre) {
        this.Nombre = nombre;
        this.Cabeza = new ListaD_Enlazada<>();
        this.Cantar_Uno = false;
    }

    //Añade una carta a la mano del jugador.
    public void AgregarCarta(Carta carta) {
        Cabeza.Añadir_al_ultimo(carta);
        Cantar_Uno = false;
    }

    // Juega una carta de la mano del jugador.
    // Utiliza el índice de la carta a jugar.
    // retorna La carta jugada.
    public Carta Jugar_Carta(int indice) {
        if (indice < 0 || indice >= Cabeza.Tamaño()) {
            throw new IndexOutOfBoundsException("Índice de carta inválido");
        }
        Carta card = Cabeza.get(indice);
        Cabeza.Remover(indice);
        return card;
    }

    // Obtiene una carta de la mano del jugador sin removerla.
    // Utiliza El índice de la carta.
    //Retorna La carta en el índice especificado.
    public Carta Obtener_Carta(int index) {
        if (index < 0 || index >= Cabeza.Tamaño()) {
            throw new IndexOutOfBoundsException("Índice de carta inválido");
        }
        return Cabeza.get(index);
    }

    // Metodos Getters y setters para mostrar el nombre, 
    // tamaño_mano (cantidad de cartas en mano)
    // Cantar UNO.
    public String getnombre() {
        return Nombre;
    }

    public int getTamano_mano() {
        return Cabeza.Tamaño();
    }

    public ListaD_Enlazada<Carta> getHand() {
        return Cabeza;
    }

    public void callUno() {
        Cantar_Uno = true;
    }

    public boolean hasCalledUno() {
        return Cantar_Uno;
    }
}
