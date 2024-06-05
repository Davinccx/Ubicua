package logic;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import mqtt.MQTTBroker;
import mqtt.MQTTPublisher;
import mqtt.MQTTSuscriber;


@WebListener
public class ProjectInitializer implements ServletContextListener {


   

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
                Log.log.info("-->Suscribe Topics<--");
		MQTTBroker broker = new MQTTBroker();
		MQTTSuscriber suscriber = new MQTTSuscriber();
                suscriber.suscribeToUbiparkTopics(broker);
                MQTTPublisher.publish(broker, "parking/entrada", "Esta entrando un coche con matricula 1234GAS");
                
		
		
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}