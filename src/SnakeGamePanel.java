import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Random;

public class SnakeGamePanel extends JPanel implements ActionListener, KeyListener {
    MyProgram program;

    private static final int SCREEN_HEIGHT = MyProgram.SCREEN_HEIGHT;
    private static final int SCREEN_WIDTH = MyProgram.SCREEN_WIDTH;
    private static final int UNIT_SIZE = 25;
    private static final int GAME_UNITS = (MyProgram.SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    private static final int BASE_SPEED = 25;
    private static int delay;

    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten = 0;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean isRunning = false;
    Timer timer;
    Random random = new Random();
    JLabel score;
    JLabel gameOver;
    JButton buttonTryAgain;
    JButton buttonExit;
    String username;

    SnakeGamePanel(MyProgram program) {
        username = program.usernameHolder.getName();
        if (username == null) username = "Nameless";

        program.addKeyListener(this);
        this.setFocusable(true);
        this.program = program;

        program.setSize(615, 640);
        this.setOpaque(true);
        this.setBackground(Color.BLACK);
        this.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);

        delay = (4 - program.selectDifficultyLevel.getSelectedIndex()) * BASE_SPEED;

        score = new JLabel("Your score is: " + applesEaten);
        score.setForeground(Color.RED);
        score.setSize(600, 600);
        score.setHorizontalAlignment(JLabel.CENTER);
        score.setVerticalAlignment(JLabel.TOP);
        score.setFont(new Font("MV Boli", Font.BOLD, 24));

        this.add(score);
        this.startGame();
    }

    public void startGame() {
        newApple();
        isRunning = true;
        timer = new Timer(delay, this);
        timer.start();
        this.requestFocusInWindow();
    }

    public void newApple() {
        appleX = random.nextInt((SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U' -> y[0] = y[0] - UNIT_SIZE;
            case 'D' -> y[0] = y[0] + UNIT_SIZE;
            case 'L' -> x[0] = x[0] - UNIT_SIZE;
            case 'R' -> x[0] = x[0] + UNIT_SIZE;
        }

    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            newApple();
            score.setText("Your score is: " + applesEaten);
        }
    }

    public void checkCollisions() {
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                isRunning = false;
                break;
            }
        }
        if (x[0] < 0 || y[0] < 0 || x[0] > SCREEN_WIDTH - UNIT_SIZE || y[0] > SCREEN_HEIGHT - UNIT_SIZE)
            isRunning = false;

        if (!isRunning) {
            timer.stop();
            gameOver();
        }
    }

    public void gameOver() {

        gameOver = new JLabel();
        gameOver.setText("<html>Gave Over " + username + "!<br/> Your score is " + applesEaten + "!<html>");
        gameOver.setFont(new Font("MV Boli", Font.BOLD, 32));
        gameOver.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        gameOver.setHorizontalAlignment(JLabel.CENTER);

        buttonExit = new JButton("Exit");
        buttonExit.setBounds(400, 480, 130, 50);
        buttonExit.setFocusable(false);
        buttonExit.addActionListener(
                e -> program.dispose()
        );

        buttonTryAgain = new JButton("Try again");
        buttonTryAgain.setBounds(400, 400, 130, 50);
        buttonTryAgain.setFocusable(false);
        buttonTryAgain.addActionListener(
                e -> {
                    program.startNewGame();
                    this.setVisible(false);
                }
        );

        try {
            var reader = new BufferedReader(new FileReader("src\\top3records.txt"));
            int x = Integer.parseInt(reader.readLine());
            System.out.println(x);
            if (x < applesEaten) {
                gameOver.setText("<html>Gave Over " + username + "!<br/> Your score is " + applesEaten + "!<html><br/>This is a new record!");
                var writer = new FileWriter("src\\top3records.txt");

                writer.write(Integer.toString(applesEaten));
                writer.close();
                reader.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        this.remove(score);
        this.add(gameOver);
        this.add(buttonTryAgain);
        this.add(buttonExit);
    }

    public void draw(Graphics g) {
        for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
        }
        g.setColor(Color.RED);
        g.fillRect(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

        for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {
                g.setColor(Color.GREEN);
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
            else {
                g.setColor(new Color(45, 180, 0));
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (direction != 'R') {
                    direction = 'L';
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') {
                    direction = 'R';
                }
                break;
            case KeyEvent.VK_UP:
                if (direction != 'D') {
                    direction = 'U';
                }
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U') {
                    direction = 'D';
                }
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isRunning) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
}
