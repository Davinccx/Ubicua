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
import logic.Log;

public class ConnectionDDBB {

    public Connection obtainConnection(boolean autoCommit) throws NullPointerException {

        Connection con = null;
        int intentos = 5;
        for (int i = 0; i < intentos; i++) {
            Log.logdb.info("Attempt {} to connect to the database");
            try {
                Context ctx = new InitialContext();
                // Get the connection factory configured in Tomcat
                DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/Vendify");

                // Obtiene una conexion
                con = ds.getConnection();
                Calendar calendar = Calendar.getInstance();
                java.sql.Date date = new java.sql.Date(calendar.getTime().getTime());
                Log.logdb.debug("Connection creation. Bd connection identifier: {} obtained in {}", con.toString(), date.toString());
                con.setAutoCommit(autoCommit);
                Log.logdb.info("Connection obtained in the attemp: "+i);

                i = intentos;
            } catch (NamingException ex) {
                
                Log.logdb.error("Error getting connection while trying: {} = {}",i,ex);
                
            } catch (SQLException ex) {
                
                Log.logdb.error("Error getting connection while trying: {} = {}",i,ex);
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
              Log.logdb.debug("Transaction closed");
          } catch (SQLException ex)
          {
              Log.logdb.error("Error closing the transaction: {}", ex);
          }
    }
    
    
    public void cancelTransaction(Connection con)
    {
        try
          {
            con.rollback();
              Log.logdb.debug("Transaction canceled");
          } catch (SQLException ex)
          {
              Log.logdb.error("ERROR sql when canceling the transation: {}",ex);
          }
    }
    
     public void closeConnection(Connection con)
    {
        try
          {
        	     Log.logdb.info("Closing the connection");
            if (null != con)
              {
				Calendar calendar = Calendar.getInstance();
				java.sql.Date date = new java.sql.Date(calendar.getTime().getTime());
	                Log.logdb.debug("Connection closed. Bd connection identifier: {} obtained in {}",con.toString(),date.toString());
                con.close();
              }

        	     Log.logdb.error("The connection has been closed");
          } catch (SQLException e)
          {
        	  Log.logdb.error("ERROR sql closing the connection: {}", e);
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
    	         Log.logdb.warn("ERROR sql creating PreparedStatement:{} ",ex);
          }

        return ps;
    }   
      
  
    
    
    public static PreparedStatement getUsers(Connection con)
    {
    	return getStatement(con,"SELECT * FROM users");  	
    }
      

    public static PreparedStatement getAdmin(Connection con)
    {
    	return getStatement(con,"SELECT * FROM admin");  	
    }
       
    
    public static PreparedStatement getParkings(Connection con)
    {
    	return getStatement(con,"SELECT * FROM parkings");  	
    }
    
    public static PreparedStatement getReservas(Connection con)
    {
    	return getStatement(con,"SELECT * FROM reservas");  	
    }
    
    public static PreparedStatement getPlazas(Connection con)
    {
    	return getStatement(con,"SELECT * FROM plaza");  	
    }
}
