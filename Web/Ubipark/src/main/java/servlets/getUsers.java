package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import database.User;
import jakarta.servlet.annotation.WebServlet;
import logic.Log;
import logic.Logic;

@WebServlet("/getUsers")
public class getUsers extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public getUsers() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Log.log.info("-- Get Users information from DB--");
        response.setContentType("application/json;charset=UTF-8");

        PrintWriter out = null;

        try {
            out = response.getWriter();
            ArrayList<User> values = Logic.getUsersFromDB();
            String jsonUsuarios = new Gson().toJson(values);
            Log.log.info("JSON Values=> {}", jsonUsuarios);
            out.println(jsonUsuarios);
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
            if (out != null) {
                out.close();
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
