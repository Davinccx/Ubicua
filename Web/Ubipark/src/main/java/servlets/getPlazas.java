package servlets;

import com.google.gson.Gson;
import database.Plaza;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import logic.Log;
import logic.Logic;

@WebServlet("/getPlazas")
public class getPlazas extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    public getPlazas() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Log.log.info("-- Get Plazas information from DB--");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = null;
        
        try {
            out = response.getWriter();
            ArrayList<Plaza> values = Logic.getPlazasFromDB();
            String jsonPlazas = new Gson().toJson(values);
            Log.log.info("JSON Values=> {}", jsonPlazas);
            out.println(jsonPlazas);
        } catch (NumberFormatException nfe) {
            Log.log.error("Number Format Exception: {}", nfe);
            sendErrorResponse(out, "-1");
        } catch (IndexOutOfBoundsException iobe) {
            Log.log.error("Index out of bound exception: {}", iobe);
            sendErrorResponse(out, "-1");
        } catch (Exception e) {
            Log.log.error("Exception: {}", e);
            sendErrorResponse(out, "-1");
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private void sendErrorResponse(PrintWriter out, String errorMessage) {
        if (out != null) {
            out.println(errorMessage);
        }
    }
}
