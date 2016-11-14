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
    public boolean parseJson(final String str_json) {
        final boolean isSuccess = super.parseJson(str_json);
        if (isSuccess) {
            try {
                final JSONObject json = new JSONObject(str_json);

                if ( !json.isNull("friendList") ) {
                    final JSONArray friendList = json.getJSONArray("friendList");
                    if (friendList.length() > 0) {
                        for (int i = 0; i < friendList.length(); i++) {
                            final HDZFriendInfo info = new HDZFriendInfo();
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

            } catch (final JSONException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }
}
