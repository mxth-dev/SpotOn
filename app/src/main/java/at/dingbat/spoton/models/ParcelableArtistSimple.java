package at.dingbat.spoton.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by bendix on 06.06.15.
 */
public class ParcelableArtistSimple extends ArtistSimple implements Parcelable {

    public List<ParcelableImage> images;

    public ParcelableArtistSimple(ArtistSimple artist) {
        this.external_urls = artist.external_urls;
        this.href = artist.href;
        this.id = artist.id;
        this.name = artist.name;
        this.type = artist.type;
        this.uri = artist.uri;
        this.images = new ArrayList<ParcelableImage>();
    }

    public ParcelableArtistSimple(Parcel parcel) {
        parcel.readMap(this.external_urls, Map.class.getClassLoader());
        this.href = parcel.readString();
        this.id = parcel.readString();
        this.name = parcel.readString();
        this.type = parcel.readString();
        this.uri = parcel.readString();

        Parcelable[] image_array = parcel.readParcelableArray(ParcelableImage.class.getClassLoader());
        images = new ArrayList<ParcelableImage>();
        for(Parcelable i: image_array) {
            images.add((ParcelableImage)i);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeMap(external_urls);
        parcel.writeString(href);
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(type);
        parcel.writeString(uri);

        Parcelable[] image_array = new Parcelable[images.size()];
        for(int i = 0; i < images.size(); i++) image_array[i] = images.get(i);
        parcel.writeParcelableArray(image_array, flags);
    }

}
