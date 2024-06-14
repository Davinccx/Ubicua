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
        Connection con = null;
        PreparedStatement statement = null;

        try {
            String nombre = GeneradorDatos.generarNombre();
            String apellido = GeneradorDatos.generarApellido();
            String username = GeneradorDatos.generarUsername();
            String email = GeneradorDatos.generarEmail();
            String password = GeneradorDatos.generarPassword();
            String telefono = GeneradorDatos.generarTelefono();
            long millis = System.currentTimeMillis();
            Date fechaActual = new Date(millis);
            String matricula = Logic.generarMatricula();

            ConnectionDDBB conector = new ConnectionDDBB();
            con = conector.obtainConnection(true);

            if (Logic.comprobarEmail(email)) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                JSONObject errorJson = new JSONObject();
                errorJson.put("error", "El E-mail ya existe");
                out.print(errorJson.toString());
                Log.log.error("El E-mail ya existe!");
            } else {
                String sql = "INSERT INTO users(nombre, apellido, email, password, telefono, fecha_registro, matricula, username) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                statement = con.prepareStatement(sql);
                Log.log.info("Query => {}", statement);

                statement.setString(1, nombre);
                statement.setString(2, apellido);
                statement.setString(3, email);
                statement.setString(4, password);
                statement.setString(5, telefono);
                statement.setDate(6, fechaActual);
                statement.setString(7, matricula);
                statement.setString(8, username);

                int result = statement.executeUpdate();

                if (result > 0) {
                    Log.log.info("Usuario registrado con éxito!");
                    JSONObject json = new JSONObject();
                    json.put("nombre", nombre);
                    json.put("apellido", apellido);
                    json.put("email", email);
                    json.put("password", password);
                    json.put("telefono", telefono);
                    json.put("fecha_registro", fechaActual);
                    json.put("matricula", matricula);
                    json.put("username", username);
                    out.print(json.toString());
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    JSONObject errorJson = new JSONObject();
                    errorJson.put("error", "Error al registrar el usuario");
                    out.print(errorJson.toString());
                    Log.log.error("Error en el registro");
                }
            }
        } catch (NumberFormatException nfe) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject errorJson = new JSONObject();
            errorJson.put("error", "Número inválido");
            out.print(errorJson.toString());
            Log.log.error("Number Format Exception: {}", nfe);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject errorJson = new JSONObject();
            errorJson.put("error", "Error interno del servidor");
            out.print(errorJson.toString());
            Log.log.error("Error interno del servidor: {}", e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (Exception e) {
                    Log.log.error("Error al cerrar el PreparedStatement: {}", e.getMessage());
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    Log.log.error("Error al cerrar la conexión: {}", e.getMessage());
                }
            }
            out.close();
        }
    }
}
