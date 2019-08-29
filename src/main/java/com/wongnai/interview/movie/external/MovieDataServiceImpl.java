package com.wongnai.interview.movie.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wongnai.interview.util.ExponentialBackOff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class MovieDataServiceImpl implements MovieDataService {

    public static final int RETRIES = 5;
    public static final String MOVIE_DATA_URL
            = "https://raw.githubusercontent.com/prust/wikipedia-movie-data/master/movies.json";

    @Autowired
    private RestOperations restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private ExponentialBackOff exponentialBackOff;

    @PostConstruct
    public void postConstruct() {
        this.exponentialBackOff = new ExponentialBackOff(RETRIES,
                ExponentialBackOff.DEFAULT_WAIT_TIME_IN_MILLI,
                ExponentialBackOff.DEFAULT_MAXIMUM_WAIT_TIME_IN_MILLI);
    }

    @Override
    public MoviesResponse fetchAll() {
        //TODO:
        // Step 1 => Implement this method to download data from MOVIE_DATA_URL and fix any error you may found.
        // Please noted that you must only read data remotely and only from given source,
        // do not download and use local file or put the file anywhere else.
        exponentialBackOff.reset();
        while (exponentialBackOff.shouldRetry()) {
            try {
                String jsonResponse = restTemplate.getForEntity(this.MOVIE_DATA_URL, String.class).getBody();
                MoviesResponse moviesResponse = new MoviesResponse();
                try {
                    moviesResponse = objectMapper.readValue(jsonResponse, MoviesResponse.class);
                } catch (IOException ioException) {
                    //ignore because this does not related with IOs
                }
                exponentialBackOff.doNotRetry();
                return moviesResponse;
            } catch (RestClientException restClientException) {
                try {
                    exponentialBackOff.errorOccuredWithException();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return null;
    }
}
