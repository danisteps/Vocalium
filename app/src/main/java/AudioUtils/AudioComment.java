package AudioUtils;

import java.util.Calendar;

/**
 * Created by Delio on 12/12/15.
 */
public class AudioComment {
    private Calendar commentTime;
    private String commentText;

    public AudioComment(Calendar time, String text)
    {
        commentTime = time;
        commentText = text;
    }

    public Calendar GetCommentTime ()
    {
        return commentTime;
    }
    public String GetCommentText ()
    {
        return commentText;
    }

}
