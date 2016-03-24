package com.luis.projectlgameengineimp;

import java.io.*;

/**
 *
 * @author Luis Valdes Frances
 */
public class StreamReader {
    
    private static String ms_sArchiveName;
    private static int ms_iTextSize;

    public StreamReader(String archiveName, int textSize) {
    	ms_sArchiveName = archiveName;
    	ms_iTextSize = textSize;
    }

    public String[] read() {

        //El total de líneas del archivo .TXT:
        String[] text = new String[ms_iTextSize];

        try {
            //"this.getClass().getResourceAsStream" no sólo crea un vinculo hacia
            //el archivo a leer, sino que además cuando es  usado en el objeto Reader 
            //como parámetro, carga una nueva líena del archivo .TXT 
            //cada vez que este ejecuta su método "read". 
            //El parámetro que se le pasa es la ruta del archivo .TXT:
            InputStream is = this.getClass().getResourceAsStream(ms_sArchiveName);
            //Al estar cargado con "is", el cual conoce la informacion del archivo .TXT, 
            //es capaz de leer caracteres de la siguiente línia distinta, 
            //por cada vez que se ejecute su método "read":
            Reader r = new InputStreamReader(is, "UTF-8");

            for (int i = 0; i < ms_iTextSize; i++) {
                try {
                    text[i] = readLine(r);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            try {
                is.close();
                r.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }


        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

        return text;

    }

    //Este método SOLO LEE una línea entera del .TXT
    //Para obtener todo el texto, se debe de invocar tantas veces como líneas
    //tenga el .TXT
    private static String readLine(Reader r) throws IOException {
        String res = null;
        StringBuffer linea = new StringBuffer();
        try {
            int c;

            while (true) {
                //Devuelve el caracter o vacio (Si lo hay) en Ascii:
                c = r.read();
               

                //Si se produce un salto de línea o termina la línea (devuelve -1),
                //se rompe el bucle y se da la línea como leída y el método como concluido:
                if (c == '\n' || c == -1) {
                    break;
                }
                //Sólo queremos añadir caracteres a nuestro StringBuffer, no el 
                //retorno de carro(que tambien es un carácter en la tabla Ascii).
                //Si añadimos tambien el carácter Ascii del retorno de carro, nos
                //quedará un salto de línea ANTES del inicio del texto de cada línea,
                //con excepción de la primera:
                if (c != '\r') {
                    //System.out.println("c(char) es " + (char) c);
                    linea.append((char) c);
                }
            }

            if (linea.length() == 0) {
                res = null;
            } else {
                res = linea.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        linea = null;
        return res;
    }
    
}
