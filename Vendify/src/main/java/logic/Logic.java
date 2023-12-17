package logic;

import java.util.ArrayList;

import database.Producto;
import database.Admin;
import database.Maquina;
import database.User;
import database.ConnectionDDBB;
import database.Venta;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Logic {
    
    
    public static ArrayList<Producto> getProductosFromDB() {

        ArrayList<Producto> productos = new ArrayList<Producto>();
        ConnectionDDBB conector = new ConnectionDDBB();
        Connection con = null;

        try {
            con = conector.obtainConnection(true);
            Log.log.debug("DataBase connected");

            PreparedStatement ps = ConnectionDDBB.getProductos(con);
            Log.log.info("Query=> {}", ps.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setPrecio(rs.getDouble("precio"));
                producto.setId_maquina(rs.getInt("id_maquina"));
                productos.add(producto);
            }
        } catch (SQLException e) {
                
            Log.log.error("Error: {}", e);
            productos = new ArrayList<Producto>();
        } catch (NullPointerException e) {
            Log.log.error("Error: {}", e);
            productos = new ArrayList<Producto>();
        } catch (Exception e) {
            Log.log.error("Error: {}", e);
            productos = new ArrayList<Producto>();
        } finally {
            conector.closeConnection(con);
        }
        return productos;

    }
    
    public static ArrayList<User> getUsersFromDB() {

        ArrayList<User> users = new ArrayList<User>();
        ConnectionDDBB conector = new ConnectionDDBB();
        Connection con = null;

        try {
            con = conector.obtainConnection(true);
            Log.log.debug("DataBase connected");
            PreparedStatement ps = ConnectionDDBB.getUsers(con);
            ResultSet rs = ps.executeQuery();
            Log.log.info("Query=> {}", ps.toString());
            while (rs.next()) {
                User usuario = new User();
                usuario.setId(rs.getInt("id"));
                usuario.setEmail(rs.getString("email"));
                usuario.setPassword(rs.getString("password"));
                usuario.setUsername(rs.getString("username"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setSaldo(rs.getInt("saldo"));
                users.add(usuario);
            }
        } catch (SQLException e) {

            Log.log.error("Error: {}", e);
            users = new ArrayList<User>();
            
        } catch (NullPointerException e) {
            Log.log.error("Error: {}", e);
            users = new ArrayList<User>();
        } catch (Exception e) {
            Log.log.error("Error: {}", e);
            users = new ArrayList<User>();
        } finally {
            conector.closeConnection(con);
        }
        return users;

    }
    
     public static ArrayList<Admin> getAdminFromDB() {

        ArrayList<Admin> administrador = new ArrayList<Admin>();
        ConnectionDDBB conector = new ConnectionDDBB();
        Connection con = null;

        try {
            con = conector.obtainConnection(true);
            Log.log.debug("Database Connected");

            PreparedStatement ps = ConnectionDDBB.getAdmin(con);
            ResultSet rs = ps.executeQuery();
            Log.log.info("Query=> {}", ps.toString());

            while (rs.next()) {
                Admin admin = new Admin();
                admin.setId(rs.getInt("id"));
                admin.setUser(rs.getString("user"));
                admin.setPassword(rs.getString("password"));
                administrador.add(admin);
            }
        } catch (SQLException e) {
            Log.log.error("Error: {}", e);
            administrador = new ArrayList<Admin>();
            
        } catch (NullPointerException e) {
            Log.log.error("Error: {}", e);
            administrador = new ArrayList<Admin>();
        } catch (Exception e) {
            Log.log.error("Error: {}", e);
            administrador = new ArrayList<Admin>();
        } finally {
            conector.closeConnection(con);
        }
        return administrador;

    }
   
    public static ArrayList<Maquina> getMaquinasFromDB() {

        ArrayList<Maquina> maquinas = new ArrayList<Maquina>();
        ConnectionDDBB conector = new ConnectionDDBB();
        Connection con = null;

        try {
            con = conector.obtainConnection(true);

            PreparedStatement ps = ConnectionDDBB.getMaquinas(con);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Maquina machine = new Maquina();
                machine.setId(rs.getInt("id"));
                machine.setLocation(rs.getString("location"));
                machine.setSaldo(rs.getDouble("saldo"));
                maquinas.add(machine);
            }
        } catch (SQLException e) {

            maquinas = new ArrayList<Maquina>();

        } catch (NullPointerException e) {

            maquinas = new ArrayList<Maquina>();
        } catch (Exception e) {

            maquinas = new ArrayList<Maquina>();
        } finally {
            conector.closeConnection(con);
        }
        return maquinas;

    }
    
    public static ArrayList<Venta> getVentasFromDB() {

        ArrayList<Venta> ventas = new ArrayList<Venta>();
        ConnectionDDBB conector = new ConnectionDDBB();
        Connection con = null;

        try {
            con = conector.obtainConnection(true);

            PreparedStatement ps = ConnectionDDBB.getVentas(con);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Venta v = new Venta();
                v.setId_venta(rs.getInt("id_venta"));
                v.setId_producto(rs.getInt("id_producto"));
                v.setId_user(rs.getInt("id_user"));
               
                ventas.add(v);
            }
        } catch (SQLException e) {

            ventas = new ArrayList<Venta>();

        } catch (NullPointerException e) {

           ventas = new ArrayList<Venta>();
        } catch (Exception e) {

            ventas = new ArrayList<Venta>();
        } finally {
            conector.closeConnection(con);
        }
        return ventas;

    }
    
    public static User getUserFromUsername(String username) {
    
            ConnectionDDBB conector = new ConnectionDDBB();
            Connection con = conector.obtainConnection(true);
            User usuario = new User();
            try {
                con = conector.obtainConnection(true);
                String sql = "SELECT * FROM users WHERE username='"+username+"'";
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    
                    usuario.setId(rs.getInt("id"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setPassword(rs.getString("password"));
                    usuario.setUsername(rs.getString("username"));
                    usuario.setTelefono(rs.getString("telefono"));
                    usuario.setSaldo(rs.getInt("saldo"));
                    
            }
        } catch (SQLException e) {

            usuario = new User();

        } catch (NullPointerException e) {

           usuario = new User();
        } catch (Exception e) {

            usuario = new User();
        } finally {
            conector.closeConnection(con);
        }
        return usuario;

    
    }
    
    public static boolean comprobarEmail(String posibleEmail){

    try {
            ConnectionDDBB conector = new ConnectionDDBB();
            Connection con = conector.obtainConnection(true);
            String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, posibleEmail);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Manejar la excepción adecuadamente
        

        }
        return false;
    




}



}