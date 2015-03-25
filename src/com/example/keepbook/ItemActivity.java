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

	// �Ұʥ\��Ϊ��ШD�N�X
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

        // ���oIntent����
        Intent intent = getIntent();
        // Ū��Action�W��
        String action = intent.getAction();
 
        Log.d(TAG, String.format("action=%s", action));
		// �p�G�O�ק�O��
        if (action.equals(".ItemActivity.EDIT_ITEM")) {
            // �����O�ƪ���P�]�w���D�B���e
            item = (Item) intent.getExtras().getSerializable(
                    ".Item");
            title_text.setText(item.getTitle());
            content_text.setText(item.getContent());
            direct_toggle.setChecked(item.getDirect()==1 ? true : false);
            clear_toggle.setChecked(item.getClear()==1 ? true : false);
        }
        // �s�W�O��
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
 
    // �I���T�w�P�������s���|�I�s�o�Ӥ�k
    public void onSubmit(View view) {
    	Log.d(TAG, String.format("onSubmit"));
    	// �T�w���s
        if (view.getId() == R.id.ok_item) {
        	Log.d(TAG, String.format("onSubmit: ok_item"));
            // Ū���ϥΪ̿�J�����D�P���e
            String titleText = title_text.getText().toString();
            String contentText = content_text.getText().toString();
            Log.d(TAG, String.format("titleText: %s, contentText:%s", titleText, contentText));
 
            // �]�w�O�ƪ��󪺼��D�P���e
            item.setTitle(titleText);
            item.setContent(contentText);
 
            // �]�w���
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
            
            // �]�w���M
            if (clear_toggle.isChecked()) {
            	item.setClear(1);
            } else {
            	item.setClear(0);
            }
            
            // �p�G�O�ק�O��
            if (getIntent().getAction().equals(
                    ".ItemActivity.EDIT_ITEM")) {
                item.setLastModify(new Date().getTime());
            }
            // �s�W�O��
            else {
                item.setDatetime(new Date().getTime());
            }
     
            Intent result = getIntent();
            // �]�w�^�Ǫ��O�ƪ���
            result.putExtra("com.example.keepbook.Item", item);
            
            // �]�w�^�����G���T�w
            setResult(Activity.RESULT_OK, result);
        }
 
        // ����
        finish();
    }
 
    public void onToggleClicked(View view) {
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();
        if (on) {
        } else {
        }
    }
    
    // �H��ݭn�X�R���\��
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
            // �Ұʳ]�w�C�⪺Activity����
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
                // �]�w�C��
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