package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import dataset.DS;
import dto.Ingredient;

public class IngredientDAO{
    public static Ingredient find(int id, ServletContext context){
        Ingredient ing = null;
        
        Connection con = null;
        try {
        	// Connexion BDD
        	con = new DS(context).getConnection();

        	// Requête
        	PreparedStatement ps = con.prepareStatement("select * from ingredients where ino = ?");
        	ps.setInt(1,id);
        	ResultSet rs = ps.executeQuery();

        	// Résultats
        	if (rs.next())
        	{
        		ing = new Ingredient();
        	    ing.setId(rs.getInt("ino"));
                ing.setName(rs.getString("name"));
                ing.setPrix(rs.getFloat("prix"));
        	}

        } catch (Exception e){
        	System.out.println(e.getMessage());
        } finally {
        	try {con.close();} catch(Exception e2) {System.out.println(e2.getMessage());}
        }
        return ing;
    }
    
    public static Ingredient find(String name, ServletContext context){
        Ingredient ing = null;
        
        Connection con = null;
        try {
        	// Connexion BDD
        	con = new DS(context).getConnection();

        	// Requête
        	PreparedStatement ps = con.prepareStatement("select * from ingredients where name = ?");
        	ps.setString(1,name);
        	ResultSet rs = ps.executeQuery();

        	// Résultats
        	if (rs.next())
        	{
        		ing = new Ingredient();
        	    ing.setId(rs.getInt("ino"));
                ing.setName(rs.getString("name"));
                ing.setPrix(rs.getFloat("prix"));
        	}

        } catch (Exception e){
        	System.out.println(e.getMessage());
        } finally {
        	try {con.close();} catch(Exception e2) {System.out.println(e2.getMessage());}
        }
        return ing;
    }
    
    public static Map<Integer,Ingredient> findall(ServletContext context){
        Connection con = null;
        Map<Integer,Ingredient> ings = new HashMap<>();

        try {
        	// Connexion BDD
			con = new DS(context).getConnection();

        	// Requête
        	Statement stmt = con.createStatement();
        	ResultSet rs = stmt.executeQuery("select * from ingredients");
        	
        	// Résultats
        	while (rs.next())
        	{
        		Ingredient ing = new Ingredient();
        	    ing.setId(rs.getInt("ino"));
                ing.setName(rs.getString("name"));
                ing.setPrix(rs.getFloat("prix"));
                ings.put(ing.getId(),ing);
        	}

        } catch (Exception e){
        	System.out.println(e.getMessage());
        } finally {
        	try {con.close();} catch(Exception e2) {System.out.println(e2.getMessage());}
        }
        return ings;
    }
    
    public static void save(Ingredient ing, ServletContext context){
        Connection con = null;
        
        try {
        	// Connexion BDD
        	con = new DS(context).getConnection();

        	// Requête
        	PreparedStatement ps = con.prepareStatement("insert into ingredients values (?,?,?)");
        	ps.setInt(1,ing.getId());
        	ps.setString(2,ing.getName());
        	ps.setFloat(3,ing.getPrix());
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
        	PreparedStatement ps = con.prepareStatement("delete from ingredients where ino = ?");
        	ps.setInt(1,id);
        	ps.executeUpdate();
        	
        } catch (Exception e){
        	System.out.println(e.getMessage());
        } finally {
        	try {con.close();} catch(Exception e2) {System.out.println(e2.getMessage());}
        }
    }
}
