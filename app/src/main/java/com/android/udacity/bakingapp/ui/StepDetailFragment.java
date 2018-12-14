package com.android.udacity.bakingapp.ui;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.udacity.bakingapp.R;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class StepDetailFragment extends Fragment implements ExoPlayer.EventListener {

    public static final String DESCRIPTION = "description";
    public static final String VIDEO_URL = "video_url";
    public static final String THUMBNAIL_URL = "thumbnail_url";
    public static final String STEP_INDEX = "step_index";
    public static final String STEP_TOTAL = "step_total";

    final String PLAYBACK_POSITION = "mPlaybackPosition";
    final String CURRENT_WINDOW = "mCurrentWindow";
    final String PLAY_WHEN_READY = "mPlayWhenReady";

    private long mPlaybackPosition;
    private int mCurrentWindow;
    private boolean mPlayWhenReady = true;

    private String mDescription;
    private String mVideoUrl;
    private String mThumbnailUrl;
    private int mStepIndex;
    private int mStepTotal;

    private SimpleExoPlayer mExoPlayer;

    @BindBool(R.bool.use_grid_layout)
    boolean mIsTablet;

    @BindView(R.id.playerView)
    SimpleExoPlayerView mExoPlayerView;
    @BindView(R.id.step_description)
    TextView mStepDescriptionTextView;
    @BindView(R.id.step_details_container)
    LinearLayout mStepDetailsContainer;
    @BindView(R.id.step_detail_linear_layout)
    LinearLayout mLayoutRootView;
    @BindView(R.id.step_image)
    ImageView mStepImageView;
    @BindView(R.id.player_preview_overlay)
    ImageView mPlayerPreviewButton;
    @BindView(R.id.step_progression)
    TextView mStepProgression;

    public StepDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mDescription = savedInstanceState.getString(DESCRIPTION);
            mVideoUrl = savedInstanceState.getString(VIDEO_URL);
            mThumbnailUrl = savedInstanceState.getString(THUMBNAIL_URL);
            mPlaybackPosition = savedInstanceState.getLong(PLAYBACK_POSITION);
            mCurrentWindow = savedInstanceState.getInt(CURRENT_WINDOW);
            mPlayWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
            mStepIndex = savedInstanceState.getInt(STEP_INDEX);
            mStepTotal = savedInstanceState.getInt(STEP_TOTAL);
        }

        View rootView = inflater.inflate(R.layout.step_fragment_detail, container, false);
        ButterKnife.bind(this, rootView);

        if (!mIsTablet && getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            PrepareUiForFullScreen();
        }

        mStepDescriptionTextView.setText(mDescription);
        mStepProgression.setText(mStepIndex + " / " + mStepTotal);
        mStepProgression.setContentDescription(
                String.format(getString(R.string.step_progression_description),
                        mStepIndex, mStepTotal));

        initializeAlbumOrPreview();

        return rootView;
    }

    public void setStepDetails(String description, String videoUrl, String thumbnailUrl,
                               int stepIndex, int stepTotal) {
        mDescription = description;
        mVideoUrl = videoUrl;
        mThumbnailUrl = thumbnailUrl;
        mStepIndex = stepIndex;
        mStepTotal = stepTotal;
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putString(DESCRIPTION, mDescription);
        currentState.putString(VIDEO_URL, mVideoUrl);
        currentState.putString(THUMBNAIL_URL, mThumbnailUrl);
        currentState.putInt(STEP_INDEX, mStepIndex);
        currentState.putInt(STEP_TOTAL, mStepTotal);

        if (mExoPlayer != null) {
            mPlaybackPosition = mExoPlayer.getCurrentPosition();
            mCurrentWindow = mExoPlayer.getCurrentWindowIndex();
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();

            currentState.putLong(PLAYBACK_POSITION, mPlaybackPosition);
            currentState.putInt(CURRENT_WINDOW, mCurrentWindow);
            currentState.putBoolean(PLAY_WHEN_READY, mPlayWhenReady);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (!mIsTablet && getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            PrepareUiForFullScreen();
        } else {
            RestoreUiFromFullScreen();
        }
    }


    private void initializeAlbumOrPreview() {
        if (mThumbnailUrl == null || mThumbnailUrl.isEmpty()) {
            mStepImageView.setImageResource(R.drawable.image_placeholder);
        } else {
            Glide.with(getActivity())
                    .load(mThumbnailUrl)
                    .placeholder(R.drawable.image_placeholder)
                    .into(mStepImageView);
        }

        mStepImageView.setVisibility(View.VISIBLE);
        mStepImageView.setContentDescription(
                String.format(getString(R.string.step_image_description), mStepIndex));

        mExoPlayerView.setVisibility(View.GONE);

        if (mVideoUrl == null || mVideoUrl.isEmpty()) {
            mPlayerPreviewButton.setVisibility(View.GONE);
        } else {
            mPlayerPreviewButton.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.player_preview_overlay)
    public void initializePlayer() {
        mStepImageView.setVisibility(View.GONE);
        mPlayerPreviewButton.setVisibility(View.GONE);
        mExoPlayerView.setVisibility(View.VISIBLE);

        if (mExoPlayer == null) {
            RenderersFactory renderersFactory = new DefaultRenderersFactory(getActivity());
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
            mExoPlayer.addListener(this);

            mExoPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.setPlayWhenReady(mPlayWhenReady);
            mExoPlayer.seekTo(mCurrentWindow, mPlaybackPosition);
        }

        Uri mediaUri = Uri.parse(mVideoUrl);

        String userAgent = Util.getUserAgent(getActivity(), getString(R.string.app_name));
        DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent);

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, httpDataSourceFactory, extractorsFactory
                , null, null);
        mExoPlayer.prepare(mediaSource, false, false);
    }


    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void initializeFullScreenListeners() {
        final View decorView = getActivity().getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0) {
                    mExoPlayerView.showController();
                }
            }
        });

        mExoPlayerView.setControllerVisibilityListener(new PlaybackControlView.VisibilityListener() {
            @Override
            public void onVisibilityChange(int visibility) {
                if (visibility == View.GONE) {
                    hideSystemUI(decorView);
                    PrepareUiForFullScreen();
                }
            }
        });

        mStepImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSystemUI(decorView);
            }
        });
    }

    private void clearFullScreenListeners() {
        final View decorView = getActivity().getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(null);
        mExoPlayerView.setControllerVisibilityListener(null);
        mStepImageView.setOnClickListener(null);
    }


    private void hideSystemUI(View decorView) {
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void RestoreUiFromFullScreen() {
        mStepDetailsContainer.setVisibility(View.VISIBLE);
        clearFullScreenListeners();
    }

    private void PrepareUiForFullScreen() {
        mStepDetailsContainer.setVisibility(View.GONE);
        initializeFullScreenListeners();
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == ExoPlayer.STATE_ENDED) {
            mStepImageView.setVisibility(View.VISIBLE);
            mPlayerPreviewButton.setVisibility(View.VISIBLE);
            mExoPlayerView.setVisibility(View.GONE);
            mExoPlayer.seekTo(mCurrentWindow, 0);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        // Não será implementado.
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        // Não será implementado.
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        // Não será implementado.
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        // Não será implementado.
    }

    @Override
    public void onPositionDiscontinuity() {
        // Não será implementado.
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        // Não será implementado.
    }
}
