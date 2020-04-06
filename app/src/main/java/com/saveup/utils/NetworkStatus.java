package com.saveup.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * This class is used to get the network status when it is enable/disable.
 */
public class NetworkStatus extends BroadcastReceiver
{
	public Context mContext;
	Context appContext;
	private String message;

	@Override
	public void onReceive(Context context, Intent intent)
	{
		mContext = context;
		try
		{

		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static boolean isOnline(Context mContext2)
	{
		if (mContext2 != null)
		{
			ConnectivityManager connectivity = (ConnectivityManager) mContext2.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null)
			{
				NetworkInfo[] info = connectivity.getAllNetworkInfo();
				if (info != null)
					for (int i = 0; i < info.length; i++)
						if (info[i].getState() == NetworkInfo.State.CONNECTED)
						{
							return true;
						}
			}
		}
		return false;
	}

	public static boolean getConnectivityStatus(Context context)
	{
		boolean conn = false;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork)
		{
			if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
			{
                conn = activeNetwork.isConnected();
			}
		}
		else
		{
			conn = false;
		}
		return conn;
	}


}
