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
import logic.Logic;

public class getProducto extends HttpServlet{
    
    private static final long serialVersionUID = 1L;
    
     public getProducto() 
    {
        super();
    }
     
     protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		          System.out.println("-- Get Products information from DB--");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try 
		{
			ArrayList<Producto> values =Logic.getProductosFromDB();
			String jsonProductos = new Gson().toJson(values);
			System.out.println("JSON Values=> "+jsonProductos);
			out.println(jsonProductos);
		} catch (NumberFormatException nfe) 
		{
			out.println("-1");
                        System.out.println("Number Format Exception:"+nfe);
		} catch (IndexOutOfBoundsException iobe) 
		{
			out.println("-1");
			System.out.println("Index out of bounds Exception: "+ iobe);
		} catch (Exception e) 
		{
			out.println("-1");
			System.out.println("Exception: "+e);
		} finally 
		{
			out.close();
		}
	}
     
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doGet(request, response);
	}
    
}
