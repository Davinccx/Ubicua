package database;

import java.sql.Date;




public class User {
    
    private int user_id;
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String telefono;
    private Date fecha_registro;   
    private String matricula;
    private String username;

    public User() {
    
    }

    //Setter
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setFecha_registro(Date fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    
    
    //Getter
    public int getUser_id() {
        return user_id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public Date getFecha_registro() {
        return fecha_registro;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getUsername() {
        return username;
    }
    
    

    
   
    
}
