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
 
// 資料功能類別
public class ItemDAO {
	protected static final String TAG = "KofuTest_ItemDAO";
	
	// 表格名稱    
    public static final String TABLE_NAME = "item";
 
    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";
 
    // 其它表格欄位名稱
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
 
    // 使用上面宣告的變數建立表格的SQL指令
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
 
    // 資料庫物件    
    private SQLiteDatabase db;
 
    // 建構子，一般的應用都不需要修改
    public ItemDAO(Context context) {
        db = MyDBHelper.getDatabase(context);
    }
 
    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }
 
    // 新增參數指定的物件
    public Item insert(Item item) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();     
 
        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
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
 
        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long id = db.insert(TABLE_NAME, null, cv);
 
        // 設定編號
        item.setId(id);
        // 回傳結果
        return item;
    }
 
    // 修改參數指定的物件
    public boolean update(Item item) {
        // 建立準備修改資料的ContentValues物件
        ContentValues cv = new ContentValues();
 
        // 加入ContentValues物件包裝的修改資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料        
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
 
        // 設定修改資料的條件為編號
        // 格式為「欄位名稱＝資料」
        String where = KEY_ID + "=" + item.getId();
 
        // 執行修改資料並回傳修改的資料數量是否成功
        return db.update(TABLE_NAME, cv, where, null) > 0;         
    }
 
    // 刪除參數指定編號的資料
    public boolean delete(long id){
        // 設定條件為編號，格式為「欄位名稱=資料」
        String where = KEY_ID + "=" + id;
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(TABLE_NAME, where , null) > 0;
    }
 
    // 讀取所有記事資料
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
 
    // 取得指定編號的資料物件
    public Item get(long id) {
        // 準備回傳結果用的物件
        Item item = null;
        // 使用編號為查詢條件
        String where = KEY_ID + "=" + id;
        // 執行查詢
        Cursor result = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);
 
        // 如果有查詢結果
        if (result.moveToFirst()) {
            // 讀取包裝一筆資料的物件
            item = getRecord(result);
        }
 
        // 關閉Cursor物件
        result.close();
        // 回傳結果
        return item;
    }
 
    // 把Cursor目前的資料包裝為物件
    public Item getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
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
 
        // 回傳結果
        return result;
    }
 
    // 取得資料數量
    public int getCount() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
 
        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
 
        return result;
    }
 
    // 建立範例資料
    public void sample() {
		Log.d(TAG, String.format("@@## sample(): create some sample item"));
        Item item = new Item(0, new Date().getTime(), Colors.BLUE, 0, "測試", "請點選右上角的加號新增記事", 0, "", 25.04719, 121.516981, 0);
 
        insert(item);
    }
 
}