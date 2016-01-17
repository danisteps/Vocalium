package br.ufpe.cin.vocalium;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Vector;

import AudioUtils.AudioComment;
import AudioUtils.AudioCommentPlayer;
import AudioUtils.AudioPlayerManager;
import Utils.DatabaseManager;
import Utils.FileManager;
import Utils.LayoutOutput;
import Utils.ServerConnection;
import Utils.UserInformation;

public class StudentListenComment extends AppCompatActivity {
    private final static Class nextActivity = StudentRatingList.class;
    public final static String EXTRA_INTENT_MESSAGE = "br.ufpe.cin.vocalium.COMMENT_MESSAGE";

    AudioPlayerManager player;
    AudioComment comment;

    private ImageButton playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_student_listen_comment);

        UserInformation user = UserInformation.getInstance();
        String path = getFilesDir() + "/";
        path += user.GetAudioId() + FileManager.getExtension(ServerConnection.FileType.Sound);


        try {
            player = new AudioPlayerManager(this, path);
        } catch (IOException e) {
            Log.e("PLAYER_ERROR", "cannot start player");
        }
        player.startUpdate(this, AudioPlayerManager.UpdateType.StudentListenComment);
        player.setCompletionListenerStudentListenComment(this);
        comment = null;
        try {
            comment = FileManager.readComment(this, "" + DatabaseManager.getCommentId(user.GetAudioId()));
        } catch (IOException e) {
            Log.e("PLAYER_ERROR", "Problem loading comment");
        } catch (ClassNotFoundException e) {
            Log.e("PLAYER_ERROR", "Problem loading comment");
        }
        player.enqueueComments(comment);


        //------------------Layout------------------------------
        int itemNumber = getIntent().getIntExtra(StudentSoundList.EXTRA_INTENT_MESSAGE, -1);
        TextView textView = (TextView)findViewById(R.id.audio_name_student_textview);
        textView.setText("Áudio " + (itemNumber+1));

        LayoutOutput.changeEndTimeChronometerStudent(player.getDuration(), this);
        playButton = (ImageButton) findViewById(R.id.play_button_student);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playButtonPress();
            }
        });

        Button nextButton = (Button)findViewById(R.id.next_button_student_listen);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity();
            }
        });
    }

    private void playButtonPress()
    {
        changeButtonImage(!player.isPlaying());
        if(player.isPlaying())
        {
            player.pause();
        }
        else
        {
            player.start();
        }
    }
    private void changeButtonImage(boolean playing)
    {
        int buttonResource;
        if(!playing)
        {
            buttonResource = R.drawable.asd;
        }
        else
        {
            buttonResource = R.drawable.pausee;
        }
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            playButton.setBackground(getDrawable(buttonResource));
        } else {
            playButton.setBackground(getResources().getDrawable(buttonResource));
        }
    }
    public void changeActivity ()
    {
        Intent intent = new Intent(this, nextActivity);
        intent.putExtra(EXTRA_INTENT_MESSAGE, comment);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        player.Release();
        super.onBackPressed();
    }
}
