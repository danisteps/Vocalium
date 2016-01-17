package br.ufpe.cin.vocalium;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import AudioUtils.AudioComment;
import Utils.DatabaseManager;
import Utils.UserInformation;

public class StudentRatingList extends AppCompatActivity {
    private final static Class nextActivity = SendComment.class;

    AudioComment comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_rating_list);

        comment = (AudioComment)getIntent().getSerializableExtra(StudentListenComment.EXTRA_INTENT_MESSAGE);
        checkComment();

        ListView listView = (ListView) findViewById(R.id.ListView_CheckList_student);
        inflateListView(listView);


        Button nextButton = (Button) findViewById(R.id.next_button_rating_student);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity();
            }
        });
    }




    private void inflateListView (ListView listView)
    {

        String[] ratingNames = comment.getRatingNames();
        float[] ratingValues = comment.getRatingValues();

        listView.setAdapter(new RatingRowAdapter(this, ratingNames, ratingValues));
    }

    private void checkComment()
    {
        if(comment == null || !comment.isEnabled())
        {
            changeActivity();
        }

    }
    private void changeActivity()
    {
        finish();
    }
}
