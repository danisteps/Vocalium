package AudioUtils; /**
 * Created by Delio on 12/12/15.
 */
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;
import java.util.Calendar;
import java.util.Vector;

public class AudioPlayerManager {
    private MediaPlayer mediaPlayer;
    private Context context;
    private Calendar videoLength;
    private Vector<AudioComment> comments;

    public AudioPlayerManager(Context cntxt, String audioPath) throws IOException {
        comments = new Vector<AudioComment>();

        Uri audioPathUri = Uri.parse(audioPath);

        context = cntxt;
        mediaPlayer = MediaPlayer.create(context, audioPathUri);              //create player from file audiopath
        mediaPlayer.setAudioStreamType(AudioPlayerManager.STREAM_MUSIC);
        mediaPlayer.prepare();
        mediaPlayer.start();

        videoLength = ConvertMilisToCalendar(mediaPlayer.getDuration());
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
    public void AddComment (String text)
    {
        //the current the of the video is given in miliseconds, so we have to convert
        Calendar currentTime = ConvertMilisToCalendar(mediaPlayer.getCurrentPosition());

        AudioComment newComment = new AudioComment(currentTime, text);

        comments.add(newComment);
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
}