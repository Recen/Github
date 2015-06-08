package db;

import java.util.ArrayList;
import java.util.List;

import Model.Topic;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class TopicDAO {
	private static final String TAG = "TopicDAO";

	private SQLiteOpenHelper mOpenHelper;
	private Context mContext;
	
	public TopicDAO(Context context){
		this.mContext = context;
		mOpenHelper = SBBSDataBase.getInstance(context).getmDataBaseHelper();
	}
	
	public long insertTopic(Topic topic,int type){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		return db.insert(TopicTable.TABLE_NAME, null, topic2ContentValue(topic,type));
	}
	
	public int insertTopic(List<Topic> topicList,int type){
		int result = 0;
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		for(Topic topic:topicList){
			long id = db.insert(TopicTable.TABLE_NAME, null, topic2ContentValue(topic,type));
			if(-1 != id){
				++result;
			}else{
				Log.e(TAG, "cannot insert the topic "+topic.toString());
			}
		}
		return result;
	}
	
	public int deleteTopic(int id,int type){
		String where = TopicTable.POST_ID+"="+id+" AND "+TopicTable.TOPIC_TYPE+"="+type;
		return mOpenHelper.getWritableDatabase().delete(TopicTable.TABLE_NAME, where, null);
	}
	
	public int deleteTopic(Topic topic,int type){
		return deleteTopic(topic.getId(),type);
	}
	
	public int deleteList(int type){
		String where = TopicTable.TOPIC_TYPE+"="+type;
		return mOpenHelper.getWritableDatabase().delete(TopicTable.TABLE_NAME, where, null);
	}
	public int updateTopic(int id,int type,ContentValues v){
		String where = TopicTable.POST_ID+"="+id+" AND "+TopicTable.TOPIC_TYPE+"="+type;
		return mOpenHelper.getWritableDatabase().update(TopicTable.TABLE_NAME, v, where, null);
	}
	
	public int updateTopic(Topic topic ,int type){
		return updateTopic(topic.getId(),type,topic2ContentValue(topic, type));
	}
	
	public List<Topic> fetchTopics(int type){
		List<Topic> topicList = new ArrayList<Topic>();
		String where = TopicTable.TOPIC_TYPE+"="+type;
		final Cursor cursor = mOpenHelper.getReadableDatabase().query(TopicTable.TABLE_NAME, null, where, null, null, null, null);
		while(cursor.moveToNext()){
			topicList.add(mRowMapper.mapRow(cursor, 1));
		}
		cursor.close();
		return topicList;
	}
	
	private ContentValues topic2ContentValue(Topic topic,int type){
		final ContentValues v = new ContentValues();
		v.put(TopicTable.TITLE, topic.getTitle());
		v.put(TopicTable.AUTHOR, topic.getAuthor());
		v.put(TopicTable.BOARD, topic.getBoardName());
		v.put(TopicTable.POST_TIME, topic.getTime());
		v.put(TopicTable.POST_ID, topic.getId());
		v.put(TopicTable.REPLIES, topic.getReplies());
		v.put(TopicTable.READ, topic.getPopularity());
		v.put(TopicTable.TOPIC_TYPE, type);
		return v;
	}
	
	public interface RowMapper<T> {
		public T mapRow(Cursor cursor,int rowNum); 
	}
	
	private static final RowMapper<Topic> mRowMapper = new RowMapper<Topic>(){

		@Override
		public Topic mapRow(Cursor cursor, int rowNum) {
			Topic topic = TopicTable.parseCursor(cursor);
			return topic;
		}
		
	};
}
