package bowling;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe a pour but d'enregistrer le nombre de quilles abattues lors des
 * lancers successifs d'<b>un seul et même</b> joueur, et de calculer le score
 * final de ce joueur
 */
public class PartieMonoJoueur {

	private final List<Tour> tours;
	private final CalculateurScore calculateurScore;
	private Tour tourCourant;

	/**
	 * Constructeur
	 */
	public PartieMonoJoueur() {
		this.tours = new ArrayList<>();
		this.calculateurScore = new CalculateurScore();
		this.tourCourant = new Tour(1);
	}

	/**
	 * Cette méthode doit être appelée à chaque lancer de boule
	 *
	 * @param nombreDeQuillesAbattues le nombre de quilles abattues lors de ce lancer
	 * @throws IllegalStateException si la partie est terminée
	 * @return vrai si le joueur doit lancer à nouveau pour continuer son tour, faux sinon	
	 */
	public boolean enregistreLancer(int nombreDeQuillesAbattues) {
		if (estTerminee()) {
			throw new IllegalStateException("La partie est terminée");
		}

		Lancer lancer = new Lancer(nombreDeQuillesAbattues);
		boolean tourContinue = tourCourant.ajouterLancer(lancer);

		// Si le tour est terminé, l'ajouter à la liste et passer au suivant
		if (tourCourant.estTermine()) {
			tours.add(tourCourant);
			
			// Créer le tour suivant si ce n'était pas le dernier
			if (tours.size() < 10) {
				tourCourant = new Tour(tours.size() + 1);
			} else {
				tourCourant = null; // Partie terminée
			}
			return false;
		}

		return tourContinue;
	}

	/**
	 * Cette méthode donne le score du joueur.
	 * Si la partie n'est pas terminée, on considère que les lancers restants
	 * abattent 0 quille.
	 * @return Le score du joueur
	 */
	public int score() {
		// Créer une copie des tours pour le calcul
		List<Tour> toursComplets = new ArrayList<>(tours);
		
		// Si la partie n'est pas terminée, simuler les tours restants avec 0 quilles
		if (!estTerminee()) {
			// Compléter le tour courant avec des 0
			if (tourCourant != null) {
				Tour tourSimule = simulerFinTour(tourCourant);
				toursComplets.add(tourSimule);
			}
			
			// Ajouter les tours restants avec des 0
			for (int i = toursComplets.size(); i < 10; i++) {
				Tour tourVide = new Tour(i + 1);
				tourVide.ajouterLancer(new Lancer(0));
				if (!tourVide.estTermine()) {
					tourVide.ajouterLancer(new Lancer(0));
				}
				toursComplets.add(tourVide);
			}
		}

		return calculateurScore.calculerScoreTotal(toursComplets);
	}

	/**
	 * Simule la fin d'un tour en cours avec des lancers à 0
	 */
	private Tour simulerFinTour(Tour tour) {
		Tour tourSimule = new Tour(tour.getNumeroTour());
		
		// Copier les lancers existants
		for (Lancer lancer : tour.getLancers()) {
			tourSimule.ajouterLancer(lancer);
		}
		
		// Compléter avec des lancers à 0
		while (!tourSimule.estTermine()) {
			tourSimule.ajouterLancer(new Lancer(0));
		}
		
		return tourSimule;
	}

	/**
	 * @return vrai si la partie est terminée pour ce joueur, faux sinon
	 */
	public boolean estTerminee() {
		return tours.size() == 10 && tourCourant == null;
	}

	/**
	 * @return Le numéro du tour courant [1..10], ou 0 si le jeu est fini
	 */
	public int numeroTourCourant() {
		if (estTerminee()) {
			return 0;
		}
		return tourCourant != null ? tourCourant.getNumeroTour() : 0;
	}

	/**
	 * @return Le numéro du prochain lancer pour tour courant [1..3], ou 0 si le jeu
	 *         est fini
	 */
	public int numeroProchainLancer() {
		if (estTerminee()) {
			return 0;
		}
		return tourCourant != null ? tourCourant.getNombreLancers() + 1 : 0;
	}

}
