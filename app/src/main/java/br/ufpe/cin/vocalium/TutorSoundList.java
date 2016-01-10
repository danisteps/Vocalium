package br.ufpe.cin.vocalium;

import android.content.Intent;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import Utils.UserInformation;

public class TutorSoundList extends AppCompatActivity {
    private final static Class nextActivity = TutorHearComm.class;
    public final static String EXTRA_INTENT_MESSAGE = "br.ufpe.cin.vocalium.AUDIO_NUMBER_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_sound_list);
        int i = 0;


        CreateUser();

        ListView listView = (ListView) findViewById(R.id.list_view_tutor_sound_list);
        listView.setAdapter(new SoundRowAdapter(this, new Pair[] {
                new Pair(1, "Audio " + (++i)),
                new Pair(2, "Audio " + (++i)),
                new Pair(3, "Audio " + (++i)),
                new Pair(4, "Audio " + (++i)),
                new Pair(5, "Audio " + (++i)),
                new Pair(6, "Audio " + (++i)),
                new Pair(7, "Audio " + (++i)),
                new Pair(8, "Audio " + (++i)),
                new Pair(9, "Audio " + (++i)),
                new Pair(10, "Audio " + (++i)),
                new Pair(11, "Audio " + (++i)),
                new Pair(12, "Audio " + (++i))}));



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
                int soundId = (int)adapter.getItemIdAtPosition(position);

                updateUserInformation(soundId);
                changeActivity(position + 1);
            }
        });
    }



    private void updateUserInformation(int soundId)
    {
        UserInformation.getInstance().SetAudioId(soundId);
    }
    private void changeActivity(int itemSelected)
    {
        Intent intent = new Intent(this, nextActivity);
        intent.putExtra(EXTRA_INTENT_MESSAGE, itemSelected);
        startActivity(intent);
        finish();
    }


    private void CreateUser ()
    {
        UserInformation user = UserInformation.getInstance();
        user.SetLogin("dlc");
        user.SetStudentId(1);
        user.SetTeacherId(1);
        user.SetTeacherName("De");
        user.SetStudentName("Lc");
    }
}
