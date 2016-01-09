package Utils;
import android.app.Activity;
import android.media.MediaPlayer;
import android.widget.Chronometer;
import android.widget.ProgressBar;
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
        TextView textView = (TextView)activity.findViewById(R.id.studentCommentText);        ///find e r?
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


}
