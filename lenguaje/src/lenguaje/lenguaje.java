/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lenguaje;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.Scanner;

/**
 *
 * @author chern007
 */
public class lenguaje {

    public static void main(String[] args) {

        int contador = 0;//contador para el bucle while, como maximo valdra times - 1

        //creamos una istancia de la clase de jsvs psra el acceso aleatorio de ficheros
        RandomAccessFile fichero = null;
        FileLock bloqueo = null;

        try {

            int times = Integer.valueOf(args[0]);//cogemos de los argumentos las veces que se repetira el proceso
            String path = args[1];//cogemos de los argumentos la ruta de guardado    

            //se abre el fichero para lectura/escritura
            fichero = new RandomAccessFile(path, "rwd");
            //bloqueamos el fichero, porque hemos entrado en región crítica
            bloqueo = fichero.getChannel().lock();

            try {

//                System.out.print("¿Número de cadenas a generar?: ");//**para pruebas**
//                int times = Integer.parseInt(br.readLine());//**para pruebas**
                while (contador < times) { //iniciamos el bucle para realizar varias palabras
                    String abecedario = "abcdefghijklmnñopqrstuvwxyz";
                    String cadena2 = "";
                    //generamos la longitud de la cadena (máximo de 20 letras para que no sean muy extensas)
                    int longitudCadena = (int) Math.floor(Math.random() * 20 + 1);
                    for (int x = 0; x < longitudCadena; x++) {
                        int caracter = (int) Math.floor(Math.random() * 27); //Generamos la cadena
                        cadena2 = cadena2 + abecedario.charAt(caracter);
                    }

                    //imprimimos en consola para poder comprobar a golpe de vista
                    System.out.println(cadena2);

                    //apuntamos a la ultima posicion escrita del fichero
                    fichero.seek(fichero.length());

                    //escribimos
                    if (fichero.length() != 0) {
                        fichero.writeBytes(System.getProperty("line.separator") + cadena2);
                    } else {
                        fichero.writeBytes(cadena2);
                    }

                    contador++;
                }

            } catch (Exception e) {

                System.err.println("ERROR: Has introducido un caracter no permitido.");
                System.err.println(e);
                return;
            }

        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {

                bloqueo.release(); //liberamos el bloqueo del canal del fichero
                bloqueo = null; // quitamos la referencia para que el recolector de basura pueda liberar memoria

                if (fichero != null) {
                    fichero.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
