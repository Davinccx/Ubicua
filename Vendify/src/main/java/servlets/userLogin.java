package servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import database.ConnectionDDBB;
import static java.lang.System.out;
import database.User;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import logic.Logic;

public class userLogin extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public userLogin() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            ConnectionDDBB conector = new ConnectionDDBB();
            Connection con = conector.obtainConnection(true);
            response.setContentType("text/html;charset=UTF-8");

            boolean logged = false;
            String posibleEmail = request.getParameter("email");
            String posiblePassword = request.getParameter("password");
            String usernameLoggeado = "";

            ArrayList<User> usuarios = Logic.getUsersFromDB();

            for (int i = 0; i < usuarios.size(); i++) {

                User x = usuarios.get(i);
                String email = x.getEmail();
                String password = x.getPassword();
                
                

                if (email != null && email.equals(posibleEmail) && posiblePassword != null && password.equals(posiblePassword)) {

                    logged = true;
                    usernameLoggeado = x.getUsername();
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
