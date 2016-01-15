package Utils;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        commentView.setLayoutParams(layoutParams);
        commentView.setPadding(10, 6, 10, 7);

        commentView.setGravity(Gravity.TOP);

        RelativeLayout insertionPoint = (RelativeLayout) activity.findViewById(R.id.layout_text_field_insertion);
        insertionPoint.addView(commentView);


        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            insertionPoint.setBackground(activity.getDrawable( R.drawable.rounded_corners));
        } else {
            insertionPoint.setBackground(activity.getResources().getDrawable(R.drawable.rounded_corners));
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

        RelativeLayout insertionPoint = (RelativeLayout) activity.findViewById(R.id.layout_text_field_insertion);
        insertionPoint.removeAllViews();

        insertionPoint.setBackground(null);
    }
    public static ImageButton showSendButton(Activity activity)
    {
        ImageButton sendButton = new ImageButton(activity);
        RelativeLayout insertionPoint = (RelativeLayout) activity.findViewById(R.id.layout_text_field_insertion);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);


        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            sendButton.setBackground(activity.getDrawable( R.drawable.send_button_small));
        } else {
            sendButton.setBackground(activity.getResources().getDrawable(R.drawable.send_button_small));
        }

        sendButton.setLayoutParams(layoutParams);

        insertionPoint.addView(sendButton);

        return sendButton;
    }
    public static void hideSendButton(Activity activity)
    {
        RelativeLayout insertionPoint = (RelativeLayout) activity.findViewById(R.id.layout_text_field_insertion);
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



}
