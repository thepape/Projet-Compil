package fr.ul.miage.exemple.Arbre;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import fr.ul.miage.exemple.Main;
import fr.ul.miage.exemple.TDS.TDS;

public class Arbre {
	
	private Noeud racine;
	
	public Arbre(Noeud r) {
		racine = r;
	}
	
	
	public void affiche(){
		
		System.out.println("\n\nAffichage de l'arbre : ");
		
		racine.affiche();
	}
	
	
	public Noeud getRacine() {
		return racine;
	}
	
	public void setRacine(Noeud r){
		racine = r;
	}
	
	public String generer_expression(Noeud n)
	{
		StringBuffer sb = new StringBuffer("");
		
		if(n.type.equals("CST"))
		{
			sb.append("\n CMOVE("+n.valeur+", R0)");
			sb.append("\n PUSH(R0)");
		}
		else if(n.type.equals("IDF"))
		{
			String categ = (String) Main.tds.get(n.valeur, "categ");
			Integer context = (Integer) Main.tds.get(n.valeur, "context");
			
			if(categ.equals("var") && context == 0)
			{
				//var globale
				sb.append("\n LD("+Main.tds.get(n.valeur, "idf")+", R0)");
				sb.append("\n PUSH(R0)");
				
				//code a confirmer...
			}
			else if(categ.equals("var") && context != 0)
			{
				//var locale
				
				//i have no fcking idea...
			}
			else if(categ.equals("param"))
			{
				int x =  (2 + (Integer) Main.tds.get(n.valeur, "rang")) * (-4);
				sb.append("\n GETFRAME("+x+", R0)");
				sb.append("\n PUSH(R0)");
			}
			
		}
		else if(n.type.equals("+") || n.type.equals("-") || n.type.equals("*") || n.type.equals("/"))
		{
			sb.append(this.generer_expression(n.fils.get(0)));
			sb.append(this.generer_expression(n.fils.get(1)));
			
			sb.append("\n POP(R2)");
			sb.append("\n POP(R1)");
			
			if(n.type.equals("+"))
				sb.append("\n ADD(R1,R2,R3)");
			else if(n.type.equals("-"))
				sb.append("\n SUB(R1,R2,R3)");
			else if(n.type.equals("*"))
				sb.append("\n MUL(R1,R2,R3)");
			else if(n.type.equals("/"))
				sb.append("\n ADD(R1,R2,R3)");
			
			sb.append("\n PUSH(R3)");
		}
		else if(n.type.equals("CALL"))
		{
			sb.append("\n ALLOCATE(1)");
			
			for(int i = n.fils.size()-1 ; i >= 0 ; i--)
			{
				sb.append(this.generer_expression(n.fils.get(i)));
			}
			
			sb.append("\n CALL("+Main.tds.get(n.valeur, "idf")+")");
			sb.append("\n DEALLOCATE("+Main.tds.get(n.valeur, "nbparam")+")");
		}
		else if(n.type.equals("READ"))
		{
			//read
		}
		
		return sb.toString();
	}
	
	public String generer_affectation(Noeud n)
	{
		StringBuffer res = new StringBuffer("");
		
		res.append(this.generer_expression(n.fils.get(1)));
		res.append("\n POP(R0)");
		res.append("\n ST(R0,"+Main.tds.get(n.valeur, "idf")+")");
		
		return res.toString();
	}
	
	public String generer_fonction1(Noeud n)
	{
		StringBuffer res = new StringBuffer("");
		
		for(Noeud f : n.fils)
		{
			if(f.type.equals("="))
			{
				res.append(this.generer_affectation(f));
			}
		}
		
		return res.toString();
	}
	
	public String generer_data()
	{
		StringBuffer res = new StringBuffer("");
		TDS tds = Main.tds;
		
		Iterator<Entry<Integer, HashMap<String, Object>>> it = tds.table.entrySet().iterator();
		Entry<Integer, HashMap<String, Object>> e;
		HashMap<String,Object> entree;
		
		while(it.hasNext())
		{
			e = it.next();
			entree = e.getValue();
			String entreeNom = (String) entree.get("idf");
			int entreeContext = (int) entree.get("context");
			String categ = (String) entree.get("categ");
			String type = (String) entree.get("type");
			Integer value = (Integer) entree.get("value");
			
			if(categ.equals("var") && entreeContext == 0 && type.equals("int"))
			{
				res.append("\n "+entreeNom+": LONG("+value+")");
			}
			
		}
		
		return res.toString();
	}
	
	public String generer_fonctions()
	{
		StringBuffer res = new StringBuffer();
		
		for(Noeud f : this.racine.fils)
		{
			res.append(this.generer_fonction1(f));
		}
		
		return res.toString();
	}
	
	public String generer_le_code()
	{
		StringBuffer res = new StringBuffer("");
		
		res.append("\n include beta.uasm.txt");
		res.append("\n CMOVE(pile,SP)");
		res.append("\n BR(debut)");
		
		res.append(this.generer_data());
		res.append("\n debut:");
		res.append("\n CALL(main)");
		res.append("\n HALT()");
		
		res.append(this.generer_fonctions());
		res.append("\n pile:");
		
		return res.toString();
	}
}
