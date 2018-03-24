import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Drone2D {
	
	private float prop1, prop2;
	private float theta, omega, alpha;
	private float disp, vel, acc;
	private int xDisp, width, height;
	private float gravity;
	private float Ts;
	private boolean pidActive;
	
	private PIDController pidc;
	
	public Drone2D() {
		prop1 = 0;
		prop2 = 0;
		theta = 0;
		omega = 0;
		alpha = 0;
		disp = 100;
		vel = 0;
		acc = 1;
		xDisp = 200;
		width = 50;
		height = 20;
		gravity = 10;
		Ts = 0.001f;
		pidActive = false;
		pidc = new PIDController(0.00f, 0.0f, 0.0f);
	}
	
	public void move() {
		// Update angular variables
		float yacc1 = (float) (Math.cos((double)theta)*prop1*Ts);
		float yacc2 = (float) (Math.cos((double)theta)*prop2*Ts);
		alpha = yacc1-yacc2;
		omega += alpha;
		theta += omega;
		if (theta > Math.PI) theta -= Math.PI;
		if (theta <= -Math.PI) theta += Math.PI;
		
		acc = -1 * (yacc1 + yacc2 - gravity*Ts);
		vel += acc;
		disp += vel;
		
		if (disp <= 50) disp = 50;
		if (disp >= 450) disp = 450;
		
		
//		System.out.println("prop1: " + prop1 + ", prop2: " + prop2 + "mult: " + Math.sin((double)theta) + " | yacc1: " + yacc1 + ", yacc2: " + yacc2 + " | alpha = " + alpha + ", omega = " + omega);
	}
	
	public void setProp1(float p) { prop1 = (p > 100?100:((p < 0?0:p))); }
	public float getProp1() { return prop1; }
	public void setProp2(float p) { prop2 = (p > 100?100:((p < 0?0:p))); }
	public float getProp2() { return prop2; }
	
	public void paintDrone(Graphics2D g2) {
		g2.setStroke(new BasicStroke(5));
		Point[] pts = getPoints();
//		g2.setColor(Color.black);
//		g2.drawLine(pts[0].x, pts[0].y, pts[1].x, pts[1].y);
//		g2.drawString("Angle: " + theta, 200, 200);
//		g2.setColor(Color.red);
//		g2.drawRect(pts[0].x, pts[0].y, 10, 10);
//		g2.setColor(Color.blue);
//		g2.drawRect(pts[1].x, pts[1].y, 10, 10);
		
		g2.setColor(Color.black);
		g2.drawLine(pts[0].x, pts[0].y, pts[1].x, pts[1].y);
		g2.drawLine(pts[1].x, pts[1].y, pts[2].x, pts[2].y);
		g2.drawLine(pts[2].x, pts[2].y, pts[3].x, pts[3].y);
		g2.drawLine(pts[3].x, pts[3].y, pts[0].x, pts[0].y);
		g2.setColor(Color.green);
		g2.drawRect((int)xDisp-5, (int)disp-5, 10, 10);
	}
	
	public void enablePID() {
		pidc.enable(0);
	}
	public void disablePID() {
		pidc.disable();
	}

	
	public Point[] getPoints() {
		Point[] pts = new Point[4];
		
		// Points relative to origin and 0 degrees
		int xtl = xDisp + (int)(-width*Math.cos((double)theta)+height*Math.sin((double)theta));
		int ytl = (int)disp + (int)(height*Math.cos((double)theta)-width*Math.sin((double)theta));
		int xtr = xDisp + (int)(width*Math.cos((double)theta)+height*Math.sin((double)theta));
		int ytr = (int)disp + (int)(height*Math.cos((double)theta)+width*Math.sin((double)theta));
//		int xbl = xDisp +(int)(-width*Math.cos((double)theta)-height*Math.sin((double)theta));
//		int ybl = (int)disp + (int)(-height*Math.cos((double)theta)-width*Math.sin((double)theta));
//		int xbr = xDisp + (int)(width*Math.cos((double)theta)-height*Math.sin((double)theta));
//		int ybr = (int)disp + (int)(-height*Math.cos((double)theta)+width*Math.sin((double)theta));
		
		int xbl = xtl, ybl = ytl, xbr = xtr, ybr = ytr; 
		pts[0] = new Point(xtl, ytl);
		pts[1] = new Point(xtr, ytr);
		pts[2] = new Point(xbr, ybr);
		pts[3] = new Point(xbl, ybl);
		
		return pts;		
	}
	
	
	
	public class PIDController extends Thread{
		private boolean active;
		private float kp, ki, kd;
		private float integral;
		private float setPoint;
		private float prevError;
		
		public PIDController() {
			active = false;
			kp = 0.01f;
			ki = 0.005f;
			kd = 0.005f;
			integral = 0;
			setPoint = 0;
		}
		
		public PIDController(float prop, float integ, float der) {
			this();
			kp = prop;
			ki = integ;
			kd = der;
		}
		
		public void enable(float set) {
			if (active) return;
			
			active = true;
			integral = 0;
			setPoint = set;
			prevError = theta-setPoint;
			try {
				this.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void disable() {
//			active = false;
//			try {
//				this.join();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		
		public void run() {
			while (active) {
				float error = theta-setPoint;
				
				integral += error*Ts;
				float derivative = (error - prevError)*Ts;
				prevError = error;
				float pid = kp*error + ki*integral + kd*derivative;
				
				// Use pid value to control props
				setProp1(prop1 - pid/2);
				setProp2(prop2 += pid/2);
				System.out.println("prop1: " + prop1 + ", prop2: " + prop2);
				
			}
		}
	}
	
}
