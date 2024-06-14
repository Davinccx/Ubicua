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
import java.sql.SQLException;
import logic.Log;

@WebServlet("/generatePlaza")
public class generatePlazas extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public generatePlazas() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Log.log.info("--Generate Random Plaza--");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        Connection con = null;
        PreparedStatement statement = null;

        try {
            int id_parking = Integer.parseInt(request.getParameter("parking_id"));
            int capacidad = Integer.parseInt(request.getParameter("capacidad"));
            int disponibles = Integer.parseInt(request.getParameter("disponibles"));

            if (disponibles > capacidad) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"El número de plazas disponibles no puede ser mayor que la capacidad total\"}");
                Log.log.error("El número de plazas disponibles no puede ser mayor que la capacidad total");
                return;
            }

            ConnectionDDBB conector = new ConnectionDDBB();
            con = conector.obtainConnection(true);

            String sql = "INSERT INTO plaza(id_parking, ocupado) VALUES (?, ?)";
            statement = con.prepareStatement(sql);

            for (int i = 0; i < capacidad; i++) {
                int ocupado = (i < capacidad - disponibles) ? 1 : 0; // Primero ocupa las plazas hasta completar la capacidad - disponibles
                statement.setInt(1, id_parking);
                statement.setInt(2, ocupado);
                statement.addBatch();
            }

            int[] result = statement.executeBatch();

            if (result.length != capacidad) {
                // Manejar el caso de que la inserción falle
                Log.log.error("Error en el registro de las plazas");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\":\"Error al generar las plazas\"}");
                return;
            }

            // JSON de respuesta si todas las inserciones fueron exitosas
            Log.log.info("Plazas registradas con éxito!");
            JSONObject json = new JSONObject();
            json.put("parking_id", id_parking);
            json.put("capacidad", capacidad);
            json.put("disponibles", disponibles);
            out.print(json.toString());

        } catch (NumberFormatException nfe) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Número inválido\"}");
            Log.log.error("Número inválido: {}", nfe.getMessage());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"Error interno del servidor\"}");
            Log.log.error("Error interno del servidor: {}", e.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    Log.log.error("Error al cerrar el PreparedStatement: {}", e.getMessage());
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    Log.log.error("Error al cerrar la conexión: {}", e.getMessage());
                }
            }
            out.close();
        }
    }
}
