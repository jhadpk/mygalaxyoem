package learn2crack.customlistview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by deepak.jha on 2/5/16.
 */
public class RobotoFont  extends TextView {
    public RobotoFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public RobotoFont(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public RobotoFont(Context context) {
        super(context);
    }
    public void setTypeface(Typeface tf, int style) {
        if (style == Typeface.BOLD) {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(),    "fonts/Roboto-Bold.ttf"));
        }
        else if(style == Typeface.ITALIC)
        {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Italic.ttf"));
        }
        else
        {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf"));
        }
    }
}
