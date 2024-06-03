
package servlets;

import com.google.gson.Gson;
import database.Parking;
import database.User;
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

@WebServlet("/getParkings")
public class getParkings extends HttpServlet{
    
     private static final long serialVersionUID = 1L;

    public getParkings() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Log.log.info("-- Get Users information from DB--");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            ArrayList<Parking> values = Logic.getParkingsFromDB();
            String jsonParkings = new Gson().toJson(values);
            Log.log.info("JSON Values=> {} ", jsonParkings);
            out.println(jsonParkings);
        } catch (NumberFormatException nfe) {
            out.println("-1");
            Log.log.error("Number Format Exception: {}", nfe);
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