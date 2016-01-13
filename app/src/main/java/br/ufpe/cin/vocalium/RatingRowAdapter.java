package br.ufpe.cin.vocalium;

import android.content.Context;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Délio on 07/01/2016.
 */
public class RatingRowAdapter extends BaseAdapter {

    private Context context;
    private Pair<Integer, String>[] sounds;
    private static LayoutInflater inflater;

    public RatingRowAdapter (Context context, Pair<Integer, String>[] sounds)
    {
        this.context = context;
        this.sounds = sounds;
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
            vi = inflater.inflate(R.layout.rating_row, null);
        }
        TextView text = (TextView) vi.findViewById(R.id.RatingText);
        text.setText(sounds[position].second);

        return vi;
    }
}
