package AudioUtils;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Delio on 12/12/15.
 */
public class AudioComment implements Serializable {
    private int audioId;
    private Calendar commentTime;
    private String commentText;

    public AudioComment(int id, Calendar time, String text)
    {
        audioId = id;
        commentTime = time;
        commentText = text;
    }

    public int GetAudioId () { return audioId; }
    public Calendar GetCommentTime ()
    {
        return commentTime;
    }
    public String GetCommentText ()
    {
        return commentText;
    }

}
