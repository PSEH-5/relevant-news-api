package com.sapient.NewsHeadlinesApi.service;

import com.sapient.NewsHeadlinesApi.dto.NewsResponseDto;
import org.springframework.stereotype.Component;

@Component
public interface NewsService {

    NewsResponseDto getRelevantNews(String country, String category,
                                           String searchText, String apiKey);
}
