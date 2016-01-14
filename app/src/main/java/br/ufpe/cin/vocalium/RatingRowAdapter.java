package br.ufpe.cin.vocalium;

import android.content.Context;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by Délio on 07/01/2016.
 */
public class RatingRowAdapter extends BaseAdapter {

    private Context context;
    private Pair<Float, String>[] ratings;
    private static LayoutInflater inflater;

    public RatingRowAdapter (Context context, String[] ratingNames)
    {
        this.context = context;

        ratings = new Pair[ratingNames.length];

        for(int i = 0; i < ratingNames.length; i ++)
        {
            ratings[i] = new Pair<>(0f, ratingNames[i]);
        }

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return ratings.length;
    }

    @Override
    public Object getItem(int position) {
        return ratings[position];
    }

    @Override
    public long getItemId(int position) {
        return ratings[position].first.longValue();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(vi == null)
        {
            vi = inflater.inflate(R.layout.rating_row, null);
        }
        TextView text = (TextView) vi.findViewById(R.id.RatingText);
        text.setText(ratings[position].second);

        RatingBar ratingBar = (RatingBar) vi.findViewById(R.id.RatingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Pair<Float, String> newPair = new Pair<>(rating, ratings[position].second);
                ratings[position] = newPair;
                Log.e("COMMENT_ERROR", "rating " + position + " changed to: " + rating);
            }
        });


        return vi;
    }
}
