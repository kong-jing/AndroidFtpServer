package com.example.ftpserver;

import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpFile;
import org.apache.ftpserver.impl.FileObserver;
import org.apache.ftpserver.impl.FtpIoSession;
import org.apache.ftpserver.impl.ServerFtpStatistics;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.IBinder;

public class FtpService extends Service
{
	private static final String TAG = "FtpService";
	static FtpServer server = null;
	static String port;
	
	@SuppressLint("SdCardPath")
	private static String dirname;  
	private static String filename;
	
	@Override
	public void onCreate()
	{
		SharedPreferences sharedPreferences= getSharedPreferences("ftpserver", Activity.MODE_PRIVATE); 
		
		//port    = sharedPreferences.getString("port", "2121");
		port = "2121";
		//dirname = sharedPreferences.getString("dirname", Environment.getExternalStorageDirectory().getPath() + "/ftp");
		dirname = Environment.getExternalStorageDirectory().getPath() + "/ftp";
		//System.out.println("dirname = " + dirname);
		filename = dirname + "/myusers.properties";
				
		/* Create Ftp Server */
    	FtpServerFactory serverFactory = new FtpServerFactory();
    	ListenerFactory factory = new ListenerFactory();

		/**设置文件上传的回调*/
		ServerFtpStatistics mServerFtpStatistics =
				(ServerFtpStatistics) serverFactory.serverContext.getFtpStatistics();
		mServerFtpStatistics.setFileObserver(new FileObserver() {
			@Override public void notifyUpload(FtpIoSession ftpIoSession, FtpFile ftpFile, long l) {//上传文件 回调信息
				File physicalFile= (File) ftpFile.getPhysicalFile();
				if(physicalFile.exists()){
					long physicalFileSize=physicalFile.length();
					Log.e(TAG, "notifyUpload: " + physicalFile.getAbsolutePath() + " size = " + physicalFileSize);
				}
			}

			@Override public void notifyDownload(FtpIoSession ftpIoSession, FtpFile ftpFile, long l) {

			}

			@Override public void notifyDelete(FtpIoSession ftpIoSession, FtpFile ftpFile) {

			}

			@Override public void notifyMkdir(FtpIoSession ftpIoSession, FtpFile ftpFile) {

			}

			@Override public void notifyRmdir(FtpIoSession ftpIoSession, FtpFile ftpFile) {

			}
		});
    	 // set the port of the listener
        factory.setPort(Integer.parseInt(port));

        // without SSL configuration
        /*// define SSL configuration
        SslConfigurationFactory ssl = new SslConfigurationFactory();
        ssl.setKeystoreFile(new File("src/test/resources/ftpserver.jks"));
        ssl.setKeystorePassword("password");

        // set the SSL configuration for the listener
        factory.setSslConfiguration(ssl.createSslConfiguration());
        factory.setImplicitSsl(true);*/

        // replace the default listener 
        serverFactory.addListener("default", factory.createListener());
        
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
                 
        InputStream is = getResources().openRawResource(R.raw.myusers);
        
        File file = new File(filename);
        
        File destfile = new File(dirname);
        if (!destfile.exists()) {
        	destfile.mkdirs();
        }
        
        inputstreamtofile(is, file);
        userManagerFactory.setFile(file);
        
        serverFactory.setUserManager(userManagerFactory.createUserManager());

        // start the server
        server = serverFactory.createServer(); 
        
        try {
			server.start();
		} catch (FtpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		} 
        
	}
	
	public void inputstreamtofile(InputStream ins, File file){
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		try {
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ins.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}