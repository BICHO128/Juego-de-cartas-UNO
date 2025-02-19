package main.java.view;

import main.java.controller.JuegoUno;
import main.java.model.Carta;
import main.java.model.Jugador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Juego_UNO_UI extends JFrame {

    private JuegoUno Juego;
    private JPanel Panel_principal;
    private JPanel Cabeza_jugador_panel;
    private JPanel Descarte_pila_panel;
    private JButton Boton_carta_mazo;
    private JButton Boton_Uno;
    private JLabel Etiqueta_del_jugador_actual;
    private JLabel Etiqueta_de_la_targeta_superior;
    private JLabel Etiqueta_color_actual;
    private JLabel Etiqueta_tiempo;
    private Map<String, ImageIcon> Imagenes_cartas;
    private Timer Tiempo_juego;
    int N_jugadores;
    String Nj;
    String[] nombresJugadores;

    public Juego_UNO_UI() {
        Nj = JOptionPane.showInputDialog("Digite la cantidad de Jugadores:  ");
        N_jugadores = Integer.parseInt(Nj);

        String[] nombresJugadores = new String[N_jugadores];
        for (int i = 0; i < N_jugadores; i++) {
            String nombre = JOptionPane.showInputDialog("Ingrese el nombre del jugador " + (i + 1) + ":");
            nombresJugadores[i] = nombre != null && !nombre.trim().isEmpty() ? nombre : "Jugador " + (i + 1);
        }
        Juego = new JuegoUno(N_jugadores, nombresJugadores);
        CargarImagenes();
        Inicializar_grafico();
        Empezar_tiempo_del_juego();
    }

    private void CargarImagenes() {
        Imagenes_cartas = new HashMap<>();
        String Ruta = "src/main/resources/images/";
        String[] Colores = {"ROJO", "AMARILLO", "VERDE", "AZUL"};

        for (String color : Colores) {
            for (int i = 0; i <= 9; i++) {
                String key = color + "_" + i;
                String path = Ruta + key + ".png";
                loadAndResizeImage(key, path);
            }

            String[] Cartas_especiales = {"MAS2", "SALTA", "REVERSA"};
            for (String special : Cartas_especiales) {
                String llave = color + "_" + special;
                String path = Ruta + llave + ".png";
                loadAndResizeImage(llave, path);
            }
        }

        loadAndResizeImage("COMODIN", Ruta + "COMODIN.png");
        loadAndResizeImage("COMODIN_MAS4", Ruta + "COMODIN_MAS4.png");
    }

    private void loadAndResizeImage(String key, String path) {
        try {
            ImageIcon icon = new ImageIcon(path);
            Image image = icon.getImage().getScaledInstance(60, 100, Image.SCALE_SMOOTH);
            Imagenes_cartas.put(key, new ImageIcon(image));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar la imagen: " + path);
        }
    }

    private void Inicializar_grafico() {
        setTitle("Juego UNO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        Panel_principal = new JPanel(new BorderLayout());

        Cabeza_jugador_panel = new JPanel(new GridLayout(0, 1, 5, 5));
        JScrollPane scrollPaneJugador = new JScrollPane(Cabeza_jugador_panel);
        scrollPaneJugador.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPaneJugador.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneJugador.setPreferredSize(new Dimension(750, 150));

        Descarte_pila_panel = new JPanel(new FlowLayout());
        Descarte_pila_panel.setPreferredSize(new Dimension(400, 200));

        Boton_carta_mazo = new JButton("Robar Carta");
        Boton_Uno = new JButton("¡UNO!");
        Etiqueta_del_jugador_actual = new JLabel();
        Etiqueta_del_jugador_actual.setFont(new Font("Arial", Font.BOLD, 18));
        Etiqueta_de_la_targeta_superior = new JLabel();
        Etiqueta_color_actual = new JLabel();
        Etiqueta_tiempo = new JLabel();

        JPanel infoPanel = new JPanel(new GridLayout(4, 1));
        infoPanel.add(Etiqueta_del_jugador_actual);
        infoPanel.add(Etiqueta_de_la_targeta_superior);
        infoPanel.add(Etiqueta_color_actual);
        infoPanel.add(Etiqueta_tiempo);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 30, 30));
        buttonPanel.add(Boton_carta_mazo);
        buttonPanel.add(Boton_Uno);

        Panel_principal.add(scrollPaneJugador, BorderLayout.SOUTH);
        Panel_principal.add(Descarte_pila_panel, BorderLayout.CENTER);
        Panel_principal.add(buttonPanel, BorderLayout.EAST);
        Panel_principal.add(infoPanel, BorderLayout.NORTH);

        Boton_carta_mazo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Juego.puede_robar_carta()) {
                    Carta cartaRobada = Juego.Tomar_carta();
                    if (Juego.es_jugada_valida(cartaRobada)) {
                        Object[] opciones = {"Jugar carta"}; // Una única opción personalizada
                        int opcion = JOptionPane.showOptionDialog(Juego_UNO_UI.this,
                                "Has robado una carta jugable. ¿Quieres jugarla?",
                                "Carta Robada",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.INFORMATION_MESSAGE,
                                null,
                                opciones,
                                opciones[0]);

                        if (opcion == 0) { // Si se selecciona "Jugar carta"
                            Juego.Turno(Juego.Mostrar_jugador_actual().getTamano_mano() - 1);
                        } else {
                            pasarTurno();
                        }
                    } else {
                        JOptionPane.showMessageDialog(Juego_UNO_UI.this,
                                "La carta robada no es jugable. Pierdes el turno.");
                        pasarTurno();
                    }
                    actualizar_interfaz();
                } else {
                    JOptionPane.showMessageDialog(Juego_UNO_UI.this,
                            "No puedes robar carta. Tienes una carta jugable en tu mano.");
                }
            }

        });

        Boton_Uno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Juego.Cantar_UNO(Juego.Mostrar_jugador_actual());
                JOptionPane.showMessageDialog(Juego_UNO_UI.this, Juego.Mostrar_jugador_actual().getnombre() + " ha dicho ¡UNO!");
            }
        });

        add(Panel_principal);

        actualizar_interfaz();
    }

    private void Empezar_tiempo_del_juego() {
        Tiempo_juego = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTimerLabel();
                if (Juego.Fin_juego()) {
                    Tiempo_juego.stop();
                    Mostrar_fin_juego();
                }
            }
        });
        Tiempo_juego.start();
    }

    private void updateTimerLabel() {
        long Tiempo_restante = Juego.Mostrar_tiempo();
        long minutos = Tiempo_restante / 60000;
        long segundos = (Tiempo_restante % 60000) / 1000;
        Etiqueta_tiempo.setText(String.format("\n Tiempo restante: %02d:%02d ", minutos, segundos));
    }

    private void actualizar_interfaz() {
        Cabeza_jugador_panel.removeAll();
        Jugador Jugador_actual = Juego.Mostrar_jugador_actual();
        JPanel filaPanelCartas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (int i = 0; i < Jugador_actual.getTamano_mano(); i++) {
            final int Carta_indice = i;
            JButton Carta_boton = createCardButton(Jugador_actual.Obtener_Carta(i));
            Carta_boton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Carta cartaJugada = Jugador_actual.Obtener_Carta(Carta_indice);
                        if (Juego.Turno(Carta_indice)) {
                            handleSpecialCards(cartaJugada);
                            if (Juego.Fin_juego()) {
                                Tiempo_juego.stop();
                                Mostrar_fin_juego();
                            } else {
                                actualizar_interfaz();
                            }
                        } else {
                            JOptionPane.showMessageDialog(Juego_UNO_UI.this, "\n Jugada inválida! ");
                        }
                    } catch (IndexOutOfBoundsException ex) {
                        JOptionPane.showMessageDialog(Juego_UNO_UI.this, " Error: Índice de carta inválido ");
                    }
                }
            });
            filaPanelCartas.add(Carta_boton);
        }
        Cabeza_jugador_panel.add(filaPanelCartas);

        Descarte_pila_panel.removeAll();
        Carta Carta_superior = Juego.Carta_superior();
        Descarte_pila_panel.add(createCardButton(Carta_superior));

        Etiqueta_del_jugador_actual.setText("\n Jugador actual: " + Jugador_actual.getnombre());
        Etiqueta_de_la_targeta_superior.setText("\n Carta superior: " + Carta_superior.toString());
        Etiqueta_color_actual.setText("\n Color actual: " + Juego.Mostrar_color_actual());

        revalidate();
        repaint();
    }

    private void handleSpecialCards(Carta playedCard) {
        if (playedCard.getTipo() == Carta.Tipo.COMODIN || playedCard.getTipo() == Carta.Tipo.COMODIN_MAS4) {
            String[] Colores = {"ROJO", "AZUL", "VERDE", "AMARILLO"};
            String seleccionar_color = (String) JOptionPane.showInputDialog(
                    this,
                    "Elige un Color:",
                    "Selección de Color",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    Colores,
                    Colores[0]
            );
            if (seleccionar_color != null) {
                Juego.setColor_actual(Carta.Color.valueOf(seleccionar_color));
            }
        }
    }

    private JButton createCardButton(Carta card) {
        JButton Boton_carta = new JButton();
        Boton_carta.setPreferredSize(new Dimension(60, 100));

        String llave_imagen = getImageKey(card);
        ImageIcon Imagen_carta = Imagenes_cartas.get(llave_imagen);

        if (Imagen_carta != null) {
            Boton_carta.setIcon(Imagen_carta);
            Boton_carta.setBorderPainted(false);
            Boton_carta.setContentAreaFilled(false);
            Boton_carta.setFocusPainted(false);
        } else {
            Boton_carta.setText(card.toString());
        }

        return Boton_carta;
    }

    private String getImageKey(Carta card) {
        if (card.getColor() == Carta.Color.COMODIN) {
            return card.getTipo() == Carta.Tipo.COMODIN ? "COMODIN" : "COMODIN_MAS4";
        }

        String color = card.getColor().toString();
        switch (card.getTipo()) {
            case NUMERICO:
                return color + "_" + card.getNumero();
            case REVERSA:
                return color + "_REVERSA";
            case SALTA:
                return color + "_SALTA";
            case ROBA_DOS:
                return color + "_MAS2";
            default:
                return "";
        }
    }

    private void Mostrar_fin_juego() {
        List<Jugador> jugadores = Juego.Mostrar_Jugadores();
        StringBuilder mensaje = new StringBuilder("¡Juego terminado!\n\nPuntuaciones finales:\n");

        for (Jugador jugador : jugadores) {
            int puntuacion = Juego.Cal_puntos_jugador(jugador);
            mensaje.append(jugador.getnombre()).append(": ").append(puntuacion).append(" puntos\n");
        }

        Jugador ganador = Juego.Mostrar_ganador();
        mensaje.append("\n Ganador: ").append(ganador.getnombre())
                .append("\n Puntuación del ganador: ").append(Juego.Cal_puntos_jugador(ganador));

        int option = JOptionPane.showOptionDialog(this, mensaje.toString(), "\n Fin del juego ",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, new String[]{" Jugar de nuevo ", " Salir "}, " Jugar de nuevo ");

        if (option == JOptionPane.YES_OPTION) {
            Reiniciar_juego();
        } else {
            System.exit(0);
        }
    }

    private void Reiniciar_juego() {
        // Pedir nuevamente la cantidad de jugadores
        String nj = JOptionPane.showInputDialog("Digite la cantidad de Jugadores: ");
        int nJugadores = Integer.parseInt(nj);

        // Pedir los nombres de los jugadores
        String[] nombresJugadores = new String[nJugadores];
        for (int i = 0; i < nJugadores; i++) {
            String nombre = JOptionPane.showInputDialog("Ingrese el nombre del jugador " + (i + 1) + ":");
            nombresJugadores[i] = nombre != null && !nombre.trim().isEmpty() ? nombre : "Jugador " + (i + 1);
        }

        // Reiniciar el juego con los nuevos datos
        Juego = new JuegoUno(nJugadores, nombresJugadores);
        actualizar_interfaz();
        Empezar_tiempo_del_juego();
    }

    private void pasarTurno() {
        Juego.Siguiente_jugador();
        actualizar_interfaz();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Juego_UNO_UI gui = new Juego_UNO_UI();
                gui.setVisible(true);
            }
        });
    }
}
