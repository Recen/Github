package activity;



import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

import com.recen.sbbs.R;

public class preference extends PreferenceActivity {
	private CheckBoxPreference receiveBox,soundBox,vibrationBox;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);  
		
		receiveBox = (CheckBoxPreference)findPreference("receiveMessage");
		soundBox = (CheckBoxPreference)findPreference("notification_sound");
		vibrationBox = (CheckBoxPreference)findPreference("notification_vibration");
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		// TODO Auto-generated method stub
		return false;
	}
	
	}

