package fr.ul.miage.exemple.Arbre;

import java.util.ArrayList;

import fr.ul.miage.exemple.Main;
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
	
	public void modifierContexteDesFils(int contexte)
	{
		for(Noeud f : this.fils)
		{
			if(f.type.equals("DEF"))
			{
				//on change le contexte de la variable locale
				Main.tds.set(f.valeur, "context", contexte);
				
				//on change le nombre de var locales de la fonction
				int nbloc = (Integer) Main.tds.get(contexte, "nbloc");
				Main.tds.set(contexte, "nbloc", ++nbloc);
				
			}
			else if(f.type.equals("IDF"))
			{
				//on verifie s'il existe une variable locale pour le contexte
				//on recupere le nom de la variable du noeud IDF
				String name = (String) Main.tds.get(f.valeur, "idf");
				//on cherche s'il existe existe une variable locale du meme nom
				Integer numVarLoc = Main.tds.find(name, contexte);
				
				//si oui
				if(numVarLoc != null)
				{
					f.valeur = numVarLoc;
				}
			}
			else if(f.fils.size() > 0)
			{
				f.modifierContexteDesFils(contexte);
			}
		}
	}
	
	public void supprimerNoeudsDef()
	{
		for(int i = 0; i < this.fils.size(); i++)
		{
			if(this.fils.get(i) != null)
			{
				if(this.fils.get(i).type.equals("DEF"))
				{
					this.fils.remove(i);
					i--;
				}
				else if(this.fils.get(i).fils.size() > 0)
				{
					this.fils.get(i).supprimerNoeudsDef();
				}
			}
		}
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
			if(valeur != null)
				System.out.println(shift+"["+id+"]( "+type+" | "+valeur+" )[]");
			else
				System.out.println(shift+"["+id+"]( "+type+" )[]");
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
			
			if(valeur != null)
				System.out.println(shift+"["+id+"]( "+type+" | "+valeur+" ):["+sbFils.toString()+"]");
			else
				System.out.println(shift+"["+id+"]( "+type+" ):["+sbFils.toString()+"]");
			
			for(int i = milieu; i < this.fils.size(); i++)
			{
				this.fils.get(i).affiche(niveau+1);
			}
		}
		
	}

}
