package AudioUtils;

import android.support.v4.util.Pair;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Vector;

/**
 * Created by Delio on 12/12/15.
 */
public class AudioComment implements Serializable {
    private int audioId;
    private Vector<Calendar> commentTime;
    private Vector<String> commentText;

    public AudioComment(int id)
    {
        audioId = id;
        commentTime = new Vector<>();
        commentText = new Vector<>();
    }
    public void addComment(Calendar time, String text)
    {
        commentTime.add(time);
        commentText.add(text);
    }
    public void removeAt (int index)
    {
        commentText.removeElementAt(index);
        commentTime.removeElementAt(index);
    }

    public int getAudioId () { return audioId; }
    public Calendar getCommentTime (int index)
    {
        return commentTime.get(index);
    }
    public String getCommentText (int index)
    {
        return commentText.get(index);
    }
    public Pair <String, Calendar> get (int index)
    {
        return new Pair<>(commentText.get(index), commentTime.get(index));
    }

    public int size ()
    {
        return commentTime.size();
    }
    public void sort ()
    {
        //ver algorítmos de sort!!
    }

}
