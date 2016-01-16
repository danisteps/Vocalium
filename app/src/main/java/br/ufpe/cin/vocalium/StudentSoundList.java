package br.ufpe.cin.vocalium;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
    private final static Class commentSoundActivity = DownloadStudent.class;
    private final static Class recordSoundActivity = MainActivity.class;
    public final static String EXTRA_INTENT_MESSAGE = "br.ufpe.cin.vocalium.AUDIO_NUMBER_MESSAGE";
    private int status;
    FloatingActionButton floatingButton;

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
                changeCommentActivity(position);
            }
        });


        //needed to access button in checkstatus
        floatingButton = (FloatingActionButton) findViewById(R.id.fab);

        createThreadUpdateFloatButton();
    }

    private void checkStatus ()
    {
        UserInformation user = UserInformation.getInstance();
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
                    showRequestDialog();
                }
                else if (status == 2){
                    changeRecordActivity();
                }
            }
        });
    }

    private void changeRecordActivity (){
        Intent intent = new Intent(this, recordSoundActivity);
        startActivity(intent);
    }


    private void inflateListView (ListView listView)
    {
        Pair<Integer, String>[] elementsPair;

        UserInformation user = UserInformation.getInstance();
        List<ParseObject> results = DatabaseManager.getSoundsCommented(user.GetTutorId(), user.GetStudentId());
        elementsPair = new Pair[results.size()];

        for(int i = 0; i < results.size(); i ++)
        {
            ParseObject sound = results.get(i);
            elementsPair[i] = new Pair<>(sound.getInt("SoundId"), "Áudio " + (i+1));

        }

        listView.setAdapter(new SoundRowAdapter(this, elementsPair, true));
    }

    private void changeCommentActivity(int soundNumber)
    {
        Intent intent = new Intent(this, commentSoundActivity);
        intent.putExtra(EXTRA_INTENT_MESSAGE, soundNumber);
        startActivity(intent);
    }

    private void showRequestDialog()
    {
        final ParseObject request = DatabaseManager.getFirstRequestFromStudent(UserInformation.getInstance().GetStudentId());
        final ParseObject tutor = DatabaseManager.getTutorObjectFromRequest(request);
        String tutorName = DatabaseManager.getTutorNameFromObject(tutor);

        Log.e("COMMENT_ERROR", "calling dialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Tutor " + tutorName + " deseja lhe adicionar como estudante");


        builder.setPositiveButton("Aceitar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                acceptRequest(request, tutor);
            }
        });
        builder.setNegativeButton("Recusar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                refuseRequest(request);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void acceptRequest(ParseObject request, ParseObject tutor)
    {
        UserInformation user = UserInformation.getInstance();
        int tutorId = DatabaseManager.getTutorIdFromObject(tutor);
        DatabaseManager.setStudentTutor(user.GetStudentId(), tutorId);
        user.populateTutorInformationForStudent(tutor);
        DatabaseManager.deleteRequest(request);
    }
    private void refuseRequest(ParseObject request)
    {
        floatingButton.setVisibility(View.INVISIBLE);
        DatabaseManager.deleteRequest(request);
    }

    private void createThreadUpdateFloatButton ()
    {
        final Activity activity = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                //only runs if there is no tutor yet
                while (status != 2)
                {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            checkStatus();
                        }
                    });
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Log.e("UPDATE_ERROR", "thread stopped");
                    }
                }
            }
        }).start();    }

    @Override
    public void onBackPressed() {

    }
}
