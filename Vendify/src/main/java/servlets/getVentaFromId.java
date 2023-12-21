package servlets;

import com.google.gson.Gson;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import database.Venta;
import java.io.PrintWriter;
import java.util.ArrayList;
import logic.Log;
import logic.Logic;

public class getVentaFromId extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public getVentaFromId() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Log.log.info("-- Get Venta information from ID, from DB--");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String ventaIdStr = request.getParameter("id_venta");
        if (ventaIdStr == null || ventaIdStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de venta no proporcionado.");
            return;
        }

        int ventaId;
        try {
            ventaId = Integer.parseInt(ventaIdStr);
            ArrayList<Venta> ventas = Logic.getVentasFromDB();

            for (int i = 0; i < ventas.size(); i++) {

                Venta newVenta = ventas.get(i);

                if (newVenta.getId_venta() == ventaId) {

                    String jsonVenta = new Gson().toJson(newVenta);
                    Log.log.info("JSON => {}", jsonVenta);
                    out.println(jsonVenta);

                }

            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de venta inválido.");
            Log.log.error("Bad Request from server");
        } catch (IndexOutOfBoundsException iobe) {
            out.println("-1");
            Log.log.error("Index out of bound exception: {}", iobe);
        } catch (Exception e) {
            out.println("-1");
            Log.log.error("Number Format Exception: {}", e);
        } finally {
            out.close();
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
