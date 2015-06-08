package activity;


import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import utli.MyApplication;


import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.recen.sbbs.R;


public class recommendPage extends Fragment {
	private static final String TAG = "recommendPage";
	Resources resources;
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentsList;
    private ImageView ivBottomLine;
    private TextView tvTabHot,tvTabCampus;
    private XmlResourceParser green,balck;
    private ColorStateList csl;
    private int currIndex = 0;
    private int bottomLineWidth;
    private int offset = 0;
    private int position_one;
   
    public final static int num = 2 ; 
    Fragment home1;
    Fragment home2;
    Fragment home3;
    
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.recommend, null);
		TranslateAnimation animation;
		resources = getResources();
		InitWidth(view);
        InitTextView(view);
        InitViewPager(view);
		if(savedInstanceState ==null){
			Fragment fragment = (Fragment)getChildFragmentManager().findFragmentByTag(0+"");// getActivity().getSupportFragmentManager().findFragmentByTag(index+""); 
			if(fragment==null){
				
			}
		}
		 Log.i(TAG, "onCreateView");
		if(currIndex == 0){
			animation = new TranslateAnimation(position_one, offset, 0, 0);
		}else {
			animation = new TranslateAnimation(offset, position_one, 0, 0);
		}
        
        animation.setFillAfter(true);
        animation.setDuration(300);
        ivBottomLine.startAnimation(animation);
		return view;
	}
	
	 private void InitTextView(View parentView) {
	        tvTabHot = (TextView) parentView.findViewById(R.id.tv_tab_1);
	        tvTabCampus = (TextView) parentView.findViewById(R.id.tv_tab_2);
	        if(currIndex == 0){
	        	tvTabHot.setTextColor(Color.rgb(33, 171, 56));
			}else {
				tvTabCampus.setTextColor(Color.rgb(33, 171, 56));
			}
	       
	        tvTabHot.setOnClickListener(new MyOnClickListener(0));
	        tvTabCampus.setOnClickListener(new MyOnClickListener(1));	 
	    }

	    private void InitViewPager(View parentView) {
	        mPager = (ViewPager) parentView.findViewById(R.id.vPager);
	        fragmentsList = new ArrayList<Fragment>();

	        
	        home1 = new hotspot_fragment();
	        home2 = new campus_fragment();

	        fragmentsList.add(home1);
	        fragmentsList.add(home2);
	       
	        Log.v(TAG, "InitViewPager");
	        mPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentsList));
	        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	        mPager.setCurrentItem(currIndex);
	        
	    }

	    private void InitWidth(View parentView) {
	        ivBottomLine = (ImageView) parentView.findViewById(R.id.iv_bottom_line);
	        bottomLineWidth = ivBottomLine.getLayoutParams().width;
	        DisplayMetrics dm = new DisplayMetrics();
	        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
	        int screenW = dm.widthPixels;
	        offset = (int) ((screenW / num - bottomLineWidth) / 2);
	        int avg = (int) (screenW / num);
	        position_one = avg + offset;
	       	        
	    }

	    public class MyOnClickListener implements View.OnClickListener {
	        private int index = 0;

	        public MyOnClickListener(int i) {
	            index = i;
	        }

	        @Override
	        public void onClick(View v) {
	            mPager.setCurrentItem(index);
	        }
	    };

	    public class MyOnPageChangeListener implements OnPageChangeListener {

	        @Override
	        public void onPageSelected(int arg0) {
	            Animation animation = null;
	            switch (arg0) {
	            case 0:
	                if (currIndex == 1) {
	                    animation = new TranslateAnimation(position_one, offset, 0, 0);
	                    tvTabCampus.setTextColor(Color.rgb(0, 0, 0));
	                    tvTabHot.setTextColor(Color.rgb(33, 171, 56));
	                    animation.setFillAfter(true);
	    	            animation.setDuration(300);
	                    ivBottomLine.startAnimation(animation);
	                   
	                }           
	                break;
	            case 1:
	                if (currIndex == 0) {
	                    animation = new TranslateAnimation(offset, position_one, 0, 0);
	                    animation.setFillAfter(true);
	    	            animation.setDuration(300);
	                    ivBottomLine.startAnimation(animation);
	                    tvTabHot.setTextColor(Color.rgb(0, 0, 0));
	                    tvTabCampus.setTextColor(Color.rgb(33, 171, 56));
	                    
	                }       
	            }
	            currIndex = arg0;
	            
//	            ivBottomLine.startAnimation(animation);
	        }

	        @Override
	        public void onPageScrolled(int arg0, float arg1, int arg2) {
	        }

	        @Override
	        public void onPageScrollStateChanged(int arg0) {
	        }
	    }
	
	
}
