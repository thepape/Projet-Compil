package fr.ul.miage.exemple.Arbre;

import java.util.ArrayList;

import fr.ul.miage.exemple.Main;
public class Noeud {
	
	public static int count = 0;
	public int id;
	public String type;
	public Integer valeur;
	public String tag = null;
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
		n.pere = this;
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
	
	public void corrigerNoeudsCall()
	{
		for(Noeud f : this.fils)
		{
			if(f.type.equals("CALL") && (f.valeur == null))
			{
				String funcName = f.tag;
				int fnum = Main.tds.find(funcName);
				f.valeur = fnum;
			}
			else if(f.fils.size() > 0)
			{
				f.corrigerNoeudsCall();
			}
		}
	}
	
	public void corrigerIDFnulls(int contexte) throws Exception
	{
		int cxt = contexte;
		
		if(this.type.equals("FUNC"))
		{
			cxt = this.valeur;
		}
				
		for(Noeud f : this.fils)
		{
			if(f.type.equals("IDF"))
			{
				String idfName = f.tag;
				Integer fnum = Main.tds.find(idfName, cxt);
				
				//si on trouve pas la variable pour le contexte, on cherche en global
				if(fnum == null)
					fnum = Main.tds.find(idfName);
				
				if(f.valeur == null)
					f.valeur = fnum;
				
				if(fnum == null)
				{
					throw new Exception("Variable "+idfName+" non declaree dans le contexte "+contexte);
				}
			}
			else if(f.fils.size() > 0)
			{
				f.corrigerIDFnulls(cxt);
			}
		}
	}
	
	public void modifierContexteDesFils(int contexte) throws Exception
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
				
				//on verifie s'il existe deja une variable definie du meme nom dans le meme contexte
				String fname = (String) Main.tds.get(f.valeur, "idf");
				Integer num_Of_First_Var_With_That_Name = Main.tds.find(fname, contexte);
				
				if(num_Of_First_Var_With_That_Name != f.valeur)
				{	
					String cxt = "global";
					if(contexte > 0)
						cxt = (String) Main.tds.get(contexte, "idf");
						
					throw new Exception("La variable "+fname+" est existe deja dans le contexte "+cxt);
				}
				
			}
			else if(f.type.equals("IDF"))
			{
				//on verifie s'il existe une variable locale pour le contexte
				//on recupere le nom de la variable du noeud IDF (qui n'est pas locale au depart)
				String name = (String) Main.tds.get(f.valeur, "idf");
				//on cherche s'il existe existe une variable locale du meme nom pour ce contexte
				Integer numVarLoc = Main.tds.find(name, contexte);
				
				//si oui
				if(numVarLoc != null)
				{
					//alors la variable appelee dans le noeud IDF sera la variable locale
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
