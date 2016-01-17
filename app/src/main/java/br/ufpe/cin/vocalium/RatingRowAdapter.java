package br.ufpe.cin.vocalium;

import android.app.Activity;
import android.content.Context;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Délio on 07/01/2016.
 */
public class RatingRowAdapter extends BaseAdapter {

    private Context context;
    private Pair<Float, String>[] ratings;
    private static LayoutInflater inflater;
    private boolean enabled = true;
    private Method longClickFunction = null;
    private Activity activity;

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
    public RatingRowAdapter (Context context, String[] ratingNames, float[] ratingValue)
    {
        enabled = false;
        this.context = context;

        ratings = new Pair[ratingNames.length];

        for(int i = 0; i < ratingNames.length; i ++)
        {
            ratings[i] = new Pair<>(ratingValue[i], ratingNames[i]);
        }

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void addRating(String name)
    {
        //copy array from before
        Pair<Float, String>[] ratingsTemp = new Pair[ratings.length+1];

        for(int i = 0; i < ratings.length; i ++)
        {
            ratingsTemp[i] = ratings[i];
        }
        //one more than before
        ratingsTemp[ratings.length] = new Pair<>(0f, name);

        ratings = ratingsTemp;
        notifyDataSetChanged();
    }
    public void removeRating (String name)
    {
        Pair<Float, String>[] ratingsTemp = new Pair[ratings.length-1];
        boolean found = false;

        Log.e("COMMENT_ERROR", "to remove rating: " + name);
        for(int i = 0, j = 0; j < ratings.length; i ++, j ++)
        {
            if(ratings[j].second.compareTo(name) != 0)
            {
                Log.e("COMMENT_ERROR", "current rating: " + ratings[i].second);
                ratingsTemp[i] = ratings[j];
            }
            else
            {
                Log.e("COMMENT_ERROR", "found: " + ratings[i].second);
                found = true;
                //repeat index next time for ratingsTemp
                i --;
            }
        }
        if(found)
        {
            ratings = ratingsTemp;
            notifyDataSetChanged();
        }
        else Log.e("COMMENT_ERROR", "didnt found one...");

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
        text.setLongClickable(true);

        final RatingBar ratingBar = (RatingBar) vi.findViewById(R.id.RatingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Pair<Float, String> newPair = new Pair<>(rating, ratings[position].second);
                ratings[position] = newPair;
                Log.e("COMMENT_ERROR", "rating " + position + " changed to: " + rating);
            }
        });

        if(longClickFunction != null)
        {
            text.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.e("COMMENT_ERROR", "calling??");
                    try {
                        longClickFunction.invoke(activity, ratings[position].second);
                    } catch (IllegalAccessException e) {
                        Log.e("COMMENT_ERROR", "problem invoking long click function");
                    } catch (InvocationTargetException e) {
                        Log.e("COMMENT_ERROR", "problem invoking long click function");
                    }
                    return true;
                }
            });
        }
        if(!enabled)
        {
            ratingBar.setRating(ratings[position].first);
            ratingBar.setIsIndicator(true);
        }


        return vi;
    }

    public void setOnLongTouchClickFunction(Method function, Activity obj)
    {
        longClickFunction = function;
        activity = obj;
    }
}
