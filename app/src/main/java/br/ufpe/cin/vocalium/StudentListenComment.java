package br.ufpe.cin.vocalium;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Vector;

import AudioUtils.AudioComment;
import AudioUtils.AudioCommentPlayer;
import Utils.LayoutOutput;
import Utils.UserInformation;

public class StudentListenComment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_student_listen_comment);

        AudioComment comments = CreateComments();
        try {
            AudioCommentPlayer player = new AudioCommentPlayer(getApplicationContext(), "", comments, this);
        } catch (IOException e) {
            LayoutOutput.getInstance().ChangeStudentCommentText("Cannot open media", this);
        }
    }

    private AudioComment CreateComments ()
    {
        int audioId = UserInformation.getInstance().GetAudioId();

        Calendar time1 = Calendar.getInstance();
        time1.setTimeInMillis(4000);
        Calendar time2 = Calendar.getInstance();
        time2.setTimeInMillis(7000);
        Calendar time3 = Calendar.getInstance();
        time3.setTimeInMillis(40000);

        String text1 = "Muito bom aqui";
        String text2 = "está aprendendo";
        String text3 = "Parabéns";


        AudioComment comment = new AudioComment(audioId);
        comment.addComment(time1, text1);
        comment.addComment(time2, text2);
        comment.addComment(time3, text3);

        return comment;
    }
}
