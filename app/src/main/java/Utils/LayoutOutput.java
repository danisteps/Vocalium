package Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import AudioUtils.AudioPlayerManager;
import br.ufpe.cin.vocalium.R;

/**
 * Created by Délio on 12/12/2015.
 */
public class LayoutOutput {
    private static LayoutOutput instance = new LayoutOutput();

    public static LayoutOutput getInstance() {
        return instance;
    }

    private LayoutOutput() {
    }

    //-----------------AudioCommentPlayer--------------------
    public static void ChangeStudentCommentText (String text, Activity activity) {
        TextView textView = (TextView)activity.findViewById(R.id.comment_show_text_student);        ///find e r?
        textView.setText(text);
    }
    //----------------TutorHearComment-----------------------
    public static void UpdateTutorHearComment (MediaPlayer mediaPlayer, Activity activity)
    {
        ProgressBar bar = (ProgressBar) activity.findViewById(R.id.tutor_hear_comment_progress_bar);
        bar.setProgress((int)AudioPlayerManager.getMediaCurrentPercentage(mediaPlayer)*10);

        int currentTime = mediaPlayer.getCurrentPosition();
        int seconds = (int)(currentTime / 1000) % 60;
        int minutes = (int)(currentTime / 60000);

        Chronometer currentChronometer = (Chronometer) activity.findViewById(R.id.audio_current_time_chronometer);
        currentChronometer.setText(String.format("%02d:%02d", minutes, seconds));
    }
    public static void ChangeEndTimeChronometer(int miliseconds, Activity activity)
    {
        int seconds = (int)(miliseconds / 1000) % 60;
        int minutes = (int)(miliseconds / 60000);

        Chronometer currentChronometer = (Chronometer) activity.findViewById(R.id.audio_end_time_chronometer);
        currentChronometer.setText(String.format("%02d:%02d", minutes, seconds));
    }
    public static EditText enableCommentView(Activity activity)
    {
        EditText commentView = new EditText(activity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        layoutParams.setMargins(40, 10, 40, 50);
        commentView.setLayoutParams(layoutParams);

        commentView.setTextColor(Color.parseColor("#ffffff"));
        commentView.setGravity(Gravity.TOP);

        LinearLayout insertionPoint = (LinearLayout) activity.findViewById(R.id.layout_text_field_insertion);
        insertionPoint.addView(commentView);


        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            commentView.setBackground(activity.getDrawable( R.drawable.rounded_corners_blue));
        } else {
            commentView.setBackground(activity.getResources().getDrawable(R.drawable.rounded_corners_blue));
        }

        return commentView;
    }
    public static void disableCommentView(Activity activity)
    {
        View view = activity.getCurrentFocus();

        if(view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        LinearLayout insertionPoint = (LinearLayout) activity.findViewById(R.id.layout_text_field_insertion);
        insertionPoint.removeAllViews();

        insertionPoint.setBackground(null);
    }
    public static Button showSendButton(Activity activity)
    {
        Button sendButton = new Button(activity);
        LinearLayout insertionPoint = (LinearLayout) activity.findViewById(R.id.layout_text_field_insertion);


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        layoutParams.setMargins(30, 20, 30, 0);
        //layoutParams
        sendButton.setText("Enviar");
        sendButton.setTextColor(Color.parseColor("#ffffff"));
        sendButton.setAllCaps(false);
        sendButton.setLayoutParams(layoutParams);
        sendButton.setPadding(0, 15, 0, 15);
        sendButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP * 50);


        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            sendButton.setBackground(activity.getDrawable( R.drawable.rounded_corners_orange));
        } else {
            sendButton.setBackground(activity.getResources().getDrawable(R.drawable.rounded_corners_orange));
        }

        sendButton.setLayoutParams(layoutParams);

        insertionPoint.addView(sendButton);

        return sendButton;
    }
    public static void hideSendButton(Activity activity)
    {
        LinearLayout insertionPoint = (LinearLayout) activity.findViewById(R.id.layout_text_field_insertion);
        insertionPoint.removeAllViews();
    }

    //---------------------------------------Student listen Comment------------------
    public static void changeEndTimeChronometerStudent(int miliseconds, Activity activity)
    {
        int seconds = (int)(miliseconds / 1000) % 60;
        int minutes = (int)(miliseconds / 60000);

        Chronometer currentChronometer = (Chronometer) activity.findViewById(R.id.audio_end_time_chronometer_student);
        currentChronometer.setText(String.format("%02d:%02d", minutes, seconds));
    }
    public static void changeCommentText(String text, Activity activity)
    {
        Log.e("COMMENT_ERROR", "changing comment");
        TextView commentView = (TextView) activity.findViewById(R.id.comment_show_text_student);
        commentView.setTextColor(Color.parseColor("#ffffff"));
        commentView.setText(text);
    }
    public static void updateStudentListenComment (MediaPlayer mediaPlayer, Activity activity)
    {
        ProgressBar bar = (ProgressBar) activity.findViewById(R.id.student_hear_comment_progress_bar);
        bar.setProgress((int)AudioPlayerManager.getMediaCurrentPercentage(mediaPlayer)*10);

        int currentTime = mediaPlayer.getCurrentPosition();
        int seconds = (int)(currentTime / 1000) % 60;
        int minutes = (int)(currentTime / 60000);

        Chronometer currentChronometer = (Chronometer) activity.findViewById(R.id.audio_current_time_chronometer_student);
        currentChronometer.setText(String.format("%02d:%02d", minutes, seconds));
    }

    public static void showErrorDialog(Context context, String error) {
        final AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
        alertbox.setMessage(error);
        final AlertDialog alert = alertbox.create();
        alert.show();

        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alert.isShowing()) {
                    alert.dismiss();
                }
            }
        };

        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 3000);
    }
}
