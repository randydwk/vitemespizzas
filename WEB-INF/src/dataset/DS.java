package dataset;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import javax.servlet.ServletContext;

public class DS {
    String url;
    String nom;
    String mdp;
    String driver;
    
    public DS(ServletContext context) throws Exception{
    	InputStream inputStream = null;
    	try {
			inputStream = context.getResourceAsStream("/WEB-INF/config.prop");
		} catch (Exception e) {
			e.printStackTrace();
		}
    	Properties p = new Properties();
        p.load(inputStream);
        this.url = p.getProperty("url");
        this.nom = p.getProperty("login");
        this.mdp = p.getProperty("password");
        this.driver = p.getProperty("driver");
    }
    
    public Connection getConnection() throws Exception{
    	Class.forName(driver);
        return DriverManager.getConnection(url,nom,mdp);
    	//return DriverManager.getConnection("jdbc:postgresql://localhost/but2",nom,mdp); // pour le travail maison
    }

}
