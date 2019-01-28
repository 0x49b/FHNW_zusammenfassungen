package ch.fhnw.edu.helloworld;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HelloWorldLayout extends RelativeLayout {
	public HelloWorldLayout(Context context) {
		super(context);
		
	    // android:layout_width="match_parent"
	    // android:layout_height="match_parent"
		LayoutParams lpView = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		this.setLayoutParams(lpView);
		
		// android:paddingBottom="@dimen/activity_vertical_margin"
		int bottom = (int) getResources().getDimension(R.dimen.activity_vertical_margin);
	    // android:paddingLeft="@dimen/activity_horizontal_margin"
		int left = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);
	    // android:paddingRight="@dimen/activity_horizontal_margin"
		int right = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);
	    // android:paddingTop="@dimen/activity_vertical_margin"
		int top = (int) getResources().getDimension(R.dimen.activity_vertical_margin);
		this.setPadding(left, top, right, bottom);
		
		TextView tv = new TextView(context);
		// android:layout_width="wrap_content"
		// android:layout_height="wrap_content"
		lpView = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		tv.setLayoutParams(lpView);
		// android:text="@string/hello_world" />
		tv.setText(getResources().getString(R.string.hello_world));

		this.addView(tv);
	}
}

