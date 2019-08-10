package com.sapient.NewsHeadlinesApi.dto;

import java.util.List;

public class NewsResponseDto {

    private String country;
    private String category;
    private String filterKeyword;
    private List<ArticleReturnDto> articleReturnDtos;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<ArticleReturnDto> getArticleReturnDtos() {
        return articleReturnDtos;
    }

    public void setArticleReturnDtos(List<ArticleReturnDto> articleReturnDtos) {
        this.articleReturnDtos = articleReturnDtos;
    }

    public String getFilterKeyword() {
        return filterKeyword;
    }

    public void setFilterKeyword(String filterKeyword) {
        this.filterKeyword = filterKeyword;
    }

    @Override
    public String toString() {
        return "Response " +
                "Country='" + country + '\'' +
                ", Category='" + category + '\'' +
                ", Filter Keyword='" + filterKeyword + '\'' +
                ", Articles are =" + articleReturnDtos +
                '}';
    }
}

