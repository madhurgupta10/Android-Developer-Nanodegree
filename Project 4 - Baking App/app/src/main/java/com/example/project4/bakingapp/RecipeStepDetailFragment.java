package com.example.project4.bakingapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project4.bakingapp.model.Recipe;
import com.example.project4.bakingapp.model.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

public class RecipeStepDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    private static Step step;
    SimpleExoPlayerView simpleExoPlayerView;
    SimpleExoPlayer exoPlayer;
    TextView descriptionTextView;
    String mediaUrl;
    int ORIENTATION;
    Recipe recipe;

    public RecipeStepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ORIENTATION = this.getActivity().getResources().getConfiguration().orientation;

        if (getArguments().containsKey(ARG_ITEM_ID)) {
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (step != null) {
            descriptionTextView = view.findViewById(R.id.recipestep_detail);
            boolean is_tablet = getResources().getBoolean(R.bool.is_tablet);
            if (ORIENTATION == 1 || is_tablet) {
                descriptionTextView.setText(step.getDescription());
            }
            simpleExoPlayerView = view.findViewById(R.id.exo_player);
            mediaUrl = step.getVideoURL();
            if (mediaUrl.length() == 0) {
                mediaUrl = step.getThumbnailURL();
            }
            if (mediaUrl.length() != 0) {
                initializePlayer(mediaUrl);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
            container.clearDisappearingChildren();
        }

        return inflater.inflate(R.layout.recipestep_detail, container, false);
    }

    private void initializePlayer(String mediaUrl) {

        TrackSelector trackSelector = new DefaultTrackSelector();
        exoPlayer = ExoPlayerFactory.newSimpleInstance(this.getContext(), trackSelector);
        simpleExoPlayerView.setPlayer(exoPlayer);

        String userAgent = Util.getUserAgent(this.getContext(), "BakingApp");
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mediaUrl), new DefaultDataSourceFactory(
                this.getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(true);

    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("mediaUrl", mediaUrl);
    }


}