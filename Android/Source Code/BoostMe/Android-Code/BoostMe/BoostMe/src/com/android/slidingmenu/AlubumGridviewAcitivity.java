package com.android.slidingmenu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appdupe.flamer.LoginUsingFacebook;
import com.appdupe.flamer.R;
import com.appdupe.flamer.pojo.FaceBookAlbumData;
import com.appdupe.flamer.pojo.FaceBookAlubumScr;
import com.appdupe.flamer.pojo.GridViewData;
import com.appdupe.flamer.pojo.ImageDeleteData;
import com.appdupe.flamer.pojo.ImageDetail;
import com.appdupe.flamer.pojo.UpdateSessionData;
import com.appdupe.flamer.pojo.UploadImage;
import com.appdupe.flamer.pojo.UploadImageChunkResponse;
import com.appdupe.flamer.pojo.UploadeImageUrl;
import com.appdupe.flamer.utility.Base64;
import com.appdupe.flamer.utility.Constant;
import com.appdupe.flamer.utility.SessionManager;
import com.appdupe.flamer.utility.Ultilities;
import com.appdupe.flamerchat.db.DatabaseHandler;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.squareup.picasso.Picasso;

public class AlubumGridviewAcitivity extends Activity {
	private static final String TAG = "AlubumGridviewAcitivity";
	private Session.StatusCallback statusCallback = new SessionStatusCallback();
	private static boolean mDebugLog = true;
	private static String mDebugTag = "AlubumGridviewAcitivity";
	private ProgressDialog mdialog;
	final int PIC_CROP = 2;

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

	private GridView PhoneImageGrid;
	private ArrayList<GridViewData> gridViewlist;
	private ImageAdapter mAdapter;
	private String alubumid;
	private String alubumName;
	private TextView alubumNameTextivew;
	private int selectedImageIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gridviewlayout);
		PhoneImageGrid = (GridView) findViewById(R.id.PhoneImageGrid);
		gridViewlist = new ArrayList<GridViewData>();
		mAdapter = new ImageAdapter(this, gridViewlist);
		PhoneImageGrid.setAdapter(mAdapter);
		alubumNameTextivew = (TextView) findViewById(R.id.alubumNameTextivew);
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
			Bundle extras = getIntent().getExtras();
			alubumid = extras.getString(Constant.ALUBUMID);
			alubumName = extras.getString(Constant.ALUBUMNAME);
			alubumNameTextivew.setText(alubumName);
			getAlubumPick(alubumid, session);
		} catch (Exception e) {
			logError("onCreate Exception " + e);
		}
	}

	private void getAlubumPick(String alubumId, Session session) {
		logDebug("getAlubumPick  ");
		if (session.isOpened()) {
			String fqlQuery = "SELECT src_big from photo where aid =" + "'"
					+ alubumId + "'";

			Bundle param = new Bundle();
			param.putString("q", fqlQuery);
			Request request = new Request(session, "/fql", param,
					HttpMethod.GET, new Request.Callback() {
						public void onCompleted(Response response) {
							try {
								String[] pramas = { response.toString() };
								new BackGroundTaskForGetAlubumImage()
										.execute(pramas);
							} catch (Exception e) {
								logError("getAlubumPick Exception " + e);
							}

						}
					});

			Request.executeBatchAsync(request);
		} else {
			getOpenedSession();
		}
	}

	private class BackGroundTaskForGetAlubumImage extends
			AsyncTask<String, Void, Void> {
		Ultilities mUltilities = new Ultilities();
		private FaceBookAlbumData mAlubumData;
		private ArrayList<FaceBookAlubumScr> PickUrlList;
		private String alubumPickResponse;
		private GridViewData mGridViewData;
		private boolean flagForsuccess;

		// private ScalingUtilities mScalingUtilities = new ScalingUtilities();

		@Override
		protected Void doInBackground(String... params) {
			try {
				File appDirectory = mUltilities
						.createAppDirectoy(getResources().getString(
								R.string.appdirectory));
				File _picDir = new File(appDirectory, getResources().getString(
						R.string.alubumpic));

				alubumPickResponse = params[0];
				alubumPickResponse = alubumPickResponse.substring(
						alubumPickResponse.indexOf("state=") + 6,
						alubumPickResponse.indexOf("}, error:"));
				Gson gson = new Gson();
				mAlubumData = gson.fromJson(alubumPickResponse,
						FaceBookAlbumData.class);
				PickUrlList = mAlubumData.getAlubumScrsList();
				if (PickUrlList != null && PickUrlList.size() > 0) {
					for (int i = 0; i < PickUrlList.size(); i++) {
						mGridViewData = new GridViewData();
						mGridViewData
								.setPicUrl(PickUrlList.get(i).getPickUrl());
						gridViewlist.add(mGridViewData);
						runOnUiThread(new Runnable() {
							public void run() {
								mdialog.dismiss();
								mAdapter.notifyDataSetChanged();
							}
						});
					}

					for (int i = 0; i < gridViewlist.size(); i++) {

						File imageFile = mUltilities.createFileInSideDirectory(
								_picDir,
								getResources()
										.getString(R.string.gridviewimage)
										+ i
										+ ".jpg");
						com.appdupe.flamer.utility.Utility
								.addBitmapToSdCardFromURL(PickUrlList.get(i)
										.getPickUrl().replaceAll(" ", "%20"),
										imageFile);
						gridViewlist.get(i).setFilePath(imageFile);

					}

					flagForsuccess = true;
				} else {
					flagForsuccess = false;
				}
			} catch (Exception e) {
				logError("BackGroundTaskForGetAlubumImage  doInBackground    Exception "
						+ e);
				flagForsuccess = false;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			if (flagForsuccess) {

			} else {
				mdialog.dismiss();
				// ErrorMessage();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mdialog = mUltilities
					.GetProcessDialog(AlubumGridviewAcitivity.this);
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
	protected void onPause() {
		super.onPause();

		if (mdialog != null) {
			mdialog.dismiss();
			mdialog.cancel();
			mdialog = null;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PIC_CROP) {
			if (resultCode == RESULT_OK) {
				Bundle extras = data.getExtras();
				// get the cropped bitmap
				Bitmap thePic = extras.getParcelable("data");
				addSelectedImageSdcard(thePic);
			} else {
				// do nothing
			}
		} else {
			Session.getActiveSession().onActivityResult(this, requestCode,
					resultCode, data);
		}
	}

	private void addSelectedImageSdcard(Bitmap mBitmap) {
		logDebug("addSelectedImageSdcard");
		Bitmap selectedImage[] = { mBitmap };
		logDebug("addSelectedImageSdcard  selectedImage " + selectedImage);
		new BackgroundTaksForCopySelectedImage().execute(selectedImage);
	}

	private class BackgroundTaksForCopySelectedImage extends
			AsyncTask<Bitmap, Void, Void> {
		Ultilities mUltilities = new Ultilities();
		private boolean copyImageSuccess;
		private int ImageIndex;
		private List<NameValuePair> prarameterforUploadChunk;

		private long chunkLength = Constant.ChunkSize;
		private long FILE_SIZE;

		private long totalBytesRead = 0;
		private long bytesRemaining;
		private boolean flagForUploadsuccess = true;
		private String fileTitle;
		private SessionManager mSessionManager = new SessionManager(
				AlubumGridviewAcitivity.this);
		private String userSessionToken;
		private String deviceId;
		private int imageOrder;
		private String imageFlage;
		private String imageChunkUploadImageResponse;
		private UploadImageChunkResponse chunkResponse;
		private ImageDeleteData imageDeleteData;

		private List<NameValuePair> deleteImageParams;
		private String deleteImageResponse;

		@Override
		protected Void doInBackground(Bitmap... params) {
			logDebug("BackgroundTaksForCopySelectedImage  doInBackground ");
			try {
				userSessionToken = mSessionManager.getUserToken();
				deviceId = Ultilities.getDeviceId(AlubumGridviewAcitivity.this);
				Bitmap selectedImage = params[0];
				logDebug("BackgroundTaksForCopySelectedImage  doInBackground selectedImage "
						+ selectedImage);

				SessionManager mSessionManager = new SessionManager(
						AlubumGridviewAcitivity.this);
				ImageIndex = mSessionManager.getImageIndex();
				imageOrder = ImageIndex;
				logDebug("BackgroundTaksForCopySelectedImage  ImageIndex "
						+ ImageIndex);
				File imageFile = null;
				// get the returned data

				Ultilities mUltilities = new Ultilities();
				File appDirectory = mUltilities
						.createAppDirectoy(getResources().getString(
								R.string.appdirectory));
				// logDebug("BackGroundTaskForUserProfile   doInBackground appDirectory "+appDirectory);
				File _picDir = new File(appDirectory, getResources().getString(
						R.string.imagedire));

				// File _picDirPforFilepic = new File(appDirectory,
				// getResources().getString(R.string.imagedirectory));

				String datestr = mUltilities.getCurrentDate();
				datestr = datestr.replaceAll("-", "");

				datestr = datestr.replaceAll(":", "");

				switch (ImageIndex) {

				case 1:
					ImageIndex = ImageIndex - 1;
					imageFile = mUltilities.createFileInSideDirectory(_picDir,
							getResources().getString(R.string.imagefilename)
									+ ImageIndex + datestr + ".jpg");
					mUltilities.copyImageTOSdCard(imageFile, selectedImage);
					break;
				case 2:
					ImageIndex = ImageIndex - 1;
					imageFile = mUltilities.createFileInSideDirectory(_picDir,
							getResources().getString(R.string.imagefilename)
									+ ImageIndex + datestr + ".jpg");
					mUltilities.copyImageTOSdCard(imageFile, selectedImage);
					break;
				case 3:
					ImageIndex = ImageIndex - 1;
					imageFile = mUltilities.createFileInSideDirectory(_picDir,
							getResources().getString(R.string.imagefilename)
									+ ImageIndex + datestr + ".jpg");
					mUltilities.copyImageTOSdCard(imageFile, selectedImage);

					break;
				case 4:
					ImageIndex = ImageIndex - 1;
					imageFile = mUltilities.createFileInSideDirectory(_picDir,
							getResources().getString(R.string.imagefilename)
									+ ImageIndex + datestr + ".jpg");
					mUltilities.copyImageTOSdCard(imageFile, selectedImage);
					break;
				case 5:
					ImageIndex = ImageIndex - 1;
					imageFile = mUltilities.createFileInSideDirectory(_picDir,
							getResources().getString(R.string.imagefilename)
									+ ImageIndex + datestr + ".jpg");
					mUltilities.copyImageTOSdCard(imageFile, selectedImage);
					break;
				case 6:
					ImageIndex = ImageIndex - 1;
					imageFile = mUltilities.createFileInSideDirectory(_picDir,
							getResources().getString(R.string.imagefilename)
									+ ImageIndex + datestr + ".jpg");
					mUltilities.copyImageTOSdCard(imageFile, selectedImage);
					break;
				}
				// / update image in database

				if (imageOrder == 1) {
					imageFlage = "1";
				} else {
					imageFlage = "2";
				}

				// Log.d(TAG,
				// "BackGroundTaskForUploadImage doInBackground  filePath "+filePath);
				// image_byte_arr=utility.convertFiletoByteArray(new
				// File(filePath));

				FileInputStream fin = null;
				try {
					fin = new FileInputStream(imageFile);
				} catch (FileNotFoundException e2) {
					e2.printStackTrace();
				}
				if (imageFile.isFile() && imageFile.length() > 0) {
					FILE_SIZE = imageFile.length();
				}
				logDebug("BackGroundTaskForUploadImage doInBackground  FILE_SIZE "
						+ FILE_SIZE);
				// Log.d(TAG,
				// "BackGroundTaskForUploadImage doInBackground  fileTitle "+fileTitle);
				fileTitle = imageFile.getName();
				fileTitle = fileTitle.replaceAll(" ", "_");
				Log.e(TAG, "filename : " + fileTitle);
				// Log.d(TAG,
				// "BackGroundTaskForUploadImage doInBackground  fileTitle "+fileTitle);
				// Log.d(TAG,
				// "BackGroundTaskForUploadImage doInBackground  fileTitle  appendTitel "+appendTitel);
				// Log.d(TAG,
				// "BackGroundTaskForUploadImage doInBackground  fileTitle  userId "+userId);

				while (totalBytesRead < FILE_SIZE) {
					if (isCancelled()) {
						break;
					}

					bytesRemaining = FILE_SIZE - totalBytesRead;

					Log.e(TAG, "FILE_SIZE : " + FILE_SIZE);
					Log.e(TAG, "bytesRemaining : " + bytesRemaining);

					if (bytesRemaining < chunkLength) // Remaining Data Part is
														// Smaller Than
														// CHUNK_SIZE //
														// CHUNK_SIZE is
														// assigned to remain
														// volume
					{
						chunkLength = bytesRemaining;
					}

					byte fileContent[] = new byte[(int) chunkLength];
					try {
						fin.read(fileContent, 0, (int) chunkLength);
						Log.e(TAG, "fin.read ");
					} catch (FileNotFoundException e1) {
						Log.e(TAG, "FileNotFoundException : ");
						e1.printStackTrace();
					} catch (IOException e) {
						Log.e(TAG, "IOException : ");
						e.printStackTrace();
					}
					Log.e(TAG, "nxt ");
					String imageStr = Base64.encodeBytes(fileContent);
					// byte[] encoded = Base64.encodeBase64(fileContent);
					String encodedString = imageStr; // new String(encoded);
					Log.e(TAG, "encodedString : " + encodedString);

					// for actual
					String[] paramsUpload = { userSessionToken, deviceId,
							encodedString, fileTitle, imageFlage };

					for (int i = 0; i < paramsUpload.length - 1; i++) {
						Log.e(TAG, "(" + i + ")" + paramsUpload[i]);
					}

					totalBytesRead = totalBytesRead + chunkLength;
					prarameterforUploadChunk = mUltilities
							.getUploadChunkParameter(paramsUpload);
					// mProgress=(int)((totalBytesRead*100)/FILE_SIZE);
					// publishProgress(mProgress);

					// Log.i(TAG,
					// "BackGroundTaskForUploadImage doInBackground totalBytesRead "+totalBytesRead);
					try {
						// for actual
						for (int i = 0; i < 3; i++) {
							imageChunkUploadImageResponse = mUltilities
									.makeHttpRequest(Constant.uploadChunk_url,
											Constant.methodeName,
											prarameterforUploadChunk);
							logDebug("BackGroundTaskForUploadImage imageChunkUploadImageResponse "
									+ imageChunkUploadImageResponse);
							Gson gson = new Gson();

							chunkResponse = gson.fromJson(
									imageChunkUploadImageResponse,
									UploadImageChunkResponse.class);

							if (chunkResponse != null
									&& chunkResponse.getErrNum() == 18
									&& chunkResponse.getErrFlag() == 0) {
								// uploade success no need to retry
								flagForUploadsuccess = true;
								break;
							} else if (chunkResponse != null
									&& chunkResponse.getErrNum() == 1
									&& chunkResponse.getErrFlag() == 1) {
								// Mandatory field missing
								flagForUploadsuccess = false;
								break;
							} else if (chunkResponse != null
									&& chunkResponse.getErrNum() == 9
									&& chunkResponse.getErrFlag() == 1) {
								// Session token expired, please login
								// need to update token
								flagForUploadsuccess = false;
								break;

							} else if (chunkResponse != null
									&& chunkResponse.getErrNum() == 31
									&& chunkResponse.getErrFlag() == 1) {
								// Invalid token, please login or register

								// need to login usere again
								flagForUploadsuccess = false;
								break;
							} else if (chunkResponse != null
									&& chunkResponse.getErrNum() == 17
									&& chunkResponse.getErrFlag() == 1) {
								// Chunk size is larger than 512 kb.
								// need to login usere again
								flagForUploadsuccess = false;
								break;

							} else {
								// retry
								flagForUploadsuccess = false;
							}
						}
						if (flagForUploadsuccess) {
							// flagForUploadsuccess=true;

						} else {
							flagForUploadsuccess = false;
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
						// Log.i(TAG,
						// "BackGroundTaskForUploadImage doInBackground IOException "+e);
						break;
					}
				}
				try {
					fin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (flagForUploadsuccess) {
					// / image upload sucess fully now first delete image from
					// server as well from data base
					// fires get image url

					DatabaseHandler mDatabaseHandler = new DatabaseHandler(
							AlubumGridviewAcitivity.this);
					String userFacebookid = mSessionManager.getFacebookId();
					int imageorder = imageOrder;
					String sdcardpath = imageFile.getAbsolutePath();
					String imageUrl = chunkResponse.getPicURL();
					ImageDetail imageDetail = new ImageDetail();
					imageDetail.setUserFacebookid(userFacebookid);
					imageDetail.setImageOrder(imageorder);
					imageDetail.setImageUrl(imageUrl);
					imageDetail.setSdcardpath(sdcardpath);
					String[] previousImageUrl = null;
					previousImageUrl = mDatabaseHandler
							.updateOrInsertImagepath(imageDetail);
					if (imageOrder != 1) {
						if (previousImageUrl != null
								&& previousImageUrl.length > 0) {

							if (!previousImageUrl[0].equals("imageNotFount")) {
								// delete image using url

								String[] deleteParams = { userSessionToken,
										deviceId, previousImageUrl[0],
										imageFlage };
								deleteImageParams = mUltilities
										.getDeleteParameter(deleteParams);
								deleteImageResponse = mUltilities
										.makeHttpRequest(
												Constant.imagedelete_url,
												Constant.methodeName,
												deleteImageParams);
								logDebug("BackGroundTaskForDeleteImage  doInBackground deleteImageResponse "
										+ deleteImageResponse);
								Gson gson = new Gson();
								imageDeleteData = gson.fromJson(
										deleteImageResponse,
										ImageDeleteData.class);
								if (imageDeleteData != null
										&& imageDeleteData.getErrNum() == 23
										&& imageDeleteData.getErrFlag() == 0) {
									// Image deleted successfully
									// File mFile=new File(previousImageUrl[1]);
									// mFile.delete();

								} else if (imageDeleteData != null
										&& imageDeleteData.getErrNum() == 31
										&& imageDeleteData.getErrFlag() == 1) {
									// Invalid token, please login or register

								} else if (imageDeleteData != null
										&& imageDeleteData.getErrNum() == 24
										&& imageDeleteData.getErrFlag() == 1) {
									// Image not found

								} else if (imageDeleteData != null
										&& imageDeleteData.getErrNum() == 25
										&& imageDeleteData.getErrFlag() == 1) {
									// Error in deleting the image
								}
							} else {
								// no need to delete iamage url
							}
						}

					} else {

						// no need to delete iamage url
					}
				}

				copyImageSuccess = true;

			} catch (Exception e) {
				logError("BackgroundTaksForCopySelectedImage  doInBackground  Exception "
						+ e);
				copyImageSuccess = false;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			logDebug("BackgroundTaksForCopySelectedImage  onPostExecute flagForUploadsuccess"
					+ flagForUploadsuccess);
			if (mdialog != null) {
				mdialog.dismiss();
			}
			if (flagForUploadsuccess) {
				Intent mIntent = new Intent(AlubumGridviewAcitivity.this,
						EditeProfileActivity.class);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				SessionManager mSessionManager = new SessionManager(
						AlubumGridviewAcitivity.this);
				if (imageOrder == 1) {
					mSessionManager.setIsProfileImageChanged(true);
				} else {
					mSessionManager.setIsProfileImageChanged(false);
				}
				mSessionManager.setIsImageChange(true);
				startActivity(mIntent);
				finish();
				// uploadImageUrl(ImageIndex+1);
			} else {

			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			mdialog = mUltilities
					.GetProcessDialog(AlubumGridviewAcitivity.this);
			mdialog.setCancelable(false);
			mdialog.setTitle("Uploading image on server");
			mdialog.setMessage("Please wait...");
			mdialog.show();

		}

	}

	// update Image Url on server
	private void uploadImageUrl(int ImageIndex) {
		logDebug("uploadImageUrl  ImageIndex " + ImageIndex);
		String imageUrl = gridViewlist.get(selectedImageIndex).getPicUrl();
		SessionManager mSessionManager = new SessionManager(this);
		String deviceid;
		String sessionToken;
		deviceid = /* "defoutlfortestin" */Ultilities.getDeviceId(this);
		sessionToken = mSessionManager.getUserToken();
		String[] params = { sessionToken, deviceid, imageUrl, "" + ImageIndex };
		new BackGroundTasForUploadSelectedImage().execute(params);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}

	public class ImageAdapter extends ArrayAdapter<GridViewData> {
		private LayoutInflater mInflater;
		// private RelativeLayout.LayoutParams mLayoutParams;
		// RequestQueue mRequestQueue =
		// Volley.newRequestQueue(AlubumGridviewAcitivity.this);
		// private ImageLoader imageLoader = new ImageLoader(mRequestQueue, new
		// BitmapLruCache(BitmapLruCache.getDefaultLruCacheSize()));
		private Ultilities ultilities = new Ultilities();

		public ImageAdapter(Activity activity, ArrayList<GridViewData> list) {
			super(activity, R.layout.gridviewitem, list);
			// int[] size = ultilities
			// .getImageHeightAndWidthForAlubumGridView(activity);
			// mLayoutParams = ultilities
			// .getRelativelayoutParams(size[1], size[0]);
			// mLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
			// mLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
			this.mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return super.getCount();
		}

		@Override
		public GridViewData getItem(int position) {
			return super.getItem(position);
		}

		@Override
		public long getItemId(int position) {
			return super.getItemId(position);
		}

		@Override
		public void remove(GridViewData object) {
			super.remove(object);
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			System.out.println("browsing in getview");
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.gridviewitem, null);
				holder.imageview = (ImageView) convertView
						.findViewById(R.id.gridviewthumbImage);
				holder.gridviewImageParentlayout = (RelativeLayout) convertView
						.findViewById(R.id.mgridviewImageParentlayout);
				holder.imageview.setBackgroundColor(Color.rgb(68, 68, 68));
				// holder.imageview.setLayoutParams(mLayoutParams);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.imageview.setId(position);
			// holder.imageview.setImageResource(R.drawable.multi_user_icon);

			holder.imageview.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					try {
						File mFile = getItem(position).getFilePath();
						Uri mUri = Uri.fromFile(new File(mFile
								.getAbsolutePath()));
						performCrop(mUri, position);
					} catch (Exception e) {
						Toast.makeText(AlubumGridviewAcitivity.this,
								"Please wait", Toast.LENGTH_SHORT).show();
					}

				}
			});

			Picasso.with(AlubumGridviewAcitivity.this) //
					.load(getItem(position).getPicUrl())
					//
					/* .placeholder(R.drawable.placeholder) *///
					/* .error(R.drawable.error) *///
					// .resize(ultilities
					// .getImageHeightAndWidthForAlubumGridView(AlubumGridviewAcitivity.this)[1],
					// ultilities
					// .getImageHeightAndWidthForAlubumGridView(AlubumGridviewAcitivity.this)[0])
					//
					.into(holder.imageview);

			// holder.imageview.setImageResource(R.drawable.multi_user_icon);
			// holder.imageview.setImageUrl(getItem(position).getPicUrl(),
			// imageLoader,"AlubuGridview",AlubumGridviewAcitivity.this);
			// imageLoader.get(getItem(position).getPicUrl(),
			// ImageLoader.getImageListener(holder.imageview,R.drawable.multi_user_icon,
			// R.drawable.ic_launcher));
			// holder.imageview.setImageBitmap(getItem(position).getBitmap());

			holder.id = position;
			return convertView;
		}
	}

	class ViewHolder {
		ImageView imageview;
		RelativeLayout gridviewImageParentlayout;
		int id;
	}

	/**
	 * Helper method to carry out crop operation
	 */
	private void performCrop(Uri picUri, int Position) {
		// take care of exceptions
		try {
			selectedImageIndex = Position;
			// call the standard crop action intent (the user device may not
			// support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// indicate image type and Uri
			cropIntent.setDataAndType(picUri, "image/*");
			// set crop properties
			cropIntent.putExtra("crop", "true");
			// indicate aspect of desired crop
			cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);
			// indicate output X and Y
			cropIntent.putExtra("outputX", 256);
			cropIntent.putExtra("outputY", 256);
			// retrieve data on return
			cropIntent.putExtra("return-data", true);
			// start the activity - we handle returning in onActivityResult
			startActivityForResult(cropIntent, PIC_CROP);
		}
		// respond to users whose devices do not support the crop action
		catch (ActivityNotFoundException anfe) {
			// display an error message
			String errorMessage = "Whoops - your device doesn't support the crop action!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private class BackGroundTasForUploadSelectedImage extends
			AsyncTask<String, Void, Void> {

		Ultilities mUltilities = new Ultilities();
		private List<NameValuePair> uploadNameValuePairList;
		private String uploadResponse;
		private boolean uploadImageurlsuccessFully;
		private UploadImage mUploadImage;

		@Override
		protected Void doInBackground(String... params) {
			logDebug("BackGroundTasForUploadSelectedImage   doInBackground  ");
			try {
				String[] uploadParameter = { params[0], params[1], params[2],
						params[3] };
				uploadNameValuePairList = mUltilities
						.getUploadSingleImage(uploadParameter);
				logDebug("BackGroundTasForUploadSelectedImage   doInBackground uploadNameValuePairList "
						+ uploadNameValuePairList);
				uploadResponse = mUltilities.makeHttpRequest(
						Constant.uploadImage_url, Constant.methodeName,
						uploadNameValuePairList);
				logDebug("BackGroundTasForUploadSelectedImage   doInBackground uploadResponse "
						+ uploadResponse);
				Gson gson = new Gson();

				mUploadImage = gson.fromJson(uploadResponse, UploadImage.class);
				if (mUploadImage.getErrNum() == 18
						&& mUploadImage.getErrFlag() == 0) {
					if (Integer.parseInt(params[3]) == 1) {
						if (mUploadImage.getProfFlag() == 1) {
							uploadImageurlsuccessFully = true;
						} else {
							// profile image upload faild
							logError("BackGroundTasForUploadSelectedImage  ProfFlag "
									+ mUploadImage.getProfFlag());
							uploadImageurlsuccessFully = false;
						}
					} else {
						ArrayList<UploadeImageUrl> images = mUploadImage
								.getImages();
						if (images != null && images.size() > 0) {
							UploadeImageUrl uploadeImageUrl = images.get(0);
							if (uploadeImageUrl != null) {
								if (uploadeImageUrl.getFlag() == 1) {
									uploadImageurlsuccessFully = true;
								} else {
									logError("BackGroundTasForUploadSelectedImage  other image upload failde  due to flag"
											+ uploadeImageUrl.getFlag());
									uploadImageurlsuccessFully = false;
								}
							} else {
								logError("BackGroundTasForUploadSelectedImage  other image upload failde ");
								uploadImageurlsuccessFully = false;
							}
						} else {
							logError("BackGroundTasForUploadSelectedImage  other image upload failde ");
							uploadImageurlsuccessFully = false;
						}

					}
				} else {
					uploadImageurlsuccessFully = false;

				}
				if (uploadImageurlsuccessFully) {
					File appDirectory = mUltilities
							.createAppDirectoy(getResources().getString(
									R.string.appdirectory));
					File _picDir = new File(appDirectory, getResources()
							.getString(R.string.alubumpic));
					mUltilities.deleteNon_EmptyDir(_picDir);
				}

			} catch (JsonSyntaxException ex) {
				// Inform then user that the the Json data contains invalid
				// syntax
				logError("BackGroundTasForUploadSelectedImage   doInBackground JsonSyntaxException "
						+ ex);
				uploadImageurlsuccessFully = false;
			} catch (Exception e) {
				// TODO: handle exception
				logError("BackGroundTasForUploadSelectedImage   doInBackground Exception "
						+ e);
				uploadImageurlsuccessFully = false;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			try {
				mdialog.dismiss();
				if (uploadImageurlsuccessFully) {
					// SessionManager mSessionManager=new
					// SessionManager(AlubumGridviewAcitivity.this);
					// mSessionManager.setIsImageSet(true);
					Intent mIntent = new Intent(AlubumGridviewAcitivity.this,
							EditeProfileActivity.class);
					mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(mIntent);
					finish();
				} else if (mUploadImage.getErrNum() == 9
						&& mUploadImage.getErrFlag() == 1) {
					// session expired need to update token

				} else if (mUploadImage.getErrNum() == 31
						&& mUploadImage.getErrFlag() == 1) {
					// session expired need to update token
					// logDebug("onPostExecute "+mUploadImage.getErrNum()+"  "+mUploadImage.getErrFlag());
					ErrorMessageInvalidSessionToken("Alert",
							mUploadImage.getErrorMassage());

				} else {
					ErrorMessage("Alrte",
							"faild to update image please try again ");
				}

			} catch (Exception e2) {
				// TODO: handle exception
				logDebug("BackGroundTaskForDownload   onPostExecute  Exception"
						+ e2);
			}

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			// mdialog=mUltilities.GetProcessDialog(AlubumGridviewAcitivity.this);
			// mdialog.setCancelable(false);
			// mdialog.show();

		}

	}

	private void getUpdateSessionToken() {
		new BackGroundTaskForUpdateToken().execute();
	}

	private class BackGroundTaskForUpdateToken extends
			AsyncTask<String, Void, Void> {
		private Ultilities mUltilities = new Ultilities();
		private String sessionToken;
		private SessionManager mSessionManager = new SessionManager(
				AlubumGridviewAcitivity.this);
		private String updatedTokenResponse;
		private List<NameValuePair> updateTokenparameterlist;
		private String deviceid;
		private String facebookId;
		private UpdateSessionData mUpdateSessionData;

		@Override
		protected Void doInBackground(String... params) {
			try {
				deviceid = /* "defoutlfortestin" */Ultilities
						.getDeviceId(AlubumGridviewAcitivity.this);
				sessionToken = mSessionManager.getUserToken();
				facebookId = mSessionManager.getFacebookId();
				String[] updateTokenParameter = { sessionToken, deviceid,
						facebookId };
				updateTokenparameterlist = mUltilities
						.getUpdateTokeParameter(updateTokenParameter);
				logDebug("BackGroundTaskForUpdateToken   doInBackground findMathchResponse "
						+ updatedTokenResponse);
				updatedTokenResponse = mUltilities.makeHttpRequest(
						Constant.UpdateToken_url, Constant.methodeName,
						updateTokenparameterlist);
				logDebug("BackGroundTaskForUpdateToken   doInBackground findMathchResponse "
						+ updatedTokenResponse);
				Gson gson = new Gson();

				mUpdateSessionData = gson.fromJson(updatedTokenResponse,
						UpdateSessionData.class);
			} catch (Exception e) {
				// TODO: handle exception
			}
			// TODO Auto-generated method stub

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				if (mUpdateSessionData.getErrNum() == 59
						&& mUpdateSessionData.getErrFlag() == 0) {

					mSessionManager.setUserToken(mUpdateSessionData.getToken());

				} else if (mUpdateSessionData.getErrNum() == 60
						&& mUpdateSessionData.getErrFlag() == 1) {
					// ErrorMessage("Alert",mUpdateSessionData.getErrMsg());
				}

			} catch (Exception e) {
				// TODO: handle exception

			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

	}

	private void ErrorMessage(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						Intent mIntent = new Intent(
								AlubumGridviewAcitivity.this,
								EditeProfileActivity.class);
						mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(mIntent);
						finish();

					}
				});

		AlertDialog alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}

	private void ErrorMessageInvalidSessionToken(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);

		builder.setPositiveButton(
				getResources().getString(R.string.okbuttontext),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							Session session = Session.getActiveSession();
							logDebug("ErrorMessageInvalidSessionToken  session "
									+ session);
							if (!session.isClosed()) {
								logDebug("ErrorMessageInvalidSessionToken  session not close need to cleaar  "
										+ session.getState());
								session.closeAndClearTokenInformation();

							} else {
								// nothing session already close no need to
								// clear
							}

							SessionManager mSessionManager = new SessionManager(
									AlubumGridviewAcitivity.this);
							mSessionManager.logoutUser();
							Intent intent = new Intent(
									AlubumGridviewAcitivity.this,
									LoginUsingFacebook.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
							finish();
						} catch (Exception e) {
							// TODO: handle exception
							logError("ErrorMessageInvalidSessionToken  Exception "
									+ e);
						}

						dialog.dismiss();

					}
				});

		/*
		 * builder.setNegativeButton("Cancel", new
		 * DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) { //
		 * TODO Auto-generated method stub dialog.dismiss(); Intent mIntent=new
		 * Intent(AlubumGridviewAcitivity.this, EditeProfileActivity.class);
		 * mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 * startActivity(mIntent); finish();
		 * 
		 * 
		 * } });
		 */

		AlertDialog alert = builder.create();
		alert.setCancelable(false);
		alert.show();
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
			// getUserAllAlubum();

		}
	}

	private void ErrorMessage() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle(getResources().getString(R.string.alert));
		builder.setMessage(getResources().getString(R.string.retriedmessage));

		builder.setPositiveButton(
				getResources().getString(R.string.okbuttontext),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Session mSession = Session.getActiveSession();
						getAlubumPick(alubumid, mSession);

						dialog.dismiss();
						finish();
					}
				});

		AlertDialog alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}

}
