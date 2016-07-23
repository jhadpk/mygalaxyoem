package learn2crack.customlistview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiNativeStrand;
import com.inmobi.commons.core.utilities.Logger;
import com.inmobi.mygalaxydemo.singlead.R;
import com.inmobi.sdk.InMobiSdk;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jagadish.shenoy on 1/11/16.
 */
public class MyGalaxyFragment extends android.app.Fragment {

    private static final String TAG = MyGalaxyFragment.class.getSimpleName();

    ///////Variables for InMobi Ads
    private static final String ACCOUNT_ID = "12345678901234567890123456789012";

    private static Long PLACEMENT_ID = 1435531322103L;//1435164166772L;//;

    private InMobiNativeStrand mNativeStrand;

    @NonNull
    private Spinner mSpinnerGender;

    @NonNull
    private Spinner mSpinnerAge;

    @NonNull
    private TextView mButtonReload;

    @NonNull
    private TextView feedback;

    @NonNull
    private LinearLayout mAdContainer;

    @NonNull
    private TextView mTextViewLoading;

    @NonNull
    private TextView lblGender;

    @NonNull
    private TextView lblAge;

    @NonNull
    private Typeface myTypefaceLight;

    @NonNull
    private ImageButton maleButton;

    @NonNull
    private ImageButton femaleButton;

    @NonNull
    private Typeface myTypefaceBold;

    private String mGender;

    private  int mCounter;

    private int fCounter;

    private int mAge;

    private static final String KEY_EXTRA_SAMSUNG_ID = "a-d-otherID";

    private static final String VALUE_EXTRA_SAMSUNG_ID = "AnkitMittal";

    private static final String KEY_EXTRA_MODEL = "a-d-model";

    private static final String VALUE_EXTRA_MODEL = "Samsung Galaxy S5";

    private static final String KEY_EXTRA_SEGMENT = "a-d-segment";

    private static final String KEY_EXTRA_GENDER = "u-gender";

    private static final String KEY_EXTRA_AGE = "u-age";

    private static final String VALUE_EXTRA_SEGMENT = "h";

    private static final String VALUE_LOADING = "Loading ...";

    private static final String VALUE_PLEASE_TRY_AGAIN = "Please try again...";

    private static final int GENDER_POSITION_MALE = 1;

    private static final int AGE_POSITION_25 = 8;

    private static final String GENDER_MALE = "m";

    private static final String GENDER_FEMALE = "f";

    private static final String GENDER_UNKNOWN = "UNKNOWN";

    private static final int AGE_UNKNOWN = 999;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initActionbar();
        Log.d(TAG, "OnCreateView");
        mCounter=0;
        fCounter=0;
        mGender = GENDER_UNKNOWN;
        //Initialize InMobi SDK before any Ad API call.
        View fragmentView = inflater.inflate(R.layout.layout_mygalaxy_fragment, container, false);
        mSpinnerGender = (Spinner) fragmentView.findViewById(R.id.spinner_gender);
        mSpinnerAge = (Spinner) fragmentView.findViewById(R.id.spinner_age);
        mButtonReload = (TextView) fragmentView.findViewById(R.id.btn_reload);

        mAdContainer = (LinearLayout) fragmentView.findViewById(R.id.ad_container);

        lblGender = (TextView) fragmentView.findViewById(R.id.lblGender);
        lblAge = (TextView) fragmentView.findViewById(R.id.lblAge);

        mButtonReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean networkconnected = isNetworkConnected();
                if(networkconnected) {
                    mButtonReload.setClickable(false);
                    mButtonReload.setEnabled(false);
                    mButtonReload.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
                    reloadAd();

                }else{

                    Toast toast = Toast.makeText(getActivity(), "Please check your internet connection.", Toast.LENGTH_SHORT);
                    LinearLayout toastLayout = (LinearLayout) toast.getView();
                    TextView toastTV = (TextView) toastLayout.getChildAt(0);
                    toastTV.setTextSize(15);
                    toast.show();
                }
            }
        });
        feedback = (TextView) fragmentView.findViewById(R.id.providefeedback);



        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity().getApplicationContext(), Feedback.class);
                startActivity(i);
            }
        });

        mTextViewLoading = new TextView(getActivity());
        mTextViewLoading.setText("Loading ...");
        myTypefaceLight = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
        myTypefaceBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Bold.ttf");
        mButtonReload.setTypeface(myTypefaceLight);
        feedback.setTypeface(myTypefaceLight);
        lblGender.setTypeface(myTypefaceLight);
        lblAge.setTypeface(myTypefaceLight);
        mTextViewLoading.setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.FILL_PARENT);
        params.weight = 1.0f;
        params.gravity = Gravity.CENTER_HORIZONTAL;
        mTextViewLoading.setLayoutParams(params);

        mSpinnerGender.setSelection(GENDER_POSITION_MALE);
        mSpinnerAge.setSelection(AGE_POSITION_25);
        if (mNativeStrand != null) {
            mNativeStrand = null;
        }
        //maleButton= (ImageButton)fragmentView.findViewById(R.id.male);
        //femaleButton= (ImageButton)fragmentView.findViewById(R.id.female);

        //maleButton.setOnClickListener(maleButtonHandler);
        //femaleButton.setOnClickListener(femaleButtonHandler);
        initInMobiSDK();
        //loadAds();
        return fragmentView;
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

//    View.OnClickListener maleButtonHandler = new View.OnClickListener() {
//
//        public void onClick(View v) {
//            mCounter++;
//            fCounter=0;
//            if(mCounter%2!=0) {
//                maleButton.setBackgroundResource(R.drawable.malebluegood);
//                femaleButton.setBackgroundResource(R.drawable.femalegood);
//                mGender = GENDER_MALE;
//            }else{
//                maleButton.setBackgroundResource(R.drawable.malegood);
//                femaleButton.setBackgroundResource(R.drawable.femalegood);
//                mGender = GENDER_UNKNOWN;
//            }
//
//        }
//    };
//
//    View.OnClickListener femaleButtonHandler = new View.OnClickListener() {
//
//        public void onClick(View v) {
//            fCounter++;
//            mCounter=0;
//            if(fCounter%2!=0) {
//                maleButton.setBackgroundResource(R.drawable.malegood);
//                femaleButton.setBackgroundResource(R.drawable.femalebluegood);
//                mGender = GENDER_FEMALE;
//            }else{
//                maleButton.setBackgroundResource(R.drawable.malegood);
//                femaleButton.setBackgroundResource(R.drawable.femalegood);
//                mGender = GENDER_UNKNOWN;
//            }
//
//        }
//    };

    private void initActionbar() {
        getActivity().getActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>InMobi Commerce on My Galaxy - Demo</font>"));
        getActivity().getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        Window window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.top_bar_color));
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setNavigationBarColor(getResources().getColor(R.color.theme_color));
        }
    }


    public static class ResizableImageView extends ImageView {

        public ResizableImageView(Context context) {
            super(context);
        }

        public ResizableImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = width * getDrawable().getIntrinsicHeight() / getDrawable().getIntrinsicWidth();
            setMeasuredDimension(width, height);
        }
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "OnDestroyView");
        deinitAd();
        super.onDestroyView();
    }

    //// Code for InMobi Ads - Non-placer
    private void initInMobiSDK() {
        InMobiSdk.setYearOfBirth(1988);

        InMobiSdk.init(getActivity(), ACCOUNT_ID);
        InMobiSdk.setLogLevel(InMobiSdk.LogLevel.DEBUG);
        Logger.setLogLevel(Logger.InternalLogLevel.INTERNAL);
        StagingNetworkRequestHelper.setStagingNetworkParameters();
        if (null == mNativeStrand) {
            mNativeStrand = new InMobiNativeStrand(getActivity(),
                    PLACEMENT_ID,
                    new StrandAdListener());
        }
    }

    private final class StrandAdListener implements InMobiNativeStrand.NativeStrandAdListener {

        @Override
        public void onAdLoadSucceeded(@NonNull InMobiNativeStrand inMobiNativeStrand) {
            Log.i(TAG, "Ad load succeeded");
            mButtonReload.setClickable(true);
            mButtonReload.setEnabled(true);
            mButtonReload.getBackground().setColorFilter(null);
            mAdContainer.removeAllViews();
            View strandView = inMobiNativeStrand.getStrandView(null, mAdContainer);
            mAdContainer.addView(strandView);
            InMobiNativeStrand.bind(strandView, inMobiNativeStrand);
        }

        @Override
        public void onAdLoadFailed(@NonNull InMobiNativeStrand inMobiNativeStrand, @NonNull final InMobiAdRequestStatus inMobiAdRequestStatus) {
          //  mTextViewLoading.setText(VALUE_PLEASE_TRY_AGAIN);
          //  mTextViewLoading.invalidate();
            reloadAd();
            Log.w(TAG, "Ad Load failed, reason:" + inMobiAdRequestStatus.getStatusCode() + "-" + inMobiAdRequestStatus.getMessage());
        }

        @Override
        public void onAdImpressed(@NonNull InMobiNativeStrand inMobiNativeStrand) {

        }

        @Override
        public void onAdClicked(@NonNull InMobiNativeStrand inMobiNativeStrand) {

        }
    }

    private void updateAge() {
        if (mSpinnerAge.getSelectedItemPosition() == 0) {
            mAge = AGE_UNKNOWN;
        } else {
            mAge = Integer.parseInt(mSpinnerAge.getSelectedItem().toString());
        }
    }

    private void updateGender() {
        if (mSpinnerGender.getSelectedItemPosition() == 0) {
            mGender = GENDER_UNKNOWN;
        } else if (mSpinnerGender.getSelectedItemPosition() == 1) {
            mGender = GENDER_MALE;
        } else {
            mGender = GENDER_FEMALE;
        }
    }

    private void reloadAd() {
        clearAds();
        loadAds();
    }

    private void clearAds() {
        InMobiNativeStrand.unbind(mNativeStrand);
        mAdContainer.removeAllViews();
    }

    private void loadAds() {
            setExtras();
            mNativeStrand.load();
            mTextViewLoading.setText(VALUE_LOADING);
            mAdContainer.addView(mTextViewLoading);
    }

    private void setExtras() {
        updateGender();
        updateAge();
        final Map<String, String> extras = new HashMap<>();
        extras.put(KEY_EXTRA_SAMSUNG_ID, VALUE_EXTRA_SAMSUNG_ID);
        extras.put(KEY_EXTRA_MODEL, VALUE_EXTRA_MODEL);
        extras.put(KEY_EXTRA_SEGMENT, VALUE_EXTRA_SEGMENT);
        extras.put("mk-carrier", "1.6.0.0");
        extras.put(KEY_EXTRA_AGE, String.valueOf(mAge));
        extras.put(KEY_EXTRA_GENDER, mGender);
        System.out.println("Gender is "+ mGender);
        mNativeStrand.setExtras(extras);
    }

    private void deinitAd() {
        clearAds();
        mNativeStrand.destroy();
    }
}
