package br.ufpe.cin.vocalium;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Switch;

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
    private final static Class nextActivity = SendComment.class;

    private AudioComment comment;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_checklist);
        final Context context = getApplicationContext();


        comment = (AudioComment)getIntent().getSerializableExtra(TutorHearComm.EXTRA_INTENT_MESSAGE);

        listView = (ListView) findViewById(R.id.ListView_CheckList);
        inflateListView(listView);



        Button button = (Button) findViewById(R.id.send_button_tutor_checklist);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity();
            }
        });

        Switch enableRating = (Switch)findViewById(R.id.switch_tutor_checklist);
        enableRating.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    listView.setVisibility(View.VISIBLE);
                else
                    listView.setVisibility(View.INVISIBLE);
                comment.setEnabled(isChecked);
            }
        });
    }
    private void changeActivity()
    {
        updateComment();

        Intent intent = new Intent(this, nextActivity);
        intent.putExtra(TutorHearComm.EXTRA_INTENT_MESSAGE, comment);
        startActivity(intent);
        finish();
    }
    private void inflateListView (ListView listView)
    {

        String[] results = DatabaseManager.getRatingNames(UserInformation.getInstance().GetTutorId());

        listView.setAdapter(new RatingRowAdapter(this, results));
    }

    private void debugComment(AudioComment comment)
    {
        Log.e("POST_ERROR", "debug comment");
        Log.e("POST_ERROR", "debug comment 1: " + comment.getCommentText(0));
        Log.e("POST_ERROR", "debug comment 1: " + comment.getCommentText(1));
    }
    private void updateComment()
    {
        comment.setRatings(listView.getAdapter());
    }
}


//context.getResources().openRawResource(fileId)