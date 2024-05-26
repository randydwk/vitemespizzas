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

import dao.IngredientDAO;
import dao.PizzaDAO;
import dto.Ingredient;
import dto.Pizza;
@SuppressWarnings("serial")
@WebServlet("/pizzas/*")
public class PizzaRestAPI extends HttpServlet
{   
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json;charset=UTF-8");
		String pathInfo = req.getPathInfo();
		PrintWriter out = resp.getWriter();
		ObjectMapper objectMapper = new ObjectMapper();
		ServletContext context = req.getServletContext();
		
		if(pathInfo == null || pathInfo.equals("/")){
			out.println(objectMapper.writeValueAsString(PizzaDAO.findall(context)));
			out.close();
			return;
		}
		
		String[] splits = pathInfo.split("/");
		if(splits.length > 3 || (splits.length == 3 && !splits[2].equals("prixfinal"))) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		Integer pizId = 0;
		try {
			pizId = Integer.parseInt(splits[1]);
		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		if(!PizzaDAO.findall(context).containsKey(pizId)) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		} else if (splits.length == 3 && splits[2].equals("prixfinal")) {
			out.println(objectMapper.writeValueAsString(PizzaDAO.find(pizId,context).getPrixFinal()));
		} else {
			out.println(objectMapper.writeValueAsString(PizzaDAO.find(pizId,context)));
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
		    
		    Pizza piz = objectMapper.readValue(payload, Pizza.class);
		    		    
		    if (PizzaDAO.findall(context).containsKey(piz.getId())) {
		    	resp.sendError(HttpServletResponse.SC_CONFLICT);
		    } else {
		    	PizzaDAO.save(piz,context);
		    	out.println(objectMapper.writeValueAsString(piz));
		    }
		    
		}
		else {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
    }
    
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	if (!UserRestAPI.verifToken(req,resp)) return;
    	
    	String pathInfo = req.getPathInfo();
		PrintWriter out = resp.getWriter();
    	ObjectMapper objectMapper = new ObjectMapper();
    	ServletContext context = req.getServletContext();
    	
		if(pathInfo == null || pathInfo.equals("/")){
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		String[] splits = pathInfo.split("/");
		
		if(splits.length <= 3) {
			Integer pizId = 0;
			try {
				pizId = Integer.parseInt(splits[1]);
			} catch (Exception e) {
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
			
			if (!PizzaDAO.findall(context).containsKey(pizId)) {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			
			Pizza piz = PizzaDAO.find(pizId,context);
			
			if (splits.length == 2) { // DELETE A PIZZA
				PizzaDAO.delete(pizId,context);
				out.println(objectMapper.writeValueAsString(piz));
			} else { // DELETE AN INGREDIENT FROM PIZZA
				Integer ingId = 0;
				try {
					ingId = Integer.parseInt(splits[2]);
				} catch (Exception e) {
					resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
					return;
				}
				
				Ingredient ing = IngredientDAO.find(ingId,context);
				if (ing == null || !piz.contains(ingId)) {
					resp.sendError(HttpServletResponse.SC_NOT_FOUND);
					return;
				}
				
				PizzaDAO.deleteIngredient(pizId, ingId,context);
				out.println(objectMapper.writeValueAsString(ing));
			}
		} else {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
	    
		return;
	}
}