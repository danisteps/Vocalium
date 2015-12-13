package Utils;

import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by Délio on 13/12/2015.
 */
public class DropboxInterface {

    final static private String APP_KEY = "w9thy6lc0kptfqz";
    final static private String APP_SECRET = "0itzj8epsscbmuw";

    private DropboxAPI<AndroidAuthSession> dropbox;

    private static DropboxInterface instance = new DropboxInterface();

    public static DropboxInterface getInstance() {
        return instance;
    }

    private DropboxInterface() {
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        dropbox = new DropboxAPI<AndroidAuthSession>(session);

        dropbox.getSession().setOAuth2AccessToken("bWP-JaPk2PAAAAAAAAAABnFWLoyK60RnKqAMoAs5tYEUgnryNPNR1VUEj1cef3OS");
    }

    public void UploadAudio (String path) throws DropboxException, FileNotFoundException {
        File file = new File(path);
        FileInputStream inputStream = new FileInputStream(file);

        DropboxAPI.Entry response = dropbox.putFile(path, inputStream, file.length(), null, null);
        //now there's is one more audio
        UserInformation.getInstance().IncreaseNumberOfAudios();
    }

    public void DownloadAudio (String path) throws FileNotFoundException, DropboxException {
        File file = new File(path);
        FileOutputStream outputStream = new FileOutputStream(file);
        DropboxAPI.DropboxFileInfo info = dropbox.getFile(path, null, outputStream, null);
    }
}
