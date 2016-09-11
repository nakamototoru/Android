package buyer.app.hidezo.com.buyer;

import java.io.IOException;

/**
 * Created by dezami on 2016/09/11.
 */
public interface MyOkhttpCallbacks {

    public void myOkhttpCallbackComplete(String response);
    public void myOkhttpCallbackError(String error);

}
