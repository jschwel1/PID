import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Drone2D {
	
    private static final int MAX_VEL = 3;
    private static final float MAX_ACC = 1f;
    
    
	private float prop1, prop2;
	private float theta, omega, alpha;
	private float disp, vel, acc;
	private int xDisp, width, height;
	private float gravity;
	private float Ts;
	private boolean pidActive;
	
	
	public Drone2D(float Ts) {
		prop1 = 0;
		prop2 = 0;
		theta = 0f;
		omega = 0;
		alpha = 0;
		disp = 100;
		vel = 0;
		acc = 1;
		xDisp = 200;
		width = 50;
		height = 20;
		gravity = 20;
		this.Ts = Ts;
		pidActive = false;
	}
	
	public void move() {
		// Update angular variables
		float yacc1 = (float) Math.abs(Math.sin(Math.PI/2 + (double)theta)*prop1*Ts);
		float yacc2 = (float) Math.abs(Math.sin(Math.PI/2 + (double)theta)*prop2*Ts);
		alpha = yacc1-yacc2;
		// 1% drag resistance
		alpha = (float) (0.99*alpha);
		omega += alpha;
		theta += omega;
//		if (theta > Math.PI) theta -= 2*Math.PI;
//		if (theta < -Math.PI) theta += 2*Math.PI;
		
		acc = (yacc1 + yacc2 - gravity*Ts);
		if (acc > MAX_ACC) acc = MAX_ACC;
		if (acc < -MAX_ACC) acc = -MAX_ACC;
		vel += acc;
        if (vel > MAX_VEL) vel = MAX_VEL;
        if (vel < -MAX_VEL) vel = -MAX_VEL;
		disp += vel;
		
		if (disp <= 50) disp = 50;
		if (disp >= 450) disp = 450;
		
		
//		System.out.println("prop1: " + prop1 + ", prop2: " + prop2 + "mult: " + Math.sin((double)theta) + " | yacc1: " + yacc1 + ", yacc2: " + yacc2 + " | alpha = " + alpha + ", omega = " + omega);
	}
	
	public void setProp1(float p) { prop1 = (p > 100?100:((p < 0?0:p))); }
	public float getProp1() { return prop1; }
	public void setProp2(float p) { prop2 = (p > 100?100:((p < 0?0:p))); }
	public float getProp2() { return prop2; }
	public float getTheta() { return theta; }
	public float getHeight() { return disp; }
	public float getAcc() { return acc; }
	public float getVel() { return vel; }
	
	
	public void paintDrone(Graphics2D g2) {
		g2.setStroke(new BasicStroke(5));
		Point[] pts = getPoints();
		g2.drawString("Angle: " + theta, 200, 200);
		
		g2.setColor(Color.blue);
		g2.drawLine(pts[0].x, 500-pts[0].y, pts[1].x, 500-pts[1].y);
		g2.setColor(Color.black);
		g2.drawLine(pts[1].x, 500-pts[1].y, pts[2].x, 500-pts[2].y);
		g2.drawLine(pts[2].x, 500-pts[2].y, pts[3].x, 500-pts[3].y);
		g2.drawLine(pts[3].x, 500-pts[3].y, pts[0].x, 500-pts[0].y);
		g2.setColor(Color.green);
		g2.drawRect((int)xDisp-5, 500-(int)disp-5, 10, 10);
	}
		
	public Point[] getPoints() {
		Point[] pts = new Point[4];
		
		// Points relative to origin and 0 degrees
		int xtl = (int) (xDisp + getNormalX(theta, -width, height));
		int ytl = (int) (disp + getNormalY(theta, -width, height));
        int xtr = (int) (xDisp + getNormalX(theta, width, height));
        int ytr = (int) (disp + getNormalY(theta, width, height));
        int xbl = (int) (xDisp + getNormalX(theta, -width, -height));
        int ybl = (int) (disp + getNormalY(theta, -width, -height));
        int xbr = (int) (xDisp + getNormalX(theta, width, -height));
        int ybr = (int) (disp + getNormalY(theta, width, -height));
		
		
		pts[0] = new Point(xtl, ytl);
		pts[1] = new Point(xtr, ytr);
		pts[2] = new Point(xbr, ybr);
		pts[3] = new Point(xbl, ybl);
		
		return pts;		
	}
	
	private float getNormalX(float theta, float xo, float yo){
	    return (float) (xo*Math.cos((double)theta) - yo*Math.sin((double) theta));
	}
	private float getNormalY(float theta, float xo, float yo){
        return (float) (xo*Math.sin((double)theta) + yo*Math.cos((double) theta));
    }

}
