package ch.sir.sircontroller;

// Test change for commit

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Main extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		
		// ----------- Implement Joystick -----
		// define main layout
		RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.main);
		
		VirtualJoystick joy1 = new VirtualJoystick(mainLayout);
		// -------
		
		// --------- JoyNod
		//JoyPub node = new JoyPub();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}