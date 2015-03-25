package com.example.keepbook;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.content.Intent;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.CalendarView.OnDateChangeListener;

public class CalendarActivity extends Activity {

	protected static final String TAG = "KofuTest_DateActivity";

	TextView text;
	CalendarView cv,test;
	
	Calendar now = Calendar.getInstance();
	int Myear = now.get(Calendar.YEAR);
	int Mmonth = now.get(Calendar.MONTH) + 1; // Note: zero based!
	int Mday = now.get(Calendar.DAY_OF_MONTH);
	String date = String.format(Myear + "-" + Mmonth + "-" + Mday).toString();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);

		Log.d(TAG, String.format("before create"));
		Log.d(TAG, String.format("year-month-day: " + Myear +
				"-" + Mmonth + "-"+Mday));
		//processViews
		cv = (CalendarView)findViewById(R.id.calendarView);

		Log.d(TAG, String.format("create listener"));
		
		OnDateChangeListener date_chage_listener = new OnDateChangeListener() {
			@Override
			public void onSelectedDayChange(CalendarView view,
					int year, int month, int dayOfMonth) {
				//TODO Auto-generated method stub
				Myear = year;
				Mmonth = month+1;
				Mday = dayOfMonth;
				
				date = String.format(Myear + "-" + Mmonth + "-" + Mday).toString();
				Log.d(TAG, String.format("OnDateChangeListener: " + date));
				
		        Intent result = getIntent();
		        
		        result.putExtra("CalenderDate", date);
		        setResult(Activity.RESULT_OK, result);
		        finish();
			}
	 	};
	 
	 	cv.setOnDateChangeListener(date_chage_listener);
	}
}
