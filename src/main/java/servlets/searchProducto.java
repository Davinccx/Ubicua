package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import database.Maquina;
import database.Producto;
import jakarta.servlet.http.HttpSession;
import logic.Log;
import logic.Logic;

public class searchProducto extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public searchProducto() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Log.log.info("-- Search Product--");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {

            HttpSession session = request.getSession();

            if (session != null) {
                String productoBuscado = (String) session.getAttribute("nombre_producto");

                if (productoBuscado != null) {
                    ArrayList<Producto> productos = Logic.getProductosFromDB();
                    ArrayList<Maquina> maquinas = Logic.getMaquinasFromDB();
                    ArrayList<Compra> productosBuscados = new ArrayList<Compra>();

                    for (int i = 0; i < maquinas.size(); i++) {
                        Maquina maquina = maquinas.get(i);
                        for (int j = 0; j < productos.size(); j++) {
                            Producto producto = productos.get(j);

                            if (producto.getNombre().contains(productoBuscado)) {
                                Compra c = new Compra();
                                c.setId_compra(0);
                                c.setNombre_producto(producto.getNombre());
                                c.setDescripcion(producto.getDescripcion());
                                c.setLocalizacion(maquina.getLocation());
                                c.setPrecio(producto.getPrecio());
                                productosBuscados.add(c);
                            }
                        }
                    }

                    String jsonProductos = new Gson().toJson(productosBuscados);
                    Log.log.info("JSON Values => {}", jsonProductos);
                    out.println(jsonProductos);
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Sesión no iniciada.");
                    Log.log.error("Usuario no loggeado");
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Sesión no iniciada.");
                Log.log.error("Bad Request from server");
            }
        } catch (NumberFormatException nfe) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error en el servidor.");
            Log.log.error("Number Format Exception: {}", nfe);
        } catch (IndexOutOfBoundsException iobe) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error en el servidor.");
            Log.log.error("Index out of bound exception: {}", iobe);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error en el servidor.");
            Log.log.error("Number Format Exception: {}", e);
        } finally {

            out.close();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
