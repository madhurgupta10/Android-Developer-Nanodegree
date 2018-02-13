package com.example.project2.popularmoviesstage1.model;

public class Movie {

    public String title;
    public String posterUrl;
    public String overview;
    public Integer userRating;
    public String releaseDate;

    public Movie() {
    }

    public Movie(String title, String posterUrl, String overview, Integer userRating, String releaseDate) {
        this.title = title;
        this.posterUrl = posterUrl;
        this.overview = overview;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Integer getUserRating() {
        return userRating;
    }

    public void setUserRating(Integer userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
