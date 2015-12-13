package br.ufpe.cin.vocalium;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.IOException;
import java.util.Calendar;
import java.util.Vector;

import AudioUtils.AudioComment;
import AudioUtils.AudioCommentPlayer;
import Utils.LayoutOutput;

public class StudentListenComment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_listen_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Vector<AudioComment> comments = CreateComments();
        try {
            AudioCommentPlayer player = new AudioCommentPlayer(getApplicationContext(), 0, comments, this);
        } catch (IOException e) {
            LayoutOutput.getInstance().ChangeStudentCommentText("Cannot open media", this);
        }
    }

    private Vector<AudioComment> CreateComments ()
    {
        Calendar time1 = Calendar.getInstance();
        time1.setTimeInMillis(10000);
        Calendar time2 = Calendar.getInstance();
        time1.setTimeInMillis(20000);
        Calendar time3 = Calendar.getInstance();
        time1.setTimeInMillis(30000);

        String text1 = "Muito bom aqui";
        String text2 = "está aprendendo";
        String text3 = "Parabéns";


        AudioComment comment1 = new AudioComment(time1, text1);
        AudioComment comment2 = new AudioComment(time2, text2);
        AudioComment comment3 = new AudioComment(time3, text3);

        Vector<AudioComment> commentVector = new Vector<>();
        commentVector.add(comment1);
        commentVector.add(comment2);
        commentVector.add(comment3);

        return commentVector;
    }
}
