package logic;

import java.util.ArrayList;

import database.Producto;
import database.Admin;
import database.Maquina;
import database.User;
import database.ConnectionDDBB;


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

            PreparedStatement ps = ConnectionDDBB.getProductos(con);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setPrecio(rs.getDouble("precio"));
                productos.add(producto);
            }
        } catch (SQLException e) {

            productos = new ArrayList<Producto>();
        } catch (NullPointerException e) {

            productos = new ArrayList<Producto>();
        } catch (Exception e) {

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

            PreparedStatement ps = ConnectionDDBB.getUsers(con);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User usuario = new User();
                usuario.setId(rs.getInt("id"));
                usuario.setEmail(rs.getString("email"));
                usuario.setPassword(rs.getString("password"));
                usuario.setUsername(rs.getString("username"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setSaldo(rs.getDouble("saldo"));
                users.add(usuario);
            }
        } catch (SQLException e) {

            users = new ArrayList<User>();
            
        } catch (NullPointerException e) {

            users = new ArrayList<User>();
        } catch (Exception e) {

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

            PreparedStatement ps = ConnectionDDBB.getAdmin(con);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Admin admin = new Admin();
                admin.setId(rs.getInt("id"));
                admin.setUser(rs.getString("user"));
                admin.setPassword(rs.getString("password"));
                administrador.add(admin);
            }
        } catch (SQLException e) {

            administrador = new ArrayList<Admin>();
            
        } catch (NullPointerException e) {

            administrador = new ArrayList<Admin>();
        } catch (Exception e) {

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
                machine.setId_producto(rs.getInt("id_producto"));
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

}
