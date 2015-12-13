package Utils;

/**
 * Created by Délio on 12/12/2015.
 */
public class LayoutOutput {
    private static LayoutOutput instance = new LayoutOutput();

    public static LayoutOutput getInstance() {
        return instance;
    }

    private LayoutOutput() {
        comment = "";
    }

    //-----------------AudioCommentPlayer--------------------
    public String comment;

}
