package Utils;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Credentials;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Proxy;

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

        client.setAuthenticator(new Authenticator() {
            @Override
            public Request authenticate(Proxy proxy, Response response) throws IOException {
                String credential = Credentials.basic("root", "JustCause");
                return response.request().newBuilder().header("Authorization", credential).build();
            }

            @Override
            public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
                return null;
            }
        });
    }

    private Method callbackFunction;
    private Object callbackObject;
    private boolean callbackSet = false;

    private Method failureCallbackFunction;
    private Object failureCallbackObject;
    private boolean failureCallbackSet = false;

    private int lastIdSet = -1;
    //----------------------------------------------------------

    public static enum FileType
    {
        Sound,
        Comment
    }

    private final OkHttpClient client;
    private final String soundUrl = "http://104.236.69.4/";
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

    //this must be called inside a thread!!!
    public void GetFile(final Context context, final String fileName, final FileType fileType) {

        String filePath = "";

        if(fileType == FileType.Sound)
        {
            filePath = soundUrl + "Sound/" + FileManager.GetRelativeFilePath()+ fileName + FileManager.getExtension(FileType.Sound);
        }
        else if(fileType == FileType.Comment)
        {
            filePath = soundUrl + "Comment/" + FileManager.GetRelativeFilePath()+ fileName + FileManager.getExtension(FileType.Comment);
        }

        Request request = new Request.Builder().url(filePath).build();

        Log.e("CONNECTION_ERROR", filePath);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException throwable) {
                Log.e("CONNECTION_ERROR", "No response");
                throwable.printStackTrace();
                failureCallback();
            }

            @Override
            public void onResponse(Response response) {
                if (!response.isSuccessful()) {
                    Log.e("CONNECTION_ERROR", "Response not succesful");
                    failureCallback();
                    return;
                }
                Log.e("CONNECTION_ERROR", "Connection succesful");
                try {
                    FileManager.SaveFile(context, response.body().bytes(), fileName, fileType);

                    callback();

                } catch (IOException e) {
                    failureCallback();
                    Log.e("CONNECTION_ERROR", "Error saving file");
                }
            }
        });
    }

    public int PostFile(final Context context, final String fileName, final FileType fileType)
    {
        String fileTypeString = "";
        String filePath = "";
        String fileServerName = "";
        if(fileType == FileType.Sound)
        {
            filePath = context.getFilesDir() + "/"+ fileName + FileManager.getExtension(FileType.Sound);
            fileTypeString = "sound";
            fileServerName = "sound";
        }
        else if(fileType == FileType.Comment)
        {
            filePath = context.getFilesDir() + "/"+ fileName + FileManager.getExtension(FileType.Comment);
            fileTypeString = "comment";
            fileServerName = "comment";
        }

        UserInformation user = UserInformation.getInstance();

        //still have to find the id of the file;
        final File file = new File(filePath);

        //verifyFileExists(file);

        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        builder = builder.addFormDataPart("teacher", "" + user.GetTutorId());
        builder = builder.addFormDataPart("student", "" + user.GetStudentId());
        builder = builder.addFormDataPart("post_type", fileTypeString);
        builder = builder.addFormDataPart("file", fileServerName, RequestBody.create(MEDIA_TYPE_MARKDOWN, file));
        RequestBody requestBody = builder.build();

        Request request = new Request.Builder().url(soundUrl).post(requestBody).build();



        Response response;
        try {
            response = client.newCall(request).execute();

            if(!response.isSuccessful()) {
                failureCallback();
                Log.e("POST_ERROR", "Response not succesful " + response.body().string());
            }
            else
            {
                String responseString = response.body().string();
                Log.e("POST_ERROR", "String: " + responseString);
                if(responseString.compareTo("failed") == 0)
                {
                    failureCallback();
                    Log.e("POST_ERROR", "Error on server " + response.body().string());
                }
                else
                {
                    int newId = Integer.parseInt(responseString);
                    lastIdSet = newId;

                    if(fileType == FileType.Sound)
                        DatabaseManager.saveSound(user.GetTutorId(), user.GetStudentId(), newId);
                    else
                        DatabaseManager.saveComment(user.GetAudioId(), newId);
                    callback();
                    return newId;
                }
            }

        } catch (IOException e) {
            failureCallback();
            Log.e("POST_ERROR", "Error executing post");
        }
        return 0;
    }

    public void setFailureCallback (Method callbackMethod, Object callbackObject)
    {
        this.failureCallbackFunction = callbackMethod;
        this.failureCallbackObject = callbackObject;
        failureCallbackSet = true;
    }
    private void failureCallback ()
    {
        if(failureCallbackSet)
        {
            Log.e("POST_ERROR", "Trying to call error function");
            try {
                failureCallbackFunction.invoke(failureCallbackObject, null);
            } catch (IllegalAccessException e) {
                Log.e("POST_ERROR", "Error on failure callback");
            } catch (InvocationTargetException e) {
                Log.e("POST_ERROR", "Error on failure callback");
            }
            callbackSet = false;
            failureCallbackSet = false;
        }
    }


    public void setCallback (Method callbackMethod, Object callbackObject)
    {
        this.callbackFunction = callbackMethod;
        this.callbackObject = callbackObject;
        callbackSet = true;
    }
    private void callback ()
    {
        if(callbackSet)
        {
            try {
                callbackFunction.invoke(callbackObject, null);
            } catch (IllegalAccessException e) {
                Log.e("POST_ERROR", "Error on callback");
            } catch (InvocationTargetException e) {
                Log.e("POST_ERROR", "Error on callback");
            }
            failureCallbackSet = false;
            callbackSet = false;
        }
    }
    public int getLastIdSet()
    {
        return lastIdSet;
    }
}
