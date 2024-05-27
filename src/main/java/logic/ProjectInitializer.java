package logic;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import mqtt.MQTTBroker;
import mqtt.MQTTSuscriber;


@WebListener
public class ProjectInitializer implements ServletContextListener {


   

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
                Log.log.info("-->Suscribe Topics<--");
		MQTTBroker broker = new MQTTBroker();
		MQTTSuscriber suscriber = new MQTTSuscriber();
		suscriber.suscribeToMaquetaTopics(broker);

		Log.log.info("-->Running weather Thread<--");
		
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}