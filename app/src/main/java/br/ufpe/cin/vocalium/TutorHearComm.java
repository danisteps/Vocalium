package br.ufpe.cin.vocalium;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import java.io.IOException;

import AudioUtils.AudioPlayerManager;
import Utils.FileManager;
import Utils.LayoutOutput;
import Utils.ServerConnection;
import Utils.UserInformation;

public class TutorHearComm extends AppCompatActivity {
    private final static Class nextActivity = TutorChecklist.class;
    private final static Class backActivity = TutorSoundList.class;

    private AudioPlayerManager player;
    private boolean commentViewEnabled = false;
    private TextView commentView;
    private View.OnClickListener sendButtonListener;
    public final static String EXTRA_INTENT_MESSAGE = "br.ufpe.cin.vocalium.COMMENT_MESSAGE";

    private ImageButton playButton;

    int itemNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_hear_comm);



        
        UserInformation user = UserInformation.getInstance();
        //change start text
        TextView audioName = (TextView)findViewById(R.id.audio_name_tutor_textview);

        itemNumber = getIntent().getIntExtra(TutorSoundList.EXTRA_INTENT_MESSAGE, -1);
        if(itemNumber != -1)
        {
            audioName.setText("Áudio " + itemNumber);
        }
        else {
            audioName.setText(user.GetStudentName());
        }

        //---------------------Audio Player-----------------
        String path = getFilesDir() + "/";
        path += user.GetAudioId() + FileManager.getExtension(ServerConnection.FileType.Sound);



        player = null;
        try {
            player = new AudioPlayerManager(this, path);
        } catch (IOException e) {
            Log.e("CONNECTION_ERROR", "Problem loading file");
        }
        player.startUpdate(this, AudioPlayerManager.UpdateType.TutorHearComment);
        player.setCompletionListenerTutorHearComment(this);
        //---------------------end PLayer--------------------


        LayoutOutput.ChangeEndTimeChronometer(player.getDuration(), this);


        playButton = (ImageButton) findViewById(R.id.play_button_tutor);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playButtonPress();
            }
        });

        Button commentButton = (Button) findViewById(R.id.comment_button_tutor_hear);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCommentViewVisibility();
                if(player.isPlaying())
                {
                    changeButtonImage(false);
                    player.pause();
                }
            }
        });

        sendButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("COMMENT_ERROR", "clicked");
                createSendDialog();
            }
        };
        showSendButton();


    }
    private void changeCommentViewVisibility()
    {
        if(commentViewEnabled)
        {
            String text = commentView.getText().toString();
            if(text.compareTo("") != 0)
            {
                addComment(text);
                player.start();
            }

            LayoutOutput.disableCommentView(this);
            showSendButton();
            commentViewEnabled = false;
        }
        else
        {
            LayoutOutput.hideSendButton(this);
            commentView = LayoutOutput.enableCommentView(this);
            commentViewEnabled = true;
        }
    }
    private void addComment(String comment)
    {
        player.addComment(comment);
    }
    private void playButtonPress()
    {
        if(player.isPlaying())
        {
            changeButtonImage(false);
            //change button as well
            player.pause();
        }
        else
        {
            changeButtonImage(true);
            //change button as well
            player.start();
            if(commentViewEnabled) {
                LayoutOutput.disableCommentView(this);
                showSendButton();
                commentViewEnabled = false;
            }
        }
    }
    private void showSendButton() {
        Button sendButton = LayoutOutput.showSendButton(this);
        sendButton.setOnClickListener(sendButtonListener);
    }

    private void createSendDialog()
    {
        changeButtonImage(false);
        player.pause();
        Log.e("COMMENT_ERROR", "calling dialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Enviar comentários?");


        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                changeActivity();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void changeActivity()
    {
        player.Release();
        Intent intent = new Intent(this, nextActivity);
        intent.putExtra(EXTRA_INTENT_MESSAGE, player.getComments());
        intent.putExtra(TutorSoundList.EXTRA_INTENT_MESSAGE, itemNumber);
        startActivity(intent);
        finish();
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

    @Override
    public void onBackPressed() {
        player.Release();
        super.onBackPressed();
    }

}
