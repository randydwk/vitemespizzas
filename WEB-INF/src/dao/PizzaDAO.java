package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import dataset.DS;
import dto.Pizza;

public class PizzaDAO{
    public static Pizza find(int id, ServletContext context){
        Connection con = null;
        Pizza piz = new Pizza();
        
        try {
        	// Connexion BDD
        	con = new DS(context).getConnection();

        	// Requête
        	PreparedStatement ps = con.prepareStatement("select * from pizzas where pno = ?");
        	ps.setInt(1,id);
        	ResultSet rs = ps.executeQuery();

        	// Résultats
        	if (rs.next())
        	{
        		piz.setId(rs.getInt("pno"));
                piz.setName(rs.getString("name"));
                piz.setType(rs.getString("type"));
                piz.setPrixBase(rs.getFloat("prixBase"));
                
                // Ingrédients
                Statement ingst = con.createStatement();
                ResultSet ingrs = ingst.executeQuery("select * from ingpizzas where pno = "+rs.getInt("pno"));
                
                while (ingrs.next()) {
                	piz.getIngs().add(IngredientDAO.find(ingrs.getInt("ino"),context));
                }
        		
                /// Prix final
                ingrs = ingst.executeQuery("select sum(prix)+avg(prixBase) as prixfinal from ingredients i join ingpizzas ip using(ino) join pizzas p using(pno) where pno = "+rs.getInt("pno"));
                if (ingrs.next()) {
                	piz.setPrixFinal(ingrs.getFloat("prixfinal"));
                }
        	}

        } catch (Exception e){
        	System.out.println(e.getMessage());
        } finally {
        	try {con.close();} catch(Exception e2) {System.out.println(e2.getMessage());}
        }
        return piz;
    }
    
    public static Pizza find(String name, ServletContext context){
        Connection con = null;
        Pizza piz = new Pizza();
        
        try {
        	// Connexion BDD
        	con = new DS(context).getConnection();

        	// Requête
        	PreparedStatement ps = con.prepareStatement("select * from pizzas where name = ?");
        	ps.setString(1,name);
        	ResultSet rs = ps.executeQuery();

        	// Résultats
        	if (rs.next())
        	{
        		piz.setId(rs.getInt("pno"));
        		piz.setName(rs.getString("name"));
        	}

        } catch (Exception e){
        	System.out.println(e.getMessage());
        } finally {
        	try {con.close();} catch(Exception e2) {System.out.println(e2.getMessage());}
        }
        return piz;
    }
    
    public static Map<Integer,Pizza> findall(ServletContext context){
        Connection con = null;
        Map<Integer,Pizza> pizs = new HashMap<>();

        try {
        	// Connexion BDD
			con = new DS(context).getConnection();

        	// Requête
        	Statement stmt = con.createStatement();
        	ResultSet rs = stmt.executeQuery("select * from pizzas");
        	
        	// Résultats
        	while (rs.next())
        	{
        		Pizza piz = new Pizza();
        		piz.setId(rs.getInt("pno"));
        		piz.setName(rs.getString("name"));
        		piz.setType(rs.getString("type"));
                piz.setPrixBase(rs.getFloat("prixBase"));
        		
        		// Ingrédients
                Statement ingst = con.createStatement();
                ResultSet ingrs = ingst.executeQuery("select * from ingpizzas where pno = "+rs.getInt("pno"));
                
                while (ingrs.next()) {
                	piz.getIngs().add(IngredientDAO.find(ingrs.getInt("ino"),context));
                }
        		
                /// Prix final
                ingrs = ingst.executeQuery("select sum(prix)+avg(prixBase) as prixfinal from ingredients i join ingpizzas ip using(ino) join pizzas p using(pno) where pno = "+rs.getInt("pno"));
                if (ingrs.next()) {
                	piz.setPrixFinal(ingrs.getFloat("prixfinal"));
                }
                
                pizs.put(piz.getId(),piz);
        	}

        } catch (Exception e){
        	System.out.println(e.getMessage());
        } finally {
        	try {con.close();} catch(Exception e2) {System.out.println(e2.getMessage());}
        }
        return pizs;
    }
    
    public static void save(Pizza piz, ServletContext context){
        Connection con = null;
        
        try {
        	// Connexion BDD
        	con = new DS(context).getConnection();

        	// Requête
        	PreparedStatement ps = con.prepareStatement("insert into pizzas values (?,?,?,?)");
        	ps.setInt(1,piz.getId());
        	ps.setString(2,piz.getName());
        	ps.setString(3,piz.getType());
        	ps.setFloat(4,piz.getPrixBase());
        	ps.executeUpdate();
        	
        } catch (Exception e){
        	System.out.println(e.getMessage());
        } finally {
        	try {con.close();} catch(Exception e2) {System.out.println(e2.getMessage());}
        }
    }
    
    public static void delete(int id, ServletContext context) {
    	Connection con = null;
        
        try {
        	// Connexion BDD
        	con = new DS(context).getConnection();

        	// Requête
        	PreparedStatement ps = con.prepareStatement("delete from pizzas where pno = ?");
        	ps.setInt(1,id);
        	ps.executeUpdate();
        	
        } catch (Exception e){
        	System.out.println(e.getMessage());
        } finally {
        	try {con.close();} catch(Exception e2) {System.out.println(e2.getMessage());}
        }
    }
    
    public static void deleteIngredient(int pizId, int ingId, ServletContext context) {
    	Connection con = null;
        
        try {
        	// Connexion BDD
        	con = new DS(context).getConnection();

        	// Requête
        	PreparedStatement ps = con.prepareStatement("delete from ingpizzas where pno = ? and ino = ?");
        	ps.setInt(1,pizId);
        	ps.setInt(2,ingId);
        	ps.executeUpdate();
        	
        } catch (Exception e){
        	System.out.println(e.getMessage());
        } finally {
        	try {con.close();} catch(Exception e2) {System.out.println(e2.getMessage());}
        }
    }
}
