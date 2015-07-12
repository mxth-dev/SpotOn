package at.dingbat.spoton.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import at.dingbat.spoton.R;
import at.dingbat.spoton.activity.MainActivity;
import at.dingbat.spoton.adapter.Adapter;
import at.dingbat.spoton.models.ParcelableArtist;
import at.dingbat.spoton.widget.recyclerview.DataHolder;
import at.dingbat.spoton.widget.recyclerview.dataholder.ArtistListItemDataHolder;
import at.dingbat.spoton.widget.recyclerview.dataholder.SearchViewDataHolder;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    public static final String TAG_ADAPTER = "adapter";
    public static final String TAG_TOOLBAR_VISIBLE = "isToolbarVisible";

    private MainActivity context;
    private SpotifyService spotify;

    private RelativeLayout root;

    private RecyclerView recycler;
    private LinearLayoutManager recycler_layout;
    private Adapter recycler_adapter;

    public boolean isToolbarVisible;

    public SearchFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = (MainActivity) getActivity();
        spotify = context.getSpotify();

        context.setToolbarText(getResources().getString(R.string.app_name));
        context.showToolbarBackArrow(false);

        if(root == null) {
            root = (RelativeLayout) inflater.inflate(R.layout.fragment_search, container, false);

            recycler = (RecyclerView) root.findViewById(R.id.fragment_search_recycler);
            recycler_layout = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            recycler.setLayoutManager(recycler_layout);

            if(savedInstanceState != null) {
                recycler_adapter = savedInstanceState.getParcelable(TAG_ADAPTER);
                isToolbarVisible = savedInstanceState.getBoolean(TAG_TOOLBAR_VISIBLE);
                if(isToolbarVisible) context.showToolbar();
                else context.hideToolbar();
            } else {
                recycler_adapter = new Adapter(new ArrayList<DataHolder>() {{
                    add(new SearchViewDataHolder());
                }});
            }

            recycler.setAdapter(recycler_adapter);

            recycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    int position = recycler_layout.findFirstVisibleItemPosition();
                    if (position == 0) {
                        View v = recycler_layout.findViewByPosition(position);
                        if (v.getTop() >= 0) {
                            isToolbarVisible = false;
                            context.hideToolbar();
                        } else {
                            isToolbarVisible = true;
                            context.showToolbar();
                        }
                    }
                }
            });



        }

        if(isToolbarVisible) context.showToolbar();
        else context.hideToolbar();

        return root;
    }

    public void search(String term) {
        spotify.searchArtists(term, new Callback<ArtistsPager>() {
            @Override
            public void success(ArtistsPager artistsPager, Response response) {
                final ArrayList<DataHolder> items = new ArrayList<DataHolder>();
                for (Artist artist : artistsPager.artists.items) {
                    items.add(new ArtistListItemDataHolder(new ParcelableArtist(artist)));
                }
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (items.size() == 0) {
                            context.showToast("Artist not found.", Toast.LENGTH_SHORT);
                        }
                        recycler_adapter.replaceAll(1, items);
                        recycler_adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("", "Error: " + error.getMessage());
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(TAG_ADAPTER, recycler_adapter);
        outState.putBoolean(TAG_TOOLBAR_VISIBLE, isToolbarVisible);
    }
}
