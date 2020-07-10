package practica2;
/* 
*  La clase ContenedorDeEnteros representa un contenedor lineal
*  mediante un vector ordenado.
*  @author Equipo 01-42 01
*/
public class ContenedorDeEnteros {
    private int [] vector;  //Vector que almacenará los elementos
    private int cardinal;   //Tamaño del contenedor
    
    /*
     * Constructor principal de la clase ContenedorDeEnteros
     *  @param tamañoMax que indica el tamaño del vector
     */
    public ContenedorDeEnteros(int tamañoMax){
        vector = new int[tamañoMax];
        cardinal = 0;
    }
    
    /*
     *  @return int tamaño del contenedor
     */
    public int cardinal(){
        return cardinal;
    }
    
    /*
     *  Inserta un entero en el contenedor
     *  @param insert Elemento a insertar
     *  @return boolean Verdadero o falso en el caso de que haya podido o no,
     *  respectivamente, insertar el entero
     */
    public boolean insertar(int insert){
        if (!buscar(insert) && cardinal != vector.length) {
            int centro;
            int inferior = 0;
            int superior = cardinal-1;
            while(inferior <= superior){
                centro = (inferior + superior)/2;
                if (vector[centro] < insert){
                    inferior = centro+1;
                }else {
                    superior = centro-1;
                }
            }
            for(int i = cardinal-1; i >= inferior; i--){
                vector[i+1] = vector[i];
            }
            vector[inferior] = insert;
            cardinal++;
            return true;
        }
        return false;
    }

    /*
     * Extrae un entero del contenedor
     * @param extract El entero a extraer
     * @return boolean Verdadero o falso si ha podido o no, respectivamente
     *  extraer el entero.
     */
    public boolean extraer(int extract) {
        int pos = busqueda(extract);
        if(pos != -1) {
            for(int i = pos; i < cardinal-1; i++){
                vector[i] = vector[i+1];
            }
            cardinal--;
            return true;
        }
        return false;
    }

    /*
     * Busca un entero dentro del contenedor
     * @param element El entero a buscar
     * @return boolean Verdadero o falso en el caso de que haya podido o no,
     *  respectivamente, encontrar el entero.
     */
    public boolean buscar(int element){
        return busqueda(element) != -1;
    }

    /*
     * Método auxiliar de buscar
     * @param element Elemento que buscamos dentro del contenedor
     * @return int Devolvemos la posición del elemento en el caso 
     * de que lo hayamos encontrado o -1 en caso contrario
     *
     */
    private int busqueda(int element){
        int centro;
        int inferior = 0;
        int superior = cardinal-1;
        while(inferior <= superior){
            centro = (inferior + superior)/2;
            if(vector[centro] < element){
                inferior = centro+1;
            }else if(vector[centro] > element){
                superior = centro-1;
            }else{
                return centro;
            }
        }
        return -1;
    }
    
    /*
     * Vacía el contenedor
     */
    public void vaciar(){
        cardinal = 0;
    }

    /*
     * @return Vector de elementos pertenecientes al contenedor de enteros
     */
    public int [] elementos(){
        int [] res = new int [cardinal];
        System.arraycopy(vector, 0, res, 0, cardinal);
        return res;
    }    
}