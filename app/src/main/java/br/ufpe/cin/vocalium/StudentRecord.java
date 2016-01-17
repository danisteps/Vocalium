package br.ufpe.cin.vocalium;

import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.NoiseSuppressor;
import android.os.*;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;

import com.ideaheap.io.VorbisFileOutputStream;

import java.io.File;
import java.io.IOException;

import Utils.FileManager;

public class StudentRecord extends AppCompatActivity {
    private final static Class nextActivity = SendRecord.class;

    private static int[] mSampleRates = new int[] { 8000, 11025, 22050, 44100 };
    //justo to be sure....
    private int bufferSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);

        private MediaCodec _codec;
        private VorbisFileOutputStream _track;
        private AudioRecord _record;
        private volatile boolean _stop = true;
        private boolean hasRecorded = false;
        private Chronometer chronometer;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_student_record);

            chronometer = (Chronometer) findViewById(R.id.student_record_chronometer);

            ImageButton startRecordingButton = (ImageButton) findViewById(R.id.button_play_record);
            startRecordingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!_stop) {
                        _stop = true;
                        stopRecording();
                    }
                    else if(!hasRecorded) {
                        _stop = false;
                        hasRecorded = true;
                        chronometer.start();

                        new Thread() {
                            @Override
                            public void run() {
                                startRecording();
                            }
                        }.start();
                    }
                }
            });
            Button deleteRecordingButton = (Button) findViewById(R.id.button_delete_record);
            deleteRecordingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deletePreviousFile();
                }
            });

            Button sendButton = (Button)findViewById(R.id.button_send_record);
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(hasRecorded)
                    {
                        if(!_stop)
                        {
                            _stop = true;
                            stopRecording();
                        }
                        sendRecord();
                    }
                }
            });


        }
    private void startRecording()
    {

        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

        try {
            _track = new VorbisFileOutputStream(getFilesDir() + FileManager.SOUND_TEMP_NAME);
        } catch (IOException ignored) {
        }


        /*int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);


        try {
            _track = new VorbisFileOutputStream("/mnt/sdcard/voz.ogg");
        } catch (IOException ignored) {
        }
        //_track = new AudioTrack(AudioUtils.AudioPlayerManager.STREAM_MUSIC, SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);
        //_track.play();

        _record = new AudioRecord(MediaRecorder.AudioSource.VOICE_RECOGNITION, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
        */

        _record = findAudioRecord();

        //we have found above the best buffersize
        short[] buffer = new short[bufferSize / 2];


        NoiseSuppressor supressor = NoiseSuppressor.create(_record.getAudioSessionId());
        AcousticEchoCanceler canceler = AcousticEchoCanceler.create(_record.getAudioSessionId());
        AutomaticGainControl gainControl = AutomaticGainControl.create(_record.getAudioSessionId());
        if(supressor != null)
            supressor.setEnabled(true);
        if(canceler != null)
            canceler.setEnabled(true);
        if(gainControl != null)
            gainControl.setEnabled(true);

        _record.startRecording();

        while (!_stop) {
            _record.read(buffer, 0, buffer.length);

            try {
                _track.write(buffer, 0, buffer.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void stopRecording()
    {
        chronometer.stop();
        _record.stop();
        _record.release();

        try {
            _track.close();
        } catch (IOException ignored) {
        }
    }
    private void deletePreviousFile()
    {
        chronometer.setBase(SystemClock.elapsedRealtime());
        hasRecorded = false;

        File tempFile = new File(getFilesDir() + FileManager.SOUND_TEMP_NAME);
        if(tempFile.exists())
        {
            tempFile.delete();
        }
    }

    private void sendRecord()
    {
        Intent intent = new Intent(this, nextActivity);
        startActivity(intent);
        finish();
    }

    public AudioRecord findAudioRecord() {
        for (int rate : mSampleRates) {
            for (short audioFormat : new short[] { AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT }) {
                for (short channelConfig : new short[] { AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_IN_STEREO }) {
                    try {
                        Log.d("PLAYER_ERROR", "Attempting rate " + rate + "Hz, bits: " + audioFormat + ", channel: "
                                + channelConfig);
                        int bufferSize = AudioRecord.getMinBufferSize(rate, channelConfig, audioFormat);

                        if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
                            // check if we can instantiate and have a success
                            AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, rate, channelConfig, audioFormat, bufferSize);

                            if (recorder.getState() == AudioRecord.STATE_INITIALIZED)
                            {
                                Log.e("PLAYER_ERROR",  "Found an AudioRecorder");
                                this.bufferSize = bufferSize;
                                return recorder;
                            }
                        }
                    } catch (Exception e) {
                        Log.e("PLAYER_ERROR", rate + "Exception, keep trying.", e);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        deletePreviousFile();
        super.onBackPressed();
    }
}
