package database;

/**
 *
 * @author David
 */
public class Parking {
    
    private int parking_id;
    private String nombre;
    private String direccion;
    private String ciudad;
    private String c_postal;
    private int capacidad_total;
    private int plazas_disponibles;
    
    
    
    public Parking(){
    
    }

    //Setters
    public void setParking_id(int parking_id) {
        this.parking_id = parking_id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public void setC_postal(String c_postal) {
        this.c_postal = c_postal;
    }

    public void setCapacidad_total(int capacidad_total) {
        this.capacidad_total = capacidad_total;
    }

    public void setPlazas_disponibles(int plazas_disponibles) {
        this.plazas_disponibles = plazas_disponibles;
    }
    
    
    
    
    //Getters
    public int getParking_id() {
        return parking_id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getC_postal() {
        return c_postal;
    }

    public int getCapacidad_total() {
        return capacidad_total;
    }

    public int getPlazas_disponibles() {
        return plazas_disponibles;
    }
    
    
    
}
