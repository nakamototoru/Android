package com.hidezo.app.buyer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by dezami on 2016/09/13.
 *
 */
class HDZApiResponseFriend extends HDZApiResponse {

    ArrayList<HDZFriendInfo> friendInfoList = new ArrayList<>();

    @Override
    public boolean parseJson(final String strjson) {
        boolean isSuccess = super.parseJson(strjson);
        if (isSuccess) {
            try {
                JSONObject json = new JSONObject(strjson);

                if ( !json.isNull("friendList") ) {
                    JSONArray friendList = json.getJSONArray("friendList");
                    if (friendList.length() > 0) {
                        for (int i = 0; i < friendList.length(); i++) {
                            HDZFriendInfo info = new HDZFriendInfo();
                            info.id = friendList.getJSONObject(i).getString("id");
                            info.name = friendList.getJSONObject(i).getString("name");
                            info.address = friendList.getJSONObject(i).getString("address");
                            info.mail_addr = friendList.getJSONObject(i).getString("mail_addr");
                            info.minister = friendList.getJSONObject(i).getString("minister");
                            info.mobile = friendList.getJSONObject(i).getString("mobile");
                            info.tel = friendList.getJSONObject(i).getString("tel");

                            friendInfoList.add(info);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }
}
