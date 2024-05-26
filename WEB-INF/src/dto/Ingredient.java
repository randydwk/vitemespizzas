package dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Ingredient implements Serializable
{
	private int id;
	private String name;
	private float prix;

	public Ingredient() {}
	
	public Ingredient(int id, String name, float prix) {
		this.id = id;
		this.name = name;
		this.prix = prix;
	}
	
	public void setId(int id){this.id = id;}
	public void setName(String name){this.name = name;}
	public void setPrix(float prix){this.prix = prix;}

	public int getId(){return this.id;}
	public String getName(){return this.name;}
	public float getPrix(){return this.prix;}
	
	public String toString(){
		return "["+id+", "+name+", "+prix+"]";
	}
}
