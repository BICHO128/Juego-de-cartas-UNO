// File: src/main/java/controller/JuegoUno.java
package main.java.controller;

//Importaciones nesesarias...
import main.java.model.Carta;
import main.java.model.Jugador;
import main.java.util.Pila;
import main.java.util.Cola;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;

public class JuegoUno {

    // Declaración de variables las cuales se van a utilizar, 
    // Pila de Cartas (Mazo y descarte)
    // Cola de jugadores
    // Carta el color actual
    // variables de tipo boolean para definir 
    // el juego en reversa y el finde juego
    // Por ultimo la duracion de la partida...
    private Pila<Carta> Mazo;
    private Pila<Carta> descartar_pila;
    private Cola<Jugador> Jugadores;
    private Carta.Color Color_actual;
    private boolean Jugo_en_reversa;
    private boolean fin_del_juego;
    private long Inicio_tiempo;
    private static final long Duracion_juego = 7 * 60 * 1000; // 7 minutos en milisegundos

    // El primer condicional nos da a conocer que el minimo de juegadores son 2 y el maximo son 10...
    public JuegoUno(int N_jugadores, String[] nombresJugadores) {
        if (N_jugadores < 2 || N_jugadores > 10) {
            throw new IllegalArgumentException("El número de jugadores debe estar entre 2 y 10");

        }
        if (nombresJugadores == null || nombresJugadores.length != N_jugadores) {
            throw new IllegalArgumentException("Se deben proporcionar exactamente " + N_jugadores + " nombres.");
        }
        // Inicializar el mano, los nuemor de jugadores,
        // el repartir las cartas y el inial el juego...
        Inicializar_mazo();
        Inicializar_jugadores(N_jugadores, nombresJugadores);
        Repartir_cartas();
        Iniciar_juego();
    }
    
    private void Inicializar_jugadores(int N_jugadores, String[] nombresJugadores) {
        Jugadores = new Cola<>();
        for (int i = 0; i < N_jugadores; i++) {
            String nombre = (nombresJugadores != null) ? nombresJugadores[i] : "Jugador " + (i + 1);
            Jugadores.Colar(new Jugador(nombre));
        }
    }

 

    // Inicializar el mazo, se refiera a que debemos crear 
    // las 108 cartas del juego UNO y guardarlas en un mazo temporal 
    // en cual lo usaremos para defini rle tipo de cata, el valor 
    // y el color de cada una...
    private void Inicializar_mazo() {
        List<Carta> Mazo_temporal = new ArrayList<>();

        for (Carta.Color color : new Carta.Color[]{Carta.Color.ROJO, Carta.Color.AMARILLO, Carta.Color.VERDE, Carta.Color.AZUL}) {
            Mazo_temporal.add(new Carta(color, Carta.Tipo.NUMERICO, 0));
            for (int i = 1; i <= 9; i++) {
                Mazo_temporal.add(new Carta(color, Carta.Tipo.NUMERICO, i));
                Mazo_temporal.add(new Carta(color, Carta.Tipo.NUMERICO, i));
            }

            for (int i = 0; i < 2; i++) {
                Mazo_temporal.add(new Carta(color, Carta.Tipo.SALTA, -1));
                Mazo_temporal.add(new Carta(color, Carta.Tipo.REVERSA, -1));
                Mazo_temporal.add(new Carta(color, Carta.Tipo.ROBA_DOS, -1));
            }
        }

        for (int i = 0; i < 4; i++) {
            Mazo_temporal.add(new Carta(Carta.Color.COMODIN, Carta.Tipo.COMODIN, -1));
            Mazo_temporal.add(new Carta(Carta.Color.COMODIN, Carta.Tipo.COMODIN_MAS4, -1));
        }

        Collections.shuffle(Mazo_temporal);

// Luego de relizar los procesoso de creacion de las cartas,
//las guardamos en un apila con un metod push
        Mazo = new Pila<>();
        for (Carta card : Mazo_temporal) {
            Mazo.push(card);
        }
    }

// Este metodo le permite al sistema repartir las cartas
// Seran 7 cartas por jugador, realiza un densecolar del jugador actual,
// le da su carta , elimina esa carta del mazo y vuelve a colar 
// al jugador para resivir su proxima carta....

    private void Repartir_cartas() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < Jugadores.Tamaño(); j++) {
                Jugador jugador = Jugadores.Descolar();
                jugador.AgregarCarta(Mazo.Eliminar());
                Jugadores.Colar(jugador);
            }
        }
    }

// Este metodo el desarte sera guardado en una lista doble enlazada
// Toma la primera carta del mazo y ala coloca en el descarte
// DO: La palabra clave do en Java se utiliza para crear un 
// bucle do-while12345. Este tipo de bucle ejecutará un bloque 
// de código al menos una vez antes de comprobar una condición para
// determinar si se repite el bucle.
    private void Iniciar_juego() {
        descartar_pila = new Pila<>();
        Carta Primera_carta;
        do {
            Primera_carta = Mazo.Eliminar();

            // Si primera carta sacada del mazo es de tipo numerico,
            // agregala al descarte y muestra el color...
            if (Primera_carta.getTipo() == Carta.Tipo.NUMERICO) {
                descartar_pila.push(Primera_carta);
                Color_actual = Primera_carta.getColor();
                break;

                // Si la primera carta no es numerica no esnumerica, 
                // la carta volvera al mazo y se intentara con la siguiente carta...
            } else {
                Mazo.push(Primera_carta);
            }
        } while (true);

        // EL juego arraca normal, el fin del juego esta desactivado,
        // el tiempo comienza...
        Jugo_en_reversa = false;
        fin_del_juego = false;
        Inicio_tiempo = System.currentTimeMillis();
    }

    // Este metodo utiliza un entero para manejar el indice de la carta...
    public boolean Turno(int indice_carta) {

        // El jugador actual para al frente de la cola (adelante)...
        Jugador Jugador_actual = Jugadores.Frente();
        

        // Si el indice de la carta es menor a (0) 
        // o el indice es mayor igual al tamaño de las cartas en su mano
        // indica que la jugada es invalida ya que intenta jugar con una cata que no existe en su mano...
        if (indice_carta < 0 || indice_carta >= Jugador_actual.getTamano_mano()) {
            return false;
        }

        // Crea la carta_jugador la cual pertenece el
        // jugador actual y obtiene el indice de la carta actual...
        Carta Carta_jugador = Jugador_actual.Obtener_Carta(indice_carta);

        // Este condicional me permite ingresar al metodo 
        // validar jugada en el cual revisas todas las condiciones posibles
        // para permitir la jugada...
        if (es_jugada_valida(Carta_jugador)) {

            // Utiliza la carta jugada del jugador actual 
            // y toma el indice de esta carta y la agrega 
            // al descarte de la pila...
            Jugador_actual.Jugar_Carta(indice_carta);
            descartar_pila.push(Carta_jugador);

            // Si me queda una carta y no digo UNO,
            // el juegador actual se llevara dos cartas 
            // adicionales a su mano...
            if (Jugador_actual.getTamano_mano() == 1 && !Jugador_actual.hasCalledUno()) {
                Robar_cartas(Jugador_actual, 2);
                JOptionPane.showMessageDialog(null, Jugador_actual.getnombre() + " Olvidaste decir UNO y roba 2 cartas!");
            }
            // Si el tamaño de la mano de algun jugador llega a 0 cartas, 
            // entonces fin_del_juego pasara a ser TRUE y juego terminara...
            if (Jugador_actual.getTamano_mano() == 0) {
                fin_del_juego = true;
                return true;
            }
            // Ingresa al metodo aplicar efecto de la carta...
            Aplicar_efecto_carta(Carta_jugador);
            return true;
        }
        return false;
    }

// Este metodo permite validar jugada de la carta actual jugada...
    public boolean es_jugada_valida (Carta carta) {

        // La carta pasa encima del mazo...
        Carta Carta_superior = descartar_pila.Frente();

        // Las cartas comodín siempre se pueden jugar
        if (carta.getColor() == Carta.Color.COMODIN) {
            return true;
        }

        // Si la carta es del mismo color que la carta superior, VALIDO...
        if (carta.getColor() == Color_actual) {
            return true;
        }

        // Si la carta es del mismo número que la carta superior (solo para cartas numéricas), VALIDO...
        if (carta.getTipo() == Carta.Tipo.NUMERICO
                && Carta_superior.getTipo() == Carta.Tipo.NUMERICO
                && carta.getNumero() == Carta_superior.getNumero()) {
            return true;
        }

        // Si la carta es del mismo tipo que la carta superior (para cartas especiales), VALIDO...
        if (carta.getTipo() == Carta_superior.getTipo()
                && carta.getTipo() != Carta.Tipo.NUMERICO) {
            return true;
        }

        return false;
    }
    // Este medodo permite aplicar el efecto de la cara que se esta jugando...

    private void Aplicar_efecto_carta(Carta card) {
        switch (card.getTipo()) {
            case SALTA:
                Siguiente_jugador();
                break;
            case REVERSA:
                Jugo_en_reversa = !Jugo_en_reversa;

                break;
            case ROBA_DOS:
                Siguiente_jugador();
                Robar_cartas(Jugadores.Frente(), 2);
                break;
            case COMODIN:
                break;
            case COMODIN_MAS4:
                Siguiente_jugador();
                Robar_cartas(Jugadores.Frente(), 4);
                break;
            default:
                break;
        }
        // Si el color de la carta jugada no es un comodín,
        // actualiza Color_actual al color de la carta...
        if (card.getColor() != Carta.Color.COMODIN) {
            Color_actual = card.getColor();
        }
        // Se debe cumpliar alguno de los los casos para 
        // darle el turno al sigunete jugador...
        if (card.getTipo() != Carta.Tipo.REVERSA || Jugadores.Tamaño() > 2) {
            Siguiente_jugador();
        }
    }

    // Este metodo agrega la cantidad de cartas al jugador seleccionado...
    public void Robar_cartas(Jugador jugador, int cantidad) {
        for (int i = 0; i < cantidad; i++) {

            //Si el mazo esta vacio, entonces devera mezclar el mazo de nuevo...
            if (Mazo.isEmpty()) {
                Mezclar_mazo();
            }
            //Agregar las cartas al jugador y eliminar las cartas del mazo...
            jugador.AgregarCarta(Mazo.Eliminar());
        }
    }
    
    public boolean puede_robar_carta(){
        Jugador Jugador_actual = Jugadores.Frente();
        for (int i = 0; i < Jugador_actual.getTamano_mano(); i++){
            if (es_jugada_valida(Jugador_actual.Obtener_Carta(i))){
                return false;
            }
        }
        return true;
    }
    
    
    // Este metodo tomar_carta, utiliza el jugador actual y le hace robar una carta...
    public Carta Tomar_carta() {
        if (Mazo.isEmpty()){
            Mezclar_mazo();
        }
        Carta carta_robada = Mazo.Eliminar();
        Jugador Jugador_actual = Jugadores.Frente();
        Jugador_actual.AgregarCarta(carta_robada);
        return carta_robada;
    }

    // Este metodo permite mezclar el mazo...
    private void Mezclar_mazo() {

        // Este carta_superior permite eliminar las cartas del descarte...
        Carta Carta_superior = descartar_pila.Eliminar();

        // Mientras el descarte no este vacion manda las cartas del descarte al mazo...
        while (!descartar_pila.isEmpty()) {
            Mazo.push(descartar_pila.Eliminar());
        }

        // Es un metodo estatico de la clase Colections en Java, 
        // que se utiliza para reordenar los elementos de una lista 
        // en un orden aleatorio...
        Collections.shuffle((List<?>) Mazo);
        descartar_pila.push(Carta_superior);
    }

    //El metodo permite el siguiente jugador...
    public void Siguiente_jugador() {

        // Si jugador en reserva...
        if (Jugo_en_reversa) {
            // En reversa, sacamos el último jugador y lo colocamos al frente
            List<Jugador> jugadoresLista = new ArrayList<>();
            while (!Jugadores.isEmpty()) {
                jugadoresLista.add(Jugadores.Descolar());
            }
            // El último jugador pasa a ser el primero
            Jugadores.Colar(jugadoresLista.get(jugadoresLista.size() - 1));
            for (int i = 0; i < jugadoresLista.size() - 1; i++) {
                Jugadores.Colar(jugadoresLista.get(i));
            }

        } else {
            // Normal: simplemente se mueve el jugador del frente al final de la cola
            Jugador jugador_actual = Jugadores.Descolar();
            Jugadores.Colar(jugador_actual);
        }
    }
    // Este color actual permite traer el color actual...
    public void setColor_actual(Carta.Color color) {
        this.Color_actual = color;
    }
    // El final del juego, si el tiempo del juego trasncurrido
    
    public boolean Fin_juego() {
        if (!fin_del_juego) {
            long Tiempo_transcurrido = System.currentTimeMillis() - Inicio_tiempo;
            
            // Es mayor o igual al limite del tiempo, entonces se termina el juego...
            if (Tiempo_transcurrido >= Duracion_juego) {
                fin_del_juego = true;
                return true;
            }
        }
        return fin_del_juego;
    }
    // Mostrar el jugador actual...
    public Jugador Mostrar_jugador_actual() {
        return Jugadores.Frente();
    }
    // Mostrar la carta actual...
    public Carta Carta_superior() {
        return descartar_pila.Frente();
    }
    // Metodo del Mostrar_Color_actual...
    public Carta.Color Mostrar_color_actual() {
        return Color_actual;
    }
    // Mostrar los jugadorres de la lista doble 
    // enlazada a un ArrayList...
    public List<Jugador> Mostrar_Jugadores() {
        List<Jugador> Lista_Jugadores = new ArrayList<>();
        int tamanio = Jugadores.Tamaño();
        for (int i = 0; i < tamanio; i++) {
            Jugador jugador = Jugadores.Descolar();
            Lista_Jugadores.add(jugador);
            Jugadores.Colar(jugador);
        }
        return Lista_Jugadores;
    }
    // Calcular el Puntaje de la partida por cada jugador,
    // sumar el total de puntaje...
    public int Calcular_puntaje() {
        int Puntaje = 0;
        for (Jugador jugador : Mostrar_Jugadores()) {
            Puntaje += Cal_puntos_jugador(jugador);
        }
        return Puntaje;
    }
    
    // Cantar UNO...
    public void Cantar_UNO(Jugador jugador) {
        jugador.callUno();
    }
    // Mostrar  el  Tiempo de la partida...
    public long Mostrar_tiempo() {
        long Transcurso_del_tiempo = System.currentTimeMillis() - Inicio_tiempo;
        return Math.max(0, Duracion_juego - Transcurso_del_tiempo);
    }
    // Este metodo muestra es el jugador que gano la partida...
    public Jugador Mostrar_ganador() {
        List<Jugador> jugadores = Mostrar_Jugadores();
        Jugador ganador = jugadores.get(0);
        int menorPuntaje = Cal_puntos_jugador(ganador);

        for (Jugador jugador : jugadores) {
            int puntajeActual = Cal_puntos_jugador(jugador);
            if (puntajeActual < menorPuntaje) {
                menorPuntaje = puntajeActual;
                ganador = jugador;
            }
        }

        return ganador;
    }
    // Calcular los puntos por jugador dependiendo de 
    // la carta y el puntaje que brinda cada una...
    public int Cal_puntos_jugador(Jugador jugador) {
        int punto = 0;
        for (Carta card : jugador.getHand()) {
            switch (card.getTipo()) {
                case NUMERICO:
                    punto += card.getNumero();
                    break;
                case SALTA:
                case REVERSA:
                case ROBA_DOS:
                    punto += 20;
                    break;
                case COMODIN:
                case COMODIN_MAS4:
                    punto += 50;
                    break;
            }
        }
        return punto;
    }
}
