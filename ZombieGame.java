import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Container;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;
import java.util.ArrayList;
import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.JOptionPane;
import sun.audio.AudioStream;
import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import java.io.*;
/**
 * Write a description of class ZombieGame here.
 *
 * @author (REDACTED)
 * @version (a version number or a date)
 */
public class ZombieGame implements MouseListener, KeyListener, ActionListener

{

    // number of rows and columns in grid
    public final int ROWS=30;
    public final int COLS=30;
    //keycodes for all of the arrow keys
    private final int SHOOTLEFT=37;
    private final int SHOOTRIGHT=39;
    private final int SHOOTUP=38;
    private  final int SHOOTDOWN=40;
    private final int MOVEUP=87;
    private final int MOVEDOWN=83;
    private final int MOVELEFT=65;
    private final int MOVERIGHT=68;
    private final int STOP=-1; //impossible value, only for beginning

    GridLayout grid;         //layout manager set to Grid to match 2D array of JButtons  
    ZGMusic music = new ZGMusic();
    //images used as icons on buttons
    ImageIcon red = new ImageIcon("tile1.png");
    //ImageIcon green = new ImageIcon("green.jpg");
    ImageIcon charlie = new ImageIcon("bourne1.jpg");
    ImageIcon bullet = new ImageIcon("bullet1.png");
    ImageIcon zombie = new ImageIcon("zombie1.png");

    private int timeCounter=0;
    private int score = 0;
    private int charlieRow = ROWS/2;
    private int charlieCol = COLS/2;
    private int charlieDir = STOP;
    private int bulletDir = STOP;
    private int numZombies;
    //Timer charlieTimer=new Timer(180, this);
    Timer bulletTimer= new Timer(30, this);
    Timer zombieSpawnTimer = new Timer(750, this);
    Timer bannerTimer = new Timer(1000, this);
    Timer zombieMoveTimer = new Timer (350, this);
    //frame and window objects
    JFrame frame = new JFrame();
    JButton[][] button = new JButton[ROWS][COLS]; 
    JPanel panel = new JPanel();

    //Status bar
    JTextArea[] topRow = new JTextArea[COLS];
    //list of bullets and list of zombies
    private ArrayList<ZGBullet> bullets = new ArrayList<ZGBullet>();
    private ArrayList<ZGZombie> zombies = new ArrayList<ZGZombie>();
    //Constructor creates Grid, frame, 2D array of JButtons  
    ZombieGame()
    {
        grid = new GridLayout( ROWS+1,COLS );//rows +1 to make room for banner at top
        frame.setLayout( grid );

        //for loop to create top banner
        for(int first=0; first < COLS; first++){
            topRow[first] = new JTextArea("");
            topRow[first].setFont(new Font("Arial", Font.BOLD, 25));
            topRow[first].setBackground(Color.GREEN);
            topRow[first].setEditable(false);
            frame.add(topRow[first]);
        }
        //Setting the text
        topRow[8].setText("AP");
        topRow[9].setText("C");
        topRow[10].setText("O");
        topRow[11].setText("M");
        topRow[12].setText("P");
        topRow[13].setText("");
        topRow[14].setText("S");
        topRow[15].setText("C");
        topRow[16].setText("I");
        topRow[17].setText("");
        topRow[18].setText("RDCT");
        //Create JButtons initialize to red, activate listeners, 
        for (int r=0; r < button.length; r++)
        {
            for(int c=0; c < button[r].length; c++)
            {
                button[r][c] = new JButton(red);
                button[r][c].addMouseListener(this);
                button[r][c].addKeyListener(this);
                button[r][c].addActionListener(this);
                button[r][c].setBorderPainted(false);
                frame.add(button[r][c]);
            }
        }

        //initialiaze frame and window
        final int FRAME_WIDTH = 600;
        final int FRAME_HEIGHT = 800;
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setTitle("REDACTED'S Extremely Rudimentary Zombie Game (2017 Game of the Year)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        button[charlieRow][charlieCol].setIcon(charlie);

        frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH); //this makes it fullscreen

        JOptionPane.showMessageDialog(frame, "Welcome to REDACTED'S Extremely Rudimentary Zombie Game! (GOTY 2017)", "Welcome",JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(frame, "Your objective is to eliminate all the zombies while not being eliminated yourself.", "Objective",JOptionPane.INFORMATION_MESSAGE);
        try{
            numZombies = Integer.parseInt(JOptionPane.showInputDialog(frame, "How many zombies do you want to fight? \n Enter a negative value for endless mode. \n Enter 0 if you want to feel like a winner. \n Enter something invalid if you want to lose.", "Zombie selection",JOptionPane.QUESTION_MESSAGE));
        }
        catch(IllegalArgumentException e)
        {
            numZombies = 99999;
        }
        JOptionPane.showMessageDialog(frame, "The controls are as follows:\n WASD to move.\n Arrow keys to shoot.\n Due to how Java works, you'll need to click the window once the game starts.", "Controls", JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(frame, "Game will now start.", "Get ready!", JOptionPane.WARNING_MESSAGE);

        //charlieTimer.start();
        bulletTimer.start();
        bannerTimer.start();
        zombieSpawnTimer.start();
        zombieMoveTimer.start();
        music.playSound3();
    }

    //CHARACTER MOVEMENT
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource().equals(bannerTimer))
        {
            timeCounter++;
        }
        //update banner every time anything happens
        topRow[0].setText("Time");
        topRow[1].setText(""+timeCounter);
        topRow[2].setText("");
        topRow[3].setText("Rem:");
        topRow[4].setText(""+numZombies);
        topRow[5].setText("");
        topRow[6].setText("Alive:");
        topRow[7].setText("" + zombies.size());
        /*
        if(e.getSource().equals(charlieTimer))
        {
        moveCharlie();
        }
         */
        if(e.getSource().equals(bulletTimer))
        {
            killZombies();
            removeBullets();
            moveBullets();
            checkWin();
        }
        if(e.getSource().equals(zombieSpawnTimer))
        {
            spawnZombie();
        }
        if(e.getSource().equals(zombieMoveTimer))
        {
            moveZombies();
            checkLose();
        }
    }

    public void moveCharlie()
    {

        if(charlieDir == MOVEUP){
            if(charlieRow >0){ //preventing OOB errors from top
                button[charlieRow][charlieCol].setIcon(red);
                charlieRow--; //This is because up is getting closer to 0 in an array
                button[charlieRow][charlieCol].setIcon(charlie);
            }
        }
        if(charlieDir == MOVEDOWN){
            if(charlieRow < ROWS-1){ //preventing OOB errors from bottom
                button[charlieRow][charlieCol].setIcon(red);
                charlieRow++; //This is because down is getting closer to the maximum in an array
                button[charlieRow][charlieCol].setIcon(charlie);
            }
        }
        if(charlieDir == MOVELEFT){
            if(charlieCol >0 ){ //preventing OOB errors from bottom
                button[charlieRow][charlieCol].setIcon(red);
                charlieCol--; //This is because left is getting closer to 0 in an array
                button[charlieRow][charlieCol].setIcon(charlie);
            }
        }
        if(charlieDir == MOVERIGHT){
            if(charlieCol < COLS-1){ //preventing OOB errors from right
                button[charlieRow][charlieCol].setIcon(red);
                charlieCol++; //This is because right is getting closer to the maximum in an array
                button[charlieRow][charlieCol].setIcon(charlie);
            }
        } 

    }

    public void keyPressed(KeyEvent e)
    {
        //Movement input
        if(e.getKeyCode() == MOVEUP)
        {
            charlieDir=MOVEUP;
            moveCharlie();
        }
        if(e.getKeyCode() == MOVEDOWN)
        {
            charlieDir=MOVEDOWN;
            moveCharlie();
        }
        if(e.getKeyCode() == MOVELEFT)
        {
            charlieDir=MOVELEFT;
            moveCharlie();
        }
        if(e.getKeyCode() == MOVERIGHT)
        {
            charlieDir=MOVERIGHT;
            moveCharlie();
        }
        //Shooting input
        if(e.getKeyCode() == SHOOTUP)
        {
            bulletDir=SHOOTUP;
            bullets.add(new ZGBullet(charlieRow, charlieCol,  bulletDir));
        }
        if(e.getKeyCode() == SHOOTDOWN)
        {
            bulletDir=SHOOTDOWN;
            bullets.add(new ZGBullet(charlieRow, charlieCol,  bulletDir));
        }
        if(e.getKeyCode() == SHOOTLEFT)
        {
            bulletDir=SHOOTLEFT;
            bullets.add(new ZGBullet(charlieRow, charlieCol,  bulletDir));
        }
        if(e.getKeyCode() == SHOOTRIGHT)
        {
            bulletDir=SHOOTRIGHT;
            bullets.add(new ZGBullet(charlieRow, charlieCol,  bulletDir));
        }
    }

    //BULLET STUFF

    public void removeBullets()
    {
        if(bullets.size() != 0){ //Removing bullets on the edge of the screen as well as checking to kill zombies
            for (int i=bullets.size()-1; i >= 0; i--){
                /*if(zombies.size() != 0)
                {
                for(int j=zombies.size()-1; j >=0; j--)
                {
                if( bullets.get(i).getBulletRow() == zombies.get(j).getZombieRow() && bullets.get(i).getBulletCol() == zombies.get(j).getZombieCol())
                {
                button[zombies.get(j).getZombieRow()][zombies.get(j).getZombieCol()].setIcon(red);
                zombies.remove(j);
                bullets.get(i).wipeOutBullet();
                }
                }
                } */
                if(bullets.get(i).getBulletCol()==0 ) 
                {
                    button[bullets.get(i).getBulletRow()][bullets.get(i).getBulletCol()].setIcon(red);
                    bullets.remove(i);
                }
                else if(bullets.get(i).getBulletCol()==COLS-1)
                {
                    button[bullets.get(i).getBulletRow()][bullets.get(i).getBulletCol()].setIcon(red);
                    bullets.remove(i);
                }
                else if(bullets.get(i).getBulletRow()==0)
                {
                    button[bullets.get(i).getBulletRow()][bullets.get(i).getBulletCol()].setIcon(red);
                    bullets.remove(i);
                }
                else if(bullets.get(i).getBulletRow()== ROWS-1)
                {
                    button[bullets.get(i).getBulletRow()][bullets.get(i).getBulletCol()].setIcon(red);
                    bullets.remove(i);
                }
            }
        }
    }

    public void killZombies()
    {
        if(bullets.size() != 0)
        { //Removing bullets on the edge of the screen as well as checking to kill zombies
            for (int i=bullets.size()-1; i >= 0; i--){
                if(zombies.size() != 0)
                {
                    for(int j=zombies.size()-1; j >=0; j--)
                    {
                        if( bullets.get(i).getBulletRow() == zombies.get(j).getZombieRow() && bullets.get(i).getBulletCol() == zombies.get(j).getZombieCol())
                        {
                            button[zombies.get(j).getZombieRow()][zombies.get(j).getZombieCol()].setIcon(red);
                            zombies.remove(j);
                            bullets.get(i).wipeOutBullet();
                        }
                    }
                }
            }
        }
    }

    public void moveBullets()
    {
        for(ZGBullet b: bullets)
        {
            if (b.getBulletDir() == SHOOTLEFT)
            {
                if(b.getBulletCol() >0)
                { 
                    button[b.getBulletRow()][b.getBulletCol()].setIcon(red);
                    b.moveLeft(); 
                    button[b.getBulletRow()][b.getBulletCol()].setIcon(bullet);
                    button[charlieRow][charlieCol].setIcon(charlie);
                }
                else
                {
                    button[b.getBulletRow()][b.getBulletCol()].setIcon(red);
                }
            }
            if (b.getBulletDir() == SHOOTRIGHT)
            {
                if (b.getBulletCol() < COLS-1)
                {
                    button[b.getBulletRow()][b.getBulletCol()].setIcon(red);
                    b.moveRight(); 
                    button[b.getBulletRow()][b.getBulletCol()].setIcon(bullet);
                    button[charlieRow][charlieCol].setIcon(charlie);
                }
                else
                {
                    button[b.getBulletRow()][b.getBulletCol()].setIcon(red);
                }
            }
            if (b.getBulletDir() == SHOOTUP)
            {
                if(b.getBulletRow() >0)
                { //preventing OOB errors from top
                    button[b.getBulletRow()][b.getBulletCol()].setIcon(red);
                    b.moveUp();
                    button[b.getBulletRow()][b.getBulletCol()].setIcon(bullet);
                    button[charlieRow][charlieCol].setIcon(charlie);
                }
                else
                {
                    button[b.getBulletRow()][b.getBulletCol()].setIcon(red);
                }
            }
            if (b.getBulletDir() == SHOOTDOWN)
            {
                if(b.getBulletRow() < ROWS-1)
                { //preventing OOB errors from top
                    button[b.getBulletRow()][b.getBulletCol()].setIcon(red);
                    b.moveDown(); 
                    button[b.getBulletRow()][b.getBulletCol()].setIcon(bullet);
                    button[charlieRow][charlieCol].setIcon(charlie);
                }
                else
                {
                    button[b.getBulletRow()][b.getBulletCol()].setIcon(red);
                }
            }
        }
    }

    //ZOMBIE STUFF
    public void spawnZombie()
    {
        if(numZombies != 0)
        {
            int rNum1=(int) (Math.random() * 4);//Determining which side they'll spawn on
            if(rNum1==0)
            { //Left side
                int rNum2=(int) (Math.random() * ROWS);
                zombies.add(new ZGZombie(rNum2, 0));
                button[rNum2][0].setIcon(zombie);
                numZombies--;
            }
            else if(rNum1==1)
            { //Right side
                int rNum2=(int) (Math.random() * ROWS);
                zombies.add(new ZGZombie(rNum2, COLS-1));
                button[rNum2][COLS-1].setIcon(zombie);
                numZombies--;
            }
            else if(rNum1==2)
            { //Top side
                int rNum2=(int) (Math.random() * COLS);
                zombies.add(new ZGZombie(0, rNum2));
                button[0][rNum2].setIcon(zombie);
                numZombies--;
            }
            else if(rNum1==3)
            { //Top side
                int rNum2=(int) (Math.random() * COLS);
                zombies.add(new ZGZombie(ROWS-1, rNum2));
                button[ROWS-1][rNum2].setIcon(zombie);
                numZombies--;
            }
        }
    }

    public void moveZombies()
    {
        for(ZGZombie z: zombies)
        {
            int rNum = (int) (Math.random() * 2); //Tossing a coin to see if it actually moves to make zombies less predictable
            if(rNum == 0)
            {
                if(z.getZombieCol() > charlieCol){ 
                    button[z.getZombieRow()][z.getZombieCol()].setIcon(red);
                    z.moveLeft(); 
                    button[z.getZombieRow()][z.getZombieCol()].setIcon(zombie);
                }

                else if (z.getZombieCol() < charlieCol)
                {
                    button[z.getZombieRow()][z.getZombieCol()].setIcon(red);
                    z.moveRight();
                    button[z.getZombieRow()][z.getZombieCol()].setIcon(zombie);
                }
                if (z.getZombieRow() > charlieRow)
                {
                    button[z.getZombieRow()][z.getZombieCol()].setIcon(red);
                    z.moveUp();
                    button[z.getZombieRow()][z.getZombieCol()].setIcon(zombie);
                }
                else if (z.getZombieRow() < charlieRow)
                {
                    button[z.getZombieRow()][z.getZombieCol()].setIcon(red);
                    z.moveDown(); 
                    button[z.getZombieRow()][z.getZombieCol()].setIcon(zombie);
                }
            }
        }
    }

    public void checkLose()
    {
        for(ZGZombie z: zombies)
        {
            if( z.getZombieRow() == charlieRow && z.getZombieCol() == charlieCol)
            {
                music.stopSound3();

                music.playSound1();
                bannerTimer.stop();
                zombieSpawnTimer.stop();
                zombieMoveTimer.stop();
                bulletTimer.stop();
                JOptionPane.showMessageDialog(frame, "You died with " + (numZombies+zombies.size()) + " zombies left. You lasted " +timeCounter+ " seconds", "RIP", JOptionPane.ERROR_MESSAGE);

                JOptionPane.showMessageDialog(frame, "Music source: https://youtu.be/mNqdin5NbzU", "This is so I don't get sued", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            }
        }
    }

    public void checkWin()
    {
        if( zombies.size() == 0 && numZombies == 0)
        {
            music.stopSound3();
            music.playSound2();

            bannerTimer.stop();
            JOptionPane.showMessageDialog(frame, "You survived! You wiped out all the zombies in " +timeCounter+ " seconds", "Good Job", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(frame, "Music source: https://youtu.be/mNqdin5NbzU", "This is so I don't get sued", JOptionPane.INFORMATION_MESSAGE);
            //charlieTimer.stop();
            bulletTimer.stop();
            zombieSpawnTimer.stop();
            zombieMoveTimer.stop();
            frame.dispose();
        }
    }
    //UNUSED METHODS

    public void keyReleased(KeyEvent e){
    }

    public void keyTyped(KeyEvent e){
    }

    public void mouseReleased(MouseEvent e){
    }

    public void mouseEntered(MouseEvent e){
    }

    public void mouseExited(MouseEvent e){
    }

    public void mouseClicked(MouseEvent e)
    {
    }

    public void mousePressed(MouseEvent e)
    {
        /*
        //System.out.println("MOUSE PRESSED");
        for(int r=0; r< button.length; r++)
        {
        for(int c=0; c< button[r].length; c++)
        {
        if(e.getSource().equals(button[r][c]))
        {
        if(button[r][c].getIcon() == red)
        {
        button[r][c].setIcon(green);
        }
        else
        {
        button[r][c].setIcon(red);
        }
        }
        }
        }
         */
    }
}