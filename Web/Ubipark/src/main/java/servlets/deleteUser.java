package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import database.ConnectionDDBB;
import logic.Log;

@WebServlet("/deleteUser")
public class deleteUser extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public deleteUser() {
        super();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        Connection con = null;
        PreparedStatement statement = null;

        try {
            String id = request.getParameter("id");

            Log.log.info("--Delete User from DB --");

            ConnectionDDBB conector = new ConnectionDDBB();
            con = conector.obtainConnection(true);

            String sql = "DELETE FROM users WHERE user_id = ?";
            statement = con.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(id));
            Log.log.info("Query => {}", statement.toString());

            int result = statement.executeUpdate();
            if (result > 0) {
                Log.log.info("Usuario eliminado con éxito.");
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                Log.log.error("No se encontró un usuario con ese ID.");
            }
        } catch (NumberFormatException nfe) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Log.log.error("Number Format Exception: {}", nfe.getMessage());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Log.log.error("Exception: {}", e.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    Log.log.error("Error al cerrar el PreparedStatement: {}", e.getMessage());
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    Log.log.error("Error al cerrar la conexión: {}", e.getMessage());
                }
            }
            out.close();
        }
    }
}
