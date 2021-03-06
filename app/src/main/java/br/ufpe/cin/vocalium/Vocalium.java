package br.ufpe.cin.vocalium;

import Utils.DatabaseManager;
import Utils.FileManager;

import android.app.Application;
import com.cengalabs.flatui.FlatUI;

/**
 * Created by Fofofly on 16/01/2016.
 */
public final class Vocalium extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FlatUI.initDefaultValues(this);
        FlatUI.setDefaultTheme(FlatUI.ORANGE);

        DatabaseManager.initializeParse(this);

        FileManager.deleteLocalFile(this);
    }
}