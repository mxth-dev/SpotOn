package at.dingbat.spoton.adapter;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;

import at.dingbat.spoton.activity.SearchActivity;
import at.dingbat.spoton.data.ParcelableArtist;
import at.dingbat.spoton.widget.ArtistListItem;
import at.dingbat.spoton.widget.SearchView;
import at.dingbat.spoton.widget.recyclerview.DataHolder;
import at.dingbat.spoton.widget.recyclerview.dataholder.ArtistListItemDataHolder;
import at.dingbat.spoton.widget.recyclerview.dataholder.SearchViewDataHolder;

/**
 * Created by bendix on 05.06.15.
 */
public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Parcelable {

    public static final int TYPE_ARTIST = 0;
    public static final int TYPE_SEARCH_HEADER = 1;

    private ArrayList<DataHolder> items;

    public SearchAdapter(ArrayList<DataHolder> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public SearchAdapter(Parcel parcel) {
        Parcelable[] arr = parcel.readParcelableArray(new ClassLoader() {
            @Override
            protected Class<?> findClass(String className) throws ClassNotFoundException {
                if(className.equals(ArtistListItemDataHolder.class.getName())) return ArtistListItemDataHolder.class;
                else if(className.equals(SearchViewDataHolder.class.getName())) return SearchViewDataHolder.class;
                return super.findClass(className);
            }
        });
        items = new ArrayList<DataHolder>();
        for(Parcelable p: arr) {
            items.add((DataHolder) p);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getItemViewId();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ARTIST:
                return new ArtistListItem.ViewHolder(new ArtistListItem(parent.getContext()));
            case TYPE_SEARCH_HEADER:
                return new SearchView.ViewHolder(new SearchView(parent.getContext()));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_ARTIST:
                ((ArtistListItem.ViewHolder)holder).item.setArtist(((ArtistListItemDataHolder)items.get(position)).artist);
                break;
            case TYPE_SEARCH_HEADER:
                ((SearchView.ViewHolder)holder).item.setHolder((SearchViewDataHolder)items.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void replaceAll(int offset, ArrayList<DataHolder> items) {
        this.items = new ArrayList<DataHolder>(this.items.subList(0, offset));
        this.items.addAll(items);
        for(DataHolder holder: items) {
            Log.d("", "Holder: "+holder.toString());
        }
        notifyItemRangeChanged(offset, this.items.size());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        Parcelable[] arr = new Parcelable[items.size()];
        for(int i = 0; i < items.size(); i++) {
            switch (getItemViewType(i)) {
                case TYPE_ARTIST:
                    arr[i] = (ArtistListItemDataHolder) items.get(i);
                    break;
                case TYPE_SEARCH_HEADER:
                    arr[i] = (SearchViewDataHolder) items.get(i);
                    break;
                default:
                    arr[i] = null;
                    break;
            }
        }
        parcel.writeParcelableArray(arr, flags);
    }

}
