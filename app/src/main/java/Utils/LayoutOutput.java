package Utils;
import android.app.Activity;
import android.widget.TextView;

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
    public void ChangeStudentCommentText (String text, Activity activity) {
        TextView textView = (TextView)activity.findViewById(R.id.studentCommentText);        ///find e r?
        textView.setText(text);
    }

}
