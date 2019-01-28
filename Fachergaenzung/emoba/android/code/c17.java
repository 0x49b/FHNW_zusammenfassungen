package edu.mad;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class Main extends Activity implements SensorEventListener {
	private float mLastX, mLastY;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private final float NOISE = (float) 5.0;
	private View myView;
	private TextView textView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		textView = (TextView) findViewById(R.id.textView);
		myView = (View) findViewById(R.id.view);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		listSensors();
	}

	private void listSensors() {
		List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
		StringBuilder sensorString = new StringBuilder("Sensors:\n");
		for (Sensor sensor : sensorList) {
		    sensorString.append(sensor.getName()).append(", \n");
		}
		textView.setText(sensorString);
	}

	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}

	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// can be safely ignored for this demo
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float x = event.values[0];
		float y = event.values[1];

		float deltaX = Math.abs(mLastX - x);
		float deltaY = Math.abs(mLastY - y);
		if ((deltaX > NOISE) || (deltaY > NOISE)) {
			myView.setBackgroundColor(Color.GREEN);
		} else {
			myView.setBackgroundColor(Color.RED);
		}
		mLastX = x;
		mLastY = y;
	}

}