/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║            POINT D'ENTRÉE DE L'APPLICATION                   ║
 * ║                                                              ║
 * ║  Ce fichier contient uniquement le main().                   ║
 * ║  Tout le reste est organisé en packages :                    ║
 * ║                                                              ║
 * ║  algorithmes/                                                ║
 * ║    ConversionBase.java       — algo conversion de base       ║
 * ║    GaussMultiplication.java  — algo multiplication Gauss     ║
 * ║                                                              ║
 * ║  ui/                                                         ║
 * ║    Palette.java              — couleurs & polices            ║
 * ║    UIHelper.java             — composants réutilisables      ║
 * ║    ConversionPanel.java      — onglet 1                      ║
 * ║    GaussPanel.java           — onglet 2                      ║
 * ║    MainWindow.java           — fenêtre principale            ║
 * ╚══════════════════════════════════════════════════════════════╝
 */
public class ConversionNormale {

    /**
     * Lance l'interface graphique sur le thread Swing (EDT).
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            new ui.MainWindow().setVisible(true);
        });
    }
}
