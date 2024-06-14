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

@WebServlet("/generateReserva")
public class generateReserva extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final Random random = new Random();

    public generateReserva() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Log.log.info("--Generate Random Reserva--");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        Connection con = null;
        PreparedStatement statementInsert = null;
        PreparedStatement statementUpdatePlaza = null;
        PreparedStatement statementUpdateParking = null;

        try {
            Date startDate = Date.valueOf("2024-05-31");
            Date endDate = Date.valueOf("2024-12-31");

            List<Integer> usersID = Logic.getUsersID();
            List<Integer> parkingsID = Logic.getParkingsID();

            int user_id = usersID.get(random.nextInt(usersID.size()));
            int parking_id = parkingsID.get(random.nextInt(parkingsID.size()));
            List<Integer> plazasID = Logic.getPlazaIDFromParking(parking_id);
            Date fecha_reserva = GeneradorDatos.generarFecha(startDate, endDate);
            Timestamp hora_inicio = GeneradorDatos.generarHoraAleatoria(fecha_reserva);
            Timestamp hora_fin = GeneradorDatos.generarHoraFin(hora_inicio);
            int plazaID = plazasID.get(random.nextInt(plazasID.size()));

            ConnectionDDBB conector = new ConnectionDDBB();
            con = conector.obtainConnection(true);

            // Inicia la transacción
            con.setAutoCommit(false);
            
            // Primera consulta: insertar la reserva
            String sqlInsert = "INSERT INTO reservas(user_id, parking_id, fecha_reserva, hora_inicio,hora_fin, id_plaza) VALUES (?, ?, ?, ?, ?, ?)";
            statementInsert = con.prepareStatement(sqlInsert);
            Log.log.info("Query Insert => {}", statementInsert);

            statementInsert.setInt(1, user_id);
            statementInsert.setInt(2, parking_id);
            statementInsert.setDate(3, fecha_reserva);
            statementInsert.setTimestamp(4, hora_inicio);
            statementInsert.setTimestamp(5, hora_fin);
            statementInsert.setInt(6, plazaID);

            int resultInsert = statementInsert.executeUpdate();

            // Segunda consulta: actualizar la plaza
            String sqlUpdatePlaza = "UPDATE plaza SET ocupado = 1 WHERE id_plaza = ?";
            statementUpdatePlaza = con.prepareStatement(sqlUpdatePlaza);
            Log.log.info("Query Update Plaza => {}", statementUpdatePlaza);

            statementUpdatePlaza.setInt(1, plazaID);

            int resultUpdatePlaza = statementUpdatePlaza.executeUpdate();
            
            // Tercera consulta: actualizar el parking
            String sqlUpdateParking = "UPDATE parkings SET plazas_disponibles = plazas_disponibles - 1 WHERE parking_id = ?";
            statementUpdateParking = con.prepareStatement(sqlUpdateParking);
            Log.log.info("Query Update Parking => {}", statementUpdateParking);

            statementUpdateParking.setInt(1, parking_id);

            int resultUpdateParking = statementUpdateParking.executeUpdate();

            // Confirma la transacción
            con.commit();

            if (resultInsert > 0 && resultUpdatePlaza > 0 && resultUpdateParking > 0) {
                Log.log.info("Reserva registrada con éxito!");
                JSONObject json = new JSONObject();
                json.put("user_id", user_id);
                json.put("parking_id", parking_id);
                json.put("fecha_reserva", fecha_reserva);
                json.put("hora_inicio", hora_inicio);
                json.put("hora_fin", hora_fin);
                json.put("plaza_id", plazaID);
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
            if (statementUpdatePlaza != null) {
                try {
                    statementUpdatePlaza.close();
                } catch (Exception e) {
                    Log.log.error("Error al cerrar el PreparedStatement: {}", e.getMessage());
                }
            }
            if (statementUpdateParking != null) {
                try {
                    statementUpdateParking.close();
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
