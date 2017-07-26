package com.example.ftpserver;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class EmbeddingFtpServerActivity extends Activity { 
	static TextView server_status;
	static TextView server_port;
	static TextView file_path;
	
	TextView server_ip;
	static String port;
	String homepath, filename;
	
	static boolean server_toggle = true;
	
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.embedding_ftp_server);

		//SharedPreferences sharedPreferences= getSharedPreferences("ftpserver", Activity.MODE_PRIVATE);
    //
		//server_status = (TextView) findViewById(R.id.server_status);
		//server_status.setText(getString(R.string.server_opening));
    //
		//server_ip = (TextView) findViewById(R.id.server_ip);
		//server_ip.setText(getLocalIpAddress());
    //
		//port = sharedPreferences.getString("port", "2121");
		//server_port = (TextView) findViewById(R.id.server_port);
		//server_port.setText(port);
    //
		//homepath = sharedPreferences.getString("path", Environment.getExternalStorageDirectory().getPath() + "/ftp");
		//filename = homepath + "/myusers.properties";
		//file_path = (TextView) findViewById(R.id.path);
    //
		//File destfile = new File(filename);
    //
		//System.out.println("filename=" + filename);
		//SharedPreferences.Editor editor = sharedPreferences.edit();
    //
		//if(destfile.exists()){
		//	file_path.setText(getHomeDirectory());
		//	editor.putString("path", getHomeDirectory());
		//}else{
		//	editor.putString("path", homepath);
		//	file_path.setText(homepath);
		//}
    //
		//editor.commit();
		//
		mContext = this;
		Intent serviceIntent;
		
		serviceIntent = new Intent(this, FtpService.class);  
        mContext.startService(serviceIntent); 
		
        //server_status.setText(getString(R.string.server_on));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.embedding_ftp_server, menu);
		return true;
	} 
	
	
	@Override 
    public boolean onOptionsItemSelected(MenuItem item) { 
        super.onOptionsItemSelected(item); 
        switch(item.getItemId())
        { 
        case  R.id.menu_settings :
        	  Intent intent = new Intent(EmbeddingFtpServerActivity.this, FtpSettings.class); 
              startActivity(intent);
            break;  
        } 
        return true; 
    }
	
	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			System.out.println("WifiPreference IpAddress" + ex.toString());
		}
		return null;
	}
 
	@SuppressWarnings("deprecation")
	String getHomeDirectory(){ 
		
		String prefix = "ftpserver.user.anonymous.homedirectory=";
		
		System.out.println("filename = " + filename); 
		
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String sBuffer = null;
		DataInputStream dataIO = new DataInputStream(fis);
		String strLine = null;
		try {
			while((strLine = dataIO.readLine()) != null) {
			    //sBuffer.append(strLine + "\n");
				if(strLine.startsWith(prefix))
				{
					sBuffer = strLine.substring(prefix.length());
					System.out.println("sBuffer = " + sBuffer);
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			dataIO.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sBuffer;
	}
	
	 
}
