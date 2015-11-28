package br.ufpe.cin.vocalium;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.NoiseSuppressor;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.ideaheap.io.VorbisFileOutputStream;

import java.io.IOException;

public final class MainActivity extends AppCompatActivity {
    private final int SAMPLE_RATE = 44100;

    private MediaCodec _codec;
    private VorbisFileOutputStream _track;
    private AudioRecord _record;
    private volatile boolean _stop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button startRecordingButton = (Button) findViewById(R.id.start_recording_button);
        startRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread() {
                    @Override
                    public void run() {
                        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);

                        int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);

                        short[] buffer = new short[bufferSize / 2];

                        try {
                            _track = new VorbisFileOutputStream("/mnt/sdcard/voz.ogg");
                        } catch (IOException ignored) {
                        }
                        //_track = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);
                        //_track.play();

                        _record = new AudioRecord(MediaRecorder.AudioSource.VOICE_RECOGNITION, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);

                        NoiseSuppressor.create(_record.getAudioSessionId()).setEnabled(true);
                        AcousticEchoCanceler.create(_record.getAudioSessionId()).setEnabled(true);
                        AutomaticGainControl.create(_record.getAudioSessionId()).setEnabled(true);

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
                }.start();
            }
        });
        Button stopRecordingButton = (Button) findViewById(R.id.stop_recording_button);
        stopRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _stop = true;

                _record.stop();
                _record.release();

                try {
                    _track.close();
                } catch (IOException ignored) {
                }
            }
        });
    }
}