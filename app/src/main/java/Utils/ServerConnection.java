package Utils;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Created by Délio on 02/01/2016.
 */


public class ServerConnection {
    private static ServerConnection instance = new ServerConnection();

    public static ServerConnection getInstance() {
        return instance;
    }

    private ServerConnection() {
        client = new OkHttpClient();

    }
    //----------------------------------------------------------

    public static enum FileType
    {
        Sound,
        Comment
    }

    private final OkHttpClient client;
    private final String soundUrl = "http://192.168.1.113/Vocalium/";
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

    //this must be called inside a thread!!!
    public void GetFile(final Context context, final String fileName, final FileType fileType) {

        String filePath = "";

        if(fileType == FileType.Sound)
        {
            filePath = soundUrl + "Sound/" + FileManager.GetRelativeFilePath()+ fileName + ".mp3";
        }
        else if(fileType == FileType.Comment)
        {
            filePath = soundUrl + "Comment/" + FileManager.GetRelativeFilePath()+ fileName + ".txt";
        }

        Request request = new Request.Builder().url(filePath).build();

        Log.e("CONNECTION_ERROR", filePath);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException throwable) {
                Log.e("CONNECTION_ERROR", "No response");
                throwable.printStackTrace();
            }

            @Override
            public void onResponse(Response response) {
                if (!response.isSuccessful()) {
                    Log.e("CONNECTION_ERROR", "Response not succesful");
                    return;
                }
                Log.e("CONNECTION_ERROR", "Connection succesful");
                try {
                    FileManager.SaveFile(context, response.body().bytes(), fileName, fileType);

                } catch (IOException e) {
                    Log.e("CONNECTION_ERROR", "Error saving file");
                }
            }
        });
    }

    public void PostFile(final Context context, final String fileName, final FileType fileType)
    {
        String fileTypeString = "";
        String filePath = "";
        String fileServerName = "";
        if(fileType == FileType.Sound)
        {
            filePath = context.getFilesDir() + "/"+ fileName + ".mp3";
            fileTypeString = "sound";
            fileServerName = "sound";
        }
        else if(fileType == FileType.Comment)
        {
            filePath = context.getFilesDir() + "/"+ fileName + ".txt";
            fileTypeString = "comment";
            fileServerName = "comment";
        }

        UserInformation user = UserInformation.getInstance();

        //still have to find the id of the file
        final File file = new File(filePath);

        //verifyFileExists(file);

        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        builder = builder.addFormDataPart("teacher", "" + user.GetTeacherId());
        builder = builder.addFormDataPart("student", "" + user.GetStudentId());
        builder = builder.addFormDataPart("post_type", fileTypeString);
        builder = builder.addFormDataPart("file", fileServerName, RequestBody.create(MEDIA_TYPE_MARKDOWN, file));
        RequestBody requestBody = builder.build();

        Request request = new Request.Builder().url(soundUrl).post(requestBody).build();



        Response response;
        try {
            response = client.newCall(request).execute();

            if(!response.isSuccessful()) {
                Log.e("POST_ERROR", "Response not succesful " + response.body().string());
            }
            else
            {
                String responseString = response.body().string();
                Log.e("POST_ERROR", "String: " + responseString);
                if(responseString.compareTo("failed") == 0)
                {
                    Log.e("POST_ERROR", "Error on server " + response.body().string());
                }
                else
                {
                    int newSoundId = Integer.parseInt(responseString);

                    //FileManager.RenameFile(context, file, "" + newSoundId, fileType);
                }
            }

        } catch (IOException e) {
            Log.e("POST_ERROR", "Error executing post");
        }
    }


    private void verifyFileExists (File file)
    {
        try {
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            Log.e("POST_ERROR", "File not found");
        }
    }
}
