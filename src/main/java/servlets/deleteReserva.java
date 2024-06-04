package servlets;

import database.ConnectionDDBB;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import logic.Log;

@WebServlet("/deleteReserva")
public class deleteReserva extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public deleteReserva() {
        super();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Log.log.info("--Delete Reserva--");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String reservaIdParam = request.getParameter("reserva_id");
        String parkingIdParam = request.getParameter("parking_id");
        String plazaIdParam = request.getParameter("plaza_id");

        if (reservaIdParam == null || parkingIdParam == null || plazaIdParam == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Missing parameters\"}");
            return;
        }

        int reserva_id = Integer.parseInt(reservaIdParam);
        int parking_id = Integer.parseInt(parkingIdParam);
        int plaza_id = Integer.parseInt(plazaIdParam);

        Connection con = null;

        try {
            ConnectionDDBB conector = new ConnectionDDBB();
            con = conector.obtainConnection(true);

            // Inicia la transacción
            con.setAutoCommit(false);

            // Primera consulta: eliminar la reserva
            String sqlDelete = "DELETE FROM reservas WHERE reserva_id = ?";
            PreparedStatement statementDelete = con.prepareStatement(sqlDelete);
            Log.log.info("Query Delete => {}", statementDelete);

            statementDelete.setInt(1, reserva_id);

            int resultDelete = statementDelete.executeUpdate();

            // Segunda consulta: actualizar la plaza
            String sqlUpdatePlaza = "UPDATE plaza SET ocupado = 0 WHERE id_plaza = ?";
            PreparedStatement statementUpdatePlaza = con.prepareStatement(sqlUpdatePlaza);
            Log.log.info("Query Update Plaza => {}", statementUpdatePlaza);

            statementUpdatePlaza.setInt(1, plaza_id);

            int resultUpdatePlaza = statementUpdatePlaza.executeUpdate();

            // Tercera consulta: actualizar el parking
            String sqlUpdateParking = "UPDATE parkings SET plazas_disponibles = plazas_disponibles + 1 WHERE parking_id = ?";
            PreparedStatement statementUpdateParking = con.prepareStatement(sqlUpdateParking);
            Log.log.info("Query Update Parking => {}", statementUpdateParking);

            statementUpdateParking.setInt(1, parking_id);

            int resultUpdateParking = statementUpdateParking.executeUpdate();

            // Confirma la transacción
            con.commit();

            if (resultDelete > 0 && resultUpdatePlaza > 0 && resultUpdateParking > 0) {
                Log.log.info("Reserva eliminada con éxito!");
                response.setStatus(HttpServletResponse.SC_OK);
                out.print("{\"success\":\"Reserva eliminada con éxito\"}");
            } else {
                Log.log.error("Error en la eliminación de la reserva");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\":\"Error al eliminar la reserva\"}");
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
