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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;

import Utils.LayoutOutput;
import Utils.UserInformation;
import br.ufpe.cin.vocalium.TutorChecklist;
import br.ufpe.cin.vocalium.TutorHearComm;

public class AudioPlayerManager {
    private MediaPlayer mediaPlayer;
    private Context context;
    private Calendar videoLength;
    private AudioComment comments;
    private Activity activity;

    private UpdateType updateType;

    public AudioPlayerManager(Context cntxt, String audioPath) throws IOException {
        comments = new AudioComment(UserInformation.getInstance().GetAudioId());

        Uri audioPathUri = Uri.parse(audioPath);

        context = cntxt;
        mediaPlayer = MediaPlayer.create(context, audioPathUri);              //create player from file audiopath
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        start();

        videoLength = ConvertMilisToCalendar(mediaPlayer.getDuration());

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer arg0) {
                Release();
                Intent tutorChecklist = new Intent(context, TutorChecklist.class);
                context.startActivity(tutorChecklist);
            }
        });

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
            update();
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
    public void AddComment (String text)
    {
        //make sure it's not playing
        if(!mediaPlayer.isPlaying())
        {
            //the current the of the video is given in miliseconds, so we have to convert
            Calendar currentTime = ConvertMilisToCalendar(mediaPlayer.getCurrentPosition());

            comments.addComment(currentTime, text);
        }
    }

    public int getAudioSessionId()
    {
        return mediaPlayer.getAudioSessionId();
    }

    public void seekPercentage(double percentage)
    {
        int newPosition = (int)(mediaPlayer.getDuration() * 100 / percentage);

        mediaPlayer.seekTo(newPosition);
    }
    public int getDuration ()
    {
        return mediaPlayer.getDuration();
    }
    private void update()
    {
        if(updateType == UpdateType.TutorHearComment)
        {
            updateTutorHearComment();
        }
    }
    public void startUpdateTutorHearComment(Activity activity)
    {
        this.activity = activity;
        updateType = UpdateType.TutorHearComment;
        update();
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
    public static float getMediaCurrentPercentage(MediaPlayer mediaPlayer)
    {
        int percentage = (int)(mediaPlayer.getCurrentPosition() * 100 / mediaPlayer.getDuration());

        return percentage;
    }


    //------------------Class for update functions-------------
    public enum UpdateType
    {
        TutorHearComment
    }

    public void updateTutorHearComment()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer.isPlaying())
                {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LayoutOutput.getInstance().UpdateTutorHearComment(mediaPlayer, activity);
                        }
                    });
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Log.e("UPDATE_ERROR", "thread stopped");
                    }
                }
            }
        }).start();
    }
}
