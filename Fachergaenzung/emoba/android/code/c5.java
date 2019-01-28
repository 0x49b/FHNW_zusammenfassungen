package ch.fhnw.edu.helloworld.v2;

import android.app.Activity;
import android.os.Bundle;

public class MessageActivity extends Activity {

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        MessageFromBitmap message = new MessageFromBitmap(this);
        setContentView(message);
    }
    
}
