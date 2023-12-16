package servlets;

import com.google.gson.Gson;
import database.User;
import database.Venta;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.util.ArrayList;
import logic.Logic;



public class getVentaInfo extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public getVentaInfo() {
        super();
    }
    
     protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            
            System.out.println("-- Get information from a Venta information from DB--");
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            HttpSession session = request.getSession();
            ArrayList<Venta> ventasUsuario = new ArrayList<Venta>();
            if (session != null) {

                String usernameLoggeado = (String) session.getAttribute("username");

                if (usernameLoggeado != null) {

                    ArrayList<Venta> ventas = Logic.getVentasFromDB();
                        
                    User user = Logic.getUserFromUsername(usernameLoggeado);
                    Integer id_usuario = user.getId();
                    for (int x = 0; x < ventas.size(); x++) {

                        Venta venta = ventas.get(x);
                        Integer id_user = venta.getId_user();

                        if (id_usuario.equals(id_user)) {

                            ventasUsuario.add(venta);

                        }

                    }
                    String jsonVentas = new Gson().toJson(ventasUsuario);
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