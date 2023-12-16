package servlets;

import com.google.gson.Gson;
import database.Maquina;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static java.lang.System.out;
import database.Venta;
import database.User;
import database.Producto;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.ArrayList;
import logic.Logic;

public class getVentasFromUser extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public getVentasFromUser() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {

            System.out.println("-- Get Ventas from User information from DB--");
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
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
                        Integer id_producto = producto.getId();

                        for (int j = 0; j < ventasUsuario.size(); j++) {

                            Venta venta = ventasUsuario.get(j);

                            for (Maquina maquina : maquinas) {
                                if (venta.getId_producto() == id_producto && producto.getId_maquina() == maquina.getId()) {

                                    Integer id_compra = venta.getId_venta();
                                    String nombre_producto = producto.getNombre();
                                    String desc_producto = producto.getDescripcion();
                                    double precio = producto.getPrecio();

                                    Compra compraUser = new Compra();
                                    compraUser.setId_compra(id_compra);
                                    compraUser.setNombre_producto(nombre_producto);
                                    compraUser.setDescripcion(desc_producto);
                                    compraUser.setPrecio(precio);
                                    compraUser.setLocalizacion(maquina.getLocation());

                                    comprasUsuario.add(compraUser);

                                }
                            }

                        }

                    }

                    String jsonVentas = new Gson().toJson(comprasUsuario);
                    System.out.println("JSON Values=> " + jsonVentas);
                    out.println(jsonVentas);
                    out.close();

                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario no logueado.");
                }

            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Sesión no iniciada.");
            }

        } catch (NumberFormatException nfe) {
            out.println("-1");
            System.out.println("Number Format Exception:" + nfe);
        } catch (IndexOutOfBoundsException iobe) {
            out.println("-1");
            System.out.println("Index out of bounds Exception: " + iobe);
        } catch (Exception e) {
            out.println("-1");
            System.out.println("Exception: " + e);
        } finally {
            out.close();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
