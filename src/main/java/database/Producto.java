package database;

public class Producto {

    private int id;
    private String nombre;
    private String descripcion;
    private int precio;
    private int id_maquina;

    public Producto() {
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public void setIdMaquina(int id_maquina){
    
        this.id_maquina = id_maquina;
    
    }
    
    public int getIdMaquina(){
    
        return id_maquina;
    }

}
