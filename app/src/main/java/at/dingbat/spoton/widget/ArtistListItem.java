package at.dingbat.spoton.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import at.dingbat.spoton.R;
import at.dingbat.spoton.activity.SearchActivity;
import at.dingbat.spoton.adapter.SearchAdapter;
import at.dingbat.spoton.data.ParcelableArtist;
import at.dingbat.spoton.widget.recyclerview.DataHolder;
import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by bendix on 05.06.15.
 */
public class ArtistListItem extends RelativeLayout implements DataHolder {

    private SearchActivity context;

    private ParcelableArtist artist;

    private boolean initialized = false;

    private ImageView image;
    private TextView text;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ArtistListItem item;
        public ViewHolder(ArtistListItem itemView) {
            super(itemView);
            this.item = itemView;
        }
    }

    public ArtistListItem(Context context) {
        super(context);
        inflate(context, R.layout.widget_artist_list_item, this);

        this.context = (SearchActivity) context;

        image = (ImageView) findViewById(R.id.widget_artist_list_item_image);
        text = (TextView) findViewById(R.id.widget_artist_list_item_text);

        initialized = true;

        if(artist != null) setArtist(artist);

    }

    public void setArtist(ParcelableArtist artist) {
        this.artist = artist;
        if(initialized) {
            if(artist == null) Log.d("", "Artist is null!");
            else {
                if(artist.images.size() > 0) {
                    image.setVisibility(VISIBLE);
                    Picasso.with(context).load(artist.images.get(0).url).into(image);
                }
                else image.setVisibility(INVISIBLE);
                text.setText(artist.name);
                setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("", "Showing artist!");
                    }
                });
            }
        }
    }

    @Override
    public int getItemViewId() {
        return SearchAdapter.TYPE_ARTIST;
    }

}