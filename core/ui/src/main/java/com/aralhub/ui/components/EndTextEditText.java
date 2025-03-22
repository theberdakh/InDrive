package com.aralhub.ui.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.aralhub.ui.R;

import java.util.Objects;

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

    public EndTextEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(Context context, AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        setClickable(true);
        setFocusable(true);
        setBackground(ContextCompat.getDrawable(context, R.drawable.selector_edit_text_auth));

        // Create start icon container
        LinearLayout startIconContainer = new LinearLayout(context);
        startIconContainer.setOrientation(HORIZONTAL);
        startIconContainer.setGravity(Gravity.CENTER_VERTICAL);
        LayoutParams startContainerParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT
        );
        startContainerParams.setMargins(dpToPx(16), 0, 0, 0);
        addView(startIconContainer, startContainerParams);

        // Create start icon ImageView
        ImageView startIconView = new ImageView(context);
        LayoutParams startIconParams = new LayoutParams(dpToPx(24), dpToPx(24));
        startIconParams.gravity = Gravity.CENTER_VERTICAL;
        startIconContainer.addView(startIconView, startIconParams);

        AppCompatEditText editText = new AppCompatEditText(context, attrs);
        editText.setMaxLines(1);
        editText.setMaxEms(10);
        editText.setEllipsize(TextUtils.TruncateAt.END);
        editText.setTextColor(ContextCompat.getColor(context, R.color.color_content_secondary));
        editText.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            setActivated(hasFocus);
        });
        LayoutParams editTextParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
        editTextParams.setMargins(0, 0, 0, 0);
        addView(editText, editTextParams);

        LinearLayout endTextContainer = new LinearLayout(context);
        endTextContainer.setOrientation(HORIZONTAL);
        endTextContainer.setGravity(Gravity.CENTER_VERTICAL);
        LayoutParams containerParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT
        );
        containerParams.setMargins(dpToPx(16), 0, dpToPx(16), 0);
        addView(endTextContainer, containerParams);

        View dividerView = new View(context);
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                dpToPx(1),
                dpToPx(24)
        );
        dividerParams.setMarginEnd(dpToPx(16));
        dividerView.setBackgroundColor(Color.LTGRAY);
        endTextContainer.addView(dividerView, dividerParams);

        TextView endTextView = new TextView(context);
        LayoutParams endTextViewParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        endTextViewParams.gravity = Gravity.CENTER_VERTICAL;
        endTextView.setClickable(true);
        endTextView.setFocusable(true);
        endTextView.setTextColor(ContextCompat.getColor(context, R.color.color_content_secondary));
        endTextView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ripple_text));
        endTextContainer.addView(endTextView, endTextViewParams);

        endTextView.setOnClickListener(v -> {
            if (endTextClickListener != null) {
                endTextClickListener.onClick(v);
            }
        });

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.EndTextEditText);

            // Handle start icon drawable
            Drawable startIcon = a.getDrawable(R.styleable.EndTextEditText_startIconDrawable);
            if (startIcon != null) {
                startIconView.setImageDrawable(startIcon);
                startIconContainer.setVisibility(VISIBLE);
            } else {
                startIconContainer.setVisibility(GONE);
            }

            String endText = a.getString(R.styleable.EndTextEditText_endText);
            String hint = a.getString(R.styleable.EndTextEditText_android_hint);
            editText.setHint(hint);
            editText.setHintTextColor(a.getColor(R.styleable.EndTextEditText_hintTextColor, Color.GRAY));
            boolean endTextContainerVisible = a.getBoolean(R.styleable.EndTextEditText_endTextVisible, false);
            if (endTextContainerVisible) {
                endTextContainer.setVisibility(VISIBLE);
            } else {
                endTextContainer.setVisibility(GONE);
            }
            endTextView.setText(endText);
            a.recycle();
        }
    }

    public void setEndTextClickListener(OnClickListener listener) {
        endTextClickListener = listener;
    }

    public String getText() {
        AppCompatEditText editText = (AppCompatEditText) getChildAt(1); // Index changed due to added startIconContainer
        return Objects.requireNonNull(editText.getText()).toString();
    }

    public void setText(String text) {
        AppCompatEditText editText = (AppCompatEditText) getChildAt(1); // Index changed
        editText.setText(text);
    }

    public void setHint(String hint) {
        AppCompatEditText editText = (AppCompatEditText) getChildAt(1); // Index changed
        editText.setHint(hint);
    }

    // Method to set start icon programmatically
    public void setStartIconDrawable(Drawable drawable) {
        LinearLayout startIconContainer = (LinearLayout) getChildAt(0);
        ImageView startIconView = (ImageView) startIconContainer.getChildAt(0);

        if (drawable != null) {
            startIconView.setImageDrawable(drawable);
            startIconContainer.setVisibility(VISIBLE);
        } else {
            startIconContainer.setVisibility(GONE);
        }
    }

    // Method to set start icon tint color programmatically
    public void setStartIconTint(int color) {
        LinearLayout startIconContainer = (LinearLayout) getChildAt(0);
        ImageView startIconView = (ImageView) startIconContainer.getChildAt(0);
        startIconView.setColorFilter(color);
    }

    private int dpToPx(float dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }

    public interface OnTextChangedListener {
        void onTextChanged(String text);
    }

    public void setEndTextVisible(boolean visible) {
        LinearLayout endTextContainer = (LinearLayout) getChildAt(2); // Index changed
        if (visible) {
            endTextContainer.setVisibility(VISIBLE);
        } else {
            endTextContainer.setVisibility(GONE);
        }
    }

    public void setStartIconVisible(boolean visible) {
        LinearLayout startIconContainer = (LinearLayout) getChildAt(0);
        if (visible) {
            startIconContainer.setVisibility(VISIBLE);
        } else {
            startIconContainer.setVisibility(GONE);
        }
    }

    public interface OnActivatedListener {
        void onActivated(boolean activated);
    }

    public void setOnActivatedListener(final EndTextEditText.OnActivatedListener onActivatedListener) {
        AppCompatEditText editText = (AppCompatEditText) getChildAt(1); // Index changed
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            onActivatedListener.onActivated(hasFocus);
        });
    }

    public void setOnTextChangedListener(final EndIconEditText.OnTextChangedListener onTextChangedListener) {
        AppCompatEditText editText = (AppCompatEditText) getChildAt(1); // Index changed
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
                onTextChangedListener.onTextChanged(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }
}