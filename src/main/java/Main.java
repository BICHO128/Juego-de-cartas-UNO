// File: src/main/java/Main.java
package main.java;


// Son las importaciones de la parte grafica del Juego UNO (implementado SWING)...
import main.java.view.Juego_UNO_UI;
import javax.swing.SwingUtilities;

// Clase principal del proyecto...
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            
            //Utiliza un contructor para Recerear la interfaz y darle visualiazcion con gui.setVisible(true)...
            @Override
            public void run() {
                Juego_UNO_UI gui = new Juego_UNO_UI();
                gui.setVisible(true);
            }
        });
    }
}