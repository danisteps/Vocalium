package br.ufpe.cin.vocalium;

import android.content.Context;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import Utils.DatabaseManager;

/**
 * Created by Délio on 07/01/2016.
 */
public class SoundRowAdapter extends BaseAdapter {

    private Context context;
    private Pair<Integer, String>[] sounds;
    private static LayoutInflater inflater;
    private boolean showSound = false;

    public SoundRowAdapter (Context context, Pair<Integer, String>[] sounds)
    {
        this.context = context;
        this.sounds = sounds;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public SoundRowAdapter (Context context, Pair<Integer, String>[] sounds, boolean showSound)
    {
        this.context = context;
        this.sounds = sounds;
        this.showSound = showSound;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return sounds.length;
    }

    @Override
    public Object getItem(int position) {
        return sounds[position].second;
    }

    @Override
    public long getItemId(int position) {
        return sounds[position].first;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(vi == null)
        {
            vi = inflater.inflate(R.layout.sound_row, null);
        }
        TextView text = (TextView) vi.findViewById(R.id.textViewSoundRow);
        text.setText(sounds[position].second);

        if(showSound)
        {
            ImageView image = (ImageView)vi.findViewById(R.id.sound_row_image_view);
            boolean imageType = DatabaseManager.isCommented(sounds[position].first);

            if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                image.setBackground(vi.getContext().getDrawable(R.drawable.sound_icon_transp));
            } else {
                image.setBackground(vi.getResources().getDrawable(R.drawable.sound_icon_transp));
            }
        }

        return vi;
    }
}
