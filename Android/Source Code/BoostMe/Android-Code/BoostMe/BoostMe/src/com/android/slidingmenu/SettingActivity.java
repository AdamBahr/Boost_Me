package com.android.slidingmenu;

import java.util.List;

import org.apache.http.NameValuePair;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.appdupe.flamer.LoginUsingFacebook;
import com.appdupe.flamer.R;
import com.appdupe.flamer.pojo.DeleteUserAccountData;
import com.appdupe.flamer.pojo.LogOutData;
import com.appdupe.flamer.pojo.UpdatePrefrence;
import com.appdupe.flamer.utility.Constant;
import com.appdupe.flamer.utility.SessionManager;
import com.appdupe.flamer.utility.Ultilities;
import com.edmodo.rangebar.RangeBar;
import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;

public class SettingActivity extends Fragment implements OnClickListener {
	private static boolean mDebugLog = true;
	private static final String mDebugTag = "SettingActivity";

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

	private RangeBar rangebar;;
	private SeekBar seeklitmitosearch;
	private int minAge;
	private int maxAge;
	private TextView maleTextview;
	private TextView femaleTextview;
	private TextView imTextview;
	private TextView ganderNameTextview;
	private TextView showmemaletextview;
	private TextView showmefemaltextview;

	private TextView limitotsearchvalue;
	private TextView showagetextview;
	private TextView maxage, agedeshtextview;
	private TextView showdistancetextview;
	private TextView distancevalue;
	private TextView MiTextview;
	private TextView kmTextview;
	private TextView minagevalue;

	private CheckBox maleChekbox, femaleChekbox;
	private TextView showmetextivew;
	private TextView maleFemaleTextview, limitsearchtextview;
	private Button contactButton;

	private Button logoutButton, deleteaccount;
	private Button update;
	private int usersexprefrence;
	private int usersex;
	private int radious;
	private boolean isDistanceUinitKm;
	private boolean ismaleselected;
	private boolean isfemalselected;
	private ProgressDialog mDialog;
	private String distanceunit;
	private int distanceunilt = 1;
	private RelativeLayout maletextviewparentlayout,
			femaletextviewparentlayout, MiTextviewparentlayout,
			kmTextviewparentLayout;
	private Session.StatusCallback statusCallback = new SessionStatusCallback();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.settingactivity, null);
		initDataList(view);
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
		Session session = Session.getActiveSession();

		if (session == null) {
			if (savedInstanceState != null) {
				session = Session.restoreSession(getActivity(), null,
						statusCallback, savedInstanceState);

			}
			if (session == null) {
				session = new Session(getActivity());

			}
			Session.setActiveSession(session);

			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {

			}
		}

		rangebar.setTickCount(56);
		rangebar.setTickHeight(0);

		rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
			@Override
			public void onIndexChangeListener(RangeBar rangeBar,
					int leftThumbIndex, int rightThumbIndex) {
				if (leftThumbIndex < 18) {
					minAge = leftThumbIndex + 18;
				} else {
					minAge = leftThumbIndex;
				}

				maxAge = rightThumbIndex;
				minagevalue.setText("" + minAge);
				maxage.setText("" + maxAge);

				// leftIndexValue.setText("" + leftThumbIndex);
				// rightIndexValue.setText("" + rightThumbIndex);
			}
		});

		try {
			update.setOnClickListener(this);
			logoutButton.setOnClickListener(this);
			MiTextviewparentlayout.setOnClickListener(this);
			kmTextviewparentLayout.setOnClickListener(this);
			// femaleChekbox.setOnClickListener(this);
			// / maleChekbox.setOnClickListener(this);
			maletextviewparentlayout.setOnClickListener(this);
			femaletextviewparentlayout.setOnClickListener(this);
			deleteaccount.setOnClickListener(this);
			contactButton.setOnClickListener(this);

			SessionManager mSessionManager = new SessionManager(getActivity());
			String userSex = mSessionManager.getUserSex();
			logDebug("onCreate userSex  " + userSex);

			minAge = Integer.parseInt(mSessionManager.getUserLowerAge());
			maxAge = Integer.parseInt(mSessionManager.getUserHeigherAge());
			logDebug("onCreate minAge  " + minAge);
			logDebug("onCreate maxAge  " + maxAge);
			rangebar.setThumbIndices(minAge, maxAge);
			minagevalue.setText("" + minAge);
			maxage.setText("" + maxAge);

			// userSex.endsWith("male")

			if (userSex.equals("1")) {
				logDebug("onCreate selected   ");
				maletextviewparentlayout
						.setBackgroundResource(R.drawable.selector_on);
				femaletextviewparentlayout
						.setBackgroundResource(R.drawable.selector_off);
				;
				ganderNameTextview.setText(getResources().getString(
						R.string.maletextview));
				maleTextview.setTextColor(Color.rgb(255, 255, 255));
				femaleTextview.setTextColor(Color.rgb(153, 153, 153));
				usersex = 1;

				ismaleselected = true;
				isfemalselected = false;
			} else {
				logDebug("onCreate female selected   ");
				maletextviewparentlayout
						.setBackgroundResource(R.drawable.selector_off);
				;
				femaletextviewparentlayout
						.setBackgroundResource(R.drawable.selector_on);

				ganderNameTextview.setText(getResources().getString(
						R.string.femaletextview));
				femaleTextview.setTextColor(Color.rgb(255, 255, 255));
				maleTextview.setTextColor(Color.rgb(153, 153, 153));
				usersex = 2;

				ismaleselected = false;
				isfemalselected = true;
			}

			seeklitmitosearch
					.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
						int progressChanged = 0;

						public void onProgressChanged(SeekBar seekBar,
								int progress, boolean fromUser) {
							radious = progress;
							logDebug("onProgressChanged  radious");
							limitotsearchvalue.setText("" + radious);
						}

						public void onStartTrackingTouch(SeekBar seekBar) {
						}

						public void onStopTrackingTouch(SeekBar seekBar) {

						}
					});
			if (mSessionManager.getDistaceUnit().equals("Km")) {
				kmTextviewparentLayout
						.setBackgroundResource(R.drawable.selector_on);
				MiTextviewparentlayout
						.setBackgroundResource(R.drawable.selector_off);

				isDistanceUinitKm = true;
				radious = mSessionManager.getDistance();
				distanceunit = "Km";
				distancevalue.setText("Km");
				distanceunilt = 1;

				kmTextview.setTextColor(Color.rgb(255, 255, 255));
				MiTextview.setTextColor(Color.rgb(153, 153, 153));
			} else {
				MiTextviewparentlayout
						.setBackgroundResource(R.drawable.selector_on);
				kmTextviewparentLayout
						.setBackgroundResource(R.drawable.selector_off);
				isDistanceUinitKm = false;
				radious = mSessionManager.getDistance();
				distanceunit = "Mi";
				distancevalue.setText("Mi");
				distanceunilt = 2;
				MiTextview.setTextColor(Color.rgb(255, 255, 255));
				kmTextview.setTextColor(Color.rgb(153, 153, 153));
			}

			logDebug("oncreate radious " + radious);
			seeklitmitosearch.setProgress(radious);

			limitotsearchvalue.setText("" + radious);

			usersexprefrence = Integer.parseInt(mSessionManager
					.getUserPrefSex());
			Log.i("TAG", "user sex pref" + usersexprefrence);
			if (usersexprefrence == 3) {
				maleChekbox.setChecked(true);
				femaleChekbox.setChecked(true);
				maleFemaleTextview.setText("Male/Female");
			} else if (usersexprefrence == 2) {
				maleChekbox.setChecked(false);
				femaleChekbox.setChecked(true);
				maleFemaleTextview.setText("Female");
			} else if (usersexprefrence == 1) {
				maleChekbox.setChecked(true);
				femaleChekbox.setChecked(false);
				maleFemaleTextview.setText("Male");
			}

			maleChekbox
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							logDebug("setOnCheckedChangeListener onCheckedChanged maleChekbox  maleChekbox isChecked "
									+ isChecked);
							logDebug("setOnCheckedChangeListener onCheckedChanged maleChekbox  femelcheckbox isChecked "
									+ femaleChekbox.isChecked());
							if (isChecked && femaleChekbox.isChecked()) {
								logDebug("setOnCheckedChangeListener onCheckedChanged  maleChekbox both selected ");
								maleFemaleTextview.setText("Male/Female");
								usersexprefrence = 3;
							} else if (isChecked) {
								logDebug("setOnCheckedChangeListener onCheckedChanged  maleChekbox male selected ");
								maleFemaleTextview.setText("Male");
								usersexprefrence = 1;
							}

							else if (femaleChekbox.isChecked()) {
								logDebug("setOnCheckedChangeListener onCheckedChanged maleChekbox  female selected ");
								maleFemaleTextview.setText("Female");
								usersexprefrence = 2;
							} else {
								femaleChekbox.setChecked(true);
								logDebug("setOnCheckedChangeListener onCheckedChanged maleChekbox  both not selecte selected ");
							}
						}
					});

			femaleChekbox
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							logDebug("setOnCheckedChangeListener onCheckedChanged femaleChekbox  femaleChekbox isChecked "
									+ isChecked);
							logDebug("setOnCheckedChangeListener onCheckedChanged  femaleChekbox  maleChekbox isChecked "
									+ maleChekbox.isChecked());

							if (isChecked && maleChekbox.isChecked()) {
								logDebug("setOnCheckedChangeListener onCheckedChanged  femaleChekbox   both selected ");
								maleFemaleTextview.setText("Male/Female");
								usersexprefrence = 3;
							} else if (isChecked) {
								logDebug("setOnCheckedChangeListener onCheckedChanged  femaleChekbox  female selected ");
								maleFemaleTextview.setText("Female");
								usersexprefrence = 2;
							}

							else if (maleChekbox.isChecked()) {
								logDebug("setOnCheckedChangeListener onCheckedChanged femaleChekbox  male selected ");
								maleFemaleTextview.setText("Male");
								usersexprefrence = 1;
							} else {
								maleChekbox.setChecked(true);

								logDebug("setOnCheckedChangeListener onCheckedChanged femaleChekbox  both not selecte selected ");
							}
						}
					});

		}

		catch (Exception e) {
			logError("onCreate   Exception  " + e);
		}
		return view;
	}

	private void initDataList(View view) {
		imTextview = (TextView) view.findViewById(R.id.imTextview);

		maleTextview = (TextView) view.findViewById(R.id.maleTextview);
		rangebar = (RangeBar) view.findViewById(R.id.rangebar1);
		contactButton = (Button) view.findViewById(R.id.contactButton);
		logoutButton = (Button) view.findViewById(R.id.logoutButton);
		update = (Button) view.findViewById(R.id.update);
		maxage = (TextView) view.findViewById(R.id.maxage);
		minagevalue = (TextView) view.findViewById(R.id.minagevalue);
		agedeshtextview = (TextView) view.findViewById(R.id.agedeshtextview);

		limitotsearchvalue = (TextView) view
				.findViewById(R.id.limitotsearchvalue);

		ganderNameTextview = (TextView) view
				.findViewById(R.id.ganderNameTextview);
		femaleTextview = (TextView) view.findViewById(R.id.femaleTextview);
		ganderNameTextview = (TextView) view
				.findViewById(R.id.ganderNameTextview);

		showmetextivew = (TextView) view.findViewById(R.id.showmetextivew);
		maleFemaleTextview = (TextView) view
				.findViewById(R.id.maleFemaleTextview);
		showmemaletextview = (TextView) view
				.findViewById(R.id.showmemaletextview);
		showmefemaltextview = (TextView) view
				.findViewById(R.id.showmefemaltextview);
		maleChekbox = (CheckBox) view.findViewById(R.id.maleChekbox);
		femaleChekbox = (CheckBox) view.findViewById(R.id.femaleChekbox);
		limitsearchtextview = (TextView) view
				.findViewById(R.id.limitsearchtextview);
		seeklitmitosearch = (SeekBar) view.findViewById(R.id.seeklitmitosearch);
		showdistancetextview = (TextView) view
				.findViewById(R.id.showdistancetextview);
		distancevalue = (TextView) view.findViewById(R.id.distancevalue);
		MiTextview = (TextView) view.findViewById(R.id.MiTextview);
		kmTextview = (TextView) view.findViewById(R.id.kmTextview);

		maletextviewparentlayout = (RelativeLayout) view
				.findViewById(R.id.maletextviewparentlayout);
		femaletextviewparentlayout = (RelativeLayout) view
				.findViewById(R.id.femaletextviewparentlayout);
		MiTextviewparentlayout = (RelativeLayout) view
				.findViewById(R.id.MiTextviewparentlayout);
		kmTextviewparentLayout = (RelativeLayout) view
				.findViewById(R.id.kmTextviewparentLayout);

		showagetextview = (TextView) view.findViewById(R.id.showagetextview);

		Typeface HelveticaLTStd_Light = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/HelveticaLTStd-Light.otf");
		Typeface HelveticaInseratLTStd_Roman = Typeface.createFromAsset(
				getActivity().getAssets(),
				"fonts/HelveticaInseratLTStd-Roman.otf");
		imTextview.setTypeface(HelveticaLTStd_Light);
		imTextview.setTextColor(Color.rgb(0, 0, 0));
		ganderNameTextview.setTypeface(HelveticaInseratLTStd_Roman);
		ganderNameTextview.setTextColor(Color.rgb(0, 0, 0));

		maleTextview.setTypeface(HelveticaLTStd_Light);

		showmetextivew.setTypeface(HelveticaLTStd_Light);
		showmetextivew.setTextColor(Color.rgb(0, 0, 0));

		maleFemaleTextview.setTypeface(HelveticaInseratLTStd_Roman);
		maleFemaleTextview.setTextColor(Color.rgb(0, 0, 0));

		showmemaletextview.setTypeface(HelveticaInseratLTStd_Roman);
		showmemaletextview.setTextColor(Color.rgb(153, 153, 153));

		showmefemaltextview.setTypeface(HelveticaInseratLTStd_Roman);
		showmefemaltextview.setTextColor(Color.rgb(153, 153, 153));

		limitsearchtextview.setTypeface(HelveticaLTStd_Light);
		limitsearchtextview.setTextColor(Color.rgb(0, 0, 0));

		limitotsearchvalue.setTypeface(HelveticaInseratLTStd_Roman);
		limitotsearchvalue.setTextColor(Color.rgb(0, 0, 0));

		showagetextview.setTypeface(HelveticaLTStd_Light);
		showagetextview.setTextColor(Color.rgb(0, 0, 0));

		minagevalue.setTypeface(HelveticaInseratLTStd_Roman);
		minagevalue.setTextColor(Color.rgb(0, 0, 0));

		maxage.setTypeface(HelveticaInseratLTStd_Roman);
		maxage.setTextColor(Color.rgb(0, 0, 0));

		agedeshtextview.setTypeface(HelveticaInseratLTStd_Roman);
		agedeshtextview.setTextColor(Color.rgb(0, 0, 0));

		showdistancetextview.setTypeface(HelveticaLTStd_Light);
		showdistancetextview.setTextColor(Color.rgb(0, 0, 0));

		distancevalue.setTypeface(HelveticaInseratLTStd_Roman);
		distancevalue.setTextColor(Color.rgb(0, 0, 0));

		MiTextview.setTypeface(HelveticaLTStd_Light);
		kmTextview.setTypeface(HelveticaLTStd_Light);
		;

		deleteaccount = (Button) view.findViewById(R.id.deleteaccount);
		deleteaccount.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.MiTextviewparentlayout) {
			if (isDistanceUinitKm) {
				isDistanceUinitKm = false;
				kmTextviewparentLayout
						.setBackgroundResource(R.drawable.selector_off);
				MiTextviewparentlayout
						.setBackgroundResource(R.drawable.selector_on);
				distanceunit = "Mi";
				distancevalue.setText(distanceunit);
				MiTextview.setTextColor(Color.rgb(255, 255, 255));
				kmTextview.setTextColor(Color.rgb(153, 153, 153));
			} else {

			}
		}
		if (v.getId() == R.id.kmTextviewparentLayout) {
			if (isDistanceUinitKm) {

			} else {
				kmTextviewparentLayout
						.setBackgroundResource(R.drawable.selector_on);
				MiTextviewparentlayout
						.setBackgroundResource(R.drawable.selector_off);
				distanceunit = "Km";
				distancevalue.setText(distanceunit);
				kmTextview.setTextColor(Color.rgb(255, 255, 255));
				MiTextview.setTextColor(Color.rgb(153, 153, 153));
				isDistanceUinitKm = true;
			}
		}
		if (v.getId() == R.id.maletextviewparentlayout) {
			if (ismaleselected) {

			} else {
				ganderNameTextview.setText(getResources().getString(
						R.string.maletextview));
				usersex = 1;
				maletextviewparentlayout
						.setBackgroundResource(R.drawable.selector_on);
				femaletextviewparentlayout
						.setBackgroundResource(R.drawable.selector_off);
				;

				maleTextview.setTextColor(Color.rgb(255, 255, 255));
				femaleTextview.setTextColor(Color.rgb(153, 153, 153));
				ismaleselected = true;
			}
		}
		if (v.getId() == R.id.femaletextviewparentlayout) {
			if (ismaleselected) {
				ganderNameTextview.setText(getResources().getString(
						R.string.femaletextview));
				usersex = 2;
				maletextviewparentlayout
						.setBackgroundResource(R.drawable.selector_off);
				;
				femaletextviewparentlayout
						.setBackgroundResource(R.drawable.selector_on);
				femaleTextview.setTextColor(Color.rgb(255, 255, 255));
				maleTextview.setTextColor(Color.rgb(153, 153, 153));
				ismaleselected = false;

			} else {

			}
		}
		if (v.getId() == R.id.update) {
			updateUserPrefrence();
		}
		if (v.getId() == R.id.logoutButton) {
			logoutCurrentUser();
		}
		if (v.getId() == R.id.contactButton) {
			Intent email = new Intent(Intent.ACTION_SEND/* Intent.ACTION_VIEW */);
			email.setType("message/rfc822");
			// email.putExtra(Intent.EXTRA_EMAIL,emailStrArray);
			email.putExtra(Intent.EXTRA_SUBJECT, "Flamer app");

			email.putExtra(Intent.EXTRA_TEXT, "Flamer app!");
			startActivity(Intent.createChooser(email,
					"Choose an Email client :"));
		}
		if (v.getId() == R.id.deleteaccount) {
			delettCurrentUser();
		}
	}

	private void logoutCurrentUser() {
		SessionManager mSessionManager = new SessionManager(getActivity());
		String sessionToke = mSessionManager.getUserToken();
		String deviceid = Ultilities.getDeviceId(getActivity());
		String[] params = { sessionToke, deviceid };
		new BackGroundTaskForLogout().execute(params);

	}

	private void delettCurrentUser() {
		SessionManager mSessionManager = new SessionManager(getActivity());
		String sessionToke = mSessionManager.getUserToken();
		String deviceid = Ultilities.getDeviceId(getActivity());
		String[] params = { sessionToke, deviceid };
		new BackGroundTaskForDeleteAccount().execute(params);
	}

	private class BackGroundTaskForDeleteAccount extends
			AsyncTask<String, Void, Void> {
		private String response;
		private boolean responseSuccess;
		private List<NameValuePair> deletuserAccountParameterList;
		private DeleteUserAccountData deleteUserAccountData;
		private Ultilities mUltilities = new Ultilities();

		@Override
		protected Void doInBackground(String... params) {
			deletuserAccountParameterList = mUltilities
					.getDeleteUserAccountParameter(params);
			logDebug("BackGroundTaskForDeleteAccount  deletuserAccountParameterList "
					+ deletuserAccountParameterList);
			response = mUltilities.makeHttpRequest(
					Constant.deleteUserAccount_url, Constant.methodeName,
					deletuserAccountParameterList);
			logDebug("BackGroundTaskForDeleteAccount  response " + response);
			Gson gson = new Gson();
			deleteUserAccountData = gson.fromJson(response,
					DeleteUserAccountData.class);
			if (deleteUserAccountData.getErrFlag() == 0
					&& deleteUserAccountData.getErrNum() == 61) {
				Session session = Session.getActiveSession();
				logDebug("BackGroundTaskForLogout  session " + session);
				if (!session.isClosed()) {
					logDebug("BackGroundTaskForLogout  session not close need to cleaar  "
							+ session.getState());
					session.closeAndClearTokenInformation();
				} else {
					// nothing session already close no need to clear
				}

				// DatabaseHandler databaseHandler=new
				// DatabaseHandler(getActivity());
				// databaseHandler.deleteUserData();
			} else if (deleteUserAccountData.getErrFlag() == 1
					&& deleteUserAccountData.getErrNum() == 31) {
				Session session = Session.getActiveSession();
				logDebug("BackGroundTaskForLogout  session " + session);
				if (!session.isClosed()) {
					logDebug("BackGroundTaskForLogout  session not close need to cleaar  "
							+ session.getState());
					session.closeAndClearTokenInformation();

				} else {
					// nothing session already close no need to clear
				}
			}

			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				mDialog = mUltilities.GetProcessDialog(getActivity());
				mDialog.setMessage("Please wait..");
				mDialog.setCancelable(true);
				mDialog.show();
			} catch (Exception e) {
				logError("BackGroundTaskForLogout onPreExecute Exception " + e);
			}
		}

		@Override
		protected void onPostExecute(java.lang.Void result) {
			super.onPostExecute(result);
			try {
				mDialog.dismiss();
				if (deleteUserAccountData.getErrFlag() == 0
						&& deleteUserAccountData.getErrNum() == 61) {
					// Intent intent = new Intent(Intent.ACTION_MAIN);
					// intent.addCategory(Intent.CATEGORY_HOME);
					// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					// startActivity(intent);
					// getActivity().finish();
					SessionManager mSessionManager = new SessionManager(
							getActivity());
					mSessionManager.logoutUser();
					Intent intent = new Intent(getActivity(),
							LoginUsingFacebook.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					getActivity().finish();
				} else if (deleteUserAccountData.getErrFlag() == 1
						&& deleteUserAccountData.getErrNum() == 62) {
					ErrorMessage("Alert", deleteUserAccountData.getErrMsg());
				} else if (deleteUserAccountData.getErrFlag() == 1
						&& deleteUserAccountData.getErrNum() == 31) {
					ErrorMessageInvalidSessionTOken("Alert",
							deleteUserAccountData.getErrMsg());
				} else {
					// some thing wrong is happend
				}

			} catch (Exception e) {
				logError("BackGroundTaskForLogout onPostExecute Exception " + e);

			}
		}
	}

	private class BackGroundTaskForLogout extends AsyncTask<String, Void, Void> {

		private String response;
		private boolean responseSuccess;
		private List<NameValuePair> logOutParameter;
		private LogOutData logOutData;
		private Ultilities mUltilities = new Ultilities();

		@Override
		protected Void doInBackground(String... params) {
			logDebug("BackGroundTaskForLogout");
			try {
				logOutParameter = mUltilities.getLogOutParameter(params);
				logDebug("BackGroundTaskForLogout  logOutParameter"
						+ logOutParameter);
				response = mUltilities.makeHttpRequest(Constant.logout_url,
						Constant.methodeName, logOutParameter);
				logDebug("BackGroundTaskForLogout  response" + response);
				Gson gson = new Gson();
				logOutData = gson.fromJson(response, LogOutData.class);
				logDebug("BackGroundTaskForLogout  errornumber is "
						+ logOutData.getErrNum());
				logDebug("BackGroundTaskForLogout  errorFlag is "
						+ logOutData.getErrFlag());
				if (logOutData.getErrFlag() == 0
						&& logOutData.getErrNum() == 41) {
					Session session = Session.getActiveSession();
					logDebug("BackGroundTaskForLogout  session " + session);
					if (!session.isClosed()) {
						logDebug("BackGroundTaskForLogout  session not close need to cleaar  "
								+ session.getState());
						session.closeAndClearTokenInformation();
					} else {
						// nothing session already close no need to clear
					}

					// DatabaseHandler databaseHandler=new
					// DatabaseHandler(getActivity());
					// databaseHandler.deleteUserData();
				} else if (logOutData.getErrFlag() == 1
						&& logOutData.getErrNum() == 31) {
					Session session = Session.getActiveSession();
					logDebug("BackGroundTaskForLogout  session " + session);
					if (!session.isClosed()) {
						logDebug("BackGroundTaskForLogout  session not close need to cleaar  "
								+ session.getState());
						session.closeAndClearTokenInformation();

					} else {
						// nothing session already close no need to clear
					}
				}

			} catch (Exception e) {
				logError("BackGroundTaskForLogout  doInBackground Exception  "
						+ e);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			try {
				mDialog.dismiss();
				if (logOutData.getErrFlag() == 0
						&& logOutData.getErrNum() == 41) {
					SessionManager mSessionManager = new SessionManager(
							getActivity());
					mSessionManager.logoutUser();
					Intent intent = new Intent(getActivity(),
							LoginUsingFacebook.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					getActivity().finish();
				} else if (logOutData.getErrFlag() == 1
						&& logOutData.getErrNum() == 37) {
					ErrorMessage("Alert", logOutData.getErrMsg());
				} else if (logOutData.getErrFlag() == 1
						&& logOutData.getErrNum() == 31) {
					ErrorMessageInvalidSessionTOken("Alert",
							logOutData.getErrMsg());
				}

			} catch (Exception e) {
				logError("BackGroundTaskForLogout onPostExecute Exception " + e);
			}

			/*
			 * Error: { "errNum": "37", "errFlag": "1", "errMsg":
			 * "Server Error! Please try again after sometime!" }
			 * 
			 * 
			 * Error: { "errNum": "31", "errFlag": "1", "errMsg":
			 * "invalid session token!" } Success: Error: { "errNum": "41",
			 * "errFlag": "0", "errMsg": "Logged out successfully" }
			 */

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			try {
				mDialog = mUltilities.GetProcessDialog(getActivity());
				mDialog.setMessage("Please wait..");
				mDialog.setCancelable(true);
				mDialog.show();
			} catch (Exception e) {
				logError("BackGroundTaskForLogout onPreExecute Exception " + e);
			}
		}
	}

	private void updateUserPrefrence() {
		SessionManager mSessionManager = new SessionManager(getActivity());
		String sessionToke = mSessionManager.getUserToken();
		String deviceid = Ultilities.getDeviceId(getActivity());
		String loweragePrefrence = "" + minAge;
		String heigherAge = "" + maxAge;
		String sexPrefrence = "" + usersexprefrence;
		String selectedusersex = "" + usersex;
		String userSelectedRadius = "" + radious;
		String[] params = { sessionToke, deviceid, selectedusersex,
				sexPrefrence, loweragePrefrence, heigherAge,
				userSelectedRadius, "" + distanceunilt };
		new BackgroundTaskForUpdatePrefrence().execute(params);
	}

	private class BackgroundTaskForUpdatePrefrence extends
			AsyncTask<String, Void, Void> {
		private String response;
		private boolean responseSuccess;
		private List<NameValuePair> prefrenceUpdateParameter;
		private UpdatePrefrence mUpdatePrefrence;
		private Ultilities mUltilities = new Ultilities();
		private String loweragePrefrence, heigherAge, sexPrefrence,
				selectedusersex, userSelectedRadius;

		@Override
		protected Void doInBackground(String... params) {
			try {
				selectedusersex = params[2];
				sexPrefrence = params[3];
				loweragePrefrence = params[4];
				heigherAge = params[5];
				userSelectedRadius = params[6];

				prefrenceUpdateParameter = mUltilities
						.getUserPrefrenceParameter(params);
				response = mUltilities.makeHttpRequest(
						Constant.updatePrefrence_url, Constant.methodeName,
						prefrenceUpdateParameter);
				logDebug("BackgroundTaskForUpdatePrefrence doInBackground  response "
						+ response);
				Gson gson = new Gson();
				mUpdatePrefrence = gson.fromJson(response,
						UpdatePrefrence.class);
			} catch (Exception e) {
				logError("BackgroundTaskForUpdatePrefrence doInBackground  Exception "
						+ e);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			try {
				mDialog.dismiss();
				if (mUpdatePrefrence.getErrFlag() == 0
						&& mUpdatePrefrence.getErrNum() == 13) {
					SessionManager mSessionManager = new SessionManager(
							getActivity());
					mSessionManager.setDistance(Integer
							.parseInt(userSelectedRadius));
					mSessionManager.setUserHeigherAge(heigherAge);
					mSessionManager.setUserLowerAge(loweragePrefrence);

					mSessionManager.setUserSex(selectedusersex);
					mSessionManager.setUserPrefSex(sexPrefrence);
					mSessionManager.setDistaceUnit(distanceunit);

					Toast.makeText(getActivity(), mUpdatePrefrence.getErrMsg(),
							Toast.LENGTH_SHORT).show();
					// "Preferences updated successfully."

				} else if (mUpdatePrefrence.getErrFlag() == 1
						&& mUpdatePrefrence.getErrNum() == 14) {
					// Change any preferences to update
					mDialog.dismiss();
					ErrorMessage("Alert", mUpdatePrefrence.getErrMsg());
				} else {
					mDialog.dismiss();
					ErrorMessage("Alert",
							"Sorry! Server not able to process your request please  try again");
				}

			} catch (Exception e) {
				logError(" BackgroundTaskForUpdatePrefrence onPostExecute Exception "
						+ e);
			}

			/*
			 * Error: { "errNum": "14", "errFlag": "1", "errMsg":
			 * "Change any preferences to update." }
			 * 
			 * Success: { "errNum": "13", "errFlag": "0", "errMsg":
			 * "Preferences updated successfully." }
			 */

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			mDialog = mUltilities.GetProcessDialog(getActivity());
			mDialog.setMessage("Please wait..");
			mDialog.setCancelable(true);
			mDialog.show();
		}

	}

	private void ErrorMessage(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

	private void ErrorMessageInvalidSessionTOken(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title);
		builder.setMessage(message);

		builder.setPositiveButton(
				getResources().getString(R.string.okbuttontext),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SessionManager mSessionManager = new SessionManager(
								getActivity());
						mSessionManager.logoutUser();
						Intent intent = new Intent(getActivity(),
								LoginUsingFacebook.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						getActivity().finish();
						dialog.dismiss();
					}
				});

		AlertDialog alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}

	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			// updateView();
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		Session.getActiveSession().addCallback(statusCallback);
		FlurryAgent.onStartSession(getActivity(), Constant.flurryKey);

	}

	@Override
	public void onStop() {
		super.onStop();
		Session.getActiveSession().removeCallback(statusCallback);
		FlurryAgent.onEndSession(getActivity());

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(getActivity(), requestCode,
				resultCode, data);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}

	/*
	 * @Override protected void onSaveInstanceState(Bundle outState) {
	 * super.onSaveInstanceState(outState); Session session =
	 * Session.getActiveSession(); Session.saveSession(session, outState); }
	 */
}
