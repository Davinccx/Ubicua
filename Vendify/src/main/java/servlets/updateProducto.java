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

public class updateProducto extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public updateProducto() {
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
            String nombre = json.optString("nombre");
            String precio = json.optString("precio");
            String descripcion = json.optString("descripcion");
            String id_maquina = json.optString("id_maquina");

            // Validar el ID aquí antes de proceder
            ConnectionDDBB conector = new ConnectionDDBB();
            Connection con = conector.obtainConnection(true);

            String sql = "UPDATE productos SET nombre=?, precio=?, descripcion=?, id_maquina=? WHERE id=?";
            PreparedStatement statement = con.prepareStatement(sql);
            Log.log.info("Query=> {}", statement);

            statement.setString(1, nombre);
            statement.setString(2, precio);
            statement.setString(3, descripcion);
            statement.setInt(4, Integer.parseInt(id_maquina));
            statement.setInt(5, Integer.parseInt(id));

            int result = statement.executeUpdate();
            if (result > 0) {
                Log.log.info("Producto actualizado con éxito.");
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                Log.log.error("No se encontró un producto con ese ID.");
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
