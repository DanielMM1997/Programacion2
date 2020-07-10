package practica2;

import java.io.*;
import java.util.Arrays;

public class PruebaContenedor {

    public static void main(String[] args) {
        pruebasContenedor();
        pruebasRendimiento();
    }

    public static void pruebasContenedor() {
        ContenedorDeEnteros prueba = new ContenedorDeEnteros(10);
        for(int i = 0; i < 10; i++) prueba.insertar(i);
        int[] ele = {1, 3, 6, 7, 0};
        for(int i = 0; i < ele.length; i++) {
            if (!prueba.buscar(ele[i])) System.out.println("Error no se encuentra el elemento (" + ele[i] + ")");
        }
        if (prueba.cardinal() != 10) System.out.println("Error en el numero de elementos es incorrecto (10)");
        if (prueba.buscar(11)) System.out.println("Error se encuentra un elemento inexistente (11)");
        if (prueba.buscar(-1)) System.out.println("Error se encuentra un elemento inexistente (-1)");
       	if (prueba.insertar(15)) System.out.println("Error se he inserta el elemento (15)");
        if (prueba.insertar(5)) System.out.println("Error se inserta un elemento repetido (5)");
        if (prueba.extraer(15)) System.out.println("Error se ha extraido un elemento inexistente (15)");
        if (!prueba.extraer(5)) System.out.println("Error no se ha extraido el elemento (5)");
        if (prueba.buscar(5)) System.out.println("Error no se extraido el elemento (5)");
        if (!prueba.extraer(2)) System.out.println("Error no se ha extraido el elemento (2)");
        if (prueba.buscar(2)) System.out.println("Error no se extraido el elemento (2)");
        if (prueba.cardinal() != 8) System.out.println("Error el numero de elementos es incorrecto (8)");
        if (!prueba.extraer(0)) System.out.println("Error no se ha extraido el elemento (0)");
        int[] vec1 = {1, 3, 4, 6, 7, 8, 9};
        int[] vec2 = prueba.elementos();
        if (!Arrays.equals(vec1, vec2)) System.out.println("Error el metodo elementos falla");
        prueba.vaciar();
        if (prueba.cardinal() != 0) System.out.println("Error el numero de elementos es incorrecto (0)");
        if (prueba.extraer(5)) System.out.println("Error no se puede extraer en contenedor vacio (5)");
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
            salida = new PrintStream(new File("salida2.txt"));
            // Pasamos los elementos del fichero a un vector
            for (int i = 0; i < 100000; i++) {
                data[i] = datos.readInt();
            }
            for (int i = 0; i < 20000; i++) {
                nodata[i] = datosNot.readInt();
            }
            datos.close();
            datosNot.close();
            ContenedorDeEnteros contenedor = new ContenedorDeEnteros(100000);
            
            // Insertamos los datos en el contenedor
            salida.println("TIEMPO DE INSERCCION CADA 10000 ELEMENTOS:");
            System.out.println("Insercion: ");
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
            System.out.println("\nExtraccion: ");
            for (int i = 0; i < 10; i++){
                long t1 = System.currentTimeMillis();
                for (int j = i*10000; j < 10000*(i+1); j++){
                    contenedor.extraer(data[j]);
                }
                long t2 = System.currentTimeMillis();
                salida.println((t2 - t1)/10. + " ms");
                System.out.println((t2 - t1)/10. + " ms");
            }

            // Busqueda exitosa los datos en el contenedor
            System.out.println("\nBusqueda exitosa: ");
            salida.println("\nTIEMPO DE BUSQUEDA EXITOSA CADA 10000 ELEMENTOS:");
            for(int i = 0; i < 10; i++){
            	for (int j = i*10000; j < 10000*(i+1); j++){
            		contenedor.insertar(data[j]);
            	}
            	long t1 = System.currentTimeMillis();
                for (int n = 0; n < 1000; n++) { //Bucle para estabilizar el tiempo
                    for (int k = 0; k < 10000*(i+1); k++){
                        contenedor.buscar(data[k]);
                    }
                }
                long t2 = System.currentTimeMillis();
                salida.println((t2-t1)/(10000.*(i+1)) + " ms");
                System.out.println((t2-t1)/(10000.*(i+1)) + " ms");
            }
            contenedor.vaciar();

            // Busqueda infructuosa los datos en el contenedor
            System.out.println("\nBusqueda infructuosa: ");
            salida.println("\nTIEMPO DE BUSQUEDA INFRUCTUOSA CADA 10000 ELEMENTOS:");
            for(int i = 0; i < 10; i++){
            	for (int j = i*10000; j < 10000*(i+1); j++){
            		contenedor.insertar(data[j]);
            	}
            	long t1 = System.currentTimeMillis();
            	for (int n = 0; n < 1000; n++) { //Bucle para estabilizar el tiempo
	                for (int k = 0; k < 20000; k++){
	                    contenedor.buscar(nodata[k]);
	                }
	            }
                long t2 = System.currentTimeMillis();
                salida.println((t2-t1)/20000. + " ms");
                System.out.println((t2-t1)/20000. + " ms");
            }
            System.out.println("DONE.");
            salida.close();
        } catch (IOException e) {
            System.out.println("El error ocurrido ha sido: " + e.getMessage());
        }
    }
}