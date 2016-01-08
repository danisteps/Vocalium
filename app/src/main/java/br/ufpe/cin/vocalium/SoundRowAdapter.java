package br.ufpe.cin.vocalium;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Délio on 07/01/2016.
 */
public class SoundRowAdapter extends BaseAdapter {

    private Context context;
    private String[] audioNames;
    private static LayoutInflater inflater = null;

    public SoundRowAdapter (Context context, String[] names)
    {
        this.context = context;
        audioNames = names;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return audioNames.length;
    }

    @Override
    public Object getItem(int position) {
        return audioNames[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(vi == null)
        {
            inflater.inflate(R.layout.sound_row, null);
        }
        TextView text = (TextView) vi.findViewById(R.id.textViewSoundRow);
        text.setText(audioNames[position]);

        return vi;
    }
}
