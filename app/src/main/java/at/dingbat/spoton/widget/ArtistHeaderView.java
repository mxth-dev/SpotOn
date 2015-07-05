package at.dingbat.spoton.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import at.dingbat.spoton.R;
import at.dingbat.spoton.models.ParcelableArtist;

/**
 * Created by bendix on 21.06.15.
 */
public class ArtistHeaderView extends RelativeLayout {

    private Context context;

    private ImageView image;
    private TextView title;

    private ParcelableArtist artist;

    private boolean initialized = false;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ArtistHeaderView item;
        public ViewHolder(ArtistHeaderView itemView) {
            super(itemView);
            this.item = itemView;
        }
    }

    public ArtistHeaderView(Context context) {
        super(context);

        this.context = context;

        inflate(context, R.layout.widget_artist_header, this);

        image = (ImageView) findViewById(R.id.widget_artist_header_image);
        title = (TextView) findViewById(R.id.widget_artist_header_title);

        initialized = true;

        if(artist != null) setArtist(artist);

    }

    public void setArtist(ParcelableArtist artist) {
        this.artist = artist;
        if(initialized) {
            if (artist.images.size() > 0) {
                Picasso.with(context).load(artist.images.get(0).url).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Palette p = Palette.generate(bitmap);
                        if(p.getDarkMutedSwatch() != null) image.setColorFilter(p.getDarkMutedSwatch().getRgb(), PorterDuff.Mode.MULTIPLY);
                        image.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }
            title.setText(artist.name);
        }
    }

}
