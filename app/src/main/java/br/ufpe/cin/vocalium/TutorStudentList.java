package br.ufpe.cin.vocalium;

import android.content.Intent;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseObject;

import java.util.List;

import Utils.DatabaseManager;
import Utils.UserInformation;

public class TutorStudentList extends AppCompatActivity {
    private final static Class nextActivity = TutorSoundList.class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tutor_student_list);



        ListView listView = (ListView) findViewById(R.id.list_view_tutor_student_list);
        inflateListView(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
                updateUserInformation(adapter, position);

                changeActivity();
            }
        });
    }
    private void inflateListView (ListView listView)
    {
        Pair<Integer, String>[] elementsPair;

        List<ParseObject> results = DatabaseManager.getStudentsFromTutor(UserInformation.getInstance().GetTutorId());
        elementsPair = new Pair[results.size()];

        for(int i = 0; i < results.size(); i ++)
        {
            ParseObject student = results.get(i);
            elementsPair[i] = new Pair<>(student.getInt("StudentId"), student.getString("StudentName"));

        }

        listView.setAdapter(new SoundRowAdapter(this, elementsPair));
    }
    private void updateUserInformation(AdapterView<?> adapter, int position)
    {
        UserInformation user = UserInformation.getInstance();
        user.SetStudentId((int)adapter.getItemIdAtPosition(position));
        user.SetStudentName((String)adapter.getItemAtPosition(position));
    }
    private void changeActivity()
    {
        Intent intent = new Intent(this, nextActivity);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}
