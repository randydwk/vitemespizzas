package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import dataset.DS;
import dto.Commande;
import dto.Pizza;

public class CommandeDAO{
    public static Commande find(int id, ServletContext context){
        Connection con = null;
        Commande comm = new Commande();
        
        try {
        	// Connexion BDD
        	con = new DS(context).getConnection();

        	// Requête
        	PreparedStatement ps = con.prepareStatement("select * from commandes where cno = ?");
        	ps.setInt(1,id);
        	ResultSet rs = ps.executeQuery();

        	// Résultats
        	if (rs.next())
        	{
        		comm.setId(rs.getInt("cno"));
                comm.setUser(rs.getInt("uno"));
                comm.setDate(rs.getString("datecomm"));
                
                // Ingrédients
                Statement pizst = con.createStatement();
                ResultSet pizzas = pizst.executeQuery("select * from compizzas where cno = "+rs.getInt("cno"));

                float prixFinal = 0;
                while (pizzas.next()) {
                	Pizza piz = PizzaDAO.find(pizzas.getInt("pno"),context);
                	comm.getPizzas().add(piz);
                	prixFinal += piz.getPrixFinal();
                }
            	comm.setPrixFinal(prixFinal);
        	}

        } catch (Exception e){
        	System.out.println(e.getMessage());
        } finally {
        	try {con.close();} catch(Exception e2) {System.out.println(e2.getMessage());}
        }
        return comm;
    }
    
    public static Map<Integer,Commande> findall(ServletContext context){
        Connection con = null;
        Map<Integer,Commande> comms = new HashMap<>();

        try {
        	// Connexion BDD
			con = new DS(context).getConnection();

        	// Requête
        	Statement stmt = con.createStatement();
        	ResultSet rs = stmt.executeQuery("select * from commandes");
        	
        	// Résultats
        	while (rs.next())
        	{
        		Commande comm = new Commande();
                comm.setId(rs.getInt("cno"));
                comm.setUser(rs.getInt("uno"));
                comm.setDate(rs.getString("datecomm"));
        		
        		// Pizzas
                Statement pizst = con.createStatement();
                ResultSet pizzas = pizst.executeQuery("select * from compizzas where cno = "+rs.getInt("cno"));
                
                float prixFinal = 0;
                while (pizzas.next()) {
                	Pizza piz = PizzaDAO.find(pizzas.getInt("pno"),context);
                	comm.getPizzas().add(piz);
                	prixFinal += piz.getPrixFinal();
                }
            	comm.setPrixFinal(prixFinal);
                
                comms.put(comm.getId(),comm);
        	}

        } catch (Exception e){
        	System.out.println(e.getMessage());
        } finally {
        	try {con.close();} catch(Exception e2) {System.out.println(e2.getMessage());}
        }
        return comms;
    }
    
    public static void save(Commande comm, ServletContext context){
        Connection con = null;
        
        try {
        	// Connexion BDD
        	con = new DS(context).getConnection();

        	// Requête
        	PreparedStatement ps = con.prepareStatement("insert into commandes values (?,?,?)");
        	ps.setInt(1,comm.getId());
        	ps.setInt(2,comm.getUser());
        	ps.setString(3,comm.getDate());
        	ps.executeUpdate();
        	
        } catch (Exception e){
        	System.out.println(e.getMessage());
        } finally {
        	try {con.close();} catch(Exception e2) {System.out.println(e2.getMessage());}
        }
    }
}
