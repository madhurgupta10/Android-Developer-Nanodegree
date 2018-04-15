package com.example.project4.bakingapp;

import android.app.Activity;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project4.bakingapp.model.Recipe;
import com.example.project4.bakingapp.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import static com.example.project4.bakingapp.RecipeStepListActivity.recipe;

public class RecipeStepDetailFragment extends Fragment {

    private static Step step;
    SimpleExoPlayerView simpleExoPlayerView;
    SimpleExoPlayer exoPlayer;
    int ORIENTATION;

    public RecipeStepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ORIENTATION = this.getActivity().getResources().getConfiguration().orientation;

        if (getArguments() != null) {
            Bundle args = getArguments();
            step = (Step) args.getSerializable("stepBundle");
            recipe = (Recipe) args.getSerializable("recipeBundle");

            Activity activity = this.getActivity();

            if (ORIENTATION == 1) {
                CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
                ImageView imageView = activity.findViewById(R.id.img_toolbar);

                this.getActivity().setTitle(recipe.getName());

                if (recipe.getImage() != null && recipe.getImage().length() > 0 && imageView != null) {
                    Picasso.with(this.getContext())
                            .load(recipe.getImage())
                            .fit()
                            .centerCrop()
                            .into(imageView);
                }

                if (appBarLayout != null) {
                    appBarLayout.setTitle(recipe.getName());
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipestep_detail, container, false);

        if (step != null) {
            final int ORIENTATION = this.getActivity().getResources().getConfiguration().orientation;
            if (ORIENTATION == 1) {
                ((TextView) rootView.findViewById(R.id.recipestep_detail)).setText(step.getDescription());
            }
            simpleExoPlayerView = rootView.findViewById(R.id.exo_player);
            String mediaUrl = step.getVideoURL();
            if (mediaUrl.length() == 0) {
                mediaUrl = step.getThumbnailURL();
            }
            if (mediaUrl.length() != 0) {
                initializePlayer(mediaUrl);
            }
        }

        return rootView;
    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mPlayerFragment.pause();
//    }

    private void initializePlayer(String mediaUrl) {

        TrackSelector trackSelector = new DefaultTrackSelector();
        exoPlayer = ExoPlayerFactory.newSimpleInstance(this.getContext(), trackSelector);
        simpleExoPlayerView.setPlayer(exoPlayer);

        String userAgent = Util.getUserAgent(this.getContext(), "ClassicalMusicQuiz");
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mediaUrl), new DefaultDataSourceFactory(
                this.getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(true);

    }
}