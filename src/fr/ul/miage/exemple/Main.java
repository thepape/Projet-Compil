/**
 * Classe qui appelle le parser
 * La lecture du flux de carractère se fait sur l'entréee standard
 */
package fr.ul.miage.exemple;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import fr.ul.miage.exemple.Arbre.Noeud;
import fr.ul.miage.exemple.TDS.TDS;
import fr.ul.miage.exemple.generated.ParserCup;
import fr.ul.miage.exemple.generated.Yylex;

/**
 * @author Pierre BURTEAUX
 * @author Romain PAPELIER
 *
 */
public class Main {
	
	public static int count = 1;
	public static boolean debugMode=true;
	public static Noeud racine = new Noeud("PROG");
	public static int currentContext=0;
	public static TDS tds = new TDS();
	public static int currentLine=1;
	
	public static String virerEspaces(String s)
	{
		String res = s;
		
		if(s.contains(" "))
		{
			res = s.replaceAll(" ", "");
		}
		
		return res;
	}
	
	public static void print(String st)
	{
		if(debugMode)
		System.out.println(st);
	}
	
	public static String[] addAll(String[] s1, String[] s2)
	{
		String[] res = new String[s1.length+s2.length];
		
		for(int i=0; i < s1.length; i++)
		{
			res[i] = s1[i];
		}
		
		for(int i=s1.length; i < res.length; i++)
		{
			res[i] = s2[i-s1.length];
		}
		
		return res;
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args)  {
		
		
		
		
		analyseFichier(new File("ex.miage"));
		
		
		/*
		String path = "Fichiers_test";
		
		File[] files = new File(path).listFiles();
				
		for (File file : files) {
			
			if(!(file.getName().equals(".DS_Store"))) {
				
				count = 1;
				racine = new Noeud("PROG");
				currentContext=0;
				tds = new TDS();
				currentLine=1;
				
				System.out.println("\n\nAnalyse : "+file.getName());
				
					//analyseFichier(file);
				
			}
		}
		*/

}


	public static void analyseFichier(File file){
		
		ParserCup parser;

		
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			parser = new ParserCup(new Yylex(fis));
		} catch (Exception e) {
			System.out.println("Fichier non trouvé ou illisible.");
			System.out.println("Entrez la chaine à analyser : ");
			parser = new ParserCup(new Yylex(System.in));
		}
		
	
		
	
		try {
			parser.parse();
			System.out.println("Fichier correctement analysé");
		
			tds.affiche();
			//System.out.println("test : "+tds.toString());

		} catch (Exception e) {
			System.err.println("...Erreur de syntaxe a la ligne "+Main.currentLine+" (un bloc commentaire compte pour 1 seule ligne)");
			e.printStackTrace();
			//System.exit(1);
		}

	
	}
}

