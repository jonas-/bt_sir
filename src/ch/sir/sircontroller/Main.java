package ch.sir.sircontroller;

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

public class Main extends Activity{
	
	private OpenGLRenderer myRenderer;
	private MyTask task;
	private VirtualJoystick joy1;
	
	private MyListener mL = new MyListener(0);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		
		RelativeLayout blLay = (RelativeLayout) findViewById(R.id.bottom_l);
		RelativeLayout brLay = (RelativeLayout) findViewById(R.id.bottom_r);
		RelativeLayout tmLay = (RelativeLayout) findViewById(R.id.top_m);
		
		// ----------- Implement Joystick -----

		joy1 = new VirtualJoystick(blLay,150, 150);
		//joy1.lockAxis('x', false);

		VirtualJoystick joy2 = new VirtualJoystick(brLay,150, 150);
		//joy2.lockAxis('y', false);
				
   		// -----------
   		GLSurfaceView mGLSurfaceView = new GLSurfaceView(this);
   		// Request an OpenGL ES 2.0 compatible context.
        mGLSurfaceView.setEGLContextClientVersion(2);
 
        // Set the renderer to our demo renderer, defined below.
        mGLSurfaceView.setRenderer(new LessonOneRenderer());
        //tmLay.addView(mGLSurfaceView);
        
        // -----------
        
        GLSurfaceView view = new GLSurfaceView(this);
        view.setRenderer(myRenderer = new OpenGLRenderer());
        tmLay.addView(view);
        
        // -----------
        
        task = new MyTask();
        
        task.execute();
		
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
// Async Task
	private class MyTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			float dx = 1;//joy1.getDx();
			Log.i("tesk", Float.toString(dx));
			if(dx != 0) {
				myRenderer.update(1.0f, 0, 0, 1.0f);
			}
			// TODO Auto-generated method stub
			//return null;
			publishProgress(null);
			return null;
		}
		
		protected void onProgressUpdate(Void... progess) {
			
		}
		
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