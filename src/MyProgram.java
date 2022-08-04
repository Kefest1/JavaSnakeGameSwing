import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class MyProgram extends JFrame implements ActionListener {
    public static final int SCREEN_WIDTH = 600;
    public static final int SCREEN_HEIGHT = 600;
    public static final Color MENU_COLOR = new Color(18, 18, 18);
    JLabel mainMenu;
    JLabel snakePicture;
    JLabel labelDifficultyLevel;
    JLabel labelEnterUsername;

    JPanel snakeGame;

    JButton buttonStartNewGame;
    JButton buttonExit;

    JComboBox<String> selectDifficultyLevel;
    JTextField usernameHolder;
    String[] gameDifficultyLevels = {"Easy", "Medium", "Hard", "Ultra"};


    MyProgram() {
        File file = new File("src\\top3records.txt");
        if (!file.isFile()) {
            System.out.println("isFile");
            try {
                file.createNewFile();
                try {
                    var writer = new FileWriter("src\\top3records.txt");
                    writer.write('0');
                    writer.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }



        mainMenu = new JLabel();
        this.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.setFocusable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupMainMenu();
        this.setVisible(true);
    }

    public void setupMainMenu() {
        snakePicture = setSnakePicture();
        mainMenu = new JLabel();
        mainMenu.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        mainMenu.setBackground(MENU_COLOR);
        mainMenu.setOpaque(true);
        mainMenu.setBorder(BorderFactory.createLineBorder(Color.GREEN, 10));
        mainMenu.add(setSnakePicture());

        buttonStartNewGame = new JButton("Start");
        buttonStartNewGame.setBounds(75, 395, 130, 50);
        buttonStartNewGame.setFocusable(false);
        buttonStartNewGame.addActionListener(
                e -> startNewGame()
        );

        buttonExit = new JButton("Exit");
        buttonExit.setBounds(75, 465, 130, 50);
        buttonExit.setFocusable(false);
        buttonExit.addActionListener(
                e -> this.dispose()
        );

        labelEnterUsername = new JLabel("Enter your username");
        labelEnterUsername.setFont(new Font("MV Boli", Font.PLAIN, 16));
        labelEnterUsername.setBounds(250, 365, 220, 60);
        labelEnterUsername.setForeground(Color.GREEN);

        labelDifficultyLevel = new JLabel("Select difficulty level");
        labelDifficultyLevel.setFont(new Font("MV Boli", Font.PLAIN, 16));
        labelDifficultyLevel.setBounds(250, 432, 220, 60);
        labelDifficultyLevel.setForeground(Color.GREEN);

        selectDifficultyLevel = new JComboBox<>(gameDifficultyLevels);
        selectDifficultyLevel.setBounds(250, 480, 100, 20);

        usernameHolder = new JTextField();
        usernameHolder.setBounds(250, 415, 100, 25);

        mainMenu.add(labelEnterUsername);
        mainMenu.add(labelDifficultyLevel);
        mainMenu.add(selectDifficultyLevel);
        mainMenu.add(usernameHolder);
        mainMenu.add(buttonStartNewGame);
        mainMenu.add(buttonExit);
        mainMenu.add(snakePicture);

        this.add(mainMenu);
    }

    public void startNewGame() {
        if (usernameHolder.getText().length() > 17) {
            JOptionPane.showMessageDialog(
                    null, "Username has to be shorter that 16 letters",
                    "Warning", JOptionPane.ERROR_MESSAGE
            );
            usernameHolder.setText("");
            return;
        }
        this.remove(mainMenu);
        this.revalidate();
        this.repaint();
        snakeGame = new SnakeGamePanel(this);

        this.add(snakeGame);
    }

    public JLabel setSnakePicture() {
        var snakePicture = new JLabel();
        snakePicture.setText("Welcome to the snake game!");
        snakePicture.setFont(new Font("MV Boli", Font.PLAIN, 40));
        snakePicture.setForeground(Color.ORANGE);
        snakePicture.setIcon(new ImageIcon("icons\\22285-snake-icon.png"));
        snakePicture.setBounds(15, 20, SCREEN_WIDTH, 330);
        snakePicture.setHorizontalTextPosition(JLabel.CENTER);
        snakePicture.setVerticalTextPosition(JLabel.TOP);
        return snakePicture;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
