package utli;

import java.util.ArrayList;
import java.util.List;

import http.HttpClient;
import http.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class BBSOperator {
	private HttpClient mClient;
	private static BBSOperator bbsOP = null;
	private static final String TAG = "BBSOperator";
	
	private BBSOperator() {
		mClient = new HttpClient();
	}
	
	public static BBSOperator getInstance(){
		if(null == bbsOP){
			bbsOP = new BBSOperator();
		}
		return bbsOP;
	}
	
	public List<Topic> getTopicList(String url) throws HttpException {
		return Topic.parseTopicList(getJsonSuccess(url));
	}
	
	public JSONObject getJsonSuccess(String url) throws HttpException {
		boolean success = false;
		JSONObject obj = getJson(url);
		try {
			success = obj.getBoolean("success");

			if (success) {
				return obj;
			} else {
				String error = obj.getString("error");
				throw new HttpException(error);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			throw new HttpException(e.getMessage(), e);
		}
	}
	
	public JSONObject getJson(String url) throws HttpException {
		return get(url).asJSONObject();
	}
	public Response get(String url) throws HttpException {
		return mClient.httpRequest(url);
	}
	
	public List<List<Board>> getAllBoards(String url) throws HttpException {
		List<List<Board>> allBoardList = new ArrayList<List<Board>>();
		
		try {
			JSONObject obj = getJsonSuccess(url);
			JSONArray groupArray = obj.getJSONArray("boards");
			for (int i = 0, len = groupArray.length(); i < len; i++) {
				JSONObject groupJson = groupArray.getJSONObject(i);
				JSONArray boardsJson = groupJson.getJSONArray("boards");
				List<Board> list = Board.parseBoardArray(boardsJson, false);
				
				Log.i(TAG, " dada--"+list.get(0).getTitle());
				//Log.i(TAG, " dada--"+list.get(1).getId());
				allBoardList.add(list);
			}
		} catch (JSONException e) {
			throw new HttpException(e.getMessage(), e);
		}
		return allBoardList;
	}
}
