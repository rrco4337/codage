package ui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

/**
 * Méthodes utilitaires statiques pour créer et styliser les composants Swing.
 *
 * Toutes les méthodes sont statiques : pas besoin d'instancier cette classe.
 * Elle est utilisée par ConversionPanel, GaussPanel et MainWindow.
 */
public class UIHelper {

    // ══════════════════════════════════════════════════════════════════════════
    //  CRÉATION DE COMPOSANTS DE BASE
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Crée un label aligné à droite avec la police et la couleur standard.
     *
     * @param txt le texte du label
     */
    public static JLabel label(String txt) {
        JLabel l = new JLabel(txt, SwingConstants.RIGHT);
        l.setFont(Palette.FONT_LBL);
        l.setForeground(Palette.FG_LIGHT);
        return l;
    }

    /**
     * Crée un champ de saisie stylisé (fond bleu, bordure ACCENT).
     *
     * @param hint texte indicatif affiché en info-bulle (tooltip)
     */
    public static JTextField champ(String hint) {
        JTextField f = new JTextField(14);
        f.setFont(Palette.FONT_FLD);
        f.setBackground(Palette.BG_PANEL);
        f.setForeground(Palette.FG_WHITE);
        f.setCaretColor(Palette.ACCENT2);
        f.setToolTipText(hint);
        f.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Palette.ACCENT, 2, true),
            new EmptyBorder(5, 10, 5, 10)));
        return f;
    }

    /**
     * Crée un bouton avec un dégradé bleu et des coins arrondis.
     * Le degradé est dessiné manuellement pour ignorer le look-and-feel système.
     *
     * @param txt le texte du bouton
     */
    public static JButton bouton(String txt) {
        JButton b = new JButton(txt) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                // Dégradé bleu vif → bleu foncé
                g2.setPaint(new GradientPaint(
                    0, 0,          Palette.ACCENT,
                    0, getHeight(), new Color(0, 80, 200)));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                // Texte centré
                g2.setColor(Palette.FG_WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth()  - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        b.setFont(Palette.FONT_BTN);
        b.setForeground(Palette.FG_WHITE);
        b.setContentAreaFilled(false);   // on dessine le fond nous-mêmes
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(260, 42));
        return b;
    }

    /**
     * Crée un panneau vide "zone de résultat" (fond bleu panel + bordure ACCENT).
     * Le contenu (JLabel) est à ajouter après.
     */
    public static JPanel resultPanel() {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setBackground(Palette.BG_PANEL);
        p.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Palette.ACCENT, 2, true),
            new EmptyBorder(10, 16, 10, 16)));
        return p;
    }

    /**
     * Crée un item de légende : un petit carré de couleur + un texte explicatif.
     * Utilisé dans le panneau d'explication du tableau Gauss.
     *
     * @param texte   description de la couleur
     * @param couleur couleur du carré
     */
    public static JPanel legendeItem(String texte, Color couleur) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        p.setBackground(new Color(12, 30, 75));

        // Petit carré coloré dessiné manuellement
        JLabel carre = new JLabel("  ") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(couleur);
                g2.fillRoundRect(0, 2, getWidth(), getHeight() - 4, 4, 4);
                g2.dispose();
            }
        };
        carre.setPreferredSize(new Dimension(16, 16));

        JLabel lbl = new JLabel(texte);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(Palette.FG_LIGHT);

        p.add(carre);
        p.add(lbl);
        return p;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STYLE DES CONTENEURS
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Applique le thème bleu sur un JTabbedPane existant.
     * Modifie les clés UIManager pour les onglets.
     */
    public static void styleTabs(JTabbedPane tabs) {
        tabs.setBackground(Palette.BG_DARK);
        tabs.setForeground(Palette.FG_LIGHT);
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.setOpaque(true);
        UIManager.put("TabbedPane.selected",             Palette.BG_PANEL);
        UIManager.put("TabbedPane.background",           Palette.BG_DARK);
        UIManager.put("TabbedPane.foreground",           Palette.FG_LIGHT);
        UIManager.put("TabbedPane.contentAreaColor",     Palette.BG_DARK);
        UIManager.put("TabbedPane.tabAreaBackground",    Palette.BG_DARK);
        UIManager.put("TabbedPane.unselectedBackground", Palette.BG_DARK);
        UIManager.put("TabbedPane.focus",                Palette.ACCENT);
    }

    /**
     * Applique le style visuel au tableau des étapes Gauss.
     *
     * Logique de couleurs par cellule :
     *   - Si la ligne correspond à une addition (colonne 4 commence par "✓") → vert
     *   - Sinon → bleu-gris
     *   - Colonne 1 (bit 0/1) → grande police, couleur contrastée
     *   - Colonne 4 (action)  → texte gras coloré
     */
    public static void styleTable(JTable table) {
        table.setBackground(Palette.BG_PANEL);
        table.setForeground(Palette.FG_WHITE);
        table.setFont(Palette.FONT_STEP);
        table.setGridColor(new Color(40, 80, 160));
        table.setRowHeight(26);
        table.setSelectionBackground(Palette.ACCENT);
        table.setSelectionForeground(Palette.FG_WHITE);

        // Style de l'en-tête
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(15, 35, 90));
        header.setForeground(Palette.ACCENT2);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setReorderingAllowed(false);

        // Renderer personnalisé : coulore chaque cellule selon la logique Gauss
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object value, boolean isSelected,
                    boolean hasFocus, int row, int col) {

                super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);

                if (isSelected) {
                    // Ligne sélectionnée par l'utilisateur
                    setBackground(Palette.ACCENT);
                    setForeground(Palette.FG_WHITE);
                    setFont(Palette.FONT_STEP);
                    setHorizontalAlignment(SwingConstants.LEFT);
                } else {
                    // Lire la colonne 4 pour savoir si on additionne à cette étape
                    Object actionVal = t.getModel().getValueAt(row, 4);
                    boolean onAjoute = actionVal != null
                                    && actionVal.toString().startsWith("✓");

                    // Fond et texte selon l'action
                    setBackground(onAjoute ? Palette.ROW_ADD  : Palette.ROW_SKIP);
                    setForeground(onAjoute ? Palette.TXT_ADD  : Palette.TXT_SKIP);
                    setFont(Palette.FONT_STEP);
                    setHorizontalAlignment(SwingConstants.LEFT);

                    // Colonne 1 — le bit (0 ou 1) : grande police, couleur vive
                    if (col == 1) {
                        setForeground(onAjoute ? new Color(80, 255, 140)
                                               : new Color(255, 120, 120));
                        setFont(new Font("Consolas", Font.BOLD, 15));
                        setHorizontalAlignment(SwingConstants.CENTER);
                    }

                    // Colonne 4 — action (✓ OUI / ✗ NON) : gras, couleur vive
                    if (col == 4) {
                        setForeground(onAjoute ? new Color(80, 255, 140)
                                               : new Color(255, 120, 120));
                        setFont(new Font("Segoe UI", Font.BOLD, 12));
                    }
                }

                setBorder(new EmptyBorder(0, 6, 0, 6));
                return this;
            }
        });
    }

    // Empêche l'instanciation (classe utilitaire uniquement)
    private UIHelper() {}
}
