package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import database.ConnectionDDBB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.Log;

@WebServlet("/updateParking")
public class updateParking extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public updateParking() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json;charset=UTF-8");
        Log.log.info("-- Update Parking information --");

        Connection con = null;

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

            int id = json.optInt("id");
            String nombre = json.optString("nombre");
            String direccion = json.optString("direccion");
            String ciudad = json.optString("ciudad");
            String c_postal = json.optString("c_postal");
            int c_total = json.optInt("c_total");
            int disponibles = json.optInt("disponibles");

            // Validar el ID aquí antes de proceder
            ConnectionDDBB conector = new ConnectionDDBB();
            con = conector.obtainConnection(true);

            String sql = "UPDATE parkings SET nombre = ?, direccion = ?, ciudad = ?, codigo_postal = ?, capacidad_total = ?, plazas_disponibles = ? WHERE parking_id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            Log.log.info("Query => {}", statement);

            statement.setString(1, nombre);
            statement.setString(2, direccion);
            statement.setString(3, ciudad);
            statement.setString(4, c_postal);
            statement.setInt(5, c_total);
            statement.setInt(6, disponibles);
            statement.setInt(7, id);

            int result = statement.executeUpdate();
            if (result > 0) {
                Log.log.info("Parking actualizado con éxito.");
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                Log.log.error("No se encontró un parking con ese ID.");
            }
        } catch (NumberFormatException nfe) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Log.log.error("Number Format Exception: {}", nfe);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Log.log.error("Exception: {}", e);
            System.err.println("Exception: " + e);
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
