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
                producto.setPrecio(rs.getInt("precio"));
                
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
                usuario.setPassword(rs.getInt("password"));
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
            Log.log.debug("DataBase disconnected");

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
            Log.log.debug("DataBase disconnected");
        }
        return administrador;

    }

    public static ArrayList<Maquina> getMaquinasFromDB() {
        ArrayList<Maquina> maquinas = new ArrayList<Maquina>();
        ConnectionDDBB conector = new ConnectionDDBB();
        Connection con = null;

        try {
            con = conector.obtainConnection(true);
            Log.log.debug("DataBase connected");
            PreparedStatement ps = ConnectionDDBB.getMaquinas(con);
            ResultSet rs = ps.executeQuery();
            Log.log.info("Query=> {}", ps.toString());
            while (rs.next()) {
                Maquina m = new Maquina();
                m.setId(rs.getInt("id"));
                m.setLocation(rs.getString("location"));
                m.setSaldo(rs.getInt("saldo"));
                m.setId_producto(rs.getInt("id_producto"));
                maquinas.add(m);
            }
        } catch (SQLException e) {

            maquinas = new ArrayList<Maquina>();
            Log.log.error("Error: {}", e);

        } catch (NullPointerException e) {

            maquinas = new ArrayList<Maquina>();
            Log.log.error("Error: {}", e);

        } catch (Exception e) {

            maquinas = new ArrayList<Maquina>();
            Log.log.error("Error: {}", e);

        } finally {
            conector.closeConnection(con);
            Log.log.debug("DataBase disconnected");
        }
        return maquinas;
    }

    public static ArrayList<Venta> getVentasFromDB() {

        ArrayList<Venta> ventas = new ArrayList<Venta>();
        ConnectionDDBB conector = new ConnectionDDBB();
        Connection con = null;

        try {
            con = conector.obtainConnection(true);
            Log.log.debug("DataBase connected");
            PreparedStatement ps = ConnectionDDBB.getVentas(con);
            ResultSet rs = ps.executeQuery();
            Log.log.info("Query=> {}", ps.toString());
            while (rs.next()) {
                Venta v = new Venta();
                v.setId_venta(rs.getInt("id_venta"));
                v.setId_producto(rs.getInt("id_producto"));
                v.setId_user(rs.getInt("id_user"));

                ventas.add(v);
            }
        } catch (SQLException e) {

            ventas = new ArrayList<Venta>();
            Log.log.error("Error: {}", e);

        } catch (NullPointerException e) {

            ventas = new ArrayList<Venta>();
            Log.log.error("Error: {}", e);
        } catch (Exception e) {

            ventas = new ArrayList<Venta>();
        } finally {
            conector.closeConnection(con);
            Log.log.debug("DataBase disconnected");

        }
        return ventas;

    }

    public static User getUserFromUsername(String username) {

        ConnectionDDBB conector = new ConnectionDDBB();
        Connection con = conector.obtainConnection(true);
        Log.log.debug("DataBase connected");
        User usuario = new User();
        try {
            con = conector.obtainConnection(true);
            String sql = "SELECT * FROM users WHERE username='" + username + "'";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            Log.log.info("Query=> {}", ps.toString());
            while (rs.next()) {

                usuario.setId(rs.getInt("id"));
                usuario.setEmail(rs.getString("email"));
                usuario.setPassword(rs.getInt("password"));
                usuario.setUsername(rs.getString("username"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setSaldo(rs.getInt("saldo"));

            }
        } catch (SQLException e) {

            usuario = new User();
            Log.log.error("Error: {}", e);

        } catch (NullPointerException e) {

            usuario = new User();
            Log.log.error("Error: {}", e);
        } catch (Exception e) {

            usuario = new User();
            Log.log.error("Error: {}", e);
        } finally {
            conector.closeConnection(con);
            Log.log.debug("DataBase disconnected");
        }
        return usuario;

    }

    public static boolean comprobarEmail(String posibleEmail) {

        try {
            ConnectionDDBB conector = new ConnectionDDBB();
            Connection con = conector.obtainConnection(true);
            Log.log.debug("DataBase connected");
            String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, posibleEmail);
            ResultSet rs = ps.executeQuery();
            Log.log.info("Query=> {}", ps.toString());

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.log.error("Error: {}", e);
            // Manejar la excepción adecuadamente

        }
        return false;

    }
    
    
    public static void insertMaquina(Maquina machine) {

        try {
            ConnectionDDBB conector = new ConnectionDDBB();
            Connection con = conector.obtainConnection(true);
            Log.log.debug("DataBase connected");
            int id = machine.getId();
            String location = machine.getLocation();
            int saldo = machine.getSaldo();
            int id_producto = machine.getId_producto();

            String sql = "INSERT  INTO maquina(location,saldo,id_producto) VALUES (?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);

            
            ps.setString(1, location);
            ps.setInt(2, saldo);
            ps.setInt(3, id_producto);
            ResultSet rs = ps.executeQuery();
            Log.log.info("Query=> {}", ps.toString());

            int affectedRows = ps.executeUpdate();
            Log.log.info("Query executed: {}", ps.toString());
            
            if(affectedRows==0){
            
                Log.log.error("No se ha podido insertar la maquina");
            
            }else{
            
                Log.log.error("Maquina insertada en la BBDD correctamente");
            
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Log.log.error("Error: {}", e);
            // Manejar la excepción adecuadamente
        }

    }
    
    public static void insertProducto(Producto producto) {

        try {
            ConnectionDDBB conector = new ConnectionDDBB();
            Connection con = conector.obtainConnection(true);
            Log.log.debug("DataBase connected");
            int id =producto.getId();
            String nombre = producto.getNombre();
            int precio = producto.getPrecio();
            String descripcion = producto.getDescripcion();
            

            String sql = "INSERT  INTO productos(nombre,precio,descripcion) VALUES (?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);

            
            ps.setString(1, nombre);
            ps.setInt(2, precio);
            ps.setString(3, descripcion);
            ResultSet rs = ps.executeQuery();
            Log.log.info("Query=> {}", ps.toString());

            int affectedRows = ps.executeUpdate();
            Log.log.info("Query executed: {}", ps.toString());
            
            if(affectedRows==0){
            
                Log.log.error("No se ha podido insertar el producto");
            
            }else{
            
                Log.log.error("Producto insertada en la BBDD correctamente");
            
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Log.log.error("Error: {}", e);
            // Manejar la excepción adecuadamente
        }

    }
    
    public static void insertVenta(Venta venta) {

        try {
            ConnectionDDBB conector = new ConnectionDDBB();
            Connection con = conector.obtainConnection(true);
            Log.log.debug("DataBase connected");
            int id_user =venta.getId_venta();
            int id_producto = venta.getId_producto();
            
            

            String sql = "INSERT INTO ventas(id_user,id_producto) VALUES (?,?)";
            PreparedStatement ps = con.prepareStatement(sql);

            
            ps.setInt(1, id_user);
            ps.setInt(2, id_producto);
            
            ResultSet rs = ps.executeQuery();
            Log.log.info("Query=> {}", ps.toString());

            int affectedRows = ps.executeUpdate();
            Log.log.info("Query executed: {}", ps.toString());
            
            if(affectedRows==0){
            
                Log.log.error("No se ha podido insertar la venta");
            
            }else{
            
                Log.log.error("Venta insertada en la BBDD correctamente");
            
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Log.log.error("Error: {}", e);
            // Manejar la excepción adecuadamente
        }

    }

}
