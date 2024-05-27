package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import database.Producto;
import logic.Log;
import logic.Logic;

public class getProducto extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public getProducto() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Log.log.info("--Get Products information from DB --");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            ArrayList<Producto> values = Logic.getProductosFromDB();
            String jsonProductos = new Gson().toJson(values);
            Log.log.info("Query=> {}", jsonProductos);
            out.println(jsonProductos);
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
