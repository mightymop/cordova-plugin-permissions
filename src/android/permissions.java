package de.mopsdom.permissions;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.content.ContextCompat;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class permissions extends CordovaPlugin {

  private static final int PERMISSION_REQUEST_CODE = 12345;
  private CallbackContext callbackContext;

  private void check(final JSONArray data, final CallbackContext callbackContext, Context ctx) {

    if (data == null || data.length() == 0) {
      callbackContext.error("bad request (parameter)");
      return;
    }

    try {
      JSONArray arr = new JSONArray();

      for (int n = 0; n < data.length(); n++) {
        String permission = data.getString(n);

        boolean isAllowed = ContextCompat.checkSelfPermission(ctx, permission) == PackageManager.PERMISSION_GRANTED;

        JSONObject obj = new JSONObject();
        obj.put("permission", permission);
        obj.put("granted", isAllowed);
        arr.put(obj);
      }

      callbackContext.success(arr);

    } catch (Exception e) {
      Log.e("cordova-plugin-permissions", e.getMessage());
      callbackContext.error(e.getMessage());
    }

  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                         int[] grantResults) {
    switch (requestCode) {
      case PERMISSION_REQUEST_CODE:
        JSONArray arr = new JSONArray();

        for (int n = 0; n < permissions.length; n++) {
          try {
            JSONObject obj = new JSONObject();
            obj.put("permission", permissions[n]);

            if (grantResults.length > 0) {
              obj.put("granted", grantResults[n] == PackageManager.PERMISSION_GRANTED);
            } else {
              obj.put("granted", false);
            }
            arr.put(obj);
          } catch (Exception e) {
            Log.e("cordova-plugin-permissions", e.getMessage());
          }
        }

        callbackContext.success(arr);
        return;
    }
  }

  private void ask(final JSONArray data, final CallbackContext callbackContext) {

    if (data == null || data.length() == 0) {
      callbackContext.error("bad request (parameter)");
      return;
    }
    try {

      ArrayList<String> listToAsk = new ArrayList<String>();

      for (int n = 0; n < data.length(); n++) {
        String permission = data.getString(n);
        listToAsk.add(permission);
      }

      this.callbackContext = callbackContext;
      cordova.getActivity().requestPermissions(listToAsk.toArray(new String[listToAsk.size()]), PERMISSION_REQUEST_CODE);
    } catch (Exception e) {
      Log.e("cordova-plugin-permissions", e.getMessage());
      callbackContext.error(e.getMessage());
    }
  }

  @Override
  public boolean execute(final String action, final JSONArray data, final CallbackContext callbackContext) {

    if (action.equals("ask")) {
      cordova.getThreadPool().execute(new Runnable() {
        public void run() {
          ask(data, callbackContext);
        }
      });

      return true;
    } else if (action.equals("check")) {
      cordova.getThreadPool().execute(new Runnable() {
        public void run() {
          check(data, callbackContext, cordova.getActivity());
        }
      });

      return true;
    }

    return false;
  }
}
