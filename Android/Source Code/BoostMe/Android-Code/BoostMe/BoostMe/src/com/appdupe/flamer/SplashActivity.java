package com.appdupe.flamer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.slidingmenu.MainActivity;
import com.appdupe.flamer.utility.SessionManager;
import com.appdupe.flamer.R;

public class SplashActivity extends Activity {
	private static boolean mDebugLog = true;
	private static String mDebugTag = "LoginUsingActivityActivity";
	protected boolean _active = true;

	/** The _splash time. */
	protected int _splashTime = 1000;

	void logDebug(String msg) {
		if (mDebugLog) {
			Log.d(mDebugTag, msg);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashactivity);

		// FlurryAgent.logEvent("LoadSplahScreen");

		Thread splashTread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while (_active && (waited < _splashTime)) {
						sleep(100);
						if (_active) {
							waited += 100;
						}
					}
				} catch (InterruptedException e) {
					// do nothing
				} finally {
					try {

						SessionManager mSessionManager = new SessionManager(
								SplashActivity.this);
						logDebug("onCreate mSessionManager " + mSessionManager);
						if (mSessionManager.isLoggedIn()) {
							logDebug("onCreate user is logedind  "
									+ mSessionManager);
							// if (mSession.isOpened())
							// {
							// FlurryAgent.logEvent("SplahScreenCompleted");
							// Bundle dataBundle = new Bundle();
							// dataBundle.putBoolean(CommanConstant.Fromsignup,
							// false);
							// dataBundle.putBoolean(CommanConstant.Fromsplash,true);
							// dataBundle.putBoolean(CommanConstant.FromLogin,
							// false);
							Intent mIntent = new Intent(SplashActivity.this,
									MainActivity.class);
							// mIntent.putExtras(dataBundle);
							startActivity(mIntent);
							finish();
							// }

						} else {
							logDebug("onCreate  user not loged in ");
							// FlurryAgent.logEvent("SplahScreenCompleted");
							Intent mIntent = new Intent(SplashActivity.this,
									LoginUsingFacebook.class);
							// Intent mIntent=new Intent(SplashActivity.this,
							// MainActivity.class);
							startActivity(mIntent);
							finish();
						}

					} catch (Exception e2) {
						// TODO: handle exception
						logDebug("onCreate  Exception " + e2);
						// FlurryAgent.logEvent("SplahScreenCompleted");
						// Intent mIntent=new Intent(SplashActivity.this,
						// LoginUsingActivityActivity.class);
						// startActivity(mIntent);
						// finish();
					}
				}
			}
		};
		splashTread.start();

	}
}
