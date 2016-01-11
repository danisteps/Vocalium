package br.ufpe.cin.vocalium;

import android.content.Intent;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

import Utils.DatabaseManager;
import Utils.UserInformation;

public class TutorSoundList extends AppCompatActivity {
    private final static Class backActivity = TutorStudentList.class;
    private final static Class nextActivity = DownloadTutor.class;
    public final static String EXTRA_INTENT_MESSAGE = "br.ufpe.cin.vocalium.AUDIO_NUMBER_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_sound_list);
        int i = 0;


        TextView textView = (TextView) findViewById(R.id.audio_name_tutor_textview);
        textView.setText(UserInformation.getInstance().GetStudentName());

        ListView listView = (ListView) findViewById(R.id.list_view_tutor_sound_list);
        inflateListView(listView);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
                int soundId = (int) adapter.getItemIdAtPosition(position);
                Log.e("POST_ERROR", "Clicado no item: " + position + ", com id : " + soundId);

                UserInformation.getInstance().SetAudioId(soundId);
                changeActivity(position + 1);
            }
        });
    }


    private void inflateListView (ListView listView)
    {
        Pair<Integer, String>[] elementsPair;

        UserInformation user = UserInformation.getInstance();
        List<ParseObject> results = DatabaseManager.getSounds(user.GetTutorId(), user.GetStudentId());
        elementsPair = new Pair[results.size()];

        for(int i = 0; i < results.size(); i ++)
        {
            ParseObject sound = results.get(i);
            elementsPair[i] = new Pair<>(sound.getInt("SoundId"), "Áudio " + (i+1));

        }

        listView.setAdapter(new SoundRowAdapter(this, elementsPair));
    }

    private void changeActivity(int itemSelected)
    {
        Intent intent = new Intent(this, nextActivity);
        intent.putExtra(EXTRA_INTENT_MESSAGE, itemSelected);
        startActivity(intent);
    }

}
