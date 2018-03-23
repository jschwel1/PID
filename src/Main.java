import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.EventListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Main extends JPanel implements KeyListener, ActionListener{
	
	Drone2D d2 = new Drone2D();
	boolean rightDown, leftDown;
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
		
		
		Timer clock = new Timer(30, this);
		clock.start();
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.clearRect(0, 0, 500, 500);
		d2.paintDrone(g2);

/*		int HEIGHT = 500;
		int WIDTH = 500;
		int start = 0;
		int setpoint = 5;
		int time = 1;
		double kp = 0.1;
		double kd = 0.2;
		double ki = 0.9;
		double timestep = 0.00001;
		double[] chart = PID(start, setpoint, timestep, time, kp, kd, ki);

		int offset = HEIGHT/2;

		int scale = 30;
		
		g2.setColor(Color.red);
		g2.drawLine(0, HEIGHT-(setpoint*scale)-offset, WIDTH, HEIGHT-(setpoint*scale)-offset);
		g2.setColor(Color.green);
		g2.drawLine(0, offset, WIDTH, offset);
		g2.setColor(Color.black);
		int x1 = 0;
		int y1 = HEIGHT- (int)(chart[0]*scale);
		
		for (int i = 1; i < chart.length; i++) {
			int x2 = i*WIDTH/chart.length;
			int y2 = HEIGHT-((int)chart[i]*scale);
			
//			x2*=scale;
//			y2*=scale;
//			
			g2.drawLine(x1, y1-offset, x2, y2-offset);
			
			x1 = x2;
			y1 = y2;
			
			
		//	g2.drawLine((int)((i-1)*WIDTH/chart.length), (int)(HEIGHT-chart[i-1]), (int)(i*WIDTH/chart.length), (int)(HEIGHT-chart[i]));
			
		}
		*/
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
//			System.out.println(t*timestep + ", " + points[t]+", "+error+", "+integral+", "+derivative);
			
		}
		
		
		return points;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int code = e.getKeyCode();
		
		if (code == KeyEvent.VK_RIGHT) {
			rightDown = true;
			System.out.println("Pressed RIGHT");
		}
		if (code == KeyEvent.VK_LEFT) {
			leftDown = true;
			System.out.println("Pressed LEFT");
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_RIGHT) {
			rightDown = false;
			System.out.println("Released RIGHT");
		}
		if (code == KeyEvent.VK_LEFT) {
			leftDown = false;
			System.out.println("Released LEFT");
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if (rightDown) {
			d2.setProp1(d2.getProp1()+1);
		}
		else {
			d2.setProp1(d2.getProp1()-1);
		}
		if (leftDown) {
			d2.setProp2(d2.getProp2()+1);
		}
		else {
			d2.setProp2(d2.getProp2()-1);
		}
		d2.move();
		this.repaint();
		
	}
}
