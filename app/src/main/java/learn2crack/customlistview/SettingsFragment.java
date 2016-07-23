package learn2crack.customlistview;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.inmobi.commons.core.storage.KeyValueStore;
import com.inmobi.mygalaxydemo.singlead.R;


public class SettingsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = SettingsFragment.class.getSimpleName();

    public interface OnSettingsSavedListener {
        void onSettingsSaved();
    }

    private OnSettingsSavedListener mOnSettingsSavedListener;

    private KeyValueStore mPrefsEditor;
    private EditText mInputAppBundleId;
    private EditText mInputStrandsEndpoint;
    private EditText mInputPlacementId;
    private EditText mInputAesKey;
    private EditText mInputIvKey;
    private EditText mInputRequestParams;
    private Button mButtonSaveSettings;
    private EditText mEditTextYear;
    private Spinner mSpinnerGender;
    private EditText mEditTextSamsungId;
    private EditText mEditTextModel;
    private Spinner mSpinnerSegment;

    private String mAppBundleId = AppConstants.DEF_APP_BUNDLE_ID;
    private String mAdServerUrl = AppConstants.DEF_AD_SERVER_URL;
    private long mPlacementId = AppConstants.DEFAULT_PLACEMENT_ID;
    private String mAesKey = AppConstants.DEF_AES_KEY;
    private String mIVKey = AppConstants.DEF_IV_KEY;
    private String mReqParams = AppConstants.DEF_REQ_PARAMS;
    private String mYearOfBirth = "";
    private int mGenderIndex = 0;
    private String mSamsungId = "";
    private String mModel = "";
    private int mSegmentIndex = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initializeSettings(view);
        mButtonSaveSettings = (Button) view.findViewById(R.id.button_done);
        mButtonSaveSettings.setOnClickListener(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mOnSettingsSavedListener = (OnSettingsSavedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSettingsSavedListener");
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            default:
                break;
            case R.id.button_done: {
                mAppBundleId = mInputAppBundleId.getText().toString().trim();
                mPrefsEditor.putString(AppConstants.KEY_APP_BUNDLE_ID, 0 == mAppBundleId.length() ? AppConstants.DEF_APP_BUNDLE_ID : mAppBundleId);

                mAdServerUrl = mInputStrandsEndpoint.getText().toString().trim();
                mPrefsEditor.putString(AppConstants.KEY_AD_SERVER, 0 == mAdServerUrl.length() ? AppConstants.DEF_AD_SERVER_URL : mAdServerUrl);

                try {
                    mPlacementId = Long.parseLong(mInputPlacementId.getText().toString().trim());
                } catch (NumberFormatException e) {
                    Log.w(TAG, "Invalid placement ID ... Reverting to the default ID ("
                            + AppConstants.DEFAULT_PLACEMENT_ID + ")");
                    mPlacementId = AppConstants.DEFAULT_PLACEMENT_ID;
                }

                if (mPlacementId < 0) {
                    Log.w(TAG, "Placement ID cannot be negative ... Reverting to the default ID ("
                            + AppConstants.DEFAULT_PLACEMENT_ID + ")");
                    mPlacementId = AppConstants.DEFAULT_PLACEMENT_ID;
                }
                mPrefsEditor.putLong(AppConstants.KEY_PLACEMENT_ID, -1L == mPlacementId ? AppConstants.DEFAULT_PLACEMENT_ID : mPlacementId);

                mAesKey = mInputAesKey.getText().toString().trim();
                mPrefsEditor.putString(AppConstants.KEY_AES_KEY, 0 == mAesKey.length() ? AppConstants.DEF_AES_KEY : mAesKey);

                mIVKey = mInputIvKey.getText().toString().trim();
                mPrefsEditor.putString(AppConstants.KEY_IV_KEY, 0 == mIVKey.length() ? AppConstants.DEF_IV_KEY : mIVKey);

                mReqParams = mInputRequestParams.getText().toString().trim();
                mPrefsEditor.putString(AppConstants.KEY_REQ_PARAMS, 0 == mReqParams.length() ? AppConstants.DEF_REQ_PARAMS : mReqParams);

                mYearOfBirth = mEditTextYear.getText().toString().trim();
                mPrefsEditor.putString(AppConstants.KEY_YEAR_OF_BIRTH, 0 == mYearOfBirth.length() ? AppConstants.DEF_YEAR_OF_BIRTH : mYearOfBirth);

                mGenderIndex = mSpinnerGender.getSelectedItemPosition();
                mPrefsEditor.putInt(AppConstants.KEY_GENDER, mGenderIndex);

                mSamsungId = mEditTextSamsungId.getText().toString().trim();
                mPrefsEditor.putString(AppConstants.KEY_SAMSUNG_ID, 0 == mSamsungId.length() ? AppConstants.DEF_SAMSUNG_ID : mSamsungId);

                mModel = mEditTextModel.getText().toString().trim();
                mPrefsEditor.putString(AppConstants.KEY_MODEL, 0 == mModel.length()? AppConstants.DEF_MODEL : mModel);

                mSegmentIndex = mSpinnerSegment.getSelectedItemPosition();
                mPrefsEditor.putInt(AppConstants.KEY_SEGMENT, mSegmentIndex);

                mOnSettingsSavedListener.onSettingsSaved();
            }
                break;
        }

    }

    private void initializeSettings(@NonNull View view) {
        mPrefsEditor = KeyValueStore.getInstance(AppConstants.PREF_FILE);

        mInputAppBundleId = (EditText) view.findViewById(R.id.input_app_bundle_id);
        mInputStrandsEndpoint = (EditText) view.findViewById(R.id.input_strands_endpoint);
        mInputPlacementId = (EditText) view.findViewById(R.id.input_placement_id);
        mInputAesKey = (EditText) view.findViewById(R.id.input_aes_key);
        mInputIvKey = (EditText) view.findViewById(R.id.input_iv_key);
        mInputRequestParams = (EditText) view.findViewById(R.id.input_request_params);
        mEditTextYear = (EditText) view.findViewById(R.id.input_yob);
        mSpinnerGender = (Spinner) view.findViewById(R.id.spinner_gender);
        mEditTextSamsungId = (EditText) view.findViewById(R.id.input_samsung_id);
        mEditTextModel = (EditText) view.findViewById(R.id.input_model);
        mSpinnerSegment = (Spinner) view.findViewById(R.id.spinner_segment);
        //mPrefsEditor.putString(AppConstants.KEY_APP_BUNDLE_ID, mPrefsEditor.getString(AppConstants.KEY_APP_BUNDLE_ID, AppConstants.DEF_APP_BUNDLE_ID));
        //mPrefsEditor.putString(AppConstants.KEY_AD_SERVER, mPrefsEditor.getString(AppConstants.KEY_AD_SERVER, AppConstants.DEF_AD_SERVER_URL));
        //mPrefsEditor.putLong(AppConstants.KEY_PLACEMENT_ID, mPrefsEditor.getLong(AppConstants.KEY_PLACEMENT_ID, AppConstants.DEFAULT_PLACEMENT_ID));
        //mPrefsEditor.putString(AppConstants.KEY_AES_KEY, mPrefsEditor.getString(AppConstants.KEY_AES_KEY, AppConstants.DEF_AES_KEY));
        //mPrefsEditor.putString(AppConstants.KEY_IV_KEY, mPrefsEditor.getString(AppConstants.KEY_IV_KEY, AppConstants.DEF_IV_KEY));
        //mPrefsEditor.putString(AppConstants.KEY_REQ_PARAMS, mPrefsEditor.getString(AppConstants.KEY_REQ_PARAMS, AppConstants.DEF_REQ_PARAMS));

        initInstanceAttrs();

        mInputAppBundleId.setText(mAppBundleId);
        mInputPlacementId.setText(Long.toString(mPlacementId));
        mInputStrandsEndpoint.setText(mAdServerUrl);
        mInputAesKey.setText(mAesKey);
        mInputIvKey.setText(mIVKey);
        mInputRequestParams.setText(mReqParams);
        mEditTextYear.setText(mYearOfBirth);
        mSpinnerGender.setSelection(mGenderIndex);
        mEditTextSamsungId.setText(mSamsungId);
        mEditTextModel.setText(mModel);
        mSpinnerSegment.setSelection(mSegmentIndex);
    }

    private void initInstanceAttrs() {
        mAppBundleId = mPrefsEditor.getString(AppConstants.KEY_APP_BUNDLE_ID, AppConstants.DEF_APP_BUNDLE_ID);
        mAdServerUrl = mPrefsEditor.getString(AppConstants.KEY_AD_SERVER, AppConstants.DEF_AD_SERVER_URL);
        mPlacementId = mPrefsEditor.getLong(AppConstants.KEY_PLACEMENT_ID, AppConstants.DEFAULT_PLACEMENT_ID);
        mIVKey = mPrefsEditor.getString(AppConstants.KEY_IV_KEY, AppConstants.DEF_IV_KEY);
        mAesKey = mPrefsEditor.getString(AppConstants.KEY_AES_KEY, AppConstants.DEF_AES_KEY);
        mReqParams = mPrefsEditor.getString(AppConstants.KEY_REQ_PARAMS, AppConstants.DEF_REQ_PARAMS);

        mYearOfBirth = mPrefsEditor.getString(AppConstants.KEY_YEAR_OF_BIRTH, AppConstants.DEF_YEAR_OF_BIRTH);
        mGenderIndex = mPrefsEditor.getInt(AppConstants.KEY_GENDER, AppConstants.DEF_GENDER_INDEX);
        mSamsungId = mPrefsEditor.getString(AppConstants.KEY_SAMSUNG_ID, AppConstants.DEF_SAMSUNG_ID);
        mModel = mPrefsEditor.getString(AppConstants.KEY_MODEL, AppConstants.DEF_MODEL);
        mSegmentIndex = mPrefsEditor.getInt(AppConstants.KEY_SEGMENT, AppConstants.DEF_SEGMENT_INDEX);
    }
}