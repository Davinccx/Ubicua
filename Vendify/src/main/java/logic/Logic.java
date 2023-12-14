package logic;

import java.util.ArrayList;

import database.Producto;
import database.Admin;
import database.Maquina;
import database.User;
import database.ConnectionDDBB;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

}
