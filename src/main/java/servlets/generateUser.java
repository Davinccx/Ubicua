package servlets;

import database.ConnectionDDBB;
import java.io.IOException;
import java.io.PrintWriter;
import org.json.JSONObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import logic.Log;
import java.util.Random;
import logic.Logic;

@WebServlet("/generateUser")
public class generateUser extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public generateUser() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Log.log.info("--Generate Random Data--");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Random random = new Random();
        try {

            String nombre = GeneradorDatos.generarNombre();
            String apellido = GeneradorDatos.generarApellido();
            String username = GeneradorDatos.generarUsername();
            String email = GeneradorDatos.generarEmail();
            String password = GeneradorDatos.generarPassword();
            String telefono = GeneradorDatos.generarTelefono();
            long millis = System.currentTimeMillis();
            Date fechaActual = new Date(millis);
            String token = Logic.generateToken();

            ConnectionDDBB conector = new ConnectionDDBB();
            Connection con = conector.obtainConnection(true);

            if (Logic.comprobarEmail(email)) {
                Log.log.error("El E-mail ya existe!");
            } else {
                String sql = "INSERT INTO users(nombre, apellido, email, password, telefono, fecha_registro, token, username) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = con.prepareStatement(sql);
                Log.log.info("Query => {}", statement);

                statement.setString(1, nombre);
                statement.setString(2, apellido);
                statement.setString(3, email);
                statement.setString(4, password);
                statement.setString(5, telefono);
                statement.setDate(6, fechaActual);
                statement.setString(7, token);
                statement.setString(8, username);

                int result = statement.executeUpdate();

                if (result > 0) {
                    Log.log.info("Usuario registrado con exito!");
                    JSONObject json = new JSONObject();
                    json.put("nombre", nombre);
                    json.put("apellido", apellido);
                    json.put("email", email);
                    json.put("password", password);
                    json.put("telefono", telefono);
                    json.put("fecha_registro", fechaActual);
                    json.put("token", token);
                    json.put("username",username);
                    out.print(json.toString());
                } else {
                    // Manejar el caso de que la inserción falle
                    Log.log.error("Error en el registro");
                }
            }
        } catch (NumberFormatException nfe) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("Número inválido.");
            out.print("{\"error\":\"Error al generar datos\"}");
            Log.log.error("Number Format Exception: {}", nfe);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("Error al procesar la solicitud.");
            Log.log.error("Number Format Exception: {}", e);
        } finally {
            out.close();
        }

    }
}
