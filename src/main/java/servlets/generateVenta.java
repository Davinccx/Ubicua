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
import java.sql.PreparedStatement;
import java.util.List;
import logic.Log;
import java.util.Random;
import logic.Logic;

@WebServlet("/GenerateVenta")
public class generateVenta extends HttpServlet {

    Random random = new Random();

    private static final long serialVersionUID = 1L;

    public generateVenta() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Log.log.info("--Generate Random Venta--");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Random random = new Random();
        try {

            List<Integer> usersID = Logic.getUsersID();
            List<Integer> machinesID = Logic.getMaquinasID();
            List<Integer> productsID = Logic.getProductsID();

            int machine = machinesID.get(random.nextInt(machinesID.size()) + 1);
            int user = usersID.get(random.nextInt(usersID.size()) + 1);
            int product = productsID.get(random.nextInt(productsID.size()) + 1);
            
            ConnectionDDBB conector = new ConnectionDDBB();
            Connection con = conector.obtainConnection(true);

            String sql = "INSERT INTO ventas(id_usuario, id_producto, id_maquina) VALUES (?, ?, ?)";
            PreparedStatement statement = con.prepareStatement(sql);
            Log.log.info("Query => {}", statement);

            statement.setInt(1, user);
            statement.setInt(2, product);
            statement.setInt(3, machine);

            int result = statement.executeUpdate();

            if (result > 0) {
                Log.log.info("Venta registrada con exito!");
                JSONObject json = new JSONObject();
                json.put("id_usuario", user);
                json.put("id_producto", product);
                json.put("id_maquina", machine);

                out.print(json.toString());
            } else {
                // Manejar el caso de que la inserción falle
                Log.log.error("Error en el registro");
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
