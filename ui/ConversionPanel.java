package ui;

import algorithmes.ConversionBase;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Onglet 1 — Conversion d'un nombre d'une base vers une autre.
 *
 * L'utilisateur entre :
 *   - le nombre à convertir (ex: "1A3F")
 *   - la base de départ     (ex: 16 pour hexadécimal)
 *   - la base d'arrivée     (ex: 2  pour binaire)
 *
 * Le résultat s'affiche en bas après clic sur "Convertir" ou touche Entrée.
 */
public class ConversionPanel extends JPanel {

    // ── Composants de saisie ──────────────────────────────────────────────────
    private final JTextField fieldNombre;
    private final JTextField fieldBaseDepart;
    private final JTextField fieldBaseArrivee;

    // ── Zone de résultat ──────────────────────────────────────────────────────
    private final JLabel labelResultat;

    // ──────────────────────────────────────────────────────────────────────────
    //  Construction de l'interface
    // ──────────────────────────────────────────────────────────────────────────

    public ConversionPanel() {
        setLayout(new GridBagLayout());
        setBackground(Palette.BG_DARK);
        setBorder(new EmptyBorder(24, 50, 24, 50));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 8, 10, 8);
        g.fill   = GridBagConstraints.HORIZONTAL;

        // ── Ligne 0 : champ "Nombre" ──────────────────────────────────────────
        g.gridx = 0; g.gridy = 0; g.weightx = 0;
        add(UIHelper.label("Nombre à convertir :"), g);
        g.gridx = 1; g.weightx = 1;
        fieldNombre = UIHelper.champ("Ex: 1A3F  ou  101101  ou  255");
        add(fieldNombre, g);

        // ── Ligne 1 : champ "Base de départ" ─────────────────────────────────
        g.gridx = 0; g.gridy = 1; g.weightx = 0;
        add(UIHelper.label("Base de départ :"), g);
        g.gridx = 1; g.weightx = 1;
        fieldBaseDepart = UIHelper.champ("Ex: 16 (hexadécimal)  ou  2 (binaire)  ou  10 (décimal)");
        add(fieldBaseDepart, g);

        // ── Ligne 2 : champ "Base d'arrivée" ─────────────────────────────────
        g.gridx = 0; g.gridy = 2; g.weightx = 0;
        add(UIHelper.label("Base d'arrivée :"), g);
        g.gridx = 1; g.weightx = 1;
        fieldBaseArrivee = UIHelper.champ("Ex: 2 (binaire)  ou  8 (octal)  ou  10 (décimal)");
        add(fieldBaseArrivee, g);

        // ── Ligne 3 : bouton Convertir ────────────────────────────────────────
        g.gridx = 0; g.gridy = 3; g.gridwidth = 2;
        g.insets = new Insets(20, 8, 10, 8);
        JButton btnConvertir = UIHelper.bouton("Convertir");
        add(btnConvertir, g);

        // ── Ligne 4 : zone de résultat ────────────────────────────────────────
        g.gridy = 4; g.insets = new Insets(4, 8, 8, 8);
        JPanel panRes = UIHelper.resultPanel();
        labelResultat = new JLabel("— entrez un nombre et appuyez sur Convertir —",
                                   SwingConstants.CENTER);
        labelResultat.setFont(Palette.FONT_RES);
        labelResultat.setForeground(new Color(100, 140, 200));
        panRes.add(labelResultat, BorderLayout.CENTER);
        add(panRes, g);

        // ── Listener : bouton + touche Entrée sur chaque champ ────────────────
        ActionListener action = e -> convertir();
        btnConvertir.addActionListener(action);
        fieldNombre.addActionListener(action);
        fieldBaseDepart.addActionListener(action);
        fieldBaseArrivee.addActionListener(action);
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Logique déclenchée au clic / touche Entrée
    // ──────────────────────────────────────────────────────────────────────────

    /** Lit les champs, appelle l'algorithme et affiche le résultat. */
    private void convertir() {
        try {
            String nombre    = fieldNombre.getText();
            int    bdep      = Integer.parseInt(fieldBaseDepart.getText().trim());
            int    barr      = Integer.parseInt(fieldBaseArrivee.getText().trim());

            String resultat  = ConversionBase.convertir(nombre, bdep, barr);

            labelResultat.setForeground(Palette.ACCENT2);
            labelResultat.setText(
                nombre.trim().toUpperCase()
                + "  (base " + bdep + ")   =   "
                + resultat
                + "  (base " + barr + ")");

        } catch (NumberFormatException ex) {
            afficherErreur("Les bases doivent être des nombres entiers (ex: 2, 8, 10, 16…)");
        } catch (IllegalArgumentException ex) {
            afficherErreur(ex.getMessage());
        }
    }

    private void afficherErreur(String msg) {
        labelResultat.setForeground(Palette.ERR_RED);
        labelResultat.setText("Erreur : " + msg);
    }
}
