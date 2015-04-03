package fr.ul.miage.exemple;

import java.util.*;
import java.util.Map.Entry;

public class TDS {
	
	private HashMap<Integer, HashMap<String,Object>> table;
	
	private int count;
	
	public TDS()
	{
		table = new HashMap<Integer, HashMap<String,Object>>();
		count = 1;
	}
	
	public Object get(int num, String colonne)
	{
		if(num > 0 && num <= this.table.size())
			return this.table.get(num).get(colonne);
		else
			return null;
		
	}
	
	public boolean addVar(String varname, String type, int value)
	{
		Iterator<Entry<Integer, HashMap<String, Object>>> it = this.table.entrySet().iterator();
		Entry<Integer, HashMap<String, Object>> e;
		HashMap<String,Object> hm;
		
		while(it.hasNext())
		{
			e = it.next();
			hm = e.getValue();
			
			if(hm != null && hm.get("idf").equals(varname) && (Integer) hm.get("context") == Main.currentContext)
			{
				return false;
			}
			
		}
		
		HashMap<String,Object> entry = new HashMap<String,Object>();
		entry.put("num", new Integer(count++));
		entry.put("idf", varname);
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
		return this.addVar(varname, type, 0);
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
			
			if(hm != null && hm.get("idf").equals(varname) && (Integer) hm.get("context") == Main.currentContext)
			{
				return false;
			}
			
		}
		
		HashMap<String,Object> entry = new HashMap<String,Object>();
		entry.put("num", new Integer(count++));
		entry.put("idf", varname);
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
			
			if(hm != null && hm.get("idf").equals(funcname) && (Integer) hm.get("context") == Main.currentContext)
			{
				return false;
			}
			
		}
		
		HashMap<String,Object> entry = new HashMap<String,Object>();
		entry.put("num", new Integer(count++));
		entry.put("idf", funcname);
		entry.put("context", new Integer(Main.currentContext));
		entry.put("categ", "func");
		entry.put("type", type);
		entry.put("nbparam", 0);
		entry.put("nbloc", 0);
		this.table.put(count++, entry);
		return true;
	}

}
