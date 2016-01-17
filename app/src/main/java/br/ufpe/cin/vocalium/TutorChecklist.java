package br.ufpe.cin.vocalium;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    private String ratingToRemove;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_checklist);
        final Context context = getApplicationContext();


        comment = (AudioComment)getIntent().getSerializableExtra(TutorHearComm.EXTRA_INTENT_MESSAGE);

        listView = (ListView) findViewById(R.id.ListView_CheckList);
        inflateListView(listView);



        TextView textView = (TextView)findViewById(R.id.audio_name_tutor_textview_checklist);
        textView.setText("Áudio " + getIntent().getIntExtra(TutorSoundList.EXTRA_INTENT_MESSAGE, -1));



        Button button = (Button) findViewById(R.id.send_button_tutor_checklist);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity();
            }
        });
        Button addRating = (Button) findViewById(R.id.add_rating_checklist);
        addRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddRatingDialog();
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

        //to remove ratings!!
        Class[] cls  ={String.class};
        Method longClickFunction = null;
        try {
            longClickFunction = this.getClass().getMethod("createRemoveRatingDialog", cls);
        } catch (NoSuchMethodException e) {
            Log.e("COMMENT_ERROR", "function not found");
        }


        String[] results = DatabaseManager.getRatingNames(UserInformation.getInstance().GetTutorId());
        RatingRowAdapter adapter = new RatingRowAdapter(this, results);
        adapter.setOnLongTouchClickFunction(longClickFunction, this);

        listView.setAdapter(adapter);
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

    private void createAddRatingDialog()
    {
        Log.e("COMMENT_ERROR", "calling dialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Nome do fator a ser adicionado");
        final EditText editText = new EditText(this);
        builder.setView(editText);


        builder.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String ratingName = editText.getText().toString();
                ((RatingRowAdapter)listView.getAdapter()).addRating(ratingName);
                DatabaseManager.addRatingName(UserInformation.getInstance().GetTutorId(), ratingName);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        final AlertDialog alert = builder.create();
        alert.show();
    }
    public void createRemoveRatingDialog(String name)
    {
        final String ratingName = name;
        Log.e("COMMENT_ERROR", "calling remove rating dialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Remover " + ratingName + " da lista de fatores?");


        builder.setPositiveButton("Remover", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DatabaseManager.removeRatingName(UserInformation.getInstance().GetTutorId(), ratingName);
                ((RatingRowAdapter)listView.getAdapter()).removeRating(ratingName);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        final AlertDialog alert = builder.create();
        alert.show();
    }
}


//context.getResources().openRawResource(fileId)