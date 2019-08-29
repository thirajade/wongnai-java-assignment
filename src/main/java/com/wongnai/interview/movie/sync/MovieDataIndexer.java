package com.wongnai.interview.movie.sync;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.MovieIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component("movieDataIndexer")
public class MovieDataIndexer {

    @Autowired
    private MovieIndex movieIndex;

    public void index(Movie movie) {
        String title = movie.getName();
        for (String wordInTitle : title.split(" ")) {
            String word = wordInTitle.toLowerCase();
            Set<Long> idx = movieIndex.get(word);
            if (idx == null) {
                idx = new HashSet<>();
                movieIndex.put(word, idx);
            }
            idx.add(movie.getId());
        }
    }
}
