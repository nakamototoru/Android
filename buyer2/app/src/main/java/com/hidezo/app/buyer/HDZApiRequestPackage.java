package com.hidezo.app.buyer;

/**
 *
 */
public class HDZApiRequestPackage {

    public static class Friend {

        public void begin(final String id,final String uuid, MyOkhttpCallbacks callbacks) {

            HDZApiRequest request = new HDZApiRequest();
            request.putKeyAndValue("id", id);
            request.putKeyAndValue("uuid", uuid);

            request.beginRequest("friend",callbacks);
        }
    }

    public static class Item {

        public void begin(final String id,final String uuid,final String supplier_id, MyOkhttpCallbacks callbacks) {

            HDZApiRequest request = new HDZApiRequest();
            request.putKeyAndValue("id", id);
            request.putKeyAndValue("uuid", uuid);
            request.putKeyAndValue("supplier_id", supplier_id);

            request.beginRequest("item",callbacks);
        }
    }
}
