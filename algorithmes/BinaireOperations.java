package algorithmes;

/**
 * Opérations bit-à-bit sur des entiers non signés.
 *
 * Opérations disponibles :
 *   AND  (ET  logique)  : 1 si les deux bits valent 1
 *   OR   (OU  logique)  : 1 si au moins un bit vaut 1
 *   XOR  (OU exclusif)  : 1 si les bits sont différents
 *   NOT  (complément)   : inverse chaque bit (sur n bits)
 *   SHL  (shift left )  : décalage vers la gauche  (× 2^n)
 *   SHR  (shift right)  : décalage vers la droite  (÷ 2^n)
 */
public class BinaireOperations {

    // ──────────────────────────────────────────────────────────────────────────
    //  Résultat d'une opération : contient la valeur + le détail affiché
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Résultat d'une opération binaire.
     * Contient la valeur numérique et les lignes à afficher dans le tableau.
     *
     * Colonnes du tableau :
     *   [0] Opérande / étiquette   (ex: "A", "B", "A AND B")
     *   [1] Valeur décimale
     *   [2] Représentation binaire (formatée sur le même nombre de bits)
     *   [3] Représentation hexadécimale
     */
    public static class Resultat {
        public final long     valeur;
        public final String[][] lignes;

        public Resultat(long valeur, String[][] lignes) {
            this.valeur = valeur;
            this.lignes = lignes;
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Opérations binaires (AND, OR, XOR, NOT)
    // ──────────────────────────────────────────────────────────────────────────

    /** ET logique bit-à-bit : résultat = A AND B */
    public static Resultat and(long A, long B) {
        long res = A & B;
        int bits = largeurBits(Math.max(A, B));
        return new Resultat(res, new String[][]{
            { "A",       String.valueOf(A),   formaterBin(A,   bits), hexa(A)   },
            { "B",       String.valueOf(B),   formaterBin(B,   bits), hexa(B)   },
            { "A AND B", String.valueOf(res), formaterBin(res, bits), hexa(res) }
        });
    }

    /** OU logique bit-à-bit : résultat = A OR B */
    public static Resultat or(long A, long B) {
        long res = A | B;
        int bits = largeurBits(Math.max(A, B));
        return new Resultat(res, new String[][]{
            { "A",      String.valueOf(A),   formaterBin(A,   bits), hexa(A)   },
            { "B",      String.valueOf(B),   formaterBin(B,   bits), hexa(B)   },
            { "A OR B", String.valueOf(res), formaterBin(res, bits), hexa(res) }
        });
    }

    /** OU exclusif bit-à-bit : résultat = A XOR B */
    public static Resultat xor(long A, long B) {
        long res = A ^ B;
        int bits = largeurBits(Math.max(A, B));
        return new Resultat(res, new String[][]{
            { "A",       String.valueOf(A),   formaterBin(A,   bits), hexa(A)   },
            { "B",       String.valueOf(B),   formaterBin(B,   bits), hexa(B)   },
            { "A XOR B", String.valueOf(res), formaterBin(res, bits), hexa(res) }
        });
    }

    /**
     * Complément bit-à-bit de A sur {@code nbBits} bits.
     * Ex: NOT 1010 (4 bits) = 0101
     *
     * @param A      la valeur à inverser
     * @param nbBits le nombre de bits à considérer (4, 8, 16, 32…)
     */
    public static Resultat not(long A, int nbBits) {
        if (nbBits < 1 || nbBits > 64)
            throw new IllegalArgumentException("Nombre de bits invalide (1–64).");
        long masque = (nbBits == 64) ? -1L : (1L << nbBits) - 1L;
        long res    = (~A) & masque;
        return new Resultat(res, new String[][]{
            { "A",     String.valueOf(A),   formaterBin(A,   nbBits), hexa(A)   },
            { "NOT A", String.valueOf(res), formaterBin(res, nbBits), hexa(res) }
        });
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Décalages (SHL, SHR)
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Décalage vers la gauche (Shift Left) de {@code n} positions.
     * Équivaut à A × 2^n.
     *
     * @param A la valeur à décaler
     * @param n nombre de positions (1–63)
     */
    public static Resultat shiftLeft(long A, int n) {
        if (n < 0 || n > 63)
            throw new IllegalArgumentException("Le décalage doit être entre 0 et 63.");
        long res = A << n;
        int  bits = Math.max(largeurBits(A) + n, 8);
        return new Resultat(res, new String[][]{
            { "A avant",           String.valueOf(A),   formaterBin(A,   bits), hexa(A)   },
            { "A << " + n + "  (×" + pow2Str(n) + ")", String.valueOf(res), formaterBin(res, bits), hexa(res) }
        });
    }

    /**
     * Décalage vers la droite (Shift Right) de {@code n} positions.
     * Équivaut à A ÷ 2^n (division entière).
     *
     * @param A la valeur à décaler
     * @param n nombre de positions (1–63)
     */
    public static Resultat shiftRight(long A, int n) {
        if (n < 0 || n > 63)
            throw new IllegalArgumentException("Le décalage doit être entre 0 et 63.");
        long res = A >>> n;   // >>> = décalage logique (sans extension de signe)
        int  bits = largeurBits(A);
        return new Resultat(res, new String[][]{
            { "A avant",           String.valueOf(A),   formaterBin(A,   bits), hexa(A)   },
            { "A >> " + n + "  (÷" + pow2Str(n) + ")", String.valueOf(res), formaterBin(res, bits), hexa(res) }
        });
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Utilitaires privés
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Calcule le nombre de bits nécessaires pour représenter {@code val}
     * arrondi au multiple de 4 supérieur (pour un affichage propre).
     */
    private static int largeurBits(long val) {
        if (val == 0) return 4;
        int n = Long.SIZE - Long.numberOfLeadingZeros(val); // bits significatifs
        return ((n + 3) / 4) * 4;  // arrondir au multiple de 4
    }

    /**
     * Formate {@code val} en binaire sur exactement {@code bits} bits,
     * avec un espace tous les 4 bits pour la lisibilité.
     * Ex: formaterBin(255, 8) → "1111 1111"
     */
    public static String formaterBin(long val, int bits) {
        StringBuilder sb = new StringBuilder();
        for (int i = bits - 1; i >= 0; i--) {
            if (i < bits - 1 && (i + 1) % 4 == 0) sb.append(' ');
            sb.append((val >> i) & 1L);
        }
        return sb.toString();
    }

    /** Représentation hexadécimale en majuscules. */
    private static String hexa(long val) {
        return Long.toHexString(val).toUpperCase();
    }

    /** Retourne "2^n" ou la valeur si n est petit. */
    private static String pow2Str(int n) {
        long v = 1L << n;
        return n <= 10 ? String.valueOf(v) : "2^" + n;
    }

    private BinaireOperations() {}
}
