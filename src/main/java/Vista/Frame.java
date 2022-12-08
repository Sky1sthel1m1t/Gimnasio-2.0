package Vista;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Frame extends JFrame {

    private JButton btnTipos = new JButton("Tipos");
    private JButton btnEntrenador = new JButton("Entrenadores");
    private JButton btnMovimientos = new JButton("Movimientos");
    private JButton btnPokemones = new JButton("Pokemones");
    private JButton btnRealizarCombate = new JButton("Realizar combate");
    private JButton btnEnseñarMovimientos = new JButton("Admin. movimientos de los pokemones");
    private JButton btnCombatesRegistrados = new JButton("Combates Registrados");

    ArrayList<JPanel> paneles = new ArrayList<>();
    private JPanel panelPrincipal = new JPanel();
    private PanelEntrenador panelEntrenador;
    private PanelTipos panelTipos;
    private PanelMovimiento panelMovimientos;
    private PanelPokemon panelPokemon;
    private PanelCombate panelCombate;
    private PanelEnseñarMovimientos panelEnseñarMovimientos;
    private PanelCombatesRegistrados panelCombatesRegistrados;

    public Frame(int tamaño){
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        init1(tamaño);
        this.setVisible(true);
    }

    public void init1(int tamaño){
        Dimension dimension = new Dimension(tamaño,tamaño);

        panelPrincipal.setLayout(null);
        panelPrincipal.setSize(dimension);
        panelPrincipal.setPreferredSize(panelPrincipal.getSize());

        JLabel lbFondo = new JLabel();
        lbFondo.setLocation(0,0);
        lbFondo.setSize(dimension);

        // numBotones * espacioY = mitadY
        // 7 * 60 = 420
        int x = panelPrincipal.getWidth() / 2;
        int y = panelPrincipal.getHeight() / 2 - (420 / 2);
        int ancho = 200;
        int altura = 50;
        int espacioY = altura + 10;

        btnTipos.setBounds(x - (ancho / 2), y, ancho, altura);
        y += espacioY;
        btnEntrenador.setBounds(x - (ancho / 2), y, ancho, altura);
        y += espacioY;
        btnMovimientos.setBounds(x - (ancho / 2), y, ancho, altura);
        y += espacioY;
        btnPokemones.setBounds(x - (ancho / 2), y, ancho, altura);
        y += espacioY;
        btnRealizarCombate.setBounds(x - (ancho / 2), y, ancho, altura);
        y += espacioY;
        btnEnseñarMovimientos.setBounds(x - (ancho / 2), y, ancho, altura);
        y += espacioY;
        btnCombatesRegistrados.setBounds(x - (ancho / 2), y, ancho, altura);

        btnTipos.addActionListener(e -> {
            if (paneles.size() == 2){
                paneles.remove(1);
            }

            panelTipos = new PanelTipos(this);
            panelPrincipal.setVisible(false);
            this.add(panelTipos);
            this.pack();
            paneles.add(panelTipos);
        });

        btnEntrenador.addActionListener(e -> {
            if (paneles.size() == 2){
                paneles.remove(1);
            }

            panelEntrenador = new PanelEntrenador(this, 30);
            panelPrincipal.setVisible(false);
            this.add(panelEntrenador);
            this.pack();
            paneles.add(panelEntrenador);
        });

        btnMovimientos.addActionListener(e -> {
            if (paneles.size() == 2){
                paneles.remove(1);
            }

            panelMovimientos = new PanelMovimiento(this, 30);
            panelPrincipal.setVisible(false);
            this.add(panelMovimientos);
            this.pack();
            paneles.add(panelMovimientos);
        });

        btnPokemones.addActionListener(e -> {
            if (paneles.size() == 2){
                paneles.remove(1);
            }

            panelPokemon = new PanelPokemon(this, 50);
            panelPrincipal.setVisible(false);
            this.add(panelPokemon);
            this.pack();
            paneles.add(panelPokemon);
        });

        btnRealizarCombate.addActionListener(e -> {
            if (paneles.size() == 2){
                paneles.remove(1);
            }

            panelCombate = new PanelCombate(this);
            panelPrincipal.setVisible(false);
            this.add(panelCombate);
            this.pack();
            paneles.add(panelCombate);
        });

        btnEnseñarMovimientos.addActionListener(e -> {
            if (paneles.size() == 2){
                paneles.remove(1);
            }

            panelEnseñarMovimientos = new PanelEnseñarMovimientos(this);
            panelPrincipal.setVisible(false);
            this.add(panelEnseñarMovimientos);
            this.pack();
            paneles.add(panelEnseñarMovimientos);
        });

        btnCombatesRegistrados.addActionListener(e -> {
            if (paneles.size() == 2){
                paneles.remove(1);
            }

            panelCombatesRegistrados = new PanelCombatesRegistrados(this, 10);
            panelPrincipal.setVisible(false);
            this.add(panelCombatesRegistrados);
            this.pack();
            paneles.add(panelCombatesRegistrados);
        });

        cargarFondo(lbFondo);

        panelPrincipal.add(btnTipos);
        panelPrincipal.add(btnEntrenador);
        panelPrincipal.add(btnMovimientos);
        panelPrincipal.add(btnPokemones);
        panelPrincipal.add(btnRealizarCombate);
        panelPrincipal.add(btnEnseñarMovimientos);
        panelPrincipal.add(btnCombatesRegistrados);
        panelPrincipal.add(lbFondo);
        paneles.add(panelPrincipal);

        this.add(panelPrincipal);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public void volver(){
        paneles.get(1).setVisible(false);
        panelPrincipal.setVisible(true);
    }

    private void cargarFondo(JLabel lbFondo){
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("Imagenes/fondo.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        Image dimg = img.getScaledInstance(lbFondo.getWidth(), lbFondo.getHeight(),
                Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(dimg);
        lbFondo.setIcon(imageIcon);
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

}
