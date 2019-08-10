package com.sapient.NewsHeadlinesApi.controller;

import org.codehaus.jackson.map.ObjectMapper;
import com.sapient.NewsHeadlinesApi.dto.NewsResponseDto;
import com.sapient.NewsHeadlinesApi.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.web.bind.annotation.RestController()
@RequestMapping("/news-api/v1")
public class RestController {

    @Autowired
    NewsService newsService;

    public static final String API_KEY = "ccaf5d41cc5140c984818c344edcc14d";

    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    /**
     * Health Check.
     *
     * @return the status
     */
    @GetMapping("/healthCheck")
    public ResponseEntity<String> healthCheck(){
        logger.info("News API is Live and Running");
        return new ResponseEntity<>("News API is live ", HttpStatus.OK);
    }

    /**
     * Get News.
     *
     * @param keyword the search keyword
     * @param country the country of the news
     * @param category the category of the news
     * @return the News Response entity
     * @throws ResourceNotFoundException the resource not found exception
     */
    @GetMapping(value="/getNews", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getNews(@RequestParam(value = "keyword") String searchText,
                                          @RequestParam(value = "country") String country,
                                          @RequestParam(value = "category") String category){
        try {
            if (!searchText.isEmpty() && !country.isEmpty() && !category.isEmpty()) {
                logger.info("The Inputs are Keyword - {}, Country - {}, Category - {}", searchText, country, category);
                NewsResponseDto newsResponseDto = newsService.getRelevantNews(country, category, searchText, API_KEY);
                ObjectMapper mapper = new ObjectMapper();

                if(newsResponseDto != null) {
                    logger.info("Response is {}", mapper.defaultPrettyPrintingWriter().
                            writeValueAsString(newsResponseDto));
                    return new ResponseEntity<>(mapper.defaultPrettyPrintingWriter().
                            writeValueAsString(newsResponseDto), HttpStatus.OK);
                }else{
                    logger.error("Error: Something went wrong while getting the news," +
                            " Try after sometime ");
                    return new ResponseEntity<>("Error: Something went wrong while getting the news," +
                            " Try after sometime", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                logger.info("Please enter all the required parameters, eg: keyword=rafael&country=in&" +
                        "category=sports]}");
                return new ResponseEntity<>("Error: Please enter all the required parameters, " +
                        "eg: keyword=rafael&country=in&category=sports]}", HttpStatus.BAD_REQUEST);
            }
        }catch(Exception ex){
            logger.error("Error: Something went wrong while getting the news," +
                    " Try after sometime, Exception is "+ex.getMessage());
            return new ResponseEntity<>("Error: Something went wrong while getting the news," +
                    " Try after sometime", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
