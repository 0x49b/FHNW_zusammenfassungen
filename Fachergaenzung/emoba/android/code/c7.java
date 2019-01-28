package ch.fhnw.edu.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

public class MainActivity extends FragmentActivity {
	private boolean isTwoPane;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (findViewById(R.id.helloworld_container) != null) {
			isTwoPane = true;
		} else {
			isTwoPane = false;
		}
	}
	
    public void onButtonClick(View v) {
    	if (isTwoPane) {
    		FragmentManager fm = getSupportFragmentManager();
    		Fragment f = fm.findFragmentById(R.id.helloworld_container);
    		if (f == null) {
    			fm.beginTransaction().add(R.id.helloworld_container, new MessageFragment()).commit();
    		}
    	} else {
	    	Intent intent = new Intent(this, MessageActivity.class);
	        startActivity(intent);
    	}
    }	

}
