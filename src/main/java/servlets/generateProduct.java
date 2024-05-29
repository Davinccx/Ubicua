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
import java.util.List;
import logic.Log;
import java.util.Random;
import logic.Logic;

@WebServlet("/GenerateProduct")
public class generateProduct extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public generateProduct() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Log.log.info("--Generate Random Product--");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Random random = new Random();
        try {

            String nombre = GeneradorDatos.generarNombreProducto();
            String descripcion = GeneradorDatos.generarDesc();
            int precio = GeneradorDatos.generarPrecio();
            List<Integer> machinesID = Logic.getMaquinasID();
            int id_maquina = machinesID.get(random.nextInt(machinesID.size())+1);
            
            ConnectionDDBB conector = new ConnectionDDBB();
            Connection con = conector.obtainConnection(true);

          
                String sql = "INSERT INTO productos(nombre, precio, descripcion, id_maquina) VALUES (?, ?, ?, ?)";
                PreparedStatement statement = con.prepareStatement(sql);
                Log.log.info("Query => {}", statement);

                statement.setString(1, nombre);
                statement.setInt(2, precio);
                statement.setString(3, descripcion);
                statement.setInt(4, id_maquina);
                
                int result = statement.executeUpdate();

                if (result > 0) {
                    Log.log.info("Producto registrado con exito!");
                    JSONObject json = new JSONObject();
                    json.put("nombre", nombre);
                    json.put("descripcion", descripcion);
                    json.put("precio", precio);
                    json.put("id_maquina", id_maquina);
                    out.print(json.toString());
                } else {
                    // Manejar el caso de que la inserción falle
                    Log.log.error("Error en el registro");
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
