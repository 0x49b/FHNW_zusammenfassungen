package ch.fhnw.edu.helloworld;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends ActionBarActivity {
	private final String KEY_MYVALUE= "value";
	private int value;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Log.d("HelloWorld", "onCreate() called " + savedInstanceState);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		value = savedInstanceState.getInt(KEY_MYVALUE);
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt(KEY_MYVALUE, value);
		super.onSaveInstanceState(outState);
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Log.d("HelloWorld", "onStart() called");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("HelloWorld", "onResume() called, value is " + value);
		if (value == 0) {
			value = 1111;
			Log.d("HelloWorld", "value set to " + value);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.d("HelloWorld", "onPause() called");
	}	
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.d("HelloWorld", "onStop() called");
	}	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("HelloWorld", "onDestroy() called");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			//View rootView = inflater.inflate(R.layout.fragment_main, container,	false);
			View rootView = new HelloWorldLayout(this.getActivity());
			return rootView;
		}
	}

}
