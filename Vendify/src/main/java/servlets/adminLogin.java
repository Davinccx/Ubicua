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
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import logic.Logic;

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

            boolean logged = false;
            String posibleUser = request.getParameter("user");
            String posiblePassword = request.getParameter("password");
            String usernameLoggeado = "";

            ArrayList<Admin> usuarios = Logic.getAdminFromDB();

            for (int i = 0; i < usuarios.size(); i++) {

                Admin x = usuarios.get(i);
                String username = x.getUser();
                String password = x.getPassword();
                
                

                if (posibleUser != null && username.equals(posibleUser) && posiblePassword != null && password.equals(posiblePassword)) {

                    logged = true;
                    usernameLoggeado = x.getUser();
                }

            }
            if (logged) {
                
                HttpSession session = request.getSession();
                session.setAttribute("username",usernameLoggeado );
                response.sendRedirect("userdashboard.html");

            } else {

                response.sendRedirect("register.html");

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
