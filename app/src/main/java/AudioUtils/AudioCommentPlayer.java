package AudioUtils; /**
 * Created by Delio on 12/12/15.
 */
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.content.Intent;
import android.util.Log;

import Utils.FileManager;
import Utils.ServerConnection;
import br.ufpe.cin.vocalium.R;

import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import Utils.LayoutOutput;
import br.ufpe.cin.vocalium.TutorChecklist;

public class AudioCommentPlayer implements Runnable{
    private MediaPlayer mediaPlayer;
    private Context context;
    private int videoLength;
    private AudioComment comments;
    private final AudioCommentPlayer audioCommentPlayer;
    private final Activity activity;

    public AudioCommentPlayer(Context cntxt, String audioPath, AudioComment cmmts, Activity activity) throws IOException {
        comments = cmmts;
        audioCommentPlayer = this;
        this.activity = activity;

        //this one is temporary
        audioPath = context.getFilesDir() + "/1" + FileManager.getExtension(ServerConnection.FileType.Sound);

        Uri audioPathUri = Uri.parse(audioPath);

        context = cntxt;
        mediaPlayer = MediaPlayer.create(context, audioPathUri);              //create player from file audiopath
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.start();

        (new Thread(audioCommentPlayer)).start();         //starts the comment management

        videoLength = mediaPlayer.getDuration();

        //after the video has completed, call this function
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {@Override public void onCompletion(MediaPlayer arg0) {
            Release();
            Intent tutorChecklist = new Intent(context,TutorChecklist.class);
            tutorChecklist.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     //needs to call another activity from outside the current one
            context.startActivity(tutorChecklist);}});
    }
    public void Release ()
    {
        mediaPlayer.release();
    }

    public void start ()
    {
        //can only play if is not playing already
        if(!mediaPlayer.isPlaying())
        {
            mediaPlayer.start();
        }
    }
    public void pause ()
    {
        //only pause if it's playing
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
        }
    }

    private Calendar ConvertMilisToCalendar (int milis)
    {
        Calendar currentTime = Calendar.getInstance();
        currentTime.setTimeInMillis(milis);

        return currentTime;
    }
    private int CalendarIsGreater (Calendar date1, Calendar date2)
    {
        if(date1.get(Calendar.HOUR_OF_DAY) > date2.get(Calendar.HOUR_OF_DAY)) return 1;
        else if (date1.get(Calendar.HOUR_OF_DAY) < date2.get(Calendar.HOUR_OF_DAY)) return -1;
        else if (date1.get(Calendar.MINUTE) > date2.get(Calendar.MINUTE)) return 1;
        else if (date1.get(Calendar.MINUTE) < date2.get(Calendar.MINUTE)) return -1;
        else if (date1.get(Calendar.SECOND) > date2.get(Calendar.SECOND)) return 1;
        else if (date1.get(Calendar.SECOND) < date2.get(Calendar.SECOND)) return -1;
        else return 0;
    }

    @Override
    public void run() {
        /*REESCREVER ISSO, MUDEI A ESTRUTURA!!!
        int commentNumber = 0;
        Log.i("AUDIO_DEBUG", "" + comments.size() + " / " + comments.elementAt(0).GetCommentTime().getTimeInMillis() + " / " + videoLength);
        while(commentNumber < comments.size() && comments.elementAt(commentNumber).GetCommentTime().getTimeInMillis() < videoLength)
        {
            Log.i("AUDIO_DEBUG", "" + comments.size() + " / " + comments.elementAt(commentNumber).GetCommentTime().getTimeInMillis() + " / " + videoLength + " / " + mediaPlayer.getCurrentPosition());
            if(comments.elementAt(commentNumber).GetCommentTime().getTimeInMillis() < mediaPlayer.getCurrentPosition())
            {
                RunUiAction runUiAction = new RunUiAction(commentNumber);
                activity.runOnUiThread(new Thread(runUiAction));

                commentNumber ++;
            }
        }
        */
    }
    public void SetComment(int id)
    {
        LayoutOutput.getInstance().ChangeStudentCommentText(comments.getCommentText(id), activity);
    }


    //this class is only to change ui stuff inside thread, otherwise it's forbidden
    class RunUiAction implements Runnable {
        final int commentId;

        public RunUiAction (int id)
        {
            commentId = id;
        }

        @Override
        public void run() {
            SetComment(commentId);
        }
    }
}
