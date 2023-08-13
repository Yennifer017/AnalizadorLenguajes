
package lenguajes.proyectolenguajesydl.util;

import java.io.*;
import javax.swing.JFileChooser;

/**
 *
 * @author yenni
 */
public class Archivo {
    public String readTextFile(String path){ //obtiene el texto contenido en un archivo y lo devuelve
        String texto="";
        try{
            File archivo=new File(path); //creando el archivo
            FileReader lector = new FileReader(archivo); //lector del archivo
            BufferedReader buffer = new BufferedReader(lector); //para leer mas rapido el archivo
            String linea;
            while((linea=buffer.readLine()) !=null ){
                texto+=linea+"\n";
            }
            buffer.close();
            lector.close();
        }catch(IOException error){
            System.out.println(error);
        }
        return texto;
    }
    
    public String getPath(){
        JFileChooser buscador=new JFileChooser(); //creando el buscador de archivos
        buscador.showOpenDialog(null); //abrir el buscador
        return buscador.getSelectedFile().getAbsolutePath();
    }
    
    public void saveFile(String texto, String ruta) { //reescribe un archivo a partir de un texto
        try {
            File archivo = new File(ruta); //obtiene el archivo de la ruta
            FileWriter escritor = new FileWriter(archivo, false);
            BufferedWriter buffer = new BufferedWriter(escritor);
            buffer.write(texto);
            buffer.close();
            escritor.close();
        } catch (IOException error) {
            System.out.println(error);
        }
    }
    public void deleteFile(String path){
        File archivo=new File(path);
        archivo.delete();
    }
}