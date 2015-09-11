package com.android.slidingmenu;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.appdupe.flamer.pojo.GellaryData;
import com.appdupe.flamer.pojo.ImageDetail;
import com.appdupe.flamer.pojo.QuaryOneResult;
import com.appdupe.flamer.pojo.QuarySecondResult;
import com.appdupe.flamer.pojo.UserFriendsData;
import com.appdupe.flamer.pojo.UserInterestAndFriendData;
import com.appdupe.flamer.pojo.UserInterestAndFriendQuaryData;
import com.appdupe.flamer.pojo.UserfriendData;
import com.appdupe.flamer.pojo.userProFileData;
import com.appdupe.flamer.utility.AlertDialogManager;
import com.appdupe.flamer.utility.Constant;
import com.appdupe.flamer.utility.ConnectionDetector;
import com.appdupe.flamer.utility.SessionManager;
import com.appdupe.flamer.utility.Ultilities;
import com.appdupe.flamer.utility.Utility;
import com.appdupe.flamer.R;
import com.appdupe.flamerchat.db.DatabaseHandler;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.google.gson.Gson;

public class Layout2 extends Fragment {
	private static final String TAG = "LAYOUT2";
	private static boolean mDebugLog = true;
	private static String mDebugTag = "Layout1";

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

	private com.appdupe.flamer.utility.ExtendedGallery imageExtendedGallery;
	private ArrayList<GellaryData> imageList;
	private ArrayList<GellaryData> userintrestdata;
	private ArrayList<GellaryData> userFriendlist;
	private ImageAdapterForGellary mAdapterForGellary;
	private ImageAdapterForGellaryInterested mAdapterForGellaryInterested;
	private ImageAdapterForGellaryfriends mAdapterForGellaryfriends;
	private LinearLayout count_layout;
	private int count;
	private TextView[] page_text;
	private TextView usernametextivew, ueragetextviw, aboutuserTextview,
			userboutTextview, intrestcont, friendcount, myfriends, openBreces,
			closebracess, userInterested, openBrecesForFriend,
			closebracessforFriend;
	private HorizontalListView userIntestedgallery;
	private HorizontalListView userfriendgallery;

	private RelativeLayout imagegalleylayout, gallery_paging;
	private RelativeLayout userAboutLayout, useFriendlayout, useInterest;
	private ProgressDialog mDialog;
	private int[] imageHeightandWIdth;
	private RelativeLayout.LayoutParams layoutParams;
	private ConnectionDetector cd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_layout2, null);

		Ultilities ultilities = new Ultilities();
		Typeface HelveticaInseratLTStd_Roman = Typeface.createFromAsset(
				getActivity().getAssets(),
				"fonts/HelveticaInseratLTStd-Roman.otf");
		Typeface HelveticaLTStd_Light = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/HelveticaLTStd-Light.otf");
		imageHeightandWIdth = ultilities
				.getImageHeightAndWidthForProfileGellary(getActivity());

		imageExtendedGallery = (com.appdupe.flamer.utility.ExtendedGallery) view
				.findViewById(R.id.imageExtendedGallery);
		userIntestedgallery = (HorizontalListView) view
				.findViewById(R.id.userIntestedgallery);
		userfriendgallery = (HorizontalListView) view
				.findViewById(R.id.userfriendgallery);
		gallery_paging = (RelativeLayout) view
				.findViewById(R.id.gallery_paging);
		useFriendlayout = (RelativeLayout) view
				.findViewById(R.id.useFriendlayout);

		myfriends = (TextView) view.findViewById(R.id.myfriends);
		openBreces = (TextView) view.findViewById(R.id.openBreces);
		closebracess = (TextView) view.findViewById(R.id.closebracess);

		myfriends.setTypeface(HelveticaLTStd_Light);
		myfriends.setTextColor(Color.rgb(124, 124, 124));

		openBreces.setTypeface(HelveticaLTStd_Light);
		openBreces.setTextColor(Color.rgb(124, 124, 124));

		closebracess.setTypeface(HelveticaLTStd_Light);
		closebracess.setTextColor(Color.rgb(124, 124, 124));

		userInterested = (TextView) view.findViewById(R.id.userInterested);
		openBrecesForFriend = (TextView) view
				.findViewById(R.id.openBrecesForFriend);
		closebracessforFriend = (TextView) view
				.findViewById(R.id.closebracessforFriend);

		userInterested.setTypeface(HelveticaLTStd_Light);
		userInterested.setTextColor(Color.rgb(124, 124, 124));

		openBrecesForFriend.setTypeface(HelveticaLTStd_Light);
		openBrecesForFriend.setTextColor(Color.rgb(1124, 124, 124));

		closebracessforFriend.setTypeface(HelveticaLTStd_Light);
		closebracessforFriend.setTextColor(Color.rgb(124, 124, 124));

		useInterest = (RelativeLayout) view.findViewById(R.id.useInterest);
		userboutTextview = (TextView) view.findViewById(R.id.userboutTextview);

		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
				imageHeightandWIdth[1], imageHeightandWIdth[0]);
		rlp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		// rlp.setMargins(0, imageLayoutHeightandWidth[2], 0, 0);
		gallery_paging.setLayoutParams(rlp);

		intrestcont = (TextView) view.findViewById(R.id.intrestcont);
		friendcount = (TextView) view.findViewById(R.id.friendcount);

		intrestcont.setTypeface(HelveticaLTStd_Light);
		intrestcont.setTextColor(Color.rgb(169, 169, 169));

		friendcount.setTypeface(HelveticaLTStd_Light);
		friendcount.setTextColor(Color.rgb(169, 169, 169));

		usernametextivew = (TextView) view.findViewById(R.id.usernametextivew);
		ueragetextviw = (TextView) view.findViewById(R.id.ueragetextviw);
		count_layout = (LinearLayout) view.findViewById(R.id.image_count);
		aboutuserTextview = (TextView) view
				.findViewById(R.id.aboutuserTextview);
		imagegalleylayout = (RelativeLayout) view
				.findViewById(R.id.imagegalleylayout);
		userAboutLayout = (RelativeLayout) view
				.findViewById(R.id.userAboutLayout);
		userAboutLayout.setVisibility(View.GONE);
		imageList = new ArrayList<GellaryData>();
		mAdapterForGellary = new ImageAdapterForGellary(getActivity(),
				imageList);
		SessionManager mSessionManager = new SessionManager(getActivity());
		new BackGroundTaskForDownloadProfileImage().execute();

		usernametextivew.setTypeface(HelveticaInseratLTStd_Roman);
		usernametextivew.setTextColor(Color.rgb(124, 124, 124));
		usernametextivew.setTextSize(17);

		ueragetextviw.setTypeface(HelveticaLTStd_Light);
		ueragetextviw.setTextColor(Color.rgb(124, 124, 124));
		ueragetextviw.setTextSize(17);

		userboutTextview.setTypeface(HelveticaInseratLTStd_Roman);
		userboutTextview.setTextColor(Color.rgb(92, 92, 92));
		userboutTextview.setTextSize(17);

		aboutuserTextview.setTypeface(HelveticaInseratLTStd_Roman);
		aboutuserTextview.setTextColor(Color.rgb(131, 131, 131));
		aboutuserTextview.setTextSize(17);

		layoutParams = ultilities.getRelativelayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layoutParams.addRule(RelativeLayout.BELOW, R.id.useFriendlayout);
		layoutParams.setMargins(0, 10, 0, 0);
		useInterest.setLayoutParams(layoutParams);

		layoutParams = ultilities.getRelativelayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layoutParams.addRule(RelativeLayout.BELOW, R.id.userAboutLayout);
		layoutParams.setMargins(0, 10, 0, 0);

		useFriendlayout.setLayoutParams(layoutParams);

		// if (!mSessionManager.getProFileIsCallde())
		// {
		// getUserProfile(); //
		// profile image is already called not need to call this service again
		// fetch profile image from sdcard;

		// }
		// else
		// {

		// }
		imageExtendedGallery.setAdapter(mAdapterForGellary);
		userintrestdata = new ArrayList<GellaryData>();

		mAdapterForGellaryInterested = new ImageAdapterForGellaryInterested(
				getActivity(), userintrestdata);
		userIntestedgallery.setAdapter(mAdapterForGellaryInterested);

		userFriendlist = new ArrayList<GellaryData>();
		mAdapterForGellaryfriends = new ImageAdapterForGellaryfriends(
				getActivity(), userFriendlist);
		userfriendgallery.setAdapter(mAdapterForGellaryfriends);

		cd = new ConnectionDetector(getActivity());
		if (cd.isConnectingToInternet()) {
			getUserInterest();
		} else {
			AlertDialogManager.internetConnetionErrorAlertDialog(getActivity());
		}

		// getUserFriend();

		imageExtendedGallery
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {

						// try
						// {

						for (int i = 0; i < count; i++) {
							page_text[i]
									.setTextColor(android.graphics.Color.GRAY);
						}
						page_text[pos]
								.setTextColor(android.graphics.Color.WHITE);
						// } catch (Exception e)
						// {
						// logError("onItemSelected  Exception "+e);
						// }

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		SessionManager sessionManager = new SessionManager(getActivity());
		if (sessionManager.isImageChange()) {
			sessionManager.setIsImageChange(false);
			new BackGroundTaskForDownloadProfileImage().execute();
		} else {
			if (sessionManager.getUserAbout() != null
					&& sessionManager.getUserAbout().length() > 0) {
				userAboutLayout.setVisibility(View.VISIBLE);
				aboutuserTextview.setText(sessionManager.getUserAbout());
			}
		}
	}

	private void getUserProfile() {
		SessionManager mSessionManager = new SessionManager(getActivity());
		String userFaceBookid = mSessionManager.getFacebookId();
		String userSessionToken = mSessionManager.getUserToken();
		String userDeviceId = Ultilities.getDeviceId(getActivity());

		String[] params = { userSessionToken, userDeviceId, userFaceBookid };
		new BackGroundTaskForUserProfile().execute(params);
	}

	private void getUserInterest() {

		Session mSession = Session.getActiveSession();
		SessionManager mSessionManager = new SessionManager(getActivity());

		String[] params = { mSessionManager.getFacebookId() };
		try {
			if (mSession.isOpened()) {
				getMyIntreste(params);
				// new BackGroundTaskForGetUserInterest().equals(params);
			} else {
				getOpenedSession();
			}
		} catch (Exception e) {
			getOpenedSession();
		}
	}

	// private void getUserFriend()
	// {
	//
	// try
	// {
	// Session mSession=Session.getActiveSession();
	// SessionManager mSessionManager =new SessionManager(getActivity());
	// // logDebug("getUserFriend  mSession  "+mSession);
	// String [] params={mSessionManager.getFacebookId()};
	// //logDebug("getUserFriend  params  "+params);
	// if (mSession!=null&&mSession.isOpened())
	// {
	// getUseFriends(params);
	// //new BackGroundTaskForGetUserInterest().equals(params);
	// }
	// else
	// {
	// getOpenedSession();
	// }
	// }
	// catch (Exception e)
	// {
	// //getOpenedSession();
	// }
	// }

	private void getMyIntreste(String[] params) {
		Session mSession = Session.getActiveSession();

		// TODO Query
		String fqlQuery = "{\"query1\":\"SELECT pic,name from page where page_id IN (SELECT page_id FROM page_fan WHERE uid="
				+ "'"
				+ params[0]
				+ "'"
				+ " )\",\"query2\":\"SELECT  name, pic_with_logo  FROM user WHERE uid IN (SELECT uid2 FROM friend WHERE uid1 ="
				+ "'" + params[0] + "'" + ")\"}";

		// String fqlQuery =
		// "{\"query1\":\"SELECT pic_with_logo,name from page where page_id IN (SELECT page_id FROM page_fan WHERE uid="
		// + "'"
		// + params[0]
		// + "'"
		// +
		// " )\",\"query2\":\"SELECT  name, pic_with_logo  FROM user WHERE uid IN (SELECT uid2 FROM friend WHERE uid1 ="
		// + "'" + params[0] + "'" + ")\"}";

		// pic_with_logo

		Bundle param = new Bundle();
		param.putString("q", fqlQuery);

		Request request = new Request(mSession, "/fql", param, HttpMethod.GET,
				new Request.Callback() {
					public void onCompleted(Response response) {
						String[] params = { response.toString() };
						Log.i(TAG, "***FQL Response :: " + response);
						new BackGroundTaskForGetUserInterest().execute(params);

					}
				});

		Request.executeBatchAsync(request);
	}

	// private void getUseFriends(String [] params)
	// {
	//
	// Session mSession=Session.getActiveSession();
	//
	//
	//
	//
	// //String fqlQuery =
	// "SELECT uid, name, pic_square  FROM user WHERE uid IN (SELECT uid2 FROM friend WHERE uid1 ="+"'"+params[0]+"'"+") LIMIT 35";
	//
	//
	// //String fqlQuery =
	// "select uid1, uid2 FROM friend WHERE uid1 = '100003056725155'";
	// //String fqlQuery =
	// "select src from photo  where album_object_id IN (SELECT  object_id   FROM album WHERE owner='params[0]' and name='Profile Pictures'";
	// //SELECT pic_small,name from page where page_id IN (SELECT page_id FROM
	// page_fan WHERE uid='100003056725155' LIMIT 35)
	// //logDebug("getUseFriends  fqlQuery"+fqlQuery);
	// Bundle param = new Bundle();
	// // param.putString("q", fqlQuery);
	// // Session session = Session.getActiveSession();
	// Request request = new Request(mSession,"/fql",param,HttpMethod.GET,new
	// Request.Callback()
	// {
	// public void onCompleted(Response response)
	// {
	// //og.i(TAG, "Result: " + response.toString());
	// // logDebug(" getUseFriends response "+response);
	// String [] params={response.toString()} ;
	// //
	// logDebug("facebook stuf  getUseFriends  onCompleted response "+response);
	// new BackGroundTaskForGetUserFreinds().execute(params);
	//
	//
	//
	// }
	// });
	//
	// Request.executeBatchAsync(request);
	// }

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
		int interestImageHeigthandwidth[] = mUltilities
				.getImageHeightAndWidthForInrestAndFriendsLyout(getActivity());

		@Override
		protected Void doInBackground(String... params) {
			try {
				intrestrespons = params[0];

				Gson gson = new Gson();

				intrestrespons = intrestrespons.substring(
						intrestrespons.indexOf("state=") + 6,
						intrestrespons.indexOf("}, error:"));

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

					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (interestList.size() > 35) {
								intrestcont.setText("" + 35 + "+");
							} else {
								intrestcont.setText("" + interestList.size());
							}
							layoutParams = mUltilities.getRelativelayoutParams(

							RelativeLayout.LayoutParams.MATCH_PARENT,
									interestImageHeigthandwidth[0]

							);
							layoutParams
									.addRule(RelativeLayout.CENTER_HORIZONTAL);
							layoutParams.addRule(RelativeLayout.BELOW,
									R.id.useFriendlayout);
							layoutParams.setMargins(0, 5, 0, 0);
							useInterest.setLayoutParams(layoutParams);
							mAdapterForGellaryInterested.notifyDataSetChanged();

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
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (FriendList.size() > 35) {
								friendcount.setText("" + 35 + "+");
							} else {
								friendcount.setText("" + FriendList.size());
							}

							layoutParams = mUltilities.getRelativelayoutParams(
									RelativeLayout.LayoutParams.FILL_PARENT,
									interestImageHeigthandwidth[0]);
							layoutParams
									.addRule(RelativeLayout.CENTER_HORIZONTAL);
							layoutParams.addRule(RelativeLayout.BELOW,
									R.id.userAboutLayout);
							layoutParams.setMargins(0, 5, 0, 0);
							useFriendlayout.setLayoutParams(layoutParams);

							mAdapterForGellaryfriends.notifyDataSetChanged();

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
			// downLoadInterestImage();
			// downloadFriendImage();

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

	}

	private void downLoadInterestImage() {
		new DownLoadInteresImage().execute();
	}

	private void downloadFriendImage() {
		new DownloadFriendImage().execute();

	}

	private class DownloadFriendImage extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			if (userFriendlist != null && userFriendlist.size() > 0) {

				for (int i = 0; i < userFriendlist.size(); i++) {

					Ultilities ultilities = new Ultilities();

					Bitmap mBitmapTem = Utility.getBitmap(userFriendlist.get(i)
							.getImageUrl());
					// Bitmap scaledBitmap=Bitmap.createScaledBitmap(mBitmapTem,
					// interestImageHeigthandwidth[1],
					// interestImageHeigthandwidth[0], true);
					mBitmapTem.recycle();
					// Bitmap mBitmap= ultilities.getCircleBitmap(scaledBitmap,
					// 16);
					// scaledBitmap.recycle();
					// logDebug("BackGroundTaskForDownloadProfileImage  doInBackground mBitmap"+mBitmap);
					// userFriendlist.get(i).setmBitmap(mBitmap);
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							mAdapterForGellaryfriends.notifyDataSetChanged();

						}
					});
				}
			}

			return null;
		}

	}

	private class DownLoadInteresImage extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {

			if (userintrestdata != null && userintrestdata.size() > 0) {
				for (int i = 0; i < userintrestdata.size(); i++) {

					Ultilities ultilities = new Ultilities();
					// int
					// interestImageHeigthandwidth[]=ultilities.getImageHeightAndWidthForInrestAndFriends(getActivity());
					Bitmap mBitmapTem = Utility.getBitmap(userintrestdata
							.get(i).getImageUrl());
					Bitmap scaledBitmap = Bitmap.createScaledBitmap(mBitmapTem,
							20, 20, true);
					mBitmapTem.recycle();
					Bitmap mBitmap = ultilities.getCircleBitmap(scaledBitmap,
							16);
					scaledBitmap.recycle();

					// userintrestdata.get(i).setmBitmap(mBitmap);
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							mAdapterForGellaryInterested.notifyDataSetChanged();

						}
					});
				}
			} else {
				// /
			}
			return null;
		}

	}

	private class BackGroundTaskForGetUserFreinds extends
			AsyncTask<String, Void, Void> {
		private String userFriendsrespons;
		private UserFriendsData userFriendsdata;
		private ArrayList<UserFriendsData> userfriendsdatalist;
		// private ArrayList<GellaryData>useliksdata;
		private Ultilities mUltilities = new Ultilities();
		private UserfriendData mUserfriendData;

		@Override
		protected Void doInBackground(String... params) {
			try {
				userFriendsrespons = params[0];
				// logDebug("BackGroundTaskForGetUserFreinds  doInBackground "+userFriendsrespons);
				Gson gson = new Gson();
				userFriendsrespons = userFriendsrespons.substring(
						userFriendsrespons.indexOf("state=") + 6,
						userFriendsrespons.indexOf("}, error:"));
				// logDebug("BackGroundTaskForGetUserFreinds  doInBackground   userFriendsrespons final"+userFriendsrespons);
				mUserfriendData = gson.fromJson(userFriendsrespons,
						UserfriendData.class);
				// logDebug("BackGroundTaskForGetUserFreinds   doInBackground mUserProFileData "+userFriendsdata);
				userfriendsdatalist = mUserfriendData.getData();
				// useliksdata=new ArrayList<GellaryData>();
				for (int i = 0; i < userfriendsdatalist.size(); i++) {
					GellaryData mGellaryData = new GellaryData();
					mGellaryData.setImageUrl(userfriendsdatalist.get(i)
							.getFriendPicurl());
					mGellaryData.setInterestedName(userfriendsdatalist.get(i)
							.getFriendname());
					// logDebug("BackGroundTaskForUserProfile   doInBackground mUserProFileData url"+userInterestlist.get(i).getIntestPicurl());
					// logDebug("BackGroundTaskForUserProfile   doInBackground mUserProFileData name"+userInterestlist.get(i).getInteresname());

					userFriendlist.add(mGellaryData);
				}
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mAdapterForGellaryfriends.notifyDataSetChanged();

					}
				});

				for (int i = 0; i < userFriendlist.size(); i++) {
					// logDebug("BackGroundTaskForDownloadProfileImage  doInBackground image url"+userFriendlist.get(i).getImageUrl());
					Ultilities ultilities = new Ultilities();
					// int
					// interestImageHeigthandwidth[]=ultilities.getImageHeightAndWidthForInrestAndFriends(getActivity());

					Bitmap mBitmapTem = Utility.getBitmap(userFriendlist.get(i)
							.getImageUrl());
					// Bitmap scaledBitmap=Bitmap.createScaledBitmap(mBitmapTem,
					// interestImageHeigthandwidth[1],
					// interestImageHeigthandwidth[0], true);
					mBitmapTem.recycle();
					// Bitmap mBitmap= mUltilities.getCircleBitmap(scaledBitmap,
					// 16);
					// scaledBitmap.recycle();
					// logDebug("BackGroundTaskForDownloadProfileImage  doInBackground mBitmap"+mBitmap);
					// userFriendlist.get(i).setmBitmap(mBitmap);
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							mAdapterForGellaryfriends.notifyDataSetChanged();

						}
					});
				}

			} catch (Exception e) {
				logError("BackGroundTaskForGetUserFreinds  doInBackground exception"
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

	/*
	 * private class DownLoaduseIterestImage extends AsyncTask<String,Void,
	 * Void> {
	 * 
	 * @Override protected Void doInBackground(String... params) { return null;
	 * }
	 * 
	 * }
	 */

	private void getOpenedSession() {
		Session.openActiveSession(getActivity(), true, statusCallback);
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
				getActivity());

		@Override
		protected Void doInBackground(String... params) {
			try {

				File appDirectory = mUltilities
						.createAppDirectoy(getResources().getString(
								R.string.appdirectory));
				File _picDir = new File(appDirectory, getResources().getString(
						R.string.imagedire));
				mUltilities.deleteNon_EmptyDir(_picDir);
				userProfileNameValuePairList = mUltilities
						.getUserProfileParameter(params);
				getProfileResponse = mUltilities.makeHttpRequest(
						Constant.getProfile_url, Constant.methodeName,
						userProfileNameValuePairList);

				Gson gson = new Gson();
				mUserProFileData = gson.fromJson(getProfileResponse,
						userProFileData.class);

				String[] images = mUserProFileData.getImages();
				// //String profileImage=mUserProFileData.getProfilePic();
				// logDebug("BackGroundTaskForUserProfile   doInBackground profileImage  "+profileImage);
				// /mGellaryData=new GellaryData();
				// mGellaryData.setImageUrl(profileImage);
				// imageList.add(mGellaryData);

				for (int i = 0; i < images.length; i++) {
					mGellaryData = new GellaryData();
					mGellaryData.setImageUrl(images[i]);
					imageList.add(mGellaryData);

				}

				for (int i = 0; i < imageList.size(); i++) {
					switch (i) {
					case 0:
						sessionManager.setProFilePicture(imageList.get(i)
								.getImageUrl());
						imageFile = mUltilities.createFileInSideDirectory(
								_picDir,
								getResources()
										.getString(R.string.imagefilename)
										+ i
										+ ".jpg");

						com.appdupe.flamer.utility.Utility
								.addBitmapToSdCardFromURL(imageList.get(i)
										.getImageUrl().replaceAll(" ", "%20"),
										imageFile);
						break;
					case 1:
						sessionManager.setProFilePicture1(imageList.get(i)
								.getImageUrl());
						imageFile = mUltilities.createFileInSideDirectory(
								_picDir,
								getResources()
										.getString(R.string.imagefilename)
										+ i
										+ ".jpg");

						com.appdupe.flamer.utility.Utility
								.addBitmapToSdCardFromURL(imageList.get(i)
										.getImageUrl().replaceAll(" ", "%20"),
										imageFile);
						break;
					case 2:
						sessionManager.setProFilePicture2(imageList.get(i)
								.getImageUrl());
						imageFile = mUltilities.createFileInSideDirectory(
								_picDir,
								getResources()
										.getString(R.string.imagefilename)
										+ i
										+ ".jpg");

						com.appdupe.flamer.utility.Utility
								.addBitmapToSdCardFromURL(imageList.get(i)
										.getImageUrl().replaceAll(" ", "%20"),
										imageFile);
						break;
					case 3:
						sessionManager.setProFilePicture3(imageList.get(i)
								.getImageUrl());
						imageFile = mUltilities.createFileInSideDirectory(
								_picDir,
								getResources()
										.getString(R.string.imagefilename)
										+ i
										+ ".jpg");

						com.appdupe.flamer.utility.Utility
								.addBitmapToSdCardFromURL(imageList.get(i)
										.getImageUrl().replaceAll(" ", "%20"),
										imageFile);
						break;
					case 4:
						sessionManager.setProFilePicture4(imageList.get(i)
								.getImageUrl());
						imageFile = mUltilities.createFileInSideDirectory(
								_picDir,
								getResources()
										.getString(R.string.imagefilename)
										+ i
										+ ".jpg");

						com.appdupe.flamer.utility.Utility
								.addBitmapToSdCardFromURL(imageList.get(i)
										.getImageUrl().replaceAll(" ", "%20"),
										imageFile);
						break;
					case 5:
						sessionManager.setProFilePicture5(imageList.get(i)
								.getImageUrl());
						imageFile = mUltilities.createFileInSideDirectory(
								_picDir,
								getResources()
										.getString(R.string.imagefilename)
										+ i
										+ ".jpg");

						com.appdupe.flamer.utility.Utility
								.addBitmapToSdCardFromURL(imageList.get(i)
										.getImageUrl().replaceAll(" ", "%20"),
										imageFile);
						break;

					}
				}

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

				mAdapterForGellary.notifyDataSetChanged();
				// count = imageExtendedGallery.getAdapter().getCount();
				// System.out.println("Gallery Image Count======>>>" + count);
				// logDebug("BackGroundTaskForUserProfile   onPostExecute count  "+count);
				//
				// page_text = new TextView[mAdapterForGellary.getCount()];
				// for (int i = 0; i < count; i++)
				// {
				// page_text[i] = new TextView(getActivity());
				// page_text[i].setText(".");
				// page_text[i].setTextSize(45);
				// page_text[i].setTypeface(null, Typeface.BOLD);
				// page_text[i].setTextColor(android.graphics.Color.GRAY);
				// count_layout.addView(page_text[i]);
				// }
				new BackGroundTaskForDownloadProfileImage().execute();
				// logDebug("BackGroundTaskForUserProfile   onPostExecute ueragetextviw  "+ueragetextviw);
				// logDebug("BackGroundTaskForUserProfile   onPostExecute usernametextivew  "+usernametextivew);

				sessionManager.setProFileIsCallde(true);
				;

				ueragetextviw.setText("" + mUserProFileData.getAge());
				usernametextivew.setText("" + mUserProFileData.getFirstName());

				sessionManager.setUserProfileName(mUserProFileData
						.getFirstName());
				sessionManager.setUserAge("" + mUserProFileData.getAge());
				if (mUserProFileData.getPersDesc() != null
						&& mUserProFileData.getPersDesc().length() > 0) {
					userAboutLayout.setVisibility(View.VISIBLE);
					sessionManager.setUserAbout("");
					aboutuserTextview.setText(""
							+ mUserProFileData.getPersDesc());
				}
			} catch (Exception e) {
				logError("BackGroundTaskForUserProfile   onPostExecute Exception  "
						+ e);
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

	}

	private class BackGroundTaskForDownloadProfileImage extends
			AsyncTask<String, Void, Void> {

		private Ultilities mUltilities = new Ultilities();
		private SessionManager mSessionManager = new SessionManager(
				getActivity());
		private GellaryData mGellaryData;
		private ArrayList<ImageDetail> imagelistFormdatabase;
		private DatabaseHandler databaseHandler = new DatabaseHandler(
				getActivity());
		private boolean success = true;

		@Override
		protected Void doInBackground(String... params) {
			try {
				imagelistFormdatabase = databaseHandler.getImageDetail();
				if (imagelistFormdatabase != null
						&& imagelistFormdatabase.size() > 0) {
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mDialog.dismiss();
							ueragetextviw.setText(""
									+ mSessionManager.getUserAge());
							usernametextivew.setText(""
									+ mSessionManager.getUserProfileName());
							userboutTextview.setText("About "
									+ mSessionManager.getUserProfileName());
							if (mSessionManager.getUserAbout() != null
									&& mSessionManager.getUserAbout().length() > 0) {
								userAboutLayout.setVisibility(View.VISIBLE);
								aboutuserTextview.setVisibility(View.VISIBLE);
								aboutuserTextview.setText(mSessionManager
										.getUserAbout());
							}
							page_text = new TextView[imagelistFormdatabase
									.size()];
							count = imagelistFormdatabase.size();
							count_layout.removeAllViews();
							for (int i = 0; i < imagelistFormdatabase.size(); i++) {
								page_text[i] = new TextView(getActivity());
								page_text[i].setText(".");
								page_text[i].setTextSize(45);
								page_text[i].setTypeface(null, Typeface.BOLD);
								page_text[i]
										.setTextColor(android.graphics.Color.GRAY);
								count_layout.addView(page_text[i]);
							}

						}
					});
					if (imageList != null && imageList.size() > 0) {
						imageList.clear();
					}
					for (int i = 0; i < imagelistFormdatabase.size(); i++) {

						mGellaryData = new GellaryData();
						mGellaryData.setImageUrl(imagelistFormdatabase.get(i)
								.getImageUrl());
						imageList.add(mGellaryData);
					}

					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							mAdapterForGellary.notifyDataSetChanged();
						}
					});

					String mydate;
					for (int i = 0; i < imagelistFormdatabase.size(); i++) {

						// imageList.add(new GellaryData());
						try {
							mydate = java.text.DateFormat.getDateTimeInstance()
									.format(Calendar.getInstance().getTime());

							Bitmap mBitmap = mUltilities
									.showImage(imagelistFormdatabase.get(i)
											.getSdcardpath());
							mydate = java.text.DateFormat.getDateTimeInstance()
									.format(Calendar.getInstance().getTime());

							Bitmap scaledBtmap = Bitmap.createScaledBitmap(
									mBitmap, imageHeightandWIdth[1],
									imageHeightandWIdth[0], true);
							mydate = java.text.DateFormat.getDateTimeInstance()
									.format(Calendar.getInstance().getTime());

							mBitmap.recycle();
							mydate = java.text.DateFormat.getDateTimeInstance()
									.format(Calendar.getInstance().getTime());

							imageList.get(i).setmBitmap(scaledBtmap);
						} catch (Exception e) {
							logError("BackGroundTaskForDownloadProfileImage  doInBackground  Exception "
									+ e);
							success = false;

						}

						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								mAdapterForGellary.notifyDataSetChanged();

							}
						});
					}

				} else {
					logError("BackGroundTaskForDownloadProfileImage  doInBackground  no value found from database");
					success = false;
				}

				// mAdapterForGellary.notifyDataSetChanged();
			} catch (Exception e) {
				logError("BackGroundTaskForDownloadProfileImage  doInBackground Exception"
						+ e);
				success = false;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			try {
				if (success) {

				} else {
					mDialog.dismiss();
				}
			} catch (Exception e) {
				logError("BackGroundTaskForDownloadProfileImage  onPostExecute  Exception "
						+ e);
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mDialog = mUltilities.GetProcessDialog(getActivity());
			mDialog.setMessage("Please Wait..");
			mDialog.setCancelable(false);
			mDialog.show();
		}
	}

	private class ImageAdapterForGellary extends ArrayAdapter<GellaryData> {
		Activity mActivity = getActivity();
		private LayoutInflater mInflater;
		private Ultilities mUltilities = new Ultilities();
		private int[] imageheightandWidth = mUltilities
				.getImageHeightAndWidthForGellary(mActivity);

		public ImageAdapterForGellary(Context context, List<GellaryData> objects) {
			super(context, R.layout.galleritem, objects);
			mInflater = (LayoutInflater) mActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			Log.i(TAG, "Profile image list size:" + objects.size());
		}

		@Override
		public int getCount() {
			return super.getCount();
		}

		@Override
		public GellaryData getItem(int position) {
			return super.getItem(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
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
			GellaryData gellaryData = getItem(position);
			holder.mProgressBar.setId(position);
			holder.imageview.setId(position);
			if (getItem(position).getmBitmap() != null) {
				holder.mProgressBar.setVisibility(View.GONE);
				holder.imageview
						.setImageBitmap(getItem(position).getmBitmap() /*
																		 * Bitmap.
																		 * createScaledBitmap
																		 * (
																		 * getItem
																		 * (
																		 * position
																		 * ).
																		 * getmBitmap
																		 * (),
																		 * imageheightandWidth
																		 * [1],
																		 * imageheightandWidth
																		 * [0],
																		 * false
																		 * )
																		 */);
			} else {
				Log.i(TAG, "iamge bitmap is null");
			}
			return convertView;
		}

		class ViewHolder {
			ImageView imageview;
			ProgressBar mProgressBar;

		}
	}

	private class ImageAdapterForGellaryInterested extends
			ArrayAdapter<GellaryData> {
		// Activity mActivity=getActivity();
		RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
		private ImageLoader imageLoader = new ImageLoader(mRequestQueue,
				new BitmapLruCache(BitmapLruCache.getDefaultLruCacheSize()));
		private LayoutInflater mInflater;
		private AQuery aQuery;
		Typeface HelveticaLTStd_Light = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/HelveticaLTStd-Light.otf");

		public ImageAdapterForGellaryInterested(Context context,
				List<GellaryData> objects) {
			super(context, R.layout.myintrested, objects);
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			aQuery = new AQuery(context);
			// logDebug("ImageAdapterForGellaryInterested   context "+context);
			// logDebug("ImageAdapterForGellaryInterested   objects "+objects);
		}

		@Override
		public int getCount() {
			// logDebug("ImageAdapterForGellaryInterested  getCount count "+super.getCount());
			return super.getCount();
		}

		@Override
		public GellaryData getItem(int position) {
			return super.getItem(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			// logDebug("ImageAdapterForGellaryInterested  getView position "+position);
			// logDebug("ImageAdapterForGellaryInterested  getView convertView "+convertView);
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
			String userInrestName = getItem(position).getInterestedName();

			if (userInrestName != null && userInrestName.length() > 0) {
				if (userInrestName.indexOf(" ") != -1) {
					userInrestName = userInrestName.substring(0,
							userInrestName.indexOf(" "));
				} else {
					// do nothing
				}

			} else {
				userInrestName = "not found";
			}
			holder.textView.setText(userInrestName);
			holder.imageview.setImageResource(R.drawable.circled_book_icon);
			// holder.imageview.setImageUrl(getItem(position).getImageUrl(),
			// imageLoader,"CircularImge",getActivity());

			Log.i(TAG, "*****---->URL :: " + getItem(position).getImageUrl());

			aQuery.id(holder.imageview).image(getItem(position).getImageUrl());
			// holder.imageview.setImageUrl(getItem(position).getImageUrl(),
			// imageLoader);
			// logDebug("ImageAdapterForGellaryInterested  interes name is "+getItem(position).getInterestedName());
			// if (getItem(position).getmBitmap()!=null)
			// {
			// //logDebug("ImageAdapterForGellaryInterested  bitmap "+getItem(position).getmBitmap());
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

			TextView textView;

		}
	}

	private class ImageAdapterForGellaryfriends extends
			ArrayAdapter<GellaryData> {

		private static final String TAG = "ImageAdapterForGellaryfriends";

		// Activity mActivity=getActivity();
		private LayoutInflater mInflater;
		RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
		private ImageLoader imageLoader = new ImageLoader(mRequestQueue,
				new BitmapLruCache(BitmapLruCache.getDefaultLruCacheSize()));
		Typeface HelveticaLTStd_Light = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/HelveticaLTStd-Light.otf");
		private AQuery aQuery;

		public ImageAdapterForGellaryfriends(Context context,
				List<GellaryData> objects) {
			super(context, R.layout.myintrested, objects);
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			aQuery = new AQuery(context);

			// logDebug("ImageAdapterForGellaryInterested   context "+context);
			// logDebug("ImageAdapterForGellaryInterested   objects "+objects);
		}

		@Override
		public int getCount() {
			// logDebug("ImageAdapterForGellaryInterested  getCount count "+super.getCount());
			return super.getCount();
		}

		@Override
		public GellaryData getItem(int position) {
			return super.getItem(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			// logDebug("ImageAdapterForGellaryInterested  getView position "+position);
			// logDebug("ImageAdapterForGellaryInterested  getView convertView "+convertView);
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
			holder.imageview.setImageResource(R.drawable.multi_user_icon);
			String friendName = getItem(position).getInterestedName();

			if (friendName != null && friendName.length() > 0) {
				friendName = friendName.substring(0, friendName.indexOf(" "));
			} else {
				friendName = "not found";
			}
			holder.textView.setText(friendName);
			// holder.imageview.setImageUrl(getItem(position).getImageUrl(),
			// imageLoader,"CircularImge",getActivity());
			// holder.imageview.setImageUrl(getItem(position).getImageUrl(),
			// imageLoader);

			Log.i(TAG, "*** getting image:: " + getItem(position).getImageUrl());

			aQuery.id(holder.imageview).image(getItem(position).getImageUrl());

			return convertView;
		}

		class ViewHolder {
			ImageView imageview;
			TextView textView;
		}
	}

	private Session.StatusCallback statusCallback = new SessionStatusCallback();

	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			// updateView();

			if (session.isOpened()) {
				// /Session mSession=Session.getActiveSession();
				SessionManager mSessionManager = new SessionManager(
						getActivity());

				String[] params = { mSessionManager.getFacebookId() };
				try {
					getMyIntreste(params);
					// new BackGroundTaskForGetUserInterest().equals(params);
				} catch (Exception e) {
					getOpenedSession();
				}
			}

		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mDialog != null) {
			mDialog.dismiss();
		}
	}

}
