package at.dingbat.spoton.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by bendix on 06.06.15.
 */
public class ParcelableArtist extends Artist implements Parcelable {

    public ParcelableFollowers followers;
    public List<ParcelableImage> images;

    public ParcelableArtist(Artist artist) {
        this.external_urls = artist.external_urls;
        this.href = artist.href;
        this.id = artist.id;
        this.name = artist.name;
        this.type = artist.type;
        this.uri = artist.uri;
        this.followers = new ParcelableFollowers(artist.followers);
        this.genres = artist.genres;
        this.images = new ArrayList<ParcelableImage>();
        for(Image i: artist.images) {
            this.images.add(new ParcelableImage(i));
        }
        this.popularity = artist.popularity;
    }

    public ParcelableArtist(Parcel parcel) {
        parcel.readMap(this.external_urls, Map.class.getClassLoader());
        this.href = parcel.readString();
        this.id = parcel.readString();
        this.name = parcel.readString();
        this.type = parcel.readString();
        this.uri = parcel.readString();

        this.followers = parcel.readParcelable(ParcelableFollowers.class.getClassLoader());
        this.genres = new ArrayList<String>();
        parcel.readStringList(this.genres);
        Parcelable[] image_array = parcel.readParcelableArray(ParcelableImage.class.getClassLoader());
        images = new ArrayList<ParcelableImage>();
        for(Parcelable i: image_array) {
            images.add((ParcelableImage)i);
        }
        this.popularity = parcel.readInt();
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


        parcel.writeParcelable(followers, flags);
        parcel.writeStringList(genres);
        Parcelable[] image_array = new Parcelable[images.size()];
        for(int i = 0; i < images.size(); i++) image_array[i] = images.get(i);
        parcel.writeParcelableArray(image_array, flags);
        parcel.writeInt(popularity);
    }

}
