package database;



public class Maquina {

    private int id;
    private String location;
    private int saldo;
    private int id_producto;
   

    public Maquina() {
        this.id = 0;
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_productos) {
        this.id_producto = id_productos;
    }
    
   

    
   
    
}
