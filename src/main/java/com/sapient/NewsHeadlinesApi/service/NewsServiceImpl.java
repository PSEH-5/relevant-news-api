package com.sapient.NewsHeadlinesApi.service;

import com.sapient.NewsHeadlinesApi.dto.ArticleDto;
import com.sapient.NewsHeadlinesApi.dto.ArticleReturnDto;
import com.sapient.NewsHeadlinesApi.dto.NewsResponseDto;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class NewsServiceImpl implements NewsService{

    private static final Logger logger = LoggerFactory.getLogger(NewsServiceImpl.class);

    @Override
    public NewsResponseDto getRelevantNews(String country, String category, String searchText, String apiKey) {

        try {
            JSONObject newsJsonObject = getNewsFromNewAPI(country, category, apiKey);

            if (newsJsonObject != null) {
                NewsResponseDto newsResponseDto = new NewsResponseDto();
                newsResponseDto.setCountry(country);
                newsResponseDto.setCategory(category);
                newsResponseDto.setFilterKeyword(searchText);

                List<ArticleReturnDto> articleReturnDtos = new ArrayList<>();

                JSONArray jsonarr_1 = (JSONArray) newsJsonObject.get("articles");

                List<ArticleDto> articleDtos = extractArticleFromResponse(jsonarr_1);

                if (articleDtos != null && articleDtos.size() > 0) {
                    logger.info("The Size of the Article DTO is "+articleDtos.size());
                    for (ArticleDto articleDto : articleDtos) {

                        boolean skipNews = true;
                        if(articleDto.getTitle()!= null && articleDto.getTitle().toLowerCase().
                                contains(searchText.toLowerCase())){
                            logger.debug("Keyword matched the title, so don't ignore News");
                            skipNews = false;
                        }
                        if(articleDto.getDescription()!= null && articleDto.getDescription().toLowerCase().
                                contains(searchText.toLowerCase())){
                            logger.debug("Keyword matched the description, so don't ignore News");
                            skipNews = false;
                        }

                        if(!skipNews) {
                            ArticleReturnDto articleReturnDto = articleResponseMapper(articleDto);
                            articleReturnDtos.add(articleReturnDto);
                        }
                    }
                }

                if (articleReturnDtos != null && articleReturnDtos.size() > 0) {
                    logger.debug("The Size of the matched Articles is "+articleReturnDtos.size());
                    newsResponseDto.setArticleReturnDtos(articleReturnDtos);
                }else{
                    logger.debug("The Articles did not match so returning empty");
                    newsResponseDto.setArticleReturnDtos(new ArrayList<>());
                }

                return newsResponseDto;

            } else {
                return null;
            }
        }
        catch (Exception ex){
            logger.error("Exception while getting the news, Exception is "+ex.getMessage());
            return null;
        }
    }

    private ArticleReturnDto articleResponseMapper(ArticleDto articleDto){

        ArticleReturnDto articleReturnDto = new ArticleReturnDto();
        if(!articleDto.getTitle().isEmpty()){
            articleReturnDto.setTitle(articleDto.getTitle());
        }
        if(!articleDto.getDescription().isEmpty()){
            String desc = articleDto.getDescription();
            if(desc.length()>100){
                desc = desc.substring(0,99)+"...";
            }
            articleReturnDto.setDescription(desc);
        }
        if(!articleDto.getUrl().isEmpty()){
            articleReturnDto.setSourceUrl(articleDto.getUrl());
        }
        return articleReturnDto;
    }

    private List<ArticleDto> extractArticleFromResponse(JSONArray jsonObject){

        List<ArticleDto> articleDtos = new ArrayList<>();

        for (int i = 0; i < jsonObject.size(); i++) {
            ArticleDto articleDto = new ArticleDto();
            JSONObject jsonobj_1 = (JSONObject) jsonObject.get(i);

            if(jsonobj_1.containsKey("source")){
                JSONObject sourceObject = (JSONObject) jsonobj_1.get("source");
                if(sourceObject.containsKey("id")){
                    articleDto.setSourceId((String)sourceObject.get("id"));
                }
                if(sourceObject.containsKey("name")){
                    articleDto.setSourceName((String)sourceObject.get("name"));
                }
            }
            if(jsonobj_1.containsKey("author")){
                articleDto.setAuthor((String)jsonobj_1.get("author"));
            }
            if(jsonobj_1.containsKey("title")){
                articleDto.setTitle((String)jsonobj_1.get("title"));
            }
            if(jsonobj_1.containsKey("description")){
                articleDto.setDescription((String)jsonobj_1.get("description"));
            }
            if(jsonobj_1.containsKey("url")){
                articleDto.setUrl((String)jsonobj_1.get("url"));
            }
            if(jsonobj_1.containsKey("content")){
                articleDto.setContent((String)jsonobj_1.get("content"));
            }
            logger.debug("Article DTO is {}",articleDto.toString());
            articleDtos.add(articleDto);
        }
        return articleDtos;

    }
    private JSONObject getNewsFromNewAPI(String country, String category, String apiKey) {
        try {

            URL url = new URL("https://newsapi.org/v2/top-headlines?country=" + country +
                    "&category=" + category +
                    "&apiKey=" + apiKey);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();

            if(responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: "+responseCode);
            }else {
                Scanner sc = new Scanner(url.openStream());
                String inline = "";
                while(sc.hasNext())
                {
                    inline+=sc.nextLine();
                }
                logger.debug("JSON data in string format");
                logger.debug(inline);
                sc.close();

                JSONParser parse = new JSONParser();
                JSONObject jsonObject = (JSONObject)parse.parse(inline);

                return jsonObject;
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
