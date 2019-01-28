package ch.fhnw.edu.helloworld;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MessageActivity extends FragmentActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_message);
    }
}
