package com.hidezo.app.buyer;

import android.app.Application;
import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.os.AsyncTask;
//import android.os.Environment;
//import android.support.v4.app.ActivityCompat;
//import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.View;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
//import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by dezamisystem2 on 2017/01/31.
 *
 */

class HDZPdfManager extends Application {

    private static final String TAG = "#HDZPdfManager";

    private static final String PDF_FILE_NAME = "hidezo_order_document.pdf";
//    private static final int WIDTH_PAGE = 595;
//    private static final int HEIGHT_PAGE = 842;
    private static final int BYTE_READ_SIZE = 10240;

    protected static File getPdfDir(final Context context) {
        return context.getFilesDir();
    }

    public static class GenerationTask extends AsyncTask<Void,Void,Void> {

        private Context context;
        private PdfDocument pdfDocument = new PdfDocument();
        private CustomAppCompatActivity activity;

        GenerationTask(final View viewContent, final Context context, final CustomAppCompatActivity activity) {
            this.context = context;
            this.activity = activity;

            // crate a page description
            final int pageNumber = 1;
            final PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(
                    viewContent.getWidth(),
                    viewContent.getHeight(),
                    pageNumber
            ).create();

            // create a new page from the PageInfo
            final PdfDocument.Page page = pdfDocument.startPage(pageInfo);
            viewContent.draw(page.getCanvas());

            // do final processing of the page
            pdfDocument.finishPage(page);
        }

        @Override
        protected Void doInBackground(final Void... params) {

            Log.d(TAG,"doInBackground");

            activity.openProgressDialog("注文書作成中","しばらくお待ち下さい。");

//            final String myDirName = "Hidezo";
//            File myDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),myDirName);
//            Log.d(TAG, myDir.toString());
//            if (!myDir.exists()) {
//                final boolean result = myDir.mkdir();
//                if (!result) {
//                    Log.d(TAG, "Failed make directory.");
//                    myDir = context.getFilesDir();
//                }
//            }

            final File directory = getPdfDir(context); //context.getFilesDir();
            final File outputFile = new File(directory, PDF_FILE_NAME);
            try {

                Log.d(TAG, "Begin");

                final OutputStream out = new FileOutputStream(outputFile);
                pdfDocument.writeTo(out);
                pdfDocument.close();
                out.close();

                Log.d(TAG,"Finish -> " + outputFile.getPath());

            } catch (final IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Void dummy) {
            Log.d(TAG, "onPostExecute");

            activity.closeProgressDialog();

            final String base64Binary = HDZPdfManager.getPdfBase64String(activity.getApplicationContext());
            activity.respondBase64String(base64Binary);
        }
    }

//    public static byte[] getPdfBinary(final Context context) {
//        final File pdfFile = new File(context.getFilesDir(), PDF_FILE_NAME);
//        try {
//            final InputStream input = new FileInputStream(pdfFile);
//            final ByteArrayOutputStream bout = new ByteArrayOutputStream();
//            final byte[] bytes = new byte[BYTE_READ_SIZE];
//            while (true) {
//                final int len = input.read(bytes);
//                if (len < 0) {
//                    break;
//                }
//                bout.write(bytes, 0, len);
//            }
//            input.close();
//            return bout.toByteArray();
//        } catch (final IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


    /**
     * Converting File to Base64.encode String type using Method
     * @param context context
     * @return base64Binary string
     */
    public static String getPdfBase64String(final Context context) {

        final File directory = getPdfDir(context);
        final File pdfFile = new File(directory, PDF_FILE_NAME);

        String encodedFile = "";
        final String lastVal;
        try {
            final InputStream input = new FileInputStream(pdfFile);
            final ByteArrayOutputStream bout = new ByteArrayOutputStream();
            final Base64OutputStream bout64 = new Base64OutputStream(bout, Base64.DEFAULT);
            final byte[] bytes = new byte[BYTE_READ_SIZE];
            while (true) {
                final int len = input.read(bytes);
                if (len < 0) {
                    break;
                }
                bout64.write(bytes, 0, len);

//                Log.d(TAG, "READ=" + String.valueOf(len) + "Byte");
            }
            bout64.close();
            encodedFile = bout.toString();

        } catch (final IOException e) {
            e.printStackTrace();
        }
        lastVal = encodedFile;

        return lastVal;
    }

}
