package Utils;

import android.content.Context;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;

import AudioUtils.AudioComment;

/**
 * Created by Délio on 02/01/2016.
 */
public class FileManager {
    public FileManager()
    {

    }

    public final static String COMMENT_TEMP_NAME = "temp_comment";
    private final static String COMMENT_TEMP_PATH = "/" + COMMENT_TEMP_NAME + ".txt";


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
        String comPath = context.getFilesDir() + "/" + commentId + ".txt";
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
        File commentFile = new File(context.getFilesDir() + "/" + fileName + ".txt");

        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(commentFile));

        AudioComment audioComment = (AudioComment) inputStream.readObject();

        inputStream.close();

        return audioComment;
    }



    public static String GetFilePath (Context context)
    {
        UserInformation user = UserInformation.getInstance();

        String path = context.getFilesDir().getAbsolutePath() + "/Sound/" + user.GetTeacherId() + "/" + user.GetStudentId() + "/";

        return path;
    }

    public static String GetRelativeFilePath ()
    {
        UserInformation user = UserInformation.getInstance();
        return user.GetTeacherId() + "/" + user.GetStudentId() + "/";
    }

    public static String getExtension(ServerConnection.FileType type)
    {
        String extension = "";
        if(type == ServerConnection.FileType.Comment) extension = ".txt";
        else if (type == ServerConnection.FileType.Sound) extension = ".mp3";

        return extension;
    }
}
