package Utils;

import android.content.Context;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;

/**
 * Created by Délio on 02/01/2016.
 */
public class FileManager {
    public FileManager()
    {

    }


    public static void SaveFile (Context context, byte[] fileBytes, int soundId) throws IOException {

        BufferedOutputStream bufferedOutputStream;
        FileOutputStream outputStream;
        outputStream = context.openFileOutput(soundId+".mp3", Context.MODE_PRIVATE);


        bufferedOutputStream = new BufferedOutputStream(outputStream);
        //writes the file downloaded into the file we created
        bufferedOutputStream.write(fileBytes, 0, fileBytes.length);
        bufferedOutputStream.close();
    }

    public static void RenameFile (Context context, File file, String name)
    {
        File newFile = new File(context.getFilesDir() + "/"+ name + ".mp3");
        file.renameTo(newFile);
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
}
