package logic;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import mqtt.MQTTSuscriber;


@WebListener
public class ProjectInitializer implements ServletContextListener {


   

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
            // Iniciar VendifyThread u otros servicios necesarios
            MQTTSuscriber subscriber = new MQTTSuscriber();
            subscriber.suscribeToMaquetaTopics();
        
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}