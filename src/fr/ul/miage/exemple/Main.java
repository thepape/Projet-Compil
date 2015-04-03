/**
 * Classe qui appelle le parser
 * La lecture du flux de carractère se fait sur l'entréee standard
 */
package fr.ul.miage.exemple;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import fr.ul.miage.exemple.generated.ParserCup;
import fr.ul.miage.exemple.generated.Yylex;

/**
 * @author Pierre BURTEAUX
 * @author Romain PAPELIER
 *
 */
public class Main {
	
	public static int count = 1;
	public static boolean debugMode=false;
	public static Noeud racine = new Noeud("PROG");
	public static int currentContext=0;
	public static TDS tds = new TDS();
	
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
	 */
	public static void main(String[] args) {
		ParserCup parser;

		
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(new File("exemple.miage"));
				parser = new ParserCup(new Yylex(fis));
			} catch (FileNotFoundException e) {
				System.out.println("Fichier non trouvé ou illisible.");
				System.out.println("Entrez la chaine à analyser : ");
				parser = new ParserCup(new Yylex(System.in));
			}
			
		
			
		
		try {
			parser.parse();
			System.out.println("Fichier correctement analysé");
		} catch (Exception e) {
			System.err.println("...Erreur de syntaxe ");
			e.printStackTrace();
			System.exit(1);
		}
	}

}
