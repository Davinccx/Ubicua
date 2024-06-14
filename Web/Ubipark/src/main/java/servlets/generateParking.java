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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import logic.Log;
import java.util.Random;

@WebServlet("/generateParking")
public class generateParking extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final Random random = new Random();

    public generateParking() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Log.log.info("--Generate Random Parking--");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        Connection con = null;
        PreparedStatement statementParking = null;
        PreparedStatement statementPlaza = null;

        try {
            String ciudad = GeneradorDatos.generarLocalizacion();
            String nombre = GeneradorDatos.generarNombreParking();
            String direccion = GeneradorDatos.generarDireccion();
            String c_postal = GeneradorDatos.generarCodigoPostal();
            int c_total = random.nextInt(10) + 1;
            int disponibles = random.nextInt(c_total) + 1;

            ConnectionDDBB conector = new ConnectionDDBB();
            con = conector.obtainConnection(true);

            // Inicia la transacción
            con.setAutoCommit(false);

            // Inserta el nuevo parking
            String sqlParking = "INSERT INTO parkings(nombre, direccion, ciudad, codigo_postal, capacidad_total, plazas_disponibles) VALUES (?, ?, ?, ?, ?, ?)";
            statementParking = con.prepareStatement(sqlParking, Statement.RETURN_GENERATED_KEYS);
            Log.log.info("Query Parking => {}", statementParking);

            statementParking.setString(1, nombre);
            statementParking.setString(2, direccion);
            statementParking.setString(3, ciudad);
            statementParking.setString(4, c_postal);
            statementParking.setInt(5, c_total);
            statementParking.setInt(6, disponibles);

            int resultParking = statementParking.executeUpdate();

            if (resultParking > 0) {
                // Obtén el ID del parking recién insertado
                ResultSet generatedKeys = statementParking.getGeneratedKeys();
                int parking_id = -1;
                if (generatedKeys.next()) {
                    parking_id = generatedKeys.getInt(1);
                }

                // Inserta las plazas asociadas al parking
                String sqlPlaza = "INSERT INTO plaza(id_parking, ocupado) VALUES (?, ?)";
                statementPlaza = con.prepareStatement(sqlPlaza);
                Log.log.info("Query Plaza => {}", statementPlaza);

                // Inserta plazas ocupadas
                int ocupadas = c_total - disponibles;
                for (int i = 0; i < ocupadas; i++) {
                    statementPlaza.setInt(1, parking_id);
                    statementPlaza.setInt(2, 1); // 1 indica ocupado
                    statementPlaza.addBatch();
                }

                // Inserta plazas disponibles
                for (int i = 0; i < disponibles; i++) {
                    statementPlaza.setInt(1, parking_id);
                    statementPlaza.setInt(2, 0); // 0 indica libre
                    statementPlaza.addBatch();
                }

                int[] resultPlaza = statementPlaza.executeBatch();

                // Confirma la transacción
                con.commit();

                Log.log.info("Parking y plazas registradas con éxito!");
                JSONObject json = new JSONObject();
                json.put("nombre", nombre);
                json.put("direccion", direccion);
                json.put("ciudad", ciudad);
                json.put("codigo_postal", c_postal);
                json.put("capacidad_total", c_total);
                json.put("plazas_disponibles", disponibles);
                out.print(json.toString());
            } else {
                Log.log.error("Error en el registro del parking");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\":\"Error al registrar el parking\"}");
            }

        } catch (NumberFormatException nfe) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Número inválido\"}");
            Log.log.error("Number Format Exception: {}", nfe.getMessage());
        } catch (Exception e) {
            // Rollback en caso de error
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackException) {
                    Log.log.error("Error al hacer rollback: {}", rollbackException.getMessage());
                }
            }
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"Error interno del servidor\"}");
            Log.log.error("Error interno del servidor: {}", e.getMessage());
        } finally {
            if (statementPlaza != null) {
                try {
                    statementPlaza.close();
                } catch (SQLException e) {
                    Log.log.error("Error al cerrar el PreparedStatement de plazas: {}", e.getMessage());
                }
            }
            if (statementParking != null) {
                try {
                    statementParking.close();
                } catch (SQLException e) {
                    Log.log.error("Error al cerrar el PreparedStatement de parking: {}", e.getMessage());
                }
            }
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    Log.log.error("Error al cerrar la conexión: {}", e.getMessage());
                }
            }
            out.close();
        }
    }
}
