package fr.ul.miage.exemple.TDS;

import java.util.*;
import java.util.Map.Entry;

import fr.ul.miage.exemple.Main;

public class TDS {
	
	private HashMap<Integer, HashMap<String,Object>> table;
	
	private int count;
	
	public TDS()
	{
		table = new HashMap<Integer, HashMap<String,Object>>();
		count = 1;
	}
	
	private String virerEspaces(String s)
	{
		System.out.println("varname : '"+s+"'");

		String res = s;
		
		if(s.contains(" "))
		{
			res = s.replaceAll(" ", "");
		}
		System.out.println("res : '"+res+"'");
		return res;
	}
	
	/**
	 * retourne le numero d'une variable ou fonction dans la TDS en fonction de son nom (idf)
	 * @param name nom de la variable ou fonction
	 * @return id numerique
	 */
	public Integer find(String pname)
	{
		String name = this.virerEspaces(pname);
		Iterator<Entry<Integer, HashMap<String, Object>>> it = this.table.entrySet().iterator();
		Entry<Integer, HashMap<String, Object>> e;
		HashMap<String,Object> entree;
		
		while(it.hasNext())
		{
			e = it.next();
			entree = e.getValue();
			String entreeNom = (String) entree.get("idf");
			int entreeContext = (int) entree.get("context");
			
			if((entree != null) && entreeNom.equals(name))
			{
				return e.getKey();
			}
			
		}
		
		return null;
	}
	
	public Integer find(String pname, int contexte)
	{
		if(pname == null)
			return null;
		
		String name = this.virerEspaces(pname);
		Iterator<Entry<Integer, HashMap<String, Object>>> it = this.table.entrySet().iterator();
		Entry<Integer, HashMap<String, Object>> e;
		HashMap<String,Object> entree;
		
		while(it.hasNext())
		{
			e = it.next();
			entree = e.getValue();
			String entreeNom = (String) entree.get("idf");
			int entreeContext = (int) entree.get("context");
			
			if((entree != null) && entreeNom.equals(name) && (entreeContext == contexte))
			{
				return e.getKey();
			}
			
		}
		
		return null;
	}
	
	public Object get(Integer num, String col)
	{
		String colonne = this.virerEspaces(col);
		
		if(num != null && num > 0 && num <= this.table.size())
			return this.table.get(num).get(colonne);
		else
			return null;
		
	}
	
	public void set(int num, String col, Object value)
	{
		String colonne = this.virerEspaces(col);
		
		if(num > 0 && num <= this.table.size())
			this.table.get(num).put(colonne, value);
		
	}
	
	
	public int addVar(String varname, String type, String value) {
		int res = -1;
		try {
			 int x = Integer.parseInt(value);
			 res = addVar( varname, type, x);
			 
		} catch(Exception e) {
			return -1;
		}
		return res;
	}

	
	
	/*
	public void addVars(String[] varnames, String type, String value)
	{
		for(int i = 0; i < varnames.length; i++)
		{
			this.addVar(varnames[i], type, value);
		}
	}
	
	*/
	
	
	
	public int addVar(String varname, String type, int value)
	{
		System.out.println("On ajoute "+varname+" avec val = "+value);
		/*
		Iterator<Entry<Integer, HashMap<String, Object>>> it = this.table.entrySet().iterator();
		Entry<Integer, HashMap<String, Object>> e;
		HashMap<String,Object> hm;
		
		while(it.hasNext())
		{
			e = it.next();
			hm = e.getValue();
			
			if(hm != null && hm.get("idf").equals(this.virerEspaces(varname)) && (Integer) hm.get("context") == Main.currentContext)
			{
				return -1;
			}
			
		}*/
		
		HashMap<String,Object> entry = new HashMap<String,Object>();
		int num = count;
		entry.put("num", new Integer(count));
		entry.put("idf", this.virerEspaces(varname));
		entry.put("context", new Integer(Main.currentContext));
		entry.put("categ", "var");
		entry.put("type", type);
		entry.put("value", value);
		
		if(Main.currentContext != 0)
		{
			Integer nbloc = (Integer) this.get(Main.currentContext,"nbloc");
			entry.put("rang", nbloc);
			this.table.get(Main.currentContext).put("nbloc", ++nbloc);
		}
		
		this.table.put(count++, entry);
		
		return num;
	}
	/*
	public void addVars(String[] varnames, String type, int value)
	{
		for(int i = 0; i < varnames.length; i++)
		{
			this.addVar(varnames[i], type, value);
		}
	}*/
	
	public int addVar(String varname, String type)
	{
		return this.addVar(this.virerEspaces(varname), type, 0);
	}
	
	public int addParam(String varname, String type, int context)
	{	/*
		Iterator<Entry<Integer, HashMap<String, Object>>> it = this.table.entrySet().iterator();
		Entry<Integer, HashMap<String, Object>> e;
		HashMap<String,Object> hm;
		
		while(it.hasNext())
		{
			e = it.next();
			hm = e.getValue();
			
			if(hm != null && hm.get("idf").equals(this.virerEspaces(varname)) && (Integer) hm.get("context") == Main.currentContext)
			{
				return false;
			}
			
		}
		*/
		HashMap<String,Object> entry = new HashMap<String,Object>();
		int num = count;
		entry.put("num", new Integer(count));
		entry.put("idf", this.virerEspaces(varname));
		entry.put("context", context);
		entry.put("categ", "param");
		entry.put("type", type);
		
		Integer nbparam = (Integer) this.get(context, "nbparam");
		entry.put("rang", nbparam);
		this.table.get(context).put("nbparam", ++nbparam);
		
		this.table.put(count++, entry);
		
		return num;
	}
	
	public int addFunc(String funcname, String type)
	{
		/*
		Iterator<Entry<Integer, HashMap<String, Object>>> it = this.table.entrySet().iterator();
		Entry<Integer, HashMap<String, Object>> e;
		HashMap<String,Object> hm;
		
		while(it.hasNext())
		{
			e = it.next();
			hm = e.getValue();
			
			if(hm != null && hm.get("idf").equals(this.virerEspaces(funcname)) && (Integer) hm.get("context") == Main.currentContext)
			{
				return false;
			}
			
		}*/
		
		HashMap<String,Object> entry = new HashMap<String,Object>();
		int num = count;
		entry.put("num", new Integer(count));
		entry.put("idf", this.virerEspaces(funcname));
		entry.put("context", new Integer(Main.currentContext));
		entry.put("categ", "func");
		entry.put("type", type);
		entry.put("nbparam", 0);
		entry.put("nbloc", 0);
		this.table.put(count++, entry);
		return num;
	}

	
	
	public void affiche(){

		String s = "\nTDS : \n";
		
		for(int i=1; i<= table.size() ; i++) {
			s = s+" - "+i+" - ";
			HashMap<String,Object> h = table.get(i);
			s = s + h;
			s = s+"\n";
		}
	
		System.out.println(s);
	}
	
}
