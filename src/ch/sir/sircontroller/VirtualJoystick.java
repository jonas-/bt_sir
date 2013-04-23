package ch.sir.sircontroller;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Debug;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class VirtualJoystick {
	
	private ImageView stick;
	private Bitmap pic;
	private Bitmap picRes;
	private RelativeLayout layout;
	private RelativeLayout.LayoutParams params;
	
	public int bitWidth = 50;
	public int bitHeight = 50;
	public int startPosX = 250;
	public int startPosY = 250;
	public int radius = 100;
	
	private boolean dragging = false;
	private boolean draggingPos = true;
	
	public VirtualJoystick(RelativeLayout layout) {
		// Create View
		this.layout = layout;
		stick = new ImageView(layout.getContext());
		
		// Add joystick bitmap to View
		pic = BitmapFactory.decodeResource(layout.getResources(), R.drawable.circle);
		picRes = Bitmap.createScaledBitmap(pic, bitWidth, bitHeight, false);
		stick.setImageBitmap(picRes);
		params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(startPosX-bitWidth/2, startPosY-bitWidth/2, 0, 0);
		stick.setLayoutParams(params);
		
		layout.addView(stick); // Add joystick to layout
		
		// Draw Circle
		Bitmap pallet = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(pallet);
		Paint paint = new Paint(); 
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawCircle(startPosX, startPosY, radius, paint);
		
		ImageView circle = new ImageView(layout.getContext());
		circle.setImageBitmap(pallet);
		
		layout.addView(circle); // add Circle to Layout
		
		// make layout sensitive
		layout.setOnTouchListener(myListener);
		
		//Log.i("var", Integer.toString(picRes.getHeight()));
	}
	
	OnTouchListener myListener = new OnTouchListener(){
		@Override
		public boolean onTouch(View v, MotionEvent event){
		    //Log.i("info", "Touched MainLayout");
		    
			updatePos(event);
			
		    return true;
		}};
		
	private void updatePos(MotionEvent event) {
		double tabX = event.getX();
		double tabY = event.getY();
		
	    int tabXint = (int) Math.round(event.getX());
	    int tabYint = (int) Math.round(event.getY());
	    
	    int posX = tabXint - bitWidth/2;
	    int posY = tabYint - bitHeight/2;
	    
		int disX = tabXint-startPosX;
		int disY = tabYint-startPosY;
		
		//drag drop 
		if ( event.getAction() == MotionEvent.ACTION_DOWN ){
			
			// start zone
			if(disX*disX + disY*disY<bitWidth*bitWidth) {
				dragging = true;
			}
		}else if ( event.getAction() == MotionEvent.ACTION_UP){
			dragging = false;
			draggingPos = true;
		}
			
		// bound to circle
		if(disX*disX + disY*disY>radius*radius) {
			draggingPos = false;
		}
		else {
			draggingPos = true;
		}
		
		//move joystick
		if(dragging) {
			//stay in circle if tab out of circle
			if(!draggingPos) {

				double dX = tabX - startPosX;
				double dY = tabY -startPosY;
				
				double num = Math.sqrt(1+Math.pow(dY/dX, 2));
				int dx = (int) Math.round(radius/num);
				int dy = (int) Math.round(Math.sqrt(radius*radius - dx*dx));
				
				// Check position in quarter
				if(dX<0) {dx = -dx;}
				if(dY<0) {dy = -dy;}
				
				posX = startPosX + dx - bitWidth/2;
				posY = startPosY + dy - bitHeight/2;
			}
			params.setMargins(posX, posY, 0, 0);
			stick.setLayoutParams(params);
		} 
		else { // if release, set position back
			params.setMargins(startPosX-bitWidth/2, startPosY-bitHeight/2, 0, 0);
			stick.setLayoutParams(params);
		}	
	}		
}