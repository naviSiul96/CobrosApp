
package pojo;

/**
 *
 * @author USER
 */
public class Cliente {
    private String nombre;
    private int id;
    private int numero;

    public Cliente(String nombre, int id, int numero) {
        this.nombre = nombre;
        this.id = id;
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
    
    @Override
    public String toString() {
        return nombre + " " + numero; 
    }
    
}
