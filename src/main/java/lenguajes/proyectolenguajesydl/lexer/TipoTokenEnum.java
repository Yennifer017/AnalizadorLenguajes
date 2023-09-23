/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package lenguajes.proyectolenguajesydl.lexer;

/**
 *
 * @author yenni
 */
public enum TipoTokenEnum {
    IDENTIFICADOR("Identificador");
    
    public final String value;

    private TipoTokenEnum(String value) {
        this.value = value;
    }
    @Override
    public String toString(){
        return this.value;
    }
}
