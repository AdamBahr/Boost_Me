package com.android.slidingmenu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.appdupe.flamer.R;
import com.appdupe.flamer.pojo.GellaryData;
import com.appdupe.flamer.pojo.InviteActionData;
import com.appdupe.flamer.pojo.QuaryOneResult;
import com.appdupe.flamer.pojo.QuarySecondResult;
import com.appdupe.flamer.pojo.UserInterestAndFriendData;
import com.appdupe.flamer.pojo.UserInterestAndFriendQuaryData;
import com.appdupe.flamer.pojo.userProFileData;
import com.appdupe.flamer.utility.AlertDialogManager;
import com.appdupe.flamer.utility.ConnectionDetector;
import com.appdupe.flamer.utility.Constant;
import com.appdupe.flamer.utility.ExtendedGallery;
import com.appdupe.flamer.utility.SessionManager;
import com.appdupe.flamer.utility.Ultilities;
import com.appdupe.flamer.utility.UltilitiesDate;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class MatChedUserProfile extends Activity implements OnClickListener {
	private Session.StatusCallback statusCallback = new SessionStatusCallback();
	private static boolean mDebugLog = true;
	private static String mDebugTag = "MatChedUserProfile";

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

	private ExtendedGallery imageExtendedGallery;
	private LinearLayout image_count;
	private TextView usernametextivew, ueragetextviw, distancetextview,
			activetimetextview, abouttextview, abouttextviewvalues,
			myfriendssharecont, userInterestedcount, usersharedfriend,
			usersahredInterested, viewMatchedProfiletextview;
	private RelativeLayout Aboutuseragelayout, likedislikebuttonlayout,
			userFriendPhotogallery, userInterestedGallery, gallery_paging;
	private HorizontalListView userfriendgallery, userIntestedgallery;
	private Button likeButton, dislikebutton;
	private ArrayList<GellaryData> imageList;
	private ArrayList<GellaryData> userintrestdata;
	private ArrayList<GellaryData> userFriendlist;

	private ImageAdapterForGellary mAdapterForGellary;
	private ImageAdapterForGellaryInterested mAdapterForGellaryInterested;
	private ImageAdapterForGellaryfriends mAdapterForGellaryfriends;
	private ProgressDialog mDialog;
	private int[] imageHeightandWIdth;
	private ConnectionDetector cd;

	private RelativeLayout.LayoutParams layoutParams;
	private int count;
	private TextView[] page_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.matcheduserprofile);
		initLayoutResource();

		userFriendPhotogallery = (RelativeLayout) findViewById(R.id.userFriendPhotogallery);
		userInterestedGallery = (RelativeLayout) findViewById(R.id.userInterestedGallery);
		gallery_paging = (RelativeLayout) findViewById(R.id.gallery_paging);

		imageList = new ArrayList<GellaryData>();
		mAdapterForGellary = new ImageAdapterForGellary(this, imageList);
		imageExtendedGallery.setAdapter(mAdapterForGellary);
		Ultilities ultilities = new Ultilities();

		imageHeightandWIdth = ultilities
				.getImageHeightAndWidthForProfileGellary(this);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.getBoolean(Constant.isFromChatScreen)) {
				likedislikebuttonlayout.setVisibility(View.GONE);
			} else {

			}
		}
		// getUserProfile();

		userintrestdata = new ArrayList<GellaryData>();
		mAdapterForGellaryInterested = new ImageAdapterForGellaryInterested(
				this, userintrestdata);
		userIntestedgallery.setAdapter(mAdapterForGellaryInterested);

		userFriendlist = new ArrayList<GellaryData>();
		mAdapterForGellaryfriends = new ImageAdapterForGellaryfriends(this,
				userFriendlist);
		userfriendgallery.setAdapter(mAdapterForGellaryfriends);

		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

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
				// session.openForRead(new
				// Session.OpenRequest(this).setPermissions(Arrays.asList("user_birthday",
				// "email","user_relationships","user_photos")).setCallback(statusCallback));
			}
		}
		try {
			likeButton.setOnClickListener(this);
			dislikebutton.setOnClickListener(this);
		} catch (Exception e) {
		}

		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
				imageHeightandWIdth[1], imageHeightandWIdth[0]);
		rlp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		// rlp.setMargins(0, imageLayoutHeightandWidth[2], 0, 0);
		gallery_paging.setLayoutParams(rlp);

		layoutParams = ultilities.getRelativelayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layoutParams.addRule(RelativeLayout.BELOW, R.id.userinterestedlayout);
		layoutParams.setMargins(0, 5, 0, 0);
		userInterestedGallery.setLayoutParams(layoutParams);

		layoutParams = ultilities.getRelativelayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layoutParams.addRule(RelativeLayout.BELOW, R.id.myfriendsnamelayout);
		layoutParams.setMargins(0, 5, 0, 0);

		userFriendPhotogallery.setLayoutParams(layoutParams);

		cd = new ConnectionDetector(getApplicationContext());
		if (cd.isConnectingToInternet()) {
			getUserProfile();
			getUserShareeInterest();
		} else {
			AlertDialogManager
					.internetConnetionErrorAlertDialog(MatChedUserProfile.this);

		}

		imageExtendedGallery
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {

						for (int i = 0; i < count; i++) {
							page_text[i]
									.setTextColor(android.graphics.Color.GRAY);
						}
						page_text[pos]
								.setTextColor(android.graphics.Color.WHITE);

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});
	}

	private void initLayoutResource() {
		viewMatchedProfiletextview = (TextView) findViewById(R.id.viewMatchedProfiletextview);
		likeButton = (Button) findViewById(R.id.likeButton);
		dislikebutton = (Button) findViewById(R.id.dislikebutton);
		imageExtendedGallery = (ExtendedGallery) findViewById(R.id.imageExtendedGallery);
		image_count = (LinearLayout) findViewById(R.id.image_count);
		usernametextivew = (TextView) findViewById(R.id.usernametextivew);
		ueragetextviw = (TextView) findViewById(R.id.ueragetextviw);
		distancetextview = (TextView) findViewById(R.id.distancetextview);
		activetimetextview = (TextView) findViewById(R.id.activetimetextview);
		userfriendgallery = (HorizontalListView) findViewById(R.id.userfriendgallery);
		userIntestedgallery = (HorizontalListView) findViewById(R.id.userIntestedgallery);
		abouttextview = (TextView) findViewById(R.id.abouttextview);
		Aboutuseragelayout = (RelativeLayout) findViewById(R.id.Aboutuseragelayout);
		Aboutuseragelayout.setVisibility(View.GONE);
		likedislikebuttonlayout = (RelativeLayout) findViewById(R.id.likedislikebuttonlayout);
		abouttextviewvalues = (TextView) findViewById(R.id.abouttextviewvalues);

		userFriendPhotogallery = (RelativeLayout) findViewById(R.id.userFriendPhotogallery);
		userInterestedGallery = (RelativeLayout) findViewById(R.id.userInterestedGallery);

		usersharedfriend = (TextView) findViewById(R.id.usersharedfriend);
		usersahredInterested = (TextView) findViewById(R.id.usersahredInterested);

		myfriendssharecont = (TextView) findViewById(R.id.myfriendssharecont);

		userInterestedcount = (TextView) findViewById(R.id.userInterestedcount);

		Typeface HelveticaInseratLTStd_Roman = Typeface.createFromAsset(
				getAssets(), "fonts/HelveticaInseratLTStd-Roman.otf");
		Typeface HelveticaLTStd_Light = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Light.otf");
		viewMatchedProfiletextview.setTypeface(HelveticaLTStd_Light);
		viewMatchedProfiletextview.setTextColor(Color.rgb(255, 255, 255));
		viewMatchedProfiletextview.setTextSize(20);

		usernametextivew.setTypeface(HelveticaInseratLTStd_Roman);
		usernametextivew.setTextColor(Color.rgb(124, 124, 124));
		usernametextivew.setTextSize(15);

		ueragetextviw.setTypeface(HelveticaLTStd_Light);
		ueragetextviw.setTextColor(Color.rgb(124, 124, 124));
		ueragetextviw.setTextSize(15);

		usersharedfriend.setTypeface(HelveticaLTStd_Light);
		usersharedfriend.setTextColor(Color.rgb(124, 124, 124));
		usersharedfriend.setTextSize(15);

		usersahredInterested.setTypeface(HelveticaLTStd_Light);
		usersahredInterested.setTextColor(Color.rgb(124, 124, 124));
		usersahredInterested.setTextSize(15);

		myfriendssharecont.setTypeface(HelveticaLTStd_Light);
		myfriendssharecont.setTextColor(Color.rgb(124, 124, 124));
		myfriendssharecont.setTextSize(15);
		userInterestedcount.setTypeface(HelveticaLTStd_Light);
		userInterestedcount.setTextColor(Color.rgb(124, 124, 124));
		userInterestedcount.setTextSize(15);

		abouttextview.setTypeface(HelveticaInseratLTStd_Roman);
		abouttextview.setTextColor(Color.rgb(92, 92, 92));
		abouttextview.setTextSize(15);

		abouttextviewvalues.setTypeface(HelveticaInseratLTStd_Roman);
		abouttextviewvalues.setTextColor(Color.rgb(131, 131, 131));
		abouttextviewvalues.setTextSize(15);

		distancetextview.setTypeface(HelveticaInseratLTStd_Roman);
		distancetextview.setTextColor(Color.rgb(92, 92, 92));
		distancetextview.setTextSize(15);

		activetimetextview.setTypeface(HelveticaInseratLTStd_Roman);
		activetimetextview.setTextColor(Color.rgb(131, 131, 131));
		activetimetextview.setTextSize(15);
	}

	private void getUserProfile() {
		SessionManager mSessionManager = new SessionManager(this);

		String macheduserFacebookid = mSessionManager
				.getMatchedUserFacebookId();
		String userSessionToken = mSessionManager.getUserToken();
		String userDeviceId = Ultilities.getDeviceId(this);
		if (macheduserFacebookid != null && macheduserFacebookid.length() > 0
				&& userSessionToken != null && userSessionToken.length() > 0
				&& userDeviceId != null && userDeviceId.length() > 0) {
			String[] params = { userSessionToken, userDeviceId,
					macheduserFacebookid };
			new BackGroundTaskForUserProfile().execute(params);
		} else {
			ErrorMessageMandetoryFiledMissing(
					getResources().getString(R.string.alert), getResources()
							.getString(R.string.retriedmessage));
		}

	}

	private class BackGroundTaskForUserProfile extends
			AsyncTask<String, Void, Void> {
		Ultilities mUltilities = new Ultilities();
		private String getProfileResponse;
		private List<NameValuePair> userProfileNameValuePairList;
		private userProFileData mUserProFileData;
		private GellaryData mGellaryData;
		private File imageFile;
		private SessionManager sessionManager = new SessionManager(
				MatChedUserProfile.this);

		@Override
		protected Void doInBackground(String... params) {
			try {

				userProfileNameValuePairList = mUltilities
						.getUserProfileParameter(params);
				getProfileResponse = mUltilities.makeHttpRequest(
						Constant.getProfile_url, Constant.methodeName,
						userProfileNameValuePairList);
				// logDebug("BackGroundTaskForUserProfile  getProfileResponse "+getProfileResponse);
				Gson gson = new Gson();
				mUserProFileData = gson.fromJson(getProfileResponse,
						userProFileData.class);

				String[] images = mUserProFileData.getImages();

				for (int i = 0; i < images.length; i++) {
					mGellaryData = new GellaryData();

					mGellaryData.setImageUrl(images[i]);
					imageList.add(mGellaryData);

				}

				runOnUiThread(new Runnable() {
					public void run() {

						if (mDialog != null) {
							mDialog.dismiss();
						}

						page_text = new TextView[imageList.size()];
						count = imageList.size();
						image_count.removeAllViews();
						for (int i = 0; i < imageList.size(); i++) {
							page_text[i] = new TextView(MatChedUserProfile.this);
							page_text[i].setText(".");
							page_text[i].setTextSize(45);
							page_text[i].setTypeface(null, Typeface.BOLD);
							page_text[i]
									.setTextColor(android.graphics.Color.GRAY);
							image_count.addView(page_text[i]);

						}

						mAdapterForGellary.notifyDataSetChanged();
						ueragetextviw.setText("" + mUserProFileData.getAge());
						usernametextivew.setText(""
								+ mUserProFileData.getFirstName());
						viewMatchedProfiletextview.setText(""
								+ mUserProFileData.getFirstName());
						SessionManager sessionManager = new SessionManager(
								MatChedUserProfile.this);
						String DistanceUinit = null;
						if (sessionManager.getDistaceUnit().equals("Km")) {
							DistanceUinit = "Km.";
						} else {
							DistanceUinit = "Mi.";
						}

						distancetextview.setText("Less then "
								+ mUserProFileData.getDistance() + " "
								+ DistanceUinit + " away");
						String gmtTime = mUserProFileData.getLastActive();
						// gmtTime=gmtTime.replaceAll("-", " ");

						String localTime = UltilitiesDate.getLocalTime(gmtTime);
						Ultilities ultilities = new Ultilities();
						// String
						// curentTime=ultilities.getCurrentDateYYYYMMdd();
						String dataString = UltilitiesDate
								.datesString(localTime);
						UltilitiesDate ultilitiesDate = new UltilitiesDate();
						int days = ultilitiesDate.getDays();
						int hours = ultilitiesDate.getHours();

						activetimetextview.setText("active " + days + " -d  "
								+ hours + "- Hour ago");
						if (mUserProFileData.getPersDesc() != null
								&& mUserProFileData.getPersDesc().length() > 0) {
							Aboutuseragelayout.setVisibility(View.VISIBLE);
							abouttextview.setText("About  " + ""
									+ mUserProFileData.getFirstName());
							abouttextviewvalues.setText(""
									+ mUserProFileData.getPersDesc());

						} else {
							Aboutuseragelayout.setVisibility(View.GONE);
						}
					}
				});

				// for (int i = 0; i < imageList.size(); i++)
				// {
				// Bitmap
				// mBitmap=Utility.getBitmap(imageList.get(i).getImageUrl().replaceAll(" ",
				// "%20"));
				// Bitmap scaledBitmap=Bitmap.createScaledBitmap(mBitmap,
				// imageHeightandWIdth[1], imageHeightandWIdth[0], true);
				// imageList.get(i).setmBitmap(scaledBitmap);
				// mBitmap.recycle();
				// runOnUiThread(new Runnable()
				// {
				// public void run()
				// {
				// mAdapterForGellary.notifyDataSetChanged();
				// }
				// });
				// }

			} catch (Exception e) {
				logError("BackGroundTaskForUserProfile   doInBackground Exception"
						+ e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			try {

				if (mDialog != null) {
					mDialog.dismiss();
				}

			} catch (Exception e) {
				logError("BackGroundTaskForUserProfile   onPostExecute Exception  "
						+ e);
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mDialog = mUltilities.GetProcessDialog(MatChedUserProfile.this);
			mDialog.setMessage("Please Wait..");
			mDialog.setCancelable(false);
			mDialog.show();
		}

	}

	private void getUserShareeInterest() {
		Session mSession = Session.getActiveSession();
		SessionManager mSessionManager = new SessionManager(this);

		String[] params = { mSessionManager.getFacebookId(),
				mSessionManager.getMatchedUserFacebookId() };
		try {
			if (mSession.isOpened()) {
				getMysharedIntreste(params);
				// new BackGroundTaskForGetUserInterest().equals(params);
			} else {
				getOpenedSession();
			}
		} catch (Exception e) {
			logDebug("getUserShareeInterest Exception " + e);
			getOpenedSession();
		}
	}

	private void getMysharedIntreste(String[] params) {
		Session mSession = Session.getActiveSession();

		String fqlQuery = "{\"query1\":\"SELECT pic_square,name from page where page_id IN (SELECT page_id  FROM page_fan WHERE uid="
				+ "'"
				+ params[0]
				+ "'"
				+ "AND page_id IN (SELECT page_id FROM page_fan WHERE uid ="
				+ "'"
				+ params[1]
				+ "'"
				+ "))\",\"query2\":\" SELECT uid, name, pic_square FROM user WHERE uid IN (SELECT uid2 FROM friend where uid1="
				+ "'"
				+ params[1]
				+ "'"
				+ " and uid2 in (SELECT uid2 FROM friend where uid1="
				+ "'"
				+ params[0] + "'" + "))\"}";

		Bundle param = new Bundle();
		param.putString("q", fqlQuery);
		// Session session = Session.getActiveSession();
		logDebug("getMysharedIntreste  fqlQuery " + fqlQuery);
		Request request = new Request(mSession, "/fql", param, HttpMethod.GET,
				new Request.Callback() {
					public void onCompleted(Response response) {
						// og.i(TAG, "Result: " + response.toString());

						String[] params = { response.toString() };
						new BackGroundTaskForGetUserInterest().execute(params);

					}
				});

		Request.executeBatchAsync(request);
	}

	private class BackGroundTaskForGetUserInterest extends
			AsyncTask<String, Void, Void> {
		private String intrestrespons;
		private UserInterestAndFriendData userInterestAndFriendData;
		private ArrayList<UserInterestAndFriendQuaryData> userInterestAnsFriendlist;
		// / private ArrayList<GellaryData>useliksdata;
		private Ultilities mUltilities = new Ultilities();
		private UserInterestAndFriendQuaryData andFriendQuaryDataIntrest;
		private UserInterestAndFriendQuaryData andFriendQuaryDatafriend;
		private ArrayList<QuaryOneResult> interestList;
		private ArrayList<QuarySecondResult> FriendList;
		private RelativeLayout.LayoutParams layoutParams;
		private RelativeLayout.LayoutParams layoutParamsfriend;
		int interestImageHeigthandwidth[] = mUltilities
				.getImageHeightAndWidthForInrestAndFriendsLyout(MatChedUserProfile.this);

		@Override
		protected Void doInBackground(String... params) {
			try {
				intrestrespons = params[0];
				Gson gson = new Gson();
				intrestrespons = intrestrespons.substring(
						intrestrespons.indexOf("state=") + 6,
						intrestrespons.indexOf("}, error:"));
				logDebug("BackGroundTaskForGetUserInterest  doInBackground  intrestrespons "
						+ intrestrespons);
				intrestrespons = intrestrespons.replaceFirst("fql_result_set",
						"fql_result_set1");
				userInterestAndFriendData = gson.fromJson(intrestrespons,
						UserInterestAndFriendData.class);
				userInterestAnsFriendlist = userInterestAndFriendData
						.getDatalist();
				andFriendQuaryDataIntrest = userInterestAnsFriendlist.get(0);
				andFriendQuaryDatafriend = userInterestAnsFriendlist.get(1);
				interestList = andFriendQuaryDataIntrest.getInterestList();
				FriendList = andFriendQuaryDatafriend.getFriendList();

				if (interestList != null && interestList.size() > 0) {
					for (int i = 0; i < interestList.size(); i++) {
						GellaryData mGellaryData = new GellaryData();
						mGellaryData.setImageUrl(interestList.get(i)
								.getInterestPicUlt());
						mGellaryData.setInterestedName(interestList.get(i)
								.getInterestName());
						// logDebug("BackGroundTaskForUserProfile   doInBackground mUserProFileData url"+userInterestlist.get(i).getIntestPicurl());
						// logDebug("BackGroundTaskForUserProfile   doInBackground mUserProFileData name"+userInterestlist.get(i).getInteresname());
						userintrestdata.add(mGellaryData);
					}

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (interestList.size() > 35) {
								userInterestedcount.setText("(" + 35 + "+)");
							} else {
								userInterestedcount.setText("("
										+ interestList.size() + ")");
							}
							mAdapterForGellaryInterested.notifyDataSetChanged();

							layoutParams = mUltilities.getRelativelayoutParams(
									RelativeLayout.LayoutParams.FILL_PARENT,
									interestImageHeigthandwidth[0]);
							layoutParams
									.addRule(RelativeLayout.CENTER_HORIZONTAL);
							layoutParams.addRule(RelativeLayout.BELOW,
									R.id.userinterestedlayout);
							layoutParams.setMargins(0, 5, 0, 0);
							userInterestedGallery.setLayoutParams(layoutParams);
						}
					});

				} else {
					// no itereste adde yet
				}

				if (FriendList != null && FriendList.size() > 0) {
					for (int i = 0; i < FriendList.size(); i++) {
						GellaryData mGellaryData = new GellaryData();
						mGellaryData.setImageUrl(FriendList.get(i)
								.getFriendPicUlt());
						mGellaryData.setInterestedName(FriendList.get(i)
								.getFriendName());

						// logDebug("BackGroundTaskForUserProfile   doInBackground mUserProFileData url"+userInterestlist.get(i).getIntestPicurl());
						// logDebug("BackGroundTaskForUserProfile   doInBackground mUserProFileData name"+userInterestlist.get(i).getInteresname());

						userFriendlist.add(mGellaryData);

					}
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (FriendList.size() > 35) {
								myfriendssharecont.setText("(" + 35 + "+)");
							}

							else {
								myfriendssharecont.setText("("
										+ FriendList.size() + ")");
							}

							mAdapterForGellaryfriends.notifyDataSetChanged();
							layoutParamsfriend = mUltilities
									.getRelativelayoutParams(
											RelativeLayout.LayoutParams.FILL_PARENT,
											interestImageHeigthandwidth[0]);
							layoutParamsfriend
									.addRule(RelativeLayout.CENTER_HORIZONTAL);
							layoutParamsfriend.addRule(RelativeLayout.BELOW,
									R.id.myfriendsnamelayout);
							layoutParamsfriend.setMargins(0, 5, 0, 0);
							userFriendPhotogallery
									.setLayoutParams(layoutParamsfriend);

						}
					});
				} else {
					// no any friends

				}

			} catch (Exception e) {
				logError("BackGroundTaskForGetUserInterest  doInBackground exception"
						+ e);

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
	}

	private void getOpenedSession() {
		logDebug("getOpenedSession ");
		Session.openActiveSession(this, true, statusCallback);
	}

	private class ImageAdapterForGellary extends ArrayAdapter<GellaryData> {
		Activity mActivity;
		private LayoutInflater mInflater;
		private Ultilities mUltilities = new Ultilities();
		private int[] imageheightandWidth = mUltilities
				.getImageHeightAndWidthForGellary(MatChedUserProfile.this);

		public ImageAdapterForGellary(Activity context,
				List<GellaryData> objects) {
			super(context, R.layout.galleritem, objects);
			mActivity = context;
			mInflater = (LayoutInflater) mActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return super.getCount();
		}

		@Override
		public GellaryData getItem(int position) {
			// TODO Auto-generated method stub
			return super.getItem(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			// TODO Auto-generated method stub-
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.galleritem, null);
				holder.imageview = (ImageView) convertView
						.findViewById(R.id.thumbImage);
				holder.mProgressBar = (ProgressBar) convertView
						.findViewById(R.id.progressBar1);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.mProgressBar.setId(position);
			holder.imageview.setId(position);

			Picasso.with(MatChedUserProfile.this) //
					.load(getItem(position).getImageUrl()) //
					/* .placeholder(R.drawable.placeholder) *///
					.error(R.drawable.error) //
					.resize(imageHeightandWIdth[1], imageHeightandWIdth[0]) //
					.into(holder.imageview);
			// if (getItem(position).getmBitmap()!=null)
			// {
			//
			// holder.mProgressBar.setVisibility(View.GONE);
			// holder.imageview.setImageBitmap(getItem(position).getmBitmap());
			// }
			// else
			// {
			//
			// }
			return convertView;
		}

		class ViewHolder {
			ImageView imageview;
			ProgressBar mProgressBar;

		}
	}

	private class ImageAdapterForGellaryInterested extends
			ArrayAdapter<GellaryData> {

		private LayoutInflater mInflater;
		private AQuery aQuery;
		RequestQueue mRequestQueue = Volley
				.newRequestQueue(MatChedUserProfile.this);
		Typeface HelveticaLTStd_Light = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Light.otf");

		private ImageLoader imageLoader = new ImageLoader(mRequestQueue,
				new BitmapLruCache(BitmapLruCache.getDefaultLruCacheSize()));

		public ImageAdapterForGellaryInterested(Context context,
				List<GellaryData> objects) {
			super(context, R.layout.myintrested, objects);
			// TODO Auto-generated constructor stub
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			aQuery = new AQuery(context);

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub

			return super.getCount();
		}

		@Override
		public GellaryData getItem(int position) {
			// TODO Auto-generated method stub
			return super.getItem(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.myintrested, null);
				holder.imageview = (ImageView) convertView
						.findViewById(R.id.userIterestgalley);
				holder.textView = (TextView) convertView
						.findViewById(R.id.myintrestedname);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.textView.setTypeface(HelveticaLTStd_Light);
			holder.textView.setTextColor(Color.rgb(124, 124, 124));
			holder.textView.setTextSize(15);
			holder.imageview.setImageResource(R.drawable.circled_book_icon);
			holder.textView.setId(position);
			holder.imageview.setId(position);

			String interestedName = getItem(position).getInterestedName();
			logDebug("ImageAdapterForGellaryInterested getView   interestedName "
					+ interestedName);
			if (interestedName != null && interestedName.length() > 0) {
				try {
					interestedName = interestedName.substring(0,
							interestedName.indexOf(" "));
				} catch (Exception e) {
					// TODO: handle exception
				}

			} else {
				interestedName = "";
			}
			holder.textView.setText(interestedName);

			// holder.imageview.setImageUrl(getItem(position).getImageUrl(),
			// imageLoader, "CircularImge", MatChedUserProfile.this);
			// holder.imageview.setImageUrl(getItem(position).getImageUrl(),
			// imageLoader);
			aQuery.id(holder.imageview).image(getItem(position).getImageUrl());

			return convertView;
		}

		class ViewHolder {
			ImageView imageview;
			TextView textView;

		}
	}

	private class ImageAdapterForGellaryfriends extends
			ArrayAdapter<GellaryData> {
		// Activity mActivity=getActivity();
		private LayoutInflater mInflater;
		private AQuery aQuery;
		RequestQueue mRequestQueue = Volley
				.newRequestQueue(MatChedUserProfile.this);
		Typeface HelveticaLTStd_Light = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Light.otf");
		private ImageLoader imageLoader = new ImageLoader(mRequestQueue,
				new BitmapLruCache(BitmapLruCache.getDefaultLruCacheSize()));

		public ImageAdapterForGellaryfriends(Context context,
				List<GellaryData> objects) {
			super(context, R.layout.myintrested, objects);
			// TODO Auto-generated constructor stub
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			aQuery = new AQuery(context);
			// logDebug("ImageAdapterForGellaryInterested   context "+context);
			// logDebug("ImageAdapterForGellaryInterested   objects "+objects);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			// logDebug("ImageAdapterForGellaryInterested  getCount count "+super.getCount());
			return super.getCount();
		}

		@Override
		public GellaryData getItem(int position) {
			// TODO Auto-generated method stub
			return super.getItem(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			// TODO Auto-generated method stub]

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.myintrested, null);
				holder.imageview = (ImageView) convertView
						.findViewById(R.id.userIterestgalley);
				holder.textView = (TextView) convertView
						.findViewById(R.id.myintrestedname);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.textView.setTypeface(HelveticaLTStd_Light);
			holder.textView.setTextColor(Color.rgb(124, 124, 124));
			holder.textView.setTextSize(15);
			holder.textView.setId(position);
			holder.imageview.setId(position);

			String friendName = getItem(position).getInterestedName();
			if (friendName != null && friendName.length() > 0) {
				friendName = friendName.substring(0, friendName.indexOf(" "));
			} else {
				friendName = "not found";
			}

			holder.textView.setText(friendName);
			holder.imageview.setImageResource(R.drawable.multi_user_icon);

			// holder.imageview.setImageUrl(getItem(position).getImageUrl(),
			// imageLoader, "CircularImge", MatChedUserProfile.this);
			// holder.imageview.setImageUrl(getItem(position).getImageUrl(),
			// imageLoader);
			aQuery.id(holder.imageview).image(getItem(position).getImageUrl());

			return convertView;
		}

		class ViewHolder {
			ImageView imageview;
			TextView textView;

		}
	}

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

	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			// updateView();

			if (session.isOpened()) {
				// /Session mSession=Session.getActiveSession();
				SessionManager mSessionManager = new SessionManager(
						MatChedUserProfile.this);

				String[] params = { mSessionManager.getFacebookId(),
						mSessionManager.getMatchedUserFacebookId() };
				try {

					getMysharedIntreste(params);
					// new BackGroundTaskForGetUserInterest().equals(params);

				} catch (Exception e) {
					// TODO: handle exception
					getOpenedSession();
				}
			}

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.likeButton) {
			likeMatchedUser("1");
		}
		if (v.getId() == R.id.dislikebutton) {
			likeMatchedUser("2");
		}

	}

	private void likeMatchedUser(String action) {
		SessionManager mSessionManager = new SessionManager(
				MatChedUserProfile.this);
		String sessionToke = mSessionManager.getUserToken();
		String devieceId = Ultilities.getDeviceId(MatChedUserProfile.this);
		String MatchedUserFacebookId = mSessionManager
				.getMatchedUserFacebookId();
		String userAction = action;
		String[] params = { sessionToke, devieceId, MatchedUserFacebookId,
				userAction };

		new BackGroundTaskForInviteAction().execute(params);

	}

	private class BackGroundTaskForInviteAction extends
			AsyncTask<String, Void, Void> {

		private String inviteActionResponse;
		private List<NameValuePair> inviteactionparamlist;
		private InviteActionData mActionData;
		private Ultilities mUltilities = new Ultilities();

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				inviteactionparamlist = mUltilities
						.getInviteActionParameter(params);

				inviteActionResponse = mUltilities.makeHttpRequest(
						Constant.inviteaction_url, Constant.methodeName,
						inviteactionparamlist);

				Gson gson = new Gson();
				mActionData = gson.fromJson(inviteActionResponse,
						InviteActionData.class);

			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mDialog = mUltilities.GetProcessDialog(MatChedUserProfile.this);
			mDialog.setMessage("Please wait..");
			mDialog.setCancelable(true);
			mDialog.show();
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				mDialog.dismiss();

				/*
				 * Error: { "errNum": "37", "errFlag": "1", "errMsg":
				 * "Server Error! Please try again after sometime!" }
				 * 
				 * Success: { "errNum": "29", "errFlag": "0", "errMsg":
				 * "Likes sent!" }
				 */

				if (mActionData.getErrNum() == 29
						&& mActionData.getErrFlag() == 0) {
					SessionManager mSessionManager = new SessionManager(
							MatChedUserProfile.this);
					mSessionManager.isInviteActionSucess(true);

					finish();
				} else if (mActionData.getErrNum() == 37
						&& mActionData.getErrFlag() == 1) {
					ErrorMessage("alrte", mActionData.getErrMsg());
				} else {
					ErrorMessage("alrte",
							"sorry Server Error! Please try again after sometime!");
				}
				finish();

			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

	private void ErrorMessage(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				MatChedUserProfile.this);
		builder.setTitle(title);
		builder.setMessage(message);

		builder.setPositiveButton(
				getResources().getString(R.string.okbuttontext),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});

		AlertDialog alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}

	private void ErrorMessageMandetoryFiledMissing(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				MatChedUserProfile.this);
		builder.setTitle(title);
		builder.setMessage(message);

		builder.setPositiveButton(
				getResources().getString(R.string.okbuttontext),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
						finish();
					}
				});

		AlertDialog alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}

}
