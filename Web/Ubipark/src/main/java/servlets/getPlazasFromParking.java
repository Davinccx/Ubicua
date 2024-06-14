package servlets;

import com.google.gson.Gson;
import database.ConnectionDDBB;
import database.Plaza;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import logic.Log;

@WebServlet("/getPlazasFromParking")
public class getPlazasFromParking extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public getPlazasFromParking() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Log.log.info("-- Get Plazas from parking information from DB--");
        response.setContentType("application/json;charset=UTF-8");

        PrintWriter out = null;
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            out = response.getWriter();
            String id = request.getParameter("parking_id");
            if (id == null || id.isEmpty()) {
                throw new NumberFormatException("El parámetro parking_id está vacío o no existe.");
            }

            ConnectionDDBB conector = new ConnectionDDBB();
            con = conector.obtainConnection(true);

            String sql = "SELECT * FROM plaza WHERE id_parking = ?";
            statement = con.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(id));

            Log.log.info("Query=> {}", statement.toString());

            resultSet = statement.executeQuery();
            ArrayList<Plaza> plazas = new ArrayList<>();
            while (resultSet.next()) {
                Plaza plaza = new Plaza();
                plaza.setId_plaza(resultSet.getInt("id_plaza"));
                plaza.setId_parking(resultSet.getInt("id_parking"));
                plaza.setOcupado(resultSet.getInt("ocupado"));
                // Añade aquí todos los campos necesarios
                plazas.add(plaza);
            }

            if (!plazas.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_OK);
                Gson gson = new Gson();
                String json = gson.toJson(plazas);
                out.print(json);
                out.flush();
            } else {
                Log.log.error("No se encontró un parking con ese ID.");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.println("No se encontraron plazas para el parking con el ID especificado.");
            }

        } catch (NumberFormatException nfe) {
            Log.log.error("Number Format Exception: {}", nfe);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            if (out != null) {
                out.println("-1");
            }
        } catch (IndexOutOfBoundsException iobe) {
            Log.log.error("Index out of bound exception: {}", iobe);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            if (out != null) {
                out.println("-1");
            }
        } catch (Exception e) {
            Log.log.error("Exception: {}", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            if (out != null) {
                out.println("-1");
            }
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (con != null) {
                    con.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                Log.log.error("Error closing resources: {}", e);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
