package at.dingbat.spoton.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import at.dingbat.spoton.R;
import at.dingbat.spoton.activity.MainActivity;
import at.dingbat.spoton.models.ParcelableTrack;
import at.dingbat.spoton.service.PlayerService;

/**
 * Created by Max on 7/5/2015.
 */
public class TrackView extends RelativeLayout {

    private MainActivity context;
    private PlayerService service;

    private ImageView image;
    private TextView title;
    private TextView album;

    private ArrayList<ParcelableTrack> playlist;
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
        service = ((MainActivity) context).getService();

        inflate(context, R.layout.widget_track_view, this);

        image = (ImageView) findViewById(R.id.widget_track_view_image);
        title = (TextView) findViewById(R.id.widget_track_view_title);
        album = (TextView) findViewById(R.id.widget_track_view_album);

        initialized = true;

        if(track != null) setTrack(track);

    }

    public void setPlaylist(ArrayList<ParcelableTrack> playlist) {
        this.playlist = playlist;
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
                    service.setPlaylist(playlist);
                    service.playTrack(playlist.indexOf(track));
                    context.showPlayer();
                }
            });
        }
    }

}
