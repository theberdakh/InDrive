package com.aralhub.ui.components;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aralhub.ui.R;

public class ProgressiveButton extends View {
    private Paint buttonPaint;
    private Paint textPaint;
    private Paint backgroundPaint;
    private RectF buttonRect;
    private Path clipPath;
    private String buttonText = "Accept";
    private int buttonColor = 0xFF9ED4B5; // Light green/mint color
    private float fillProgress = 0f;
    private ValueAnimator fillAnimator;

    public ProgressiveButton(Context context) {
        super(context);
        init(null);
    }

    public ProgressiveButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ProgressiveButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        buttonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        buttonPaint.setStyle(Paint.Style.FILL);
        buttonPaint.setColor(buttonColor);

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setStyle(Paint.Style.FILL);


        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);

        buttonRect = new RectF();
        clipPath = new Path();
        setupFillAnimation();

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressiveButton);
        setButtonText(a.getString(R.styleable.ProgressiveButton_progressiveButtonText));
        setButtonColor(a.getColor(R.styleable.ProgressiveButton_progressColor, 0));
        textPaint.setTextSize(a.getDimension(R.styleable.ProgressiveButton_textSize, 40f));
        textPaint.setColor(a.getColor(R.styleable.ProgressiveButton_textColor, 0xFF000000)); //Black text
        backgroundPaint.setColor(a.getColor(R.styleable.ProgressiveButton_buttonColor, 0xFFEEEEEE)); // Light gray background
        a.recycle();

        setClickable(true);
        setupRipple();

    }


    private void setupRipple() {
        post(() -> {
            float cornerRadius = getHeight() / 4f;
            float[] outerRadii = new float[8];
            for (int i = 0; i < 8; i++) {
                outerRadii[i] = cornerRadius;
            }

            RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);
            ShapeDrawable maskDrawable = new ShapeDrawable(roundRectShape);

            // Create ripple drawable with default ripple color
            RippleDrawable rippleDrawable = new RippleDrawable(
                    ColorStateList.valueOf(0x40007FFF), // Ripple color (light blue)
                    null, // Content drawable (we'll draw our own content)
                    maskDrawable // Mask drawable
            );

            setForeground(rippleDrawable);
        });

    }

    private void setupFillAnimation() {
        fillAnimator = ValueAnimator.ofFloat(0f, 1f);
        fillAnimator.setDuration(500); // 500ms duration
        fillAnimator.setInterpolator(new LinearInterpolator());
        fillAnimator.addUpdateListener(animation -> {
            fillProgress = (float) animation.getAnimatedValue();
            invalidate();
        });
        fillAnimator.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        buttonRect.set(0, 0, w, h);

        // Update clip path for rounded corners
        float cornerRadius = h / 4f;
        clipPath.reset();
        clipPath.addRoundRect(buttonRect, cornerRadius, cornerRadius, Path.Direction.CW);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        // Save canvas state before clipping
        canvas.save();

        // Apply rounded corners clipping
        canvas.clipPath(clipPath);

        // Draw background (gray)
        canvas.drawRect(buttonRect, backgroundPaint);

        // Draw filled portion
        float fillWidth = buttonRect.width() * fillProgress;
        RectF fillRect = new RectF(buttonRect.left, buttonRect.top,
                buttonRect.left + fillWidth, buttonRect.bottom);
        canvas.drawRect(fillRect, buttonPaint);

        // Restore canvas to remove clipping
        canvas.restore();

        // Draw centered text
        float textX = getWidth() / 2f;
        float textY = getHeight() / 2f - ((textPaint.descent() + textPaint.ascent()) / 2f);
        canvas.drawText(buttonText, textX, textY, textPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = 300;
        int desiredHeight = 100;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }

        setMeasuredDimension(width, height);
    }

    // Public methods to control the animation
    public void startFillAnimation() {
        fillAnimator.start();
    }

    public void resetFillAnimation() {
        fillAnimator.cancel();
        fillProgress = 0f;
        invalidate();
    }

    // Setter methods for customization
    public void setButtonText(String text) {
        this.buttonText = text;
        invalidate();
    }

    public void setButtonColor(int color) {
        this.buttonColor = color;
        buttonPaint.setColor(color);
        invalidate();
    }

    public void setTextSize(float size) {
        textPaint.setTextSize(size);
        invalidate();
    }

    public void setTextColor(int color) {
        textPaint.setColor(color);
        invalidate();
    }

    public void setAnimationDuration(long duration) {
        fillAnimator.setDuration(duration);
    }
}