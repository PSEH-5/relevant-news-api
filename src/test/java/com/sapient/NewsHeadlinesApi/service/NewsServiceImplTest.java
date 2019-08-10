package com.sapient.NewsHeadlinesApi.service;

import com.sapient.NewsHeadlinesApi.dto.NewsResponseDto;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewsServiceImplTest {

    public static final String API_KEY = "ccaf5d41cc5140c984818c344edcc14d";

    @Test
    public void getRelevantNewsTest(){
        NewsService newsService = new NewsServiceImpl();
        NewsResponseDto result = newsService.getRelevantNews("in", "sports", "rafael", API_KEY);
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.getArticleReturnDtos().size());
    }
}
