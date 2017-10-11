package com.example.ftpserver;

import org.apache.ftpserver.ftplet.FtpException;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

public class FtpSettings extends Activity { 
	
	static PrefsFragement pref = null;
	static SharedPreferences sharedPreferences = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		pref = new PrefsFragement();
		
		getFragmentManager().beginTransaction().replace(android.R.id.content, pref).commit();
		
		sharedPreferences = getSharedPreferences("ftpserver", Activity.MODE_PRIVATE); 
	} 
	
	public static PrefsFragement getPref()
	{
		return pref;
	}
	
	public static class PrefsFragement extends PreferenceFragment implements OnPreferenceChangeListener{
		
		EditTextPreference portEditPre;
		EditTextPreference pathEditPre;
		Preference         statusPre;
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.setting);
			
			portEditPre = (EditTextPreference) findPreference("server_port"); 
			pathEditPre = (EditTextPreference) findPreference("file_path"); 
			statusPre   = (Preference) findPreference("enable_server"); 
						
			String port = sharedPreferences.getString("port", "2221");  
			portEditPre.setSummary(port); 
			 
			String path = sharedPreferences.getString("path", Environment.getExternalStorageDirectory().getPath() + "/ftp");
			pathEditPre.setSummary(path);
			
			portEditPre.setOnPreferenceChangeListener(this);
			pathEditPre.setOnPreferenceChangeListener(this);
		}
		
		public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, 
				Preference preference) { 
			
			String key = preference.getKey();
			
			if (key.equals("enable_server")) {
				if(FtpService.server != null && EmbeddingFtpServerActivity.server_toggle)
				{
					statusPre.setSummary(getString(R.string.server_off));
					FtpService.server.stop();
					EmbeddingFtpServerActivity.server_status.setText(getString(R.string.server_off)); 
					EmbeddingFtpServerActivity.server_toggle = false;
				}else if(FtpService.server != null && !EmbeddingFtpServerActivity.server_toggle)
				{
					try {
						FtpService.server.start();
						EmbeddingFtpServerActivity.server_status.setText(getString(R.string.server_on)); 
					} catch (FtpException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					statusPre.setSummary(getString(R.string.server_on));
					EmbeddingFtpServerActivity.server_toggle = true;
				}
			}else if(key.equals("restore_default")){
				
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putString("port", "2121");
				editor.putString("path", Environment.getExternalStorageDirectory().getPath() + "/ftp");
				editor.commit();
				
				statusPre.setSummary(getString(R.string.server_off));
				FtpService.server.stop();
				EmbeddingFtpServerActivity.server_status.setText(getString(R.string.server_off)); 
				EmbeddingFtpServerActivity.server_toggle = false;
				
				portEditPre.setSummary("2121");
				portEditPre.setText("2121");
				EmbeddingFtpServerActivity.server_port.setText("2121");
				
				pathEditPre.setSummary(Environment.getExternalStorageDirectory().getPath() + "/ftp");
				pathEditPre.setText(Environment.getExternalStorageDirectory().getPath() + "/ftp");
				EmbeddingFtpServerActivity.file_path.setText(Environment.getExternalStorageDirectory().getPath() + "/ftp");
			}
			
			return super.onPreferenceTreeClick(preferenceScreen, preference);
			 
		}
		
		public boolean onPreferenceChange(Preference preference, Object newValue)
		{
			String key = preference.getKey();
			String newrefsValue = newValue.toString();  
			
			SharedPreferences.Editor editor = sharedPreferences.edit();
			 
			if(key.equals("server_port")){					
				System.out.println("portEditPre = " + portEditPre.getText() + ", prefsValue = " + newrefsValue);
				portEditPre.setSummary(newrefsValue);
				portEditPre.setText(newrefsValue);
				EmbeddingFtpServerActivity.server_port.setText(newrefsValue);
				
				editor.putString("port", newrefsValue); 
				
			}else if(key.equals("file_path")){ 
				pathEditPre.setSummary(newrefsValue);
				pathEditPre.setText(newrefsValue);
				EmbeddingFtpServerActivity.file_path.setText(newrefsValue);
				
				editor.putString("path", newrefsValue); 
			}
			
			editor.commit(); 
			
			return false; 
		}
	}
}