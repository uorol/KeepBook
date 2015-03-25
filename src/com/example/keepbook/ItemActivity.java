package com.example.keepbook;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;
 
public class ItemActivity extends Activity {
 
	protected static final String TAG = "KofuTest_ItemActivity";

	// 啟動功能用的請求代碼
	private static final int START_CAMERA = 0;
	private static final int START_RECORD = 1;
	private static final int START_LOCATION = 2;
	private static final int START_DATE = 3;
	private static final int START_COLOR = 4;
	
    private EditText title_text, content_text;
    private TextView date_text;
    private ToggleButton direct_toggle, clear_toggle;
    private Item item;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

    	processViews();

        // 取得Intent物件
        Intent intent = getIntent();
        // 讀取Action名稱
        String action = intent.getAction();
 
        Log.d(TAG, String.format("action=%s", action));
		// 如果是修改記事
        if (action.equals(".ItemActivity.EDIT_ITEM")) {
            // 接收記事物件與設定標題、內容
            item = (Item) intent.getExtras().getSerializable(
                    ".Item");
            title_text.setText(item.getTitle());
            content_text.setText(item.getContent());
            direct_toggle.setChecked(item.getDirect()==1 ? true : false);
            clear_toggle.setChecked(item.getClear()==1 ? true : false);
        }
        // 新增記事
        else {
            item = new Item();
        }
    }
 
    private void processViews() {
        title_text = (EditText) findViewById(R.id.title_text);
        content_text = (EditText) findViewById(R.id.content_text);
        date_text = (TextView) findViewById(R.id.date_data);
        direct_toggle = (ToggleButton) findViewById(R.id.direct_toggle_btn);
        clear_toggle = (ToggleButton) findViewById(R.id.clear_toggle_btn);
        
    	Calendar now = Calendar.getInstance();
    	String date = String.format((now.get(Calendar.YEAR) + "-"
    								+ (now.get(Calendar.MONTH)+1) + "-" 
    								+ now.get(Calendar.DAY_OF_MONTH)).toString());
        
    	if (date_text == null)
    		Log.w(TAG, String.format("error! date_text is null"));
    	else 
    		date_text.setText(date.toString());
    }
 
    // 點擊確定與取消按鈕都會呼叫這個方法
    public void onSubmit(View view) {
    	Log.d(TAG, String.format("onSubmit"));
    	// 確定按鈕
        if (view.getId() == R.id.ok_item) {
        	Log.d(TAG, String.format("onSubmit: ok_item"));
            // 讀取使用者輸入的標題與內容
            String titleText = title_text.getText().toString();
            String contentText = content_text.getText().toString();
            Log.d(TAG, String.format("titleText: %s, contentText:%s", titleText, contentText));
 
            // 設定記事物件的標題與內容
            item.setTitle(titleText);
            item.setContent(contentText);
 
            // 設定欠債
            if (direct_toggle.isChecked()) {
            	item.setDirect(1);
            } else {
            	item.setDirect(0);
            }
            
            if (item.getColor() == Colors.LIGHTGREY ||
            		item.getColor() == Colors.RED ||
            		item.getColor() == Colors.BLUE) {
            	if (direct_toggle.isChecked()) {
            		Log.d(TAG, String.format("direct_toggle: isChecked"));
            		item.setColor(getColors((Colors.BLUE.parseColor())));
            	} else {
            		Log.d(TAG, String.format("direct_toggle: is unChecked"));
            		item.setColor(getColors((Colors.RED.parseColor())));
            	}
            }
            
            // 設定結清
            if (clear_toggle.isChecked()) {
            	item.setClear(1);
            } else {
            	item.setClear(0);
            }
            
            // 如果是修改記事
            if (getIntent().getAction().equals(
                    ".ItemActivity.EDIT_ITEM")) {
                item.setLastModify(new Date().getTime());
            }
            // 新增記事
            else {
                item.setDatetime(new Date().getTime());
            }
     
            Intent result = getIntent();
            // 設定回傳的記事物件
            result.putExtra("com.example.keepbook.Item", item);
            
            // 設定回應結果為確定
            setResult(Activity.RESULT_OK, result);
        }
 
        // 結束
        finish();
    }
 
    public void onToggleClicked(View view) {
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();
        if (on) {
        } else {
        }
    }
    
    // 以後需要擴充的功能
    public void clickFunction(View view) {
        int id = view.getId();
 
        switch (id) {
        case R.id.take_picture:
            break;
        case R.id.record_sound:
            break;
        case R.id.set_location:
            break;
        case R.id.set_date:
        	// trigger calendar view to select date
        	Log.d(TAG, String.format("clickFunction: setDate"));
            startActivityForResult(
                    new Intent(this, CalendarActivity.class), START_DATE);
            break;
        case R.id.select_color:
            // 啟動設定顏色的Activity元件
        	Log.d(TAG, String.format("clickFunction: setColors"));
            startActivityForResult(
                    new Intent(this, ColorActivity.class), START_COLOR);
        	break;
        }
 
    }
 
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case START_CAMERA:
                    break;
                case START_RECORD:
                    break;
                case START_LOCATION:
                    break;
                case START_DATE:
                	Log.d(TAG, String.format("onActivityResult: setDate +++"));
                    String date = data.getStringExtra("CalenderDate");
                    Log.d(TAG, String.format("onActivityResult: setDate " + date.toString()));
                    date_text.setText(date);
                    break;
                // 設定顏色
                case START_COLOR:
                	Log.d(TAG, String.format("onActivityResult: setColors"));
                    int colorId = data.getIntExtra(
                            "colorId", Colors.LIGHTGREY.parseColor());
                    item.setColor(getColors(colorId));
                    break;
            }
        }
    }
    
    static Colors getColors(int color) {
        Colors result = Colors.LIGHTGREY;
     
        if (color == Colors.BLUE.parseColor()) {
            result = Colors.BLUE;
        }
        else if (color == Colors.PURPLE.parseColor()) {
            result = Colors.PURPLE;
        }
        else if (color == Colors.GREEN.parseColor()) {
            result = Colors.GREEN;
        }
        else if (color == Colors.ORANGE.parseColor()) {
            result = Colors.ORANGE;
        }
        else if (color == Colors.RED.parseColor()) {
            result = Colors.RED;
        }
     
        return result;
    }
    
}