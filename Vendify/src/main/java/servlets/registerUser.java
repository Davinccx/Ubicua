package servlets;

import java.io.IOException;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import database.ConnectionDDBB;
import static java.lang.System.out;
import java.sql.PreparedStatement;

public class registerUser extends HttpServlet{
    
    
    private static final long serialVersionUID = 1L;

    public registerUser() {
        super();
    }
    
     protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        
        try{
            ConnectionDDBB conector = new ConnectionDDBB();
            Connection con = conector.obtainConnection(true);
            response.setContentType("text/html;charset=UTF-8");
                      
            double saldo = 0;
            String email = request.getParameter("email");
            String username = request.getParameter("user");
            String password = request.getParameter("password");
            String telephone = request.getParameter("telephone");
            
            String sql = "INSERT INTO users(email, password, username,telefono,saldo) VALUES (?,?,?,?,?)";
            PreparedStatement statement = con.prepareStatement(sql);
           
            statement.setString(1, email); // Considera usar hashing para la contraseña
            statement.setString(2, password);
            statement.setString(3, username);
            statement.setString(4,telephone);
            statement.setDouble(5,saldo);
            int result = statement.executeUpdate();
            if (result > 0) {
                    response.sendRedirect("successRegister.html");
                } else {
                    response.getStatus();
                }
            
        
        }catch (NumberFormatException nfe) {
            out.println("-1");
            System.out.println("Number Format Exception:" + nfe);
        } catch (IndexOutOfBoundsException iobe) {
            out.println("-1");
            System.out.println("Index out of bounds Exception: " + iobe);
        } catch (Exception e) {
            out.println("-1");
            System.out.println("Exception: " + e);
        } finally {
            out.close();
        }
        }
    
    }

