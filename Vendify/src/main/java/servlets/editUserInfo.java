package servlets;

import com.google.gson.Gson;
import database.Maquina;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static java.lang.System.out;
import database.Venta;
import database.User;
import database.Producto;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.ArrayList;
import logic.Logic;

public class editUserInfo extends HttpServlet{
    
    private static final long serialVersionUID = 1L;

    public editUserInfo() {
        super();
    }
    
     protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         
     }
     
      protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
    
