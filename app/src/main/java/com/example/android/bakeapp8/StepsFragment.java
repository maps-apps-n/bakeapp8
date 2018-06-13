package com.example.android.bakeapp8;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StepsFragment extends Fragment {

    @Nullable
    @BindView(R.id.exoplay)SimpleExoPlayerView exo;
    @Nullable
    @BindView(R.id.description)TextView desc;
    @Nullable
    @BindView(R.id.appbar)AppBarLayout bar;
    @Nullable
    @BindView(R.id.toolbar)Toolbar tb;
    @Nullable
    @BindView(R.id.desc_tvbar)TextView tvtb;
    @Nullable
    @BindView(R.id.img_back)ImageButton backimg;
    @Nullable
    @BindView(R.id.desc_layout)LinearLayout linear;

    private static final String STEPS = "step";
    private static final String POSITION = "pos";
    private static final String FULLSCREEN = "fullscreen";
    private static final String NULL = "null";
    private static final String SLIDER_POS = "slider_position";

    private ArrayList<RecipesParcelableResp.sObjects> step;
    private String videoUrl;
    private String thumbnailUrl;
    private String description;
    private String descTxt;
    private int pos;
    private long exo_pos = 0;
    private SimpleExoPlayer exoPlayer;
    private Unbinder unbind;
    private boolean isFullScreen = false;
    private boolean isNull;
    private boolean StoppedExo = false;
    private long StopExo;

    public StepsFragment() { }

    public static StepsFragment newInstance(ArrayList<RecipesParcelableResp.sObjects> sList, int position) {

        StepsFragment steps = new StepsFragment();
        Bundle b = new Bundle();
        b.putParcelableArrayList(STEPS,sList);
        b.putInt(POSITION, position);

        if(position == -1){
            b.putBoolean(NULL,true);
        } else {
            b.putBoolean(NULL,false);
        }

        steps.setArguments(b);
        return steps;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        isNull = getArguments().getBoolean(NULL);
        if(!isNull) {
            step = getArguments().getParcelableArrayList(STEPS);
            pos = getArguments().getInt(POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_directions, container, false);
        unbind = ButterKnife.bind(this,v);

        checkFullScreen();

        if(savedInstanceState == null){
            isFullScreen = false;
        } else {
            isFullScreen = savedInstanceState.getBoolean(FULLSCREEN);
            exo_pos = savedInstanceState.getLong(SLIDER_POS);
        }
        if(!isNull) {
            videoUrl = step.get(pos).getVideoURL();
            description = step.get(pos).getShortDescription();
            descTxt = step.get(pos).getDescription();
            thumbnailUrl = step.get(pos).getThumbnailURL();
            if(tvtb != null) {
                tvtb.setText(description);
                desc.setText(descTxt);

                backimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().onBackPressed();
                    }
                }); }

            v.findViewById(R.id.exo_next).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isFullScreen) {
                        isFullScreen = true;
                        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    } else {
                        isFullScreen = false;
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    }
                }
            });
            if (!videoUrl.equals("")) {
                initializePlayer(Uri.parse(videoUrl));
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
            }
        } else {
            assert linear != null;
            linear.setVisibility(View.INVISIBLE);
        }
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FULLSCREEN, isFullScreen);
        if(exoPlayer != null) {
            outState.putLong(SLIDER_POS, exoPlayer.getCurrentPosition());
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        if(exoPlayer != null) {
            initializePlayer(Uri.parse(videoUrl));
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if(exoPlayer != null) {
            StopExo = exoPlayer.getCurrentPosition();
            StoppedExo = true;
            releasePlayer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
        unbind.unbind();
    }
    private void initializePlayer(Uri mediaUri){
        if(exoPlayer == null){
            LoadControl control = new DefaultLoadControl();
            TrackSelector select = new DefaultTrackSelector();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(),select,control);
            exo.setPlayer(exoPlayer);
            String userAgent = Util.getUserAgent(getContext(),"bakeapp");
            MediaSource source = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(),userAgent), new DefaultExtractorsFactory(),null,null);
            exoPlayer.prepare(source);
            if (exo_pos != 0 && !StoppedExo){
                exoPlayer.seekTo(exo_pos);
            } else {
                exoPlayer.seekTo(StopExo);
            }
        }
    }
    private void releasePlayer(){
        if(exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }
    public void checkFullScreen(){
        Configuration conf = new Configuration();
        if (conf.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isFullScreen = true;
        } else if (conf.orientation == Configuration.ORIENTATION_PORTRAIT){
            isFullScreen = false;
        }
    }
}
