package ui;

import algorithmes.GaussMultiplication;
import algorithmes.GaussMultiplication.Resultat;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Onglet 2 — Multiplication binaire par la méthode de Gauss (décalages).
 *
 * Contient :
 *   1. Un bloc d'explication pédagogique en français.
 *   2. Deux champs de saisie (A et B, en décimal).
 *   3. Un tableau montrant chaque étape de l'algorithme.
 *   4. Le résultat final en décimal et binaire.
 */
public class GaussPanel extends JPanel {

    // ── Champs de saisie ──────────────────────────────────────────────────────
    private JTextField fieldA;
    private JTextField fieldB;

    // ── Zone de résultat ──────────────────────────────────────────────────────
    private final JLabel labelProduit;

    // ── Modèle de données du tableau ──────────────────────────────────────────
    private final DefaultTableModel modelTableau;

    // ──────────────────────────────────────────────────────────────────────────
    //  Construction de l'interface
    // ──────────────────────────────────────────────────────────────────────────

    public GaussPanel() {
        setLayout(new BorderLayout(0, 8));
        setBackground(Palette.BG_DARK);
        setBorder(new EmptyBorder(10, 20, 10, 20));

        // ── Section 1 : explication pédagogique ───────────────────────────────
        add(construireExplication(), BorderLayout.NORTH);

        // ── Section 2 : saisie + tableau ──────────────────────────────────────
        JPanel centre = new JPanel(new BorderLayout(0, 6));
        centre.setBackground(Palette.BG_DARK);
        centre.add(construireSaisie(), BorderLayout.NORTH);

        // Tableau des étapes
        String[] colonnes = {
            "Étape",
            "Dernier bit de B",
            "A  (décimal = binaire)",
            "B  (décimal = binaire)",
            "On ajoute A ?",
            "Total accumulé  (décimal = binaire)"
        };
        modelTableau = new DefaultTableModel(colonnes, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(modelTableau);
        UIHelper.styleTable(table);

        // Largeurs préférées des colonnes (ajustables)
        int[] largeurs = {55, 120, 165, 165, 135, 210};
        for (int i = 0; i < largeurs.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(largeurs[i]);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBackground(Palette.BG_DARK);
        scroll.getViewport().setBackground(Palette.BG_PANEL);
        scroll.setBorder(new LineBorder(Palette.ACCENT, 2, true));
        centre.add(scroll, BorderLayout.CENTER);

        add(centre, BorderLayout.CENTER);

        // ── Section 3 : résultat final ────────────────────────────────────────
        JPanel panResultat = UIHelper.resultPanel();
        labelProduit = new JLabel("— entrez A et B puis cliquez sur Calculer —",
                                  SwingConstants.CENTER);
        labelProduit.setFont(new Font("Consolas", Font.BOLD, 14));
        labelProduit.setForeground(new Color(100, 140, 200));
        panResultat.add(labelProduit, BorderLayout.CENTER);
        add(panResultat, BorderLayout.SOUTH);
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Construction des sous-panneaux
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Construit le bloc d'explication pédagogique en haut de l'onglet.
     * Explique le principe de l'algorithme en français simple.
     */
    private JPanel construireExplication() {
        JPanel pan = new JPanel(new BorderLayout(0, 4));
        pan.setBackground(new Color(12, 30, 75));
        pan.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Palette.ACCENT2, 2, true),
            new EmptyBorder(10, 16, 10, 16)));

        // ---- Titre ----
        JLabel titre = new JLabel(
            "📖  Comment fonctionne la méthode de Gauss (multiplication par décalages) ?");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titre.setForeground(Palette.ACCENT2);
        pan.add(titre, BorderLayout.NORTH);

        // ---- Explication pas à pas ----
        JLabel explication = new JLabel("<html>"
            + "<b style='color:#B4D2FF'>Principe :</b> "
            + "multiplier A × B <u>sans faire de vraie multiplication</u>.<br>"
            + "On utilise uniquement des "
            + "<b style='color:#00C3FF'>additions</b> et des "
            + "<b style='color:#00C3FF'>décalages de bits</b>.<br><br>"
            + "<b style='color:#B4D2FF'>À chaque étape :</b><br>"
            + "&nbsp;&nbsp;① Regarder le <b>dernier bit</b> (tout à droite) de B.<br>"
            + "&nbsp;&nbsp;② Si ce bit vaut <b style='color:#50FF90'>1</b>"
            + " &rarr; on <b>ajoute A</b> au total.<br>"
            + "&nbsp;&nbsp;&nbsp;&nbsp; Si ce bit vaut "
            + "<b style='color:#FF7878'>0</b> &rarr; on n'ajoute <b>rien</b>.<br>"
            + "&nbsp;&nbsp;③ On <b style='color:#00C3FF'>décale A vers la gauche</b>"
            + "  (A &times; 2 = ajouter un 0 à droite en binaire).<br>"
            + "&nbsp;&nbsp;④ On <b style='color:#FF7878'>décale B vers la droite</b>"
            + "  (B &divide; 2 = supprimer le dernier bit).<br>"
            + "&nbsp;&nbsp;⑤ On recommence jusqu'à ce que B = 0.<br></html>");
        explication.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        explication.setForeground(Palette.FG_LIGHT);
        pan.add(explication, BorderLayout.CENTER);

        // ---- Légende des couleurs ----
        JPanel legende = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 4));
        legende.setBackground(new Color(12, 30, 75));
        legende.add(UIHelper.legendeItem(
            "Ligne VERTE  = bit vaut 1  → on AJOUTE A",   Palette.ROW_ADD));
        legende.add(UIHelper.legendeItem(
            "Ligne GRISE  = bit vaut 0  → on n'ajoute rien", Palette.ROW_SKIP));
        pan.add(legende, BorderLayout.SOUTH);

        return pan;
    }

    /**
     * Construit le sous-panneau de saisie (champs A, B + bouton).
     */
    private JPanel construireSaisie() {
        JPanel pan = new JPanel(new GridBagLayout());
        pan.setBackground(Palette.BG_DARK);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill   = GridBagConstraints.HORIZONTAL;

        // Champ A
        g.gridx = 0; g.gridy = 0; g.weightx = 0;
        pan.add(UIHelper.label("A (entier décimal) :"), g);
        g.gridx = 1; g.weightx = 0.5;
        fieldA = UIHelper.champ("Ex: 13");
        pan.add(fieldA, g);

        // Champ B
        g.gridx = 2; g.weightx = 0;
        pan.add(UIHelper.label("B (entier décimal) :"), g);
        g.gridx = 3; g.weightx = 0.5;
        fieldB = UIHelper.champ("Ex: 11");
        pan.add(fieldB, g);

        // Bouton
        g.gridx = 0; g.gridy = 1; g.gridwidth = 4;
        g.insets = new Insets(10, 8, 4, 8);
        JButton btnCalculer = UIHelper.bouton("▶   Calculer A × B   (étape par étape)");
        pan.add(btnCalculer, g);

        // Listener
        ActionListener action = e -> calculer();
        btnCalculer.addActionListener(action);
        fieldA.addActionListener(action);
        fieldB.addActionListener(action);

        return pan;
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Logique déclenchée au clic / touche Entrée
    // ──────────────────────────────────────────────────────────────────────────

    /** Lit A et B, exécute l'algorithme et remplit le tableau + le résultat. */
    private void calculer() {
        modelTableau.setRowCount(0);   // vider le tableau précédent
        try {
            long a = Long.parseLong(fieldA.getText().trim());
            long b = Long.parseLong(fieldB.getText().trim());

            Resultat res = GaussMultiplication.calculer(a, b);

            // Remplir le tableau avec chaque étape
            for (String[] ligne : res.etapes)
                modelTableau.addRow(ligne);

            // Afficher le résultat final
            labelProduit.setForeground(Palette.ACCENT2);
            labelProduit.setText(
                "Résultat  :  " + a + "  ×  " + b + "  =  " + res.produit
                + "     [ binaire :  " + Long.toBinaryString(res.produit) + " ]");

        } catch (NumberFormatException ex) {
            afficherErreur("Veuillez entrer deux nombres entiers positifs (ex: 13 et 11).");
        } catch (IllegalArgumentException ex) {
            afficherErreur(ex.getMessage());
        }
    }

    private void afficherErreur(String msg) {
        labelProduit.setForeground(Palette.ERR_RED);
        labelProduit.setText("Erreur : " + msg);
    }
}
