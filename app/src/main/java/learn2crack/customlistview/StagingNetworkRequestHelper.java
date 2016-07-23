
package learn2crack.customlistview;

import android.content.Context;
import android.util.Log;

import com.google.ads.AdRequest;
import com.inmobi.ads.AdNetworkRequest;
import com.inmobi.commons.core.storage.KeyValueStore;
import com.inmobi.commons.core.utilities.info.AppInfo;
import com.inmobi.sdk.InMobiSdk;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jagadish.shenoy on 11/3/15.
 */
public class StagingNetworkRequestHelper {

    private static final String TAG = StagingNetworkRequestHelper.class.getSimpleName();

    public static void setStagingNetworkParameters() {
        // app install source - always defaults to com.android.vending
        // to change this value, change the value of AppConstants#DEF_APP_INSTALL_SOURCE
        AdNetworkRequest.setAppInstallSource(getAppInstallSource());
        AdNetworkRequest.setAdServerUrl(getAdServerUrl());
        String aesKey = getAesKey();
        String ivKey = getIvKey();
        if (null != aesKey && 0 != aesKey.trim().length()) {
            AdNetworkRequest.setAesKey(aesKey.getBytes());
        }
        if (null != ivKey && 0 != ivKey.trim().length()) {
            AdNetworkRequest.setIvKey(ivKey.getBytes());
        }
        AppInfo appInfo = AppInfo.getInstance();
        appInfo.setPackageName(getAppBundleId());
    }

    private static String getAppInstallSource() {
        return AppConstants.DEF_APP_INSTALL_SOURCE;
    }

    private static String getAppBundleId() {
        KeyValueStore editor = KeyValueStore.getInstance(AppConstants.PREF_FILE);
        return editor.getString(AppConstants.KEY_APP_BUNDLE_ID, AppConstants.DEF_APP_BUNDLE_ID);
    }

    private static String getAdServerUrl() {
        KeyValueStore editor = KeyValueStore.getInstance(AppConstants.PREF_FILE);
        return editor.getString(AppConstants.KEY_AD_SERVER, AppConstants.DEF_AD_SERVER_URL);
    }

    public static long getPlacementId() {
        KeyValueStore editor = KeyValueStore.getInstance(AppConstants.PREF_FILE);
        return editor.getLong(AppConstants.KEY_PLACEMENT_ID, AppConstants.DEFAULT_PLACEMENT_ID);
    }

    public static Map<String, String> getRequestParams() {
        KeyValueStore editor = KeyValueStore.getInstance(AppConstants.PREF_FILE);
        return buildMap(editor.getString(AppConstants.KEY_REQ_PARAMS, AppConstants.DEF_REQ_PARAMS));
    }

    private static String getAesKey() {
        KeyValueStore editor = KeyValueStore.getInstance(AppConstants.PREF_FILE);
        return editor.getString(AppConstants.KEY_IV_KEY, AppConstants.DEF_IV_KEY);
    }

    private static String getIvKey() {
        KeyValueStore editor = KeyValueStore.getInstance(AppConstants.PREF_FILE);
        return editor.getString(AppConstants.KEY_AES_KEY, AppConstants.DEF_AES_KEY);
    }

    private static Map<String, String> buildMap(String requestParams) {
        Map<String, String> map = new HashMap<>();
        if (null != requestParams && !"".equals(requestParams.trim())) {
            String tokens[] = requestParams.split("&");
            for (String token : tokens) {
                String keyval[] = token.split("=");
                String key = keyval[0];
                String val = keyval[1];
                if (key != null && !"".equals(key.trim()) && val != null
                        && !"".equals(val.trim()))
                    map.put(key, val);
            }
        }
        return map;
    }

    public static Integer getYearOfBirth(Context context) {
        KeyValueStore keyValueStore = KeyValueStore.getInstance(context, AppConstants.PREF_FILE);
        String yearOfBirth = keyValueStore.getString(AppConstants.KEY_YEAR_OF_BIRTH,
                AppConstants.DEF_YEAR_OF_BIRTH);
        if(!AppConstants.DEF_YEAR_OF_BIRTH.equals(yearOfBirth)) {
            try {
                return Integer.parseInt(yearOfBirth);
            }catch (NumberFormatException ex) {
                Log.e(TAG, "Invalid Year of birth:"+yearOfBirth);
                return null;
            }
        }
        return null;
    }

    public static InMobiSdk.Gender getGender(Context context) {
        KeyValueStore keyValueStore = KeyValueStore.getInstance(context,AppConstants.PREF_FILE);
        int genderIndex = keyValueStore.getInt(AppConstants.KEY_GENDER, AppConstants.DEF_GENDER_INDEX);
        if(genderIndex == 1) {
            return InMobiSdk.Gender.MALE;
        } else if(genderIndex == 2) {
            return InMobiSdk.Gender.FEMALE;
        }
        return null;
    }

    public static String getSamsungId() {
        KeyValueStore keyValueStore = KeyValueStore.getInstance(AppConstants.PREF_FILE);
        String samsungId = keyValueStore.getString(AppConstants.KEY_SAMSUNG_ID,
                AppConstants.DEF_SAMSUNG_ID);
        if(!AppConstants.DEF_SAMSUNG_ID.equals(samsungId)) {
            return samsungId;
        }
        return null;
    }

    public static String getModel() {
        KeyValueStore keyValueStore = KeyValueStore.getInstance(AppConstants.PREF_FILE);
        String model = keyValueStore.getString(AppConstants.KEY_MODEL, AppConstants.DEF_MODEL);
        if(!AppConstants.DEF_SAMSUNG_ID.equals(model)) {
            return model;
        }
        return null;
    }

    public static String getSegment() {
        KeyValueStore keyValueStore = KeyValueStore.getInstance(AppConstants.PREF_FILE);
        int segmentIndex = keyValueStore.getInt(AppConstants.KEY_SEGMENT, AppConstants.DEF_SEGMENT_INDEX);
        if(0 == segmentIndex) {
            return null;
        } else if(1 == segmentIndex) {
            return "h";
        } else if(2 == segmentIndex) {
            return "m";
        } else {
            return "l";
        }
    }
}
