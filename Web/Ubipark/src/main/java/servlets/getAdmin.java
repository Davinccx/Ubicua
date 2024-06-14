package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import database.Admin;
import jakarta.servlet.annotation.WebServlet;
import logic.Log;
import logic.Logic;

@WebServlet("/getAdmins")
public class getAdmin extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public getAdmin() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Log.log.info("--Get Admin information from DB --");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = null;

        try {
            out = response.getWriter();
            ArrayList<Admin> values = Logic.getAdminFromDB();
            String jsonAdmin = new Gson().toJson(values);
            Log.log.info("JSON=> {}", jsonAdmin);
            out.println(jsonAdmin);
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
