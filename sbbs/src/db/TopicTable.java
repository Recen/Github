package db;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import Model.Topic;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;


public class TopicTable implements BaseColumns {
	private static final String TAG ="TOPIC_TABLE";

	public static final String TITLE = "title";
	public static final String AUTHOR = "author";
	public static final String BOARD = "board";
	public static final String POST_TIME = "post_time";
	public static final String POST_ID = "id";
	public static final String REPLIES = "repliese";
	public static final String READ = "read";
	
	public static final String TOPIC_TYPE = "type";
	public static final String TABLE_NAME = "topic";
	
	public static final int TYPE_HOT = 100;
	public static final int TYPE_CAMPUS = 101;
	public static final int TYPE_0 = 0;
	public static final int TYPE_1 = 1;
	public static final int TYPE_2 = 2;
	public static final int TYPE_3 = 3;
	public static final int TYPE_4 = 4;
	public static final int TYPE_5 = 5;
	public static final int TYPE_6 = 6;
	public static final int TYPE_7 = 7;
	public static final int TYPE_8 = 8;
	public static final int TYPE_9 = 9;
	public static final int TYPE_A = 10;
	public static final int TYPE_B = 11;
	
	
	public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME 
						+"("+_ID+" INTEGER PRIMARY KEY,"+TOPIC_TYPE+" text not null,"+POST_ID+" text not null,"
						+TITLE+" text not null,"+BOARD+" text not null,"+AUTHOR+" text not null,"+POST_TIME+" text not null,"
						+REPLIES+" text not null,"+READ+" text not null)";
	
	public static final String DROP_TABLE = "DROP TABLE "+TABLE_NAME;
	
	public static Topic parseCursor(Cursor cursor){
		if (null == cursor || 0 == cursor.getCount()) {
			Log.w(TAG, "Cann't parse Cursor, bacause cursor is null or empty.");
			return null;
		} else if (-1 == cursor.getPosition()) {
			cursor.moveToFirst();
		}
		Topic topic = new Topic();
		String title = cursor.getString(cursor.getColumnIndex(TITLE));
		String author = cursor.getString(cursor.getColumnIndex(AUTHOR));
		String board = cursor.getString(cursor.getColumnIndex(BOARD));
		long time = cursor.getLong(cursor.getColumnIndex(POST_TIME));
		int id = cursor.getInt(cursor.getColumnIndex(POST_ID));
		int replies = cursor.getInt(cursor.getColumnIndex(REPLIES));
		String read = cursor.getString(cursor.getColumnIndex(READ));
		
		Date date = new Date(time * 1000);
		SimpleDateFormat format = new SimpleDateFormat(
				"MMM dd HH:mm:ss yyyy", Locale.ENGLISH);
		String post_time = format.format(date);
		topic.setTitle(title).setAuthor(author).setBoardName(board).setTime(post_time).
				setId(id).setPopularity(read).setReplies(replies);
		return topic;
	}
}
