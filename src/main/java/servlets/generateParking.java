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


@WebServlet("/generateParking")
public class generateParking extends HttpServlet {

    Random random = new Random();

    private static final long serialVersionUID = 1L;

    public generateParking() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Log.log.info("--Generate Random Parking--");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Random random = new Random();
        try {

            
            String ciudad = GeneradorDatos.generarLocalizacion();
            String nombre = GeneradorDatos.generarNombreParking();
            String direccion = GeneradorDatos.generarDireccion();
            String c_postal = GeneradorDatos.generarCodigoPostal();
            int c_total = random.nextInt(10)+1;
            int disponibles = random.nextInt(c_total)+1;
            
            ConnectionDDBB conector = new ConnectionDDBB();
            Connection con = conector.obtainConnection(true);

            String sql = "INSERT INTO parkings(nombre, direccion, ciudad, codigo_postal, capacidad_total, plazas_disponibles) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = con.prepareStatement(sql);
            Log.log.info("Query => {}", statement);

            statement.setString(1, nombre);
            statement.setString(2, direccion);
            statement.setString(3, ciudad);
            statement.setString(4, c_postal);
            statement.setInt(5, c_total);
            statement.setInt(6, disponibles);

            

            int result = statement.executeUpdate();

            if (result > 0) {
                Log.log.info("Maquina registrada con exito!");
                JSONObject json = new JSONObject();
                json.put("nombre", nombre);
                json.put("direccion", direccion);
                json.put("ciudad", ciudad);
                json.put("codigo_postal", c_postal);
                json.put("capacidad_total", c_total);
                json.put("plazas_disponibles", disponibles);
               
                
                
                out.print(json.toString());
            } else {
                // Manejar el caso de que la inserción falle
                Log.log.error("Error en el registro del parking");
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
