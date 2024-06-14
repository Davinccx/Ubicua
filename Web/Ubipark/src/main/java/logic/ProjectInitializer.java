package logic;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;


@WebListener
public class ProjectInitializer implements ServletContextListener {


   

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
                Log.log.info("-->Inicia la aplicación<--");
                
		
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}