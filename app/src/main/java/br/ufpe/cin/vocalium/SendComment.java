package br.ufpe.cin.vocalium;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import AudioUtils.AudioComment;
import Utils.DatabaseManager;
import Utils.FileManager;
import Utils.ServerConnection;
import Utils.UserInformation;

public class SendComment extends AppCompatActivity {
    private final static Class nextActivity = TutorSoundList.class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_comment);


        UserInformation user = UserInformation.getInstance();
        //change start text
        TextView audioName = (TextView)findViewById(R.id.audio_name_send_comment);
        audioName.setText(user.GetStudentName());

        AudioComment comment = (AudioComment)getIntent().getSerializableExtra(TutorHearComm.EXTRA_INTENT_MESSAGE);

        sendComment(comment);
    }

    private void sendComment(final AudioComment comment)
    {
        final Activity activity = this;
        Thread serverThread = (new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    FileManager.saveTemporaryComment(activity, comment);
                } catch (IOException e) {
                    Log.e("COMMENT_ERROR", "cannot save temporary comment file");
                    return;
                }

                int newId = ServerConnection.getInstance().PostFile(activity, FileManager.COMMENT_TEMP_NAME, ServerConnection.FileType.Comment);

                if(newId == 0)
                {
                    Log.e("COMMENT_ERROR", "error posting file!!");
                    return;
                }
                try {
                    FileManager.saveComment(activity, comment, ""+newId);
                    DatabaseManager.saveComment(UserInformation.getInstance().GetAudioId(), newId);
                } catch (IOException e) {
                    Log.e("COMMENT_ERROR", "error saving new file");
                }
                FileManager.deleteTemporaryComment(activity);

                changeActivity();
            }
        }));
        serverThread.start();
    }

    private void changeActivity()
    {
        Log.e("COMMENT_ERROR", "Post successful");

        Intent intent = new Intent(this, nextActivity);
        startActivity(intent);
        finish();
    }
}
