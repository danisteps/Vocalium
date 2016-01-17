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
import br.ufpe.cin.vocalium.StudentListenComment;
import br.ufpe.cin.vocalium.TutorChecklist;
import br.ufpe.cin.vocalium.TutorHearComm;

public class AudioPlayerManager {
    private MediaPlayer mediaPlayer;
    private Context context;
    private Calendar videoLength;
    private AudioComment comments;
    private int commentPosition;
    private Activity activity;
    private boolean released;
    private boolean inUse;

    private boolean showNextMessage = false;
    private String nextComment = "";

    private UpdateType updateType;

    public AudioPlayerManager(Context cntxt, String audioPath) throws IOException {
        comments = new AudioComment(UserInformation.getInstance().GetAudioId());

        Uri audioPathUri = Uri.parse(audioPath);

        context = cntxt;
        mediaPlayer = MediaPlayer.create(context, audioPathUri);              //create player from file audiopath
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        start();
        released = false;
        inUse = true;

        videoLength = ConvertMilisToCalendar(mediaPlayer.getDuration());



    }
    public void Release ()
    {
        released = true;
        while (inUse);              //wait for mediaPlayer to be freed, it wont take long so there is no problem in a while
        mediaPlayer.release();
    }

    public void setCompletionListenerTutorHearComment(final TutorHearComm activity)
    {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer arg0) {
                activity.changeActivity();
            }
        });
    }
    public void setCompletionListenerStudentListenComment(final StudentListenComment activity)
    {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer arg0) {
                activity.changeActivity();
            }
        });
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
    public void addComment (String text)
    {
        //make sure it's not playing
        if(!mediaPlayer.isPlaying())
        {
            //the current the of the video is given in miliseconds, so we have to convert
            int currentTime = mediaPlayer.getCurrentPosition();

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
    public boolean isPlaying()
    {
        return mediaPlayer.isPlaying();
    }
    private void update()
    {
        if(updateType == UpdateType.TutorHearComment)
        {
            updateTutorHearComment();
        }
        else if(updateType == UpdateType.StudentListenComment)
        {
            Log.e("COMMENT_ERROR", "updating student");
            updateStudentListenComment();
        }
    }
    public void startUpdate(Activity activity, UpdateType type)
    {
        Log.e("COMMENT_ERROR", "update setted to: " + type.name());
        this.activity = activity;
        updateType = type;
        update();
    }
    public AudioComment getComments ()
    {
        return comments;
    }
    public void enqueueComments(AudioComment comment)
    {
        this.comments = comment;
    }



    private Calendar ConvertMilisToCalendar (int milis)
    {
        Calendar currentTime = Calendar.getInstance();
        currentTime.setTimeInMillis(milis);

        return currentTime;
    }
    public static float getMediaCurrentPercentage(MediaPlayer mediaPlayer)
    {
        int percentage = (int)(mediaPlayer.getCurrentPosition() * 100 / mediaPlayer.getDuration());

        return percentage;
    }


    //------------------Class for update functions-------------
    public enum UpdateType
    {
        TutorHearComment,
        StudentListenComment
    }

    public void updateTutorHearComment()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!released && mediaPlayer.isPlaying())
                {
                    inUse = true;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LayoutOutput.UpdateTutorHearComment(mediaPlayer, activity);
                        }
                    });
                    inUse = false;
                    try {
                        Thread.sleep(900);
                    } catch (InterruptedException e) {
                        Log.e("UPDATE_ERROR", "thread stopped");
                    }
                }
            }
        }).start();
    }
    public void updateStudentListenComment ()
    {
        Log.e("COMMENT_ERROR", "starting update thread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!released && mediaPlayer.isPlaying())
                {
                    inUse = true;
                    getNextComment();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LayoutOutput.updateStudentListenComment(mediaPlayer, activity);
                            if(showNextMessage)
                            {
                                showNextMessage = false;
                                LayoutOutput.changeCommentText(nextComment, activity);
                            }
                        }
                    });
                    inUse = false;
                    try {
                        Thread.sleep(900);
                    } catch (InterruptedException e) {
                        Log.e("UPDATE_ERROR", "thread stopped");
                    }
                }
            }
        }).start();
    }
    private void getNextComment()
    {
        if(commentPosition < comments.size())
        {
            int milisec = comments.getCommentTime(commentPosition);
            if(mediaPlayer.getCurrentPosition() > milisec)
            {
                int seconds = (int)(milisec / 1000) % 60;
                int minutes = (int)(milisec / 60000);
                nextComment = String.format("%02d:%02d", minutes, seconds);
                nextComment = nextComment + "   " + comments.getCommentText(commentPosition);
                showNextMessage = true;

                commentPosition ++;
            }
        }
    }
}
