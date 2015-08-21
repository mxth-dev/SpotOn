package at.dingbat.spoton.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import at.dingbat.spoton.R;
import at.dingbat.spoton.activity.MainActivity;
import at.dingbat.spoton.models.ParcelableTrack;
import at.dingbat.spoton.service.PlayerService;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment {

    private MainActivity context;
    private PlayerService service;

    private RelativeLayout root;
    private ImageView background;
    private TextView text_artist;
    private TextView text_album;
    private TextView text_title;
    private FloatingActionButton button_play;
    private Button button_previous;
    private Button button_next;
    private SeekBar seekbar;

    private ParcelableTrack track;

    public PlayerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = (MainActivity) getActivity();

        root = (RelativeLayout) inflater.inflate(R.layout.fragment_player, container, false);
        background = (ImageView) root.findViewById(R.id.fragment_player_background);
        text_artist = (TextView) root.findViewById(R.id.fragment_player_artist);
        text_album = (TextView) root.findViewById(R.id.fragment_player_album);
        text_title = (TextView) root.findViewById(R.id.fragment_player_title);
        button_play = (FloatingActionButton) root.findViewById(R.id.fragment_player_button_play);
        button_previous = (Button) root.findViewById(R.id.fragment_player_button_previous);
        button_next = (Button) root.findViewById(R.id.fragment_player_button_next);
        seekbar = (SeekBar) root.findViewById(R.id.fragment_player_seekbar);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                service.setPosition((int)(service.getDuration()*((float)seekbar.getProgress()/100f)));
            }
        });

        service = context.getService();

        setTrack(service.getCurrentTrack());

        service.setOnTrackChanged(new Runnable() {
            @Override
            public void run() {
                setTrack(service.getCurrentTrack());
            }
        });

        button_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(service.isPlaying()) {
                    service.pause();
                    button_play.setImageResource(R.mipmap.ic_play_arrow_white_36dp);
                } else {
                    service.resume();
                    button_play.setImageResource(R.mipmap.ic_pause_white_36dp);
                }
            }
        });

        button_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                service.previousTrack();
            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                service.nextTrack();
            }
        });

        return root;
    }

    private void setTrack(ParcelableTrack track) {
        this.track = track;
        if(track.album.images.size() > 0) Picasso.with(getActivity()).load(track.album.images.get(0).url).into(background);
        else background.setImageResource(R.mipmap.ic_placeholder);
        text_artist.setText(track.artist.name);
        text_album.setText(track.album.name);
        text_title.setText(track.name);

        if(service.isPlaying()) button_play.setImageResource(R.mipmap.ic_pause_white_36dp);
        else button_play.setImageResource(R.mipmap.ic_play_arrow_white_36dp);

        if(service.isFirst()) button_previous.setVisibility(View.GONE);
        else button_previous.setVisibility(View.VISIBLE);
        if(service.isLast()) button_next.setVisibility(View.GONE);
        else button_next.setVisibility(View.VISIBLE);
    }

}
