package ui;

import algorithmes.BinaireOperations;
import algorithmes.BinaireOperations.Resultat;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Onglet 3 — Opérations bit-à-bit et décalages.
 *
 * Opérations disponibles :
 *   AND, OR, XOR     — opérations logiques entre deux nombres
 *   NOT              — complément bit-à-bit sur N bits
 *   SHL (shift left) — décalage à gauche  (A × 2^n)
 *   SHR (shift right)— décalage à droite  (A ÷ 2^n)
 *
 * Les entrées acceptent :
 *   - décimal  (ex: 255)
 *   - binaire  avec espaces (ex: 1111 1111)  → base 2
 *   - hexa     (ex: FF)                       → base 16
 *
 * Format de saisie : nombre@base  (ex: "FF@16", "1111 1111@2", "255@10")
 * Si aucune base n'est précisée, décimal (base 10) est supposé.
 */
public class BinairePanel extends JPanel {

    // ── Explication courte de l'opération sélectionnée ────────────────────────
    private final JLabel  labelExplication;

    // ── Champs de saisie ──────────────────────────────────────────────────────
    private final JTextField fieldA;
    private final JTextField fieldBaseA;
    private JTextField fieldB;      // masqué pour NOT / SHL / SHR
    private final JTextField fieldBaseB;
    private final JTextField fieldN;        // nombre de bits (NOT) ou positions (SHL/SHR)
    private final JLabel     labelN;

    // ── Sélection d'opération ─────────────────────────────────────────────────
    private final JComboBox<String> comboOp;

    // ── Tableau de résultat ───────────────────────────────────────────────────
    private final DefaultTableModel modelTableau;
    private final JLabel            labelResultat;

    // ── Panneau qui contient fieldB + labelBaseB (masquable) ─────────────────
    private final JPanel panelB;

    // ──────────────────────────────────────────────────────────────────────────

    public BinairePanel() {
        setLayout(new BorderLayout(0, 8));
        setBackground(Palette.BG_DARK);
        setBorder(new EmptyBorder(10, 20, 10, 20));

        // ── Explication pédagogique ────────────────────────────────────────────
        add(construireExplication(), BorderLayout.NORTH);

        // ── Centre : saisie + tableau ──────────────────────────────────────────
        JPanel centre = new JPanel(new BorderLayout(0, 6));
        centre.setBackground(Palette.BG_DARK);

        // ---- Sélecteur d'opération ----
        String[] operations = {
            "AND  — ET logique (A & B)",
            "OR   — OU logique (A | B)",
            "XOR  — OU exclusif (A ^ B)",
            "NOT  — Complément (inverser A sur N bits)",
            "SHL  — Décalage gauche (A << N)",
            "SHR  — Décalage droite (A >> N)"
        };
        comboOp = new JComboBox<>(operations);
        comboOp.setFont(new Font("Segoe UI", Font.BOLD, 13));
        comboOp.setBackground(Palette.BG_PANEL);
        comboOp.setForeground(Palette.ACCENT2);
        comboOp.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Palette.ACCENT, 2, true),
            new EmptyBorder(4, 8, 4, 8)));

        JPanel panCombo = new JPanel(new BorderLayout(8, 0));
        panCombo.setBackground(Palette.BG_DARK);
        panCombo.setBorder(new EmptyBorder(0, 0, 4, 0));
        panCombo.add(UIHelper.label("Opération :"), BorderLayout.WEST);
        panCombo.add(comboOp, BorderLayout.CENTER);
        centre.add(panCombo, BorderLayout.NORTH);

        // ---- Formulaire de saisie ----
        JPanel saisie = new JPanel(new GridBagLayout());
        saisie.setBackground(Palette.BG_DARK);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.fill   = GridBagConstraints.HORIZONTAL;

        // Champ A + base
        g.gridx=0; g.gridy=0; g.weightx=0; saisie.add(UIHelper.label("A :"), g);
        g.gridx=1; g.weightx=0.6; fieldA = UIHelper.champ("Ex: 255   ou   1111 1111   ou   FF"); saisie.add(fieldA, g);
        g.gridx=2; g.weightx=0;   saisie.add(UIHelper.label("base de A :"), g);
        g.gridx=3; g.weightx=0.2; fieldBaseA = UIHelper.champ("10"); saisie.add(fieldBaseA, g);

        // Champ B + base  (masqué pour NOT / SHL / SHR)
        panelB = new JPanel(new GridBagLayout());
        panelB.setBackground(Palette.BG_DARK);
        GridBagConstraints gb = new GridBagConstraints();
        gb.insets = new Insets(6, 6, 6, 6);
        gb.fill   = GridBagConstraints.HORIZONTAL;
        gb.gridx=0; gb.gridy=0; gb.weightx=0; panelB.add(UIHelper.label("B :"), gb);
        gb.gridx=1; gb.weightx=0.6; fieldB = UIHelper.champ("Ex: 170   ou   1010 1010"); panelB.add(fieldB, gb);
        gb.gridx=2; gb.weightx=0;   panelB.add(UIHelper.label("base de B :"), gb);
        gb.gridx=3; gb.weightx=0.2; fieldBaseB = UIHelper.champ("10"); panelB.add(fieldBaseB, gb);

        g.gridx=0; g.gridy=1; g.gridwidth=4;
        saisie.add(panelB, g);

        // Champ N (nombre de bits ou positions de décalage)
        labelN    = UIHelper.label("Nombre de bits :");
        fieldN    = UIHelper.champ("Ex: 8");
        JPanel panN = new JPanel(new GridBagLayout());
        panN.setBackground(Palette.BG_DARK);
        GridBagConstraints gn = new GridBagConstraints();
        gn.insets = new Insets(6, 6, 6, 6);
        gn.fill   = GridBagConstraints.HORIZONTAL;
        gn.gridx=0; gn.gridy=0; gn.weightx=0; panN.add(labelN, gn);
        gn.gridx=1; gn.weightx=0.3; panN.add(fieldN, gn);
        gn.gridx=2; gn.weightx=0.7; panN.add(new JLabel(), gn);  // spacer

        g.gridx=0; g.gridy=2; g.gridwidth=4;
        saisie.add(panN, g);

        // Explication courte
        labelExplication = new JLabel("", SwingConstants.LEFT);
        labelExplication.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        labelExplication.setForeground(Palette.FG_LIGHT);
        g.gridy=3;
        saisie.add(labelExplication, g);

        // Bouton
        g.gridy=4; g.insets = new Insets(10, 6, 4, 6);
        JButton btnCalculer = UIHelper.bouton("▶   Calculer");
        saisie.add(btnCalculer, g);

        centre.add(saisie, BorderLayout.CENTER);

        // ---- Tableau de résultat ----
        String[] colonnes = { "Opérande", "Décimal", "Binaire  (groupes de 4 bits)", "Hexadécimal" };
        modelTableau = new DefaultTableModel(colonnes, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(modelTableau);
        styleTableBinaire(table);
        int[] larg = {160, 100, 300, 100};
        for (int i = 0; i < larg.length; i++) table.getColumnModel().getColumn(i).setPreferredWidth(larg[i]);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(Palette.ACCENT, 2, true));
        scroll.getViewport().setBackground(Palette.BG_PANEL);

        JPanel bas = new JPanel(new BorderLayout(0, 6));
        bas.setBackground(Palette.BG_DARK);
        bas.add(scroll, BorderLayout.CENTER);

        // Résultat final
        JPanel panRes = UIHelper.resultPanel();
        labelResultat = new JLabel("— choisissez une opération et cliquez sur Calculer —",
                                   SwingConstants.CENTER);
        labelResultat.setFont(new Font("Consolas", Font.BOLD, 14));
        labelResultat.setForeground(new Color(100, 140, 200));
        panRes.add(labelResultat, BorderLayout.CENTER);
        bas.add(panRes, BorderLayout.SOUTH);

        centre.add(bas, BorderLayout.SOUTH);

        // ---- Hauteur fixée pour le tableau ----
        scroll.setPreferredSize(new Dimension(0, 130));

        add(centre, BorderLayout.CENTER);

        // ── Listeners ─────────────────────────────────────────────────────────
        comboOp.addActionListener(e -> mettreAJourFormulaire());
        ActionListener action = e -> calculer();
        btnCalculer.addActionListener(action);
        fieldA.addActionListener(action);
        fieldB.addActionListener(action);
        fieldN.addActionListener(action);

        mettreAJourFormulaire();   // état initial
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Mise à jour du formulaire selon l'opération choisie
    // ──────────────────────────────────────────────────────────────────────────

    private void mettreAJourFormulaire() {
        int op = comboOp.getSelectedIndex();
        boolean deuxOperandes = (op <= 2);   // AND, OR, XOR
        boolean avecN         = (op >= 3);   // NOT, SHL, SHR

        panelB.setVisible(deuxOperandes);
        labelN.setVisible(avecN);
        fieldN.setVisible(avecN);

        switch (op) {
            case 0: labelExplication.setText("Résultat = 1 seulement si les deux bits sont 1.  Ex: 1010 AND 1100 = 1000"); break;
            case 1: labelExplication.setText("Résultat = 1 si au moins un bit est 1.  Ex: 1010 OR 1100 = 1110"); break;
            case 2: labelExplication.setText("Résultat = 1 si les bits sont différents.  Ex: 1010 XOR 1100 = 0110"); break;
            case 3: labelN.setText("Nb de bits  :"); labelExplication.setText("Inversion de chaque bit de A sur N bits.  Ex: NOT 1010 (4 bits) = 0101"); break;
            case 4: labelN.setText("Décalage N  :"); labelExplication.setText("Chaque bit se déplace de N rangs vers la gauche (= ×2^N).  Ex: 0011 << 2 = 1100"); break;
            case 5: labelN.setText("Décalage N  :"); labelExplication.setText("Chaque bit se déplace de N rangs vers la droite (= ÷2^N).  Ex: 1100 >> 2 = 0011"); break;
        }
        revalidate(); repaint();
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Calcul principal
    // ──────────────────────────────────────────────────────────────────────────

    private void calculer() {
        modelTableau.setRowCount(0);
        try {
            long   a    = lireEntree(fieldA.getText(), fieldBaseA.getText());
            int    op   = comboOp.getSelectedIndex();
            Resultat res;

            if (op <= 2) {
                // Opérations à deux opérandes
                long b = lireEntree(fieldB.getText(), fieldBaseB.getText());
                if      (op == 0) res = BinaireOperations.and(a, b);
                else if (op == 1) res = BinaireOperations.or(a, b);
                else              res = BinaireOperations.xor(a, b);
            } else {
                // Opérations unaires avec N
                int n = Integer.parseInt(fieldN.getText().trim());
                if      (op == 3) res = BinaireOperations.not(a, n);
                else if (op == 4) res = BinaireOperations.shiftLeft(a, n);
                else              res = BinaireOperations.shiftRight(a, n);
            }

            for (String[] ligne : res.lignes) modelTableau.addRow(ligne);

            // Affichage du résultat final en plusieurs bases
            long v = res.valeur;
            int  bits = v == 0 ? 8 : (int)(((64 - Long.numberOfLeadingZeros(v) + 3) / 4) * 4);
            bits = Math.max(8, bits);
            labelResultat.setForeground(Palette.ACCENT2);
            labelResultat.setText(
                "Résultat  :  décimal = " + v
                + "   |   binaire = " + BinaireOperations.formaterBin(v, bits)
                + "   |   hex = " + Long.toHexString(v).toUpperCase()
                + "   |   octal = " + Long.toOctalString(v));

        } catch (NumberFormatException ex) {
            afficherErreur("Entrez un entier valide (ex: 255  ou une base valide 2–36).");
        } catch (IllegalArgumentException ex) {
            afficherErreur(ex.getMessage());
        }
    }

    /**
     * Lit un nombre depuis un champ texte et le convertit en long.
     * Supporte les espaces dans la représentation (ex: "1111 1111").
     *
     * @param valeur  texte saisi (ex: "1111 1111" ou "FF" ou "255")
     * @param baseTxt base de saisie (ex: "2", "16", "10")
     */
    private long lireEntree(String valeur, String baseTxt) {
        valeur  = valeur.trim().replace(" ", "").toUpperCase();
        int base = Integer.parseInt(baseTxt.trim());
        if (base < 2 || base > 36)
            throw new IllegalArgumentException("Base invalide : " + base + " (doit être 2–36).");
        return Long.parseLong(valeur, base);
    }

    private void afficherErreur(String msg) {
        labelResultat.setForeground(Palette.ERR_RED);
        labelResultat.setText("Erreur : " + msg);
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Explication pédagogique (en haut)
    // ──────────────────────────────────────────────────────────────────────────

    private JPanel construireExplication() {
        JPanel pan = new JPanel(new BorderLayout(0, 4));
        pan.setBackground(new Color(12, 30, 75));
        pan.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Palette.ACCENT2, 2, true),
            new EmptyBorder(8, 16, 8, 16)));

        JLabel titre = new JLabel("⚙️  Opérations bit-à-bit & Décalages");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titre.setForeground(Palette.ACCENT2);
        pan.add(titre, BorderLayout.NORTH);

        JLabel desc = new JLabel("<html>"
            + "<b style='color:#B4D2FF'>Les opérations bit-à-bit</b> traitent chaque bit indépendamment :<br>"
            + "&nbsp;&nbsp;<b style='color:#50FF90'>AND</b> : 1 ET 1 = 1, sinon 0 &nbsp;&nbsp;"
            + "&nbsp;&nbsp;<b style='color:#50AAFF'>OR</b>  : 0 OU 0 = 0, sinon 1 &nbsp;&nbsp;"
            + "&nbsp;&nbsp;<b style='color:#FFD050'>XOR</b> : 1 si bits différents &nbsp;&nbsp;"
            + "&nbsp;&nbsp;<b style='color:#FF7878'>NOT</b> : inverse chaque bit<br>"
            + "<b style='color:#B4D2FF'>Les décalages</b> déplacent tous les bits :<br>"
            + "&nbsp;&nbsp;<b style='color:#00C3FF'>SHL (&lt;&lt;)</b> : décaler à gauche = multiplier par 2 &nbsp;&nbsp;"
            + "&nbsp;&nbsp;<b style='color:#FF7878'>SHR (&gt;&gt;)</b> : décaler à droite = diviser par 2<br>"
            + "<b style='color:#B4D2FF'>Formats acceptés pour A et B :</b>"
            + " décimal (base 10), binaire avec espaces (base 2), hexa (base 16)…"
            + "</html>");
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        desc.setForeground(Palette.FG_LIGHT);
        pan.add(desc, BorderLayout.CENTER);

        return pan;
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Style du tableau
    // ──────────────────────────────────────────────────────────────────────────

    private void styleTableBinaire(JTable t) {
        t.setBackground(Palette.BG_PANEL);
        t.setForeground(Palette.FG_WHITE);
        t.setFont(Palette.FONT_STEP);
        t.setGridColor(new Color(40, 80, 160));
        t.setRowHeight(28);
        t.setSelectionBackground(Palette.ACCENT);
        t.setSelectionForeground(Palette.FG_WHITE);

        JTableHeader h = t.getTableHeader();
        h.setBackground(new Color(15, 35, 90));
        h.setForeground(Palette.ACCENT2);
        h.setFont(new Font("Segoe UI", Font.BOLD, 13));
        h.setReorderingAllowed(false);

        // Couleur de chaque ligne : A=bleu, B=violet, résultat=cyan
        Color[] couleurs = {
            new Color(20, 40, 100),    // A
            new Color(40, 20, 100),    // B
            new Color(0,  70,  90),    // résultat
        };
        Color[] textes = {
            new Color(160, 200, 255),
            new Color(200, 160, 255),
            Palette.ACCENT2,
        };

        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (isSelected) {
                    setBackground(Palette.ACCENT);
                    setForeground(Palette.FG_WHITE);
                } else {
                    int idx = Math.min(row, couleurs.length - 1);
                    setBackground(couleurs[idx]);
                    setForeground(textes[idx]);
                    // Dernière ligne = résultat → toujours la couleur résultat
                    if (row == table.getRowCount() - 1) {
                        setBackground(couleurs[2]);
                        setForeground(textes[2]);
                        setFont(new Font("Consolas", Font.BOLD, 13));
                    } else {
                        setFont(Palette.FONT_STEP);
                    }
                }
                setBorder(new EmptyBorder(0, 8, 0, 8));
                return this;
            }
        });
    }
}
