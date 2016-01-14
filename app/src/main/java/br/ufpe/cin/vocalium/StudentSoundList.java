package br.ufpe.cin.vocalium;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

import Utils.DatabaseManager;
import Utils.UserInformation;

/**
 * Created by danielesoarespassos on 13/01/2016.
 */
public class StudentSoundList extends AppCompatActivity {

    //private final static Class backActivity = StudentSoundList.class;
    private final static Class nextActivity = DownloadTutor.class;
    public final static String EXTRA_INTENT_MESSAGE = "br.ufpe.cin.vocalium.AUDIO_NUMBER_MESSAGE";
    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_student);
        int i = 0;
        status = 0;

        UserInformation user = UserInformation.getInstance();

        TextView textView = (TextView) findViewById(R.id.audio_name_student_textview);
        textView.setText(UserInformation.getInstance().GetStudentName());

        ListView listView = (ListView) findViewById(R.id.list_view_student_sound_list);
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

        FloatingActionButton floatingButton = (FloatingActionButton) findViewById(R.id.fab);
        if (user.GetTutorId() == -1){
            if (DatabaseManager.getFirstRequestFromStudent(user.GetStudentId()) != null){
                changeIcon(floatingButton, android.R.drawable.ic_dialog_email);
                status = 1;
            }
            else{
                floatingButton.setVisibility(View.INVISIBLE);
            }
        }else {
            changeIcon(floatingButton, android.R.drawable.ic_input_add);
            status = 2;
        }

    }

    private void changeIcon (FloatingActionButton floatingButton, int icone){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            floatingButton.setImageDrawable(getResources().getDrawable(icone, getApplicationContext().getTheme()));
        } else {
            floatingButton.setImageDrawable(getResources().getDrawable(icone));
        }
        floatingButton.setVisibility(View.VISIBLE);
        floatingButton.setClickable(true);

        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == 1){
                    setActivity(StudentListenComment.class); //MUDAR PARA ADICIONAR TUTOR!!!
                }
                else if (status == 2){
                    setActivity(MainActivity.class);
                }
            }
        });
    }

    private void setActivity (Class activity){
        Intent intent = new Intent(this, activity);
        startActivity(intent);
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
