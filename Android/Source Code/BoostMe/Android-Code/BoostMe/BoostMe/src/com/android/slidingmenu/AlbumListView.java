package com.android.slidingmenu;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.appdupe.flamer.R;
import com.appdupe.flamer.pojo.AlubumListData;
import com.appdupe.flamer.pojo.FQLFirstSet;
import com.appdupe.flamer.pojo.FQLSecondResult;
import com.appdupe.flamer.pojo.FaceBookAlubumData;
import com.appdupe.flamer.pojo.ListviewAlubumData;
import com.appdupe.flamer.utility.Constant;
import com.appdupe.flamer.utility.SessionManager;
import com.appdupe.flamer.utility.Ultilities;
import com.appdupe.flamer.utility.Utility;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;

public class AlbumListView extends Activity implements OnItemClickListener {
	private Session.StatusCallback statusCallback = new SessionStatusCallback();
	private static boolean mDebugLog = true;
	private static String mDebugTag = "AlbumListView";
	private TextView imagegallerytextview;

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

	private ArrayList<ListviewAlubumData> alubumList;
	private AlubumListViewAdapter mAlubumListViewAdapter;
	private ListView alubumlistview;
	private Dialog mdialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alubumlistview);
		alubumlistview = (ListView) findViewById(R.id.alubumlistview);
		imagegallerytextview = (TextView) findViewById(R.id.imagegallerytextview);
		Ultilities mUltilities = new Ultilities();
		int imageHeightAndWidht[] = mUltilities
				.getImageHeightAndWidthForAlubumListview(this);
		alubumList = new ArrayList<ListviewAlubumData>();
		mAlubumListViewAdapter = new AlubumListViewAdapter(this, alubumList,
				imageHeightAndWidht);
		alubumlistview.setAdapter(mAlubumListViewAdapter);
		imagegallerytextview.setText("Albums");
		Typeface HelveticaLTStd_Light = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Light.otf");
		imagegallerytextview.setTypeface(HelveticaLTStd_Light);
		imagegallerytextview.setTextColor(Color.rgb(255, 255, 255));
		imagegallerytextview.setTextSize(20);
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
		Session session = Session.getActiveSession();

		logDebug("onCreate session " + session);
		if (session == null) { // logDebug("onCreate savedInstanceState "+savedInstanceState);
			if (savedInstanceState != null) {
				session = Session.restoreSession(this, null, statusCallback,
						savedInstanceState);
				// logDebug("onCreate savedInstanceState restore session   "+session);
			}
			if (session == null) {
				session = new Session(this);
				// logDebug("onCreate savedInstanceState create session   "+session);
			}
			Session.setActiveSession(session);
			// logDebug("onCreate savedInstanceState state session    "+session.getState());
			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
				// session.openForRead(new
				// Session.OpenRequest(this).setPermissions(Arrays.asList("user_birthday",
				// "email","user_relationships","user_photos")).setCallback(statusCallback));
			}
		}

		try {
			if (session.isOpened()) {
				getUserAllAlubum();
			} else {
				getOpenedSession();
			}
			alubumlistview.setOnItemClickListener(this);
			;

		} catch (Exception e) {
			logError("error onCreate Exception " + e);

		}
	}

	private void getUserAllAlubum() {
		SessionManager mSessionManager = new SessionManager(this);

		String[] params = { mSessionManager.getFacebookId() };

		logDebug("getUserAllAlubum params " + params);

		// String fqlQuery=
		// "{\"query2\":\"select aid,photo_count,cover_pid, name from album where owner ="+params[0]+"ORDER BY cover_pid\", \"query1\":\"select pid, src from photo where pid in (SELECT cover_pid from album where owner ="+params[0]+") ORDER BY pid\"}";

		String fqlQuery = "{\"query2\":\"select aid,photo_count,cover_pid, name from album where owner = "
				+ params[0]
				+ " and type != 'wall' ORDER BY cover_pid\", \"query1\":\"select pid, src from photo where pid in (SELECT cover_pid from album where owner ="
				+ params[0] + " ORDER BY cover_pid) ORDER BY pid\"}";

		// String fqlQuery =
		// "select src_big from photo  where album_object_id IN (SELECT  object_id   FROM album WHERE owner="+"'"+params[0]+"'"+" and name='Profile Pictures') LIMIT 5";
		// String fqlQuery =
		// "select uid1, uid2 FROM friend WHERE uid1 = '100003056725155'";
		// String fqlQuery =
		// "select src from photo  where album_object_id IN (SELECT  object_id   FROM album WHERE owner='params[0]' and name='Profile Pictures'";

		logDebug("getUserAllAlubum fqlQuery " + fqlQuery);
		// Log.i(TAG, "Result: " + fqlQuery);
		Bundle param = new Bundle();
		param.putString("q", fqlQuery);
		Session session = Session.getActiveSession();
		Request request = new Request(session, "/fql", param, HttpMethod.GET,
				new Request.Callback() {
					public void onCompleted(Response response) {
						try {
							logDebug("getUserAllAlubum    Result  "
									+ response.toString());

							String[] pramas = { response.toString() };
							new BackGroundTaskForGetAlubumData()
									.execute(pramas);
						} catch (Exception e) {
							logError("Request onCompleted Exception " + e);
						}
						// Log.i(TAG, "Result: " + response.toString());

					}
				});

		Request.executeBatchAsync(request);

	}

	private class BackGroundTaskForDownLoadImage extends
			AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			logDebug("BackGroundTaskForDownLoadImage doInBackground");
			try {
				for (int i = 0; i < alubumList.size(); i++) {

					// logDebug("BackGroundTaskForDownLoadImage doInBackground  url  is "+alubumList.get(i).getPickUrl());
					Bitmap mBitmap = Utility.getBitmap(alubumList.get(i)
							.getPickUrl().replaceAll(" ", "%20"));
					// logDebug("BackGroundTaskForDownLoadImage doInBackground  mBitmap "+mBitmap);
					alubumList.get(i).setmBitmap(mBitmap);
					runOnUiThread(new Runnable() {
						public void run() {
							mAlubumListViewAdapter.notifyDataSetChanged();
						}
					});
				}
			} catch (Exception e) {
				// TODO: handle exception
				logError("BackGroundTaskForDownLoadImage doInBackground Exception "
						+ e);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			mdialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

	}

	private class BackGroundTaskForGetAlubumData extends
			AsyncTask<String, Void, Void> {
		private static final String TAG = "BackGroundTaskForGetAlubumData";
		Ultilities mUltilities = new Ultilities();
		private String alubumResponse;
		private AlubumListData mAlubumListData;
		private FaceBookAlubumData mFaceBookAlubumData;

		private FaceBookAlubumData mFaceBookAlubumData1;

		private ArrayList<FQLFirstSet> alubumpickurlList;
		private ArrayList<FQLSecondResult> alubumNameList;
		private ArrayList<FaceBookAlubumData> facebookArrayList;
		private ListviewAlubumData mListviewAlubumData;

		@Override
		protected Void doInBackground(String... params) {
			try {
				alubumResponse = params[0];
				Log.e(TAG, "1 alubumResponse : " + alubumResponse);
				alubumResponse = alubumResponse.substring(
						alubumResponse.indexOf("state=") + 6,
						alubumResponse.indexOf("}, error:"));
				// logDebug("BackGroundTaskForGetAlubumData  doInBackground alubumResponse "+alubumResponse);

				Log.e(TAG, "2 alubumResponse : " + alubumResponse);

				alubumResponse = alubumResponse.replaceFirst("fql_result_set",
						"fql_result_set1");

				Log.e(TAG, "3 alubumResponse : " + alubumResponse);

				Gson gson = new Gson();
				mAlubumListData = gson.fromJson(alubumResponse,
						AlubumListData.class);
				// logDebug("BackGroundTaskForGetAlubumData  doInBackground mAlubumListData "+mAlubumListData);
				facebookArrayList = mAlubumListData.getFacebookArrayList();
				mFaceBookAlubumData = facebookArrayList.get(0);
				mFaceBookAlubumData1 = facebookArrayList.get(1);

				// logDebug("BackGroundTaskForGetAlubumData doInBackground  mFaceBookAlubumData "+mFaceBookAlubumData);
				// logDebug("BackGroundTaskForGetAlubumData doInBackground  mFaceBookAlubumData1 "+mFaceBookAlubumData1);

				alubumpickurlList = mFaceBookAlubumData.getPickList();
				alubumNameList = mFaceBookAlubumData1.getAlubumnamList();

				for (int i = 0; i < alubumpickurlList.size(); i++) {
					String alubumName = alubumNameList.get(i).getName();
					String alubumId = alubumNameList.get(i).getAlubumId();
					int photocount = alubumNameList.get(i).getPhotoCount();
					String pickUrl = alubumpickurlList.get(i).getPickUrl();
					// logDebug("BackGroundTaskForGetAlubumData doInBackground alubumName  "+alubumName);
					// logDebug("BackGroundTaskForGetAlubumData doInBackground alubumId "+alubumId);
					// logDebug("BackGroundTaskForGetAlubumData doInBackground pickUrl "+pickUrl);
					mListviewAlubumData = new ListviewAlubumData();
					mListviewAlubumData.setAlubumid(alubumId);
					mListviewAlubumData.setAlubumName(alubumName);
					mListviewAlubumData.setPickUrl(pickUrl);
					mListviewAlubumData.setPhotoCount(photocount);
					alubumList.add(mListviewAlubumData);
				}

			} catch (Exception e) {
				logError("BackGroundTaskForGetAlubumData  Exception " + e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (mdialog != null) {
				mdialog.dismiss();

			}
			mAlubumListViewAdapter.notifyDataSetChanged();
			// new BackGroundTaskForDownLoadImage().execute();

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mdialog = mUltilities.GetProcessDialog(AlbumListView.this);
			mdialog.setCancelable(false);
			mdialog.show();
		}
	}

	private void getOpenedSession() {
		Session.openActiveSession(this, true, statusCallback);
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
		// if (mdialog!=null)
		// {
		// mdialog.dismiss();
		// mdialog=null;
		// }

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

	private class AlubumListViewAdapter extends
			ArrayAdapter<ListviewAlubumData> {

		RequestQueue mRequestQueue = Volley.newRequestQueue(AlbumListView.this);
		private ImageLoader imageLoader = new ImageLoader(mRequestQueue,
				new BitmapLruCache(BitmapLruCache.getDefaultLruCacheSize()));
		private RelativeLayout.LayoutParams params;
		private int imageHeigthAndWidth[];
		private Activity mActivity;
		Typeface HelveticaLTStd_Light = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Light.otf");
		private LayoutInflater mInflater;

		public AlubumListViewAdapter(Activity context,
				List<ListviewAlubumData> objects, int imageHeigthAndWidth[]) {
			super(context, R.layout.alubumlistviewitem, objects);
			// TODO Auto-generated constructor stub
			mActivity = context;
			mInflater = (LayoutInflater) mActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.imageHeigthAndWidth = imageHeigthAndWidth;
			Ultilities ultilities = new Ultilities();
			// params=ultilities.getRelativelayoutParams(imageHeigthAndWidth[1],
			// imageHeigthAndWidth[0]);
			// params.addRule(RelativeLayout.CENTER_HORIZONTAL);
			// params.addRule(RelativeLayout.CENTER_VERTICAL);
			/*
			 * params.addRule(RelativeLayout.); params.setMargins(5, 0, 0, 0);
			 */
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return super.getCount();
		}

		@Override
		public ListviewAlubumData getItem(int position) {
			// TODO Auto-generated method stub
			return super.getItem(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			// TODO Auto-generated method stub-
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.alubumlistviewitem,
						null);
				holder.imageview = (NetworkImageView) convertView
						.findViewById(R.id.albumlistviewitemImageview);
				holder.imageview.setImageResource(R.drawable.multi_user_icon);
				holder.textview = (TextView) convertView
						.findViewById(R.id.albumlistviewitemTextView);
				holder.photocount = (TextView) convertView
						.findViewById(R.id.photocount);
				holder.textview.setTypeface(HelveticaLTStd_Light);
				holder.photocount.setTypeface(HelveticaLTStd_Light);
				holder.textview.setTextColor(Color.rgb(105, 105, 105));
				holder.photocount.setTextColor(Color.rgb(153, 153, 153));
				// holder.alubumitemimageViewParent =
				// (RelativeLayout)convertView.findViewById(R.id.alubumitemimageViewParent);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
				holder.imageview.setImageResource(R.drawable.multi_user_icon);

			}

			holder.textview.setId(position);
			holder.imageview.setId(position);
			holder.photocount.setId(position);
			// holder.alubumitemimageViewParent.setId(position);

			// holder.imageview.setLayoutParams(params);
			holder.photocount.setText("" + getItem(position).getPhotoCount()
					+ " Photos");
			holder.textview.setText("" + getItem(position).getAlubumName());

			holder.imageview.setImageResource(R.drawable.multi_user_icon);
			// holder.imageview.setImageUrl(getItem(position).getPickUrl(),
			// imageLoader,"AlubumLisView",AlbumListView.this);
			holder.imageview.setImageUrl(getItem(position).getPickUrl(),
					imageLoader);
			// if (getItem(position).getmBitmap()!=null)
			// {
			//
			// // holder.mProgressBar.setVisibility(View.GONE);
			// holder.imageview.setImageBitmap(
			// Bitmap.createScaledBitmap(getItem(position).getmBitmap(),
			// imageHeigthAndWidth[1], imageHeigthAndWidth[0], false));
			// }
			// else
			// {
			//
			// }

			return convertView;
		}

		class ViewHolder {
			NetworkImageView imageview;
			TextView textview;
			TextView photocount;
			RelativeLayout alubumitemimageViewParent;

		}

	}

	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			// updateView();
			logDebug("SessionStatusCallback  ");
			logDebug("SessionStatusCallback Session  " + session);
			logDebug("SessionStatusCallback  SessionState " + state);
			logDebug("SessionStatusCallback  Exception " + exception);
			getUserAllAlubum();

		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		logDebug("onItemClick  ");
		ListviewAlubumData mAlubumData = (ListviewAlubumData) arg0
				.getItemAtPosition(arg2);
		String alubumId = mAlubumData.getAlubumid();
		String alubumName = mAlubumData.getAlubumName();
		logDebug("onItemClick alubumId " + alubumId);
		logDebug("onItemClick alubumName " + alubumName);
		Bundle mBundle = new Bundle();
		mBundle.putString(Constant.ALUBUMNAME, alubumName);
		mBundle.putString(Constant.ALUBUMID, alubumId);
		if (mAlubumData.getPhotoCount() > 0) {
			Intent mIntent = new Intent(AlbumListView.this,
					AlubumGridviewAcitivity.class);
			mIntent.putExtras(mBundle);
			startActivity(mIntent);
		} else {

		}

	}

}
