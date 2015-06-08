package db;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import Model.Mail;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;


public class MailTable implements BaseColumns {
	private static final String TAG = "MailTable";
	
	public static final String TABLE_NAME = "mail_table";
	public static final String MAIL_ID = "mail_id";
	public static final String SIZE = "mail_size";
	public static final String STATUS = "read_status";
	public static final String AUTHOR = "author";
	public static final String TITLE = "title";
	public static final String TYPE = "type";
	public static final String TIME = "time";
	
	public static final String CREATE_TABLE= "CREATE TABLE "+TABLE_NAME+"("+_ID +" INTEGER PRIMARY KEY,"
									+MAIL_ID+" TEXT NOT NULL,"+SIZE+" TEXT,"+STATUS+" TEXT NOT NULL,"
									+AUTHOR+" TEXT NOT NULL,"+TITLE+" TEXT NOT NULL,"+TYPE+" TEXT NOT NULL,"
									+TIME+" TEXT NOT NULL)";
	public static final String DROP_TABLE = "DROP TABLE "+ TABLE_NAME;
	
	public static Mail parseCursor(Cursor cursor){
		if(null == cursor || 0 == cursor.getCount()){
			Log.w(TAG, "Cann't parse Cursor, bacause cursor is null or empty.");
			return null;
		}else if(-1 == cursor.getPosition()){
			cursor.moveToFirst();
		}
		Mail mail = new Mail();
		String id = cursor.getString(cursor.getColumnIndex(MAIL_ID));
		String status = cursor.getString(cursor.getColumnIndex(STATUS));
		String author = cursor.getString(cursor.getColumnIndex(AUTHOR));
		String title = cursor.getString(cursor.getColumnIndex(TITLE));
		long milliseconds = cursor.getLong(cursor.getColumnIndex(TIME));
		Date date = new Date(milliseconds * 1000);
		SimpleDateFormat format = new SimpleDateFormat(
				"MMM dd HH:mm:ss yyyy", Locale.ENGLISH);
		String time = format.format(date);
		mail.setDate(time);
		mail.setFrom(author);
		mail.setNum(id);
		mail.setTitle(title);
		if("true".equals(status)){
			mail.setUnRead(true);
		}else{
			mail.setUnRead(false);
		}
		return mail;
	}
}
