import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int Screen_width = 600;
    static final int Screen_Height = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (Screen_width*Screen_Height)/UNIT_SIZE;
    static final int DELAY =75;

    final  int x[] = new int[GAME_UNITS];
    final  int y[] = new int[GAME_UNITS];

    int bodyParts = 6;
    int applesEaten , appleX , appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer ;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(Screen_width , Screen_Height));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        StartGame();
    }
    public void StartGame(){
        NewApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        if (running) {
            /*
            for (int i = 0; i < Screen_Height / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, Screen_Height);
                g.drawLine(0, i * UNIT_SIZE, Screen_width, i * UNIT_SIZE);
            }
             */
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    //g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
                    g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    //g.setColor(new Color(45, 180, 0));
                    g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    //g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
                    g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.LIGHT_GRAY);
            g.setFont(new Font("Ink Free", Font.BOLD,20));
            FontMetrics metrics = getFontMetrics(g.getFont());

            g.drawString("Score: "+applesEaten,(Screen_width-metrics.stringWidth("Score: "+applesEaten))/40 ,g.getFont().getSize());
        }
        else {
            gameOver(g);
        }
    }
    public void NewApple(){
        appleX =random.nextInt((int)(Screen_width/UNIT_SIZE))*UNIT_SIZE;
        appleY =random.nextInt((int)(Screen_Height/UNIT_SIZE))*UNIT_SIZE;

    }
    public void move(){
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i-1];
            y[i] =y[i-1];
        }
        switch (direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }
    public void checkApple(){
        if ((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
            applesEaten++;
            NewApple();
        }
    }
    public void cheackCollision(){
        // checks if head collides with body
        for (int i = bodyParts; i >0 ; i--) {
            if ((x[0] == x[i] ) && (y[0] == y[i])) {
                running = false;
            }
        }
        // check if head touches left border
        if(x[0]< 0){
            running = false;
        }
        //right border
        if(x[0]> Screen_width){
            running = false;
        }
        //top
        if(y[0]< 0){
            running = false;
        }
        //bottom
        if(y[0]> Screen_Height){
            running = false;
        }

        if(!running){
            timer.stop();
        }

    }
    public void gameOver(Graphics g){
        //score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD,40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());

        g.drawString("Score: "+applesEaten,(Screen_width-metrics1.stringWidth("Score: "+applesEaten))/2 ,g.getFont().getSize());


        //game over text
        g.setColor(Color.green);
        g.setFont(new Font("Ink Free", Font.BOLD,75));
        FontMetrics metrics = getFontMetrics(g.getFont());

        g.drawString("GAME OVER",(Screen_width-metrics.stringWidth("GAME OVER"))/2 ,Screen_Height/2);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running){
            move();
            checkApple();
            cheackCollision();
        }
        repaint();
    }
    public  class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if (direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
