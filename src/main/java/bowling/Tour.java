package bowling;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un tour de bowling
 */
public class Tour {
    private final List<Lancer> lancers;
    private final int numeroTour;
    private final boolean estDernierTour;

    /**
     * Constructeur d'un tour
     * @param numeroTour le numéro du tour (1-10)
     */
    public Tour(int numeroTour) {
        this.numeroTour = numeroTour;
        this.estDernierTour = (numeroTour == 10);
        this.lancers = new ArrayList<>();
    }

    /**
     * Ajoute un lancer au tour
     * @param lancer le lancer à ajouter
     * @return true si le tour continue (le joueur doit lancer à nouveau)
     */
    public boolean ajouterLancer(Lancer lancer) {
        lancers.add(lancer);

        // Si c'est le dernier tour, les règles sont différentes
        if (estDernierTour) {
            return gererDernierTour(lancer);
        }

        // Pour les tours 1-9
        // Si c'est un strike au premier lancer, le tour est fini
        if (lancers.size() == 1 && lancer.estStrike()) {
            return false;
        }

        // Si on a fait 2 lancers, le tour est fini
        if (lancers.size() == 2) {
            return false;
        }

        // Sinon, le tour continue
        return true;
    }

    /**
     * Gère la logique spéciale du dernier tour
     */
    private boolean gererDernierTour(Lancer lancer) {
        if (lancers.size() == 1) {
            // Premier lancer du dernier tour
            if (lancer.estStrike()) {
                return true; // Strike au dernier tour -> droit à 2 lancers supplémentaires
            }
            return true; // Premier lancer normal -> continue
        } else if (lancers.size() == 2) {
            // Deuxième lancer du dernier tour
            if (estStrike() || estSpare()) {
                return true; // Strike ou spare au dernier tour -> droit à 1 lancer supplémentaire
            }
            return false; // Tour normal terminé
        } else {
            // Troisième lancer (seulement possible au dernier tour)
            return false;
        }
    }

    /**
     * @return true si ce tour est terminé
     */
    public boolean estTermine() {
        if (estDernierTour) {
            // Dernier tour : terminé après 2 lancers (sauf strike/spare)
            if (lancers.size() == 2 && !estStrike() && !estSpare()) {
                return true;
            }
            // Dernier tour : terminé après 3 lancers
            return lancers.size() == 3;
        } else {
            // Tours 1-9 : terminé après strike ou 2 lancers
            return (lancers.size() == 1 && estStrike()) || lancers.size() == 2;
        }
    }

    /**
     * @return true si ce tour est un strike
     */
    public boolean estStrike() {
        return !lancers.isEmpty() && lancers.get(0).estStrike();
    }

    /**
     * @return true si ce tour est un spare
     */
    public boolean estSpare() {
        if (lancers.size() < 2) {
            return false;
        }
        if (estStrike()) {
            return false; // Un strike n'est pas un spare
        }
        return getQuillesAbattuesTour() == 10;
    }

    /**
     * @return le nombre total de quilles abattues dans ce tour
     */
    public int getQuillesAbattuesTour() {
        return lancers.stream().mapToInt(Lancer::getQuillesAbattues).sum();
    }

    /**
     * @return la liste des lancers de ce tour
     */
    public List<Lancer> getLancers() {
        return new ArrayList<>(lancers);
    }

    /**
     * @return le numéro de ce tour
     */
    public int getNumeroTour() {
        return numeroTour;
    }

    /**
     * @return le nombre de lancers effectués dans ce tour
     */
    public int getNombreLancers() {
        return lancers.size();
    }

    /**
     * @return true si c'est le dernier tour (tour 10)
     */
    public boolean estDernierTour() {
        return estDernierTour;
    }
}
