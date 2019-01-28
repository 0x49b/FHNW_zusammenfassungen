package ch.fhnw.edu.helloworld.v2;

import ch.fhnw.edu.helloworld.R;
import ch.fhnw.edu.helloworld.R.drawable;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;


public class MessageFromBitmap extends View {
	private HelloMessage helloMessage;

	public MessageFromBitmap(Activity context) {
		super(context);
		helloMessage = new HelloMessage(context);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		helloMessage.draw(canvas);
	}
	
	@Override
	protected void onSizeChanged(int width, int height, int oldw, int oldh) {
		helloMessage.adjustTextLayout(width, height);
	}	
}

class HelloMessage {
	private Bitmap helloBitmap;
	private int centerX;
	private int centerY;
	
	public HelloMessage(Context context) {
		Resources res = context.getResources();
		helloBitmap = BitmapFactory.decodeResource(res, R.drawable.helloworld);
	}

	void draw(Canvas canvas) {
		canvas.drawBitmap(helloBitmap, centerX, centerY, null);		
	}
	
	public void adjustTextLayout(int viewWidth, int viewHeight) {
		centerX = (viewWidth/2) - (helloBitmap.getWidth()/2);
		centerY = (viewHeight/2) - (helloBitmap.getHeight()/2);
	}
}
