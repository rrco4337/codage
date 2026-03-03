package algorithmes;

/**
 * Algorithme de conversion d'un nombre d'une base vers une autre.
 *
 * Supporte les bases 2 à 36.
 * Les chiffres au-delà de 9 sont représentés par des lettres : A=10, B=11 … Z=35.
 *
 * Méthode :
 *   Étape 1 — base de départ  → décimal  (règle de Horner)
 *   Étape 2 — décimal          → base d'arrivée (divisions successives)
 */
public class ConversionBase {

    /**
     * Convertit {@code nombre} (exprimé en base {@code baseDepart})
     * vers la base {@code baseArrivee}.
     *
     * @param nombre      la chaîne à convertir (ex: "1A3F")
     * @param baseDepart  base source  (entre 2 et 36)
     * @param baseArrivee base cible   (entre 2 et 36)
     * @return            la représentation du nombre dans la base cible
     * @throws IllegalArgumentException si les paramètres sont invalides
     */
    public static String convertir(String nombre, int baseDepart, int baseArrivee) {

        // ── Vérifications ──────────────────────────────────────────────────────
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("Le nombre ne peut pas être vide.");
        if (baseDepart  < 2 || baseDepart  > 36)
            throw new IllegalArgumentException("Base de départ invalide (2–36).");
        if (baseArrivee < 2 || baseArrivee > 36)
            throw new IllegalArgumentException("Base d'arrivée invalide (2–36).");

        // Supprimer tous les espaces (permet d'écrire "1111 1111" ou "FF 3A")
        nombre = nombre.trim().replace(" ", "").replace("\t", "").toUpperCase();

        // ── Étape 1 : base de départ → décimal ────────────────────────────────
        long decimal = 0;
        for (int i = 0; i < nombre.length(); i++) {
            char c = nombre.charAt(i);
            int  valeurChiffre;

            if      (c >= '0' && c <= '9') valeurChiffre = c - '0';
            else if (c >= 'A' && c <= 'Z') valeurChiffre = c - 'A' + 10;
            else throw new IllegalArgumentException("Caractère invalide : '" + c + "'");

            if (valeurChiffre >= baseDepart)
                throw new IllegalArgumentException(
                    "Le chiffre '" + c + "' est trop grand pour la base " + baseDepart + ".");

            // Règle de Horner : accumule les chiffres de gauche à droite
            decimal = decimal * baseDepart + valeurChiffre;
        }

        // ── Étape 2 : décimal → base d'arrivée ────────────────────────────────
        if (decimal == 0) return "0";

        StringBuilder resultat = new StringBuilder();
        long valeur = decimal;
        while (valeur > 0) {
            int reste = (int)(valeur % baseArrivee);
            // reste < 10 → chiffre '0'–'9', sinon lettre 'A'–'Z'
            resultat.append(reste < 10 ? (char)('0' + reste) : (char)('A' + reste - 10));
            valeur /= baseArrivee;
        }
        // Les restes sont obtenus à l'envers → inverser
        return resultat.reverse().toString();
    }

    // Empêche l'instanciation (classe utilitaire)
    private ConversionBase() {}
}
