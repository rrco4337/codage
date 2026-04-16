package ui;

import algorithmes.IEEE754Encoding;
import algorithmes.IEEE754Encoding.Resultat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Onglet IEEE 754 (32 bits) : encodage d'une valeur en simple precision.
 */
public class IEEE754Panel extends JPanel {

    private final JTextField fieldValeur;
    private final JLabel labelType;
    private final JLabel labelBinaire;
    private final JLabel labelHexa;
    private final JLabel labelSigne;
    private final JLabel labelExposant;
    private final JLabel labelMantisse;

    public IEEE754Panel() {
        setLayout(new GridBagLayout());
        setBackground(Palette.BG_DARK);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx = 0; g.gridy = 0; g.weightx = 0;
        add(UIHelper.label("Valeur decimale :"), g);
        g.gridx = 1; g.weightx = 1;
        fieldValeur = UIHelper.champ("Ex: 29,75  ou  -12,15");
        add(fieldValeur, g);

        g.gridx = 0; g.gridy = 1; g.gridwidth = 2; g.weightx = 1;
        JButton btnEncoder = UIHelper.bouton("Encoder en IEEE 754 (32 bits)");
        add(btnEncoder, g);

        JPanel res = UIHelper.resultPanel();
        res.setLayout(new GridBagLayout());
        GridBagConstraints r = new GridBagConstraints();
        r.insets = new Insets(4, 8, 4, 8);
        r.fill = GridBagConstraints.HORIZONTAL;
        r.gridx = 0; r.weightx = 0;
        r.gridy = 0;
        res.add(UIHelper.label("Type :"), r);
        r.gridy = 1;
        res.add(UIHelper.label("Binaire (1|8|23) :"), r);
        r.gridy = 2;
        res.add(UIHelper.label("Hexa :"), r);
        r.gridy = 3;
        res.add(UIHelper.label("Signe :"), r);
        r.gridy = 4;
        res.add(UIHelper.label("Exposant :"), r);
        r.gridy = 5;
        res.add(UIHelper.label("Mantisse :"), r);

        r.gridx = 1; r.weightx = 1;
        r.gridy = 0;
        labelType = valeurLabel("-");
        res.add(labelType, r);
        r.gridy = 1;
        labelBinaire = valeurLabel("-");
        res.add(labelBinaire, r);
        r.gridy = 2;
        labelHexa = valeurLabel("-");
        res.add(labelHexa, r);
        r.gridy = 3;
        labelSigne = valeurLabel("-");
        res.add(labelSigne, r);
        r.gridy = 4;
        labelExposant = valeurLabel("-");
        res.add(labelExposant, r);
        r.gridy = 5;
        labelMantisse = valeurLabel("-");
        res.add(labelMantisse, r);

        g.gridy = 2;
        add(res, g);

        ActionListener action = e -> encoder();
        btnEncoder.addActionListener(action);
        fieldValeur.addActionListener(action);
    }

    private JLabel valeurLabel(String txt) {
        JLabel lbl = new JLabel(txt);
        lbl.setFont(new Font("Consolas", Font.BOLD, 14));
        lbl.setForeground(Palette.ACCENT2);
        return lbl;
    }

    private void encoder() {
        try {
            Resultat r = IEEE754Encoding.encoderDepuisTexte(fieldValeur.getText());
            labelType.setForeground(Palette.ACCENT2);
            labelBinaire.setForeground(Palette.ACCENT2);
            labelHexa.setForeground(Palette.ACCENT2);
            labelSigne.setForeground(Palette.ACCENT2);
            labelExposant.setForeground(Palette.ACCENT2);
            labelMantisse.setForeground(Palette.ACCENT2);

            labelType.setText(r.typeValeur + "   (float interprete: " + r.valeur + ")");
            labelBinaire.setText(r.binaire32);
            labelHexa.setText("0x" + r.hexadecimal);
            labelSigne.setText(r.bitSigne);
            labelExposant.setText(r.exposant + "   (decimal: " + Integer.parseInt(r.exposant, 2) + ")");
            labelMantisse.setText(r.mantisse);
        } catch (IllegalArgumentException ex) {
            afficherErreur(ex.getMessage());
        }
    }

    private void afficherErreur(String msg) {
        labelType.setForeground(Palette.ERR_RED);
        labelBinaire.setForeground(Palette.ERR_RED);
        labelHexa.setForeground(Palette.ERR_RED);
        labelSigne.setForeground(Palette.ERR_RED);
        labelExposant.setForeground(Palette.ERR_RED);
        labelMantisse.setForeground(Palette.ERR_RED);

        labelType.setText("Erreur");
        labelBinaire.setText(msg);
        labelHexa.setText("-");
        labelSigne.setText("-");
        labelExposant.setText("-");
        labelMantisse.setText("-");
    }
}