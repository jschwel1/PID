import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Main extends JPanel implements KeyListener, ActionListener{
	
	Drone2D d2 = new Drone2D();
	boolean rightDown, leftDown, upDown;
	PID_Controller anglePID;
	public static void main(String args[]) {
		new Main();
	}
	
	public Main() {
		super();
		JFrame f = new JFrame("PID Test");
		f.setVisible(true);
		f.setSize(500, 500);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = f.getContentPane();
		c.add(this);
		this.repaint();
		f.addKeyListener(this);
		rightDown = false;
		leftDown = false;
		
		anglePID = new PID_Controller(.001f, 5f, 0f, 0f);
		
		Timer clock = new Timer(10, this);
		clock.start();
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.clearRect(0, 0, 500, 500);
		d2.paintDrone(g2);
		g2.setColor(Color.yellow);
		g2.fillRect(0, 450-(int)d2.getProp2()*10, 50, (int)d2.getProp2()*10);
        g2.fillRect(50, 450-(int)d2.getProp1()*10, 100, (int)d2.getProp1()*10);
	}
	
	public double[] PID(int start, int setpoint, double timestep, double time, double kp, double kd, 
						double ki) {
		double[] points = new double[(int) (time/timestep)] ;
		points[0] = start;
		double lastError = start - setpoint;
		double integral = 0;
		for (int t = 1; t < (int)(time/timestep); t++) {
			double error = setpoint-points[t-1];
			integral += (error * timestep);
			double derivative = (error-lastError)*timestep;
			lastError = error;
			double output = kp*error + kd*derivative + ki*integral;
			points[t] = points[t-1] + output;
			System.out.println(t*timestep + ", " + points[t]+", "+error+", "+integral+", "+derivative);
			
		}
		
		
		return points;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int code = e.getKeyCode();
		
		if (code == KeyEvent.VK_RIGHT) {
			rightDown = true;
		}
		if (code == KeyEvent.VK_LEFT) {
			leftDown = true;
		}
		if (code == KeyEvent.VK_UP) {
            upDown = true;
        }
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_RIGHT) {
			rightDown = false;
		}
		if (code == KeyEvent.VK_LEFT) {
			leftDown = false;
		}
		if (code == KeyEvent.VK_UP) {
            upDown = false;
        }
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
	    float delta = 0.1f;
        
        
        if (!rightDown && !leftDown) {
            anglePIDcontroller(anglePID.getPID(d2.getTheta(), 0), d2);
            d2.move();
            anglePIDcontroller(anglePID.getPID(d2.getTheta(), 0), d2);
        }
        else {
    	    if (upDown){
                d2.setProp1(d2.getProp1()+delta);
                d2.setProp2(d2.getProp2()+delta);
                System.out.println("p1: " + d2.getProp1() + ", p2: " + d2.getProp2());
            }
            else {
        	    if (rightDown) {
        			d2.setProp1(d2.getProp1()+delta);
        		}
        		else {
        			d2.setProp1(d2.getProp1()-delta);
        		}
        		if (leftDown) {
        			d2.setProp2(d2.getProp2()+delta);
        		}
        		else {
        			d2.setProp2(d2.getProp2()-delta);
        		}
            }
        }

		d2.move();
		this.repaint();
		
	}
	float throttle = 2;
	public void anglePIDcontroller(float PID, Drone2D d){
	    
	    if (upDown)
	        throttle = (float) Math.min(100, throttle+.1);
	    else
	        throttle = (float) Math.max(2, throttle-.1);
	    if (throttle-PID <0) throttle += (throttle-PID);
	    d.setProp1(throttle);
	    d.setProp2(throttle);
	    d.setProp1(throttle-PID);
	    d.setProp2(throttle+PID);
        System.out.println(PID + " " + throttle);
	    
	}
	
}
