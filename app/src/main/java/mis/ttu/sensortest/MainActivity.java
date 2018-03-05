package mis.ttu.sensortest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity implements SensorEventListener {


    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long lastUpdate = 0;
    private float x, y, z;
    public float xmax,ymax;
    int i = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 100;
    private long last_shake_time = 0;
    private static final long SHAKE_HOLDOFF_MS = 5000;

    ImageView Catch;



  /*  CustomDrawableView mCustomDrawableView = null;
    ShapeDrawable mDrawable = new ShapeDrawable();*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        xmax = height- 50;
        ymax = width - 50;

       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);*/

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x+y+z - last_x - last_y - last_z) / diffTime * 10000;

                //if (speed > SHAKE_THRESHOLD) {
                    // yes, this is a shake action!
                    long shake_time = System.currentTimeMillis();
                    if (shake_time - last_shake_time >= SHAKE_HOLDOFF_MS) {
                        i++;
                        last_shake_time = shake_time;
                    }
                //}

                if(Round(x,4)>3.0000){
                    //Log.e("sensor", "Right shake detected " );
                    Log.e("sensor", "Left" );
                    Log.e("sensor", String.valueOf(Round(x,4)));
                }
                else if(Round(x,4)<-3.0000){
                    //Log.e("sensor", "Left shake detected " );
                    Log.e("sensor", "Right" );
                    Log.e("sensor", String.valueOf(Round(x,4)));
                }

                last_x = x;
                last_y = y;
                last_z = z;

//                updateCatch();
            }


            }
    }

    private void updateCatch(){

        float frameTime = 0.666f;

        last_x += (x * frameTime);

        float xS = (x / 2) * frameTime;
        float yS = (y / 2) * frameTime;

        x -= xS;
        y -= yS;

        if (x > xmax) {
            x = xmax;
        } else if (x < 0) {
            x= 0;
        }
        if (y > ymax) {
            y = ymax;
        } else if (y < 0) {
            y = 0;
        }


    }

    public static float Round(float Rval, int Rpl) {
        float p = (float)Math.pow(10,Rpl);
        Rval = Rval * p;
        float tmp = Math.round(Rval);
        return (float)tmp/p;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /*public class CustomDrawableView extends View
    {
        static final float width = 50;
        static final float height = 50;

        public CustomDrawableView(Context context)
        {
            super(context);

            mDrawable = new ShapeDrawable(new OvalShape());
            mDrawable.getPaint().setColor(0xff74AC23);
            mDrawable.setBounds(x, y, x + width, y + height);
        }

        protected void onDraw(Canvas canvas)
        {
            RectF oval = new RectF(Accelerometer.x, Accelerometer.y, Accelerometer.x + width, Accelerometer.y
                    + height); // set bounds of rectangle
            Paint p = new Paint(); // set some paint options
            canvas.drawOval(oval, p);
            invalidate();
        }*/
    }



