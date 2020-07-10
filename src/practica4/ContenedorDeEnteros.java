package practica4;


import java.util.Stack;
import java.util.LinkedList;

/**
 * Class ContenedorDeEnteros based on an B tree and The Book "Fundamentos de estructuras de datos: Soluciones en ADA JAVA y C++"
 */
public class ContenedorDeEnteros {
	private FicheroAyuda file = new FicheroAyuda();;
	private int card;
	private int order;
	private int root = -1;
	private Stack<SackInfo> STK = new Stack<SackInfo>();;
	
	/*
	 * Main constructor of the "ContenedorDeEnteros" class
	 */
	public ContenedorDeEnteros() {
	}
	/*
	 * Searches an int on the container
	 * @param The int element
	 * @return boolean true or false whether the int was in the container or not
	 */
	public boolean buscar(int num) {
            STK.clear();
            int dirEntero = root;
            int pos;
            Entero Entero;

            while (dirEntero != FicheroAyuda.dirNula) {
                Entero = new Entero(dirEntero);
                pos = Entero.buscar(num);
                STK.add(new SackInfo(Entero, pos));
                if (Entero.esta(num)) {
                    return true;
                }
                dirEntero = Entero.Links[pos];
            }
            return false;
	}
	/*
   	 * @return An array with the elements of the container
	 */
	public int[] elementos() {
            int[] res = new int[this.card];
            int[] pos = {0};
            if (this.root != -1) {
                elementos(res, pos, new Entero(this.root));
            }
            return res;
	}
	/*
	 * @return The amount of elements stored in the container
	 */
	public int cardinal() {
            return card;
	}
	/*
	 * Empties the container
	 */
	public void vaciar() {
            this.file.cerrar();
            this.crear(file.nombre(), this.order);
	}
	/*
	 * Inserts an int in to the container
	 * @param The int to insert
	 * @return boolean true or false whether the int was inserted or not
	 */
	public boolean insertar(int valor) {
            if (buscar(valor)) {
                return false;
            }
            Entero Tmp;
            ++card;
            if (STK.empty()) {
                Tmp = new Entero();
                Tmp.numElem = 1;
                Tmp.Values[0] = valor;
                this.root = Tmp.direccion;
                Tmp.guarda();
                return true;
            }
            // the STK is not empty
            SackInfo info = (SackInfo) STK.pop();
            Tmp = info.Entero;
            int pos = info.pos;
            Tmp.numElem++;
            for (int i = Tmp.numElem - 1; i >= pos + 1; i--) {
                Tmp.Values[i] = Tmp.Values[i - 1];
                Tmp.Links[i + 1] = Tmp.Links[i];
            }
            Tmp.Values[pos] = valor;
            Tmp.Links[pos + 1] = FicheroAyuda.dirNula;
            if (Tmp.numElem < order) { // Guardamos el Entero
                Tmp.guarda();
                return true;
            }
            while (!STK.empty()) { // Arreglamos la sobrecarga.
                info = (SackInfo) STK.pop();
                Entero der = null, izq = null;
                Entero padre = info.Entero;
                pos = info.pos;
                if (pos > 0) { // Tiene hermano izquierdo.
                    izq = new Entero(padre.Links[pos - 1]);
                    if (izq.numElem < order - 1) { // Rotamos
                        rotaciónderizq(padre, pos - 1, izq, Tmp);
                        return true;
                    }
                }
                if (pos < padre.numElem) { // Tiene hermano derecho.
                    der = new Entero(padre.Links[pos + 1]);
                    if (der.numElem < order - 1) { // Rotamos
                        rotaciónizqder(padre, pos, Tmp, der);
                        return true;
                    }
                }
                // No hemos podido rotar -> particionamos el Entero
                if (pos == 0) {
                    particion_2_3(padre, pos, Tmp, der);
                } else {
                    particion_2_3(padre, pos - 1, izq, Tmp);
                }
                if (padre.numElem < order) { // Se soluciona 
                    padre.guarda();
                    return true;
                }
                Tmp = padre;
            }
        // Se parte la raíz.
        particion_1_2(Tmp);
        return true;
    }
    /*
     * Extracts an int from the container
     * @param The int extract
     * @return boolean true or false whether the int was extracted or not
     */
    public boolean extraer(int dato) {
            if (!buscar(dato)) {
                return false;
            }
            Entero Tmp;
            SackInfo info = (SackInfo) STK.pop();
            Tmp = info.Entero;
            int pos = info.pos;
            --card;
            if (Tmp.Links[0] != FicheroAyuda.dirNula) {

                // Extraccion desde un Entero no hoja.
                STK.add(new SackInfo(info.Entero, info.pos));

                // Hay que buscar el sucesor y cambiarlo.
                LinkedList<SackInfo> cola = new LinkedList<SackInfo>();
                int dir = Tmp.Links[pos];
                do { // Descendemos por las ramas izquierdas.
                    Tmp = new Entero(dir);
                    dir = Tmp.Links[0];
                    pos = dir == FicheroAyuda.dirNula ? 1 : 0;

                    // Guardamos el camino en una cola.
                    cola.addLast(new SackInfo(Tmp, pos));
                } while (dir != FicheroAyuda.dirNula);
                info = (SackInfo) STK.pop();

                // Se sustituye por el sucesor.
                info.Entero.Values[info.pos - 1] = Tmp.Values[0];

                // Se escribe por si no hay mas modificaciones.
                info.Entero.guarda();
                STK.add(info);
                while (!cola.isEmpty()) {
                    // Se pasa el camino de la cola a la STKBúsqueda.
                    Tmp = ((SackInfo) cola.getFirst()).Entero;
                    STK.add(cola.getFirst());
                    cola.removeFirst();
                }
                info = (SackInfo) STK.pop();
                Tmp = info.Entero;
                pos = info.pos;
            }

            // Extracción en un Entero hoja.
            for (int i = pos; i < Tmp.numElem; i++) {
                Tmp.Values[i - 1] = Tmp.Values[i];
                Tmp.Links[i] = Tmp.Links[i + 1];
            }
            Tmp.numElem--;
            while ((Tmp.numElem < (order + 1) / 2 - 1 && Tmp.direccion != root)) {
                Entero padre, der = null, izq = null;
                info = (SackInfo) STK.pop();
                padre = info.Entero; // Se saca el padre de la STKBúsqueda.
                pos = info.pos;

                if (pos < padre.numElem) { // Tiene hermano derecho.
                    der = new Entero(padre.Links[pos + 1]);
                    if (der.numElem > (order + 1) / 2 - 1) {
                        rotaciónderizq(padre, pos, Tmp, der);
                        return true;
                    }
                }
                if (pos > 0) { // Tiene hermano izquierdo.
                    izq = new Entero(padre.Links[pos - 1]);
                    if (izq.numElem > (order + 1) / 2 - 1) {
                        rotaciónizqder(padre, pos - 1, izq, Tmp);
                        return true;
                    }
                }
                // No se puede rotar, entonces se recombina.
                if (pos > 0 && pos < padre.numElem) {
                    recombinación_3_2(padre, pos, izq, Tmp, der);
                } else if (pos > 0) {
                    recombinación_2_1(padre, pos - 1, izq, Tmp);
                } else {
                    recombinación_2_1(padre, pos, Tmp, der);
                }
                if (card > order) {
                    Tmp = padre;
                } else {
                    root = Tmp.direccion;
                }
            }
            if (Tmp.numElem > 0) {
                    Tmp.guarda();
            } else {
                    root = FicheroAyuda.dirNula;
            }
            return true;
	}
	/*
	 * Method that creates a ContenedorDeEnteros 
	 * @param directory 
	 * @param orden - tree's order
	 */
	public void crear(String directory, int orden) {
            this.order = orden;
            if (orden > 4) {
                file.crear(directory, 8 * orden, 3);
                root = FicheroAyuda.dirNula;
                card = 0;
                file.adjunto(0, orden);
                file.adjunto(1, card);
                file.adjunto(2, root);
            } else {
                System.out.println("Performance of a B tree of order lower than 5 is unsure");
            }
	}
	/*
	 * Method that opens a ContenedorDeEnteros
	 * @param directory
	 */
	public void abrir(String directory) {
            file.abrir(directory);
            order = file.adjunto(0);
            card = file.adjunto(1);
            root = file.adjunto(2);
	}

	/*
	 * Method that closes a ContenedorDeEnteros
	 */
	public void cerrar() {
            file.adjunto(1, card);
            file.adjunto(2, root);
            file.cerrar();
	}
	/**
	 * Partición 1-2
	 * @param izq - Entero a partir
	 */
	private void particion_1_2(Entero izq) {
            Entero der = new Entero(); // derecho
            der.direccion = der.direccion;
            der.numElem = order / 2;
            der.Links[0] = izq.Links[order - order / 2];
            // [xxxx] [ ] => [xx ] [xx ]
            for (int i = 1; i <= der.numElem; i++) {
                der.Values[i - 1] = izq.Values[order - order / 2 - 1 + i];
                der.Links[i] = izq.Links[order - order / 2 - 1 + i + 1];
            }
            // Creamos el nuevo padre y lo enlazamos a los Enteros partidos
            Entero padre = new Entero();
            padre.Values[0] = izq.Values[order - order / 2 - 1];
            padre.Links[1] = der.direccion;
            padre.Links[0] = izq.direccion;
            izq.numElem = order - order / 2 - 1;
            padre.numElem = 1;
            root = padre.direccion;
            // Guardamos los Enteros
            padre.guarda();
            der.guarda();
            izq.guarda();
	}
	/**
	 * Partición 2-3
	 * @param padre - Padre en la partición
	 * @param posizq - Posición donde se parte
	 * @param izq - Hijo izquierdo en la partición
	 * @param der - Hijo derecho en la partición
	 */
	private void particion_2_3(Entero padre, int posizq, Entero izq, Entero der) {
            int clavesRepartir = izq.numElem + der.numElem - 1;
            Entero reg = new Entero();
            int ncizq = (clavesRepartir) / 3;
            int ncreg = (clavesRepartir + 1) / 3;
            int ncder = (clavesRepartir + 2) / 3;
            int antncder = der.numElem;
            int antncizq = izq.numElem;
            // Se inserta en el padre un nuevo par clave registro
            padre.numElem++;
            for (int i = padre.numElem - 1; i >= posizq + 1; i--) {
                padre.Values[i] = padre.Values[i - 1];
                padre.Links[i + 1] = padre.Links[i];
            }
            padre.Values[posizq] = izq.Values[ncizq];
            padre.Links[posizq + 1] = reg.direccion;
            // Pasamos datos de izq a reg: [aaa] [ ] => [aa ] [a ]
            reg.numElem = ncreg;
            reg.Links[0] = izq.Links[ncizq + 1];
            for (int i = ncizq + 2; i <= antncizq; i++) {
                reg.Values[i - ncizq - 2] = izq.Values[i - 1];
                reg.Links[i - ncizq - 1] = izq.Links[i];
            }
            izq.numElem = ncizq;

            // Pasamos el dato del padre a la posición correspondiente de reg
            reg.Values[antncizq - ncizq - 1] = padre.Values[posizq + 1];
            int posl = antncizq - ncizq;
            reg.Links[posl] = der.Links[0]; // [x ] [yyy] => [xy ] [ yy]
            for (int i = posl + 1; i <= ncreg; i++) {
                reg.Values[i - 1] = der.Values[i - posl - 1];
                reg.Links[i] = der.Links[i - posl];
            }

            int ncpas = antncder - ncder;

            // Pasamos al padre el valor correspondiente y compactamos der
            padre.Values[posizq + 1] = der.Values[ncpas - 1];
            der.Links[0] = der.Links[ncpas]; // [ yy] => [yy ]
            for (int i = ncpas + 1; i <= antncder; i++) {
                der.Values[i - ncpas - 1] = der.Values[i - 1];
                der.Links[i - ncpas] = der.Links[i];
            }
            der.numElem = ncder;

            // Guardamos los Enteros
            izq.guarda();
            reg.guarda();
            der.guarda();
	}
	/**
	 * Rotación derecha-izquierda
	 * @param padre - Padre en la rotacion
	 * @param posizq - Posición desde la que se rota
	 * @param izq - Hijo izquierdo en la rotacion
	 * @param der - Hijo derecho en la rotacion
	 */
	private void rotaciónderizq(Entero padre, int posizq, Entero izq, Entero der) {
            int clavesRepartir = izq.numElem + der.numElem;
            int ncder = (clavesRepartir) / 2;
            int ncizq = clavesRepartir - ncder;
            int ncpas = der.numElem - ncder;
            int antncizq = izq.numElem;
            // Pasamos la clave del padre y datos de der a izq
            izq.numElem = ncizq;
            izq.Values[antncizq] = padre.Values[posizq];
            izq.Links[antncizq + 1] = der.Links[0];
            for (int i = 1; i < ncpas; i++) { // [xx ] [yyyy] => [xxy ] [ yyy]
                izq.Values[antncizq + i] = der.Values[i - 1];
                izq.Links[antncizq + 1 + i] = der.Links[i];
            }
            // Pasamos clave al padre
            padre.Values[posizq] = der.Values[ncpas - 1];
            // Quitamos hueco en der
            der.Links[0] = der.Links[ncpas];
            for (int i = 1; i <= ncder; i++) { // [ yyy] => [yyy ]
                der.Values[i - 1] = der.Values[i + ncpas - 1];
                der.Links[i] = der.Links[i + ncpas];
            }
            der.numElem = ncder;
            padre.guarda();
            izq.guarda();
            der.guarda();
	}
	/**
	 * Rotación izquierda-derecha
	 * @param padre - Padre en la rotacion
	 * @param posizq - Posición desde la que se rota
	 * @param izq - Hijo izquierdo en la rotacion
	 * @param der - Hijo derecho en la rotacion
	 */
	private void rotaciónizqder(Entero padre, int posizq, Entero izq, Entero der) {
            int clavesRepartir = izq.numElem + der.numElem;
            int ncizq = (clavesRepartir) / 2;
            int ncder = clavesRepartir - ncizq;

            int ncpas = ncder - der.numElem;
            int antncder = der.numElem;
            // Hacemos hueco en Entero der: [yy ] => [ yy ]
            der.numElem = ncder;
            for (int i = antncder; i >= 1; i--) {
                der.Values[i + ncpas - 1] = der.Values[i - 1];
                der.Links[i + ncpas] = der.Links[i];
            }
            der.Links[ncpas] = der.Links[0];
            // Rellenar el Entero der: [xxxx] [ yy ] => [xxx ] [xyy ]
            der.Values[ncpas - 1] = padre.Values[posizq];
            for (int i = ncizq + 2; i <= izq.numElem; i++) {
                der.Values[i - (ncizq + 1) - 1] = izq.Values[i - 1];
                der.Links[i - (ncizq + 1)] = izq.Links[i];
            }
            der.Links[0] = izq.Links[ncizq + 1];
            // Modificar el Entero padre
            padre.Values[posizq] = izq.Values[ncizq];
            // Modificar el Entero izq
            izq.numElem = ncizq;

            // Se escribe en el fichero los tres Enteros
            padre.guarda();
            izq.guarda();
            der.guarda();
	}

	/**
	 * Recombinación 2-1 en la extracción
	 * @param padre - Padre en la recombinación
	 * @param posizq - Posición desde la que se rota
	 * @param izq - Hijo izquierdo en la recombinación
	 * @param der - Hijo derecho en la recombinación
	 */
	private void recombinación_2_1(Entero padre, int posizq, Entero izq, Entero der) {
            // Bajamos la clave discriminante del padre al final del izquierdo
            int antncizq = izq.numElem;
            izq.numElem = izq.numElem + 1 + der.numElem;
            izq.Values[antncizq] = padre.Values[posizq];

            // Pasamos el enlace cero de der a izq
            izq.Links[antncizq + 1] = der.Links[0];

            // Pasamos el resto de claves y enlaces
            // [xx ] [xx ] => [xxxx] [ ]
            for (int i = 1; i <= der.numElem; i++) {
                izq.Values[antncizq + i] = der.Values[i - 1];
                izq.Links[antncizq + 1 + i] = der.Links[i];
            }

            // Quitamos del padre la clave y el enlace der
            for (int i = posizq + 1; i < padre.numElem; i++) {
                padre.Values[i - 1] = padre.Values[i];
                padre.Links[i] = padre.Links[i + 1];
            }
            padre.numElem--;
            izq.guarda();
            file.liberarPágina(der.direccion);
	}
	/**
	 * Recombinación 3-1 en una extracción
	 * @param padre - Padre en la recombinacion
	 * @param pos - Posición desde la que se rota
	 * @param izq - Hijo izquierdo en la recombinación
	 * @param reg - Hijo implicado en la recombinación
	 * @param der - Hijo derecho en la recombinación
	 */
	private void recombinación_3_2(Entero padre, int pos, Entero izq, Entero reg, Entero der) {
            int aRepartir = izq.numElem + reg.numElem + der.numElem + 1;
            int ncder = aRepartir / 2;
            int ncizq = aRepartir - ncder;
            int antncizq = izq.numElem;
            int antncder = der.numElem;

            // Rellenamos el hermano izquierdo
            izq.numElem = ncizq;
            izq.Values[antncizq] = padre.Values[pos - 1];
            izq.Links[antncizq + 1] = reg.Links[0];

            // [xx ] [yy ] => [xxy ] [ y ]
            for (int i = antncizq + 2; i <= ncizq; i++) {
                izq.Values[i - 1] = reg.Values[i - antncizq - 2];
                izq.Links[i] = reg.Links[i - antncizq - 1];
            }

            // Desplazamiento del hermano derecho para hacer hueco
            der.numElem = ncder;
            int ncpas = ncder - antncder;
            for (int i = antncder; i >= 1; i--) { // [zz ] => [ zz ]
                der.Values[i + ncpas - 1] = der.Values[i - 1];
                der.Links[i + ncpas] = der.Links[i];
            }
            der.Links[ncpas] = der.Links[0];
            der.Values[ncpas - 1] = padre.Values[pos];

            // Rellenamos el hermano derecho
            // [ y ] [ zz ] => [ ] [yzz ]
            for (int i = ncpas - 1; i >= 1; i--) {
                der.Values[i - 1] = reg.Values[reg.numElem + i - ncpas];
                der.Links[i] = reg.Links[reg.numElem + i - ncpas + 1];
            }

            der.Links[0] = reg.Links[reg.numElem - ncpas + 1];

            // Modificar el Entero padre
            file.liberarPágina(reg.direccion);
            izq.guarda();
            der.guarda();
            for (int i = pos; i < padre.numElem; i++) {
                padre.Values[i - 1] = padre.Values[i];
                padre.Links[i] = padre.Links[i + 1];
            }
            padre.numElem--;
            padre.Values[pos - 1] = reg.Values[reg.numElem - ncpas];
	}
	/**
	 * Método privado que ayuda al público para poner los números recursivamente en el vector
	 * @param res - Elementos del contenedor
	 * @param pos - Posición de avance del vector
	 * @param Entero - Entero a inspeccionar
	 */
	private void elementos(int[] res, int[] pos, Entero Entero) {
            for (int i = 0; i < Entero.numElem; i++) {
                if (Entero.Links[i] != FicheroAyuda.dirNula) {
                        elementos(res, pos, new Entero(Entero.Links[i]));
                }
                res[pos[0]] = Entero.Values[i];
                pos[0]++;
            }
            if (Entero.Links[Entero.numElem] != FicheroAyuda.dirNula) {
                elementos(res, pos, new Entero(Entero.Links[Entero.numElem]));
            }
	}
	
	
	private class Entero {
            private int[] Values = new int[order];
            private int numElem = 0;
            private int[] Links = new int[order + 1];
            private int direccion;
            private Entero() {
                for (int i = 0; i < Links.length; i++) {
                        Links[i] = FicheroAyuda.dirNula;
                }
                this.direccion = file.tomarPágina();
                guarda();
            }
            /**
             * Constructor, lee una dirección del file y lo convierte en Entero
             * @param direccion int - Dirección del Entero a leer
             */
            private Entero(int direccion) {
                this.direccion = direccion;
                byte[] b = file.leer(this.direccion);
                this.numElem = Conversor.aInt(Conversor
                                .toma(b, 0, 4));
                for (int i = 0; i < order - 1; i++) {
                        Values[i] = Conversor.aInt(Conversor.toma(b, 4 + i * 4, 4));
                }
                for (int i = 0; i < order; i++) {
                        Links[i] = Conversor.aInt(Conversor.toma(b, order* 4 + i * 4, 4));
                }
            }

            /**
             * Pasar Entero a memoria secundaria
             */
            private void guarda() {
                byte[] b = new byte[8 * order];
                int pos = Conversor.añade(b, Conversor.aByte(numElem), 0);
                for (int i = 0; i < order - 1; i++) {
                        pos = Conversor.añade(b, Conversor.aByte(Values[i]), pos);
                }
                for (int i = 0; i < order; i++) {
                        pos = Conversor.añade(b, Conversor.aByte(Links[i]), pos);
                }
                file.escribir(b, direccion);
            }
            /**
             * Comprueba si un elemento está contenido en el Entero
             * @param num int - valor a buscar
             * @return boolean - Indica si ha encontrado el elemento
             */
            private boolean esta(int num) {
                int prim, ulti, med;
                prim = 1;
                ulti = numElem;
                while (prim <= ulti) {
                        med = (prim + ulti) / 2;
                        if (this.Values[med - 1] == num) {
                                return true;
                        }
                        if (num < this.Values[med - 1]) {
                                ulti = med - 1;
                        } else {
                                prim = med + 1;
                        }
                }
                return false;
            }
            /*
             * Searches an int on the container
             * @param the int to search
             * @return value's position or Node to search next
             */
            private int buscar(int num) {
                int pos, med;
                int prim = 1;
                int ulti = numElem;
                while (prim <= ulti) {
                    med = (prim + ulti) / 2;
                    if (this.Values[med - 1] == num) {
                        pos = med;
                        return pos;
                    }
                    if (num < this.Values[med - 1]) {
                        ulti = med - 1;
                    } else {
                        prim = med + 1;
                    }
                }
                pos = prim - 1;
                return pos;
            }
	}
        
	private class SackInfo {
            public Entero Entero;
            public int pos;
            /*
             * Constructor that links an Entero with a position 
             * @param The Entero to link
             * @param The position
             */
            public SackInfo(Entero Entero, int pos) {
                this.Entero = Entero;
                this.pos = pos;
            }
	}
}
