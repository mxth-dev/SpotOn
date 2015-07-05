package at.dingbat.spoton.widget.recyclerview.dataholder;

import android.os.Parcel;
import android.os.Parcelable;

import at.dingbat.spoton.adapter.Adapter;
import at.dingbat.spoton.models.ParcelableArtist;
import at.dingbat.spoton.widget.recyclerview.DataHolder;

/**
 * Created by bendix on 21.06.15.
 */
public class ArtistHeaderViewDataHolder implements DataHolder, Parcelable {

    public ParcelableArtist artist;

    public ArtistHeaderViewDataHolder(Parcel parcel) {
        this.artist = parcel.readParcelable(ParcelableArtist.class.getClassLoader());
    }

    public ArtistHeaderViewDataHolder(ParcelableArtist artist) {
        this.artist = artist;
    }

    @Override
    public int getItemViewId() {
        return Adapter.TYPE_ARTIST_HEADER;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(artist, flags);
    }

}
