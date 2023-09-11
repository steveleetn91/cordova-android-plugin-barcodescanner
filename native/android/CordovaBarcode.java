package org.apache.cordova;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

class CordovaBarcode extends CordovaPlugin {
    private static final String TAG = "CordovaBarcode";
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("scanner")) {
            this.scanner();
            return true;
        }
        return false;
    }
    public PluginResult scanner(){
        if (ContextCompat.checkSelfPermission(this.cordova.getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.cordova.getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
            return null;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        this.cordova.startActivityForResult(this,intent,1);


        return null;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

//        ImageView imageview = (ImageView) findViewById(R.id.ImageView01); //sets imageview as the bitmap
//        imageview.setImageBitmap(image);
        BarcodeScannerOptions options =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_ALL_FORMATS)
                        .build();
        BarcodeScanner scanner = BarcodeScanning.getClient(options);
        if (resultCode == Activity.RESULT_CANCELED) {
            // code to handle cancelled state
        }
        else if (requestCode == 1) {
            // code to handle data from CAMERA_REQUEST
            //analyzer.analyze(image);
            Bitmap image = (Bitmap) data.getExtras().get("data");
            LOG.d(TAG,image.toString());
            Task<List<Barcode>> result = scanner.process(image,0)
                    .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(List<Barcode> barcodes) {
                            // Task completed successfully
                            // ...
                            if(barcodes.size() == 0) {
                                fireAdEvent("BARCODE_FAIL");
                                return;
                            }
                            for (Barcode barcode: barcodes) {
                                Rect bounds = barcode.getBoundingBox();
                                Point[] corners = barcode.getCornerPoints();

                                String rawValue = barcode.getRawValue();
                                int valueType = barcode.getValueType();
                                // See API reference for complete list of supported types

                                switch (valueType) {
                                    case Barcode.TYPE_WIFI:
                                        String ssid = barcode.getWifi().getSsid();
                                        String password = barcode.getWifi().getPassword();
                                        JSONObject TYPE_WIFI = new JSONObject();
                                        try {
                                            TYPE_WIFI.put("ssid",ssid);
                                            TYPE_WIFI.put("password",password);
                                            TYPE_WIFI.put("type",Barcode.TYPE_WIFI);
                                            TYPE_WIFI.put("code",rawValue);
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                        fireAdEvent("BARCODE_RECEIVE",TYPE_WIFI);
                                        break;
                                    case Barcode.TYPE_URL:
                                        String title = barcode.getUrl().getTitle();
                                        String url = barcode.getUrl().getUrl();
                                        JSONObject TYPE_URL = new JSONObject();
                                        try {
                                            TYPE_URL.put("title",title);
                                            TYPE_URL.put("url",url);
                                            TYPE_URL.put("type",Barcode.TYPE_URL);
                                            TYPE_URL.put("code",rawValue);
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                        fireAdEvent("BARCODE_RECEIVE",TYPE_URL);
                                        break;
                                    default:
                                        JSONObject TYPE_DEFAULT = new JSONObject();
                                        try {
                                            TYPE_DEFAULT.put("code",rawValue);
                                            TYPE_DEFAULT.put("type","others");
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                        fireAdEvent("BARCODE_RECEIVE",TYPE_DEFAULT);
                                        break;
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Task failed with an exception
                            // ...
                            LOG.d(TAG,"Barcode Fail");
                        }
                    });
        }
        else if (requestCode == 2) {
            // code to handle data from CONTACT_VIEW
        }
    }
    public void fireAdEvent(String eventName, JSONObject data) {
        String js = new CordovaEventBuilder(eventName).withData(data).build();
        loadJS(js);
    }
    public void fireAdEvent(String eventName) {
        String js = new CordovaEventBuilder(eventName).build();
        loadJS(js);
    }
    private void loadJS(String js) {
        this.webView.loadUrl(js);
    }
}