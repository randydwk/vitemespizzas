package controleurs;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataset.DS;
import io.jsonwebtoken.Claims;
@SuppressWarnings("serial")
@WebServlet("/users/*")
public class UserRestAPI extends HttpServlet
{   
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {   	
		PrintWriter out = resp.getWriter();
		String pathInfo = req.getPathInfo();
		
		String[] splits = null;
		if (pathInfo != null) {splits = pathInfo.split("/");}
		if(pathInfo == null || pathInfo.isEmpty() || splits.length != 2 || !splits[1].equals("token")){
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		String token = genererToken(req,resp,out);
		out.println("{\"token\":\""+token+"\"}");
		out.close();
    }
    
    static boolean verifToken(HttpServletRequest req, HttpServletResponse resp) {    	
    	String authorization = req.getHeader("Authorization");
    	if (authorization == null || !authorization.startsWith("Bearer")) {
    		try {resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);}
    		catch (IOException e) {e.printStackTrace();}
    		return false;
    	}

    	String token = authorization.substring("Bearer".length()).trim();
    	try {JwtManager.decodeJWT(token);}
    	catch (Exception e) {
    		try {resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);}
    		catch (IOException e1) {e1.printStackTrace();}
    		return false;
    	}
    	
    	return true;
    }
    
    static String genererToken(HttpServletRequest req, HttpServletResponse resp, PrintWriter out) {
    	String login = req.getParameter("login");
	    String pwd = req.getParameter("pwd");
	    String token = "";
	    
	    if (login != null && pwd != null) {
	    	Connection con = null;        
	        try {
	        	// Connexion BDD
	        	con = new DS(req.getServletContext()).getConnection();

	        	// Requête
	        	PreparedStatement ps = con.prepareStatement("select * from users where login=? and pwd=?");
	        	ps.setString(1,login);
	        	ps.setString(2,pwd);
	        	ResultSet rs = ps.executeQuery();

	        	// Résultats
				if (rs.next())
				{
					token = JwtManager.createJWT();
				} else {
					resp.sendError(HttpServletResponse.SC_NOT_FOUND);
				}
	        } catch (Exception e){
	        	out.println(e.getMessage());
	        } finally {
	        	try {con.close();} catch(Exception e2) {out.println(e2.getMessage());}
	        }
	    } else {
	    	try {resp.sendError(HttpServletResponse.SC_BAD_REQUEST);}
	    	catch (IOException e) {e.printStackTrace();}
	    }
	    return token;
    }
}