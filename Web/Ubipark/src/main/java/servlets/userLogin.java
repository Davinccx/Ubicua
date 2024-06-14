package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import database.ConnectionDDBB;
import database.User;
import logic.Log;
import logic.Logic;

@WebServlet("/userLogin")
public class userLogin extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public userLogin() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Log.log.info("-- User login function --");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        Connection con = null;

        try {
            ConnectionDDBB conector = new ConnectionDDBB();
            con = conector.obtainConnection(true);

            boolean logged = false;
            String posibleEmail = request.getParameter("email");
            String posiblePassword = request.getParameter("password");
            String usernameLoggeado = "";
            User userLog = new User();

            ArrayList<User> usuarios = Logic.getUsersFromDB();

            for (int i = 0; i < usuarios.size(); i++) {
                User x = usuarios.get(i);
                String email = x.getEmail();
                String password = x.getPassword();

                if (email != null && email.equals(posibleEmail) && posiblePassword != null && password.equals(posiblePassword)) {
                    logged = true;
                    usernameLoggeado = x.getUsername();
                    userLog = x;
                }
            }

            if (logged) {
                HttpSession session = request.getSession();
                session.setAttribute("username", usernameLoggeado);
                Log.log.info("Usuario {} loggeado correctamente", usernameLoggeado);
                response.sendRedirect("userdashboard.html");
            } else {
                response.sendRedirect("login.html?error=true");
                Log.log.error("Error al iniciar sesión como {}", posibleEmail);
            }
        } catch (NumberFormatException nfe) {
            Log.log.error("Number Format Exception: {}", nfe);
        } catch (IndexOutOfBoundsException iobe) {
            Log.log.error("Index out of bounds Exception: {}", iobe);
        } catch (Exception e) {
            out.println("Error al procesar la solicitud.");
            Log.log.error("Number Format Exception: {}", e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    Log.log.error("Error al cerrar la conexión: {}", e);
                }
            }
            out.close();
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("username") != null) {
            String username = (String) session.getAttribute("username");
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(username);
        } else {
            response.sendRedirect("login.html");
        }
    }
}
