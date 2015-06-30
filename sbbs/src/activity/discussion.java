 package activity;

import java.util.ArrayList;
import java.util.List;

import utli.BBSOperator;
import utli.HttpException;

import Adapter.SectionAdapter;
import Model.Board;
import Model.SBBSURLS;
import Model.TaskResult;
import Task.GenericTask;
import Task.TaskAdapter;
import Task.TaskListener;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.recen.sbbs.R;

public class discussion extends Fragment{
	
	private static final String TAG= "discussion";
	private GridView myGridView;
	private DrawerLayout mDrawerLayout = null;
	private ListView secListView;
	private String boardUrl;
	private List<List<Board>> boardList;
	private List<Board> secList;
	private GenericTask mRetrieveTask;
	private SectionAdapter secAdapter;
	private int boardPosition;
	private boolean forceLoad = false;
	private static final int MODE = 2;
private TaskListener mRetrieveHotTaskListener = new TaskAdapter() {
		//任务监听器
		private ProgressDialog pdialog;
		/* (non-Javadoc)
		 * @see Task.TaskAdapter#onPreExecute(Task.GenericTask)
		 */
		@Override
		public void onPreExecute(GenericTask task) {
			// TODO Auto-generated method stub
			super.onPreExecute(task);
			pdialog = new ProgressDialog(getActivity());
			pdialog.setMessage(getResources().getString(R.string.loading));
			pdialog.show();//显示正在加载的对话框
			pdialog.setCanceledOnTouchOutside(false);
		}

		/* (non-Javadoc)
		 * @see Task.TaskAdapter#onPostExecute(Task.GenericTask, utli.TaskResult)
		 */
		@Override
		public void onPostExecute(GenericTask task, TaskResult result) {
			// TODO Auto-generated method stub
			super.onPostExecute(task, result);
			pdialog.dismiss();
			processResult(result);//处理部分
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "getContenTaskListener";
		}
		
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.discussion, null);
		
		mDrawerLayout = (DrawerLayout)view.findViewById(R.id.drawer_layout);
		myGridView = (GridView)view.findViewById(R.id.disGridView);
		secListView = (ListView)view.findViewById(R.id.secList);
		secAdapter = new SectionAdapter(getActivity());
		secListView.setAdapter(secAdapter);
		boardUrl = SBBSURLS.BOARD_SECTIONS;
		//界面的gridview填充是用下面的数组写死的
		final String[]  section = {"本站系统","东南大学","电脑技术","学术科学","艺术文化","乡情校谊",
							 "休闲娱乐","知性感性","人文信息","体坛风暴","校务信箱","社团群体"};
		//这里就直接和Adapter联系
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.disgradview_item, section);
		myGridView.setAdapter(adapter);
		bindListener();
		
		return view;
	}	
	
	private void bindListener(){
		
		myGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
					//Toast.makeText(getActivity(), section[position]+"被点击", 20).show();
				 	doRetrieve();//点到那个版块 就在这里执行
				 	boardPosition = position;
				 	mDrawerLayout.openDrawer(Gravity.RIGHT);//这里是抽屉效果的实现，点一下滑出来					
			}
		});
		
		secListView.setOnItemClickListener(new OnItemClickListener() {
			//这部分绑定的就是滑出来的那个listview里的内容
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Board board = secList.get(position);
			
				Intent intent = new Intent(getActivity(),TopicList.class);
				intent.putExtra("MODE", MODE);
				intent.putExtra("boardID", board.getId());
				startActivity(intent);				
			}
		});
	}
	
	
	private void doRetrieve() {
		if (null != mRetrieveTask
				&& GenericTask.Status.RUNNING == mRetrieveTask.getStatus()) {
			return;
		}
		mRetrieveTask = new RetrieveSecTask();
		Log.i(TAG, "abilitdsadasdasdasdy");
		mRetrieveTask.setListener(mRetrieveHotTaskListener);
		mRetrieveTask.execute(boardUrl);//boardUrl是大板块的地址

		Log.i(TAG, TAG + "-->doRetrieve");
	}
	
	private class RetrieveSecTask extends GenericTask{
		@Override
		protected TaskResult _doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				Log.i(TAG, params[0]);
				boardList = BBSOperator.getInstance().getAllBoards(params[0]);//这是版块的内容，你看下面的调试信息就知道具体是什么了
				Log.i(TAG, "---->"+boardList.get(2).get(2).getTitle());//这是调试
				} catch (HttpException e) {
					// TODO Auto-gsenerated catch block
					e.printStackTrace();
					e.getMessage();
					return TaskResult.Failed;
				}
			if (null == boardList || boardList.size() == 0) {
				return TaskResult.NO_DATA;
			}
			return TaskResult.OK;
			}
		}
	private void processResult(TaskResult result) {
		if (TaskResult.Failed == result) {
//			Toast.makeText(MyApplication.mContext, errorCause,
//					Toast.LENGTH_SHORT).show();
			Log.i(TAG, "Failed");
			return;
		}
		if (result == TaskResult.NO_DATA) {
//			Toast.makeText(MyApplication.mContext, R.string.hot_no_data,
//					Toast.LENGTH_SHORT).show();
			Log.i(TAG, "No Data");
			return;
		}
		forceLoad = false;
		draw();//这个和hotfragment一样了
		//goTop();

	}	
	
	private void draw() {
		secList = boardList.get(boardPosition);//boardPosition就是点击的是哪个位置，例如第一个是本站系统 0
		secAdapter.refresh(secList);
	}
}
