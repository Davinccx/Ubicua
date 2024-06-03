package database;




public class Admin {
    
    private int admin_id;
    private String username;
    private String password;

    public Admin() {
        
    }
    
   
    public int getId() {
        return admin_id;
    }

    public void setId(int id) {
        this.admin_id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
}
