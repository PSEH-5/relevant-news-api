package com.sapient.NewsHeadlinesApi.dto;

public class ArticleReturnDto {

    private String title;
    private String description;
    private String sourceUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    @Override
    public String toString() {
        return "ArticleReturnDto{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", sourceUrl='" + sourceUrl + '\'' +
                '}';
    }
}
