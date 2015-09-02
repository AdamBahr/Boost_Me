package com.android.slidingmenu;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdupe.androidpushnotifications.ChatActivity;
import com.appdupe.flamer.pojo.ImageDetail;
import com.appdupe.flamer.pojo.LikeMatcheddataForListview;
import com.appdupe.flamer.utility.Constant;
import com.appdupe.flamer.utility.ScalingUtilities;
import com.appdupe.flamer.utility.SessionManager;
import com.appdupe.flamer.utility.Ultilities;
import com.appdupe.flamer.utility.ScalingUtilities.ScalingLogic;
import com.appdupe.flamer.R;
import com.appdupe.flamerchat.db.DatabaseHandler;

public class MatchFoundActivity extends Activity implements OnClickListener {

	private static String TAG = "MatchFoundActivity";

	private TextView bothUserTextview;
	private ImageView userImageview, friendImageview;
	private Button sendMessageButton, keepSwiping;
	private String strSenderFbId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.match_found_screen);

		initComponent();

		Bundle bucket = getIntent().getExtras();
		if (bucket != null) {
			strSenderFbId = bucket.getString("SENDER_FB_ID");
			String strSenderuserName = bucket.getString("SENDER_USERNAME");
			bothUserTextview.setText("You and " + strSenderuserName
					+ " Have liked each other.");
		}
		// setUserImage();
		setProfilePick(userImageview);
		setFriendImage();

		sendMessageButton.setOnClickListener(this);
		keepSwiping.setOnClickListener(this);
	}

	private void setFriendImage() {
		DatabaseHandler mDatabaseHandler = new DatabaseHandler(this);
		Ultilities mUltilities = new Ultilities();
		SessionManager manager = new SessionManager(this);
		String mfacebookid = manager.getFacebookId();
		LikeMatcheddataForListview matcheddataForListview = mDatabaseHandler
				.getSenderDetail(strSenderFbId);
		String imagePath = matcheddataForListview.getFilePath();
		Bitmap bitmapimage = mUltilities.showImage/*
												 * setImageToImageViewBitmapFactory.
												 * decodeFiledecodeFile
												 */(imagePath);
		ScalingUtilities mScalingUtilities = new ScalingUtilities();
		Bitmap cropedBitmap = mScalingUtilities.createScaledBitmap(bitmapimage,
				200, 200, ScalingLogic.CROP);
		bitmapimage.recycle();
		Bitmap friendImage = mUltilities.getCircleBitmap(cropedBitmap, 1);
		friendImageview.setImageBitmap(friendImage);
		cropedBitmap.recycle();
	}

	// private void setUserImage() {
	// // TODO Auto-generated method stub
	// File appDirectory;
	//
	// File _picDir;
	// File myimgFile;
	// try {
	// Ultilities mUltilities=new Ultilities();
	// appDirectory =
	// mUltilities.createAppDirectoy(getResources().getString(R.string.appdirectory));
	// _picDir = new File(appDirectory,
	// getResources().getString(R.string.imagedirectory));
	// myimgFile= new File(_picDir,
	// getResources().getString(R.string.imagefilename)+"0.jpg");
	// Bitmap bitmapimage =
	// mUltilities.showImage/*setImageToImageViewBitmapFactory.decodeFiledecodeFile*/(myimgFile.getAbsolutePath());
	// ScalingUtilities mScalingUtilities =new ScalingUtilities();
	// Bitmap cropedBitmap= mScalingUtilities.createScaledBitmap(bitmapimage,
	// 200, 200, ScalingLogic.CROP);
	// bitmapimage.recycle();
	// Bitmap userImage= mUltilities.getCircleBitmap(cropedBitmap, 1);
	// userImageview.setImageBitmap(userImage);
	//
	// // logDebug("onCreate   userImage "+userImage);
	// cropedBitmap.recycle();
	//
	//
	//
	// } catch (Exception e)
	// {
	// // TODO: handle exception
	// //logError("onCreate  Exception "+e);
	//
	// }
	// }

	private void setProfilePick(ImageView userProfilImage) {
		Ultilities mUltilities = new Ultilities();
		File appDirectory;
		File _picDir;
		File myimgFile;
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
							bitmapimage, 200, 200, ScalingLogic.CROP);
					bitmapimage.recycle();
					mBitmap = mUltilities.getCircleBitmap(cropedBitmap, 1);
					cropedBitmap.recycle();
					userProfilImage.setImageBitmap(mBitmap);
				} else {

				}

			} else {

			}

		} catch (Exception e) {
			// TODO: handle exception

			ImageView[] params = { userProfilImage };
			// new
			// BackGroundTaskForDownloadProfileImageIfUseDeletedFormDirectory().execute(params);
		}

	}

	private void initComponent() {
		// TODO Auto-generated method stub
		bothUserTextview = (TextView) findViewById(R.id.both_user_textview);
		userImageview = (ImageView) findViewById(R.id.user_imageview);
		friendImageview = (ImageView) findViewById(R.id.friend_imageview);
		sendMessageButton = (Button) findViewById(R.id.send_message_button);
		keepSwiping = (Button) findViewById(R.id.keep_swiping);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.send_message_button) {
			Bundle mBundle = new Bundle();
			mBundle.putString(Constant.FRIENDFACEBOOKID, strSenderFbId);
			mBundle.putString(Constant.CHECK_FOR_PUSH_OR_NOT, "1");

			Intent mIntent = new Intent(MatchFoundActivity.this,
					ChatActivity.class);
			mIntent.putExtras(mBundle);
			startActivity(mIntent);
			finish();
		}
		if (v.getId() == R.id.keep_swiping) {
			finish();
		}
	}
}
