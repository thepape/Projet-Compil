package fr.ul.miage.exemple.Arbre;

import java.util.ArrayList;
public class Noeud {
	
	public static int count = 0;
	public int id;
	public String type;
	public String valeur;
	public ArrayList<Noeud> fils;
	public Noeud pere;
	
	public Noeud(String e, String v)
	{
		this.id = Noeud.count++;
		this.type = e;
		this.valeur = v;
		fils = new ArrayList<Noeud>();
	}
	
	public Noeud(String e)
	{
		this.id = Noeud.count++;
		this.type = e;
		this.valeur = "";
		fils = new ArrayList<Noeud>();
	}

	public void ajouterFils(String e, String v)
	{
		this.fils.add(new Noeud(e,v));
	}
	
	public void ajouterFils(Noeud n)
	{
		this.fils.add(n);
	}
	
	public void ajouterFils(Object n)
	{
		this.fils.add((Noeud) n);
	}
	
	public void ajouterFilsDe(Noeud n)
	{
		for(Noeud f : n.fils)
		{
			this.ajouterFils(f);
		}
	}


	
	public void affiche(){
		
		System.out.println("Noeud type : "+type+" | valeur : "+valeur+" | nb fils : "+fils.size()+" | "+this);
		
		for (Noeud n : fils){
			System.out.println("----- Noeud type : "+n.type+" | valeur : "+n.valeur+" | nb fils : "+n.fils.size()+" | "+n);
		}
		
		for (Noeud n : fils){
			n.affiche();
		}
	}



}
