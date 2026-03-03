package ui;

import java.awt.*;

/**
 * Constantes de couleurs et de polices du thème bleu.
 *
 * Centraliser ici tous les réglages visuels permet de changer
 * le thème en modifiant un seul fichier.
 */
public class Palette {

    // ── Couleurs de fond ───────────────────────────────────────────────────────
    /** Fond principal de la fenêtre (bleu très foncé). */
    public static final Color BG_DARK  = new Color(10,  25,  60);
    /** Fond des panneaux et champs (bleu marine). */
    public static final Color BG_PANEL = new Color(20,  45, 100);

    // ── Couleurs d'accentuation ────────────────────────────────────────────────
    /** Bleu vif : bordures, boutons, traits. */
    public static final Color ACCENT   = new Color(30, 120, 255);
    /** Cyan : résultats, titres, valeurs importantes. */
    public static final Color ACCENT2  = new Color(0,  195, 255);

    // ── Couleurs de texte ──────────────────────────────────────────────────────
    /** Texte blanc. */
    public static final Color FG_WHITE = Color.WHITE;
    /** Texte bleu clair (labels secondaires). */
    public static final Color FG_LIGHT = new Color(180, 210, 255);
    /** Texte rouge pour les messages d'erreur. */
    public static final Color ERR_RED  = new Color(255,  80,  80);

    // ── Couleurs du tableau Gauss ──────────────────────────────────────────────
    /** Fond vert foncé : ligne où on AJOUTE A (bit = 1). */
    public static final Color ROW_ADD  = new Color(20,  90,  45);
    /** Fond bleu-gris : ligne où on n'ajoute pas (bit = 0). */
    public static final Color ROW_SKIP = new Color(30,  45,  85);
    /** Texte vert clair sur ligne ROW_ADD. */
    public static final Color TXT_ADD  = new Color(130, 255, 160);
    /** Texte bleu clair sur ligne ROW_SKIP. */
    public static final Color TXT_SKIP = new Color(160, 180, 220);

    // ── Polices ────────────────────────────────────────────────────────────────
    /** Police des labels (gras). */
    public static final Font FONT_LBL  = new Font("Segoe UI", Font.BOLD,  14);
    /** Police des champs de saisie (monospace). */
    public static final Font FONT_FLD  = new Font("Consolas",  Font.PLAIN, 15);
    /** Police des boutons (gras). */
    public static final Font FONT_BTN  = new Font("Segoe UI", Font.BOLD,  14);
    /** Police des résultats (monospace gras). */
    public static final Font FONT_RES  = new Font("Consolas",  Font.BOLD,  15);
    /** Police des lignes du tableau d'étapes (monospace). */
    public static final Font FONT_STEP = new Font("Consolas",  Font.PLAIN, 13);

    // Empêche l'instanciation (classe de constantes uniquement)
    private Palette() {}
}
