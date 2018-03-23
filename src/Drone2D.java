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
	}
	
	public void move() {
		// Update angular variables
		float yacc1 = (float) (Math.cos((double)theta)*prop1*Ts);
		float yacc2 = (float) (Math.cos((double)theta)*prop2*Ts);
		alpha = yacc1-yacc2;
		omega += alpha;
		theta += omega;
		if (theta > 2*Math.PI) theta -= 2*Math.PI;
		if (theta < -2*Math.PI) theta += 2*Math.PI;
		
		acc = -1 * (yacc1 + yacc2 - gravity*Ts);
		vel += acc;
		disp += vel;
		
		if (disp <= 50) disp = 50;
		if (disp >= 450) disp = 450;
		
		
		System.out.println("prop1: " + prop1 + ", prop2: " + prop2 + "mult: " + Math.sin((double)theta) + " | yacc1: " + yacc1 + ", yacc2: " + yacc2 + " | alpha = " + alpha + ", omega = " + omega);
	}
	
	public void setProp1(float p) { prop1 = (p > 100?100:((p < 0?0:p))); }
	public float getProp1() { return prop1; }
	public void setProp2(float p) { prop2 = (p > 100?100:((p < 0?0:p))); }
	public float getProp2() { return prop2; }
	
	public void paintDrone(Graphics2D g2) {
		g2.setStroke(new BasicStroke(5));
		Point[] pts = getPoints();
		g2.setColor(Color.black);
		g2.drawLine(pts[0].x, pts[0].y, pts[1].x, pts[1].y);
		g2.drawString("Angle: " + theta, 200, 200);
		g2.setColor(Color.red);
		g2.drawRect(pts[0].x, pts[0].y, 10, 10);
		g2.setColor(Color.blue);
		g2.drawRect(pts[1].x, pts[1].y, 10, 10);
		g2.setColor(Color.green);
		g2.drawRect((int)xDisp, (int)disp, 10, 10);
	}
	

	
	public Point[] getPoints() {
		Point[] pts = new Point[2];
		int rcy = (int) (disp + width*Math.sin((double)theta));
		int rcx = (int) (xDisp + width*Math.cos((double)theta));
		int lcy = (int) (disp - width*Math.sin((double)theta));
		int lcx = (int) (xDisp - width*Math.cos((double)theta));
		
		
		pts[0] = new Point(rcx,rcy);
		pts[1] = new Point(lcx,lcy);
		
		return pts;		
	}
}
