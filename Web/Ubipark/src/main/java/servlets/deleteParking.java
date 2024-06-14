package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import database.ConnectionDDBB;
import jakarta.servlet.annotation.WebServlet;
import logic.Log;

@WebServlet("/deleteParking")
public class deleteParking extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public deleteParking() {
        super();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        Connection con = null;
        PreparedStatement statementDeletePlazas = null;
        PreparedStatement statementDeleteParking = null;

        try {
            String id = request.getParameter("parking_id");
            Log.log.info("--Delete Parking and associated Plazas from DB --");

            ConnectionDDBB conector = new ConnectionDDBB();
            con = conector.obtainConnection(true);
            response.setContentType("text/html;charset=UTF-8");

            // Inicia la transacción
            con.setAutoCommit(false);

            // Primero, elimina las plazas asociadas al parking
            String sqlDeletePlazas = "DELETE FROM plazas WHERE parking_id = ?";
            statementDeletePlazas = con.prepareStatement(sqlDeletePlazas);
            statementDeletePlazas.setInt(1, Integer.parseInt(id));
            Log.log.info("Query Delete Plazas => {}", statementDeletePlazas.toString());

            int resultPlazas = statementDeletePlazas.executeUpdate();

            // Luego, elimina el parking
            String sqlDeleteParking = "DELETE FROM parkings WHERE parking_id = ?";
            statementDeleteParking = con.prepareStatement(sqlDeleteParking);
            statementDeleteParking.setInt(1, Integer.parseInt(id));
            Log.log.info("Query Delete Parking => {}", statementDeleteParking.toString());

            int resultParking = statementDeleteParking.executeUpdate();

            // Confirma la transacción
            con.commit();

            if (resultParking > 0) {
                Log.log.info("Parking y plazas eliminados con éxito.");
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                Log.log.error("No se encontró un parking con ese ID.");
            }
        } catch (NumberFormatException nfe) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackException) {
                    Log.log.error("Error al hacer rollback: {}", rollbackException.getMessage());
                }
            }
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Log.log.error("Number Format Exception: {}", nfe);
        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackException) {
                    Log.log.error("Error al hacer rollback: {}", rollbackException.getMessage());
                }
            }
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Log.log.error("Error interno del servidor: {}", e.getMessage());
        } finally {
            if (statementDeletePlazas != null) {
                try {
                    statementDeletePlazas.close();
                } catch (SQLException e) {
                    Log.log.error("Error al cerrar statementDeletePlazas: {}", e.getMessage());
                }
            }
            if (statementDeleteParking != null) {
                try {
                    statementDeleteParking.close();
                } catch (SQLException e) {
                    Log.log.error("Error al cerrar statementDeleteParking: {}", e.getMessage());
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
