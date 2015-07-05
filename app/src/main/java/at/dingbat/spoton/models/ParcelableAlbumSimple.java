package at.dingbat.spoton.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by Max on 7/5/2015.
 */
public class ParcelableAlbumSimple extends AlbumSimple implements Parcelable {

    //Relevant fields from AlbumSimple
    public String id;
    public String name;
    public ArrayList<ParcelableImage> images;

    public ParcelableAlbumSimple(AlbumSimple album) {
        this.id = album.id;
        this.name = album.name;
        this.images = new ArrayList<>();
        for(Image i: album.images) {
            this.images.add(new ParcelableImage(i));
        }
    }

    public ParcelableAlbumSimple(Parcel parcel) {
        this.id = parcel.readString();
        this.name = parcel.readString();
        this.images = new ArrayList<>();
        parcel.readTypedList(this.images, ParcelableImage.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeTypedList(images);
    }

}
