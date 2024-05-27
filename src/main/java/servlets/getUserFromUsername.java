package servlets;

import com.google.gson.Gson;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import database.User;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.ArrayList;
import logic.Log;
import logic.Logic;

public class getUserFromUsername extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public getUserFromUsername() {
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

                    for (int i = 0; i < usuarios.size(); i++) {

                        User newUser = usuarios.get(i);

                        if (newUser.getUsername().equals(usernameLoggeado)) {

                            String jsonUser = new Gson().toJson(newUser);
                            Log.log.info("JSON => {}", jsonUser);
                            out.println(jsonUser);
                            out.close();

                        }

                    }

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
