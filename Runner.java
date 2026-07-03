import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
public class Runner extends Canvas implements KeyListener, Runnable{
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
    private final Font SCORE_FONT = new Font("Monospaced", Font.BOLD, 22);
    private final Font SYSTEM_FAILURE = new Font("Monospaced", Font.BOLD, 36);
    private final Font REBOOT = new Font("Monospaced", Font.PLAIN, 16);
    private Rectangle playerHitbox;
    private Rectangle obstacleHitbox;
    public Runner(){
        setPreferredSize(new Dimension(450,400));
        setIgnoreRepaint(true);
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
        playerHitbox = new Rectangle(50, playerY, 30, 30);
        obstacleHitbox = new Rectangle(obstacleX, GROUNDY, 20, 30);
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
            boolean shouldRender = false;
            long now = System.nanoTime();
            delta+= (now-lastTime)/ns;
            lastTime = now;
            while(delta >= 1){
                updateLogic();
                delta--;
                shouldRender = true;
            }
            if(shouldRender){
                render();
            }
            else{
                try{
                    Thread.sleep(1);
                }
                catch(InterruptedException e){
                    System.out.println("Error sleeping");
                }
            }
        }
    }
    
    protected void render( ){
        BufferStrategy bufferStrategy = this.getBufferStrategy(); 
        if(bufferStrategy == null){
            this.createBufferStrategy(2);
            return;
        }

        Graphics2D graphics2D = (Graphics2D) bufferStrategy.getDrawGraphics();
        graphics2D.setColor(BACKGROUND);
        graphics2D.fillRect(0, 0, getWidth(), getHeight()); 

        graphics2D.setColor(FLOOR);
        graphics2D.drawLine(0,(GROUNDY+30),getWidth(),(GROUNDY+30));

        graphics2D.setColor(PLAYER);
        graphics2D.fillRoundRect(50,playerY,30,30,10,10);

        Color colour = new Color(255,0,128);
        graphics2D.setColor(colour);
        graphics2D.fillRect(obstacleX,GROUNDY,20,30);

        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(SCORE_FONT);
        graphics2D.drawString("SCORE: " + score, 20, 40);
        if(gameOver){
            graphics2D.setColor(new Color(0, 0, 0, 150)); 
            graphics2D.fillRect(0, 0, getWidth(), getHeight());

            graphics2D.setColor(Color.WHITE);
            graphics2D.setFont(SYSTEM_FAILURE);
            graphics2D.drawString("GAME OVER", 50, 150);
            graphics2D.setFont(REBOOT);
            graphics2D.drawString("> Press SPACE to restart", 70, 190);
        }
        graphics2D.dispose();
        bufferStrategy.show();
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
                obstacleX = 460;
                score++;

                if(score % 5 == 0){
                    obstacleSpeed++;
                }
            }
            playerHitbox.setLocation(50,playerY);
            obstacleHitbox.setLocation(obstacleX,GROUNDY);
            if(playerHitbox.intersects(obstacleHitbox)){
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
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        game.requestFocusInWindow();
        game.startGame();
     }
}