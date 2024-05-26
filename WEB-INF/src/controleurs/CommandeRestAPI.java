package controleurs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.CommandeDAO;
import dao.PizzaDAO;
import dto.Commande;
import dto.Pizza;
@SuppressWarnings("serial")
@WebServlet("/commandes/*")
public class CommandeRestAPI extends HttpServlet
{   
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json;charset=UTF-8");
		String pathInfo = req.getPathInfo();
		PrintWriter out = resp.getWriter();
		ObjectMapper objectMapper = new ObjectMapper();
		ServletContext context = req.getServletContext();
		
		if(pathInfo == null || pathInfo.equals("/")){
			out.println(objectMapper.writeValueAsString(CommandeDAO.findall(context)));
			out.close();
			return;
		}
		
		String[] splits = pathInfo.split("/");
		if(splits.length > 3 || (splits.length == 3 && !splits[2].equals("prixfinal"))) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		Integer commId = 0;
		try {
			commId = Integer.parseInt(splits[1]);
		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		if(!PizzaDAO.findall(context).containsKey(commId)) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		} else if (splits.length == 3 && splits[2].equals("prixfinal")) {
			out.println(objectMapper.writeValueAsString(CommandeDAO.find(commId,context).getPrixFinal()));
		} else {
			out.println(objectMapper.writeValueAsString(CommandeDAO.find(commId,context)));
		}
		out.close();
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	if (!UserRestAPI.verifToken(req,resp)) return;
    	
    	String pathInfo = req.getPathInfo();
    	PrintWriter out = resp.getWriter();
    	ObjectMapper objectMapper = new ObjectMapper();
    	ServletContext context = req.getServletContext();
    	
		if (pathInfo == null || pathInfo.equals("/")){

			StringBuilder buffer = new StringBuilder();
		    BufferedReader reader = req.getReader();
		    String line;
		    while ((line = reader.readLine()) != null) {
		        buffer.append(line);
		    }
		    
		    String payload = buffer.toString();
		    
		    Commande comm = objectMapper.readValue(payload, Commande.class);
		    		    
		    if (CommandeDAO.findall(context).containsKey(comm.getId())) {
		    	resp.sendError(HttpServletResponse.SC_CONFLICT);
		    } else {
		    	CommandeDAO.save(comm,context);
		    	out.println(objectMapper.writeValueAsString(comm));
		    }
		    
		}
		else {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
    }
}