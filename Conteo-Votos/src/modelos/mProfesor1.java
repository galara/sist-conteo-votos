/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;
/**
 *
 * @author GLARA
 */

public class mProfesor1 {

    private final String nombre;
    private final String id;
    

    public mProfesor1(String nombre,String id) {
        this.nombre = nombre;
        this.id = id;
    }

    public String getID() {
        return id;
    }

        
    @Override
    public String toString() {
        return nombre;
    }

}
