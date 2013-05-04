package ch.sir.sircontroller;

import java.util.EventObject;

import ch.sir.sircontroller.JoyHandler.JoyHandlerListener;
import ch.sir.sircontroller.MyEventClass.MyEventClassListener;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Main extends Activity implements JoyHandlerListener{
	
	private OpenGLRenderer myCube;
	private VirtualJoystick joy1;
	private VirtualJoystick joy2;
	private GLSurfaceView cubeView;
	
	// Event listener
	public void handleJoyEvent(JoyHandler e) {
		//Log.i("Event Listener", "Not in zero");
		float speed = 1;
		if(e.y == 0) {
			speed = 0;
		}
		//myCube.update(1, 0, 0, speed);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		
		RelativeLayout blLay = (RelativeLayout) findViewById(R.id.bottom_l);
		RelativeLayout brLay = (RelativeLayout) findViewById(R.id.bottom_r);
		RelativeLayout tmLay = (RelativeLayout) findViewById(R.id.top_m);
		
        // -----------
        cubeView  = new GLSurfaceView(this);
        cubeView.setRenderer(myCube = new OpenGLRenderer());
        tmLay.addView(cubeView);
        
        // -----------
		
		// ----------- Implement Joystick -----

		joy1 = new VirtualJoystick(blLay,150, 150);
		joy1.addEventListener(this);
		joy1.addEventListener(myCube);
		joy1.lockAxis('y', false);

		joy2 = new VirtualJoystick(brLay,150, 150);
		joy2.addEventListener(this);
		joy2.addEventListener(myCube);
		joy2.lockAxis('x', false);
				
   		// -----------
   		GLSurfaceView mGLSurfaceView = new GLSurfaceView(this);
        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setRenderer(new LessonOneRenderer());
        //tmLay.addView(mGLSurfaceView);
        

        
		/*
		 * Debugger dbg = new Debugger();
		dbg.supervised = joy2;
		dbg.start();
		*/
		
		// -------
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

		
// Async Task for Debuging
	private class Debugger extends Thread{
		
		public VirtualJoystick supervised;

		@Override
		public void run() {
			while(true) {
				
				Log.i("joy2: dx", Float.toString(supervised.getDx()));
				
				try {
					synchronized (this) {
						wait(1000);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}
		}		
	}

	
}