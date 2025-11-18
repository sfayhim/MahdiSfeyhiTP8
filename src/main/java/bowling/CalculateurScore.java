package bowling;

import java.util.List;

/**
 * Classe responsable du calcul des scores au bowling
 */
public class CalculateurScore {

    /**
     * Calcule le score total d'une partie
     * @param tours la liste des tours de la partie
     * @return le score total
     */
    public int calculerScoreTotal(List<Tour> tours) {
        int scoreTotal = 0;
        
        for (int i = 0; i < tours.size(); i++) {
            Tour tour = tours.get(i);
            
            if (tour.estDernierTour()) {
                // Le dernier tour a ses propres règles
                scoreTotal += calculerScoreDernierTour(tour);
            } else {
                scoreTotal += calculerScoreTour(tour, tours, i);
            }
        }
        
        return scoreTotal;
    }

    /**
     * Calcule le score d'un tour (hors dernier tour)
     * @param tour le tour à calculer
     * @param tours la liste de tous les tours
     * @param indexTour l'index du tour dans la liste
     * @return le score du tour
     */
    private int calculerScoreTour(Tour tour, List<Tour> tours, int indexTour) {
        int score = tour.getQuillesAbattuesTour();
        
        if (tour.estStrike()) {
            // Strike : ajouter les 2 prochains lancers
            score += obtenirBonusStrike(tours, indexTour);
        } else if (tour.estSpare()) {
            // Spare : ajouter le prochain lancer
            score += obtenirBonusSpare(tours, indexTour);
        }
        
        return score;
    }

    /**
     * Calcule le score du dernier tour
     * @param dernierTour le dernier tour
     * @return le score du dernier tour
     */
    private int calculerScoreDernierTour(Tour dernierTour) {
        // Au dernier tour, on compte simplement toutes les quilles abattues
        return dernierTour.getQuillesAbattuesTour();
    }

    /**
     * Obtient le bonus pour un strike (2 prochains lancers)
     * @param tours la liste des tours
     * @param indexTourStrike l'index du tour avec strike
     * @return le bonus du strike
     */
    private int obtenirBonusStrike(List<Tour> tours, int indexTourStrike) {
        int bonus = 0;
        int lancersComptes = 0;
        
        // Chercher les 2 prochains lancers
        for (int i = indexTourStrike + 1; i < tours.size() && lancersComptes < 2; i++) {
            Tour tourSuivant = tours.get(i);
            List<Lancer> lancers = tourSuivant.getLancers();
            
            for (int j = 0; j < lancers.size() && lancersComptes < 2; j++) {
                bonus += lancers.get(j).getQuillesAbattues();
                lancersComptes++;
            }
        }
        
        return bonus;
    }

    /**
     * Obtient le bonus pour un spare (1 prochain lancer)
     * @param tours la liste des tours
     * @param indexTourSpare l'index du tour avec spare
     * @return le bonus du spare
     */
    private int obtenirBonusSpare(List<Tour> tours, int indexTourSpare) {
        // Chercher le prochain lancer
        for (int i = indexTourSpare + 1; i < tours.size(); i++) {
            Tour tourSuivant = tours.get(i);
            List<Lancer> lancers = tourSuivant.getLancers();
            
            if (!lancers.isEmpty()) {
                return lancers.get(0).getQuillesAbattues();
            }
        }
        
        return 0; // Aucun lancer suivant trouvé
    }
}
