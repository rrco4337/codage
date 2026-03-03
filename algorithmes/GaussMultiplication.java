package algorithmes;

import java.util.ArrayList;
import java.util.List;

/**
 * Algorithme de multiplication binaire par décalages — méthode de Gauss.
 *
 * Principe :
 *   On multiplie A × B sans faire de vraie multiplication.
 *   On utilise seulement des additions et des décalages de bits.
 *
 *   résultat = 0
 *   Tant que B > 0 :
 *     ① Regarder le dernier bit (bit le plus à droite) de B.
 *     ② Si ce bit vaut 1 → ajouter A au résultat.
 *        Si ce bit vaut 0 → ne rien ajouter.
 *     ③ Décaler A vers la gauche  (A = A × 2).
 *     ④ Décaler B vers la droite  (B = B ÷ 2 → le dernier bit disparaît).
 *     ⑤ Recommencer jusqu'à ce que B = 0.
 */
public class GaussMultiplication {

    // ──────────────────────────────────────────────────────────────────────────
    //  Classe résultat : contient le produit + le détail de chaque étape
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Résultat complet d'un calcul Gauss :
     * le produit final et le tableau de toutes les étapes intermédiaires.
     */
    public static class Resultat {

        /** Le produit A × B. */
        public final long produit;

        /**
         * Tableau des étapes.
         * Chaque ligne contient 6 colonnes :
         *   [0] Numéro de l'étape
         *   [1] Dernier bit de B (0 ou 1)
         *   [2] Valeur de A en décimal et binaire
         *   [3] Valeur de B en décimal et binaire
         *   [4] On ajoute A ? (✓ OUI ou ✗ NON)
         *   [5] Total accumulé en décimal et binaire
         */
        public final String[][] etapes;

        public Resultat(long produit, String[][] etapes) {
            this.produit = produit;
            this.etapes  = etapes;
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Méthode principale
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Calcule A × B par décalages (méthode de Gauss).
     * Renvoie un {@link Resultat} contenant le produit + le détail pas à pas.
     *
     * @param A premier opérande (doit être ≥ 0)
     * @param B second  opérande (doit être ≥ 0)
     */
    public static Resultat calculer(long A, long B) {
        if (A < 0 || B < 0)
            throw new IllegalArgumentException("A et B doivent être ≥ 0.");

        List<String[]> lignes = new ArrayList<>();
        long a     = A;   // A sera décalé à gauche à chaque étape (×2)
        long b     = B;   // B sera décalé à droite à chaque étape (÷2)
        long total = 0;   // accumulateur du résultat
        int  etape = 0;

        while (b > 0) {
            int     bit0    = (int)(b & 1L);   // dernier bit de B : 0 ou 1
            boolean ajouter = (bit0 == 1);     // on ajoute A seulement si bit0 = 1

            if (ajouter) total += a;           // ② accumulation

            // Enregistrer l'état de cette étape dans le tableau
            lignes.add(new String[]{
                String.valueOf(++etape),
                String.valueOf(bit0),
                a + "  =  " + Long.toBinaryString(a),
                b + "  =  " + Long.toBinaryString(b),
                ajouter ? "✓  OUI  → +" + a : "✗  NON  → +0",
                total + "  =  " + Long.toBinaryString(total)
            });

            a = a << 1;   // ③ décalage gauche  : A × 2
            b = b >> 1;   // ④ décalage droite  : B ÷ 2
        }

        return new Resultat(total, lignes.toArray(new String[0][]));
    }

    // Empêche l'instanciation (classe utilitaire)
    private GaussMultiplication() {}
}
