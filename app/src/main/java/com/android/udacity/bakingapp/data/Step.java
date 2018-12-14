package com.android.udacity.bakingapp.data;


// Classe que representa todos os dados referentes a um passo do preparo de uma receita
public class Step {
    private long mId;
    private String mShortDescription;
    private String mDescription;
    private String mVideoUrl;
    private String mThumbnailUrl;

    public Step(long id, String shortDescription, String description, String videoUrl,
                String thumbnailUrl) {
        this.mId = id;
        this.mShortDescription = shortDescription;
        this.mDescription = description;
        this.mVideoUrl = videoUrl;
        this.mThumbnailUrl = thumbnailUrl;
    }

    public long getId() {
        return this.mId;
    }

    public String getShortDescription() {
        return this.mShortDescription;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public String getVideoUrl() {
        return this.mVideoUrl;
    }

    public String getThumbnailUrl() {
        return this.mThumbnailUrl;
    }
}