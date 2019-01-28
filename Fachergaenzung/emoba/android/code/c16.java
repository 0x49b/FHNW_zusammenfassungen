package ch.fhnw.edu.mad;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	private static String TAG = "HelloWorld";
	private Uri wordsUri = Uri.parse("content://ch.fhnw.edu.mad.helloworld/words");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.standard_insert_button).setOnClickListener(this);
		findViewById(R.id.bulk_insert_button).setOnClickListener(this);
	}
	
	private void insertOneHundredRecords() {
		for (int i = 0; i<100; i++) {
			 ContentValues values = new ContentValues();
			 values.put("Id", i);
			 values.put("HelloString", "Hello World");
			 getContentResolver().insert(wordsUri, values);
		}
	}
	
	private void bulkInsertOneHundredRecords() {
		ContentValues[] items = new ContentValues[100];
		for (int i = 0; i < 100; i++) {
			ContentValues item = new ContentValues();
			item.put("Id", i);
			item.put("HelloString", "Hello World");
			items[i] = item;
		}
		getContentResolver().bulkInsert(wordsUri, items);
	}

	@Override
	public void onClick(View v) {
		int nr = getContentResolver().delete(wordsUri, null, null);
		Log.d(TAG, nr + " records deleted");
		long startTime = System.currentTimeMillis();
		if (v.getId()==R.id.standard_insert_button) {
			insertOneHundredRecords();
		} else {
			bulkInsertOneHundredRecords();
		}
		long diff = System.currentTimeMillis() - startTime;
		((TextView)findViewById(R.id.exec_time_label)).setText(Long.toString(diff)+"ms");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
