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

        //El total de l�neas del archivo .TXT:
        String[] text = new String[ms_iTextSize];

        try {
            //"this.getClass().getResourceAsStream" no s�lo crea un vinculo hacia
            //el archivo a leer, sino que adem�s cuando es  usado en el objeto Reader 
            //como par�metro, carga una nueva l�ena del archivo .TXT 
            //cada vez que este ejecuta su m�todo "read". 
            //El par�metro que se le pasa es la ruta del archivo .TXT:
            InputStream is = this.getClass().getResourceAsStream(ms_sArchiveName);
            //Al estar cargado con "is", el cual conoce la informacion del archivo .TXT, 
            //es capaz de leer caracteres de la siguiente l�nia distinta, 
            //por cada vez que se ejecute su m�todo "read":
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

    //Este m�todo SOLO LEE una l�nea entera del .TXT
    //Para obtener todo el texto, se debe de invocar tantas veces como l�neas
    //tenga el .TXT
    private static String readLine(Reader r) throws IOException {
        String res = null;
        StringBuffer linea = new StringBuffer();
        try {
            int c;

            while (true) {
                //Devuelve el caracter o vacio (Si lo hay) en Ascii:
                c = r.read();
               

                //Si se produce un salto de l�nea o termina la l�nea (devuelve -1),
                //se rompe el bucle y se da la l�nea como le�da y el m�todo como concluido:
                if (c == '\n' || c == -1) {
                    break;
                }
                //S�lo queremos a�adir caracteres a nuestro StringBuffer, no el 
                //retorno de carro(que tambien es un car�cter en la tabla Ascii).
                //Si a�adimos tambien el car�cter Ascii del retorno de carro, nos
                //quedar� un salto de l�nea ANTES del inicio del texto de cada l�nea,
                //con excepci�n de la primera:
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
