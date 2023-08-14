
package lenguajes.proyectolenguajesydl.util;

import java.io.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

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
    public void saveAs(String text, String extension){
        String path = JOptionPane.showInputDialog(null, "Ingresa un nombre para guardar el archivo",
                "Guardando un nuevo archivo", JOptionPane.INFORMATION_MESSAGE);
        if (path != null && !path.equals("")) {
            this.saveFile(text, path + extension);
            JOptionPane.showMessageDialog(null, "Se ha guardado el archivo", ""
                    + "Guardado exitoso", JOptionPane.ERROR_MESSAGE);
        } else if (path != null && path.equals("")) {
            JOptionPane.showMessageDialog(null, """
                                            No has ingresado un nombre de archivo valido,
                                            no se ha podido guardar, intentalo de nuevo.""",
                    "Se ha producido un error", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    public void saveFromExistentPath(String text, String path, String fileName){
        int option = JOptionPane.showConfirmDialog(null, "¿Guardar el archivo "
                + fileName + "?", "Guardando archivo..",
                JOptionPane.CANCEL_OPTION, JOptionPane.OK_CANCEL_OPTION);
        if (option == 0) { //cuando confirma
            this.saveFile(text, path);
            JOptionPane.showMessageDialog(null, "Se ha guardado el archivo", ""
                    + "Guardado exitoso", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}