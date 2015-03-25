package com.example.keepbook;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 




import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.util.Log;
 
// ��ƥ\�����O
public class ItemDAO {
	protected static final String TAG = "KofuTest_ItemDAO";
	
	// ���W��    
    public static final String TABLE_NAME = "item";
 
    // �s��������W�١A�T�w����
    public static final String KEY_ID = "_id";
 
    // �䥦������W��
    public static final String DATETIME_COLUMN = "datetime";
    public static final String COLOR_COLUMN = "color";
    public static final String DIRECT_COLUMN = "direct";
    public static final String TITLE_COLUMN = "title";
    public static final String CONTENT_COLUMN = "content";
    public static final String FILENAME_COLUMN = "filename";
    public static final String CLEAR_COLUMN = "clear";
    public static final String LATITUDE_COLUMN = "latitude";
    public static final String LONGITUDE_COLUMN = "longitude";
    public static final String LASTMODIFY_COLUMN = "lastmodify";
 
    // �ϥΤW���ŧi���ܼƫإߪ�檺SQL���O
    public static final String CREATE_TABLE = 
            "CREATE TABLE " + TABLE_NAME + " (" + 
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DATETIME_COLUMN + " INTEGER NOT NULL, " +
            COLOR_COLUMN + " INTEGER NOT NULL, " +
            DIRECT_COLUMN + " DIRECT NOT NULL, " +
            TITLE_COLUMN + " TEXT NOT NULL, " +
            CONTENT_COLUMN + " TEXT NOT NULL, " +
            FILENAME_COLUMN + " TEXT, " +
            CLEAR_COLUMN + " INTEGER, " +
            LATITUDE_COLUMN + " REAL, " + 
            LONGITUDE_COLUMN + " REAL, " + 
            LASTMODIFY_COLUMN + " INTEGER)";
 
    // ��Ʈw����    
    private SQLiteDatabase db;
 
    // �غc�l�A�@�몺���γ����ݭn�ק�
    public ItemDAO(Context context) {
        db = MyDBHelper.getDatabase(context);
    }
 
    // ������Ʈw�A�@�몺���γ����ݭn�ק�
    public void close() {
        db.close();
    }
 
    // �s�W�Ѽƫ��w������
    public Item insert(Item item) {
        // �إ߷ǳƷs�W��ƪ�ContentValues����
        ContentValues cv = new ContentValues();     
 
        // �[�JContentValues����]�˪��s�W���
        // �Ĥ@�ӰѼƬO���W�١A �ĤG�ӰѼƬO��쪺���
        cv.put(DATETIME_COLUMN, item.getDatetime());
        cv.put(COLOR_COLUMN, item.getColor().parseColor());
        cv.put(DIRECT_COLUMN, item.getDirect());
        cv.put(TITLE_COLUMN, item.getTitle());
        cv.put(CONTENT_COLUMN, item.getContent());
        cv.put(CLEAR_COLUMN, item.getClear());
        cv.put(FILENAME_COLUMN, item.getFileName());
        cv.put(LATITUDE_COLUMN, item.getLatitude());
        cv.put(LONGITUDE_COLUMN, item.getLongitude());
        cv.put(LASTMODIFY_COLUMN, item.getLastModify());
 
        // �s�W�@����ƨè��o�s��
        // �Ĥ@�ӰѼƬO���W��
        // �ĤG�ӰѼƬO�S�����w���Ȫ��w�]��
        // �ĤT�ӰѼƬO�]�˷s�W��ƪ�ContentValues����
        long id = db.insert(TABLE_NAME, null, cv);
 
        // �]�w�s��
        item.setId(id);
        // �^�ǵ��G
        return item;
    }
 
    // �ק�Ѽƫ��w������
    public boolean update(Item item) {
        // �إ߷ǳƭק��ƪ�ContentValues����
        ContentValues cv = new ContentValues();
 
        // �[�JContentValues����]�˪��ק���
        // �Ĥ@�ӰѼƬO���W�١A �ĤG�ӰѼƬO��쪺���        
        cv.put(DATETIME_COLUMN, item.getDatetime());
        cv.put(COLOR_COLUMN, item.getColor().parseColor());
        cv.put(DIRECT_COLUMN, item.getDirect());
        cv.put(TITLE_COLUMN, item.getTitle());
        cv.put(CONTENT_COLUMN, item.getContent());
        cv.put(CLEAR_COLUMN, item.getClear());
        cv.put(FILENAME_COLUMN, item.getFileName());
        cv.put(LATITUDE_COLUMN, item.getLatitude());
        cv.put(LONGITUDE_COLUMN, item.getLongitude());
        cv.put(LASTMODIFY_COLUMN, item.getLastModify());
 
        // �]�w�ק��ƪ����󬰽s��
        // �榡���u���W�١׸�ơv
        String where = KEY_ID + "=" + item.getId();
 
        // ����ק��ƨæ^�ǭק諸��Ƽƶq�O�_���\
        return db.update(TABLE_NAME, cv, where, null) > 0;         
    }
 
    // �R���Ѽƫ��w�s�������
    public boolean delete(long id){
        // �]�w���󬰽s���A�榡���u���W��=��ơv
        String where = KEY_ID + "=" + id;
        // �R�����w�s����ƨæ^�ǧR���O�_���\
        return db.delete(TABLE_NAME, where , null) > 0;
    }
 
    // Ū���Ҧ��O�Ƹ��
    public List<Item> getAll() {
        List<Item> result = new ArrayList<>();
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);
 
        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }
 
        cursor.close();
        return result;
    }
 
    // ���o���w�s������ƪ���
    public Item get(long id) {
        // �ǳƦ^�ǵ��G�Ϊ�����
        Item item = null;
        // �ϥνs�����d�߱���
        String where = KEY_ID + "=" + id;
        // ����d��
        Cursor result = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);
 
        // �p�G���d�ߵ��G
        if (result.moveToFirst()) {
            // Ū���]�ˤ@����ƪ�����
            item = getRecord(result);
        }
 
        // ����Cursor����
        result.close();
        // �^�ǵ��G
        return item;
    }
 
    // ��Cursor�ثe����ƥ]�ˬ�����
    public Item getRecord(Cursor cursor) {
        // �ǳƦ^�ǵ��G�Ϊ�����
        Item result = new Item();
 
        result.setId(cursor.getLong(0));
        result.setDatetime(cursor.getLong(1));
        result.setColor(ItemActivity.getColors(cursor.getInt(2)));
        result.setDirect(cursor.getInt(3));
        result.setTitle(cursor.getString(4));
        result.setContent(cursor.getString(5));
        result.setClear(cursor.getInt(6));
        result.setFileName(cursor.getString(7));
        result.setLatitude(cursor.getDouble(8));
        result.setLongitude(cursor.getDouble(9));
        result.setLastModify(cursor.getLong(10));
 
        // �^�ǵ��G
        return result;
    }
 
    // ���o��Ƽƶq
    public int getCount() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
 
        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
 
        return result;
    }
 
    // �إ߽d�Ҹ��
    public void sample() {
		Log.d(TAG, String.format("@@## sample(): create some sample item"));
        Item item = new Item(0, new Date().getTime(), Colors.BLUE, 0, "����", "���I��k�W�����[���s�W�O��", 0, "", 25.04719, 121.516981, 0);
 
        insert(item);
    }
 
}