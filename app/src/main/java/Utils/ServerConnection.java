package Utils;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import AudioUtils.AudioPlayerManager;


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

    private final OkHttpClient client;
    private final String soundUrl = "http://192.168.1.113/Vocalium/Sound/";

    //this must be called inside a thread!!!
    public void GetFile(final Context context, final int soundId) {
        String path = soundUrl + FileManager.GetRelativeFilePath() + soundId + ".mp3";
        Request request = new Request.Builder().url(path).build();

        Log.e("CONNECTION_ERROR", path);

        //-------On fail--------------
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
                    FileManager.SaveFile(context, response.body().bytes(), soundId);

                } catch (IOException e) {
                    Log.e("CONNECTION_ERROR", "Error saving file");
                }
            }
        });
    }
}
