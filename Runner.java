import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Runner extends JPanel implements KeyListener, ActionListener{
    private Timer timer;
    private boolean gameOver;
    private int score;
    private int playerY;
    private int playerVelocity;
    private final int GRAVITY = 1;
    private final int JUMPSTRENGTH = -15;
    private final int GROUNDY = 250;
    private int obstacleX;
    private int obstacleSpeed;
    public Runner(){
        setFocusable(true);
        addKeyListener(this);
        timer = new Timer(16,this);
        timer.start();
        gameOver = false;
        score = 0;
        playerY = 250;
        playerVelocity = 0;
        obstacleX = 400;
        obstacleSpeed = 6;
    }

    @Override
    protected void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        graphics.setColor(Color.DARK_GRAY);
        graphics.fillRect(0,0,getWidth(),getHeight());
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.fillRect(0,(GROUNDY+30),getWidth(),50);
        graphics.setColor(Color.GREEN);
        graphics.fillRect(50,playerY,30,30);
        graphics.setColor(Color.RED);
        graphics.fillRect(obstacleX,GROUNDY,20,30);
        graphics.setColor(Color.WHITE);
        Font font = new Font("ARIAL",Font.BOLD,20);
        graphics.setFont(font);
        graphics.drawString(("Score: " + score),20,30);
        if(gameOver){
            graphics.setColor(Color.RED);
            font = new Font("ARIAL",Font.BOLD,30);
            graphics.setFont(font);
            graphics.drawString("GAME OVER", 100,150);
            graphics.setColor(Color.WHITE);
            font = new Font("ARIAL",Font.PLAIN,16);
            graphics.setFont(font);
            graphics.drawString("Press SPACE to Restart",110,180);
        }
     }
     @Override
     public void  actionPerformed(ActionEvent event){
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
        repaint();
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
     }
}