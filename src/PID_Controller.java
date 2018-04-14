
public class PID_Controller {
    private static final float MAX_INTEGRAL = 50;
    private float kp, kd, ki;
    private float integral;
    private float lastError;
    private float timestep;
    private float[] lastVals;
    
    public PID_Controller(float ts){
        kp = 0;
        kd = 0;
        ki = 0;
        integral = 0;
        lastError = 0;
        timestep = ts;
        // Initialize all values to 0
        lastVals = new float[200];
        for(int i =0; i < lastVals.length; i++){
            lastVals[i]=0;
        }
    }
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
    
    
    public float getPID(float input, float setPoint){
        float error = input - setPoint;
        float der = (error - lastError)*timestep;
        lastError=error;
        float sum = lastVals[0];
        for (int i = 1; i < lastVals.length; i++){
            sum += lastVals[i];
            lastVals[i-1] = lastVals[i];
        }
        lastVals[lastVals.length-1] = error;
        integral = sum*timestep;
        System.out.print("\terr=" + lastError + "\tint=" + integral);
        return error*kp + der*kd + integral*ki;
    }
    
}
