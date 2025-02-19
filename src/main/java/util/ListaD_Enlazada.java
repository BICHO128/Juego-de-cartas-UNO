// File: src/main/java/util/DoublyLinkedList.java
package main.java.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

// La T siginifica que es una clase dinamica, la cual se adapta a cualquier tipo de dato.
// Implementar Iterable hace que tu clase pueda recorrerse fácilmente, 
// ya sea usando for-each o directamente con un iterador.
public class ListaD_Enlazada<T> implements Iterable<T> {

    // Este motodo remueve el indice especifico...
    public void Remover(int index) {
      
        if (index < 0 || index >= Tamaño) {
            throw new IndexOutOfBoundsException("Índice fuera de rango");
        }
        if (index == 0) {
            Remover_primera();

            return;
        }
        Node actual = Cabeza;

        for (int i = 0; i < index; i++) {
            actual = actual.siguiente;
        }
        if (actual.siguiente != null) {
            actual.siguiente.anterior = actual.anterior;
        } else {
            cola = actual.anterior;
        }
        actual.anterior.siguiente = actual.siguiente;

        Tamaño--;
    }

    // Nodo interno para la lista doblemente enlazada...
    private class Node {

        //Dato que guardara el Nodo...
        T data;

        // Este puntero guarda el valor de la ubicación del nodo anterior...
        Node anterior;

        // Este puntero guarda el valor de la ubicación del nodo siguiente...
        Node siguiente;

        Node(T data) {
            this.data = data;
        }
    }

    private Node Cabeza; // Primer nodo de la lista..
    private Node cola; // Último nodo de la lista..
    private int Tamaño;  // Tamaño de la lista..

    // Constructor: inicializa una lista vacía
    public ListaD_Enlazada() {
        // Las pocisiones dentro de la lista doblemente 
        // enlazadas de inicializan en null y en 0, (Esta vacia)
        Cabeza = null;
        cola = null;
        Tamaño = 0;
    }

    // Añade un elemento al final de la lista
    public void Añadir_al_ultimo(T element) {

        //Crea un nuevo nodo...
        Node nuevo_nodo = new Node(element);

        //Pregunta si esta vacio...
        if (isEmpty()) {

            //Si esta vacio su cola y su cabeza 
            //seran igual al nuevo nodo (Solo tendra un elemento)
            Cabeza = cola = nuevo_nodo;

        } else {
            //El nodo que estaba de ultimo 
            //(cola) su puntero siguiente apuntara al nodo nuevo...
            cola.siguiente = nuevo_nodo;

            // Y el nuevo nodo, su puntero anterior apuntara a cola...
            nuevo_nodo.anterior = cola;
            //Y el nodo cola (el ultimo) sera igual a nuevo_nodo...
            cola = nuevo_nodo;
        }
        //Tamaño en la lista doble_enlazada se aumentara  + 1 elemento...
        Tamaño++;
    }

    // Elimina y devuelve el primer elemento de la lista
    public T Remover_primera() {

        // La lista doble enlazada esta vacia lanza una exepcion...
        if (isEmpty()) {
            throw new NoSuchElementException("La lista está vacía");
        }
        //Trae el valor guardado dentro del nodo, ya sea integrar, string, etc...
        T data = Cabeza.data;

        //El primer nodo ahora sera el de la posiocion siguiente...
        Cabeza = Cabeza.siguiente;

        //Si la cabeza es igual a null... la cola sera igual a null
        //( se refiere a que se elimino el unico elemento guardado en la lista doble)..
        if (Cabeza == null) {
            cola = null;

            // SI no se cumple entonces el nodo cabeza sera 
            // el primero ya que su anterior sera null (ninguno)...
        } else {
            Cabeza.anterior = null;
        }

        //Elimina un elemento de la lista doble enlazada y retorna el dato borrado...
        Tamaño--;
        return data;
    }

    // Devuelve el elemento en la posición especificada...
    public T get(int Indice) {
        // si el indice es menor a 0 o el indice es 
        // mayor o igual al tamaño de la lista doble enlazada...
        if (Indice < 0 || Indice >= Tamaño) {
            throw new IndexOutOfBoundsException("Índice fuera de rango");
        }
        //Nodo actual sera el primer nodo...
        Node Actual = Cabeza;
        //Recorre toda la lista buscado el 
        //valor deseado guardado dentro de nodo hasta encontrarlo...
        for (int i = 0; i < Indice; i++) {
            Actual = Actual.siguiente;
        }
        //Retorna el valor del nodo encontrado...
        return Actual.data;
    }

    // Verifica si la lista está vacía...
    public boolean isEmpty() {
        return Tamaño == 0;
    }

    // Devuelve el tamaño de la lista...
    public int Tamaño() {
        return Tamaño;
    }

    // Implementación del iterador para recorrer la lista con un bucle FOREACH...
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node Actual = Cabeza;

            @Override

            //HasNext significa si (tine un siguiente) y retorna true o false...
            public boolean hasNext() {
                return Actual != null;
            }

            @Override
            public T next() {
                //Si no tine un siguiente entonces notifica por una exepcion...
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                //REcorre y devuelve el dato en el caul esta iterando...
                T data = Actual.data;
                Actual = Actual.siguiente;
                return data;
            }
        };
    }
}
