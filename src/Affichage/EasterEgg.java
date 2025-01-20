package Affichage;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Classe principale représentant l'easter egg du jeu Snake.
 * Utilise un JFrame pour afficher le jeu.
 */
public class EasterEgg extends JFrame {
    private static final long serialVersionUID = 1L;

    // Dimensions de la fenêtre
    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;

    // Framerate du jeu
    private static final int FPS = 120;

    /**
     * Constructeur de l'easter egg.
     * Configure la fenêtre et initialise les composants.
     */
    public EasterEgg() {
        super();

        // Configuration de la fenêtre
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setUndecorated(true);

        // Création du Panel de jeu
        GamePanel gamePanel = new GamePanel(this);
        add(gamePanel);

        // Gestion des touches via KeyListener
        KeyListener keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                gamePanel.actionPerformed(new ActionEvent(this, e.getKeyCode(), null));
            }
        };
        gamePanel.addKeyListener(keyListener);
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();

        // Boucle de jeu avec un timer
        Timer timer = new Timer(1000 / FPS, gamePanel);
        timer.start();

        setLocationRelativeTo(null);
        setVisible(true);
    }
}

/**
 * Panel de jeu pour l'easter egg.
 * Contient la logique du jeu et les éléments graphiques.
 */
class GamePanel extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;

    // Objets du jeu
    private Snake snake;
    private Food food;

    // Configuration du jeu
    private int gameSpeed = 10; // Nombre de frames entre deux "ticks"
    private int nbFrameSinceLastTick = 0;

    // Taille de la grille
    private int cellSizeX = 50;
    private int cellSizeY = 50;
    private int gridSizeX;
    private int gridSizeY;

    // Référence à la fenêtre principale
    private JFrame frame;

    /**
     * Constructeur du Panel de jeu.
     *
     * @param frame La fenêtre principale contenant ce Panel.
     */
    public GamePanel(JFrame frame) {
        this.frame = frame;

        // Calcul de la taille de la grille
        gridSizeX = EasterEgg.WIDTH / cellSizeX;
        gridSizeY = EasterEgg.HEIGHT / cellSizeY;

        setBackground(Color.BLACK);

        // Initialisation du serpent et de la nourriture
        snake = new Snake(gridSizeX / 2, gridSizeY / 2);
        food = new Food(gridSizeX, gridSizeY);
    }

    /**
     * Dessine les éléments du jeu.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Dessin de la nourriture
        g2d.setColor(Color.RED);
        g2d.fillRect(food.getX() * cellSizeX, food.getY() * cellSizeY, cellSizeX, cellSizeY);

        // Dessin du serpent
        g2d.setColor(Color.GREEN);
        for (int i = 0; i < snake.getSize(); i++) {
            g2d.fillRect(snake.getBodyX(i) * cellSizeX, snake.getBodyY(i) * cellSizeY, cellSizeX, cellSizeY);
        }
    }

    /**
     * Gère les actions déclenchées (mises à jour ou événements clavier).
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getID() == 0) {
            gameUpdate();
        } else {
            int newDirection = snake.getDirection();

            switch (e.getID()) {
                case KeyEvent.VK_UP -> newDirection = 1;
                case KeyEvent.VK_RIGHT -> newDirection = 2;
                case KeyEvent.VK_DOWN -> newDirection = 3;
                case KeyEvent.VK_LEFT -> newDirection = 4;
            }

            if (snake.setDirection(newDirection)) {
                snake.updateBody(gridSizeX, gridSizeY);
                nbFrameSinceLastTick = 0;
            }
        }
    }

    /**
     * Met à jour l'état du jeu.
     */
    private void gameUpdate() {
        nbFrameSinceLastTick = (nbFrameSinceLastTick + 1) % gameSpeed;

        if (snake.isEating(food.getX(), food.getY())) {
            food = new Food(gridSizeX - 1, gridSizeY - 1);
            snake.grow();
            
        }
        
        if (nbFrameSinceLastTick == 0) {
            snake.updateBody(gridSizeX, gridSizeY);
        }

        if (snake.isDead()) {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }

     

        repaint();
    }
}

/**
 * Représente le serpent du jeu.
 */
class Snake {
    public static final int MAX_SIZE = 200;

    private int[] bodyX = new int[MAX_SIZE];
    private int[] bodyY = new int[MAX_SIZE];
    private int size = 5;
    

	private int direction = 2;

    public Snake(int startX, int startY) {
        bodyX[0] = startX;
        bodyY[0] = startY;
    }

    public void updateBody(int gridSizeX, int gridSizeY) {
        for (int i = size - 1; i > 0; i--) {
            bodyX[i] = bodyX[i - 1];
            bodyY[i] = bodyY[i - 1];
        }

        bodyX[0] = (bodyX[0] + getDirX() + gridSizeX) % gridSizeX;
        bodyY[0] = (bodyY[0] + getDirY() + gridSizeY) % gridSizeY;
    }

    private int getDirX() {
        return (direction == 2) ? 1 : (direction == 4) ? -1 : 0;
    }

    private int getDirY() {
        return (direction == 3) ? 1 : (direction == 1) ? -1 : 0;
    }

    public boolean isDead() {
        for (int i = 1; i < size; i++) {
            if (bodyX[0] == bodyX[i] && bodyY[0] == bodyY[i]) return true;
        }
        return false;
    }

    public boolean isEating(int foodX, int foodY) {
        return bodyX[0] == foodX && bodyY[0] == foodY;
    }


    public void grow() {
        this.size++;
        this.bodyX[this.size - 1] = this.bodyX[this.size - 2];
        this.bodyY[this.size - 1] = this.bodyY[this.size - 2];
        
    }
    
    public int getSize() {
		return size;
	}

    public int getBodyX(int index) {
        return bodyX[index];
    }

    public int getBodyY(int index) {
        return bodyY[index];
    }

    public boolean setDirection(int newDirection) {
        if (Math.abs(direction - newDirection) == 2) return false;
        direction = newDirection;
        return true;
    }

    public int getDirection() {
        return direction;
    }
}

/**
 * Représente la nourriture du jeu.
 */
class Food {
    private final int x;
    private final int y;

    public Food(int gridSizeX, int gridSizeY) {
        this.x = (int) (Math.random() * (gridSizeX - 1));
        this.y = (int) (Math.random() * (gridSizeY - 1));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
