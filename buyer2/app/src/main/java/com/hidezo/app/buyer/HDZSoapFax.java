package com.hidezo.app.buyer;

import android.os.AsyncTask;
//import android.os.Bundle;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpsServiceConnectionSE;
import org.ksoap2.transport.HttpsTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by dezamisystem2 on 2017/01/28.
 *
 */
public class HDZSoapFax extends AsyncTask<Object,Object,Object> {

    /*
    private static final String faximoAccountId = "nakamoto"
	private static final String faximoPassword = "ab123456"
     */

//    private static final String SOAP_ACTION = "http://tempuri.org/GetInteger2";
//    private static final String METHOD_NAME = "GetInteger2";
    private static final String NAMESPACE = "https://soap.faximo.jp/soapapi-sv/SoapServFS_1200.php";
    private static final String URL = "https://soap.faximo.jp/soapapi-sv/FSBinding1220.wsdl";

    private KeyStore keyStore;

    @Override
    protected void onPostExecute(final Object result) {
        super.onPostExecute(result);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(final Object... arg0) {
        return null;
    }

    public void ConnectionWithSelfSignedCertificate(final KeyStore keyStore) {
        this.keyStore = keyStore;
    }

    public String getSendList() throws IOException, XmlPullParserException,KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        final String METHOD_NAME = "getSendList";

        final SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        final PropertyInfo pi = new PropertyInfo();
        pi.setName("i");
        pi.setValue(123);
        request.addProperty(pi);

        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        final URI uri = URI.create(URL);
        final HttpsTransportSE httpsTransport = new HttpsTransportSE(uri.getHost(), uri.getPort(), uri.getPath(), 10000);
        ((HttpsServiceConnectionSE)httpsTransport.getServiceConnection()).setSSLSocketFactory(getSSLSocketFactory());

        final String SOAP_ACTION = NAMESPACE + "/" + METHOD_NAME;
        httpsTransport.call(SOAP_ACTION, envelope);

        // 結果取得
        final SoapPrimitive result = (SoapPrimitive)envelope.getResponse();

//        return Integer.parseInt(result.toString());

        return result.toString();
    }


    /**
     * （編集不可）
     * @return SocketFactory Object
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    private SSLSocketFactory getSSLSocketFactory() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);
        final SSLContext context = SSLContext.getInstance("SSL");
        context.init(null, tmf.getTrustManagers(), null);
        return context.getSocketFactory();
    }
}
