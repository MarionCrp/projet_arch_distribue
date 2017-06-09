package ClientCorba;

import java.util.Properties;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;

public class Client {

	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		props.put("org.omg.CORBA.ORBInitialPort", "2000");
		props.put("org.omg.CORBA.ORBInitialHost", "127.0.0.1");
		ORB requestBroker = ORB.init((String[]) null, props);
		
		
	}
}
