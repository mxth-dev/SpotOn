package at.dingbat.spoton.activity;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import at.dingbat.spoton.R;
import at.dingbat.spoton.models.ParcelableArtist;
import at.dingbat.spoton.fragment.ArtistFragment;
import at.dingbat.spoton.fragment.SearchFragment;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;


public class MainActivity extends ActionBarActivity {

    public static final String PARCEL_RECYCLER_ADAPTER = "recycler_adapter";
    public static final String PARCEL_TOOLBAR_VISIBLE = "toolbar_visible";

    private SpotifyApi api;
    private SpotifyService spotify;

    private Toolbar toolbar;

    private ValueAnimator hideToolbar;
    private ValueAnimator showToolbar;

    private boolean showingToolbar = false;
    private boolean hidingToolbar = false;
    private boolean toolbarVisible = false;

    private int colorPrimary;

    private Toast toast;

    private SearchFragment searchFragment;
    private ArtistFragment artistFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        api = new SpotifyApi();
        spotify = api.getService();

        toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if(savedInstanceState != null) {
            boolean tv = savedInstanceState.getBoolean(PARCEL_TOOLBAR_VISIBLE);
            if(tv) showToolbar();
            else hideToolbar();
        } else {
            showSearch();
        }

        colorPrimary = getResources().getColor(R.color.colorPrimary);

    }

    public void showSearch() {
        searchFragment = new SearchFragment();
        getFragmentManager().beginTransaction().replace(R.id.activity_main_fragment, searchFragment, "Search").commit();
    }

    public void showArtist(ParcelableArtist artist) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("artist", artist);
        artistFragment = new ArtistFragment();
        artistFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.activity_main_fragment, artistFragment, "Artist").addToBackStack("Artist").commit();
    }

    public void search(String term) {
        if(searchFragment != null) searchFragment.search(term);
    }

    public SpotifyService getSpotify() {
        return spotify;
    }

    public void setToolbarText(String text) {
        toolbar.setTitle(text);
    }

    public void showToolbarBackArrow(boolean show) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(show);
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
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelable(PARCEL_RECYCLER_ADAPTER, recycler_adapter);
        outState.putBoolean(PARCEL_TOOLBAR_VISIBLE, toolbarVisible);
    }

    public SpotifyService getSpotifyService() {
        return spotify;
    }
}
