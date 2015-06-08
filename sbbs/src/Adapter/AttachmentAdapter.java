package Adapter;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import Model.Attachment;
import Model.TaskResult;
import Task.GenericTask;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.recen.sbbs.R;

public class AttachmentAdapter extends BaseAdapter {
	private List<Attachment> attList;
	private Context context;
	private Bitmap bitmap;
	private LayoutInflater inflater;
	private View view;
	private static final String TAG = "AttachmentAdapter";

	public AttachmentAdapter(Context context) {
		this.context = context;
		attList = new ArrayList<Attachment>();
	}

	@Override
	public int getCount() {
		return attList.size();
	}

	@Override
	public Object getItem(int position) {
		return attList.get(position);
	}
	
	public String getAttachmentUrl(int position){
		Attachment att = (Attachment)getItem(position);
		return att.getUrl();
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	public void refresh(List<Attachment> list) {
		this.attList = list;
		this.notifyDataSetChanged();
	}

	public void refresh() {
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		if (null == convertView) {
			convertView = inflater.inflate(R.layout.att_item, null);
			view = convertView;
			holder = new ViewHolder();
			holder.nameView = (TextView) convertView
					.findViewById(R.id.att_name);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.att_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Attachment att = (Attachment) getItem(position);
		if (att.isImage()) {

//			imageLoader.displayImage(att.getUrl(),
//					holder.imageView);
//			imageLoader.displayImage(att.getUrl(), holder.imageView, options);
			GenericTask getImage = new GetBitmapTask();
			getImage.execute(att.getUrl());
//			holder.imageView.setImageBitmap(bitmap);
//			holder.imageView.setVisibility(View.VISIBLE);
			
		} else {
			Log.i(TAG, "isNotImage");
			holder.nameView.setText(att.getFileName());
			holder.nameView.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	
	
	private static class ViewHolder {
		TextView nameView;
		ImageView imageView;
	}
	
	private Bitmap getImage(String urlpath)
            throws Exception {
	 	URL url = new URL(urlpath);
	    InputStream is= url.openStream();
		bitmap = BitmapFactory.decodeStream(is);
	    return bitmap;
	}
	
	private class GetBitmapTask extends GenericTask{
		ImageView imageView = (ImageView) view.findViewById(R.id.att_image);
		/* (non-Javadoc)
		 * @see Task.GenericTask#onPostExecute(Model.TaskResult)
		 */
		@Override
		protected void onPostExecute(TaskResult result) {
			// TODO Auto-generated method stub
			if (result == TaskResult.OK) {
				imageView.setImageBitmap(bitmap);
				imageView.setVisibility(View.VISIBLE);
			}; 
		
			
		}

		@Override
		protected TaskResult _doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				bitmap = getImage(params[0]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (bitmap != null) {
				return TaskResult.OK;
			} else {
				return TaskResult.Failed;
			}
		}
		
	

		
	}

}
