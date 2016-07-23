package learn2crack.customlistview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.inmobi.mygalaxydemo.singlead.R;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by deepak.jha on 2/4/16.
 */


public class Feedback extends Activity {

    private EditText txtComments;
    private TextView btnSubmit;
    private String advertisingId;
    private String androidId;
    private String o1Hash;
    private ImageButton happyButton;
    private ImageButton okButton;
    private ImageButton sadButton;
    private TextView lblHappy;
    private TextView lblOk;
    private TextView lblSad;
    private Typeface myTypefaceLight;
    private Typeface myTypefaceBold;
    private static final int LONG_DELAY = 3500;
    private int rating;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionbar();

        setContentView(R.layout.activity_feedback_form);

        rating = 0;
        myTypefaceLight = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        myTypefaceBold = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        happyButton= (ImageButton)findViewById(R.id.happy);
        okButton= (ImageButton)findViewById(R.id.ok);
        sadButton= (ImageButton)findViewById(R.id.sad);
        lblHappy =(TextView) findViewById(R.id.lblhappy);
        lblOk =(TextView) findViewById(R.id.lblok);
        lblSad =(TextView) findViewById(R.id.lblsad);

        okButton.setBackgroundResource(R.drawable.bluesmileyokay);
        happyButton.setBackgroundResource(R.drawable.greensmileyawesome);
        sadButton.setBackgroundResource(R.drawable.redsmileysad);
        lblHappy.setTypeface(myTypefaceLight);
        lblOk.setTypeface(myTypefaceLight);
        lblSad.setTypeface(myTypefaceLight);

        TextView lblHeader = (TextView)findViewById(R.id.lblFeedback);

        lblHeader.setTypeface(myTypefaceLight);

        TextView lblComments =(TextView) findViewById(R.id.lblcomments);
        TextView submit =(TextView) findViewById(R.id.submit);
        lblComments.setTypeface(myTypefaceLight);
        submit.setTypeface(myTypefaceLight);

        addListenerOnButton();
        new Thread(new Runnable() {
            public void run() {
                try {
                    learn2crack.customlistview.AdvertisingIdClient.AdInfo adInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
                    advertisingId = adInfo.getId();
                    System.out.println(advertisingId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        happyButton.setOnClickListener(happyButtonHandler);
        okButton.setOnClickListener(okButtonHandler);
        sadButton.setOnClickListener(sadButtonHandler);
    }


    View.OnClickListener happyButtonHandler = new View.OnClickListener() {

        public void onClick(View v) {
            happyButton.setBackgroundResource(R.drawable.greensmileyaftertap);
            sadButton.setBackgroundResource(R.drawable.redsmileysad);
            okButton.setBackgroundResource(R.drawable.bluesmileyokay);
            lblHappy.setTypeface(myTypefaceBold);
            lblOk.setTypeface(myTypefaceLight);
            lblSad.setTypeface(myTypefaceLight);
            rating = 1;


        }
    };

    View.OnClickListener okButtonHandler = new View.OnClickListener() {

        public void onClick(View v) {
            okButton.setBackgroundResource(R.drawable.bluesmileyaftertap);
            happyButton.setBackgroundResource(R.drawable.greensmileyawesome);
            sadButton.setBackgroundResource(R.drawable.redsmileysad);
            lblHappy.setTypeface(myTypefaceLight);
            lblOk.setTypeface(myTypefaceBold);
            lblSad.setTypeface(myTypefaceLight);
            rating = 2;

        }
    };

    View.OnClickListener sadButtonHandler = new View.OnClickListener() {

        public void onClick(View v) {
            sadButton.setBackgroundResource(R.drawable.redsimleyaftertap);
            happyButton.setBackgroundResource(R.drawable.greensmileyawesome);
            okButton.setBackgroundResource(R.drawable.bluesmileyokay);
            lblHappy.setTypeface(myTypefaceLight);
            lblOk.setTypeface(myTypefaceLight);
            lblSad.setTypeface(myTypefaceBold);
            rating = 3;

        }
    };


    private void initActionbar() {
        getActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>InMobi Commerce on My Galaxy - Demo</font>"));
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.top_bar_color));
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.theme_color));
        }
    }

    public void addListenerOnButton() {

        //ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        btnSubmit = (TextView) findViewById(R.id.submit);
        btnSubmit.setTypeface(myTypefaceLight);
        txtComments = (EditText) findViewById(R.id.comments);
        //if click on me, then display the current rating value.
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (rating == 0) {
                    Toast toast = Toast.makeText(Feedback.this, "Please select a rating using one of the Smileys.", Toast.LENGTH_LONG);
                    LinearLayout toastLayout = (LinearLayout) toast.getView();
                    TextView toastTV = (TextView) toastLayout.getChildAt(0);
                    toastTV.setTextSize(15);
                    toast.show();

                } else {
                    androidId = getPlatformId();
                    o1Hash = getDigested(androidId, "SHA-1");

                    try {
                        submitFeedback(txtComments.getText().toString(), rating, advertisingId, o1Hash);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        });

    }

    public String getPlatformId() {
        String mAndroidId = android.provider.Settings.Secure.getString(getApplicationContext().getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);

        if (mAndroidId == null) {
            mAndroidId = android.provider.Settings.System.getString(getApplicationContext().getContentResolver(),
                    android.provider.Settings.System.ANDROID_ID);
        }
        return mAndroidId;
    }

    public String getDigested(String androidId, String digest) {
        int decimal16 = 16;
        int hexFf = 0xff;
        final int hex100 = 0x100;
        try {
            if ((null == androidId) || ("".equals(androidId.trim()))) {
                return "TEST_EMULATOR";
            }

            MessageDigest md = MessageDigest.getInstance(digest);
            md.update(androidId.getBytes());
            byte byteData[] = md.digest();

            // convert the byte to hex format
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & hexFf) + hex100, decimal16).substring(1));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public void submitFeedback(String comments, int rating, String gpId, String o1Hashed) throws IOException {
        URL url = null;
        HttpURLConnection conn = null;
        StringBuffer contentXML = null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            url = new URL("http://54.169.2.221/OemFeedbackService/api/sendfeedback");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            Long tsLong = System.currentTimeMillis()/1000;
            //String ts = tsLong.toString();
            String input = "{\"gpid\":\""+gpId+"\",\"o1id\":\""+o1Hashed+"\",\"oemdeviceid\":\"\",\"timestamp\":"+tsLong+",\"rating\":"+rating+",\"description\":\""+comments+"\" }";
            System.out.println(input);

            Boolean networkconnected = isNetworkConnected();
            if(networkconnected) {
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                out.write(input);
                out.close();

                if (conn.getResponseCode() == 200) {
                    Intent i = new Intent(Feedback.this, MainActivity.class);
                    startActivity(i);
                    Toast toast = Toast.makeText(Feedback.this, "Submitted Successfully. Thanks for your feedback.", Toast.LENGTH_SHORT);
                    LinearLayout toastLayout = (LinearLayout) toast.getView();
                    TextView toastTV = (TextView) toastLayout.getChildAt(0);
                    toastTV.setTextSize(15);
                    toast.show();

                } else {
                    Toast toast = Toast.makeText(Feedback.this, "Not submitted. Server Error.", Toast.LENGTH_LONG);
                    LinearLayout toastLayout = (LinearLayout) toast.getView();
                    TextView toastTV = (TextView) toastLayout.getChildAt(0);
                    toastTV.setTextSize(15);
                    toast.show();
                    throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());

                }
            }else{

                Toast toast = Toast.makeText(Feedback.this, "Please check your internet connection.", Toast.LENGTH_LONG);
                LinearLayout toastLayout = (LinearLayout) toast.getView();
                TextView toastTV = (TextView) toastLayout.getChildAt(0);
                toastTV.setTextSize(15);
                toast.show();

            }
        }catch (ProtocolException pex) {
            Log.d("Invalid api call", pex.getMessage());
            pex.printStackTrace();
        } catch (MalformedURLException mex) {
            Log.d("Error", mex.getMessage());
            mex.printStackTrace();
        } catch (IOException ioex) {
            Log.d("Error", ioex.getMessage());
            ioex.printStackTrace();
        }
    }

}