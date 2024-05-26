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
import dto.Ingredient;
@SuppressWarnings("serial")
@WebServlet("/ingredients/*")
public class IngredientRestAPI extends HttpServlet
{   
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	resp.setContentType("application/json;charset=UTF-8");		
		String pathInfo = req.getPathInfo();
		PrintWriter out = resp.getWriter();
		ObjectMapper objectMapper = new ObjectMapper();
		ServletContext context = req.getServletContext();
		
		if(pathInfo == null || pathInfo.equals("/")){
			out.println(objectMapper.writeValueAsString(IngredientDAO.findall(context)));
			out.close();
			return;
		}
		
		String[] splits = pathInfo.split("/");
		if(splits.length > 3 || (splits.length == 3 && !splits[2].equals("name"))) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		Integer ingId = 0;
		try {
			ingId = Integer.parseInt(splits[1]);
		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		if(!IngredientDAO.findall(context).containsKey(ingId)) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		} else if (splits.length == 3 && splits[2].equals("name")) {
			out.println(objectMapper.writeValueAsString(IngredientDAO.find(ingId,context).getName()));
		} else {
			out.println(objectMapper.writeValueAsString(IngredientDAO.find(ingId,context)));
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
		    
		    Ingredient ing = objectMapper.readValue(payload, Ingredient.class);
		    		    
		    if (IngredientDAO.findall(context).containsKey(ing.getId())) {
		    	resp.sendError(HttpServletResponse.SC_CONFLICT);
		    } else {
		    	IngredientDAO.save(ing,context);
		    	out.println(objectMapper.writeValueAsString(ing));
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
		
		if(splits.length != 2) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		Integer ingId = 0;
		try {
			ingId = Integer.parseInt(splits[1]);
		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		if (!IngredientDAO.findall(context).containsKey(ingId)) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		Ingredient ing = IngredientDAO.find(ingId,context);
		
		IngredientDAO.delete(ingId,context);
		
	    out.println(objectMapper.writeValueAsString(ing));
		return;
	}
}