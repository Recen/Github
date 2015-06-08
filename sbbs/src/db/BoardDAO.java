package db;

import java.util.ArrayList;
import java.util.List;

import Model.Board;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class BoardDAO {

	private static final String TAG = "BOARDDAO";
	private SQLiteOpenHelper mSQLiteOpenHelper;
	
	public BoardDAO(Context context){
		mSQLiteOpenHelper = SBBSDataBase.getInstance(context).getmDataBaseHelper();
	}
	
	public long insertBoard(Board board,int sections){
		SQLiteDatabase db = mSQLiteOpenHelper.getWritableDatabase();
		return db.insert(BoardTable.TABLE_NAME, null, board2ContentValue(board, sections));
	}
	
	public int insertBoard(List<Board> list,int sections){
		int result = 0;
		for(Board board:list){
			long id = insertBoard(board,sections);
			if(-1 == id){
				Log.e(TAG, "cannot insert board "+board.toString());
			}else{
				++result;
			}
		}
		return result;
	}
	
	public int insertAllBoard(List<List<Board>> list){
		int result = 0;
		for(int i = 0,len = list.size();i < len;i++){
			int num = insertBoard(list.get(i),i);
			result += num;
		}
		return result;
	}
	public int deleteBoard(Board board){
		SQLiteDatabase db = mSQLiteOpenHelper.getWritableDatabase();
		String where = BoardTable.BOARD_ID+"="+board.getId();
		return db.delete(BoardTable.TABLE_NAME, where, null);
	}
	
	public int deleteList(int sections){
		SQLiteDatabase db = mSQLiteOpenHelper.getWritableDatabase();
		String where = BoardTable.BOARD_FOLDER+"="+sections;
		return db.delete(BoardTable.TABLE_NAME, where, null);
	}
	
	public int deleteAllList(int maxSection){
		int result = 0;
		for(int i = 0;i < maxSection;i++){
			int id = deleteList(i);
			result += id;
		}
		return result;
	}
	public int updateBoard(Board board,int sections){
		SQLiteDatabase db = mSQLiteOpenHelper.getWritableDatabase();
		String where = BoardTable.BOARD_ID+"="+board.getId();
		return db.update(BoardTable.TABLE_NAME, board2ContentValue(board, sections), where, null);
	}
	
	public int updateBoard(List<Board> list,int sections){
		int result = 0;
		for(Board board:list){
			int id = updateBoard(board,sections);
			if(id <= 0){
				Log.e(TAG, "update board error:"+board.toString());
			}else{
				++result;
			}
		}
		return result;
	}
	
	public List<Board> fetchBoard(int sections){
		List<Board> boardList = new ArrayList<Board>();
		String where = BoardTable.BOARD_FOLDER+"="+sections;
		SQLiteDatabase db = mSQLiteOpenHelper.getReadableDatabase();
		final Cursor cursor = db.query(BoardTable.TABLE_NAME, null, where, null, null, null, null);
		while(cursor.moveToNext()){
			Board board = mRowMapper.mapRow(cursor, 1);
			boardList.add(board);
		}
		cursor.close();
		db.close();
		return boardList;
	}
	
	public List<List<Board>> fetchAllBoard(int maxSection){
		List<List<Board>> allBoards = new ArrayList<List<Board>>();
		for(int i = 0 ;i < maxSection;i++){
			List<Board> list = fetchBoard(i);
			if(null != list && list.size() != 0){
				allBoards.add(list);
			}
		}
		return allBoards;
	}
	private ContentValues board2ContentValue(Board board,int sections){
		final ContentValues v = new ContentValues();
		v.put(BoardTable.BOARD_ID, board.getId());
		v.put(BoardTable.BOARD_NAME, board.getTitle());
		v.put(BoardTable.BOARD_FOLDER, sections);
		return v;
	}
	
	public interface RowMapper<T> {
		public T mapRow(Cursor cursor,int rowNum);
	}
	
	private RowMapper<Board> mRowMapper = new RowMapper<Board>() {

		@Override
		public Board mapRow(Cursor cursor, int rowNum) {
			return BoardTable.parseBoard(cursor);
		}
	};
}
