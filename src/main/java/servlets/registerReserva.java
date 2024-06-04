package servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import database.ConnectionDDBB;
import jakarta.servlet.annotation.WebServlet;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import logic.Log;
import logic.Logic;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;


@WebServlet("/registerReserva")
public class registerReserva extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public registerReserva() {
        super();
    }
        
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");

        try {
            ConnectionDDBB conector = new ConnectionDDBB();
            Connection con = conector.obtainConnection(true);
            
            String username = request.getParameter("username");
            int user_id = Logic.getUsersIDFromUsername(username).get(0);
            int parking_id = Integer.parseInt(request.getParameter("parkingID"));
            
            String fecha= request.getParameter("date");
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            
            Date fecha_reserva = null;
            java.util.Date utilDate = formato.parse(fecha);
            fecha_reserva = new java.sql.Date(utilDate.getTime());
            
            int id_plaza = Integer.parseInt(request.getParameter("plaza"));
            String h_inicio = request.getParameter("time_inicio");
            String h_fin= request.getParameter("time_fin");
            
            SimpleDateFormat formatoFechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            
            String fechaHoraInicio = fecha + " " + h_inicio;
            java.util.Date utilFechaHoraInicio = formatoFechaHora.parse(fechaHoraInicio);
            Timestamp horaInicio = new Timestamp(utilFechaHoraInicio.getTime());
            
            String fechaHoraFin = fecha + " " + h_fin;
            java.util.Date utilFechaHoraFin = formatoFechaHora.parse(fechaHoraFin);
            Timestamp horaFin = new Timestamp(utilFechaHoraFin.getTime());
            
            String sql = "INSERT INTO reservas(user_id, parking_id, fecha_reserva, hora_inicio, hora_fin, id_plaza) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, user_id);
            statement.setInt(2, parking_id);
            statement.setDate(3, fecha_reserva);
            statement.setTimestamp(4, horaInicio);
            statement.setTimestamp(5, horaFin);
            statement.setInt(6, id_plaza);
            
            int result = statement.executeUpdate();
            if (result > 0) {
                    
                    Log.log.info("Reserva registrada con exito!");
                } else {
                    // Manejar el caso de que la inserción falle
                    Log.log.error("Error en la reserva");
                }
                         
            
        } catch (NumberFormatException nfe) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("Número inválido.");
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
