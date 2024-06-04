package logic;

import java.util.ArrayList;

import database.Admin;
import database.User;
import database.ConnectionDDBB;
import database.Parking;
import database.Plaza;
import database.Reserva;


import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;

import java.util.Random;


public class Logic {

    
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
                usuario.setUser_id(rs.getInt("user_id"));
                usuario.setEmail(rs.getString("email"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellido(rs.getString("apellido"));
                usuario.setEmail(rs.getString("email"));
                usuario.setPassword(rs.getString("password"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setFecha_registro(rs.getDate("fecha_registro"));
                usuario.setMatricula(rs.getString("matricula"));
                usuario.setUsername(rs.getString("username"));
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
                admin.setId(rs.getInt("admin_id"));
                admin.setUsername(rs.getString("username"));
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

    public static ArrayList<Parking> getParkingsFromDB() {
        ArrayList<Parking> parkings = new ArrayList<Parking>();
        ConnectionDDBB conector = new ConnectionDDBB();
        Connection con = null;

        try {
            con = conector.obtainConnection(true);
            Log.log.debug("DataBase connected");
            PreparedStatement ps = ConnectionDDBB.getParkings(con);
            ResultSet rs = ps.executeQuery();
            Log.log.info("Query=> {}", ps.toString());
            while (rs.next()) {
                Parking p = new Parking();
                p.setParking_id(rs.getInt("parking_id"));
                p.setNombre(rs.getString("nombre"));
                p.setDireccion(rs.getString("direccion"));
                p.setCiudad(rs.getString("ciudad"));
                p.setC_postal(rs.getString("codigo_postal"));
                p.setCapacidad_total(rs.getInt("capacidad_total"));
                p.setPlazas_disponibles(rs.getInt("plazas_disponibles"));
                parkings.add(p);
            }
        } catch (SQLException e) {

            parkings = new ArrayList<Parking>();
            Log.log.error("Error: {}", e);

        } catch (NullPointerException e) {

            parkings = new ArrayList<Parking>();
            Log.log.error("Error: {}", e);

        } catch (Exception e) {

            parkings = new ArrayList<Parking>();
            Log.log.error("Error: {}", e);

        } finally {
            conector.closeConnection(con);
            Log.log.debug("DataBase disconnected");
        }
        return parkings;
    }

    public static ArrayList<Reserva> getReservasFromDB() {

        ArrayList<Reserva> reservas = new ArrayList<Reserva>();
        ConnectionDDBB conector = new ConnectionDDBB();
        Connection con = null;

        try {
            con = conector.obtainConnection(true);
            Log.log.debug("DataBase connected");
            PreparedStatement ps = ConnectionDDBB.getReservas(con);
            ResultSet rs = ps.executeQuery();
            Log.log.info("Query=> {}", ps.toString());
            while (rs.next()) {
                Reserva r = new Reserva();
                r.setReserva_id(rs.getInt("reserva_id"));
                r.setUser_id(rs.getInt("user_id"));
                r.setParking_id(rs.getInt("parking_id"));
                r.setFecha_reserva(rs.getDate("fecha_reserva"));
                r.setHora_inicio(rs.getTimestamp("hora_inicio"));
                r.setHora_fin(rs.getTimestamp("hora_fin"));
                r.setId_plaza(rs.getInt("id_plaza"));
                reservas.add(r);
            }
        } catch (SQLException e) {

            reservas = new ArrayList<Reserva>();
            Log.log.error("Error: {}", e);

        } catch (NullPointerException e) {

            reservas = new ArrayList<Reserva>();
            Log.log.error("Error: {}", e);
        } catch (Exception e) {

            reservas = new ArrayList<Reserva>();
        } finally {
            conector.closeConnection(con);
            Log.log.debug("DataBase disconnected");

        }
        return reservas;

    }
    
    public static ArrayList<Plaza> getPlazasFromDB() {

        ArrayList<Plaza> plazas = new ArrayList<Plaza>();
        ConnectionDDBB conector = new ConnectionDDBB();
        Connection con = null;

        try {
            con = conector.obtainConnection(true);
            Log.log.debug("DataBase connected");
            PreparedStatement ps = ConnectionDDBB.getPlazas(con);
            ResultSet rs = ps.executeQuery();
            Log.log.info("Query=> {}", ps.toString());
            while (rs.next()) {
                Plaza p = new Plaza();
                p.setId_parking(rs.getInt("id_parking"));
                p.setId_plaza(rs.getInt("id_plaza"));
                p.setOcupado(rs.getInt("ocupado"));
                plazas.add(p);
            }
        } catch (SQLException e) {

            plazas = new ArrayList<Plaza>();
            Log.log.error("Error: {}", e);

        } catch (NullPointerException e) {

            plazas = new ArrayList<Plaza>();
            Log.log.error("Error: {}", e);
        } catch (Exception e) {

            plazas = new ArrayList<Plaza>();
        } finally {
            conector.closeConnection(con);
            Log.log.debug("DataBase disconnected");

        }
        return plazas;

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

                usuario.setUser_id(rs.getInt("user_id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellido(rs.getString("apellido"));
                usuario.setEmail(rs.getString("email"));
                usuario.setPassword(rs.getString("password"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setFecha_registro(rs.getDate("fecha_registro"));
                usuario.setMatricula(rs.getString("matricula"));
                usuario.setUsername(rs.getString("username"));

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
    
    
    public static void insertParking(Parking p) {

        try {
            ConnectionDDBB conector = new ConnectionDDBB();
            Connection con = conector.obtainConnection(true);
            Log.log.debug("DataBase connected");

            String nombre = p.getNombre();
            String direccion = p.getDireccion();
            String ciudad = p.getCiudad();
            String c_postal = p.getC_postal();
            int capacidad_t = p.getCapacidad_total();
            int plazas_d = p.getPlazas_disponibles();
           

            String sql = "INSERT  INTO parkings(nombre,direccion,ciudad,codigo_postal,capacidad_total,plazas_disponibles) VALUES (?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);

            
            ps.setString(1, nombre);
            ps.setString(2, direccion);
            ps.setString(3, ciudad);
            ps.setString(4, c_postal);
            ps.setInt(5, capacidad_t);
            ps.setInt(6, plazas_d);
            
            ResultSet rs = ps.executeQuery();
            Log.log.info("Query=> {}", ps.toString());

            int affectedRows = ps.executeUpdate();
            Log.log.info("Query executed: {}", ps.toString());
            
            if(affectedRows==0){
            
                Log.log.error("No se ha podido insertar el parking");
            
            }else{
            
                Log.log.error("Parking insertado en la BBDD correctamente");
            
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Log.log.error("Error: {}", e);
            // Manejar la excepción adecuadamente
        }

    }
    
    public static void insertReserva(Reserva reserva) {

        try {
            ConnectionDDBB conector = new ConnectionDDBB();
            Connection con = conector.obtainConnection(true);
            Log.log.debug("DataBase connected");
            
            
            int user_id =reserva.getUser_id();
            int parking_id =reserva.getParking_id();
            Date fecha_reserva = reserva.getFecha_reserva();
            Timestamp hora_inicio = reserva.getHora_inicio();
            Timestamp hora_fin = reserva.getHora_fin();

            String sql = "INSERT  INTO reservas(user_id,parking_id,fecha_reserva,hora_inicio,hora_fin) VALUES (?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);

            
            ps.setInt(1, user_id);
            ps.setInt(2, parking_id);
            ps.setDate(3, fecha_reserva);
            ps.setTimestamp(4, hora_inicio);
            ps.setTimestamp(5, hora_fin);
            ResultSet rs = ps.executeQuery();
            Log.log.info("Query=> {}", ps.toString());

            int affectedRows = ps.executeUpdate();
            Log.log.info("Query executed: {}", ps.toString());
            
            if(affectedRows==0){
            
                Log.log.error("No se ha podido insertar la reserva");
            
            }else{
            
                Log.log.error("Reserva insertada en la BBDD correctamente");
            
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Log.log.error("Error: {}", e);
            // Manejar la excepción adecuadamente
        }

    }
  
    
    public static String generarMatricula(){
        
        Random random = new Random();

        // Generar cuatro dígitos aleatorios
        StringBuilder digitos = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int digito = random.nextInt(10); // Genera un número entre 0 y 9
            digitos.append(digito);
        }

        // Generar tres letras aleatorias
        StringBuilder letras = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            char letra = (char) ('A' + random.nextInt(26));
            letras.append(letra);
        }

        // Combinar dígitos y letras para formar la matrícula
        return digitos.toString() + letras.toString();
        
    }
    
   
    public static List<Integer> getUsersID(){
         List<Integer> listaIds = new ArrayList<>();

        try {
            ConnectionDDBB conector = new ConnectionDDBB();
            Connection con = conector.obtainConnection(true);

            Log.log.debug("DataBase connected");

            String sql = "SELECT user_id FROM users"; 
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            Log.log.info("Query=> {}", ps.toString());

            while (rs.next()) {
                listaIds.add(rs.getInt("user_id"));
            }

        } catch (Exception e) {
            Log.log.error("Error: {}", e);
        } 

        return listaIds;
    }
    
    public static List<Integer> getUsersIDFromUsername(String username){
         List<Integer> listaIds = new ArrayList<>();

        try {
            ConnectionDDBB conector = new ConnectionDDBB();
            Connection con = conector.obtainConnection(true);

            Log.log.debug("DataBase connected");

            String sql = "SELECT user_id FROM users WHERE username = ?"; 
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            
            ResultSet rs = ps.executeQuery();
            Log.log.info("Query=> {}", ps.toString());

            while (rs.next()) {
                listaIds.add(rs.getInt("user_id"));
            }

        } catch (Exception e) {
            Log.log.error("Error: {}", e);
        } 

        return listaIds;
    }
    
    public static List<Integer> getParkingsID(){
         List<Integer> listaIds = new ArrayList<>();

        try {
            ConnectionDDBB conector = new ConnectionDDBB();
            Connection con = conector.obtainConnection(true);

            Log.log.debug("DataBase connected");

            String sql = "SELECT parking_id FROM parkings"; 
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            Log.log.info("Query=> {}", ps.toString());

            while (rs.next()) {
                listaIds.add(rs.getInt("parking_id"));
            }

        } catch (Exception e) {
            Log.log.error("Error: {}", e);
        } 

        return listaIds;
    }
    public static List<Integer> getPlazaID(){
         List<Integer> listaIds = new ArrayList<>();

        try {
            ConnectionDDBB conector = new ConnectionDDBB();
            Connection con = conector.obtainConnection(true);

            Log.log.debug("DataBase connected");

            String sql = "SELECT id_plaza FROM plaza"; 
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            Log.log.info("Query=> {}", ps.toString());

            while (rs.next()) {
                listaIds.add(rs.getInt("id_plaza"));
            }

        } catch (Exception e) {
            Log.log.error("Error: {}", e);
        } 

        return listaIds;
    }
    
    
     public static List<Integer> getPlazaIDFromParking(int parking_id){
         List<Integer> listaIds = new ArrayList<>();

        try {
            ConnectionDDBB conector = new ConnectionDDBB();
            Connection con = conector.obtainConnection(true);

            Log.log.debug("DataBase connected");

            String sql = "SELECT id_plaza FROM plaza WHERE id_parking= ? AND ocupado = 0 "; 
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, parking_id);
            
            ResultSet rs = ps.executeQuery();
            Log.log.info("Query=> {}", ps.toString());

            while (rs.next()) {
                listaIds.add(rs.getInt("id_plaza"));
            }

        } catch (Exception e) {
            Log.log.error("Error: {}", e);
        } 

        return listaIds;
    }
    
    
   

}
