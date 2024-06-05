package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import database.ConnectionDDBB;
import logic.Log;
import logic.Logic;

@WebServlet("/registerReserva")
public class registerReserva extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public registerReserva() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Read the JSON data from the request
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            // Parse the JSON data
            JSONObject json = new JSONObject(sb.toString());

            // Extract values from JSON
            String username = json.getString("username");
            int parking_id = json.getInt("parkingID");
            String dateStr = json.getString("date");
            String time_inicio = json.getString("time_inicio");
            String time_fin = json.getString("time_fin");
            int plaza = json.getInt("plaza");
            int user_id = Logic.getUserFromUsername(username).getUser_id(); // Implement this method in your Logic class
            
            // Parse date and time
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = formato.parse(dateStr);
            Date fecha_reserva = new Date(utilDate.getTime());

            SimpleDateFormat formatoFechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            String fechaHoraInicio = dateStr + " " + time_inicio;
            java.util.Date utilFechaHoraInicio = formatoFechaHora.parse(fechaHoraInicio);
            Timestamp horaInicio = new Timestamp(utilFechaHoraInicio.getTime());

            String fechaHoraFin = dateStr + " " + time_fin;
            java.util.Date utilFechaHoraFin = formatoFechaHora.parse(fechaHoraFin);
            Timestamp horaFin = new Timestamp(utilFechaHoraFin.getTime());

            // Database insertion logic
            ConnectionDDBB conector = new ConnectionDDBB();
            Connection con = conector.obtainConnection(true);
            try {
                con.setAutoCommit(false); // Start transaction

                // Update plaza to occupied
                String updatePlazaSql = "UPDATE plaza SET ocupado = 1 WHERE id_plaza = ?";
                try (PreparedStatement updatePlazaStmt = con.prepareStatement(updatePlazaSql)) {
                    updatePlazaStmt.setInt(1, plaza);
                    int updatePlazaResult = updatePlazaStmt.executeUpdate();
                    if (updatePlazaResult == 0) {
                        throw new Exception("Failed to update plaza status");
                    }
                }

                // Reduce available spaces in parking
                String updateParkingSql = "UPDATE parkings SET plazas_disponibles = plazas_disponibles - 1 WHERE parking_id = ?";
                try (PreparedStatement updateParkingStmt = con.prepareStatement(updateParkingSql)) {
                    updateParkingStmt.setInt(1, parking_id);
                    int updateParkingResult = updateParkingStmt.executeUpdate();
                    if (updateParkingResult == 0) {
                        throw new Exception("Failed to update parking availability");
                    }
                }

                // Register reservation
                String insertReservaSql = "INSERT INTO reservas(user_id, parking_id, fecha_reserva, hora_inicio, hora_fin, id_plaza) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement insertReservaStmt = con.prepareStatement(insertReservaSql)) {
                    insertReservaStmt.setInt(1, user_id);
                    insertReservaStmt.setInt(2, parking_id);
                    insertReservaStmt.setDate(3, fecha_reserva);
                    insertReservaStmt.setTimestamp(4, horaInicio);
                    insertReservaStmt.setTimestamp(5, horaFin);
                    insertReservaStmt.setInt(6, plaza);

                    int insertReservaResult = insertReservaStmt.executeUpdate();
                    if (insertReservaResult == 0) {
                        throw new Exception("Failed to register reservation");
                    }
                }

                con.commit(); // Commit transaction
                Log.log.info("Reserva registrada con éxito!");
                response.setStatus(HttpServletResponse.SC_OK);
                out.println("{\"message\":\"Reserva registrada con éxito\"}");

            } catch (Exception e) {
                if (con != null) {
                    try {
                        con.rollback(); // Rollback transaction on error
                    } catch (Exception ex) {
                        Log.log.error("Error during rollback: {}", ex);
                    }
                }
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.println("{\"message\":\"Error al procesar la solicitud\"}");
                Log.log.error("Error al procesar la solicitud: {}", e);
            } finally {
                if (con != null) {
                    try {
                        con.setAutoCommit(true);
                        con.close();
                    } catch (Exception ex) {
                        Log.log.error("Error closing connection: {}", ex);
                    }
                }
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("{\"message\":\"Error al procesar la solicitud\"}");
            Log.log.error("Error al procesar la solicitud: {}", e);
        } finally {
            out.close();
        }
    }
}
