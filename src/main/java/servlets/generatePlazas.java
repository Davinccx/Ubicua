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
import logic.Log;



@WebServlet("/generatePlaza")
public class generatePlazas extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    
    public generatePlazas() {
        super();
    }



@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    Log.log.info("--Generate Random Plaza--");
    response.setContentType("application/json"); // Cambiado a application/json
    PrintWriter out = response.getWriter();
    
    try {
        
        int id_parking = Integer.parseInt(request.getParameter("parking_id"));
        int capacidad = Integer.parseInt(request.getParameter("capacidad"));
        int disponibles = Integer.parseInt(request.getParameter("disponibles"));     
        
        int ocupado = 0;
        ConnectionDDBB conector = new ConnectionDDBB();
        Connection con = conector.obtainConnection(true);

        
        String sql = "INSERT INTO plaza(id_parking, ocupado) VALUES (?, ?)";
        PreparedStatement statement = con.prepareStatement(sql);
        statement.setInt(1, id_parking);
        statement.setInt(2, ocupado); // Valor 1 para indicar que la plaza está ocupada
        int result = statement.executeUpdate();

        if (result <= 0) {
                // Manejar el caso de que la inserción falle
                Log.log.error("Error en el registro de la reserva");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\":\"Error al generar las plazas\"}");
                return;
        }

        // JSON de respuesta si todas las inserciones fueron exitosas
        Log.log.info("Plazas registrada con exito!");
        JSONObject json = new JSONObject();
        out.print(json.toString());

    } catch (NumberFormatException nfe) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        out.print("{\"error\":\"Número inválido\"}");
        Log.log.error("Número inválido: {}", nfe.getMessage());
    } catch (Exception e) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        out.print("{\"error\":\"Error interno del servidor\"}");
        Log.log.error("Error interno del servidor: {}", e.getMessage());
    } finally {
        out.close();
    }
}

}
