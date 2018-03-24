
public class PID_Controller {

    private float kp, kd, ki;
    private float integral;
    private float lastError;
    private float timestep;
    public PID_Controller(float ts){
        kp = 0;
        kd = 0;
        ki = 0;
        integral = 0;
        lastError = 0;
        timestep = ts;
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
        integral += error*timestep;
        return error*kp + der*kd + integral*ki;
    }
    
}
