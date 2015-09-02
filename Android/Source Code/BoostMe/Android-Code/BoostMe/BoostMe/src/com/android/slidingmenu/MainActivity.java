package com.android.slidingmenu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.androidquery.AQuery;
import com.appdupe.androidpushnotifications.ChatActivity;
import com.appdupe.flamer.R;
import com.appdupe.flamer.pojo.ImageDetail;
import com.appdupe.flamer.pojo.LikeMatcheddataForListview;
import com.appdupe.flamer.pojo.LikedMatcheData;
import com.appdupe.flamer.pojo.Likes;
import com.appdupe.flamer.pojo.UploadImage;
import com.appdupe.flamer.utility.AlertDialogManager;
import com.appdupe.flamer.utility.Constant;
import com.appdupe.flamer.utility.ScalingUtilities;
import com.appdupe.flamer.utility.ScalingUtilities.ScalingLogic;
import com.appdupe.flamer.utility.SessionManager;
import com.appdupe.flamer.utility.Ultilities;
import com.appdupe.flamerchat.db.DatabaseHandler;
import com.facebook.Session;
import com.facebook.SessionState;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;

public class MainActivity extends SherlockFragmentActivity implements
		OnClickListener, OnOpenListener {
	// MainLayout mLayout;
	private static final String TAG = "MainActivity";
	// private ListView lvMenu;
	private ListView matcheslistview;
	// private String[] lvMenuItems;
	Button btMenu;
	private Button buttonRightMenu;
	TextView tvTitle;
	private static boolean mDebugLog = true;
	private static String mDebugTag = "MainActivity";
	private Typeface topbartextviewFont;
	private SharedPreferences preferences;
	private Editor editor;
	private Animation anime;

	// SerchUi
	private EditText etSerchFriend;

	// private Button btnSerch;

	void logDebug(String msg) {

		if (mDebugLog) {
			Log.d(mDebugTag, msg);
		}
	}

	void logError(String msg) {

		if (mDebugLog) {
			Log.e(mDebugTag, msg);
		}
	}

	double mLatitude = 0;
	double mLongitude = 0;
	double dLatitude = 0;
	double dLongitude = 0;

	private Session.StatusCallback statusCallback = new SessionStatusCallback();
	private Dialog mdialog;

	private boolean usersignup = false;
	private boolean isProfileclicked = false;
	// private LinearLayout menu_right;
	// private LinearLayout menu_left;
	private ArrayList<LikeMatcheddataForListview> arryList;
	private MatchedDataAdapter adapter;

	private ImageView profileimage;

	private LinearLayout profilelayout, homelayout, messages, settinglayout,
			invitelayout;

	private boolean flagforHome;
	private boolean flagForProfile;
	private boolean flagForsetting;
	// private boolean flagForMessage;
	// private boolean flagForInvite;
	// private boolean flagformatchedListDisplay;

	private SlidingMenu menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// mLayout = (MainLayout)
		// this.getLayoutInflater().inflate(R.layout.slidmenuxamplemainactivity,
		// null);
		// setContentView(mLayout);
		setContentView(R.layout.slidmenuxamplemainactivity);
		tvTitle = (TextView) findViewById(R.id.activity_main_content_title);
		topbartextviewFont = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Light.otf");
		tvTitle.setTypeface(topbartextviewFont);
		tvTitle.setTextColor(Color.rgb(255, 255, 255));
		tvTitle.setTextSize(20);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = preferences.edit();
		// logDebug("onCreate  ");

		// ActionBar ab = getSupportActionBar();
		// ab.setHomeButtonEnabled(true);
		// ab.setDisplayHomeAsUpEnabled(true);

		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT_RIGHT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		Log.d(TAG, "onCreate  before add menu ");
		menu.setMenu(R.layout.leftmenu);
		menu.setSecondaryMenu(R.layout.rightmenu);
		Log.d(TAG, "onCreate  add menu ");
		menu.setSlidingEnabled(true);
		Log.d(TAG, "onCreate  finish");

		// search
		etSerchFriend = (EditText) menu
				.findViewById(R.id.et_serch_right_side_menu);
		// btnSerch = (Button) menu.findViewById(R.id.btn_serch_right_side);

		View leftmenuview = menu.getMenu();
		View rightmenuview = menu.getSecondaryMenu();

		initLayoutComponent(leftmenuview, rightmenuview);

		menu.setSecondaryOnOpenListner(this);

		// lvMenuItems = getResources().getStringArray(R.array.menu_items);

		// lvMenu.setAdapter(new
		// ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
		// lvMenuItems));

		matcheslistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// logDebug("setOnItemClickListener  onItemClick arg2 "+arg2);
				LikeMatcheddataForListview matcheddataForListview = (LikeMatcheddataForListview) arg0
						.getItemAtPosition(arg2);
				String faceboolid = matcheddataForListview.getFacebookid();
				// logDebug(" background setOnItemClickListener  onItemClick friend facebook id faceboolid "+faceboolid);
				// logDebug(" background setOnItemClickListener  onItemClick user facebook id  faceboolid"+new
				// SessionManager(MainActivity.this).getFacebookId());
				Bundle mBundle = new Bundle();
				mBundle.putString(Constant.FRIENDFACEBOOKID, faceboolid);
				mBundle.putString(Constant.CHECK_FOR_PUSH_OR_NOT, "1");

				Intent mIntent = new Intent(MainActivity.this,
						ChatActivity.class);
				mIntent.putExtras(mBundle);
				startActivity(mIntent);
			}
		});

		buttonRightMenu = (Button) findViewById(R.id.button_right_menu);

		btMenu = (Button) findViewById(R.id.button_menu);
		btMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Show/hide the menu
				toggleMenu(v);
				// menu_right.setVisibility(View.GONE);;
				// menu_left.setVisibility(View.VISIBLE);
			}
		});

		try {

			profilelayout.setOnClickListener(this);
			homelayout.setOnClickListener(this);
			messages.setOnClickListener(this);
			settinglayout.setOnClickListener(this);
			invitelayout.setOnClickListener(this);

		} catch (Exception e) {
			logError("oncreate   Exception  " + e);
		}

		Session session = Session.getActiveSession();

		if (session == null) {
			if (savedInstanceState != null) {
				session = Session.restoreSession(this, null, statusCallback,
						savedInstanceState);

			}
			if (session == null) {
				session = new Session(this);

			}
			Session.setActiveSession(session);

			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {

			}
		}

		// Bundle extras = getIntent().getExtras();
		System.out.println("Get Intent done");
		try {
			FragmentManager fm = MainActivity.this.getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			Layout1 fragment = new Layout1();
			ft.add(R.id.activity_main_content_fragment, fragment);
			tvTitle.setText(getResources().getString(R.string.app_name));

			ft.commit();
			setProfilePick(profileimage);

			// if(extras !=null)
			// {
			// System.out.println("onCreate extras");
			//
			// Session msession = Session.getActiveSession();
			// if (extras.getBoolean(CommanConstant.Fromsignup))
			// {
			//
			// // if (msession.isOpened())
			// // {
			// //
			// // getProfildPic();
			// // }
			// // else
			// // {
			// // usersignup=true;
			// // getOpenedSession();
			// // }
			//
			// }
			// else if (extras.getBoolean(CommanConstant.FromLogin))
			// {
			//
			// fineLikedMatched();
			//
			// FragmentManager fm =
			// MainActivity.this.getSupportFragmentManager();
			// FragmentTransaction ft = fm.beginTransaction();
			// Layout1 fragment = new Layout1();
			// ft.add(R.id.activity_main_content_fragment, fragment);
			// tvTitle.setText(getResources().getString(R.string.app_name));
			//
			// ft.commit();
			// setProfilePick(profileimage);
			//
			//
			// }
			// else if (extras.getBoolean(CommanConstant.Fromsplash))
			// {
			// fineLikedMatched();
			// // logDebug("onCreate extra  from Fromsplash ");
			// // logDebug("onCreate session "+session);
			// FragmentManager fm =
			// MainActivity.this.getSupportFragmentManager();
			// FragmentTransaction ft = fm.beginTransaction();
			// Layout1 fragment = new Layout1();
			// ft.add(R.id.activity_main_content_fragment, fragment);
			// tvTitle.setText(getResources().getString(R.string.app_name));
			// ft.commit();
			// /*if (msession.isOpened())
			// {
			//
			//
			// }
			// else
			// {
			// // userFromsplash=true;
			// // getOpenedSession();
			// }*/
			// setProfilePick(profileimage);
			// }
			//
			// }
			// else
			// {
			//
			// }
		} catch (Exception e) {
			logError("onCreate Exception " + e);

		}

		Ultilities mUltilities = new Ultilities();
		int imageHeightAndWidht[] = mUltilities
				.getImageHeightAndWidthForAlubumListview(this);
		arryList = new ArrayList<LikeMatcheddataForListview>();
		adapter = new MatchedDataAdapter(this, arryList, imageHeightAndWidht);
		matcheslistview.setAdapter(adapter);

		// final SessionManager sessionManager = new SessionManager(this);

		buttonRightMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isProfileclicked) {
					Intent mIntent = new Intent(MainActivity.this,
							EditeProfileActivity.class);
					startActivity(mIntent);
				} else {
					toggleRightMenu(v);

					// //menu_right.setVisibility(View.VISIBLE);;
					// //menu_left.setVisibility(View.GONE);
					// if (!flagformatchedListDisplay)
					// {
					// flagformatchedListDisplay=true;
					// fineLikedMatched();
					// }
					// else
					// {
					// flagformatchedListDisplay=false;
					// arryList.clear();
					// adapter.notifyDataSetChanged();
					// }

				}

			}
		});

		initSerchData();

	}

	/**
	 * method responsible for intialise serch function at right side friend list
	 * adapter of this activity
	 */
	private void initSerchData() {
		etSerchFriend.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				adapter.getFilter().filter(s.toString().trim());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	@Override
	public void onOpen() {
		logDebug("onOpen");
		findLikedMatched();
	}

	@Override
	protected void onResume() {
		super.onResume();
		logDebug(" MainActivity   onResume  called");
		SessionManager sessionManager = new SessionManager(this);
		if (sessionManager.isIsProfileImageChanged()) {
			sessionManager.setIsProfileImageChanged(false);
			setProfilePick(profileimage);
		} else {

		}
	}

	private void setProfilePick(ImageView userProfilImage) {
		Ultilities mUltilities = new Ultilities();
		// File appDirectory;
		// File _picDir;
		// File myimgFile;
		try {

			// appDirectory =
			// mUltilities.createAppDirectoy(getResources().getString(R.string.appdirectory));
			// _picDir = new File(appDirectory,
			// getResources().getString(R.string.imagedirectory));
			// myimgFile= new File(_picDir,
			// getResources().getString(R.string.imagefilename)+"0.jpg");
			DatabaseHandler mdaDatabaseHandler = new DatabaseHandler(this);
			String imageOrderArray[] = { "1" };
			ArrayList<ImageDetail> imagelist = mdaDatabaseHandler
					.getImageDetailByImageOrder(imageOrderArray);
			if (imagelist != null && imagelist.size() > 0) {
				Bitmap bitmapimage = mUltilities.showImage/*
														 * setImageToImageViewBitmapFactory
														 * .decodeFiledecodeFile
														 */(imagelist.get(0)
						.getSdcardpath());
				Bitmap cropedBitmap = null;
				ScalingUtilities mScalingUtilities = new ScalingUtilities();
				Bitmap mBitmap = null;
				if (bitmapimage != null) {
					cropedBitmap = mScalingUtilities.createScaledBitmap(
							bitmapimage, 80, 80, ScalingLogic.CROP);
					bitmapimage.recycle();
					mBitmap = mUltilities.getCircleBitmap(cropedBitmap, 1);
					cropedBitmap.recycle();
					userProfilImage.setImageBitmap(mBitmap);
				} else {
				}
			} else {

			}

		} catch (Exception e) {
			ImageView[] params = { userProfilImage };
			new BackGroundTaskForDownloadProfileImageIfUseDeletedFormDirectory()
					.execute(params);
		}

	}

	private class BackGroundTaskForDownloadProfileImageIfUseDeletedFormDirectory
			extends AsyncTask<ImageView, Void, Void> {
		private Ultilities mUltilities = new Ultilities();
		private SessionManager mSessionManager = new SessionManager(
				MainActivity.this);
		private ImageView userProfilImage;

		@Override
		protected Void doInBackground(ImageView... params) {
			userProfilImage = params[0];

			try {
				File imageFile = mUltilities.createFile(getResources()
						.getString(R.string.imagedire), getResources()
						.getString(R.string.imagefilename) + "0.jpg");

				com.appdupe.flamer.utility.Utility.addBitmapToSdCardFromURL(
						mSessionManager.getUserPrifilePck().replaceAll(" ",
								"%20"), imageFile);
			} catch (Exception e) {
				logError("BackGroundTaskForDownloadProfileImageIfUseDeletedFormDirectory Exception "
						+ e);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			File _sdCard;
			File _picDir;
			File myimgFile;
			try {
				_sdCard = mUltilities.getSdCardPath();

				_picDir = new File(_sdCard, getResources().getString(
						R.string.imagedire));

				myimgFile = new File(_picDir, "profilepic" + 0 + ".jpg");

				Bitmap bitmapimage = mUltilities.showImage/*
														 * setImageToImageViewBitmapFactory
														 * .decodeFiledecodeFile
														 */(myimgFile
						.getAbsolutePath());

				ScalingUtilities mScalingUtilities = new ScalingUtilities();
				Bitmap cropedBitmap = mScalingUtilities.createScaledBitmap(
						bitmapimage, 80, 80, ScalingLogic.CROP);
				bitmapimage.recycle();
				Bitmap mBitmap = mUltilities.getCircleBitmap(cropedBitmap, 1);
				cropedBitmap.recycle();

				userProfilImage.setImageBitmap(mBitmap);

			} catch (Exception e) {
				logError("BackGroundTaskForDownloadProfileImageIfUseDeletedFormDirectory onPostExecute  Exception"
						+ e);
			}
		}

	}

	private void initLayoutComponent(View leftmenu, View rightmenu) {
		// menu_right=(LinearLayout)rightmenu.findViewById(R.id.menu_right);
		// menu_left=(LinearLayout)leftmenu.findViewById(R.id.menu_left);
		matcheslistview = (ListView) rightmenu
				.findViewById(R.id.menu_right_ListView);
		// lvMenu = (ListView) findViewById(R.id.menu_listview);

		profileimage = (ImageView) leftmenu.findViewById(R.id.profileimage);
		// homeimageview=(ImageView)findViewById(R.id.homeimageview);
		// messageimage=(ImageView)findViewById(R.id.messageimage);
		// settingimage=(ImageView)findViewById(R.id.settingimage);
		// inviteimage=(ImageView)findViewById(R.id.inviteimage);
		// profileimage.setBackgroundResource(R.drawable.profile_boader_on);

		profilelayout = (LinearLayout) leftmenu
				.findViewById(R.id.profilelayout);
		homelayout = (LinearLayout) leftmenu.findViewById(R.id.homelayout);
		messages = (LinearLayout) leftmenu.findViewById(R.id.messages);
		settinglayout = (LinearLayout) leftmenu
				.findViewById(R.id.settinglayout);
		invitelayout = (LinearLayout) leftmenu.findViewById(R.id.invitelayout);

	}

	private void findLikedMatched() {

		logDebug("fineLikedMatched");

		// try {
		String deviceid = Ultilities.getDeviceId(this);
		logDebug("fineLikedMatched   deviceid" + deviceid);
		SessionManager mSessionManager = new SessionManager(this);

		String sessionToken = mSessionManager.getUserToken();
		logDebug("fineLikedMatched   sessionToken" + sessionToken);
		Ultilities mUltilitie = new Ultilities();
		String currentdeviceTime = mSessionManager.getLastUpdatedTime();
		String curenttime = mUltilitie.getCurrentGmtTime();
		mSessionManager.setLastUpdate(curenttime);
		/*
		 * if (currentdeviceTime!=null&&currentdeviceTime.length()>0) {
		 * 
		 * } else { currentdeviceTime=mUltilitie.getCurrentGmtTime();
		 * mSessionManager.setLastUpdate(currentdeviceTime); }
		 */

		String params[] = { sessionToken, deviceid, currentdeviceTime };
		new BackgroundTaskForFindLikeMatched().execute(params);
		// } catch (Exception e)
		// {
		// logError("fineLikedMatched Exception "+e);
		// }

		/* String currentdeviceTime; */

	}

	private class BackgroundTaskForFindLikeMatched extends
			AsyncTask<String, Void, Void> {
		private Ultilities mUltilities = new Ultilities();
		private List<NameValuePair> getuserparameter;
		private String likedmatchedata;
		private LikedMatcheData matcheData;
		private ArrayList<Likes> likesList;
		private LikeMatcheddataForListview matcheddataForListview;
		DatabaseHandler mDatabaseHandler = new DatabaseHandler(
				MainActivity.this);
		private boolean isResponseSuccess = true;

		@Override
		protected Void doInBackground(String... params) {
			try {

				File appDirectory = mUltilities
						.createAppDirectoy(getResources().getString(
								R.string.appdirectory));
				logDebug("BackgroundTaskForFindLikeMatched   doInBackground appDirectory "
						+ appDirectory);
				File _picDir = new File(appDirectory, getResources().getString(
						R.string.imagedirematchuserdirectory));

				logDebug("BackgroundTaskForFindLikeMatched doInBackground ");

				getuserparameter = mUltilities.getUserLikedParameter(params);

				logDebug("BackgroundTaskForFindLikeMatched doInBackground   getuserparameter "
						+ getuserparameter);

				likedmatchedata = mUltilities.makeHttpRequest(
						Constant.getliked_url, Constant.methodeName,
						getuserparameter);

				logDebug("BackgroundTaskForFindLikeMatched doInBackground   likedmatchedata "
						+ likedmatchedata);

				Gson gson = new Gson();
				matcheData = gson.fromJson(likedmatchedata,
						LikedMatcheData.class);

				logDebug("BackgroundTaskForFindLikeMatched doInBackground   matcheData "
						+ matcheData);

				// "errNum": "51",
				// "errFlag": "0",
				// "errMsg": "Matches found!",

				if (matcheData.getErrFlag() == 0) {
					likesList = matcheData.getLikes();
					logDebug("BackgroundTaskForFindLikeMatched doInBackground   likesList "
							+ likesList);
					if (arryList != null) {
						arryList.clear();
					}

					logDebug("BackgroundTaskForFindLikeMatched doInBackground   likesList sized "
							+ likesList.size());
					for (int i = 0; i < likesList.size(); i++) {
						matcheddataForListview = new LikeMatcheddataForListview();
						String userName = likesList.get(i).getfName();
						String facebookid = likesList.get(i).getFbId();
						// Log.i(TAG, "Background facebookid......"+facebookid);
						String picturl = likesList.get(i).getpPic();
						int falg = likesList.get(i).getFlag();
						String latd = likesList.get(i).getLadt();
						matcheddataForListview.setFacebookid(facebookid);
						matcheddataForListview.setUserName(userName);
						matcheddataForListview.setImageUrl(picturl);
						matcheddataForListview.setFlag("" + falg);
						matcheddataForListview.setladt(latd);
						// matcheddataForListview.setFilePath(filePath);
						File imageFile = mUltilities.createFileInSideDirectory(
								_picDir, userName + facebookid + ".jpg");
						// logDebug("BackGroundTaskForUserProfile doInBackground imageFile is profile "+imageFile.isFile());
						com.appdupe.flamer.utility.Utility
								.addBitmapToSdCardFromURL(likesList.get(i)
										.getpPic().replaceAll(" ", "%20"),
										imageFile);
						matcheddataForListview.setFilePath(imageFile
								.getAbsolutePath());
						if (!preferences.getString(Constant.FACEBOOK_ID, "")
								.equals(facebookid)) {
							arryList.add(matcheddataForListview);
						}

					}
					DatabaseHandler mDatabaseHandler = new DatabaseHandler(
							MainActivity.this);
					SessionManager mSessionManager = new SessionManager(
							MainActivity.this);
					String userFacebookid = mSessionManager.getFacebookId();

					//
					boolean isdataiserted = mDatabaseHandler.insertMatchList(
							arryList, userFacebookid);

					// what it is??
					// ////////////////////////////////////////////////////////////////////////////////

					ArrayList<LikeMatcheddataForListview> arryListtem = mDatabaseHandler
							.getUserFindMatch();
					logDebug("arryListtem  " + arryListtem);
					if (arryListtem != null && arryListtem.size() > 0) {
						logDebug("arryList  size " + arryListtem.size());
						arryList.clear();

						arryList.addAll(arryListtem);
						// adapter.notifyDataSetChanged();

						// for (int i = 0; i < arryList.size(); i++)
						// {
						// Bitmap bitmapimage =
						// mUltilities.showImage/*setImageToImageViewBitmapFactory.decodeFiledecodeFile*/(arryList.get(i).getFilePath());
						// logDebug("setProfilePick bitmapimage"+bitmapimage);
						// ScalingUtilities mScalingUtilities =new
						// ScalingUtilities();
						// Bitmap cropedBitmap=
						// mScalingUtilities.createScaledBitmap(bitmapimage,
						// 100, 100, ScalingLogic.CROP);
						// bitmapimage.recycle();
						// Bitmap mBitmap=
						// mUltilities.getCircleBitmap(cropedBitmap, 1);
						// arryList.get(i).setmBitmap(mBitmap);
						// cropedBitmap.recycle();
						// // logDebug("setProfilePick  mBitmap"+mBitmap);
						// // userProfilImage.setImageBitmap(mBitmap);
						//
						// }
					}

				}
				// "errNum": "50",
				// "errFlag": "1",
				// "errMsg": "Sorry, no matches found!"
				else if (matcheData.getErrFlag() == 1) {
					ArrayList<LikeMatcheddataForListview> arryListtem = mDatabaseHandler
							.getUserFindMatch();
					logDebug("arryListtem  " + arryListtem);
					if (arryListtem != null && arryListtem.size() > 0) {
						logDebug("arryList  size " + arryListtem.size());
						arryList.clear();

						arryList.addAll(arryListtem);
					}

				} else {
					// do nothing
				}

			} catch (Exception e) {
				logError("BackgroundTaskForFindLikeMatched doInBackground Exception "
						+ e);
				// some thign wrong happend
				isResponseSuccess = false;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			logError("BackgroundTaskForFindLikeMatched onPostExecute  ");
			try {
				mdialog.dismiss();
			} catch (Exception e) {
				logError("BackgroundTaskForFindLikeMatched   onPostExecute  Exception "
						+ e);
			}
			if (!isResponseSuccess) {
				AlertDialogManager.errorMessage(MainActivity.this, "Alert",
						"Request timeout");
			}

			// Error:
			// {
			// "errNum": "50",
			// "errFlag": "1",
			// "errMsg": "Sorry, no matches found!"
			// }
			//
			// Success:
			// {
			// "errNum": "51",
			// "errFlag": "0",
			// "errMsg": "Matches found!",
			// "likes": [
			// {
			// "fbId": "13526723462",
			// "fName": "matched_user_name",
			// "ladt": "2013-12-06 05:40:01",
			// "pPic": "http://108.166.190.172:81/tinderClone/pics/1381606.jpg",
			// "flag": 3
			// },
			// {
			// "fbId": "135267234625",
			// "fName": "matched_user_name",
			// "ladt": "2013-12-06 05:40:01",
			// "pPic": "http://108.166.190.172:81/tinderClone/pics/1381606.jpg",
			// "flag": 4
			// }
			// ]

			adapter.notifyDataSetChanged();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			logDebug("BackgroundTaskForFindLikeMatched onPreExecute  ");
			try {
				mdialog = mUltilities.GetProcessDialog(MainActivity.this);
				mdialog.setCancelable(false);
				mdialog.show();
			} catch (Exception e) {
				logError("BackgroundTaskForFindLikeMatched   onPreExecute  Exception "
						+ e);
			}

		}

	}

	private class MatchedDataAdapter extends
			ArrayAdapter<LikeMatcheddataForListview> {
		// RequestQueue mRequestQueue =
		// Volley.newRequestQueue(MainActivity.this);
		private AQuery aQuery;
		// private ImageLoader imageLoader = new ImageLoader(mRequestQueue,
		// new BitmapLruCache(BitmapLruCache.getDefaultLruCacheSize()));
		// private int imageHeigthAndWidth [];
		private Activity mActivity;
		private LayoutInflater mInflater;
		private SessionManager sessionManager;

		public MatchedDataAdapter(Activity context,
				List<LikeMatcheddataForListview> objects,
				int imageHeigthAndWidth[]) {
			super(context, R.layout.matchedlistviewitem, objects);
			mActivity = context;
			mInflater = (LayoutInflater) mActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// this.imageHeigthAndWidth=imageHeigthAndWidth;
			sessionManager = new SessionManager(context);
			aQuery = new AQuery(context);
		}

		@Override
		public int getCount() {
			return super.getCount();
		}

		@Override
		public LikeMatcheddataForListview getItem(int position) {
			return super.getItem(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.matchedlistviewitem,
						null);
				holder.imageview = (ImageView) convertView
						.findViewById(R.id.userimage);
				holder.textview = (TextView) convertView
						.findViewById(R.id.userName);
				holder.lastMasage = (TextView) convertView
						.findViewById(R.id.lastmessage);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.textview.setId(position);
			holder.imageview.setId(position);
			holder.lastMasage.setId(position);
			holder.textview.setText(getItem(position).getUserName());
			// holder.imageview.setImageUrl(getItem(position).getImageUrl(),
			// imageLoader,"CircularImge",MainActivity.this);
			aQuery.id(holder.imageview).image(getItem(position).getImageUrl());
			// holder.imageview.setImageUrl(getItem(position).getImageUrl(),
			// imageLoader);
			try {
				holder.lastMasage.setText(sessionManager
						.getLastMessage(getItem(position).getFacebookid()));
			} catch (Exception e) {
				logError("getView  Exception " + e);
			}

			/*
			 * holder.lastMasage.setText(getItem(position).getFacebookid()); if
			 * (getItem(position).getmBitmap()!=null) {
			 * 
			 * // holder.mProgressBar.setVisibility(View.GONE);
			 * holder.imageview.setImageBitmap(
			 * Bitmap.createScaledBitmap(getItem(position).getmBitmap(),
			 * imageHeigthAndWidth[1], imageHeigthAndWidth[0], false)); } else {
			 * 
			 * }
			 */

			return convertView;
		}

		class ViewHolder {
			ImageView imageview;
			TextView textview;
			TextView lastMasage;

		}
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu)
	// {
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }

	public void toggleMenu(View v) {
		// mLayout.toggleMenu();
		menu.toggle();
	}

	public void toggleRightMenu(View v) {
		// mLayout.toggleMenuRight();
		menu.showSecondaryMenu();
	}

	@Override
	public void onBackPressed() {
		if (menu.isMenuShowing()) {
			menu.toggle();
		} else if (menu.isSecondaryMenuShowing()) {
			menu.showSecondaryMenu();
		} else {
			super.onBackPressed();
		}
	}

	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {

			if (usersignup) {
				// getProfildPic();
				usersignup = false;
			}

		}
	}

	// private void getOpenedSession() {
	// Session.openActiveSession(this, true, statusCallback);
	// }

	@Override
	public void onStart() {
		super.onStart();
		Session.getActiveSession().addCallback(statusCallback);
		FlurryAgent.onStartSession(this, Constant.flurryKey);
	}

	@Override
	public void onStop() {
		super.onStop();
		Session.getActiveSession().removeCallback(statusCallback);
		FlurryAgent.onEndSession(this);
		if (mdialog != null) {
			mdialog.dismiss();
			mdialog = null;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}

	// private void getProfildPic ()
	// {
	//
	//
	// //logDebug("getProfildPic ");
	// SessionManager mSessionManager=new SessionManager(this);
	//
	// String[] params={mSessionManager.getFacebookId()};
	// //logDebug("onCreate params "+params);
	//
	// String fqlQuery =
	// "select src_big from photo  where album_object_id IN (SELECT  object_id   FROM album WHERE owner="+"'"+params[0]+"'"+" and name='Profile Pictures') LIMIT 5";
	// //String fqlQuery =
	// "select uid1, uid2 FROM friend WHERE uid1 = '100003056725155'";
	// //String fqlQuery =
	// "select src from photo  where album_object_id IN (SELECT  object_id   FROM album WHERE owner='params[0]' and name='Profile Pictures'";
	// Log.i(TAG, "Result: " + fqlQuery);
	// Bundle param = new Bundle();
	// param.putString("q", fqlQuery);
	// Session session = Session.getActiveSession();
	// Request request = new Request(session,"/fql",param,HttpMethod.GET,new
	// Request.Callback()
	// {
	// public void onCompleted(Response response)
	// {
	// //Log.i(TAG, "Result: " + response.toString());
	// logDebug("getProfildPic    Result  "+response.toString());
	// String [] params={response.toString()} ;
	// new BackGroundTaskForDownload().execute(params);
	// }
	// });
	//
	// Request.executeBatchAsync(request);
	//
	//
	// }

	// private void findMatch()
	// {
	// new BackGroundTaskForFindMatch().execute();
	// }

	// private class BackGroundTaskForFindMatch extends AsyncTask<String, Void,
	// Void>
	// {
	//
	// private Ultilities mUltilities=new Ultilities();
	// private String sessionToken;
	// private SessionManager mSessionManager=new
	// SessionManager(MainActivity.this);
	// private String lognResponse;
	// private List<NameValuePair>findMatchNameValuePairList;
	// private String deviceid;
	// @Override
	// protected Void doInBackground(String... params)
	// {
	// deviceid=/*"defoutlfortestin"*/Ultilities.getDeviceId(MainActivity.this);
	// sessionToken=mSessionManager.getUserToken();
	// String [] findMatchParamere={sessionToken,deviceid};
	// findMatchNameValuePairList=mUltilities.getFindMatchParameter(findMatchParamere);
	// lognResponse=
	// mUltilities.makeHttpRequest(CommanConstant.uploadImage_url,CommanConstant.methodeName,findMatchNameValuePairList);
	//
	//
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(Void result)
	// {
	// super.onPostExecute(result);
	// }
	//
	// @Override
	// protected void onPreExecute()
	// {
	// super.onPreExecute();
	// }
	// }

	private class BackGroundTaskForUploadImage extends
			AsyncTask<String, Void, String> {
		private long chunkLength = 64 * 1024;
		private long FILE_SIZE;
		// private byte [] image_byte_arr;
		private long totalBytesRead = 0;
		private long bytesRemaining;
		// private boolean flagForUploadsuccess;
		private String fileName;
		private String deviceid;
		private Ultilities mUltilities = new Ultilities();
		private String sessionToken;
		private SessionManager mSessionManager = new SessionManager(
				MainActivity.this);
		private String lognResponse;
		private List<NameValuePair> uploadNameValuePairList;
		private String imagefalg;
		private boolean lastImageUploade = false;
		private UploadImage mUploadImage;

		@Override
		protected String doInBackground(String... params) {
			// Log.d(TAG,
			// "BackGroundTaskForUploadImage doInBackground  filePath "+filePath);
			// image_byte_arr=utility.convertFiletoByteArray(new
			// File(filePath));
			// logDebug("BackGroundTaskForUploadImage   BackGroundTaskForUploadImage ");
			deviceid = /* "defoutlfortestin" */Ultilities
					.getDeviceId(MainActivity.this);
			sessionToken = mSessionManager.getUserToken();
			// logDebug("BackGroundTaskForUploadImage   BackGroundTaskForUploadImage deviceid "+deviceid);
			// logDebug("BackGroundTaskForUploadImage   BackGroundTaskForUploadImage sessionToken "+sessionToken);
			FileInputStream fin = null;
			for (int i = 0; i < params.length; i++) {

				if (i == 0) {
					imagefalg = "1";

				} else {
					imagefalg = "2";
				}
				chunkLength = 524288;

				// 536870912

				totalBytesRead = 0;
				bytesRemaining = 0;
				// logDebug("BackGroundTaskForUploadImage   BackGroundTaskForUploadImage chunkLength "+chunkLength);
				// logDebug("BackGroundTaskForUploadImage   BackGroundTaskForUploadImage totalBytesRead "+totalBytesRead);
				// logDebug("BackGroundTaskForUploadImage   BackGroundTaskForUploadImage bytesRemaining "+bytesRemaining);

				File mFile = new File(params[i]);
				fileName = mFile.getName();
				// logDebug("BackGroundTaskForUploadImage   BackGroundTaskForUploadImage fileName "+fileName);

				try {
					fin = new FileInputStream(mFile);
				} catch (FileNotFoundException e2) {
					e2.printStackTrace();

				}
				if (mFile.isFile() && mFile.length() > 0) {
					FILE_SIZE = mFile.length();
				}

				// Log.d(TAG,
				// "BackGroundTaskForUploadImage doInBackground  image_byte_arr "+image_byte_arr);

				// if (image_byte_arr!=null&&image_byte_arr.length>0)
				// {
				// FILE_SIZE=image_byte_arr.length;
				// }
				Log.d(TAG,
						"BackGroundTaskForUploadImage doInBackground  FILE_SIZE "
								+ FILE_SIZE);

				// Log.d(TAG,
				// "BackGroundTaskForUploadImage doInBackground  fileTitle "+fileTitle);

				while (totalBytesRead < FILE_SIZE) {
					try {
						bytesRemaining = FILE_SIZE - totalBytesRead;

						if (bytesRemaining < chunkLength) // Remaining Data Part
															// is Smaller Than
															// CHUNK_SIZE //
															// CHUNK_SIZE is
															// assigned to
															// remain volume
						{
							if (i == 0) {
								// logDebug("BackGroundTaskForUploadImage doInBackground idex is "+i
								// );
								// logDebug("BackGroundTaskForUploadImage doInBackground bytesRemaining "+bytesRemaining
								// );
								// logDebug("BackGroundTaskForUploadImage doInBackground chunkLength "+chunkLength
								// );
								// logDebug("BackGroundTaskForUploadImage doInBackground totalBytesRead "+totalBytesRead
								// );
								lastImageUploade = true;
							} else {
								lastImageUploade = false;
							}
							chunkLength = bytesRemaining;
							System.out.println("CHUNK_SIZE: " + chunkLength);
						}
						// temporary = new byte[(int) chunkLength];
						byte[] chunk = null;
						chunk = new byte[(int) chunkLength];

						byte fileContent[] = new byte[(int) chunkLength];
						try {

							fin.read(fileContent, 0, (int) chunkLength);

						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						System.arraycopy(fileContent, 0, chunk, 0,
								(int) chunkLength);
						// System.arraycopy(image_byte_arr, (int)
						// totalBytesRead, chunk, 0, (int) chunkLength);
						byte[] encoded = Base64.encodeBase64(chunk);
						String encodedString = new String(encoded);
						String[] uploadParameter = { sessionToken, deviceid,
								fileName, encodedString, imagefalg };
						uploadNameValuePairList = mUltilities
								.getUploadParameter(uploadParameter);

						lognResponse = mUltilities.makeHttpRequest(
								Constant.uploadImage_url, Constant.methodeName,
								uploadNameValuePairList);

						totalBytesRead = totalBytesRead + chunkLength;
						// first image last chunk is null;
						if (lastImageUploade) {

							Gson gson = new Gson();

							mUploadImage = gson.fromJson(lognResponse,
									UploadImage.class);
							Log.i(TAG,
									"BackGroundTaskForUploadImage doInBackground first image uploaded sucessfull erronumber is "
											+ mUploadImage.getErrNum());
							if (mUploadImage.getErrNum() == 18) {
								Log.i(TAG,
										"BackGroundTaskForUploadImage doInBackground first image uploaded sucessfull lognResponse "
												+ lognResponse);
								Log.i(TAG,
										"BackGroundTaskForUploadImage doInBackground first image pick url"
												+ mUploadImage.getPicURL());

								mSessionManager.setProFilePicture(mUploadImage
										.getPicURL());
							}
						} else {
							// do nothing
						}

						Log.i(TAG,
								"BackGroundTaskForUploadImage doInBackground totalBytesRead "
										+ totalBytesRead);
						Log.i(TAG,
								"BackGroundTaskForUploadImage doInBackground lognResponse "
										+ lognResponse);

					} catch (Exception e) {
						// mdialog.dismiss();
						String upploadOriginalFile = "UploadOriginalFile_faild";
						FlurryAgent.logEvent(upploadOriginalFile);
						// flagForUploadsuccess=false;

						e.printStackTrace();
						// Log.i(TAG,
						// "BackGroundTaskForUploadImage doInBackground IOException "+e);
						break;
					}

				}

				File appDirectory = mUltilities
						.createAppDirectoy(getResources().getString(
								R.string.appdirectory));
				// logDebug("BackGroundTaskForDownload   doInBackground appDirectory "+appDirectory);
				File _picDir = new File(appDirectory, getResources().getString(
						R.string.imagedirectory));

				// File imageFile=
				// mUltilities.createFile(getResources().getString(R.string.imagedire),
				// getResources().getString(R.string.imagefilename)+"0.jpg");
				// com.example.tinderapp.utility.Utility.addBitmapToSdCardFromURL(mLoginData.getProfilePic().replaceAll(" ","%20"),imageFile);

				File imageFile = mUltilities.createFileInSideDirectory(_picDir,
						getResources().getString(R.string.imagefilename)
								+ "0.jpg");
				// logDebug("BackGroundTaskForDownload   doInBackground _imageFile  is exist"+imageFile.isFile());
				com.appdupe.flamer.utility.Utility.addBitmapToSdCardFromURL(
						mSessionManager.getUserPrifilePck().replaceAll(" ",
								"%20"), imageFile);

			}
			try {
				fin.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
			try {

				// mUltilities.deleteNon_EmptyDir(_picDir);

				mdialog.dismiss();

				// logDebug("onCreate extra  from Fromsplash ");
				// l//ogDebug("onCreate session "+session);
				FragmentManager fm = MainActivity.this
						.getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				Layout1 fragment = new Layout1();
				ft.add(R.id.activity_main_content_fragment, fragment);
				ft.commit();

			} catch (Exception e) {

			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
	}

	/*
	 * // private void onMenuItemClick(AdapterView<?> parent, View view,int
	 * position, long id) // { // String selectedItem = lvMenuItems[position];
	 * // String currentItem = tvTitle.getText().toString(); // if
	 * (selectedItem.compareTo(currentItem) == 0) // { // mLayout.toggleMenu();
	 * // return; // } // // FragmentManager fm =
	 * MainActivity.this.getSupportFragmentManager(); // FragmentTransaction ft
	 * = fm.beginTransaction(); // Fragment fragment = null; // // if
	 * (selectedItem.compareTo("Profile") == 0) // { // // fragment = new
	 * Layout2(); //
	 * tvTitle.setText(getResources().getString(R.string.myprofile)); // // }
	 * else if (selectedItem.compareTo("Home") == 0) // { // fragment = new
	 * Layout1(); //
	 * tvTitle.setText(getResources().getString(R.string.app_name)); // } //
	 * else if (selectedItem.compareTo("Setting") == 0) // { //
	 * tvTitle.setText(getResources().getString(R.string.settings)); // fragment
	 * = new SettingActivity(); // } // // if (fragment != null) // { //
	 * ft.replace(R.id.activity_main_content_fragment, fragment); //
	 * ft.commit(); // tvTitle.setText(selectedItem); // } //
	 * mLayout.toggleMenu(); // }
	 * 
	 * 
	 * // private boolean flagforHome; // private boolean flagForProfile; //
	 * private boolean flagForsetting; // private boolean flagForMessage; //
	 * private boolean flagForInvite;
	 */

	@Override
	public void onClick(View v) {
		// if (!CommanConstant.isMatchedFound)
		// {

		FragmentManager fm = MainActivity.this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment fragment = null;

		if (v.getId() == R.id.homelayout) {
			if (flagforHome) {
				menu.toggle();
				return;
			} else {
				fragment = new Layout1();
				buttonRightMenu
						.setBackgroundResource(R.drawable.selector_for_message_button);
				tvTitle.setText(getResources().getString(R.string.app_name));
				flagforHome = true;
				flagForProfile = false;
				flagForsetting = false;
				// flagForMessage=false;
				// flagForInvite=false;
				isProfileclicked = false;
				if (fragment != null) {
					ft.replace(R.id.activity_main_content_fragment, fragment);
					ft.commit();
					// tvTitle.setText(selectedItem);
				}
				menu.toggle();
			}
		} else if (v.getId() == R.id.profilelayout) {
			if (flagForProfile) {
				menu.toggle();
				return;
			} else {
				buttonRightMenu.setBackgroundResource(R.drawable.edit_btn);
				;
				isProfileclicked = true;

				fragment = new Layout2();
				tvTitle.setText(getResources().getString(R.string.myprofile));
				flagforHome = false;
				flagForProfile = true;
				flagForsetting = false;
				// flagForMessage=false;
				// flagForInvite=false;
				if (fragment != null) {
					ft.replace(R.id.activity_main_content_fragment, fragment);
					ft.commit();
					// tvTitle.setText(selectedItem);
				}
				menu.toggle();
			}
		} else if (v.getId() == R.id.settinglayout) {
			if (flagForsetting) {
				menu.toggle();
				return;
			} else {
				buttonRightMenu
						.setBackgroundResource(R.drawable.selector_for_message_button);
				;
				tvTitle.setText(getResources().getString(R.string.settings));
				fragment = new SettingActivity();
				flagforHome = false;
				flagForProfile = false;
				flagForsetting = true;
				// flagForMessage=false;
				// flagForInvite=false;
				isProfileclicked = false;
				if (fragment != null) {
					ft.replace(R.id.activity_main_content_fragment, fragment);
					ft.commit();
					// tvTitle.setText(selectedItem);
				}
				menu.toggle();
			}
		} else if (v.getId() == R.id.messages) {

			toggleRightMenu(v);

		} else if (v.getId() == R.id.invitelayout) {
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
			sendIntent.setType("text/plain");
			startActivity(sendIntent);
		}
	}

}
