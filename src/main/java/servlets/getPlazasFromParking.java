package servlets;

import com.google.gson.Gson;
import database.ConnectionDDBB;
import database.Plaza;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import database.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import logic.Log;
import logic.Logic;

@WebServlet("/getPlazasFromParking")
public class getPlazasFromParking extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public getPlazasFromParking() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        Log.log.info("-- Get Plazas from parking information from DB--");
        response.setContentType("application/json;charset=UTF-8");
        try {
            String id = request.getParameter("parking_id");
            if (id == null || id.isEmpty()) {
                throw new NumberFormatException("El parámetro parking_id está vacío o no existe.");
            }

            ConnectionDDBB conector = new ConnectionDDBB();
            Connection con = conector.obtainConnection(true);

            String sql = "SELECT * FROM plaza WHERE id_parking = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(id));

            Log.log.info("Query=> {}", statement.toString());

            ResultSet resultSet = statement.executeQuery();
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
            }

        } catch (NumberFormatException nfe) {
            Log.log.error("Number Format Exception: {}", nfe);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("-1");
        } catch (IndexOutOfBoundsException iobe) {
            Log.log.error("Index out of bound exception: {}", iobe);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("-1");
        } catch (Exception e) {
            Log.log.error("Exception: {}", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("-1");
        } finally {
            out.close();
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
