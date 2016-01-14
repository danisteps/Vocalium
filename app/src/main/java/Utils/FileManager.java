package Utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import AudioUtils.AudioComment;

/**
 * Created by Délio on 02/01/2016.
 */
public class FileManager {
    public FileManager()
    {

    }

    public final static String COMMENT_TEMP_NAME = "temp_comment";
    private final static String COMMENT_TEMP_PATH = "/" + COMMENT_TEMP_NAME + FileManager.getExtension(ServerConnection.FileType.Comment);


    public static void SaveFile(Context context, byte[] fileBytes, String fileName, ServerConnection.FileType type) throws IOException {

        String extension = getExtension(type);

        BufferedOutputStream bufferedOutputStream;
        FileOutputStream outputStream;
        outputStream = context.openFileOutput(fileName + extension, Context.MODE_PRIVATE);


        bufferedOutputStream = new BufferedOutputStream(outputStream);
        //writes the file downloaded into the file we created
        bufferedOutputStream.write(fileBytes, 0, fileBytes.length);
        bufferedOutputStream.close();
    }

    public static void saveComment(Context context, AudioComment comment, String commentId) throws IOException {
        String comPath = context.getFilesDir() + "/" + commentId + FileManager.getExtension(ServerConnection.FileType.Comment);
        File comFile = new File(comPath);
        FileOutputStream fOutputStream = new FileOutputStream(comFile);

        ObjectOutputStream outputStream = new ObjectOutputStream(fOutputStream);

        outputStream.writeObject(comment);

        outputStream.close();
    }

    public static File saveTemporaryComment(Context context, AudioComment comment) throws IOException {
        String tempComPath = context.getFilesDir() + COMMENT_TEMP_PATH;
        File tempCom = new File(tempComPath);
        FileOutputStream fOutputStream = new FileOutputStream(tempCom);

        ObjectOutputStream outputStream = new ObjectOutputStream(fOutputStream);

        outputStream.writeObject(comment);

        outputStream.close();

        return tempCom;
    }
    public static void deleteTemporaryComment(Context context)
    {
        String tempComPath = context.getFilesDir() + COMMENT_TEMP_NAME;
        File tempCom = new File(tempComPath);

        tempCom.delete();
    }

    public static void RenameFile (Context context, File file, String name, ServerConnection.FileType type)
    {
        String extension = getExtension(type);

        File newFile = new File(context.getFilesDir() + "/"+ name + extension);
        file.renameTo(newFile);
    }

    public static AudioComment readComment(Context context, String fileName) throws IOException, ClassNotFoundException {
        File commentFile = new File(context.getFilesDir() + "/" + fileName + FileManager.getExtension(ServerConnection.FileType.Comment));

        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(commentFile));

        AudioComment audioComment = (AudioComment) inputStream.readObject();

        inputStream.close();

        return audioComment;
    }
    public static void verifyOrCreateTutor (String path, final String fileName, final Activity object, final Method onFinishFunction, final Method onErrorFunction)
    {
        File file = new File(path);
        if(!file.exists())
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.e("POST_ERROR", "Downloading!!");
                    ServerConnection server = ServerConnection.getInstance();
                    server.setFailureCallback(onErrorFunction, object);
                    server.setCallback(onFinishFunction, object);

                    server.GetFile(object, fileName, ServerConnection.FileType.Sound);
                }
            }).start();
        }
        else
        {
            Log.e("POST_ERROR", "Already exists!!");
            try {
                onFinishFunction.invoke(object, null);
            } catch (IllegalAccessException e) {
                Log.e("POST_ERROR", "problem calling function");
            } catch (InvocationTargetException e) {
                Log.e("POST_ERROR", "problem calling function");
            }
        }
    }



    public static String GetFilePath (Context context)
    {
        UserInformation user = UserInformation.getInstance();

        String path = context.getFilesDir().getAbsolutePath() + "/Sound/" + user.GetTutorId() + "/" + user.GetStudentId() + "/";

        return path;
    }

    public static String GetRelativeFilePath ()
    {
        UserInformation user = UserInformation.getInstance();
        return user.GetTutorId() + "/" + user.GetStudentId() + "/";
    }

    public static String getExtension(ServerConnection.FileType type)
    {
        String extension = "";
        if(type == ServerConnection.FileType.Comment) extension = ".txt";
        else if (type == ServerConnection.FileType.Sound) extension = ".mp3";
        //else if (type == ServerConnection.FileType.Sound) extension = ".ogg";

        return extension;
    }

    public static void deleteLocalFile (Context context) {
        File file = context.getFilesDir();

        File[] files = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(getExtension(ServerConnection.FileType.Sound));
            }
        });
        for(File toDelete : files)
        {
            toDelete.delete();
        }
    }
}
