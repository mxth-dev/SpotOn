package at.dingbat.spoton.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import at.dingbat.spoton.R;
import at.dingbat.spoton.activity.MainActivity;
import at.dingbat.spoton.models.ParcelableTrack;

/**
 * Created by Max on 7/5/2015.
 */
public class TrackView extends RelativeLayout {

    private MainActivity context;

    private ImageView image;
    private TextView title;
    private TextView album;

    private ParcelableTrack track;

    private boolean initialized = false;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TrackView item;
        public ViewHolder(TrackView itemView) {
            super(itemView);
            this.item = itemView;
        }
    }

    public TrackView(Context context) {
        super(context);

        this.context = (MainActivity) context;

        inflate(context, R.layout.widget_track_view, this);

        image = (ImageView) findViewById(R.id.widget_track_view_image);
        title = (TextView) findViewById(R.id.widget_track_view_title);
        album = (TextView) findViewById(R.id.widget_track_view_album);

        initialized = true;

        if(track != null) setTrack(track);

    }

    public void setTrack(final ParcelableTrack track) {
        this.track = track;
        if(initialized) {
            Picasso.with(context).load(track.album.images.get(0).url).into(image);
            title.setText(track.name);
            album.setText(track.album.name);
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.showTrack(track);
                }
            });
        }
    }

}
