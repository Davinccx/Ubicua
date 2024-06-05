package mqtt;


public class MQTTBroker 
{		
	private static int qos = 2;
	private static String broker = "tcp://broker.emqx.io:1883";
	private static String clientId = "Servidor";
        public static final String UBIPARK_TOPICS = "parking/entrada";
		
	public MQTTBroker()
	{
	}

	public static int getQos() {
		return qos;
	}

	public static String getBroker() {
		return broker;
	}

	public static String getClientId() {
		return clientId;
	}			
}


