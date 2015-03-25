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
	
	// ��涵�ت���
	private MenuItem add_item, search_item, revert_item, share_item, delete_item;
	 
	// �w��ܶ��ؼƶq
	private int selectedCount = 0;

	//Record test
	MediaRecorder mMR = null;
	MediaPlayer mMP = null;
	Boolean mRecord = false;
	
    // ������Ӫ��r��}�C
    //private ArrayList<String> data = new ArrayList<>();
    //private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keep_book);
        mMR = new MediaRecorder();
        processViews();
        processControllers();

        // �إ߸�Ʈw����
        itemDAO = new ItemDAO(getApplicationContext());
        Log.d(TAG, String.format("@@## new ItemDAO, getCount=%d", itemDAO.getCount()));
     
        // �p�G��Ʈw�O�Ū��A�N�إߤ@�ǽd�Ҹ��
        // �o�O���F��K���եΪ��A�������ε{���H��i�H����
        if (itemDAO.getCount() == 0) {
        	Log.d(TAG, String.format("@@## new ItemDAO, calling itemDAO.sample()"));
            itemDAO.sample();
        }
     
        // ���o�Ҧ��O�Ƹ��
        items = itemDAO.getAll();
     
        itemAdapter = new ItemAdapter(this, R.layout.single_item, items);
        item_list.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();
        
        // Ū���b�e���t�m�ɤw�g�]�w�n�W�٪�����
        show_app_name = (TextView) findViewById(R.id.show_app_title);
    }

    private void processViews() {
        item_list = (ListView)findViewById(R.id.item_list);
        show_app_name = (TextView) findViewById(R.id.show_app_title);
    }

    private void processControllers() {
        // �إ߿�涵���I����ť����
        OnItemClickListener itemListener = new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, 
                    int position, long id) {
                // �ϥ�Action�W�٫إ߱Ұʥt�@��Activity����ݭn��Intent����
                Intent intent = new Intent(".ItemActivity.EDIT_ITEM");
 
                Item item = itemAdapter.getItem(position);
                
                // �]�w�O�ƽs���P���D
                intent.putExtra("position", position);
                intent.putExtra(".Item", item);
 
                // �I�s�ustartActivityForResult�v�A�ĤG�ӰѼơu1�v��ܰ���ק�
                startActivityForResult(intent, 1);
            }
        };
        if (itemListener == null){
        	Log.w(TAG, String.format("itemListener is null object"));
        } else {
        	// ���U��涵���I����ť����
        	item_list.setOnItemClickListener(itemListener);
        }

        // �إ߿�涵�ت�����ť����
        OnItemLongClickListener itemLongListener = new OnItemLongClickListener() {
            // �Ĥ@�ӰѼƬO�ϥΪ̾ާ@��ListView����
            // �ĤG�ӰѼƬO�ϥΪ̿�ܪ�����
            // �ĤT�ӰѼƬO�ϥΪ̿�ܪ����ؽs���A�Ĥ@�ӬO0
            // �ĥ|�ӰѼƦb�o�̨S���γ~         
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, 
                    int position, long id) {
            	Item item = itemAdapter.getItem(position);

                // �B�z�O�_��ܤw��ܶ���
                processMenu(item);
                // ���s�]�w�O�ƶ���
                itemAdapter.set(position, item);

                return true;
            }
        }; 
        if (itemLongListener == null){
        	Log.w(TAG, String.format("itemLongListener is null object"));
        } else {
            // ���U��涵�ت�����ť����
            item_list.setOnItemLongClickListener(itemLongListener);
        }

        // �إߪ�����ť����
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
            // ���U������ť����
            show_app_name.setOnLongClickListener(listener); 
        }
        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // �p�G�Q�Ұʪ�Activity����Ǧ^�T�w�����G
    	Log.d(TAG, String.format("onActivityResult: resultCode=%d", resultCode));
        if (resultCode == Activity.RESULT_OK) {
        	Log.d(TAG, String.format("onActivityResult: RESULT_OK", resultCode));
            
        	Item item = (Item) data.getExtras().getSerializable(
                    "com.example.keepbook.Item");

            if (requestCode == 0) {
                // �s�W�O�Ƹ�ƨ��Ʈw
                item = itemDAO.insert(item);
     
                items.add(item);
                itemAdapter.notifyDataSetChanged();
            }
            else if (requestCode == 1) {
                int position = data.getIntExtra("position", -1);
     
                if (position != -1) {
                    // �ק��Ʈw�����O�Ƹ��
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

        // ���o��涵�ت���
        add_item = menu.findItem(R.id.add_item);
        search_item = menu.findItem(R.id.search_item);
        revert_item = menu.findItem(R.id.revert_item);
        share_item = menu.findItem(R.id.share_item);
        delete_item = menu.findItem(R.id.delete_item);
     
        // �]�w��涵��
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
        	// �إ߱Ұʥt�@��Activity����ݭn��Intent����
            Intent intent = new Intent(".ItemActivity.ADD_ITEM");
            // �I�s�ustartActivityForResult�v�A�ĤG�ӰѼơu0�v�ثe�S���ϥ�
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
            // �S�����
            if (selectedCount == 0) {
                break;
            }
 
            // �إ߻P��ܸ߰ݬO�_�R������ܮ�
            AlertDialog.Builder d = new AlertDialog.Builder(this);
            String message = getString(R.string.delete_item);
            d.setTitle(R.string.delete)
                    .setMessage(String.format(message, selectedCount));
            d.setPositiveButton(android.R.string.yes,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // �R���Ҧ��w�Ŀ諸����
                            int index = itemAdapter.getCount() - 1;
 
                            while (index > -1) {
                                Item item = itemAdapter.get(index);
 
                                if (item.isSelected()) {
                                    itemAdapter.remove(item);
                                    // �R����Ʈw�����O�Ƹ��
                                    itemDAO.delete(item.getId());
                                }
 
                                index--;
                            }
 
                            // �q����Ƨ���
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

 // �B�z�O�_��ܤw��ܶ���
    private void processMenu(Item item) {
        // �p�G�ݭn�]�w�O�ƶ���
        if (item != null) {
            // �]�w�w�Ŀ諸���A
            item.setSelected(!item.isSelected());
     
            // �p��w�Ŀ�ƶq
            if (item.isSelected()) {
                selectedCount++;
            }
            else {
                selectedCount--;
            }
        }
     
        // �ھڿ�ܪ����p�A�]�w�O�_��ܿ�涵��
        add_item.setVisible(selectedCount == 0);
        search_item.setVisible(selectedCount == 0);
        revert_item.setVisible(selectedCount > 0);
        share_item.setVisible(selectedCount > 0);
        delete_item.setVisible(selectedCount > 0);
    }
    
    // �I�����ε{���W�٤����I�s����k
    public void aboutApp(View view) {
        // �إ߱Ұʥt�@��Activity����ݭn��Intent����
        // �غc�����Ĥ@�ӰѼơG�uthis�v
        // �غc�����ĤG�ӰѼơG�uActivity�������O�W��.class�v
        Intent intent = new Intent(this, AboutActivity.class);
        // �I�s�ustartActivity�v�A�ѼƬ��@�ӫإߦn��Intent����
        // �o��ԭz����H��A�p�G�S��������~�A�N�|�Ұʫ��w������
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
