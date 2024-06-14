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
import java.sql.Timestamp;
import java.util.List;
import logic.Log;
import java.util.Random;
import logic.Logic;

@WebServlet("/generateHistorico")
public class generateHistorico extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final Random random = new Random();

    public generateHistorico() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Log.log.info("--Generate Random Reserva--");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        Connection con = null;
        PreparedStatement statementInsert = null;
       

        try {
            Date startDate = Date.valueOf("2024-05-31");
            Date endDate = Date.valueOf("2024-12-31");

            List<Integer> usersID = Logic.getUsersID();
            List<Integer> parkingsID = Logic.getParkingsID();

            String matricula = Logic.generarMatricula();
            int parking_id = parkingsID.get(random.nextInt(parkingsID.size()));
            List<Integer> plazasID = Logic.getPlazaIDFromParking(parking_id);
            Date fecha_reserva = GeneradorDatos.generarFecha(startDate, endDate);
            Timestamp hora_inicio = GeneradorDatos.generarHoraAleatoria(fecha_reserva);
            Timestamp hora_fin = GeneradorDatos.generarHoraFin(hora_inicio);
            int importe = random.nextInt(100)+1;

            ConnectionDDBB conector = new ConnectionDDBB();
            con = conector.obtainConnection(true);

            // Inicia la transacción
            con.setAutoCommit(false);
            
            // Primera consulta: insertar la reserva
            String sqlInsert = "INSERT INTO historico(matricula, id_parking, hora_inicio, hora_fin, importe) VALUES (?, ?, ?, ?, ?)";
            statementInsert = con.prepareStatement(sqlInsert);
            Log.log.info("Query Insert => {}", statementInsert);

            statementInsert.setString(1, matricula);
            statementInsert.setInt(2, parking_id);
            statementInsert.setTimestamp(3, hora_inicio);
            statementInsert.setTimestamp(4, hora_fin);
            statementInsert.setInt(5, importe);

            int resultInsert = statementInsert.executeUpdate();

            if (resultInsert > 0) {
                Log.log.info("Historico registradp con éxito!");
                JSONObject json = new JSONObject();
                json.put("matricula", matricula);
                json.put("parking_id", parking_id);
                json.put("hora_inicio", hora_inicio);
                json.put("hora_fin", hora_fin);
                json.put("importe", importe);
                out.print(json.toString());
            } else {
                Log.log.error("Error en el registro de la reserva");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\":\"Error al registrar la reserva\"}");
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
                } catch (Exception rollbackException) {
                    Log.log.error("Error al hacer rollback: {}", rollbackException.getMessage());
                }
            }
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"Error interno del servidor\"}");
            Log.log.error("Error interno del servidor: {}", e.getMessage());
        } finally {
            if (statementInsert != null) {
                try {
                    statementInsert.close();
                } catch (Exception e) {
                    Log.log.error("Error al cerrar el PreparedStatement: {}", e.getMessage());
                }
            }
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (Exception e) {
                    Log.log.error("Error al cerrar la conexión: {}", e.getMessage());
                }
            }
            out.close();
        }
    }
}
