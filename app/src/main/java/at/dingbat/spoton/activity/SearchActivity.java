package at.dingbat.spoton.activity;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import at.dingbat.spoton.R;
import at.dingbat.spoton.adapter.SearchAdapter;
import at.dingbat.spoton.data.ParcelableArtist;
import at.dingbat.spoton.widget.recyclerview.DataHolder;
import at.dingbat.spoton.widget.recyclerview.dataholder.ArtistListItemDataHolder;
import at.dingbat.spoton.widget.recyclerview.dataholder.SearchViewDataHolder;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Artists;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SearchActivity extends ActionBarActivity {

    public static final String PARCEL_RECYCLER_ADAPTER = "recycler_adapter";
    public static final String PARCEL_TOOLBAR_VISIBLE = "toolbarVisible";

    private SpotifyApi api;
    private SpotifyService spotify;

    private Toolbar toolbar;

    private RecyclerView recycler;
    private LinearLayoutManager recycler_layout;
    private SearchAdapter recycler_adapter;

    private ValueAnimator hideToolbar;
    private ValueAnimator showToolbar;

    private boolean showingToolbar = false;
    private boolean hidingToolbar = false;
    private boolean toolbarVisible = false;

    private int colorPrimary;

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        api = new SpotifyApi();
        spotify = api.getService();

        toolbar = (Toolbar) findViewById(R.id.activity_search_toolbar);
        setSupportActionBar(toolbar);

        recycler = (RecyclerView) findViewById(R.id.activity_search_recycler);
        recycler_layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(recycler_layout);

        if(savedInstanceState != null) {
            recycler_adapter = savedInstanceState.getParcelable(PARCEL_RECYCLER_ADAPTER);
            boolean tv = savedInstanceState.getBoolean(PARCEL_TOOLBAR_VISIBLE);
            if(tv) showToolbar();
            else hideToolbar();
        } else {
            recycler_adapter = new SearchAdapter(new ArrayList<DataHolder>() {{
                add(new SearchViewDataHolder());
            }});
        }

        recycler.setAdapter(recycler_adapter);

        colorPrimary = getResources().getColor(R.color.colorPrimary);

        recycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int position = recycler_layout.findFirstVisibleItemPosition();
                if (position == 0) {
                    View v = recycler_layout.findViewByPosition(position);
                    if (v.getTop() >= 0) {
                        hideToolbar();
                    } else {
                        showToolbar();
                    }
                }
            }
        });

    }

    public void showToolbar() {
        if(Color.alpha(((ColorDrawable)toolbar.getBackground()).getColor()) == 0 && !toolbarVisible) {
            toolbarVisible = true;
            showingToolbar = true;
            int start_value = 0;
            int time = 200;
            if (hidingToolbar) {
                hidingToolbar = false;
                start_value = (int) hideToolbar.getAnimatedValue();
                time = (int) hideToolbar.getCurrentPlayTime();
                hideToolbar.cancel();
            }
            showToolbar = ValueAnimator.ofInt(start_value, 255);
            showToolbar.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int value = (Integer) valueAnimator.getAnimatedValue();

                    int color = Color.argb(value, Color.red(colorPrimary), Color.green(colorPrimary), Color.blue(colorPrimary));
                    toolbar.setBackgroundColor(color);

                    if(value == 255) showingToolbar = false;
                }
            });
            showToolbar.setDuration(time);
            showToolbar.start();
        }
    }

    public void hideToolbar() {
        if(Color.alpha(((ColorDrawable)toolbar.getBackground()).getColor()) == 255 && toolbarVisible) {
            toolbarVisible = false;
            hidingToolbar = true;
            int start_value = 255;
            int time = 200;
            if (showingToolbar) {
                showingToolbar = false;
                start_value = (int) showToolbar.getAnimatedValue();
                time = (int) showToolbar.getCurrentPlayTime();
                showToolbar.cancel();
            }
            hideToolbar = ValueAnimator.ofInt(start_value, 0);
            hideToolbar.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int value = (Integer) valueAnimator.getAnimatedValue();

                    int color = Color.argb(value, Color.red(colorPrimary), Color.green(colorPrimary), Color.blue(colorPrimary));
                    toolbar.setBackgroundColor(color);

                    if(value == 0) hidingToolbar = false;
                }
            });
            hideToolbar.setDuration(time);
            hideToolbar.start();
        }
    }

    public void search(String term) {
        spotify.searchArtists(term, new Callback<ArtistsPager>() {
            @Override
            public void success(ArtistsPager artistsPager, Response response) {
                final ArrayList<DataHolder> items = new ArrayList<DataHolder>();
                for (Artist artist : artistsPager.artists.items) {
                    items.add(new ArtistListItemDataHolder(new ParcelableArtist(artist)));
                }
                SearchActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(items.size() == 0) {
                            showToast("Nothing found.", Toast.LENGTH_SHORT);
                        }
                        recycler_adapter.replaceAll(1, items);
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("", "Error: "+error.getMessage());
            }
        });
    }

    public void showToast(String message, int duration) {
        if(toast != null) toast.cancel();
        toast = Toast.makeText(this, message, duration);
        toast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PARCEL_RECYCLER_ADAPTER, recycler_adapter);
        outState.putBoolean(PARCEL_TOOLBAR_VISIBLE, toolbarVisible);
    }

    public SpotifyService getSpotifyService() {
        return spotify;
    }
}
