package ch.fhnw.edu.helloworld;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	public static final String TAG = "HelloWorld-MainActivity";
	private TextView helloWorldView;
	private Messenger serviceMessenger;
	private Messenger myMessenger;

	private void updateHelloWorldView(RGBColor c) {
		helloWorldView.setBackgroundColor(Color.rgb(c.r,c.g, c.b));
		Log.d(TAG, c.r + " " + c.g + " " + c.b);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		helloWorldView = (TextView) findViewById(R.id.txtView);
		helloWorldView.setText("Hello World");

		myMessenger = new Messenger(new IncomingHandler(this));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		bindService(new Intent(this, ColorService.class), svcConn, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (serviceMessenger != null) {
			unbindService(svcConn);
		}
	}
	
	private ServiceConnection svcConn = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			serviceMessenger = new Messenger(service);
			Log.d(TAG, "service is connected");
		}

		public void onServiceDisconnected(ComponentName className) {
			serviceMessenger = null;
			Log.d(TAG, "service is disconnected");
		}
	};	
	
	public void start(View view) {
		Message msg = Message.obtain(null, ColorService.MSG_START_SERVICE);
		msg.replyTo = myMessenger;
		try {
			serviceMessenger.send(msg);
		} catch (RemoteException e) {
			Log.e(TAG, e.getMessage());
		}
	}
	
	public void stop(View view) {
		Message msg = Message.obtain(null, ColorService.MSG_STOP_SERVICE);
		try {
			serviceMessenger.send(msg);
		} catch (RemoteException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	static class IncomingHandler extends Handler {
		private final WeakReference<MainActivity> mainActivity;
		
		public IncomingHandler(MainActivity activity) {
			this.mainActivity = new WeakReference<MainActivity>(activity);
		}
		
	    @Override
	    public void handleMessage(Message msg) {
	        switch (msg.what) {
	            case ColorService.MSG_SET_VALUE:
	            	RGBColor color = (RGBColor) msg.getData().getSerializable(ColorService.COLOR_VALUE);
	            	mainActivity.get().updateHelloWorldView(color);
	                break;
	            default:
	                super.handleMessage(msg);
	        }
	    }
	}		
}
