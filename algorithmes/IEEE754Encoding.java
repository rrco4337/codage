package algorithmes;

/**
 * Encodage IEEE 754 simple precision (32 bits) pour un float Java.
 */
public class IEEE754Encoding {

    public static class Resultat {
        public final float valeur;
        public final int bits;
        public final String binaire32;
        public final String bitSigne;
        public final String exposant;
        public final String mantisse;
        public final String hexadecimal;
        public final String typeValeur;

        public Resultat(float valeur, int bits, String binaire32,
                        String bitSigne, String exposant, String mantisse,
                        String hexadecimal, String typeValeur) {
            this.valeur = valeur;
            this.bits = bits;
            this.binaire32 = binaire32;
            this.bitSigne = bitSigne;
            this.exposant = exposant;
            this.mantisse = mantisse;
            this.hexadecimal = hexadecimal;
            this.typeValeur = typeValeur;
        }
    }

    /**
     * Encode une valeur texte en IEEE 754 simple precision.
     * Accepte virgule ou point decimal: "29,75" ou "29.75".
     */
    public static Resultat encoderDepuisTexte(String texte) {
        if (texte == null || texte.isBlank()) {
            throw new IllegalArgumentException("Entrez une valeur numerique (ex: 29,75).");
        }

        String normalise = texte.trim().replace(" ", "").replace(',', '.');
        float valeur;
        try {
            valeur = Float.parseFloat(normalise);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Valeur invalide: " + texte);
        }

        int bits = Float.floatToIntBits(valeur);
        String binaire32 = String.format("%32s", Integer.toBinaryString(bits)).replace(' ', '0');
        String bitSigne = binaire32.substring(0, 1);
        String exposant = binaire32.substring(1, 9);
        String mantisse = binaire32.substring(9);
        String hexa = String.format("%08X", bits);

        return new Resultat(
            valeur,
            bits,
            bitSigne + " " + exposant + " " + grouperMantisse(mantisse),
            bitSigne,
            exposant,
            mantisse,
            hexa,
            typeValeur(exposant, mantisse)
        );
    }

    private static String typeValeur(String exposant, String mantisse) {
        if ("11111111".equals(exposant)) {
            return mantisse.chars().allMatch(c -> c == '0') ? "Infini" : "NaN";
        }
        if ("00000000".equals(exposant)) {
            return mantisse.chars().allMatch(c -> c == '0') ? "Zero" : "Subnormal";
        }
        return "Normal";
    }

    private static String grouperMantisse(String mantisse) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mantisse.length(); i++) {
            if (i > 0 && i % 4 == 0) sb.append(' ');
            sb.append(mantisse.charAt(i));
        }
        return sb.toString();
    }

    private IEEE754Encoding() {}
}