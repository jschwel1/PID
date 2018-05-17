import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Controller extends JPanel implements KeyListener, Runnable{

    final static int REFRESH_RATE_PID = 10;
    final static int REFRESH_RATE_DRONE = 1;
    public static void main(String[] args){
        Controller c = new Controller();
        Thread ct = new Thread(c);
        ct.start();
        
    }
    
    Drone2D d;
    PID_Controller p;
    float throttle1 = 0f;
    float throttle2 = 0f;
    
    public Controller(){

        
        d = new Drone2D(REFRESH_RATE_DRONE);
        p = new PID_Controller(REFRESH_RATE_PID, 100f, 0f, 100f, 200f);
        
        JFrame f = new JFrame("PID Test");
        f.setVisible(true);
        f.setSize(500, 500);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container c = f.getContentPane();
        c.add(this);
        
        
        Thread dt = new Thread(d);
        Thread pt = new Thread(p);
        
        dt.start();
        pt.start();
    }
    
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        g2.clearRect(0, 0, 500, 500);
        try{
            d.paintDrone(g2);
            g2.setColor(Color.yellow);
            g2.fillRect(0, 450-(int)d.getProp2()*10, 50, (int)d.getProp2()*10);
            g2.fillRect(50, 450-(int)d.getProp1()*10, 100, (int)d.getProp1()*10);
            g2.drawLine(0, 300, 500, 300);
        } catch(Exception e){
            System.err.println("Could not print Drone");
            e.printStackTrace();
        }
       
    }
    
    
    @Override
    public void run() {
        while(true){
            // GOAL: get height to  300
            
            // Get Drone statistics
            float height = d.getHeight();
            
            // Feed data into PID
            p.setInput(height);
            float PID = p.getPID();
            
            // Update Drone values
            throttle1=-PID;
            throttle2=-PID;
//            System.out.println(PID + " -> " + throttle1);
            d.setProp1(throttle1);
            d.setProp2(throttle2);
            
            try{
                Thread.sleep(REFRESH_RATE_DRONE);
            } catch(Exception e){
                e.printStackTrace();
            }
            
            // Repaint
            this.repaint();
        }
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }
	
}
