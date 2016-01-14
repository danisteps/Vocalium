package AudioUtils;

import android.support.v4.util.Pair;
import android.util.Log;
import android.widget.RatingBar;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Vector;

import br.ufpe.cin.vocalium.RatingRowAdapter;

/**
 * Created by Delio on 12/12/15.
 */
public class AudioComment implements Serializable {
    private int audioId;
    private Vector<Integer> commentTime;
    private Vector<String> commentText;
    private Pair<String, Float>[] ratings;

    public AudioComment(int id)
    {
        audioId = id;
        commentTime = new Vector<>();
        commentText = new Vector<>();
    }
    public void addComment(int time, String text)
    {
        int position = findPosition(time);
        commentTime.insertElementAt(time, position);
        commentText.insertElementAt(text, position);
    }
    public void removeAt (int index)
    {
        commentText.removeElementAt(index);
        commentTime.removeElementAt(index);
    }
    public void setRatings(RatingRowAdapter ratings)
    {

    }

    public int getAudioId () { return audioId; }
    public int getCommentTime (int index)
    {
        return commentTime.get(index);
    }
    public String getCommentText (int index)
    {
        return commentText.get(index);
    }
    public Pair <Integer, String> get (int index)
    {
        return new Pair<>(commentTime.get(index), commentText.get(index));
    }

    public int size ()
    {
        return commentTime.size();
    }
    public int findPosition (int time)
    {
        if(commentTime.size() == 0 || time < commentTime.get(0))
        {
            return 0;
        }
        else if(time > commentTime.lastElement())
        {
            return commentTime.size();
        }
        for(int i = 0; i < commentText.size() ; i ++)
        {
            if(time < commentTime.get(i))
            {
                return i;
            }
        }
        return commentTime.size();
    }

    public static void debugComment(AudioComment comment)
    {
        for(int i = 0; i < comment.size(); i ++)
        {
            Log.e("COMMENT_ERROR", "commentText "+ i+ ": " + comment.getCommentText(i));
            Log.e("COMMENT_ERROR", "commentTime "+ i+ ": " + comment.getCommentTime(i));
        }
    }
}
