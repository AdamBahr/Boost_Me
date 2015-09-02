package com.appdupe.androidpushnotifications;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.NameValuePair;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slidingmenu.BitmapLruCache;
import com.android.slidingmenu.MatChedUserProfile;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.appdupe.flamer.pojo.ImageDetail;
import com.appdupe.flamer.pojo.LikeMatcheddataForListview;
import com.appdupe.flamer.utility.Constant;
import com.appdupe.flamer.utility.ScalingUtilities;
import com.appdupe.flamer.utility.SessionManager;
import com.appdupe.flamer.utility.Ultilities;
import com.appdupe.flamer.R;
import com.appdupe.flamerchat.db.DatabaseHandler;
import com.google.gson.Gson;

public class ChatActivity extends ListActivity implements OnClickListener {

	RequestQueue mRequestQueue;
	private ImageLoader imageLoader;
	private static boolean mDebugLog = true;
	private static String mDebugTag = "ChatActivity";

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

	private static String TAG = "ChatActivity";
	private ListView chatList;
	private EditText chatEditText;
	private Button sendChatMessageButton;
	private String strFbIdFriend/* ="100000877715757" */;
	private BroadcastReceiver receiver;

	// private String strFbIdXolo="100000877715757";
	private String strFriendFbId;// xolo
	// private String strFbId="100000877715757";//lenovo

	// private String deviceId="911328850098461";//xolo
	private String deviceId/* ="911328850098461" */;// lenovo
	private boolean bFlagForHistoryMessage = false;

	private ChatMessageList objMessageData;
	private AwesomeAdapter messageAdapter;
	private ArrayList<ChatMessageList> listChatData = new ArrayList<ChatMessageList>();
	private String userFacebookid;
	private String sessiosntoken;
	private Bitmap userImage;
	private Bitmap friendImage;
	IntentFilter filter;
	private RelativeLayout loadmoreButton, userProfilelayout, flagreportlayout,
			blockuserlayout;
	// private int startLimit=0,endLimit=30,limit=30,pageNum=1;
	private ImageView senderimage;
	private TextView senderName, MoreOptionLayout;
	private String senderimageUrl;
	private String cureentImageurl;
	private Animation blockUserAnimation, blockuserAnimationBottomToUp;
	private RelativeLayout blockUserLayout;
	private View headerView;
	private Button userinfoimageview;
	private Button popumenubutton;
	private AQuery aQuery;
	private int startLimit = 0, endLimit = 2, limit = 30, pageNum = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RequestQueue mRequestQueue = Volley.newRequestQueue(this);
		imageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(
				BitmapLruCache.getDefaultLruCacheSize()));
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chat_screen);
		aQuery = new AQuery(this);
		initComponent();

		Bundle bucket = getIntent().getExtras();
		Ultilities mUltilities = new Ultilities();
		File appDirectory;
		File _picDir;
		File myimgFile;

		try {

			setProfilePick();

		} catch (Exception e) {
			logError("onCreate  Exception " + e);

		}

		// Log.d("facebookidddd.........................."," background oncreate     bucket.............."+
		// bucket);
		if (bucket != null) {
			String checkForPush = bucket
					.getString(Constant.CHECK_FOR_PUSH_OR_NOT);
			Log.i(TAG, "background checkForPush..........." + checkForPush);
			if (checkForPush != null && checkForPush.equals("1")) {
				strFriendFbId = bucket.getString(Constant.FRIENDFACEBOOKID);
				// Log.i(TAG,
				// "face book id    friend face book id id "+strFbIdFriend);
				// Log.i(TAG, "face book id   user facebok id face book id id "+
				// new SessionManager(this).getFacebookId());
			} else {
				Bundle bucket1 = getIntent().getBundleExtra(
						"PUSH_MESSAGE_BUNDLE");
				strFriendFbId = bucket1.getString("sfid");
			}

		} else {

		}

		DatabaseHandler mDatabaseHandler = new DatabaseHandler(this);
		SessionManager manager = new SessionManager(this);
		String mfacebookid = manager.getFacebookId();
		LikeMatcheddataForListview matcheddataForListview = mDatabaseHandler
				.getSenderDetail(strFriendFbId);
		Log.i(TAG, "oncreate imagePath  matcheddataForListview"
				+ matcheddataForListview);

		String imagePath = matcheddataForListview.getFilePath();
		senderimageUrl = matcheddataForListview.getImageUrl();
		String senderNamevalues = matcheddataForListview.getUserName();

		ScalingUtilities mScalingUtilities = new ScalingUtilities();

		// senderimage.setImageUrl(senderimageUrl, imageLoader,
		// "CircularImge",this);
		// senderimage.setImageUrl(senderimageUrl, imageLoader);
		aQuery.id(senderimage).image(senderimageUrl);
		senderName.setText(senderNamevalues);

		com.appdupe.flamer.utility.SessionManager mSessionManager = new com.appdupe.flamer.utility.SessionManager(
				this);
		userFacebookid = mSessionManager.getFacebookId();
		sessiosntoken = mSessionManager.getUserToken();
		// Log.d("facebookidddd..........................","background oncreate  facerbook iddddddddddddd .............."+
		// strFriendFbId);

		deviceId = Ultilities.getDeviceId(ChatActivity.this);

		// signedactivitylistview.addHeaderView(headerView,null, false);
		getListView().addHeaderView(headerView);
		messageAdapter = new AwesomeAdapter(ChatActivity.this, listChatData);
		setListAdapter(messageAdapter);
		if (bFlagForHistoryMessage) {
			String[] params = { sessiosntoken, deviceId, strFriendFbId, "300" };
			new BackgroundforMessagehistory().execute(params);
		} else {
			// Log.i(TAG, "onCreate get message from the db");

			new BackgroundForGetDataFromDB().execute();
		}

		filter = new IntentFilter();
		filter.addAction("com.embed.anddroidpushntificationdemo11.push");

		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				Bundle bucket = intent.getExtras();

				String strMessage = bucket.getString("MESSAGE_FOR_PUSH");
				String strMessageType = bucket
						.getString("MESSAGE_FOR_PUSH_MESSAGETYPE");
				String strMessageId = bucket
						.getString("MESSAGE_FOR_PUSH_MESSAGEID");
				String strSenderName = bucket
						.getString("MESSAGE_FOR_PUSH_SENDERNAME");
				String strDateTime = bucket
						.getString("MESSAGE_FOR_PUSH_DATETIME");
				String strFacebookId = bucket
						.getString("MESSAGE_FOR_PUSH_FACEBOOKID");

				if (strMessageType.equals("2")) {
					String[] params = { sessiosntoken, deviceId, strMessageId };
					new BackgroundForPullMessage().execute(params);
				} else {
					ChatMessageList objMessageData = new ChatMessageList();
					objMessageData.setStrMessage(strMessage);
					objMessageData.setStrDateTime(strDateTime);
					objMessageData.setStrSenderFacebookId(strFacebookId);
					objMessageData.setStrSendername(strSenderName);
					objMessageData.setStrFlagForMessageSuccess("1");
					objMessageData.setStrReceiverId(userFacebookid);
					Log.i(TAG, "onReceive setStrSenderId...." + strFriendFbId);
					objMessageData.setStrSenderId(strFriendFbId);
					if (strFriendFbId.equals(strFacebookId)) {
						listChatData.add(objMessageData);
					} else {
						Toast.makeText(ChatActivity.this,
								"You have message from :" + strSenderName,
								Toast.LENGTH_SHORT).show();
					}

					new BackgroundForInsertMessageDB().execute(objMessageData);
				}

				messageAdapter.notifyDataSetChanged();
				getListView().setSelection(listChatData.size());
			}
		};

		sendChatMessageButton.setOnClickListener(this);
		loadmoreButton.setOnClickListener(this);
		senderimage.setOnClickListener(this);
		MoreOptionLayout.setOnClickListener(this);
		userinfoimageview.setOnClickListener(this);
		flagreportlayout.setOnClickListener(this);
		blockuserlayout.setOnClickListener(this);
	}

	private void setProfilePick() {
		Ultilities mUltilities = new Ultilities();
		File appDirectory;
		File _picDir;
		File myimgFile;
		try {

			DatabaseHandler mdaDatabaseHandler = new DatabaseHandler(this);
			String imageOrderArray[] = { "1" };
			ArrayList<ImageDetail> imagelist = mdaDatabaseHandler
					.getImageDetailByImageOrder(imageOrderArray);
			if (imagelist != null && imagelist.size() > 0) {

				cureentImageurl = imagelist.get(0).getImageUrl();

			} else {

			}

		} catch (Exception e) {
			// ImageView [] params={userProfilImage};
			// new
			// BackGroundTaskForDownloadProfileImageIfUseDeletedFormDirectory().execute(params);
		}

	}

	private void initComponent() {
		// chatList=(ListView) findViewById(R.id.list);
		popumenubutton = (Button) findViewById(R.id.popumenubutton);

		popumenubutton.setVisibility(View.GONE);

		popumenubutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Creating the instance of PopupMenu
				PopupMenu popup = new PopupMenu(ChatActivity.this,
						popumenubutton);
				// Inflating the Popup using xml file
				popup.getMenuInflater().inflate(R.menu.popup_menu,
						popup.getMenu());

				// registering popup with OnMenuItemClickListener
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item) {
						Toast.makeText(ChatActivity.this,
								"You Clicked : " + item.getTitle(),
								Toast.LENGTH_SHORT).show();
						return true;
					}
				});

				popup.show();// showing popup menu
			}
		});// closing the setOnClickListener method

		blockUserLayout = (RelativeLayout) findViewById(R.id.blockUserLayout);
		headerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.header, null, false);
		// headerView.setVisibility(View.GONE);
		chatEditText = (EditText) findViewById(R.id.chat_editText);
		sendChatMessageButton = (Button) findViewById(R.id.send_chat_message_button);

		loadmoreButton = (RelativeLayout) headerView
				.findViewById(R.id.loadmore_button);
		loadmoreButton.setVisibility(View.GONE);
		senderimage = (ImageView) findViewById(R.id.senderimage);
		senderName = (TextView) findViewById(R.id.senderName);
		MoreOptionLayout = (TextView) findViewById(R.id.MoreOptionLayout);

		userProfilelayout = (RelativeLayout) findViewById(R.id.userProfilelayout);
		flagreportlayout = (RelativeLayout) findViewById(R.id.flagreportlayout);
		blockuserlayout = (RelativeLayout) findViewById(R.id.blockuserlayout);

		userinfoimageview = (Button) findViewById(R.id.userinfoimageview);

		Typeface topbartextviewFont = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Light.otf");
		senderName.setTypeface(topbartextviewFont);
		senderName.setTextColor(Color.rgb(255, 255, 255));
		senderName.setTextSize(20);

		MoreOptionLayout.setTypeface(topbartextviewFont);
		MoreOptionLayout.setTextColor(Color.rgb(255, 255, 255));
		MoreOptionLayout.setTextSize(20);

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (receiver != null) {
			registerReceiver(receiver, filter);
		} else {

		}
		SessionManager session = new SessionManager(ChatActivity.this);
		session.setFirstScreen(true);

		// chatList.setAdapter(messageAdapter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		SessionManager session = new SessionManager(ChatActivity.this);
		session.setFirstScreen(false);
		unregisterReceiver(receiver);
		if (bFlagForHistoryMessage) {

		} else {

		}
		// unregisterReceiver(receiver);
	}

	@Override
	protected void onStop() {
		super.onStop();

		// unregisterReceiver(deliveryBroadcastReceiver);

	}

	private class BackgroundForGetDataFromDB extends
			AsyncTask<String, Void, ArrayList<ChatMessageList>> {

		@Override
		protected ArrayList<ChatMessageList> doInBackground(String... params) {
			DatabaseHandler objDBHandler = new DatabaseHandler(
					ChatActivity.this);
			/*
			 * if (pageNum==1) { listChatData.clear(); } else {a
			 * 
			 * }
			 */
			if (pageNum == 0) {
				listChatData.clear();
			} else {

			}

			ArrayList<ChatMessageList> listChatDataDb = objDBHandler
					.getChatMessages(strFriendFbId, startLimit, limit);
			Log.i(TAG, "BackgroundForGetDataFromDB listChatDataDb size....."
					+ listChatDataDb.size());
			// listChatData.addAll(listChatDataDb);

			return listChatDataDb;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(ArrayList<ChatMessageList> result) {
			super.onPostExecute(result);
			Log.i(TAG,
					"BackgroundForGetDataFromDB result size....."
							+ result.size());
			Collections.reverse(result);
			// Collections.reverse(listChatDataDb);
			listChatData.addAll(0, result);
			messageAdapter.notifyDataSetChanged();
			if (listChatData != null && listChatData.size() > 0) {
				getListView().setSelection(listChatData.size());
			}

		}
	}

	private class BackgroundForPullMessage extends
			AsyncTask<String, Void, ChatMessageData> {

		@Override
		protected ChatMessageData doInBackground(String... arg0) {
			// String
			// sendMessageUrl="http://108.166.190.172:81/tinderClone/process.php/getChatMessage";

			Utility myUtility = new Utility();
			// String params[]={};
			List<NameValuePair> sendMessageReqList = myUtility
					.getPullMessageReq(arg0);
			String messageResponse = myUtility.makeHttpRequest(
					Constant.getChatMessage_url, Constant.methodeName,
					sendMessageReqList);
			ChatMessageData objChatMessage = null;
			if (messageResponse != null) {
				Gson gson = new Gson();
				objChatMessage = gson.fromJson(messageResponse,
						ChatMessageData.class);
				if (objChatMessage != null) {
					if (objChatMessage.getErrFlag() == 0) {
						List<ChatMessageList> listChat = objChatMessage
								.getListChat();
						ChatMessageList objChatMessageData = listChat.get(0);
						objChatMessageData.setStrFlagForMessageSuccess("1");
						objChatMessageData.setStrSenderId(objChatMessageData
								.getStrSenderFacebookId());
						objChatMessageData.setStrReceiverId(userFacebookid);
						DatabaseHandler objDBHandler = new DatabaseHandler(
								ChatActivity.this);
						listChatData.add(objChatMessageData);
						objDBHandler.insertMessageData(objChatMessageData);

					} else {

					}
				} else {

				}
			} else {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(ChatActivity.this,
								R.string.request_timeout, Toast.LENGTH_SHORT)
								.show();
					}
				});
			}

			return objChatMessage;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(ChatMessageData result) {
			super.onPostExecute(result);

			messageAdapter.notifyDataSetChanged();
			getListView().setSelection(listChatData.size());
		}
	}

	private class BackgroundforMessagehistory extends
			AsyncTask<String, Void, ChatMessageData> {

		@Override
		protected ChatMessageData doInBackground(String... params) {

			// String
			// sendMessageUrl="http://108.166.190.172:81/tinderClone/process.php/getChatHistory";

			Utility myUtility = new Utility();
			// String params[]={};
			List<NameValuePair> sendMessageReqList = myUtility
					.getPullMessageReq(params);
			String messageResponse = myUtility.makeHttpRequest(
					Constant.getChatHistory_url, Constant.methodeName,
					sendMessageReqList);
			ChatMessageData objChatMessage = null;
			if (messageResponse != null) {
				Gson gson = new Gson();
				objChatMessage = gson.fromJson(messageResponse,
						ChatMessageData.class);
				if (objChatMessage != null) {
					if (objChatMessage.getErrFlag() == 0) {
						List<ChatMessageList> listChat = objChatMessage
								.getListChat();
						ChatMessageList objChatMessageData = listChat.get(0);
						objChatMessageData.setStrSenderId(objChatMessageData
								.getStrSenderFacebookId());
						objChatMessageData.setStrReceiverId(userFacebookid);
						objChatMessageData.setStrFlagForMessageSuccess("1");
						DatabaseHandler objDBHandler = new DatabaseHandler(
								ChatActivity.this);
						listChatData.add(objChatMessageData);
						objDBHandler.insertMessageData(objChatMessageData);

					} else {

					}
				} else {

				}
			} else {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(ChatActivity.this,
								R.string.request_timeout, Toast.LENGTH_SHORT)
								.show();
					}
				});
			}

			return objChatMessage;

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(ChatMessageData result) {
			super.onPostExecute(result);
			if (result != null) {
				if (result.getErrFlag() == 0) {
					List<ChatMessageList> listChat = result.getListChat();
					listChatData.clear();
					listChat.addAll(listChat);
				} else {

				}
			} else {

			}
		}
	}

	private class BackgroundForInsertMessageDB extends
			AsyncTask<ChatMessageList, Void, String> {
		@Override
		protected String doInBackground(ChatMessageList... params) {
			DatabaseHandler objDBHandler = new DatabaseHandler(
					ChatActivity.this);
			Log.i(TAG,
					"doInBackground params getStrMessage...."
							+ params[0].getStrSenderId());
			params[0].setStrReceiverId(userFacebookid);
			// params[0].setStrSenderId(params[0].getStrSenderFacebookId());
			params[0].setStrFlagForMessageSuccess("1");
			objDBHandler.insertMessageData(params[0]);
			return null;
		}

	}

	public class AwesomeAdapter extends BaseAdapter {
		private Context mContext;
		private ArrayList<ChatMessageList> mMessages;

		public AwesomeAdapter(Context context,
				ArrayList<ChatMessageList> messages) {
			super();
			this.mContext = context;
			this.mMessages = messages;
		}

		@Override
		public int getCount() {
			return mMessages.size();
		}

		@Override
		public Object getItem(int position) {
			return mMessages.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ChatMessageList message = (ChatMessageList) this.getItem(position);

			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.sms_row, parent, false);
				holder.message = (TextView) convertView
						.findViewById(R.id.message_text);
				holder.userImageview = (NetworkImageView) convertView
						.findViewById(R.id.userImageview);
				holder.chatedate = (TextView) convertView
						.findViewById(R.id.chatedate);
				holder.chateboxlayout = (RelativeLayout) convertView
						.findViewById(R.id.chateboxlayout);
				convertView.setTag(holder);
			} else
				holder = (ViewHolder) convertView.getTag();

			holder.message.setText(message.getStrMessage());
			holder.chatedate.setText(message.getStrDateTime());

			// LayoutParams lp = (LayoutParams)
			// holder.message.getLayoutParams();

			String userId = "";
			Log.i(TAG,
					"getView userFacebookid........." + userFacebookid
							+ "..........getStrSenderFacebookId........."
							+ message.getStrSenderFacebookId());
			if (userFacebookid.equals("" + message.getStrSenderFacebookId())) {

				RelativeLayout.LayoutParams paramsImage = new RelativeLayout.LayoutParams(
						50, 50);
				// paramsImage.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
				// RelativeLayout.TRUE);
				paramsImage.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				paramsImage.setMargins(0, 0, 10, 0);
				holder.userImageview.setLayoutParams(paramsImage);

				RelativeLayout.LayoutParams paramsTextview = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				paramsTextview.addRule(RelativeLayout.LEFT_OF,
						holder.userImageview.getId());
				paramsTextview.setMargins(0, 0, 10, 0);
				holder.message.setLayoutParams(paramsTextview);

				holder.message
						.setBackgroundResource(R.drawable.speech_bubble_green);
				// lp.gravity = Gravity.RIGHT;
				// holder.userImageview.setImageUrl(cureentImageurl,
				// imageLoader, "CircularImge",ChatActivity.this);
				holder.userImageview.setImageUrl(cureentImageurl, imageLoader);
				// holder.userImageview.setImageBitmap(userImage);

			}
			// If not mine then it is from sender to show orange background and
			// align to left
			else {
				RelativeLayout.LayoutParams paramsImage = new RelativeLayout.LayoutParams(
						50, 50);
				// paramsImage.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
				// RelativeLayout.TRUE);
				paramsImage.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				paramsImage.setMargins(10, 0, 0, 0);
				holder.userImageview.setLayoutParams(paramsImage);

				RelativeLayout.LayoutParams paramsTextview = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				paramsTextview.addRule(RelativeLayout.RIGHT_OF,
						holder.userImageview.getId());
				paramsTextview.setMargins(10, 0, 0, 0);
				holder.message.setLayoutParams(paramsTextview);

				holder.message
						.setBackgroundResource(R.drawable.speech_bubble_orange);
				// lp.gravity = Gravity.LEFT;
				// holder.userImageview.setImageUrl(senderimageUrl, imageLoader,
				// "CircularImge",ChatActivity.this);
				holder.userImageview.setImageUrl(senderimageUrl, imageLoader);

				// holder.userImageview.setImageBitmap(friendImage);
			}
			// holder.message.setLayoutParams(lp);

			// holder.message.setTextColor(R.color.textColor);
			/* } */
			return convertView;
		}

		private class ViewHolder {
			TextView message;
			NetworkImageView userImageview;
			TextView chatedate;
			RelativeLayout chateboxlayout;
		}

		@Override
		public long getItemId(int position) {
			// Unimplemented, because we aren't using Sqlite.
			return 0;
		}
	}

	private class BackGroundForSendMessage extends
			AsyncTask<String, Void, SendMessageResponse> {
		@Override
		protected SendMessageResponse doInBackground(String... arg0) {
			// String
			// sendMessageUrl="http://108.166.190.172:81/tinderClone/process.php/sendMessage";

			Utility myUtility = new Utility();
			// String params[]={};
			List<NameValuePair> sendMessageReqList = myUtility
					.getSendMessageReq(arg0);
			String messageResponse = myUtility.makeHttpRequest(
					Constant.sendMessage_url, Constant.methodeName,
					sendMessageReqList);
			SendMessageResponse sendMessagerespObj = null;
			if (messageResponse != null) {
				Gson gson = new Gson();
				sendMessagerespObj = gson.fromJson(messageResponse,
						SendMessageResponse.class);
				objMessageData = new ChatMessageList();
				objMessageData.setStrSenderFacebookId(userFacebookid);
				objMessageData.setStrMessage(arg0[3]);
				objMessageData.setStrDateTime("");
				objMessageData.setStrSendername("");
				objMessageData.setStrSenderId(userFacebookid);
				objMessageData.setStrReceiverId(strFriendFbId);
				DatabaseHandler objDBHandler = new DatabaseHandler(
						ChatActivity.this);
				if (sendMessagerespObj != null) {
					if (sendMessagerespObj.getStatusNumber() == 0) {
						objMessageData.setStrFlagForMessageSuccess("1");

						// listChatData.add(objMessageData);
						objDBHandler.insertMessageData(objMessageData);

					} else {
						objMessageData.setStrFlagForMessageSuccess("0");

						// listChatData.add(objMessageData);
						objDBHandler.insertMessageData(objMessageData);
					}
				} else {
					objMessageData.setStrFlagForMessageSuccess("0");

					// listChatData.add(objMessageData);
					objDBHandler.insertMessageData(objMessageData);
				}
			} else {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(ChatActivity.this,
								R.string.request_timeout, Toast.LENGTH_SHORT)
								.show();
					}
				});
			}

			return sendMessagerespObj;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(SendMessageResponse result) {
			super.onPostExecute(result);
			if (result != null) {
				if (result.getStatusNumber() == 0) {

				} else {
					Toast.makeText(ChatActivity.this,
							result.getStatusMessage(), Toast.LENGTH_SHORT)
							.show();
				}
			} else {

			}
		}
	}

	@Override
	public void onClick(View arg0) {
		if (arg0.getId() == R.id.send_chat_message_button) {
			String chatMessage = chatEditText.getText().toString().trim();
			if (chatMessage != null && chatMessage.length() > 0) {
				/* if (Utility.isNetworkAvailable(ChatActivity.this)) { */
				objMessageData = new ChatMessageList();
				objMessageData.setStrSenderFacebookId(userFacebookid);
				objMessageData.setStrMessage(chatMessage);
				objMessageData.setStrDateTime("");
				objMessageData.setStrSendername("");
				objMessageData.setStrSenderId(userFacebookid);
				objMessageData.setStrReceiverId(strFriendFbId);
				Log.i(TAG, "onClick     userFacebookid " + userFacebookid);
				Log.i(TAG, "background onClick     strFriendFbId "
						+ strFriendFbId);
				listChatData.add(objMessageData);
				chatEditText.setText("");
				messageAdapter.notifyDataSetChanged();
				getListView().setSelection(listChatData.size());
				String params[] = { sessiosntoken, deviceId, strFriendFbId,
						chatMessage };
				new BackGroundForSendMessage().execute(params);
				/*
				 * } else { Toast.makeText(ChatActivity.this,
				 * R.string.network_string , Toast.LENGTH_SHORT).show(); }
				 */
			} else {
				Toast.makeText(ChatActivity.this, R.string.enter_message,
						Toast.LENGTH_SHORT).show();
			}
		}
		if (arg0.getId() == R.id.loadmore_button) {
			/*
			 * pageNum++; endLimit=pageNum*limit; startLimit=endLimit-limit;
			 * String params[]={"1"}; new
			 * BackgroundForGetDataFromDB().execute(params);
			 * //startLimit=0,endLimit=30,limit=30,pageNum=1;
			 */
			pageNum = 1;
			startLimit = startLimit + limit;
			String params[] = { "1" };
			new BackgroundForGetDataFromDB().execute(params);
		}

		if (arg0.getId() == R.id.senderimage) {
			SessionManager mSessionManager = new SessionManager(
					ChatActivity.this);
			mSessionManager.setMatchedUserFacebookId(strFriendFbId);
			mSessionManager.setImageIndexForLikeDislike(0);
			Intent mIntent = new Intent(ChatActivity.this,
					MatChedUserProfile.class);
			Bundle bundle = new Bundle();
			bundle.putBoolean(Constant.isFromChatScreen, true);
			mIntent.putExtras(bundle);
			startActivity(mIntent);
		}
		if (arg0.getId() == R.id.MoreOptionLayout) {
			headerView.setVisibility(View.GONE);
			blockUserLayout.setVisibility(View.VISIBLE);
			blockUserLayout.startAnimation(blockUserAnimation);
		}

		// userProfilelayout.setOnClickListener(this);
		// flagreportlayout.setOnClickListener(this);
		// blockuserlayout.setOnClickListener(this);

		if (arg0.getId() == R.id.userinfoimageview) {
			logDebug("onClick   userProfile ");
			headerView.setVisibility(View.GONE);
			// blockUserLayout.startAnimation(blockuserAnimationBottomToUp);
			SessionManager mSessionManager = new SessionManager(
					ChatActivity.this);
			mSessionManager.setMatchedUserFacebookId(strFriendFbId);
			mSessionManager.setImageIndexForLikeDislike(0);
			Intent mIntent = new Intent(ChatActivity.this,
					MatChedUserProfile.class);
			Bundle bundle = new Bundle();
			bundle.putBoolean(Constant.isFromChatScreen, true);
			mIntent.putExtras(bundle);
			startActivity(mIntent);

		}
		if (arg0.getId() == R.id.flagreportlayout) {
			headerView.setVisibility(View.VISIBLE);
			blockUserLayout.startAnimation(blockuserAnimationBottomToUp);
			logDebug("onClick   flagreportlayout ");
			Intent email = new Intent(Intent.ACTION_SEND/* Intent.ACTION_VIEW */);
			email.setType("message/rfc822");
			// email.putExtra(Intent.EXTRA_EMAIL,emailStrArray);
			email.putExtra(Intent.EXTRA_SUBJECT, "Flamer app");
			email.putExtra(Intent.EXTRA_TEXT, "Flamer app!");
			startActivity(Intent.createChooser(email,
					"Choose an Email client :"));

		}
		if (arg0.getId() == R.id.blockuserlayout) {
			headerView.setVisibility(View.VISIBLE);
			blockUserLayout.startAnimation(blockuserAnimationBottomToUp);

			logDebug("onClick   flagreportlayout ");
		}

	}

}
