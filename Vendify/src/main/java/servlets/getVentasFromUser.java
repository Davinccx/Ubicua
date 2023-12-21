package servlets;

import com.google.gson.Gson;
import database.Maquina;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import database.Venta;
import database.User;
import database.Producto;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.ArrayList;
import logic.Log;
import logic.Logic;

public class getVentasFromUser extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public getVentasFromUser() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        Log.log.info("-- Get Compras from an User --");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {

            
            HttpSession session = request.getSession();
            ArrayList<Venta> ventasUsuario = new ArrayList<Venta>();
            ArrayList<Compra> comprasUsuario = new ArrayList<Compra>();
            if (session != null) {

                String usernameLoggeado = (String) session.getAttribute("username");

                if (usernameLoggeado != null) {

                    ArrayList<Venta> ventas = Logic.getVentasFromDB();
                    ArrayList<Producto> productos = Logic.getProductosFromDB();
                    ArrayList<Maquina> maquinas = Logic.getMaquinasFromDB();
                    User user = Logic.getUserFromUsername(usernameLoggeado);
                    Integer id_usuario = user.getId();
                    for (int x = 0; x < ventas.size(); x++) {

                        Venta venta = ventas.get(x);
                        Integer id_user = venta.getId_user();

                        if (id_usuario.equals(id_user)) {

                            ventasUsuario.add(venta);

                        }

                    }

                    for (int i = 0; i < productos.size(); i++) {

                        Producto producto = productos.get(i);
                        int id_producto = producto.getId();

                        for (int j = 0; j < ventasUsuario.size(); j++) {

                            Venta venta = ventasUsuario.get(j);
                            if (venta.getId_producto() == id_producto) {
                                for (Maquina maquina : maquinas) {

                                    if (id_producto == maquina.getId()) {

                                        Integer id_compra = venta.getId_venta();
                                        String nombre_producto = producto.getNombre();
                                        String desc_producto = producto.getDescripcion();
                                        Integer precio = producto.getPrecio();

                                        Compra compraUser = new Compra();
                                        compraUser.setId_compra(id_compra);
                                        compraUser.setNombre_producto(nombre_producto);
                                        compraUser.setDescripcion(desc_producto);
                                        compraUser.setPrecio(precio);
                                        compraUser.setLocalizacion(maquina.getLocation());
                                        compraUser.setId_maquina(maquina.getId());
                                        comprasUsuario.add(compraUser);

                                    }
                                }

                            }
                        }

                    }

                    String jsonVentas = new Gson().toJson(comprasUsuario);
                    Log.log.info("JSON Values=> {}" ,jsonVentas);
                    out.println(jsonVentas);
                    out.close();

                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario no logueado.");
                    Log.log.error("Usuario no loggeado");
                }

            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Sesión no iniciada.");
                Log.log.error("Bad Request from server");
            }   

        } catch (NumberFormatException nfe) {
            out.println("-1");
            Log.log.error("Number Format Exception: {}", nfe);
        } catch (IndexOutOfBoundsException iobe) {
            out.println("-1");
            Log.log.error("Index out of bound exception: {}", iobe);
        } catch (Exception e) {
            out.println("-1");
            Log.log.error("Number Format Exception: {}", e);
        } finally {
            out.close();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
