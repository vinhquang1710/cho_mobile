package com.example.administrator.chotot.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.widget.TextView;

import com.example.administrator.chotot.R;

/**
 * Created by Administrator on 14/10/2016.
 */

public class CustomFontTextView extends TextView {
    private final static int ROBOTO_THIN = 0;
    private final static int ROBOTO_LIGHT = 1;
    private final static int ROBOTO_REGULAR = 2;
    private final static int ROBOTO_ITALIC = 3;
    private final static int ROBOTO_MEDIUM = 4;
    private final static int ROBOTO_BOLD = 5;

    private final static SparseArray<Typeface> mTypefaces = new SparseArray<Typeface>(6);

    public CustomFontTextView(Context context) {
        super(context);
    }

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(context, attrs);
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        parseAttributes(context, attrs);
    }

    private void parseAttributes(Context context, AttributeSet attrs) {
        TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.CustomFontTextView);

        int typefaceValue = values.getInt(R.styleable.CustomFontTextView_typeface, 0);
        values.recycle();

        setTypeface(obtaintTypeface(context, typefaceValue));
    }

    private Typeface obtaintTypeface(Context context, int typefaceValue) throws IllegalArgumentException {
        Typeface typeface = mTypefaces.get(typefaceValue);
        if (typeface == null) {
            typeface = createTypeface(context, typefaceValue);
            mTypefaces.put(typefaceValue, typeface);
        }
        return typeface;
    }

    private Typeface createTypeface(Context context, int typefaceValue) throws IllegalArgumentException {
        Typeface typeface;
        switch (typefaceValue) {
            case ROBOTO_THIN:
                typeface = Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf");
                break;

            case ROBOTO_LIGHT:
                typeface = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
                break;

            case ROBOTO_REGULAR:
                typeface = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
                break;
            case ROBOTO_ITALIC:
                typeface = Typeface.createFromAsset(context.getAssets(), "Roboto-Italic.ttf");
                break;
            case ROBOTO_MEDIUM:
                typeface = Typeface.createFromAsset(context.getAssets(), "Roboto-Medium.ttf");
                break;

            case ROBOTO_BOLD:
                typeface = Typeface.createFromAsset(context.getAssets(), "Roboto-Bold.ttf");
                break;

            default:
                throw new IllegalArgumentException("Unknown `typeface` attribute value " + typefaceValue);
        }
        return typeface;
    }
}
