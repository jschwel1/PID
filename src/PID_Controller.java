import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Semaphore;

import javax.swing.Timer;

public class PID_Controller implements Runnable{
    private static final float MAX_INTEGRAL = 50;
    private float kp, kd, ki;
    private float integral;
    private float lastError;
    private float timestep;
    private float[] lastVals;
    private float setPoint;
    private float input;
    private float PID;
    
    private Semaphore setPointMutex;
    private Semaphore PIDValueMutex;
    private Semaphore inputValueMutex;
    
    private Timer clock;
    
    /**
     * 
     * @param ts - timestep in ms
     */
    public PID_Controller(int ts){
        kp = 0;
        kd = 0;
        ki = 0;
        integral = 0;
        lastError = 0;
        timestep = (float)ts/1000.0f; // timestep in seconds
        // Initialize all values to 0
        lastVals = new float[200];
        for(int i =0; i < lastVals.length; i++){
            lastVals[i]=0;
        }
        
        setPoint=0;
        input=0;
        PID=0;
        
        setPointMutex = new Semaphore(1);
        PIDValueMutex = new Semaphore(1);
        inputValueMutex = new Semaphore(1);
        
    }
    /*
    public PID_Controller(float ts, float kp){
        this(ts);
        this.kp = kp;
    }
    public PID_Controller(float ts, float kp, float ki){
        this(ts);
        this.kp = kp;
        this.ki = ki;
    }
    public PID_Controller(float ts, float kp, float ki, float kd){
        this(ts);
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
    }
    */
    /**
     * 
     * @param ts
     * @param kp
     * @param ki
     * @param kd
     * @param setpoint
     */
    public PID_Controller(int ts, float kp, float ki, float kd, float setpoint){
        this(ts);
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        this.setPoint = setpoint;
        System.out.println("Created... Delay=" + (int)(1000*timestep) );
    }
    
    public void setInput(float in){
     try{
         inputValueMutex.acquire();
         input = in;
         inputValueMutex.release();
     } catch (InterruptedException e){
         e.printStackTrace();
     }
     
    }
    
    public float getPID(){
        float p=0;
        try {
            PIDValueMutex.acquire();
            p = PID;
            PIDValueMutex.release();
        } catch(Exception e){
            e.printStackTrace();
            PIDValueMutex.release();
        }
        return p;
    }
    
    public void run(){
        while(true){
            float error=0;
            try {
                inputValueMutex.acquire();
                error = input - setPoint;
                error = sign(error)*(float)Math.sqrt(Math.abs(error));
                inputValueMutex.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            float der = (error - lastError)*timestep;
            lastError=error;
            float sum = lastVals[0];
            for (int i = 1; i < lastVals.length; i++){
                sum += lastVals[i];
                lastVals[i-1] = lastVals[i];
            }
            
            lastVals[lastVals.length-1] = error;
            integral = sum*timestep;
            
            //System.out.print("\terr=" + lastError + "\tint=" + integral);
            try {
                PIDValueMutex.acquire();
                PID = error*kp + der*kd + integral*ki;
                PIDValueMutex.release();
            } catch( Exception e){
                e.printStackTrace();
            }
            System.out.println("(PID)=("+error+","+integral+","+der+")->"+PID);
            
            try{
                Thread.sleep((int)(1000*timestep));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    private static float sign(float n){
        if (n>0) return 1;
        if (n<0) return -1;
        return 0;
    }
}
