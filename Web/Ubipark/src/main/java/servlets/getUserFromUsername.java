package servlets;

import com.google.gson.Gson;
import database.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import logic.Log;
import logic.Logic;

@WebServlet("/getUserFromUsername")
public class getUserFromUsername extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public getUserFromUsername() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Log.log.info("-- Get User from username information from DB--");
        response.setContentType("application/json;charset=UTF-8");

        PrintWriter out = null;

        try {
            out = response.getWriter();

            HttpSession session = request.getSession(false); // Use false to avoid creating a new session if one doesn't exist
            if (session != null) {

                String usernameLoggeado = (String) session.getAttribute("username");

                if (usernameLoggeado != null) {

                    ArrayList<User> usuarios = Logic.getUsersFromDB();
                    User foundUser = null;

                    for (User user : usuarios) {
                        if (user.getUsername().equals(usernameLoggeado)) {
                            foundUser = user;
                            break;
                        }
                    }

                    if (foundUser != null) {
                        String jsonUser = new Gson().toJson(foundUser);
                        Log.log.info("JSON => {}", jsonUser);
                        out.println(jsonUser);
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Usuario no encontrado.");
                        Log.log.error("Usuario no encontrado");
                    }

                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario no logueado.");
                    Log.log.error("Usuario no logueado");
                }

            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Sesión no iniciada.");
                Log.log.error("Sesión no iniciada");
            }

        } catch (NumberFormatException nfe) {
            Log.log.error("Number Format Exception: {}", nfe);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            if (out != null) {
                out.println("-1");
            }
        } catch (IndexOutOfBoundsException iobe) {
            Log.log.error("Index out of bound exception: {}", iobe);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            if (out != null) {
                out.println("-1");
            }
        } catch (Exception e) {
            Log.log.error("Exception: {}", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            if (out != null) {
                out.println("-1");
            }
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
