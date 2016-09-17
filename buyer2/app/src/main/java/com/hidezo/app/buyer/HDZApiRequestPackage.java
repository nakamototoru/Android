package com.hidezo.app.buyer;

/**
 * Created by dezami on 2016/09/13.
 *
 */
public class HDZApiRequestPackage {

    public static class LoginCheck {

        public void begin(final String id,final String uuid, HDZClient.HDZCallbacks callbacks) {

            HDZApiRequest request = new HDZApiRequest();
            request.putKeyAndValue("id", id);
            request.putKeyAndValue("uuid", uuid);

            request.beginRequest("login_check/store",callbacks);
        }
    }

    public static class Login {

        public void begin(final String id,final String uuid,final String password, HDZClient.HDZCallbacks callbacks) {
            HDZApiRequest request = new HDZApiRequest();
            request.putKeyAndValue("id", id);
            request.putKeyAndValue("uuid", uuid);
            request.putKeyAndValue("password", password);

            request.beginRequest("login/store",callbacks);
        }
    }

    public static class Friend {

        public void begin(final String id,final String uuid, HDZClient.HDZCallbacks callbacks) {

            HDZApiRequest request = new HDZApiRequest();
            request.putKeyAndValue("id", id);
            request.putKeyAndValue("uuid", uuid);

            request.beginRequest("store/friend",callbacks);
        }
    }

    public static class Item {

        public void begin(final String id,final String uuid,final String supplier_id, HDZClient.HDZCallbacks callbacks) {

            HDZApiRequest request = new HDZApiRequest();
            request.putKeyAndValue("id", id);
            request.putKeyAndValue("uuid", uuid);
            request.putKeyAndValue("supplier_id", supplier_id);

            request.beginRequest("store/item",callbacks);
        }
    }
}
