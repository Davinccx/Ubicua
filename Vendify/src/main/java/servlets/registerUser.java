package servlets;

import java.io.IOException;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import database.ConnectionDDBB;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import logic.Logic;

public class registerUser extends HttpServlet{
    
    
    private static final long serialVersionUID = 1L;

    public registerUser() {
        super();
    }
    
     protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        
        PrintWriter out = response.getWriter();

        try {
            ConnectionDDBB conector = new ConnectionDDBB();
            Connection con = conector.obtainConnection(true);
            response.setContentType("text/html;charset=UTF-8");

            String email = request.getParameter("email");
            String username = request.getParameter("user");
            String password = request.getParameter("password"); // Asegúrate de aplicar hashing a la contraseña antes de almacenarla
            String telephone = request.getParameter("telephone");
            double saldo = 0;

            // Comprobar si el email ya existe en la base de datos
            if (Logic.comprobarEmail(email)) {
                // Si el email ya existe, enviar al usuario a una página de error o mostrar un mensaje
                response.sendRedirect("register.html?error=true");
            } else {
                // Si el email no existe, insertar el nuevo usuario
                String sql = "INSERT INTO users(email, password, username, telefono, saldo) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement statement = con.prepareStatement(sql);
                statement.setString(1, email);
                statement.setString(2, password);
                statement.setString(3, username);
                statement.setString(4, telephone);
                statement.setDouble(5, saldo);

                int result = statement.executeUpdate();
                if (result > 0) {
                    response.sendRedirect("successRegister.html");
                } else {
                    // Manejar el caso de que la inserción falle
                    
                    response.sendRedirect("register.html?error=true");
                }
            }

        } catch (NumberFormatException nfe) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("Número inválido.");
            System.out.println("Number Format Exception: " + nfe);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("Error al procesar la solicitud.");
            System.out.println("Exception: " + e);
        } finally {
            out.close();
        }
    }
     
    

    
}
    
    

