package servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import database.ConnectionDDBB;
import jakarta.servlet.annotation.WebServlet;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import logic.Log;
import org.json.JSONObject;

@WebServlet("/UpdateVenta")
public class updateVenta extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public updateVenta() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json;charset=UTF-8");
        Log.log.info("-- Update Venta information --");

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
            String id_usuario = json.optString("id_usuario");
            String id_producto = json.optString("id_producto");
            String id_maquina = json.optString("id_maquina");
       

            // Validar el ID aquí antes de proceder
            ConnectionDDBB conector = new ConnectionDDBB();
            Connection con = conector.obtainConnection(true);

            String sql = "UPDATE ventas SET id_usuario=?, id_producto=?, id_maquina=? WHERE id=?";
            PreparedStatement statement = con.prepareStatement(sql);
            Log.log.info("Query=> {}", statement);

            statement.setInt(1, Integer.parseInt(id_usuario));
            statement.setInt(2, Integer.parseInt(id_producto));
            statement.setInt(3, Integer.parseInt(id_maquina));
            statement.setInt(4, Integer.parseInt(id));

            int result = statement.executeUpdate();
            if (result > 0) {
                Log.log.info("Venta actualizado con éxito.");
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                Log.log.error("No se encontró una venta con ese ID.");
            }
        } catch (NumberFormatException nfe) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
