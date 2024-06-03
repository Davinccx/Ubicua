package servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import database.ConnectionDDBB;
import static java.lang.System.out;
import database.Admin;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import logic.Log;
import logic.Logic;


@WebServlet("/adminLogin")
public class adminLogin extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public adminLogin() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            ConnectionDDBB conector = new ConnectionDDBB();
            Connection con = conector.obtainConnection(true);
            response.setContentType("text/html;charset=UTF-8");
            Log.log.info("--Admin login function --");
            boolean logged = false;
            String posibleUser = request.getParameter("user");
            String posiblePassword = request.getParameter("password");
            String usernameLoggeado = "";

            ArrayList<Admin> usuarios = Logic.getAdminFromDB();

            for (int i = 0; i < usuarios.size(); i++) {

                Admin x = usuarios.get(i);
                String username = x.getUsername();
                String password = x.getPassword();

                if (posibleUser != null && username.equals(posibleUser) && posiblePassword != null && password.equals(posiblePassword)) {

                    logged = true;
                    usernameLoggeado = x.getUsername();
                    Log.log.info("Administrador loggeado correctamente {}", usernameLoggeado);
                }

            }
            if (logged) {

                HttpSession session = request.getSession();
                session.setAttribute("username", usernameLoggeado);
                response.sendRedirect("admin/adminDashboard.html");

            } else {

                response.sendRedirect("adminLogin.html?error=true");
                Log.log.error("No se ha podido iniciar sesión");

            }

        } catch (NumberFormatException nfe) {
            out.println("-1");
            Log.log.error("Number Format Exception: {}", nfe);
        } catch (IndexOutOfBoundsException iobe) {
            out.println("-1");
            Log.log.error("Index out of Bounds Exception: {}", iobe);
        } catch (Exception e) {
            out.println("-1");
            Log.log.error("Exception: {}", e);
        } finally {
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
            response.sendRedirect("adminLogin.html");
        }
    }

}
