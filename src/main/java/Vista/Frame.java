package Vista;

import Visuales.Fuentes;

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

    public Frame(int tamaño) {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        init1(tamaño);
        this.setVisible(true);
        this.setResizable(false);
    }

    public void init1(int tamaño) {
        Dimension dimension = new Dimension(tamaño, tamaño);
        JLabel lbFondo = new JLabel();
        JLabel lbTitulo1 = new JLabel("Gimnasio");
        JLabel lbTitulo2 = new JLabel("Pokemon");
        JLabel lbImgDerecha = new JLabel();
        JLabel lbImgIzquierda = new JLabel();
        Color colorFondoBotones = Color.BLACK;
        Color colorLetrasBotones = new Color(0xE53838);

        panelPrincipal.setLayout(null);
        panelPrincipal.setSize(dimension);
        panelPrincipal.setPreferredSize(panelPrincipal.getSize());

        lbFondo.setLocation(0, 0);
        lbFondo.setSize(dimension);

        // numBotones * espacioY = mitadY
        // 7 * 60 = 420
        int x = panelPrincipal.getWidth() / 2;
        int y = panelPrincipal.getHeight() / 2 - (760 / 2);
        int ancho = 350;
        int altura = 50;
        int espacioY = altura + 10;

        Font fuente = Fuentes.getFuentes().getSolid(50);

        lbImgIzquierda.setBounds(x - (250),y, 150, 140);
        lbTitulo1.setBounds(x - (300 / 2), y, 300, 70);
        lbTitulo1.setFont(fuente);
        lbTitulo1.setHorizontalAlignment(JLabel.CENTER);
        lbTitulo1.setForeground(Color.BLACK);
        lbImgDerecha.setBounds(x + (110),y, 150, 140);
        y += lbTitulo1.getHeight();
        lbTitulo2.setBounds(x - (300 / 2), y, 300, 70);
        lbTitulo2.setFont(fuente);
        lbTitulo2.setHorizontalAlignment(JLabel.CENTER);
        lbTitulo2.setForeground(Color.BLACK);
        y += lbTitulo2.getHeight() + 10;

        cargarImagenesTitulo(150,140, lbImgIzquierda, lbImgDerecha);

        fuente = Fuentes.getFuentes().getSolid(29);

        btnTipos.setBounds(x - (ancho / 2), y, ancho, altura);
        btnTipos.setFont(fuente);
        btnTipos.setFocusPainted(false);
        btnTipos.setBorderPainted(false);
        btnTipos.setBackground(colorFondoBotones);
        btnTipos.setForeground(colorLetrasBotones);
        y += espacioY;
        btnEntrenador.setBounds(x - (ancho / 2), y, ancho, altura);
        btnEntrenador.setFont(fuente);
        btnEntrenador.setFocusPainted(false);
        btnEntrenador.setBorderPainted(false);
        btnEntrenador.setBackground(colorFondoBotones);
        btnEntrenador.setForeground(colorLetrasBotones);
        y += espacioY;
        btnMovimientos.setBounds(x - (ancho / 2), y, ancho, altura);
        btnMovimientos.setFont(fuente);
        btnMovimientos.setFocusPainted(false);
        btnMovimientos.setBorderPainted(false);
        btnMovimientos.setBackground(colorFondoBotones);
        btnMovimientos.setForeground(colorLetrasBotones);
        y += espacioY;
        btnPokemones.setBounds(x - (ancho / 2), y, ancho, altura);
        btnPokemones.setFont(fuente);
        btnPokemones.setFocusPainted(false);
        btnPokemones.setBorderPainted(false);
        btnPokemones.setBackground(colorFondoBotones);
        btnPokemones.setForeground(colorLetrasBotones);
        y += espacioY;
        btnRealizarCombate.setBounds(x - (ancho / 2), y, ancho, altura);
        btnRealizarCombate.setFont(fuente);
        btnRealizarCombate.setFocusPainted(false);
        btnRealizarCombate.setBorderPainted(false);
        btnRealizarCombate.setBackground(colorFondoBotones);
        btnRealizarCombate.setForeground(colorLetrasBotones);
        y += espacioY;
        btnEnseñarMovimientos.setBounds(x - (ancho / 2), y, ancho, altura);
        btnEnseñarMovimientos.setFont(Fuentes.getFuentes().getSolid(16));
        btnEnseñarMovimientos.setFocusPainted(false);
        btnEnseñarMovimientos.setBorderPainted(false);
        btnEnseñarMovimientos.setBackground(colorFondoBotones);
        btnEnseñarMovimientos.setForeground(colorLetrasBotones);
        y += espacioY;
        btnCombatesRegistrados.setBounds(x - (ancho / 2), y, ancho, altura);
        btnCombatesRegistrados.setFont(fuente);
        btnCombatesRegistrados.setFocusPainted(false);
        btnCombatesRegistrados.setBorderPainted(false);
        btnCombatesRegistrados.setBackground(colorFondoBotones);
        btnCombatesRegistrados.setForeground(colorLetrasBotones);

        System.out.println(y);

        btnTipos.addActionListener(e -> {
            if (paneles.size() == 2) {
                paneles.remove(1);
            }

            panelTipos = new PanelTipos(this);
            panelPrincipal.setVisible(false);
            this.add(panelTipos);
            this.pack();
            paneles.add(panelTipos);
        });

        btnEntrenador.addActionListener(e -> {
            if (paneles.size() == 2) {
                paneles.remove(1);
            }

            panelEntrenador = new PanelEntrenador(this, 30);
            panelPrincipal.setVisible(false);
            this.add(panelEntrenador);
            this.pack();
            paneles.add(panelEntrenador);
        });

        btnMovimientos.addActionListener(e -> {
            if (paneles.size() == 2) {
                paneles.remove(1);
            }

            panelMovimientos = new PanelMovimiento(this, 30);
            panelPrincipal.setVisible(false);
            this.add(panelMovimientos);
            this.pack();
            paneles.add(panelMovimientos);
        });

        btnPokemones.addActionListener(e -> {
            if (paneles.size() == 2) {
                paneles.remove(1);
            }

            panelPokemon = new PanelPokemon(this, 50);
            panelPrincipal.setVisible(false);
            this.add(panelPokemon);
            this.pack();
            paneles.add(panelPokemon);
        });

        btnRealizarCombate.addActionListener(e -> {
            if (paneles.size() == 2) {
                paneles.remove(1);
            }

            panelCombate = new PanelCombate(this);
            panelPrincipal.setVisible(false);
            this.add(panelCombate);
            this.pack();
            paneles.add(panelCombate);
        });

        btnEnseñarMovimientos.addActionListener(e -> {
            if (paneles.size() == 2) {
                paneles.remove(1);
            }

            panelEnseñarMovimientos = new PanelEnseñarMovimientos(this);
            panelPrincipal.setVisible(false);
            this.add(panelEnseñarMovimientos);
            this.pack();
            paneles.add(panelEnseñarMovimientos);
        });

        btnCombatesRegistrados.addActionListener(e -> {
            if (paneles.size() == 2) {
                paneles.remove(1);
            }

            panelCombatesRegistrados = new PanelCombatesRegistrados(this, 10);
            panelPrincipal.setVisible(false);
            this.add(panelCombatesRegistrados);
            this.pack();
            paneles.add(panelCombatesRegistrados);
        });

        cargarFondo(lbFondo);

        panelPrincipal.add(lbTitulo1);
        panelPrincipal.add(lbTitulo2);
        panelPrincipal.add(lbImgIzquierda);
        panelPrincipal.add(lbImgDerecha);
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

    public void volver() {
        paneles.get(1).setVisible(false);
        panelPrincipal.setVisible(true);
    }

    private void cargarFondo(JLabel lbFondo) {
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

    private void cargarImagenesTitulo(int ancho, int alto, JLabel izq, JLabel der) {
        BufferedImage img1 = null;
        BufferedImage img2 = null;
        try {
            img1 = ImageIO.read(new File("Imagenes/Blastoise.png"));
            img2 = ImageIO.read(new File("Imagenes/Charizard.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        Image dimg = img1.getScaledInstance(ancho, alto,
                Image.SCALE_SMOOTH);
        Image dimg2 = img2.getScaledInstance(ancho, alto,
                Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(dimg);
        der.setIcon(imageIcon);
        imageIcon = new ImageIcon(dimg2);
        izq.setIcon(imageIcon);
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }
}
