package br.ufpe.cin.vocalium;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseObject;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import AudioUtils.AudioComment;
import AudioUtils.AudioPlayerManager;
import Utils.DatabaseManager;
import Utils.FileManager;
import Utils.ServerConnection;
import Utils.UserInformation;

public class TutorChecklist extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_checklist);
        final Context context = getApplicationContext();

        UserInformation user = UserInformation.getInstance();



        new Thread(new Runnable() {
            @Override
            public void run() {
                //ServerConnection.getInstance().PostFile(context, "1", ServerConnection.FileType.Sound);
                //ServerConnection.getInstance().GetFile(context, "1", ServerConnection.FileType.Comment);
            }
        }).start();


        /*String filePath = context.getFilesDir() + "/1.mp3";
        File file = new File(filePath);
        file.delete();*/

        /*//REMEMBER!!! Only call this after complete download!!
        String path = context.getFilesDir() + "/1.mp3";

        try {
            AudioPlayerManager player = new AudioPlayerManager(context, path);
        } catch (IOException e) {
            Log.e("CONNECTION_ERROR", "Problem loading file");
        }*/

    }

    private AudioComment CreateComments ()
    {
        int audioId = UserInformation.getInstance().GetAudioId();

        int time1 = 4000;
        int time2 = 7000;
        int time3 = 40000;

        String text1 = "Muito bom aqui";
        String text2 = "está aprendendo";
        String text3 = "Parabéns";


        AudioComment comment = new AudioComment(audioId);
        comment.addComment(time1, text1);
        comment.addComment(time2, text2);
        comment.addComment(time3, text3);

        return comment;
    }
    private void debugComment(AudioComment comment)
    {
        Log.e("POST_ERROR", "debug comment");
        Log.e("POST_ERROR", "debug comment 1: " + comment.getCommentText(0));
        Log.e("POST_ERROR", "debug comment 1: " + comment.getCommentText(1));
    }
}


//context.getResources().openRawResource(fileId)