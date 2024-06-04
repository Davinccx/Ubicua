package servlets;

import com.google.gson.Gson;
import database.Reserva;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import database.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.ArrayList;
import logic.Log;
import logic.Logic;

@WebServlet("/getReservasFromUsername")
public class getReservasFromUsername extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public getReservasFromUsername() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        Log.log.info("-- Get User from username information from DB--");
        response.setContentType("text/html;charset=UTF-8");
        try {

            HttpSession session = request.getSession();
            if (session != null) {

                String usernameLoggeado = (String) session.getAttribute("username");

                if (usernameLoggeado != null) {

                    ArrayList<User> usuarios = Logic.getUsersFromDB();
                    ArrayList<Reserva> reservas = Logic.getReservasFromDB();
                    ArrayList<Reserva> reservasUser = new ArrayList<>();
                    User newUser = new User();

                    for (int i = 0; i < usuarios.size(); i++) {

                        newUser = usuarios.get(i);

                        if (newUser.getUsername().equals(usernameLoggeado)) {

                            break;

                        }

                    }

                    Reserva newReserva = new Reserva();
                    for (int j = 0; j < reservas.size(); j++) {

                        newReserva = reservas.get(j);

                        if (newReserva.getUser_id() == newUser.getUser_id()) {

                            reservasUser.add(newReserva);
                        }

                    }

                    String reservasJson = new Gson().toJson(reservasUser);
                    Log.log.info("JSON => {}", reservasJson);
                    out.println(reservasJson);
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
