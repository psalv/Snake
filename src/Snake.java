
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.io.*;

import javax.swing.*;
import java.lang.Math;
import java.awt.event.*;

import IO.*;
import Queue.*;

public class Snake extends JFrame {


    // Public constants
    public static final int DEFAULT_TIME_DELAY = 35;

    // Attributes
    private int timeDelay = DEFAULT_TIME_DELAY;

    public JPanel gamePanel;

    //Will be used to know from which position we are moving, and which positions are occupied.
    private CoordPair front;                 //The moving tile
    private Square end;                      //The tile to delete


    //A way to tell the current direction that we are travelling.
    private static final String up = "U";
    private static final String down = "D";
    private static final String left = "L";
    private static final String right = "R";
    private static final String space = "SPACE";
    private static final String quitt = "QUIT";
    private static final String speed = "SPEED";
    private static final String music = "MUSIC";

    public static enum Direction {UP, DOWN, LEFT, RIGHT}

    private Direction dir = Direction.RIGHT;

    private boolean start = true;

    //Size of the snake game.
    private int size;

    //This is where I will look up and change the colour of the tiles.
    private Square[][] squareGrid;

    private int score = 0;
    private boolean quit = false;

    //Music playing variables
    public Clip clip;
    public boolean playing = true;

    private LinkedPriorityQueue highscores = new readScores().getQueue();

    //Highscore entry variables
    private static final String ENTER = "enter";
    private String entry;
    private JTextField input;
    private boolean cont;


    public Snake(int size) {

        // set up GUI aspects of the BuildSnakePreKeyBindings component
        super("Snake mk.III");
        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //This makes music play, this may have been the only time a youtube comment has every been helpfl.
        play();               //comment to remove music

        //Adds the key listener, still need to implement the associated methods.
        this.size = size;

        setSquareGrid();
        setFood();

        //set up the GUI window
        this.add(gamePanel);
        this.pack();
        this.setSize(20 * size, 20 * size);
        this.setVisible(true);
        this.setResizable(false);
        runLoop();
    }

    public void play() {
        try {
            File file = new File("Echobo.wav");                  //Only reads .wav files
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();
//            Thread.sleep(clip.getMicrosecondLength());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Initializes and prepares the JFrame layout with the grid of square objects.
     */
    public void setSquareGrid() {

        gamePanel = new JPanel();

        //Initializes size of grid and front/end positions.
        front = new CoordPair(size / 2, size / 2);

        // To build the maze we will use a 2D array storing all square objects.

        squareGrid = new Square[size][size];
        // SquareLayout will arrange the Squares in the window
        gamePanel.setLayout(new SquareLayout(size, size, 0));

        // for each row
        for (Integer r = 1; r < size; r++) {

            for (Integer c = 1; c < size; c++) {

                //The snakes starting position.
                if (r == size / 2) {

                    if (c >= size / 2 && c < size / 2 + 4) {
                        squareGrid[r][c] = new Square(Square.SquareType.BODY);
                    } else if (c == 1 || c == size - 1)
                        squareGrid[r][c] = new Square(Square.SquareType.WALL);

                    else {
                        squareGrid[r][c] = new Square(Square.SquareType.NONE);
                    }
                }

                //Otherwise the tile is a NONE tile (unless it's a border).

                else {
                    if (r == 1 || c == 1 || r == size - 1 || c == size - 1)
                        squareGrid[r][c] = new Square(Square.SquareType.WALL);
                    else
                        squareGrid[r][c] = new Square(Square.SquareType.NONE);
                }

                gamePanel.add(squareGrid[r][c]);

            }// end for cols
        }// end for rows

        //Sets the next of each tile (to be used to find the new end).
        for (int i = size / 2 + 3; i > size / 2; --i) {
            squareGrid[size / 2][i].setNext(squareGrid[size / 2][i - 1]);
        }

        end = squareGrid[size / 2][size / 2 + 3];

        bindKeys(gamePanel);
    }

    /**
     * Resets the snake game so one can play again by pressing the spacebar.
     */
    private void reset() {

        for (Integer r = 1; r < size; r++) {
            for (Integer c = 1; c < size; c++) {

                if (r == size / 2) {
                    if (c >= size / 2 && c < size / 2 + 4) {
                        squareGrid[r][c].setType(Square.SquareType.BODY);
                    } else if (!(c == 1 || c == size - 1))
                        squareGrid[r][c].setType(Square.SquareType.NONE);

                } else if (!(r == 1 || c == 1 || r == size - 1 || c == size - 1))
                    squareGrid[r][c].setType(Square.SquareType.NONE);
            }
        }

        for (int i = size / 2 + 3; i > size / 2; --i) {
            squareGrid[size / 2][i].setNext(squareGrid[size / 2][i - 1]);
        }

        setFood();
        repaint();

        dir = Direction.RIGHT;
        start = true;
        score = 0;
        front = new CoordPair(size / 2, size / 2);
        end = squareGrid[size / 2][size / 2 + 3];
    }


    /**
     * Makes a square tile of type food, does so randomly.
     * Criteria requires the random tile is NONE type.
     */
    public void setFood() {
        Square toDo = squareGrid[1][1];
        while (toDo.getType() == Square.SquareType.WALL || toDo.getType() == Square.SquareType.BODY) {
            try {
                int r = (int) (Math.random() * (size - 2) + 1);
                int c = (int) (Math.random() * (size - 2) + 1);
                toDo = squareGrid[r][c];
            } catch (ArrayIndexOutOfBoundsException e) {
                continue;
            }
        }
        toDo.setType(Square.SquareType.FOOD);
    }


    public Square getSquare(CoordPair c) {
        return squareGrid[c.gRight()][c.gLeft()];
    }

    /**
     * Uses the direction and the squareGrid to find the next tile.
     *
     * @return the square in the next tile
     */
    public Square getNext() {
        String d = getDirection();

        if (d.equals("U")) {
            front.sRight(front.gRight() - 1);
        } else if (d.equals("D")) {
            front.sRight(front.gRight() + 1);
        } else if (d.equals("L")) {
            front.sLeft(front.gLeft() - 1);
        } else if (d.equals("R")) {
            front.sLeft(front.gLeft() + 1);
        }

        return getSquare(front);

    }

    public void runLoop() {

        while (!quit) {

            try {
                Thread.sleep(100);
            } catch (Exception e) {
                System.err.println("Error while issuing time delay\n" + e.getMessage());
            }

            if (start)
                run();
        }
    }

    public class TextPromptBox extends JFrame {

        public TextPromptBox(boolean first, Integer score) {
            super("You did alright: " + score.toString());

            if (first)
                setTitle("NEW HIGH SCORE: " + score.toString());

            super.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            input = new JTextField();

            bindKeys(input);
            add(input, BorderLayout.CENTER);
            pack();
            setSize(200, 50);
            setVisible(true);
            setResizable(false);
        }

        private void close() {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }

        private void bindKeys(JComponent p) {
            p.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), ENTER);
            p.getActionMap().put(ENTER, new EnterAction());
        }

        private class EnterAction extends AbstractAction {

            public void actionPerformed(ActionEvent e) {
                entry = input.getText();
                cont = true;
                close();
            }
        }
    }


    /**
     * Creates a while loop that waits for a direction other than right to be pressed, starting the game.
     * After starting will continue to run until next() returns false meaning we have hit a wall or body.
     */
    public void run() {

        boolean again = true;

        while (dir == Direction.RIGHT) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                System.err.println("Error while issuing time delay\n" + e.getMessage());
            }
        }

        start = false;
        while (again) {
            again = next();
            repaint();
        }


        if ((score > highscores.lastPriority()) && timeDelay == 35) {
            cont = false;

            boolean first = (highscores.firstPriority() < score);
            new TextPromptBox(first, score);

            while (!cont) {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    System.err.println("Error while issuing time delay\n" + e.getMessage());
                }
            }

            highscores.enqueue(entry, score);
            new saveScores(highscores);

        }

        System.out.println("\n--------------------\n\nFinal score: " + score);
        System.out.println("\nHigh scores:");
        System.out.println(highscores.toString());
        System.out.println("--------------------");
    }


    /**
     * Uses the direction we are currently going in to determine the next tile.
     * If the tile is a wall or body part, the game will end.
     * Otherwise the snake either moves or grows, depending on if the tile is food or not (food increments the score).
     *
     * @return a boolean representing whether or not the move was valid, so the game knows when to finish.
     */
    public boolean next() {

        try {
            Square old = getSquare(front);
            Square nxt = getNext();

            if (nxt.getType() == Square.SquareType.WALL || nxt.getType() == Square.SquareType.BODY)
                return false;

            old.setNext(nxt);


            if (nxt.getType() == Square.SquareType.NONE) {
                setEnd();
                nxt.setType(Square.SquareType.BODY);
            } else {
                nxt.setType(Square.SquareType.BODY);
                ++score;
                setFood();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e);
            return false;
        }

        return true;

    }

    /**
     * Sets the old end as a NONE tile without a next reference and creates a pointer to the new end tile.
     */
    public void setEnd() {
        Square old = end;
        old.setType(Square.SquareType.NONE);
        end = old.getNext();
        old.setNext(null);
    }

    /**
     * This method will update the maze to reflect any changes to the hexagonal
     * tiles it shows.  The method includes a time delay, which can be changed
     * with the setDelay method.
     */
    public void repaint() {
        try {
            Thread.sleep(this.timeDelay);
        } catch (Exception e) {
            System.err.println("Error while issuing time delay\n" + e.getMessage());
        }
        super.repaint();
    }

    /**
     * Uses the direction we are currently travelling ot return a string representation.
     *
     * @return a string rep of the direction.
     */
    public String getDirection() {
        if (dir == Direction.UP)
            return up;
        else if (dir == Direction.DOWN)
            return down;
        else if (dir == Direction.LEFT)
            return left;
        else
            return right;
    }


    private void bindKeys(JComponent p) {

        p.getInputMap().put(KeyStroke.getKeyStroke("UP"), up);
        p.getInputMap().put(KeyStroke.getKeyStroke("W"), up);
        p.getActionMap().put(up, new UpAction());

        p.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), down);
        p.getInputMap().put(KeyStroke.getKeyStroke("S"), down);
        p.getActionMap().put(down, new DownAction());

        p.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), left);
        p.getInputMap().put(KeyStroke.getKeyStroke("A"), left);
        p.getActionMap().put(left, new LeftAction());

        p.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), right);
        p.getInputMap().put(KeyStroke.getKeyStroke("D"), right);
        p.getActionMap().put(right, new RightAction());

        p.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), space);
        p.getActionMap().put(space, new SpaceAction());

        p.getInputMap().put(KeyStroke.getKeyStroke("P"), quitt);
        p.getActionMap().put(quitt, new QuitAction());

        p.getInputMap().put(KeyStroke.getKeyStroke("T"), speed);
        p.getActionMap().put(speed, new SpeedAction());

        p.getInputMap().put(KeyStroke.getKeyStroke("M"), music);
        p.getActionMap().put(music, new MusicAction());
    }

    private class UpAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            if (dir != Direction.DOWN)
                dir = Direction.UP;
        }
    }

    private class DownAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            if (dir != Direction.UP)
                dir = Direction.DOWN;
        }
    }

    private class LeftAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            if (dir != Direction.RIGHT || start)
                dir = Direction.LEFT;
        }
    }

    private class RightAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            if (dir != Direction.LEFT)
                dir = Direction.RIGHT;
        }
    }

    private class SpaceAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            reset();
        }
    }

    private class QuitAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            quit = true;
        }
    }

    private class SpeedAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            if (timeDelay == 50) {
                timeDelay = 100;
            } else if (timeDelay == 100) {
                timeDelay = 35;
            } else
                timeDelay = 50;
        }
    }

    private class MusicAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            if (playing) {
                clip.stop();
                playing = false;
            } else {
                clip.start();
                playing = true;
            }
        }
    }


    public static void main(String[] args) {
        new Snake(20);
    }
}

