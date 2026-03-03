package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Fenêtre principale de l'application.
 *
 * Contient :
 *   - Un titre en haut.
 *   - Un JTabbedPane avec deux onglets :
 *       Onglet 1 → ConversionPanel   (conversion de base X vers base Y)
 *       Onglet 2 → GaussPanel        (multiplication binaire par décalages)
 */
public class MainWindow extends JFrame {

    public MainWindow() {
        // ── Configuration de la fenêtre ────────────────────────────────────────
        setTitle("Outils Numériques — Conversion & Calcul Binaire");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 680);
        setLocationRelativeTo(null);   // centrer l'écran
        setMinimumSize(new Dimension(640, 500));

        // ── Panneau racine ────────────────────────────────────────────────────
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Palette.BG_DARK);
        setContentPane(root);

        // ── Titre principal ───────────────────────────────────────────────────
        JLabel titre = new JLabel("Outils Numériques", SwingConstants.CENTER);
        titre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titre.setForeground(Palette.ACCENT2);
        titre.setBorder(new EmptyBorder(18, 0, 10, 0));
        root.add(titre, BorderLayout.NORTH);

        // ── Onglets ───────────────────────────────────────────────────────────
        JTabbedPane onglets = new JTabbedPane();
        UIHelper.styleTabs(onglets);

        // Onglet 1 — Conversion de base
        onglets.addTab("  📐  Conversion de base  ", new ConversionPanel());

        // Onglet 2 — Calcul binaire Gauss
        onglets.addTab("  🔢  Multiplication Gauss  ", new GaussPanel());

        // Onglet 3 — Opérations bit-à-bit et décalages
        onglets.addTab("  ⛙️  Opérations binaires (AND/OR/XOR/NOT/Shift)  ", new BinairePanel());

        root.add(onglets, BorderLayout.CENTER);
    }
}
