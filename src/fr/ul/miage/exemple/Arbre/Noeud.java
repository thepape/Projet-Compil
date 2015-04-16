package fr.ul.miage.exemple.Arbre;

import java.util.ArrayList;
public class Noeud {
	
	public static int count = 0;
	public int id;
	public String type;
	public Integer valeur;
	public ArrayList<Noeud> fils;
	public Noeud pere;
	
	public Noeud(String t, Integer v)
	{
		this.id = Noeud.count++;
		this.type = t;
		this.valeur = v;
		fils = new ArrayList<Noeud>();
	}
	
	public Noeud(String e)
	{
		this.id = Noeud.count++;
		this.type = e;
		this.valeur = null;
		fils = new ArrayList<Noeud>();
	}

	public void ajouterFils(String e, Integer v)
	{
		this.fils.add(new Noeud(e,v));
	}
	
	public void empilerFils(Noeud n)
	{
		ArrayList<Noeud> newFils = new ArrayList<Noeud>();
		newFils.add(n);
		
		for(Noeud f : this.fils)
		{
			newFils.add(f);
		}
		
		this.fils = newFils;
	}
	
	public void supprimerFils(Noeud nas)
	{
		ArrayList<Noeud> newFils = new ArrayList<Noeud>();
		for(Noeud no : this.fils)
		{
			if(!no.equals(nas))
				newFils.add(no);
		}
		this.fils = newFils;
	}
	
	public void empilerFils(Object n)
	{
		this.empilerFils((Noeud) n); 
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
	
	public void empilerFilsDe(Noeud n)
	{
		for(Noeud f : n.fils)
		{
			this.empilerFils(f);
		}
	}

	
	public void ajouterFils(ArrayList<Noeud> l){
		for(Noeud n : l) {
			ajouterFils(n);
		}
	}
	
	

	public Noeud copy(){
		return new Noeud(type,valeur);
	}
	
	public boolean equals(Noeud n)
	{
		return(this.id == n.id);
	}
	
	/*
	public void affiche(){
		
		System.out.println("["+this.id+"]Noeud type : "+type+" | valeur : "+valeur+" | nb fils : "+fils.size()+" | "+this);
		
		for (Noeud n : fils){
			if(n!=null){
				System.out.println("----- ["+n.id+"]Noeud type : "+n.type+" | valeur : "+n.valeur+" | nb fils : "+n.fils.size()+" | "+n);
			} else {
				System.out.println("----- Noeud type : null !!");
			}
		}
		
		for (Noeud n : fils){
			if(n!=null && n.fils.size()>0) {
				n.affiche();
			} 
		}
	}
*/
	public void affiche()
	{
		this.affiche(0);
	}
	
	public void affiche(int niveau)
	{
		//creation d'un decallage par tabulation pour l'effet visuel
		StringBuffer sb = new StringBuffer("");
		
		for(int i=0; i < niveau;i++)
		{
			sb.append("\t\t");
		}
		
		String shift = sb.toString();
		
		//si c une feuille, on l'imprime avec le decallage
		if(this.fils.size() == 0)
		{
			System.out.println(shift+"["+id+"]( "+type+" | "+valeur+" )[]");
		}
		else
		{
			//sinon, on relance l'affichage recursif sur la premiere moitie des fils de ce noeuds un niveau de tab + loin
			int milieu = (int) Math.ceil((double) this.fils.size()/2); 
			
			for(int i = 0; i < milieu; i++)
			{
				this.fils.get(i).affiche(niveau+1);
			}
			//ensuite on imprime ce noeud
			StringBuffer sbFils = new StringBuffer("");
			
			for(int i=0; i < this.fils.size(); i++)
			{
				sbFils.append(this.fils.get(i).id+"|");
			}
			//on enleve le dernier "|"
			sbFils.deleteCharAt(sbFils.length()-1);
			
			System.out.println(shift+"["+id+"]( "+type+" | "+valeur+" ):["+sbFils.toString()+"]");
			
			for(int i = milieu; i < this.fils.size(); i++)
			{
				this.fils.get(i).affiche(niveau+1);
			}
		}
		
	}

}
