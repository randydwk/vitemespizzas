package dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Pizza implements Serializable
{
	private int id;
	private String name;
	private String type;
	private float prixBase;
	private float prixFinal;
	private List<Ingredient> ings = new ArrayList<>();
	
	public Pizza() {}
	
	public Pizza(int id, String name, String type, float prixBase, List<Ingredient> ings) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.prixBase = prixBase;
		this.ings = ings;
	}

	public void setId(int id){this.id = id;}
	public void setName(String name){this.name = name;}
	public void setType(String type){this.type = type;}
	public void setPrixBase(float prixBase){this.prixBase = prixBase;}
	public void setPrixFinal(float prixFinal){this.prixFinal = prixFinal;}
	public void setIngs(List<Ingredient> ings){this.ings = ings;}

	public int getId(){return this.id;}
	public String getName(){return this.name;}
	public String getType(){return this.type;}
	public float getPrixBase(){return this.prixBase;}
	public float getPrixFinal(){return this.prixFinal;}
	public List<Ingredient> getIngs(){return this.ings;}
	
	public boolean contains(int ingID) {
		for (Ingredient ing : this.ings) {
			if (ing.getId() == ingID) return true;
		}
		return false;
	}

	public String toString(){
		return "["+id+", "+name+", "+type+", "+prixBase+", "+prixFinal+", "+ings.toString()+"]";
	}
}
