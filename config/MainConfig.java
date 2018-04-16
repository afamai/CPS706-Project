package config;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.net.InetAddress;

public class MainConfig 
{
    public final static String CONFIG_FILE_LOCATION = "config/config.txt";
    private static File configFile = new File(CONFIG_FILE_LOCATION);
	private static Properties properties = new Properties();

    public static InetAddress getIP(String key) throws Exception{
		properties.load(new FileInputStream(configFile));
		String ip = properties.getProperty(key, "localhost");
		return InetAddress.getByName(ip);
	}

	public static int getPort(String key) throws Exception{
		properties.load(new FileInputStream(configFile));
		return Integer.parseInt(properties.getProperty(key, "0"));
	}
	
}
