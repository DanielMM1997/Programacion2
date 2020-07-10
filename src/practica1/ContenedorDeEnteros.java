package practica1;

/* 
*  La clase ContenedorDeEnteros representa una colección de enteros 
*  implementada como una lista encadenada en la que no se admiten 
*  elementos repetidos ni estan ordenados.
*  @author Equipo 01-42 01
*/
public class ContenedorDeEnteros {
    
    /**
     * Clase privada para representar un nodo
     */
    private class Nodo {
        int info;       // Informacion que contiene el nodo
        Nodo siguiente; // Referencia al primer nodo

        Nodo(int info, Nodo sig) {
            this.info = info;
            siguiente = sig;
        }
    }
    
    private Nodo primero;   //  Referencia al primer nodo
    private int cardinal;   //  Tamaño del contenedor

    //  Constructor de Contenedor de enteros
    public ContenedorDeEnteros() {
        cardinal = 0;
        primero = null;
    }

    // @return número de elementos
    public int cardinal() {
        return cardinal;
    }

    /* 
    *   Inserta elementos en el contenedor
    *   @param info elemento a insertar en el contenedor
    *   @return true si se ha podido insertar en el contendor
    *   @return false si no se ha podido insertar en el contendor
    */
    public boolean insertar(int info){
        if(!buscar(info)){
            primero = new Nodo(info, primero);
            cardinal++;
            return true;
        }
        return false;
    }
    
    /*
    *   Extrae elementos del contenedor
    *   @param info elemento a extraer del contenedor 
    *   @return true si se ha podido extraer del contendor
    *   @return false si no se ha podido extraer del contendor
    */
    public boolean extraer(int info) {
        Nodo actual = primero;
        Nodo anterior = null;

        while(actual != null) {
            if(actual.info == info) {
                if(anterior == null) {
                    primero = primero.siguiente;
                }else {
                    anterior.siguiente = actual.siguiente;
                }
                cardinal--;
                return true;
            }
            anterior = actual;
            actual = actual.siguiente;
        }
        return false;
    }
    
    /*
    *   Realiza una búsqueda de un elemento en el contenedor
    *   @param n elemento buscado en el contenedor
    *   @return true/false en el caso de que la búsqueda haya sido o no
    *   fructífera
    */
    public boolean buscar (int info) {
        Nodo actual = primero;
        while(actual != null) {
            if(actual.info == info) {
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }
    
    //  Vacía el contenedor
    public void vaciar() {
        cardinal = 0;
        primero = null;
    }
    
    //  @return vector con los elementos del contendor.
    public int[] elementos() {
        int[] elementos = new int[cardinal];
        Nodo actual = primero;
        for(int i = 0; i < cardinal; i++) {
            elementos[i] = actual.info;
            actual = actual.siguiente;
        }
        return elementos;
    }
}