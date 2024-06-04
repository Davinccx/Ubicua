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

            String sql = "INSERT INTO reservas(user_id, parking_id, fecha_reserva, hora_inicio, hora_fin, id_plaza) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = con.prepareStatement(sql);
            Log.log.info("Query => {}", statement);

            statement.setInt(1, user_id);
            statement.setInt(2, parking_id);
            statement.setDate(3, fecha_reserva);
            statement.setTimestamp(4, hora_inicio);
            statement.setTimestamp(5, hora_fin);
            statement.setInt(6, plazaID);

            int result = statement.executeUpdate();

            if (result > 0) {
                Log.log.info("Reserva registrada con exito!");
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
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"Error interno del servidor\"}");
            Log.log.error("Error interno del servidor: {}", e.getMessage());
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    Log.log.error("Error al cerrar la conexión: {}", e.getMessage());
                }
            }
            out.close();
        }
    }
}
