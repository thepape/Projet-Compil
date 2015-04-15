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
		String res = s;
		
		if(s.contains(" "))
		{
			res = s.replaceAll(" ", "");
		}
		
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
			
			if((entree != null) && entreeNom.equals(name) && (entreeContext == Main.currentContext))
			{
				return e.getKey();
			}
			
		}
		
		return null;
	}
	
	public Object get(int num, String col)
	{
		String colonne = this.virerEspaces(col);
		
		if(num > 0 && num <= this.table.size())
			return this.table.get(num).get(colonne);
		else
			return null;
		
	}
	
	
	public boolean addVar(String varname, String type, String value) {

		try {
			 int x = Integer.parseInt(value);
			 addVar( varname, type, x);
			 
		} catch(Exception e) {
			return false;
		}
		return true;
	}

	
	
	
	public void addVars(String[] varnames, String type, String value)
	{
		for(int i = 0; i < varnames.length; i++)
		{
			this.addVar(varnames[i], type, value);
		}
	}
	
	
	
	
	
	public boolean addVar(String varname, String type, int value)
	{
		System.out.println("On ajoute "+varname+" avec val = "+value);

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
		
		HashMap<String,Object> entry = new HashMap<String,Object>();
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
		
		return true;
	}
	
	public void addVars(String[] varnames, String type, int value)
	{
		for(int i = 0; i < varnames.length; i++)
		{
			this.addVar(varnames[i], type, value);
		}
	}
	
	public boolean addVar(String varname, String type)
	{
		return this.addVar(this.virerEspaces(varname), type, 0);
	}
	
	public boolean addParam(String varname, String type)
	{
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
		
		HashMap<String,Object> entry = new HashMap<String,Object>();
		entry.put("num", new Integer(count++));
		entry.put("idf", this.virerEspaces(varname));
		entry.put("context", new Integer(Main.currentContext));
		entry.put("categ", "param");
		entry.put("type", type);
		
		Integer nbparam = (Integer) this.get(Main.currentContext, "nbparam");
		entry.put("rang", nbparam);
		this.table.get(Main.currentContext-1).put("nbparam", ++nbparam);
		
		this.table.put(count++, entry);
		
		return true;
	}
	
	public boolean addFunc(String funcname, String type)
	{
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
			
		}
		
		HashMap<String,Object> entry = new HashMap<String,Object>();
		entry.put("num", new Integer(count++));
		entry.put("idf", this.virerEspaces(funcname));
		entry.put("context", new Integer(Main.currentContext));
		entry.put("categ", "func");
		entry.put("type", type);
		entry.put("nbparam", 0);
		entry.put("nbloc", 0);
		this.table.put(count++, entry);
		return true;
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
