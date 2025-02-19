// File: src/main/java/util/Stack.java
package main.java.util;

public class Pila<T> {
    private ListaD_Enlazada<T> list = new ListaD_Enlazada<>();

    // Añade un elemento a la cima de la pila
    public void push(T element) {
        list.Añadir_al_ultimo(element);
    }

    // Elimina y devuelve el elemento en la cima de la pila
    public T Eliminar() {
        return list.Remover_primera();
    }

    // Devuelve el elemento en la cima de la pila sin eliminarlo
    public T Frente() {
        if (isEmpty()) {
            throw new IllegalStateException("La pila está vacía");
        }
        return list.get(list.Tamaño() - 1);
    }

    // Verifica si la pila está vacía
    public boolean isEmpty() {
        return list.isEmpty();
    }

    // Devuelve el tamaño de la pila
    public int size() {
        return list.Tamaño();
    }
}

