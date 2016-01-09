package br.ufpe.cin.vocalium;

import android.app.Activity;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import java.io.IOException;

import AudioUtils.AudioPlayerManager;
import Utils.LayoutOutput;
import Utils.ServerConnection;
import Utils.UserInformation;

public class TutorHearComm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_hear_comm);

        CreateUser();


        
        UserInformation user = UserInformation.getInstance();
        //change start text
        TextView audioName = (TextView)findViewById(R.id.audio_name_tutor_textview);
        audioName.setText(user.GetStudentName());

        //---------------------Audio Player-----------------
        String path = getFilesDir() + "/1.mp3";

        AudioPlayerManager player = null;
        try {
            player = new AudioPlayerManager(this, path);
        } catch (IOException e) {
            Log.e("CONNECTION_ERROR", "Problem loading file");
        }
        player.startUpdateTutorHearComment(this);
        //---------------------end PLayer--------------------

        LayoutOutput.ChangeEndTimeChronometer(player.getDuration(), this);


    }

    private void CreateUser ()
    {
        UserInformation user = UserInformation.getInstance();
        user.SetLogin("dlc");
        user.SetStudentId(1);
        user.SetTeacherId(1);
        user.SetAudioId(1);
        user.SetTeacherName("De");
        user.SetStudentName("Lc");
    }

}
