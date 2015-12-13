package AudioUtils; /**
 * Created by Delio on 12/12/15.
 */
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.content.Intent;

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
    private Calendar videoLength;
    private Vector<AudioComment> comments;
    private final AudioCommentPlayer audioCommentPlayer;

    public AudioCommentPlayer(Context cntxt, String audioPath, Vector<AudioComment> cmmts) throws IOException {
        comments = cmmts;
        audioCommentPlayer = this;

        Uri audioPathUri = Uri.parse("R.raw.surpass");
        //Uri audioPathUri = Uri.parse(audioPath);

        context = cntxt;
        mediaPlayer = MediaPlayer.create(context, audioPathUri);              //create player from file audiopath
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.prepareAsync();

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer player) {
                player.start();                                 //starts the media player
                new Thread(audioCommentPlayer).start();         //starts the comment management
            }
        });

        videoLength = ConvertMilisToCalendar(mediaPlayer.getDuration());

        /*mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {@Override
                                                                                    public void onCompletion(MediaPlayer arg0) {
            Intent tutorChecklist = new Intent(context,TutorChecklist.class);
            context.startActivity(tutorChecklist);}});*/
    }
    public void Release ()
    {
        mediaPlayer.release();
    }

    public void PlayVideo ()
    {
        //can only play if is not playing already
        if(!mediaPlayer.isPlaying())
        {
            mediaPlayer.start();
        }
    }
    public void PauseVideo ()
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
        int commentNumber = 0;
        while(commentNumber < comments.size() && comments.elementAt(commentNumber).GetCommentTime().getTimeInMillis() < mediaPlayer.getDuration())
        {
            if(comments.elementAt(commentNumber).GetCommentTime().getTimeInMillis() > mediaPlayer.getCurrentPosition())
            {
                SetComment(commentNumber);
                commentNumber ++;
            }
        }
    }
    private void SetComment(int id)
    {
        LayoutOutput.getInstance().comment = comments.elementAt(id).GetCommentText();
    }
}
