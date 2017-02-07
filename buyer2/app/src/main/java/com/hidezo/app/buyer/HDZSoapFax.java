package com.hidezo.app.buyer;

import android.os.AsyncTask;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
//import java.text.DecimalFormat;
//import java.text.NumberFormat;
//import java.text.ParseException;
//import java.util.Locale;

/**
 * Created by dezamisystem2 on 2017/02/03.
 *
 */
class HDZSoapFax {

    private static final String TAG = "#HDZSoapFax";

    private static final String ACTION_NAME_GetSendList = "getSendList";
    private static final String ACTION_NAME_DoSendRequest = "doSendRequest";

    // MARK: アクセスアドレス
    private static final String theUrlString = "https://soap.faximo.jp/soapapi-sv/SoapServFS_1200.php";
    // MARK: アカウント
    private static final String faximoAccountId = "nakamoto";
    private static final String faximoPassword = "ab123456";
    // MARK: XMLパーツ
    private static final String soapXmlFirst = "<?xml version='1.0' encoding='UTF-8'?><SOAP-ENV:Envelope xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/' xmlns:ns1='urn:faximoAPI'>";
    private static final String soapXmlHeader1 = "<SOAP-ENV:Header><ns1:SOAPHeader><ns1:processkey>process_key</ns1:processkey><ns1:Attestation><ns1:userid>";
    private static final String soapXmlHeader2 = "</ns1:userid><ns1:password>";
    private static final String soapXmlHeader3 = "</ns1:password></ns1:Attestation></ns1:SOAPHeader></SOAP-ENV:Header>";


    /**
     * 非同期アクセスクラス
     */
    private static class SoapTask extends AsyncTask<Void,Void,String> {

        String soapAction;
        String soapXml;
        private CustomAppCompatActivity activity;

        /**
         * コンストラクタ
         * @param soapAction コマンド
         * @param soapXml xml body
         */
        SoapTask(final String soapAction, final String soapXml, final CustomAppCompatActivity activity) {
            this.soapAction = soapAction;
            this.soapXml = soapXml;
            this.activity = activity;
        }

        /**
         *
         * @param params ???
         * @return response xml string
         */
        @Override
        protected String doInBackground(final Void... params) {

            activity.openProgressDialog("注文書送信中","しばらくお待ち下さい。");

            // XML
            String soapMessageFinal = soapXmlFirst;
            // Header
            soapMessageFinal += soapXmlHeader1;
            soapMessageFinal += faximoAccountId;
            soapMessageFinal += soapXmlHeader2;
            soapMessageFinal += faximoPassword;
            soapMessageFinal += soapXmlHeader3;
            // Body
            soapMessageFinal += soapXml;

            try {
                final byte[] requestData = soapMessageFinal.getBytes("UTF-8");
                final URL url = new URL(theUrlString);

                final HttpURLConnection connection;
                connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestProperty("Accept-Charset", "UTF-8");
                // connection.setRequestProperty("Accept-Encoding","gzip,deflate");
//        connection.setRequestProperty("Content-Type", "text/xml; UTF-8");
//        connection.setRequestProperty("SOAPAction", soapAction);
//        connection.setRequestProperty("User-Agent", "android");
//        connection.setRequestProperty("Host", "base_urlforwebservices like - xyz.net");
                connection.setRequestProperty("Content-Type","text/xml; charset=utf-8");
                connection.setRequestProperty("Content-Type","text/html; charset=utf-8");
                connection.setRequestProperty("Content-Length",String.valueOf(requestData.length));
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);

                // HTTPBody
                final OutputStream os = connection.getOutputStream();
                os.write(requestData, 0, requestData.length);
                os.flush();
                os.close();

                final InputStream is = connection.getInputStream();

                // To String
                final BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                String responseXml = "";
                while((line = rd.readLine()) != null) {
                    responseXml += line;
                    responseXml += '\r';
                }
                rd.close();

//                Log.d(TAG, "Response:" + responseXml);
//                parseXml(responseXml);
                return responseXml;

            } catch (final UnsupportedEncodingException e) {
                Log.d(TAG, "UnsupportedEncodingException");
                e.printStackTrace();
            } catch (final MalformedURLException e) {
                Log.d(TAG, "MalformedURLException");
                e.printStackTrace();
            } catch (final IOException e) {
                Log.d(TAG, "IOException");
                e.printStackTrace();
            }

            return "";
        }

        /**
         *
         * @param responseXml response xml string
         */
        @Override
        protected void onPostExecute(final String responseXml) {
            Log.d(TAG, "onPostExecute");

            activity.closeProgressDialog();

            try {
                final boolean isSuccess = parseXml(responseXml);

                if (isSuccess) {
                    Log.d(TAG,"isSuccess = true");
                }
                else {
                    Log.d(TAG,"isSuccess = false");
                }

                if (soapAction.equals(ACTION_NAME_DoSendRequest)) {
                    Log.d(TAG,"getSendList");
                }
                else if (soapAction.equals(ACTION_NAME_GetSendList)) {
                    Log.d(TAG,"doSendRequest");
                }

            } catch (final XmlPullParserException e) {
                Log.d(TAG, "XmlPullParserException");
                e.printStackTrace();
            } catch (final IOException e) {
                Log.d(TAG, "IOException");
                e.printStackTrace();
            }
        }
    }

    /**
     * 送信履歴取得
     */
    static void getSendList(final CustomAppCompatActivity activity) {
        // XML
        String soapXml = "";
        // Body
        soapXml += "<SOAP-ENV:Body><ns1:getSendList><FAXSendListRequest><ns1:SendListSearchCondition>";
        soapXml += "<ns1:userkey>userKey</ns1:userkey>";
        soapXml += "</ns1:SendListSearchCondition></FAXSendListRequest></ns1:getSendList></SOAP-ENV:Body></SOAP-ENV:Envelope>";
        // END of XML

        new HDZSoapFax.SoapTask(ACTION_NAME_GetSendList,soapXml,activity).execute();
    }

    /**
     * FAX送信
     * @param base64Binary データ文字列
     * @param faxNumber FAX番号
     * @param subject 件名
     */
    static void doSendRequest(final String base64Binary, final String faxNumber, final String subject, final CustomAppCompatActivity activity) {

//        final Locale theLocale = Locale.getDefault();
//        final NumberFormat numberFormat = DecimalFormat.getInstance(theLocale);
//        final Number theNumber;
//        final String[] numbers = telNumber.split("-",0);
//        String faxNumber = "";
//        for (final String part: numbers) {
//            faxNumber += part;
//        }
//        for (int i = 0; i < numbers.length; i++) {
//            faxNumber += numbers[i];
//        }
        Log.d(TAG, "FaxNumber = " + faxNumber);
//        try {

            // XML
            String soapXml = "";
            // Body
            soapXml += "<SOAP-ENV:Body><ns1:doSendRequest><FAXSendRequest><ns1:SendData>";
            soapXml += "<ns1:sendto>";
            soapXml += faxNumber;
            soapXml += "</ns1:sendto>";
            soapXml += "<ns1:subject>";
            soapXml += subject;
            soapXml += "</ns1:subject>";
            soapXml += "<ns1:Attachment>";
            soapXml += "<ns1:attachmentname>testpdf.pdf</ns1:attachmentname>";
            soapXml += "<ns1:attachmentdata>";
            soapXml += base64Binary;
            soapXml += "</ns1:attachmentdata>";
            soapXml += "</ns1:Attachment>";
            soapXml += "</ns1:SendData></FAXSendRequest></ns1:doSendRequest></SOAP-ENV:Body></SOAP-ENV:Envelope>";
            // END of XML

            new HDZSoapFax.SoapTask(ACTION_NAME_DoSendRequest,soapXml,activity).execute();

//        } catch (final ParseException e) {
//            e.printStackTrace();
            // The string value might be either 99.99 or 99,99, depending on Locale.
            // We can deal with this safely, by forcing to be a point for the decimal separator, and then using Double.valueOf ...
            //http://stackoverflow.com/questions/4323599/best-way-to-parsedouble-with-comma-as-decimal-separator
//            String valueWithDot = telNumber.replaceAll("-","");
//
//            try {
//
////                return Double.valueOf(valueWithDot);
//            } catch (NumberFormatException e2)  {
//                // This happens if we're trying (say) to parse a string that isn't a number, as though it were a number!
//                // If this happens, it should only be due to application logic problems.
//                // In this case, the safest thing to do is return 0, having first fired-off a log warning.
//                Log.w("CORE", "Warning: Value is not a number" + telNumber);
////                return 0.0;
//            }
//        }
    }

    /**
     *
     * @param xmlString request xml
     * @return is success
     * @throws XmlPullParserException
     * @throws IOException
     */
    private static boolean parseXml(final String xmlString) throws XmlPullParserException, IOException {

        Log.d(TAG,"parseXml");

        final XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        final XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(xmlString));

        int eventType = parser.getEventType();
        boolean isSuccess = false;
        boolean isTagResult = false;
        while (eventType != XmlPullParser.END_DOCUMENT) {

            if(eventType == XmlPullParser.START_DOCUMENT) {
                Log.d(TAG, "Start document");
            } else if(eventType == XmlPullParser.START_TAG) {
                final String name = parser.getName();
                Log.d(TAG, "Start tag : " + name);

                if (name.equals("ns1:result")) {
                    isTagResult = true;
                }

            } else if(eventType == XmlPullParser.END_TAG) {
                Log.d(TAG, "End tag : " + parser.getName());

                isTagResult = false;

            } else if(eventType == XmlPullParser.TEXT) {
                final String text = parser.getText();
                Log.d(TAG, "Text : " + text);

                if (isTagResult) {
                    final int code = Integer.parseInt(text);
                    if (code == 0) {
                        isSuccess = true;
                    }
                }
            }

            eventType = parser.next();
        }

        return isSuccess;
    }
}
