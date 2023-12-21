package servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import database.ConnectionDDBB;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import logic.Log;
import org.json.JSONObject;

public class updateUser extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public updateUser() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json;charset=UTF-8");
        Log.log.info("-- Update Product information --");

        try {
            StringBuilder buffer = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
            } catch (IOException e) {
                throw new ServletException("Error reading request body", e);
            }

            String data = buffer.toString();
            JSONObject json = new JSONObject(data);

            String id = json.optString("id");
            String username = json.optString("username");
            String password = json.optString("password");
            String telephone = json.optString("telephone");
            String saldo = json.optString("saldo");

            // Validar el ID aquí antes de proceder
            ConnectionDDBB conector = new ConnectionDDBB();
            Connection con = conector.obtainConnection(true);

            String sql = "UPDATE users SET password=?, username=?, telefono=?, saldo=? WHERE id=?";
            PreparedStatement statement = con.prepareStatement(sql);
            Log.log.info("Query=> {}", statement);

            statement.setString(1, password);
            statement.setString(2, username);
            statement.setString(3, telephone);
            statement.setInt(4, Integer.parseInt(saldo));
            statement.setInt(5, Integer.parseInt(id));

            int result = statement.executeUpdate();
            if (result > 0) {
                Log.log.info("Usuario actualizado con éxito.");
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                Log.log.error("No se encontró un usuario con ese ID.");
            }
        } catch (NumberFormatException nfe) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Log.log.error("Number Format Exception: {}", nfe);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Log.log.error("Number Format Exception: {}", e);
            System.err.println("Exception: " + e);
        } finally {
            out.close();
        }
    }
}
