import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Runner extends JPanel implements KeyListener, Runnable{
    private Thread mainThread;
    private boolean gameRunning;
    private boolean gameOver;
    private int score;
    private int playerY;
    private int playerVelocity;
    private final int GRAVITY = 1;
    private final int JUMPSTRENGTH = -15;
    private final int GROUNDY = 250;
    private int obstacleX;
    private int obstacleSpeed;
    private final Color BACKGROUND = new Color(15,15,20);
    private final Color FLOOR = new Color(0,255,255,100);
    private final Color PLAYER = new Color(0,255,255);
    public Runner(){
        setFocusable(true);
        addKeyListener(this);
        gameOver = false;
        score = 0;
        playerY = 250;
        playerVelocity = 0;
        obstacleX = 400;
        obstacleSpeed = 6;
        gameRunning = false;
        setBackground(BACKGROUND);
    }
    public void startGame(){
        gameRunning = true;
        mainThread = new Thread(this);
        mainThread.start();
    }
    @Override
    public void run(){
        long lastTime = System.nanoTime();
        double Fps = 60;
        double ns = 1000000000/Fps;
        double delta = 0;
        while(gameRunning){
            long now = System.nanoTime();
            delta+= (now-lastTime)/ns;
            lastTime = now;
            while(delta >= 1){
                updateLogic();
                delta--;
            }
            repaint();
        }
    }
    @Override
    protected void paintComponent(Graphics graphics){
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setColor(FLOOR);
        graphics2D.drawLine(0,(GROUNDY+30),getWidth(),(GROUNDY+30));
        graphics2D.setColor(PLAYER);
        graphics2D.fillRoundRect(50,playerY,30,30,10,10);
        Color colour = new Color(255,0,128);
        graphics2D.setColor(colour);
        graphics2D.fillRect(obstacleX,GROUNDY,20,30);
        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(new Font("Monospaced", Font.BOLD, 22));
        graphics2D.drawString("SCORE: " + score, 20, 40);
        if(gameOver){
            graphics2D.setColor(new Color(0, 0, 0, 150)); // Dark overlay
            graphics2D.fillRect(0, 0, getWidth(), getHeight());

            graphics2D.setColor(Color.WHITE);
            graphics2D.setFont(new Font("Monospaced", Font.BOLD, 36));
            graphics2D.drawString("SYSTEM FAILURE", 50, 150);
            
            graphics2D.setFont(new Font("Monospaced", Font.PLAIN, 16));
            graphics2D.drawString("> Press SPACE to reboot_", 70, 190);
        }
        Toolkit.getDefaultToolkit().sync();
     }
     public void  updateLogic(){
        if(!gameOver){
            playerVelocity+=GRAVITY;
            playerY+=playerVelocity;
            if(playerY >=  GROUNDY){
                playerY = GROUNDY;
                playerVelocity = 0;
            }
            obstacleX -= obstacleSpeed;
            if(obstacleX < -20){
                obstacleX = 400;
                score++;

                if(score % 5 == 0){
                    obstacleSpeed++;
                }
            }
            Rectangle player = new Rectangle(50,playerY,30,30);
            Rectangle obstacle = new Rectangle(obstacleX,GROUNDY,20,30);
            if(player.intersects(obstacle)){
                gameOver = true;
            }
        }
     }
     @Override
     public void keyPressed(KeyEvent event){
        if(event.getKeyCode() == KeyEvent.VK_SPACE){
            if(gameOver){
                playerY = GROUNDY;
                score = 0;
                playerVelocity = 0;
                obstacleX = 400;
                obstacleSpeed = 6;
                gameOver = false;
            }
            else if(playerY >= GROUNDY){
                playerVelocity= JUMPSTRENGTH;
            }
        }
     }
     @Override
     public void keyReleased(KeyEvent event){
        //nothing
     }

     @Override
     public void keyTyped(KeyEvent event){
        //nothing
     }

     public static void main(String[] args){
        JFrame frame = new JFrame("Endless Runner");
        Runner game = new Runner();
        frame.add(game);
        frame.setSize(450,300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        game.startGame();
     }
}