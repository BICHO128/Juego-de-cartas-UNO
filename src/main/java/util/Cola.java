// File: src/main/java/util/Queue.java
package main.java.util;

// El <T> indica que es una clase generica, es decir,
// puede trabajar con cualquier tipo de dato (Integer, String,etc)...
public class Cola<T> {
    
    //Trae una lista enlazada para la lista de los elementos de la cola...
    private ListaD_Enlazada<T> list = new ListaD_Enlazada<>();

    // Añade un elemento al final de la cola...
    public void Colar(T Elemento) {
        list.Añadir_al_ultimo(Elemento);
    }

    // Elimina y devuelve el elemento al frente de la cola...
    public T Descolar() {
        return list.Remover_primera();
    }

    // Devuelve el elemento al frente de la cola sin eliminarlo...
    public T Frente() {
        
        //isEmpty se utiliza para saber si esta vacio...
        if (isEmpty()) {
            throw new IllegalStateException("La cola está vacía");
        }
        return list.get(0);
    }

    // Verifica si la cola está vacía
    public boolean isEmpty() {
        return list.isEmpty();
    }

    // Devuelve el tamaño de la cola
    public int Tamaño() {
        return list.Tamaño();
    }
}