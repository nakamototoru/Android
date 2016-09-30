package com.hidezo.app.buyer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dezami on 2016/09/30.
 *
 */

class HDZApiResponseMessage extends HDZApiResponse {

    /*
    "user_flg": "1",
      "charge": "",
      "message": "テスト送信です。\nこれはテスト。",
      "posted_at": "06/12 PM 3:30",
      "name": "ひでぞう酒店"
     */

    public static class Detail {
        String user_flg = "";
        String charge = "";
        String message = "";
        String posted_at = "";
        String nameOfSender = "";
    }

    public String messageCount;
    public ArrayList<Detail> messageList = new ArrayList<>();
    public ArrayList<String> chargeList = new ArrayList<>();

    @Override
    public boolean parseJson(final String strJson) {
        boolean isSuccess = super.parseJson(strJson);
        if (isSuccess) {
            try {
                JSONObject json = new JSONObject(strJson);

                messageCount = json.getString("messageCount");

                JSONArray json_message_list = json.getJSONArray("messageList");
                for (int i = 0; i < json_message_list.length(); i++) {
                    Detail detail = new Detail();
                    detail.user_flg = json_message_list.getJSONObject(i).getString("user_flg");
                    detail.charge = json_message_list.getJSONObject(i).getString("charge");
                    detail.message = json_message_list.getJSONObject(i).getString("message");
                    detail.posted_at = json_message_list.getJSONObject(i).getString("posted_at");
                    detail.nameOfSender = json_message_list.getJSONObject(i).getString("name");
                    messageList.add(detail);
                }

                JSONArray json_charge_list = json.getJSONArray("chargeList");
                for (int i = 0; i < json_charge_list.length(); i++) {
                    String charge = json_charge_list.getString(i);
                    chargeList.add(charge);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }
}
