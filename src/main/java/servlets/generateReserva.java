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

    Random random = new Random();

    private static final long serialVersionUID = 1L;

    public generateReserva() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Log.log.info("--Generate Random Reserva--");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Random random = new Random();
        try {
            
            Date startDate = Date.valueOf("2024-05-31");
            Date endDate = Date.valueOf("2024-12-31");
            
            List<Integer> usersID = Logic.getUsersID();
            List<Integer> parkingsID = Logic.getParkingsID();
            
            
            int user_id = usersID.get(random.nextInt(usersID.size()));
            int parking_id = parkingsID.get(random.nextInt(parkingsID.size()));
            Date fecha_reserva = GeneradorDatos.generarFecha(startDate, endDate);
            Timestamp hora_inicio = GeneradorDatos.generarHoraAleatoria(fecha_reserva);
            Timestamp hora_fin = GeneradorDatos.generarHoraFin(hora_inicio);
            
            
            ConnectionDDBB conector = new ConnectionDDBB();
            Connection con = conector.obtainConnection(true);

            String sql = "INSERT INTO reservas(user_id, parking_id, fecha_reserva, hora_inicio, hora_fin,id_plaza) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = con.prepareStatement(sql);
            Log.log.info("Query => {}", statement);

            statement.setInt(1, user_id);
            statement.setInt(2, parking_id);
            statement.setDate(3, fecha_reserva);
            statement.setTimestamp(4, hora_inicio);
            statement.setTimestamp(5, hora_fin);      

            int result = statement.executeUpdate();

            if (result > 0) {
                Log.log.info("Reserva registrada con exito!");
                JSONObject json = new JSONObject();
                json.put("user_id", user_id);
                json.put("parking_id", parking_id);
                json.put("fecha_reserva", fecha_reserva);
                json.put("hora_inicio", hora_inicio);
                json.put("hora_fin", hora_fin);
                out.print(json.toString());
            } else {
                // Manejar el caso de que la inserción falle
                Log.log.error("Error en el registro de la reserva");
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
