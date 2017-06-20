package com.project.golovolomki.Alert;

        import android.app.Activity;
        import android.support.annotation.ColorRes;
        import android.support.annotation.DrawableRes;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.annotation.StringRes;
        import android.support.v4.content.ContextCompat;
        import android.util.Log;
        import android.view.View;
        import android.view.ViewGroup;

        import com.project.golovolomki.R;

        import java.lang.ref.WeakReference;

public final class Alerter {

    private static WeakReference<Activity> activityWeakReference;

    private Alert alert;



    private Alerter() {
    }


    public static Alerter create(@NonNull final Activity activity) {
        if (activity == null) {
            throw new IllegalArgumentException("Activity cannot be null!");
        }

        final Alerter alerter = new Alerter();

        //Clear Current Alert, if one is Active
        Alerter.clearCurrent(activity);

        alerter.setActivity(activity);
        alerter.setAlert(new Alert(activity));

        return alerter;
    }


    private static void clearCurrent(@NonNull final Activity activity) {
        if (activity == null) {
            return;
        }
        try {
            final View alertView = activity.getWindow().getDecorView().findViewById(R.id.flAlertBackground);
            //Check if the Alert is added to the Window
            if (alertView == null || alertView.getWindowToken() == null) {
                Log.d(Alerter.class.getClass().getSimpleName(), activity.getString(R.string.msg_no_alert_showing));
            } else {
                //Animate the Alpha
                alertView.animate().alpha(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        //And remove the view for the parent layout
                        ((ViewGroup) alertView.getParent()).removeView(alertView);
                    }
                }).start();

                Log.d(Alerter.class.getClass().getSimpleName(), activity.getString(R.string.msg_alert_cleared));
            }
        } catch (Exception ex) {
            Log.e(Alerter.class.getClass().getSimpleName(), Log.getStackTraceString(ex));
        }
    }


    public Alert show() {
        //This will get the Activity Window's DecorView
        if (getActivityWeakReference() != null) {
            getActivityWeakReference().get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Add the new Alert to the View Hierarchy
                    final ViewGroup decorView = Alerter.this.getActivityDecorView();
                    if (decorView != null && Alerter.this.getAlert().getParent() == null) {
                        decorView.addView(Alerter.this.getAlert());
                    }
                }
            });
        }

        return getAlert();
    }


    public Alerter setTitle(@StringRes final int titleId) {
        if (getAlert() != null) {
            getAlert().setTitle(titleId);
        }

        return this;
    }


    public Alerter setTitle(final String title) {
        if (getAlert() != null) {
            getAlert().setTitle(title);
        }

        return this;
    }


    public Alerter setText(@StringRes final int textId) {
        if (getAlert() != null) {
            getAlert().setText(textId);
        }

        return this;
    }


    public Alerter setText(final String text) {
        if (getAlert() != null) {
            getAlert().setText(text);
        }

        return this;
    }


    public Alerter setBackgroundColor(@ColorRes final int colorResId) {
        if (getAlert() != null && getActivityWeakReference() != null) {
            getAlert().setAlertBackgroundColor(ContextCompat.getColor(getActivityWeakReference().get(), colorResId));
        }

        return this;
    }


    public Alerter setIcon(@DrawableRes final int iconId) {
        if (getAlert() != null) {
            getAlert().setIcon(iconId);
        }

        return this;
    }

    public Alerter setOnClickListener(@NonNull final View.OnClickListener onClickListener) {
        if (getAlert() != null) {
            getAlert().setOnClickListener(onClickListener);
        }

        return this;
    }


    public Alerter setDuration(@NonNull final long milliseconds) {
        if (getAlert() != null) {
            getAlert().setDuration(milliseconds);
        }
        return this;
    }





    public static boolean getStateShowing() {
        return Alert.isShowing;
    }


    private Alert getAlert() {
        return alert;
    }


    private void setAlert(final Alert alert) {
        this.alert = alert;
    }

    @Nullable
    private WeakReference<Activity> getActivityWeakReference() {
        return activityWeakReference;
    }

    @Nullable
    private ViewGroup getActivityDecorView() {
        ViewGroup decorView = null;

        if (getActivityWeakReference() != null && getActivityWeakReference().get() != null) {
            decorView = (ViewGroup) getActivityWeakReference().get().getWindow().getDecorView();
        }

        return decorView;
    }


    private void setActivity(@NonNull final Activity activity) {
        activityWeakReference = new WeakReference<>(activity);
    }
}
