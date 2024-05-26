package dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Commande implements Serializable
{
	private int id;
	private int user;
	private String date;
	private float prixFinal;
	private List<Pizza> pizzas = new ArrayList<>();

	public Commande() {}
	
	public void setId(int id){this.id = id;}
	public void setUser(int user){this.user = user;}
	public void setDate(String date){this.date = date;}
	public void setPrixFinal(float prixFinal){this.prixFinal = prixFinal;}
	public void setPizzas(List<Pizza> pizzas){this.pizzas = pizzas;}
		
	public int getId(){return this.id;}
	public int getUser(){return this.user;}
	public String getDate(){return this.date;}
	public float getPrixFinal(){return this.prixFinal;}
	public List<Pizza> getPizzas(){return this.pizzas;}

	public String toString(){
		return "["+id+", "+user+", "+date+"]";
	}
}