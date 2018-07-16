package com.ritvi.kaajneeti.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ritvi.kaajneeti.R;
import com.ritvi.kaajneeti.Util.StringUtils;
import com.ritvi.kaajneeti.Util.TagUtils;
import com.ritvi.kaajneeti.activity.home.HomeActivity;

import org.json.JSONObject;

/**
 * Created by sunil on 18-08-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        try {
            String notification = remoteMessage.getData().toString();
            String success = remoteMessage.getData().get("success");
            String result = remoteMessage.getData().get("result");
            String title = remoteMessage.getData().get("title");
            String description = remoteMessage.getData().get("description");
            String type = remoteMessage.getData().get("type");

            Log.d(TagUtils.getTag(), "notification:-" + notification);
            Log.d(TagUtils.getTag(), "success:-" + success);
            Log.d(TagUtils.getTag(), "result:-" + result);
            Log.d(TagUtils.getTag(), "title:-" + title);
            Log.d(TagUtils.getTag(), "description:-" + description);
            Log.d(TagUtils.getTag(), "type:-" + type);

            checkType(type, result);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
            try {
                Log.d(TAG, "From: " + remoteMessage.getFrom());
                Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
            } catch (Exception e1) {
                Log.d(TAG, e1.toString());
            }
        }
//        }
    }


    public void checkType(String type, String result) {
        try {
            Log.d(TagUtils.getTag(), "type:-" + type);
            sendPostNotification("Kaajneeti",type, result);
            updateChatActivity(getApplicationContext(),type,result);
//            switch (type) {
//                case "post-generated":
//                    sendPostNotification("Kaajneeti",type, result);
//                    break;
//                case "complaint-tagged":
//                    sendPostNotification("Kaajneeti",type, result);
//                    break;
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendPostNotification(String title,String type, String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);

            String message = jsonObject.optString("FromUserProfileName") + " " + jsonObject.optString("NotificationMessage");

            Log.d(TagUtils.getTag(),"notification message:-"+message);
            Log.d(TagUtils.getTag(),"notification type:-"+type);

            Intent intent = new Intent(this, HomeActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("type", type);
            intent.putExtra("data", data);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateChatActivity(Context context,String type, String message) {
        Intent intent = new Intent(StringUtils.UPDATE_NOTIFICATION);

        //put whatever data you want to send, if any
        intent.putExtra("type", type);
        intent.putExtra("data", message);

        //send broadcast
        context.sendBroadcast(intent);
    }


}