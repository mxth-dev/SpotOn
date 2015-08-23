package at.dingbat.spoton.models;

import android.os.Parcel;
import android.os.Parcelable;

import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Max on 7/5/2015.
 */
public class ParcelableTrack extends Track implements Parcelable {

    //Relevant fields from Track/TrackSimple
    public String id;
    public String name;
    public String preview_url;
    public long duration_ms;
    public ParcelableAlbumSimple album;
    public ParcelableArtistSimple artist;

    public ParcelableTrack(Track track) {
        this.id = track.id;
        this.name = track.name;
        this.preview_url = track.preview_url;
        this.duration_ms = track.duration_ms;
        this.album = new ParcelableAlbumSimple(track.album);
        this.artist = new ParcelableArtistSimple(track.artists.get(0));
    }

    public ParcelableTrack(Parcel parcel) {
        this.id = parcel.readString();
        this.name = parcel.readString();
        this.preview_url = parcel.readString();
        this.duration_ms = parcel.readLong();
        this.album = parcel.readParcelable(ParcelableAlbumSimple.class.getClassLoader());
        this.artist = parcel.readParcelable(ParcelableArtistSimple.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(preview_url);
        dest.writeLong(duration_ms);
        dest.writeParcelable(album, flags);
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public ParcelableTrack createFromParcel(Parcel parcel) {
            return new ParcelableTrack(parcel);
        }

        @Override
        public ParcelableTrack[] newArray(int i) {
            return new ParcelableTrack[i];
        }
    };

}
