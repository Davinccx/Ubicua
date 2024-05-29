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
import java.sql.PreparedStatement;
import logic.Log;
import java.util.Random;
import logic.Logic;

@WebServlet("/GenerateUser")
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

            String username = GeneradorDatos.generarUsername();
            String email = GeneradorDatos.generarEmail();
            String password = GeneradorDatos.generarPassword();
            String telefono = GeneradorDatos.generarTelefono();
            String token = Logic.generateToken();
            int saldo = random.nextInt(100) + 1;

            ConnectionDDBB conector = new ConnectionDDBB();
            Connection con = conector.obtainConnection(true);

            if (Logic.comprobarEmail(email)) {
                Log.log.error("El E-mail ya existe!");
            } else {
                String sql = "INSERT INTO users(email, password, username, telefono, saldo, token) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = con.prepareStatement(sql);
                Log.log.info("Query => {}", statement);

                statement.setString(1, email);
                statement.setString(2, password);
                statement.setString(3, username);
                statement.setString(4, telefono);
                statement.setDouble(5, saldo);
                statement.setString(6, token);

                int result = statement.executeUpdate();

                if (result > 0) {
                    Log.log.info("Usuario registrado con exito!");
                    JSONObject json = new JSONObject();
                    json.put("email", email);
                    json.put("password", password);
                    json.put("username", username);
                    json.put("telefono", telefono);
                    json.put("saldo", saldo);
                    json.put("token", token);
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
