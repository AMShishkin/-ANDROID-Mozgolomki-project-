package com.example.ams.golovolomki.Alert;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.ams.golovolomki.Activities.Typefaces;
import com.example.ams.golovolomki.Assistants.DatabaseHelper;
import com.example.ams.golovolomki.R;

public class Alert extends FrameLayout implements View.OnClickListener, Animation.AnimationListener {

    private static final int CLEAN_UP_DELAY_MILLIS = 100, SCREEN_SCALE_FACTOR = 6;
    private static final long DISPLAY_TIME_IN_SECONDS = 3000;

    //UI
    private FrameLayout flBackground;
    private TextView tvTitle, tvText;
    private ImageView ivIcon;

    private Animation slideInAnimation, slideOutAnimation;

    private long duration = DISPLAY_TIME_IN_SECONDS;

    private boolean marginSet;
    public static boolean isShowing = false;



    public Alert(@NonNull final Context context) {
        super(context, null, R.attr.alertStyle);
        initView();
    }

    public Alert(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs, R.attr.alertStyle);
        initView();
    }

    public Alert(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        inflate(getContext(), R.layout.alerter_alert_view, this);
        setHapticFeedbackEnabled(true);

        flBackground = (FrameLayout)findViewById(R.id.flAlertBackground);

        tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvText = (TextView)findViewById(R.id.tvText);

        flBackground.setOnClickListener(this);


        setAlertBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.darker_gray));

        tvTitle.setTypeface(Typefaces.get(this.getContext(), "fonts/titleItem.ttf"));
        tvText.setTypeface(Typefaces.get(this.getContext(), "fonts/cavia_puzzle.ttf"));

        findViewById(R.id.tvTitle).setTextAlignment(Boolean.valueOf(DatabaseHelper.settingsCursor.getString(9)) ? View.TEXT_ALIGNMENT_CENTER : View.TEXT_ALIGNMENT_VIEW_START);
        findViewById(R.id.tvText).setTextAlignment(Boolean.valueOf(DatabaseHelper.settingsCursor.getString(9)) ? View.TEXT_ALIGNMENT_CENTER : View.TEXT_ALIGNMENT_VIEW_START);

        //Setup Enter Animation
        slideInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.alerter_slide_in_from_top);
        slideOutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.alerter_slide_out_to_top);

        slideInAnimation.setAnimationListener(this);

        //Set Animation to be Run when View is added to Window
        setAnimation(slideInAnimation);

        flBackground.setPadding(
                flBackground.getPaddingLeft(),
                flBackground.getPaddingTop() + (getScreenHeight() / SCREEN_SCALE_FACTOR),
                flBackground.getPaddingRight(),
                flBackground.getPaddingBottom()
        );
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!marginSet) {
            marginSet = true;

            // Add a negative top margin to compensate for overshoot enter animation
            final ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) getLayoutParams();
            params.topMargin = params.topMargin - (getScreenHeight() / SCREEN_SCALE_FACTOR);
            requestLayout();
        }
    }

    // Release resources once view is detached.
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        slideInAnimation.setAnimationListener(null);
    }


    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        performClick();
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(final View v) {
        hide();
    }

    @Override
    public void setOnClickListener(final OnClickListener listener) {
        flBackground.setOnClickListener(listener);
    }

    @Override
    public void setVisibility(final int visibility) {
        super.setVisibility(visibility);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(visibility);
        }
    }

    @Override
    public void onAnimationStart(final Animation animation) {
        if (!isInEditMode()) {
            isShowing = true;
            performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAnimationEnd(final Animation animation) {

        //Start the Handler to clean up the Alert
        postDelayed(new Runnable() {
            @Override
            public void run() {
                hide();
            }
        }, duration);
    }

    @Override
    public void onAnimationRepeat(final Animation animation) {
    }

    public void hide() {
        isShowing = false;

        slideOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(final Animation animation) {
                flBackground.setOnClickListener(null);
                flBackground.setClickable(false);
            }

            @Override
            public void onAnimationEnd(final Animation animation) {
                removeFromParent();
            }

            @Override
            public void onAnimationRepeat(final Animation animation) {
            }
        });

        startAnimation(slideOutAnimation);
    }

    private void removeFromParent() {

        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getParent() != null) ((ViewGroup) getParent()).removeView(Alert.this);
             }
        }, CLEAN_UP_DELAY_MILLIS);
    }

    public void setAlertBackgroundColor(@ColorInt final int color) {
        flBackground.setBackgroundColor(color);
    }

    public void setTitle(@StringRes final int titleId) {
        setTitle(getContext().getString(titleId));
    }

    public void setText(@StringRes final int textId) {
        setText(getContext().getString(textId));
    }

    public FrameLayout getAlertBackground() {
        return flBackground;
    }

    public TextView getTitle() {
        return tvTitle;
    }

    public void setTitle(@NonNull final String title) {
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(VISIBLE);
            tvTitle.setText(title);
        }
    }

    public TextView getText() {
        return tvText;
    }

    public void setText(final String text) {
        if (!TextUtils.isEmpty(text)) {
            tvText.setVisibility(VISIBLE);
            tvText.setText(text);
        }
    }

    public ImageView getIcon() {
        return ivIcon;
    }

    public void setIcon(@DrawableRes final int iconId) {
        final Drawable iconDrawable = ContextCompat.getDrawable(getContext(), iconId);
        iconDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        ivIcon.setImageDrawable(iconDrawable);
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(final long duration) {
        this.duration = duration;
    }



    private int getScreenHeight() {
        final WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        final DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }
}