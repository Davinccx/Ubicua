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

@WebServlet("/deleteReserva")
public class deleteReserva extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public deleteReserva() {
        super();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        try {
            String id = request.getParameter("reserva_id");

            Log.log.info("--Delete Venta from DB --");

            ConnectionDDBB conector = new ConnectionDDBB();
            Connection con = conector.obtainConnection(true);
            response.setContentType("text/html;charset=UTF-8");

            String sql = "DELETE FROM reservas WHERE reserva_id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(id));
            Log.log.info("Query=> {}", statement.toString());

            int result = statement.executeUpdate();
            if (result > 0) {
                Log.log.info("Reserva eliminado con éxito.");
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                Log.log.error("No se encontró una reserva con ese ID.");

            }
        } catch (NumberFormatException nfe) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Log.log.error("Number Format Exception: {}", nfe);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Log.log.error("Number Format Exception: {}", e);
        } finally {
            out.close();
        }
    }
}
