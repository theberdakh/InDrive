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
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aralhub.ui.R;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;

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

    // Expiration related fields
    private String expirationTimeString;
    private Instant expirationTime;
    private ValueAnimator countdownAnimator;
    private boolean showExpiration = false;
    private boolean hasExpired = false;

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
        textPaint.setTextSize(a.getDimension(R.styleable.ProgressiveButton_textSize, 30f));
        textPaint.setColor(a.getColor(R.styleable.ProgressiveButton_textColor, 0xFF000000)); // Black text
        backgroundPaint.setColor(a.getColor(R.styleable.ProgressiveButton_buttonColor, 0xFFEEEEEE)); // Light gray background
        a.recycle();

        setClickable(true);
        setupRipple();
    }

    private void setupRipple() {
        post(() -> {
            float cornerRadius = getHeight() / 3f;
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
    }

    // Add this new method for the reverse animation
    private void setupReverseFillAnimation() {
        if (fillAnimator != null) {
            fillAnimator.cancel();
        }

        fillAnimator = ValueAnimator.ofFloat(1f, 0f); // Reverse direction from 1 to 0
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
        float cornerRadius = h / 3f; // Match with setupRipple for consistency
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

        if (showExpiration) {
            // Draw the remaining time text
            canvas.drawText(getRemainingTimeText(), textX, textY, textPaint);
        } else {
            // Draw the normal button text
            canvas.drawText(buttonText, textX, textY, textPaint);
        }
    }

    // Method to calculate and display remaining time
    private String getRemainingTimeText() {
        Instant now = Instant.now();

        // If already expired
        if (now.isAfter(expirationTime)) {
            hasExpired = true;
            return "Qabıllanbadı";
        }

        // Rest of the method remains unchanged
        long remainingMillis = expirationTime.toEpochMilli() - now.toEpochMilli();
        long seconds = remainingMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        seconds = seconds % 60;
        minutes = minutes % 60;

        // Format time remaining
        if (hours > 0) {
            return String.format("Qabıllaw (%dh %02dm)", hours, minutes);
        } else if (minutes > 0) {
            return String.format("Qabıllaw (%dm %02ds)", minutes, seconds);
        } else {
            return String.format("Qabıllaw (%ds)", seconds);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = 300;
        int desiredHeight = 80;

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
        setupFillAnimation();
        fillAnimator.start();
    }

    public void resetFillAnimation() {
        if (fillAnimator != null) {
            fillAnimator.cancel();
        }
        fillProgress = 0f;
        invalidate();
    }

    // Setter methods for customization
    public void setButtonText(String text) {
        if (text != null) {
            this.buttonText = text;
            invalidate();
        }
    }

    public void setButtonColor(int color) {
        if (color != 0) {
            this.buttonColor = color;
            buttonPaint.setColor(color);
            invalidate();
        }
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
        if (fillAnimator != null) {
            fillAnimator.setDuration(duration);
        }
    }

    private Instant startTime; // Add this field to the class

    public void setExpirationTime(String expirationTimeString) {
        this.expirationTimeString = expirationTimeString;
        this.expirationTime = ZonedDateTime.parse(expirationTimeString).toInstant();
        this.startTime = Instant.now(); // Record when we started the countdown
        Log.i("ProgressiveButton", "Expiration time set to: " + expirationTimeString);
        this.showExpiration = true;
        this.hasExpired = false;

        // Start with full progress
        fillProgress = 1f;

        // Set up countdown animation
        setupCountdownAnimation();

        // Start the countdown
        countdownAnimator.start();

        // Invalidate to update the view
        invalidate();
    }

    private void updateFillProgressBasedOnTime() {
        Instant now = Instant.now();

        // If already expired
        if (now.isAfter(expirationTime)) {
            fillProgress = 0f;
            hasExpired = true;
            return;
        }

        // Calculate total duration and elapsed time
        long totalDuration = expirationTime.toEpochMilli() - startTime.toEpochMilli();
        long remainingTime = expirationTime.toEpochMilli() - now.toEpochMilli();

        // Calculate progress as remaining time / total time
        if (totalDuration > 0) {
            fillProgress = (float) remainingTime / totalDuration;
            // Ensure progress is between 0 and 1
            fillProgress = Math.max(0f, Math.min(1f, fillProgress));
        } else {
            fillProgress = 0f;
        }
    }

    private void setupCountdownAnimation() {
        // Cancel existing animator if it exists
        if (countdownAnimator != null) {
            countdownAnimator.cancel();
        }

        countdownAnimator = ValueAnimator.ofInt(0, 1000);
        countdownAnimator.setDuration(1000); // Update every second
        countdownAnimator.setRepeatCount(ValueAnimator.INFINITE);
        countdownAnimator.setInterpolator(new LinearInterpolator());
        countdownAnimator.addUpdateListener(animation -> {
            // Calculate remaining time percentage and update fill progress
            updateFillProgressBasedOnTime();

            // Invalidate to redraw
            invalidate();
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // Clean up all animations when the view is detached
        if (countdownAnimator != null) {
            countdownAnimator.cancel();
        }
        if (fillAnimator != null) {
            fillAnimator.cancel();
        }
    }
}