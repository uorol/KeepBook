package com.example.keepbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.MediaPlayer;


public class KeepBook extends Activity {

	protected static final String TAG = "KofuTest";
	
	private TextView show_app_name;
	private ListView item_list;
	private ItemDAO itemDAO;
	private List<Item> items;
	private ItemAdapter itemAdapter;
	
	// 選單項目物件
	private MenuItem add_item, search_item, revert_item, share_item, delete_item;
	 
	// 已選擇項目數量
	private int selectedCount = 0;

	//Record test
	MediaRecorder mMR = null;
	MediaPlayer mMP = null;
	Boolean mRecord = false;
	
    // 換掉原來的字串陣列
    //private ArrayList<String> data = new ArrayList<>();
    //private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keep_book);
        mMR = new MediaRecorder();
        processViews();
        processControllers();

        // 建立資料庫物件
        itemDAO = new ItemDAO(getApplicationContext());
        Log.d(TAG, String.format("@@## new ItemDAO, getCount=%d", itemDAO.getCount()));
     
        // 如果資料庫是空的，就建立一些範例資料
        // 這是為了方便測試用的，完成應用程式以後可以拿掉
        if (itemDAO.getCount() == 0) {
        	Log.d(TAG, String.format("@@## new ItemDAO, calling itemDAO.sample()"));
            itemDAO.sample();
        }
     
        // 取得所有記事資料
        items = itemDAO.getAll();
     
        itemAdapter = new ItemAdapter(this, R.layout.single_item, items);
        item_list.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();
        
        // 讀取在畫面配置檔已經設定好名稱的元件
        show_app_name = (TextView) findViewById(R.id.show_app_title);
    }

    private void processViews() {
        item_list = (ListView)findViewById(R.id.item_list);
        show_app_name = (TextView) findViewById(R.id.show_app_title);
    }

    private void processControllers() {
        // 建立選單項目點擊監聽物件
        OnItemClickListener itemListener = new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, 
                    int position, long id) {
                // 使用Action名稱建立啟動另一個Activity元件需要的Intent物件
                Intent intent = new Intent(".ItemActivity.EDIT_ITEM");
 
                Item item = itemAdapter.getItem(position);
                
                // 設定記事編號與標題
                intent.putExtra("position", position);
                intent.putExtra(".Item", item);
 
                // 呼叫「startActivityForResult」，第二個參數「1」表示執行修改
                startActivityForResult(intent, 1);
            }
        };
        if (itemListener == null){
        	Log.w(TAG, String.format("itemListener is null object"));
        } else {
        	// 註冊選單項目點擊監聽物件
        	item_list.setOnItemClickListener(itemListener);
        }

        // 建立選單項目長按監聽物件
        OnItemLongClickListener itemLongListener = new OnItemLongClickListener() {
            // 第一個參數是使用者操作的ListView物件
            // 第二個參數是使用者選擇的項目
            // 第三個參數是使用者選擇的項目編號，第一個是0
            // 第四個參數在這裡沒有用途         
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, 
                    int position, long id) {
            	Item item = itemAdapter.getItem(position);

                // 處理是否顯示已選擇項目
                processMenu(item);
                // 重新設定記事項目
                itemAdapter.set(position, item);

                return true;
            }
        }; 
        if (itemLongListener == null){
        	Log.w(TAG, String.format("itemLongListener is null object"));
        } else {
            // 註冊選單項目長按監聽物件
            item_list.setOnItemLongClickListener(itemLongListener);
        }

        // 建立長按監聽物件
        OnLongClickListener listener = new OnLongClickListener() {
 
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder dialog = 
                        new AlertDialog.Builder(KeepBook.this);
                dialog.setTitle(R.string.app_name)
                      .setMessage(R.string.about)
                      .show();
                return false;
            }
 
        };
        if (listener == null){
        	Log.w(TAG, String.format("listener is null object"));
        } else {
            // 註冊長按監聽物件
            show_app_name.setOnLongClickListener(listener); 
        }
        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 如果被啟動的Activity元件傳回確定的結果
    	Log.d(TAG, String.format("onActivityResult: resultCode=%d", resultCode));
        if (resultCode == Activity.RESULT_OK) {
        	Log.d(TAG, String.format("onActivityResult: RESULT_OK", resultCode));
            
        	Item item = (Item) data.getExtras().getSerializable(
                    "com.example.keepbook.Item");

            if (requestCode == 0) {
                // 新增記事資料到資料庫
                item = itemDAO.insert(item);
     
                items.add(item);
                itemAdapter.notifyDataSetChanged();
            }
            else if (requestCode == 1) {
                int position = data.getIntExtra("position", -1);
     
                if (position != -1) {
                    // 修改資料庫中的記事資料
                    itemDAO.update(item);
     
                    items.set(position, item);
                    itemAdapter.notifyDataSetChanged();
                }
            }
        }
    }
    
    // Load the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        // 取得選單項目物件
        add_item = menu.findItem(R.id.add_item);
        search_item = menu.findItem(R.id.search_item);
        revert_item = menu.findItem(R.id.revert_item);
        share_item = menu.findItem(R.id.share_item);
        delete_item = menu.findItem(R.id.delete_item);
     
        // 設定選單項目
        processMenu(null);
        
        return true;
    }

    // Hook the onClick function for Menu
    public void clickMenuItem(MenuItem item) {
        // get onClick item via itemId
        int itemId = item.getItemId();
 
        switch (itemId) {
        case R.id.search_item:
            break;
        case R.id.add_item:
        	// 建立啟動另一個Activity元件需要的Intent物件
            Intent intent = new Intent(".ItemActivity.ADD_ITEM");
            // 呼叫「startActivityForResult」，第二個參數「0」目前沒有使用
            startActivityForResult(intent, 0);
            break;
        case R.id.revert_item:
            for (int i = 0; i < itemAdapter.getCount(); i++) {
                Item ri = itemAdapter.getItem(i);
 
                if (ri.isSelected()) {
                    ri.setSelected(false);
                    itemAdapter.set(i, ri);
                }
            }
        	break;
        case R.id.delete_item:
            // 沒有選擇
            if (selectedCount == 0) {
                break;
            }
 
            // 建立與顯示詢問是否刪除的對話框
            AlertDialog.Builder d = new AlertDialog.Builder(this);
            String message = getString(R.string.delete_item);
            d.setTitle(R.string.delete)
                    .setMessage(String.format(message, selectedCount));
            d.setPositiveButton(android.R.string.yes,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 刪除所有已勾選的項目
                            int index = itemAdapter.getCount() - 1;
 
                            while (index > -1) {
                                Item item = itemAdapter.get(index);
 
                                if (item.isSelected()) {
                                    itemAdapter.remove(item);
                                    // 刪除資料庫中的記事資料
                                    itemDAO.delete(item.getId());
                                }
 
                                index--;
                            }
 
                            // 通知資料改變
                            itemAdapter.notifyDataSetChanged();
                            selectedCount = 0;
                            processMenu(null);                                
                        }
                    });
            d.setNegativeButton(android.R.string.no, null);
            d.show();
 
            break;
        case R.id.googleplus_item:
            break;
        case R.id.facebook_item:
            break;
        }
    }

 // 處理是否顯示已選擇項目
    private void processMenu(Item item) {
        // 如果需要設定記事項目
        if (item != null) {
            // 設定已勾選的狀態
            item.setSelected(!item.isSelected());
     
            // 計算已勾選數量
            if (item.isSelected()) {
                selectedCount++;
            }
            else {
                selectedCount--;
            }
        }
     
        // 根據選擇的狀況，設定是否顯示選單項目
        add_item.setVisible(selectedCount == 0);
        search_item.setVisible(selectedCount == 0);
        revert_item.setVisible(selectedCount > 0);
        share_item.setVisible(selectedCount > 0);
        delete_item.setVisible(selectedCount > 0);
    }
    
    // 點擊應用程式名稱元件後呼叫的方法
    public void aboutApp(View view) {
        // 建立啟動另一個Activity元件需要的Intent物件
        // 建構式的第一個參數：「this」
        // 建構式的第二個參數：「Activity元件類別名稱.class」
        Intent intent = new Intent(this, AboutActivity.class);
        // 呼叫「startActivity」，參數為一個建立好的Intent物件
        // 這行敘述執行以後，如果沒有任何錯誤，就會啟動指定的元件
        startActivity(intent);
    }

    private void RecordTest() {
		// TODO Auto-generated method stub
		if (!mRecord)//start recording
		{
			mMR.setAudioSource(MediaRecorder.AudioSource.MIC); 
			mMR.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			//mMR.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mMR.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
			mMR.setAudioSamplingRate(44100);
			mMR.setAudioChannels(1);
			
			try {
				FileOutputStream mFis = openFileOutput("samplefile.3gp", MODE_WORLD_READABLE);
				mMR.setOutputFile(mFis.getFD()); 
				mMR.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			mMR.start();   
		}
		else//stop recording
		{
			mMR.stop();
			mMR.reset();
		}
		mRecord = !mRecord;
    }
    
    private void PlayRecordData() {
		if (mMP != null)
			ReleaseMP();
		
		mMP = new MediaPlayer();
		try {
			FileInputStream mFis = openFileInput("samplefile.3gp");
			mMP.setDataSource(mFis.getFD());
			mMP.setAudioStreamType(AudioManager.STREAM_SYSTEM);
			mMP.prepare();
			Log.d(TAG, String.format("set stream type to %d", AudioManager.STREAM_SYSTEM));
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mMP.start();
    }
    
    private void ReleaseMP() {
    	if (mMP != null){
    		if (mMP.isPlaying())
    			mMP.stop();
    		mMP.release();
    		mMP = null;
    	}
    }
    
}
