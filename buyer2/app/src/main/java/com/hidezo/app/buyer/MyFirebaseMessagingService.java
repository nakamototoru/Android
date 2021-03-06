/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hidezo.app.buyer;

//import android.app.Notification;
//import android.app.ActionBar;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
//import android.support.v4.app.NotificationManagerCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

//import java.util.Map;

/**
 * 通知時アクション
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "##FirebaseMsgService";

    /**
     * フォアグランド時に呼ばれる
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // Handle FCM messages.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "From: " + remoteMessage.getFrom());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() == null) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "remoteMessageGetNotification = NULL");
            }
            return;
        }
        else {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            }
        }

        final String messageBody = remoteMessage.getNotification().getBody();
        int type_flag = 0;

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            }

            // プッシュメッセージのdataに含めた値を取得
            final Map<String, String> data = remoteMessage.getData();
            if (data != null && data.size() > 0) {
                final String typeStr = data.get("type");
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "TYPE = " + typeStr);
                }

                type_flag = Integer.parseInt(typeStr);
            }
        }
        else {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "remoteMessageDataSize = 0");
            }
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        sendNotification(messageBody,type_flag);
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(final String messageBody, final int type_flag) {

//        Log.d(TAG, messageBody);
        final AppGlobals globals = (AppGlobals) this.getApplication();

        // タップ時に呼ばれるIntentを生成
        final Intent intent;
        // ログインチェック
        if (globals.getLoginState()) {
            // ログイン時
            switch (type_flag) {
                case 1:
                    // メッセージ
                    intent = new Intent(this, ActivityOrderes.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    break;
                default:
                    // 動的商品
                    intent = new Intent(this, ActivitySuppliers.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    break;
            }
        }
        else {
            // 非ログイン時
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /*Request code*/, intent, PendingIntent.FLAG_ONE_SHOT);

        // 通知アクション
        final Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_notification)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

//        /* Add Big View Specific Configuration */
//        final NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
//
//        // Sets a title for the Inbox style big view
//        inboxStyle.setBigContentTitle(messageBody);
//
//        inboxStyle.addLine("追加ライン");
//
//        notificationBuilder.setStyle(inboxStyle);
        // ------------------------------------------

        // 通知実行
        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /*ID of notification*/, notificationBuilder.build());

    }
}
