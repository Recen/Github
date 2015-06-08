package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SBBSDataBase  {

	public static final int VERSION = 1;
	public static final String DATEBASE_NAME = "sbbs.db";
	private DataBaseHelper mDataBaseHelper;
	private static SBBSDataBase sInstance = null;
	
	private static class DataBaseHelper extends SQLiteOpenHelper{

		public DataBaseHelper(Context context) {
			super(context, DATEBASE_NAME, null, VERSION);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			createAllTables(db);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			dropAllTables(db);
			createAllTables(db);
		}
		
	}
	
	private SBBSDataBase(Context context){
		this.setmDataBaseHelper(new DataBaseHelper(context));
	}
	
	public static synchronized SBBSDataBase getInstance(Context context){
		if(null == sInstance){
			sInstance = new SBBSDataBase(context);
		}
		return sInstance;
	}
	
	private static void createAllTables(SQLiteDatabase db) {
		db.execSQL(TopicTable.CREATE_TABLE);
		db.execSQL(BoardTable.CREATE_TABLE);
		db.execSQL(MailTable.CREATE_TABLE);
	}

	private static void dropAllTables(SQLiteDatabase db){
		db.execSQL(TopicTable.DROP_TABLE);
		db.execSQL(BoardTable.DROP_TABLE);
//		db.execSQL(MailTable.DROP_TABLE);
	}
	
	public void setmDataBaseHelper(DataBaseHelper mDataBaseHelper) {
		this.mDataBaseHelper = mDataBaseHelper;
	}

	public DataBaseHelper getmDataBaseHelper() {
		return mDataBaseHelper;
	}

	public SQLiteDatabase getDb(boolean writeable){
		if(writeable){
			return mDataBaseHelper.getWritableDatabase();
		}else{
			return mDataBaseHelper.getReadableDatabase();
		}
	}
	
	public void close(){
		if(null != sInstance){
			mDataBaseHelper.close();
			sInstance = null;
		}
	}
}
