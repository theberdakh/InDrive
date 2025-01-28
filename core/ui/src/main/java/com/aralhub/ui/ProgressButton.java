package com.aralhub.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressButton extends FrameLayout {

    private TextView textView;
    private ProgressBar progressBar;
    private boolean isTapToStopEnabled;
    private OnClickListener onClickListener;
    private boolean isProgressStarted = false;

    public ProgressButton(Context context) {
        super(context);
        init(context, null);
    }

    public ProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public ProgressButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.CENTER;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProgressButton);
        isTapToStopEnabled = a.getBoolean(R.styleable.ProgressButton_enableTapToStop, true);
        textView = new TextView(context);
        textView.setText(a.getText(R.styleable.ProgressButton_text));
        textView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        textView.setTextAppearance(a.getResourceId(R.styleable.ProgressButton_textAppearance, 0));
        addView(textView, params);

        progressBar = new ProgressBar(context);
        progressBar.setIndeterminate(true);
        progressBar.setIndeterminateTintList(ColorStateList.valueOf(Color.WHITE));
        progressBar.setVisibility(GONE);

        textView.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );
        int textViewHeight = textView.getMeasuredHeight(); // to make the progress bar's height equal to the text view's height
        LayoutParams progressBarParams = new LayoutParams(
                textViewHeight,
                textViewHeight
        );
        progressBarParams.gravity = Gravity.CENTER;
        addView(progressBar, progressBarParams);

        super.setOnClickListener(v -> {
            handleProgressState();
            if (onClickListener != null) {
                onClickListener.onClick(v);
            }
        });

        a.recycle();
    }

    private void handleProgressState() {
        isProgressStarted = !isProgressStarted;
        if (isProgressStarted) {
            textView.setVisibility(GONE);
            progressBar.setVisibility(VISIBLE);
        } else {
            if (isTapToStopEnabled) {
                textView.setVisibility(VISIBLE);
                progressBar.setVisibility(GONE);
            }
        }
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private int dpToPx(float dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }

}