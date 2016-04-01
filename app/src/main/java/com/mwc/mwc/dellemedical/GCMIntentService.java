package com.mwc.mwc.dellemedical;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";

	private Controller aController = null;
	private static final int NOTIFY_ME_ID = 5476;
	public static int count = 0;

	public GCMIntentService() {

		// Call extended class Constructor GCMBaseIntentService
		super(GCMConfig.GOOGLE_SENDER_ID);
	}

	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) {

		try {
			// Get Global Controller Class object (see application tag in
			// AndroidManifest.xml)
			if (aController == null)
				aController = (Controller) getApplicationContext();

			LoginActivity.GCMRegister_Id = registrationId;

			Log.i(TAG, "Device registered: regId = " + registrationId);
			aController.displayMessageOnScreen(context,
					"Your device registred with GCM");
			Log.d("NAME", "**********USER NAME");
			aController.register(context, "", "", registrationId);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * Method called on device unregistred
	 * */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		if (aController == null)
			aController = (Controller) getApplicationContext();
		Log.i(TAG, "Device unregistered");
		aController.displayMessageOnScreen(context,
				getString(R.string.gcm_unregistered));
		aController.unregister(context, registrationId);
	}

	/**
	 * Method called on Receiving a new message from GCM server
	 * */
	@Override
	protected void onMessage(Context context, Intent intent) {

		if (aController == null)
			aController = (Controller) getApplicationContext();

		Log.i(TAG, "Received message");
		String message = intent.getExtras().getString("message"); // *****************************
																	// GCM
																	// INTENT

		aController.displayMessageOnScreen(context, message);
		// notifies user
		generateNotification(context, message);
	}

	/**
	 * Method called on receiving a deleted message
	 * */
	@Override
	protected void onDeletedMessages(Context context, int total) {

		if (aController == null)
			aController = (Controller) getApplicationContext();

		Log.i(TAG, "Received deleted messages notification");
		String message = getString(R.string.gcm_deleted, total);
		aController.displayMessageOnScreen(context, message);
		// notifies user
		generateNotification(context, message);
	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId) {

		if (aController == null)
			aController = (Controller) getApplicationContext();

		Log.i(TAG, "Received error: " + errorId);
		aController.displayMessageOnScreen(context,
				getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {

		try {
			if (aController == null)
				aController = (Controller) getApplicationContext();

			// log message
			Log.i(TAG, "Received recoverable error: " + errorId);
			aController.displayMessageOnScreen(context,
					getString(R.string.gcm_recoverable_error, errorId));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Create a notification to inform the user that server has sent a message.
	 */
	private static void generateNotification(Context context, String message) {

		try {

			String title = context.getString(R.string.app_name);
			SharedPreferences sharedPreferences;
			sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(context
							.getApplicationContext());
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString("NOTIFICATIONSTRING", "TRUE");
			editor.commit();

			// //*******************************************************************************
			int icon = R.drawable.logout;
			 
			int mNotificationId = 001;
			 
			PendingIntent resultPendingIntent =
			        PendingIntent.getActivity(
			                context,
			                0,
			                new Intent(
			    				context, HomeScreenActivity.class),       //------------------Activity Change
			                PendingIntent.FLAG_CANCEL_CURRENT
			        );
			 
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					context);
			Notification notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
			        .setAutoCancel(true)
			        .setContentTitle(title)
			        .setNumber(++count)
			        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
			        .setSubText("\n "+count+" new messages\n")
			        .setContentIntent(resultPendingIntent)
			        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
			        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logout))
			        .setContentText(message).build();
			 
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.notify(mNotificationId, notification);
			// //*******************************************************************************
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
