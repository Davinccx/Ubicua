package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import database.ConnectionDDBB;
import logic.Log;
import logic.Logic;

@WebServlet("/registerUser")
public class registerUser extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public registerUser() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");

        Connection con = null; // Declare connection outside try-catch block

        try {
            ConnectionDDBB conector = new ConnectionDDBB();
            con = conector.obtainConnection(true);

            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String username = request.getParameter("user");
            String email = request.getParameter("email");
            String password = request.getParameter("password"); // Ensure to hash the password before storing it
            String telephone = request.getParameter("telephone");
            long millis = System.currentTimeMillis();
            Date fechaActual = new Date(millis);
            String matricula = request.getParameter("matricula");

            // Check if the email already exists in the database
            if (Logic.comprobarEmail(email)) {
                // If the email already exists, redirect the user to an error page or display a message
                response.sendRedirect("register.html?error=true");
                Log.log.error("El E-mail ya existe!");
            } else {
                // If the email does not exist, insert the new user
                String sql = "INSERT INTO users(nombre, apellido, email, password, telefono, fecha_registro, matricula, username) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = con.prepareStatement(sql);
                statement.setString(1, nombre);
                statement.setString(2, apellido);
                statement.setString(3, email);
                statement.setString(4, password);
                statement.setString(5, telephone);
                statement.setDate(6, fechaActual);
                statement.setString(7, matricula);
                statement.setString(8, username);

                int result = statement.executeUpdate();
                if (result > 0) {
                    response.sendRedirect("successRegister.html");
                    Log.log.info("Usuario registrado con éxito!");
                } else {
                    // Handle the case where the insertion fails
                    response.sendRedirect("register.html?error=true");
                    Log.log.error("Error en el registro");
                }
            }

        } catch (NumberFormatException nfe) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("Número inválido.");
            Log.log.error("Number Format Exception: {}", nfe);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("Error al procesar la solicitud.");
            Log.log.error("Exception: {}", e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    Log.log.error("Error al cerrar la conexión: {}", e);
                }
            }
            if (out != null) {
                out.close();
            }
        }
    }
}
