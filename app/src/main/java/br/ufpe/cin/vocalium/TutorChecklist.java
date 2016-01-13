package br.ufpe.cin.vocalium;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.Parse;
import com.parse.ParseObject;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import AudioUtils.AudioComment;
import AudioUtils.AudioPlayerManager;
import Utils.DatabaseManager;
import Utils.FileManager;
import Utils.ServerConnection;
import Utils.UserInformation;

public class TutorChecklist extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_checklist);
        final Context context = getApplicationContext();

        DatabaseManager.initializeParse(this);

        UserInformation.getInstance().SetTutorId(1);

        ListView listView = (ListView) findViewById(R.id.ListView_CheckList);
        inflateListView(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {

            }
        });


    }
    private void inflateListView (ListView listView)
    {
        Pair<Integer, String>[] elementsPair;

        String[] results = DatabaseManager.getRatingNames(UserInformation.getInstance().GetTutorId());
        elementsPair = new Pair[results.length];

        for(int i = 0; i < results.length; i ++)
        {
            String student = results[i];
            elementsPair[i] = new Pair<>(0, student);

        }

        listView.setAdapter(new RatingRowAdapter(this, elementsPair));
    }

    private AudioComment CreateComments ()
    {
        int audioId = UserInformation.getInstance().GetAudioId();

        int time1 = 4000;
        int time2 = 7000;
        int time3 = 40000;

        String text1 = "Muito bom aqui";
        String text2 = "está aprendendo";
        String text3 = "Parabéns";


        AudioComment comment = new AudioComment(audioId);
        comment.addComment(time1, text1);
        comment.addComment(time2, text2);
        comment.addComment(time3, text3);

        return comment;
    }
    private void debugComment(AudioComment comment)
    {
        Log.e("POST_ERROR", "debug comment");
        Log.e("POST_ERROR", "debug comment 1: " + comment.getCommentText(0));
        Log.e("POST_ERROR", "debug comment 1: " + comment.getCommentText(1));
    }
}


//context.getResources().openRawResource(fileId)