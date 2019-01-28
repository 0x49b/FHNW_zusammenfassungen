package ch.fhnw.edu.helloworld;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class ColorService extends Service {
	private static String TAG = "HelloWorld-ColorService";
	
	public static final int MSG_STOP_SERVICE = 0;
	public static final int MSG_START_SERVICE = 1;
	public static final int MSG_SET_VALUE = 2;
	public static final String COLOR_VALUE = null;

	private Messenger myMessenger;
	private Messenger mainMessenger;
	private Timer timer = null;
	
	@Override
	public void onCreate() {
		myMessenger = new Messenger(new IncomingHandler(this));
	}

	@Override
	public IBinder onBind(Intent arg0) {
		Log.d(TAG, "onBind() called");
		return myMessenger.getBinder();
	}
	
	@Override
	public void onDestroy() {
		stopColoring();
		Log.d(TAG, "onDestroy() called");
		super.onDestroy();
	}

    static class IncomingHandler extends Handler {
		private final WeakReference<ColorService> service;
    	
    	public IncomingHandler(ColorService service) {
			this.service = new WeakReference<ColorService>(service);
		}
    	
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_START_SERVICE:
                	service.get().mainMessenger = msg.replyTo;
                	service.get().startColoring();
                    break;
                case MSG_STOP_SERVICE:
                	service.get().stopColoring();
                	break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

	private void startColoring() {
		if (timer == null) {
			timer = new Timer();
			timer.schedule(new ColorTask(mainMessenger), 0, 1000);
		}
	}

	private void stopColoring() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}
	
	private class ColorTask extends TimerTask {
		private Messenger messenger;
		private RGBColor color = new RGBColor();
		
		public ColorTask(Messenger messenger) {
			this.messenger = messenger;
		}
		
		@Override
		public void run() {
			if (this.messenger != null) {
				try {
					color.r = (int) Math.round(Math.random() * 255);
					color.g = (int) Math.round(Math.random() * 255);
					color.b = (int) Math.round(Math.random() * 255);

					Message msg = Message.obtain();
					msg.what = ColorService.MSG_SET_VALUE;
					Bundle data = new Bundle();
					data.putSerializable(COLOR_VALUE, color);
					msg.setData(data);
					this.messenger.send(msg);
				} catch (RemoteException e) {
					Log.e(TAG, e.getMessage());
				}
			}
			Log.d(TAG, "color is " + color);
		}
	};	
}
