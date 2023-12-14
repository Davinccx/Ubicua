package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectionDDBB {

    public Connection obtainConnection(boolean autoCommit) throws NullPointerException {

        Connection con = null;
        int intentos = 5;
        for (int i = 0; i < intentos; i++) {

            try {
                Context ctx = new InitialContext();
                // Get the connection factory configured in Tomcat
                DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/Vendify");

                // Obtiene una conexion
                con = ds.getConnection();
                Calendar calendar = Calendar.getInstance();
                java.sql.Date date = new java.sql.Date(calendar.getTime().getTime());
                System.out.println("Connection creation. Bd connection identifier: {} obtained in {}" + con.toString() + date.toString());
                con.setAutoCommit(autoCommit);

                i = intentos;
            } catch (NamingException ex) {

            } catch (SQLException ex) {

                throw (new NullPointerException("SQL connection is null"));
            }
        }

        return con;
    }
    
    public void closeTransaction(Connection con)
    {
        try
          {
            con.commit();
              System.out.println("Transaction closed");
          } catch (SQLException ex)
          {
              System.out.println("Error closing the transaction: {}"+ ex);
          }
    }
    
    
    public void cancelTransaction(Connection con)
    {
        try
          {
            con.rollback();
              System.out.println("Transaction canceled");
          } catch (SQLException ex)
          {
              System.out.println("ERROR sql when canceling the transation: {}"+ex);
          }
    }
    
     public void closeConnection(Connection con)
    {
        try
          {
        	     System.out.println("Closing the connection");
            if (null != con)
              {
				Calendar calendar = Calendar.getInstance();
				java.sql.Date date = new java.sql.Date(calendar.getTime().getTime());
	                 System.out.println("Connection closed. Bd connection identifier: {} obtained in {}"+con.toString()+ date.toString());
                con.close();
              }

        	     System.out.println("The connection has been closed");
          } catch (SQLException e)
          {
        	     System.out.println("ERROR sql closing the connection: {}"+ e);
        	  e.printStackTrace();
          }
    }
     
      public static PreparedStatement getStatement(Connection con,String sql)
    {
        PreparedStatement ps = null;
        try
          {
            if (con != null)
              {
                ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

              }
          } catch (SQLException ex)
          {
    	         System.out.println("ERROR sql creating PreparedStatement:{} "+ex);
          }

        return ps;
    }   
      
    public static PreparedStatement getProductos(Connection con)
    {
    	return getStatement(con,"SELECT * FROM productos");  	
    }
      
      
      
}
