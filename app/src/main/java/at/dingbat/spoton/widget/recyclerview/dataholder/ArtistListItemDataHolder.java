package at.dingbat.spoton.widget.recyclerview.dataholder;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.AbstractList;

import at.dingbat.spoton.adapter.SearchAdapter;
import at.dingbat.spoton.data.ParcelableArtist;
import at.dingbat.spoton.widget.ArtistListItem;
import at.dingbat.spoton.widget.recyclerview.DataHolder;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by bendix on 05.06.15.
 */
public class ArtistListItemDataHolder implements DataHolder, Parcelable {

    public ParcelableArtist artist;

    public ArtistListItemDataHolder(ParcelableArtist artist) {
        this.artist = artist;
    }

    public ArtistListItemDataHolder(Parcel parcel) {
        this.artist = parcel.readParcelable(ParcelableArtist.class.getClassLoader());
    }

    @Override
    public int getItemViewId() {
        return SearchAdapter.TYPE_ARTIST;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(artist, i);
    }

}
