package practica4;

import java.io.*;
import java.util.Arrays;

public class PruebaContenedor {

    public static void main(String[] args) {
        pruebasContenedor();
        pruebasRendimiento();
    }

    public static void pruebasContenedor() {
        ContenedorDeEnteros a = new ContenedorDeEnteros();
        a.crear("temp", 5);
        for(int i = 0; i < 10; i++) a.insertar(i);
        int[] ele = {1, 3, 6, 7, 0};
        for(int i = 0; i < ele.length; i++) {
            if (!a.buscar(ele[i])) System.out.println("Error no se encuentra el elemento (" + ele[i] + ")");
        }
        if (a.cardinal() != 10) System.out.println("Error el numero de elementos es incorrecto (10)");
        a.vaciar();
        if (a.cardinal() != 0) System.out.println("Error el numero de elementos es incorrecto (0)");
        int[] vectorAleatorio = new int[] { 3, 6, 4, 8, 7, 9 };
        for (int i = 0; i < vectorAleatorio.length; i++) {
            a.insertar(vectorAleatorio[i]);
        }
        if (a.cardinal() != 6) System.out.println("Error en el numero de elementos es incorrecto (6)");
        if (a.buscar(11)) System.out.println("Error se encuentra un elemento inexistente (11)");
        if (a.buscar(-1)) System.out.println("Error se encuentra un elemento inexistente (-1)");
       	if (!a.insertar(15)) System.out.println("Error no se he inserta el elemento (15)");
       	if (!a.insertar(10)) System.out.println("Error no se he inserta el elemento (10)");
        if (a.insertar(4)) System.out.println("Error se inserta un elemento repetido (4)");
        if (!a.extraer(15)) System.out.println("Error no se ha extraido el elemento (15)");
        if (a.buscar(15)) System.out.println("Error se ha encontrado el elemento (15)");
        if (!a.extraer(3)) System.out.println("Error no se ha extraido el elemento (3)");
        if (a.buscar(3)) System.out.println("Error se ha encontrado el elemento (3)");
        if (a.extraer(2)) System.out.println("Error se ha extraido el elemento inexistente (2)");
        if (a.buscar(2)) System.out.println("Error se ha encontrado el elemento (2)");
        if (a.cardinal() != 6) System.out.println("Error el numero de elementos es incorrecto (6)");
        a.vaciar();
        a.cerrar();
    }

    public static void pruebasRendimiento(){
        // Fichero de entrada datos
        RandomAccessFile datos = null;
        // Fichero de entrada no datos
        RandomAccessFile datosNot = null;
        // Fichero de escritura
        PrintStream salida = null;
        // Vectores donde se van almacenar los datos
        int[] data = new int[100000];
        int[] nodata = new int[20000];
        
        try {
            datos = new RandomAccessFile("datos.dat", "r");
            datosNot = new RandomAccessFile("datos_no.dat", "r");
            salida = new PrintStream(new File("salida4.txt"));
            // Pasamos los elementos del fichero a un vector
            for (int i = 0; i < 100000; i++) {
                data[i] = datos.readInt();
            }
            for (int i = 0; i < 20000; i++) {
                nodata[i] = datosNot.readInt();
            }
            datos.close();
            datosNot.close();
            
            int[] orden = { 5, 7, 9, 11, 20, 25, 55, 75, 105, 201, 301 };
            ContenedorDeEnteros contenedor;
            for (int numOrden = 0; numOrden < orden.length; numOrden++) {

                contenedor = new ContenedorDeEnteros();
                contenedor.crear("temp", orden[numOrden]);
                System.out.print("Prueba de rendimiento para arbolB de orden " + orden[numOrden]);

                // Tipo de contenedor
                salida.println("OERACIONES PARA ARBOL B DE ORDEN " + orden[numOrden]);

                // Insertamos los datos en el contenedor
                salida.println("\nTIEMPO DE INSERCCION CADA 10000 ELEMENTOS:");
                System.out.println("\nInsercion de orden " + orden[numOrden]);
                for (int i = 0; i < 10; i++){
                    long t1 = System.currentTimeMillis();
                    for (int j = i*10000; j < 10000*(i+1); j++){
                        contenedor.insertar(data[j]);
                    }
                    long t2 = System.currentTimeMillis();
                    salida.println((t2 - t1)/10. + " ms");
                    System.out.println((t2 - t1)/10. + " ms");
                }
                
                // Extraemos los datos en el contenedor
                salida.println("\nTIEMPO DE EXTRACCION CADA 10000 ELEMENTOS:");
                System.out.println("\nExtraccionde orden " + orden[numOrden]);
                for (int i = 0; i < 10; i++){
                    long t1 = System.currentTimeMillis();
                    for (int j = i*10000; j < 10000*(i+1); j++){
                        contenedor.extraer(data[j]);
                    }
                    long t2 = System.currentTimeMillis();
                    salida.println((t2 - t1)/10. + " ms");
                    System.out.println((t2 - t1)/10. + " ms");
                }
                contenedor.vaciar();

                // Busqueda exitosa los datos en el contenedor
                salida.println("\nTIEMPO DE BUSQUEDA EXITOSA CADA 10000 ELEMENTOS:");
                System.out.println("\nBusqueda exitosa de orden " + orden[numOrden]);
                for(int i = 0; i < 10; i++){
                    for (int j = i*10000; j < 10000*(i+1); j++){
                            contenedor.insertar(data[j]);
                    }
                    long t1 = System.currentTimeMillis();
                        for (int k = 0; k < 10000*(i+1); k++){
                            contenedor.buscar(data[k]);
                        }
                    long t2 = System.currentTimeMillis();
                    salida.println((t2-t1)/(10.*(i+1)) + " ms");
                    System.out.println((t2-t1)/(10.*(i+1)) + " ms");
                }
                contenedor.vaciar();

                 // Busqueda infructuosa los datos en el contenedor
                System.out.println("\nBusqueda infructuosa de orden " + orden[numOrden]);
                salida.println("\nTIEMPO DE BUSQUEDA INFRUCTUOSA CADA 10000 ELEMENTOS:");
                for(int i = 0; i < 10; i++){
                    for (int j = i*10000; j < 10000*(i+1); j++){
                        contenedor.insertar(data[j]);
                    }
                    long t1 = System.currentTimeMillis();
                        for (int k = 0; k < 20000; k++){
                            contenedor.buscar(nodata[k]);
                        }
                    long t2 = System.currentTimeMillis();
                    salida.println((t2-t1)/20. + " ms");
                    System.out.println((t2-t1)/20. + " ms");
                }
                contenedor.vaciar();
                contenedor.cerrar();
            }
            salida.close();
            System.out.println("DONE.");
        } catch (IOException e) {
            System.out.println("El error ocurrido ha sido: " + e.getMessage());
        }
    }
}