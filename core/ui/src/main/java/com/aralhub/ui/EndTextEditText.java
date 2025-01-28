package com.aralhub.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

public class EndTextEditText extends LinearLayout {

    private OnClickListener endTextClickListener;


    public EndTextEditText(Context context) {
        super(context);
        init(context, null);
    }

    public EndTextEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(Context context, AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        EditText editText = new AppCompatEditText(context, attrs);
        LayoutParams editTextParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
        editTextParams.setMargins(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));
        addView(editText, editTextParams);

        TextView endTextView = new TextView(context);
        LayoutParams endTextViewParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        endTextViewParams.gravity = Gravity.CENTER_VERTICAL;
        endTextViewParams.setMargins(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));
        endTextView.setClickable(true);
        endTextView.setFocusable(true);
        endTextView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ripple_text));
        addView(endTextView, endTextViewParams);

        endTextView.setOnClickListener(v -> {
            if (endTextClickListener != null) {
                endTextClickListener.onClick(v);
            }
        });

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.EndTextEditText);
            String endText = a.getString(R.styleable.EndTextEditText_endText);
            endTextView.setText(endText);
            endTextView.setTextAppearance(a.getResourceId(R.styleable.EndTextEditText_endTextAppearance, R.style.EndTextEditTextTheme));
            a.recycle();
        }
    }

    public void setEndTextClickListener(OnClickListener listener) {
        endTextClickListener = listener;
    }

    public EndTextEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private int dpToPx(float dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }

}
