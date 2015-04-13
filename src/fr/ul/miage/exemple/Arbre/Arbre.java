package fr.ul.miage.exemple.Arbre;

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
}
