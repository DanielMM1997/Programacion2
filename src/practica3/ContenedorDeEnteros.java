package practica3;

public class ContenedorDeEnteros {
    
    class NodoBinario {
        int clave;
        NodoBinario[] enlaces;

        public NodoBinario(int info){
            clave = info;
            enlaces = new NodoBinario[2];
        }
    }
    private int cardinal;
    private NodoBinario raiz;

    /** 
    * Constructor que permite crear contenedores vacios.
    */    
    public ContenedorDeEnteros(){
        raiz = null;
        cardinal = 0;
    }

    /**
    * Metodo que devuelve el numero de elementos del contenedor.
    */    
    public int cardinal(){
        return cardinal;
    }

    /** 
     * Metodo que devuelve verdadero si el valor pasado por parametro pertenece 
     * al contenedor y falso en caso contrario.
     */   
    public boolean buscar(int elemento) {
        NodoBinario aux = raiz;
        while (aux != null) {
            if (aux.clave < elemento) {
                aux = aux.enlaces[1];
            }else if (aux.clave > elemento) {
                aux = aux.enlaces[0];
            }else {
                return true;
            }
        }
        return false;
    }
    /*
    public boolean insertar(int elemento) {
        if (!buscar(elemento)) {
            raiz = insertar(raiz, elemento);
            return true;
        }
        return false;
    }
    
    private NodoBinario insertar(NodoBinario nodo, int elemento) {
        if (nodo == null) {
            cardinal++;
            nodo = new NodoBinario(elemento);
        } else {
            if (nodo.clave < elemento){
                nodo.enlaces[1] = insertar(nodo.enlaces[1], elemento);
            } else {
                nodo.enlaces[0] = insertar(nodo.enlaces[0], elemento);
            }
        }
        return nodo;
    }
*/
    public boolean extraer(int valor){
        if (buscar(valor)) {
            raiz = extraer(raiz, valor);
            return true;
        }
        return false;            
    }

    private NodoBinario extraer(NodoBinario nodo, int valor) {
        if (nodo != null) {
            if (valor == nodo.clave) {
                cardinal--;
                if ((nodo.enlaces[0] == null) || (nodo.enlaces[1] == null)) { //si tiene algun enlaces
                    
                    if (nodo.enlaces[0] == null) {
                        return nodo.enlaces[1];
                    } else {
                        return nodo.enlaces[0];
                    }
                } else {  //si tiene dos enlacess
                    nodo.enlaces[1] = extraerSucesor(nodo, nodo.enlaces[1]);
                }
            } else {
                if (nodo.clave < valor) {
                    nodo.enlaces[1] = extraer(nodo.enlaces[1], valor);
                } else {
                    nodo.enlaces[0] = extraer(nodo.enlaces[0], valor);
                }
            }
        }
        return nodo;
    }

    private NodoBinario extraerSucesor(NodoBinario nodoExtraer, NodoBinario nodo){
        if (nodo.enlaces[0] == null) {
            nodoExtraer.clave = nodo.clave;
            //cardinal--;
            nodo = nodo.enlaces[1];
        } else {
            nodo.enlaces[0] = extraerSucesor(nodoExtraer, nodo.enlaces[0]);
        }
        return nodo;
    }
 /*   
    public boolean extraer(int elemento) {
        if (buscar(elemento)) {
            NodoBinario aux = raiz;
            while(aux != null){
                if (elemento == aux.clave){
                    cardinal--;
                    if ((aux.enlaces[0] == null) && (aux.enlaces[1] == null)) {
                        aux.clave = 20;
                        //aux = aux.enlaces[0];
                        return true;
                    } else if ((aux.enlaces[0] == null) || (aux.enlaces[1] != null)) {
                        aux = aux.enlaces[1];
                        return true;
                    } else if ((aux.enlaces[0] != null) || (aux.enlaces[1] == null)) {
                        aux = aux.enlaces[0];
                        return true;
                    } else {  //si tiene dos enlacess
                        aux.enlaces[1] = extraerSucesor(aux, aux.enlaces[1]);
                    }
                } else {
                    if (aux.clave < elemento) {
                        aux = aux.enlaces[1];
                    } else {
                        aux = aux.enlaces[0];
                    }
                }
            }
            
        }
        return false;
    }
    */
    public boolean insertar(int elemento){
        if (!buscar(elemento)) {
            NodoBinario aux = raiz;
            while(aux != null){
                if (aux.clave < elemento){
                    if (aux.enlaces[1] == null) {
                        aux.enlaces[1] = new NodoBinario(elemento);
                        cardinal++;
                        return true;
                    }
                    aux = aux.enlaces[1];
                } else {
                    if (aux.enlaces[0] == null) {
                        aux.enlaces[0] = new NodoBinario(elemento);
                        cardinal++;
                        return true;
                    }
                    aux = aux.enlaces[0];
                }        
            }
            raiz = new NodoBinario(elemento);
            cardinal++;
            return true;
        }
        return false;
    }
   
    /**
    * Metodo que vacia el contenedor.
    */    
    public void vaciar(){
        raiz = null;
        cardinal = 0;
    }


    /**
    * Metodo que devuelve un vector de enteros ordenados de menor a mayor 
    * con los elementos que se encuentren en el contenedor.
     * @return 
    */     
    public int[] elementos () {
    	int[] result = new int[cardinal];
    	inorden(raiz, result, 0);
    	return result;
    }

    private int inorden(NodoBinario nodo, int[] res, int pos){
    	if (nodo == null) return pos;
    	pos = inorden(nodo.enlaces[0], res, pos);
    	res[pos++] = nodo.clave;
    	return inorden(nodo.enlaces[1], res, pos);
    }
}