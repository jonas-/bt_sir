package ch.sir.sircontroller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.sir.sircontroller.JoyHandler.JoyHandlerListener;

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
	
	// ---- Event handler ------------
	private List _listeners = new ArrayList();
	
	public synchronized void addEventListener(JoyHandlerListener listener) {
		//Log.i("Joy", "addEventListener called");
		_listeners.add(listener);
	}
	public synchronized void removeEventListener(JoyHandlerListener listener) {
		//Log.i("Joy", "removeEventListener called");
		_listeners.remove(listener);
	}
	
	private synchronized void firePosition() {
		//Log.i("Joy", "fireEvent called");
		JoyHandler event = new JoyHandler(this, getDx(), getDy(), "Name bla");
		Iterator i = _listeners.iterator();
		
		while(i.hasNext()) {
			((JoyHandlerListener) i.next()).handleJoyEvent(event);
		}
	}	
	// ---------------
	
	
	private ImageView stick;
	private Bitmap pic;
	private Bitmap picRes;
	private RelativeLayout layout;
	private RelativeLayout.LayoutParams params;
	
	public int bitWidth = 50;
	public int bitHeight = 50;
	public int startPosX;
	public int startPosY;
	public int radius = 100;
	
	private boolean dragging = false;
	private boolean draggingPos = true;
	
	private boolean _moveX = true;
	private boolean _moveY = true;
	
	private int _posX;
	private int _posY;
	
	// Constructor
	public VirtualJoystick(RelativeLayout layout,int posx, int posy) {
		// Set Startposition
		this.startPosX = posx;
		this.startPosY = posy;
		
		_posX = startPosX-bitWidth/2;
		_posY = startPosY-bitWidth/2;
		
		// Create View
		this.layout = layout;
		stick = new ImageView(layout.getContext());
		
		// Create joystick bitmap
		pic = BitmapFactory.decodeResource(layout.getResources(), R.drawable.circle);
		picRes = Bitmap.createScaledBitmap(pic, bitWidth, bitHeight, false);
		stick.setImageBitmap(picRes);
		params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(_posX, _posY, 0, 0);
		stick.setLayoutParams(params);	
		
		// Draw Circle
		int wScreen = startPosX+radius;
		int hScreen= startPosY+radius;
		Bitmap pallet = Bitmap.createBitmap(wScreen, hScreen, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(pallet);
		Paint paint = new Paint(); 
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawCircle(startPosX, startPosY, radius, paint);
		canvas.drawLine(startPosX, startPosY+radius, startPosX, startPosY-radius, paint); // draw vertical line
		canvas.drawLine(startPosX+radius, startPosY, startPosX-radius, startPosY, paint); // draw hotizontal line
		
		ImageView circle = new ImageView(layout.getContext());
		circle.setImageBitmap(pallet);
		
		// Add graphics to view
		layout.addView(circle); // add Circle+ to Layout
		layout.addView(stick); // Add joystick to layout
		
		// make layout sensitive
		layout.setOnTouchListener(myListener);
		
		//Log.i("var", Integer.toString(picRes.getHeight()));
	}
	
	OnTouchListener myListener = new OnTouchListener(){
		@Override
		public boolean onTouch(View v, MotionEvent event){
		    //Log.i("info", "Touched MainLayout");
		    
			updatePos(event);
			//Log.i("var", Integer.toString(getDx()));
			
		    return true;
		}};
	
	public void lockAxis(char axis, boolean bol) {
		if(axis == 'x') {this._moveX = bol;}
		if(axis == 'y') {this._moveY =bol;}
	}
	
	public float getDx() {
		return ((float) -(this.startPosX - this._posX-bitWidth/2))/radius;
	}
	
	public float getDy() {
		return (float) (this.startPosY - this._posY-bitHeight/2)/radius;
	}
		
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
			// lock position to axis
			if(!_moveX) {posX = startPosX-bitWidth/2;}
			if(!_moveY) {posY = startPosY-bitWidth/2;}
			
			// set final position
			this._posX = posX;
			this._posY = posY;
			params.setMargins(_posX, _posY, 0, 0);
			stick.setLayoutParams(params);
		} 
		else { // if release, set position back
			_posX = startPosX-bitWidth/2;
			_posY = startPosY-bitHeight/2;
			params.setMargins(_posX, _posY, 0, 0);
			stick.setLayoutParams(params);
		}
		firePosition();
	}	
}