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
	
	/**
	 * genere une expression
	 * @param n noeud racine de l'expression (+),(-),(CST),(CALL),...
	 * @return code asm
	 */
	public String generer_expression(Noeud n)
	{
		StringBuffer sb = new StringBuffer("\n|generer expression");
		
		if(n.type.equals("CST"))
		{
			sb.append("\n| CST:"+n.valeur);
			sb.append("\n CMOVE("+n.valeur+", R0)");
			sb.append("\n PUSH(R0)");
		}
		else if(n.type.equals("IDF"))
		{
			sb.append("\n| IDF:"+Main.tds.get(n.valeur, "idf"));
			String categ = (String) Main.tds.get(n.valeur, "categ");
			Integer context = (Integer) Main.tds.get(n.valeur, "context");
			
			if(categ.equals("var") && context == 0)
			{
				//var globale
				sb.append("\n LD("+Main.tds.get(n.valeur, "idf")+", R0)");
				sb.append("\n PUSH(R0)");
				
			}
			else if(categ.equals("var") && context != 0)
			{
				//var locale
				int x =  (0 + (Integer) Main.tds.get(n.valeur, "rang")) * (4);
				sb.append("\n GETFRAME("+x+", R0)");
				sb.append("\n PUSH(R0)");
			}
			else if(categ.equals("param"))
			{
				int x =  (3 + (Integer) Main.tds.get(n.valeur, "rang")) * (-4);
				sb.append("\n GETFRAME("+x+", R0)");
				sb.append("\n PUSH(R0)");
			}
			
		}
		else if(n.type.equals("+") || n.type.equals("-") || n.type.equals("*") || n.type.equals("/"))
		{
			sb.append("\n| OP:"+n.type);
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
				sb.append("\n DIV(R1,R2,R3)");
			
			sb.append("\n PUSH(R3)");
		}
		else if(n.type.equals("CALL"))
		{
			//sb.append("\n ALLOCATE("+Main.tds.get(n.valeur, "nbloc")+")|allocation des variables locales");
			if(!Main.tds.get(n.valeur, "type").equals("void"))
			{
				sb.append("\n ALLOCATE(1) |+allocation de la valeur de retour");
			}
			
			for(int i = n.fils.size()-1 ; i >= 0 ; i--)
			{
				sb.append(this.generer_expression(n.fils.get(i)));
			}
			
			sb.append("\n CALL("+Main.tds.get(n.valeur, "idf")+")");
			//sb.append("\n DEALLOCATE("+Main.tds.get(n.valeur, "nbloc")+") |desallocation des variables locales");
		}
		else if(n.type.equals("READ"))
		{
			//read
		}
		
		return sb.toString();
	}
	
	/**
	 * genere le code permettant d'affecter une valeur a une variable / un parametre
	 * @param n noeud racine de l'affectation (=)
	 * @return code asm
	 */
	public String generer_affectation(Noeud n)
	{
		StringBuffer res = new StringBuffer("\n|generer affectation");
		res.append("\n| "+Main.tds.get(n.fils.get(0).valeur, "idf")+" = ");
		
		String categ = (String) Main.tds.get(n.fils.get(0).valeur, "categ");
		Integer context = (Integer) Main.tds.get(n.fils.get(0).valeur, "context");
		
		//on genere l'expression du fils de droite
		res.append(this.generer_expression(n.fils.get(1)));
		//on recupere sa valeur
		res.append("\n POP(R0)");
		
		if(categ.equals("var") && context == 0)
		{
			//var globale, on stocke la valeur dans le label ecrit via generer_data
			res.append("\n ST(R0,"+Main.tds.get(n.fils.get(0).valeur, "idf")+")");
			
			
		}
		else if(categ.equals("var") && context != 0)
		{
			//var locale, on stocke la valeur SUR le BP, dans la pile
			int x =  (0 + (Integer) Main.tds.get(n.fils.get(0).valeur, "rang")) * (4);
			res.append("\n PUTFRAME(R0,"+x+")");;
		}
		else if(categ.equals("param"))
		{
			//paremetre, on stocle la valeur SOUS le BP, dans la pile
			int x =  (2 + (Integer) Main.tds.get(n.fils.get(0).valeur, "rang")) * (-4);
			res.append("\n PUTFRAME(R0,"+x+")");
			
		}
		
		return res.toString();
	}
	
	/**
	 * genere une instruction de code
	 * @param n noeud racine de l'instruction (=), (CALL), (IF), ...
	 * @param contexte contexte dans lequel se trouve l'instruction
	 * @return code asm
	 */
	public String generer_instruction(Noeud n, int contexte)
	{
		StringBuffer res = new StringBuffer("\n|generer instruction");
		
		
			if(n.type.equals("="))
			{
				
				res.append(this.generer_affectation(n));
			}
			else if(n.type.equals("CALL"))
			{
				
				
				//pour chaque argument de la fonction, en partant de la fin,
				for(int i = n.fils.size()-1 ; i >= 0 ; i--)
				{
					//on recupere sa valeur
					res.append(this.generer_expression(n.fils.get(i)));
					/*res.append("\n POP(R0)");
					//et on la stocke dans les parametres de la fonction
					int x = ((2 + (int) Main.tds.get(n.fils.get(i).valeur, "rang"))*(-4));
					res.append("\n PUTFRAME(R0,"+x+")");*/
				}
				
				res.append("\n CALL("+Main.tds.get(n.valeur, "idf")+")");
				//res.append("\n DEALLOCATE("+Main.tds.get(n.valeur, "nbparam")+") |desallocation des parametres");
			}
			else if(n.type.equals("RET"))
			{
				res.append(this.generer_expression(n.fils.get(0)));
				res.append("\n POP(R0)");
				res.append("\n PUTFRAME(R0,"+((3 + (int) Main.tds.get(contexte, "nbparam")) * (-4))+")");
				res.append("\n BR(ret_"+Main.tds.get(contexte, "idf")+")");
			}
		
		return res.toString();
	}
	
	/**
	 * genere le code d'une declaration de fonction
	 * @param n noeud racine de la fonction ('FUNC')
	 * @return code asm
	 */
	public String generer_fonction1(Noeud n)
	{
		StringBuffer res = new StringBuffer("\n|generer la fonction");
		res.append("\n"+Main.tds.get(n.valeur, "idf")+":");
		//push(lp) permet de garder en memoire la position ou le pointeur de lecture doit retourner a la fin de la fonction
		res.append("\n PUSH(LP)");
		//push(bp) permet de definir la base de la partie de la pile dediee a la fonction
		res.append("\n PUSH(BP)");
		res.append("\n MOVE(SP,BP)");
		//on alloue autant de places en pile que le nombre de variables locales (situees SUR BP dans la pile)
		res.append("\n ALLOCATE("+Main.tds.get(n.valeur, "nbloc")+") |allocation des variables locales");
		
		//pour chaque variable locale de la fonction dans la TDS, on la met dans les emplacements alloues
		//via le allocate precedent via un PUTFRAME(valeur, var.rang*4)
		Iterator<Entry<Integer, HashMap<String, Object>>> it = Main.tds.table.entrySet().iterator();
		Entry<Integer, HashMap<String, Object>> e;
		HashMap<String,Object> entree;
		
		while(it.hasNext())
		{
			e = it.next();
			entree = e.getValue();
			
			if(entree != null && entree.get("categ").equals("var") && entree.get("context").equals(n.valeur))
			{
				res.append("\n CMOVE("+Main.tds.get((Integer) entree.get("num"), "value")+",R0)");
				int x =  (0 + (Integer) Main.tds.get((Integer) entree.get("num"), "rang")) * (4);
				res.append("\n PUTFRAME(R0,"+x+")");
			}
			
		}
		
		//on genere les instructions presentes dans la fonction
		for(Noeud f : n.fils)
		{
			
				res.append(this.generer_instruction(f, n.valeur));
		}
		res.append("\nret_"+Main.tds.get(n.valeur, "idf")+":");
		//on retire de la pile les variables locales (on en a pu besoin)
		res.append("\n DEALLOCATE("+Main.tds.get(n.valeur, "nbloc")+") |desallocation des variables locales");
		//on enleve l'indicateur de base pile local
		res.append("\n POP(BP)");
		//on recupere le numero d'instruction qui nous permet de retourner dans le code appelant
		res.append("\n POP(LP)");
		//on retire de la pile les arguments de la fonction
		res.append("\n DEALLOCATE("+Main.tds.get(n.valeur, "nbparam")+") |desallocation des variables parametres effectifs");
		res.append("\nRTN()");
		return res.toString();
	}
	
	/**
	 * genere le code assembleur relativ aux variables globales de la TDS
	 * @return code ASM
	 */
	public String generer_data()
	{
		StringBuffer res = new StringBuffer("\n|generer data");
		TDS tds = Main.tds;
		
		Iterator<Entry<Integer, HashMap<String, Object>>> it = tds.table.entrySet().iterator();
		Entry<Integer, HashMap<String, Object>> e;
		HashMap<String,Object> entree;
		
		//pour chaque variable dans la tds...
		while(it.hasNext())
		{
			e = it.next();
			entree = e.getValue();
			String entreeNom = (String) entree.get("idf");
			int entreeContext = (int) entree.get("context");
			String categ = (String) entree.get("categ");
			String type = (String) entree.get("type");
			Integer value = (Integer) entree.get("value");
			
			//...si elle est globale ( et de type int)
			if(categ.equals("var") && entreeContext == 0 && type.equals("int"))
			{
				res.append("\n "+entreeNom+": LONG("+value+")");
			}
			
		}
		
		return res.toString();
	}
	
	/**
	 * genere les fonctions du code (fonction main et autres fonctions) 
	 * INDISPENSABLE
	 * @return code assembleur
	 */
	public String generer_fonctions()
	{
		StringBuffer res = new StringBuffer("\n|generer fonctions");
		
		//pour chaque fils du noeud racine "PROG"
		for(Noeud f : this.racine.fils)
		{
			if(f.type.equals("FUNC"))
				res.append(this.generer_fonction1(f));
		}
		
		return res.toString();
	}
	
	/**
	 * methode qui genere le code assembleur
	 * @return code assembleur
	 */
	public String generer_le_code()
	{
		StringBuffer res = new StringBuffer("\n|generer le code");
		
		res.append("\n .include beta.uasm.txt");
		res.append("\n CMOVE(pile,SP)");
		//on va au debut du programme global
		res.append("\n BR(debut)");
		
		//generation de la tds (variables globales)
		res.append(this.generer_data());
		
		//appel de la fonction Main (fonction principale du code)
		res.append("\n debut:");
		res.append("\n PUSH(LP)");
		res.append("\n CALL(main)");
		res.append("\n POP(LP)");
		res.append("\n HALT()");
		
		//on genere les fonctions presentes dans le code
		res.append(this.generer_fonctions());
		res.append("\n pile:");
		
		return res.toString();
	}
}
