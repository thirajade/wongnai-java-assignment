package com.wongnai.interview.movie.sync;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.MovieRepository;
import com.wongnai.interview.movie.external.MovieData;
import com.wongnai.interview.movie.external.MovieDataService;
import com.wongnai.interview.movie.external.MoviesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class MovieDataSynchronizer {
	@Autowired
	private MovieDataService movieDataService;

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
    private MovieDataIndexer movieDataIndexer;

	@Transactional
	public void forceSync() {
		//TODO: implement this to sync movie into repository
		MoviesResponse moviesResponse = movieDataService.fetchAll();

		//if data is null, it will be force sync again when search
        if (moviesResponse != null) {
            for (MovieData movieData : moviesResponse) {
                Movie movie = new Movie(movieData.getTitle());
                movie.getActors().addAll(movieData.getCast());
                movieRepository.save(movie);
                movieDataIndexer.index(movie);
            }
        }
	}
}
