package com.wongnai.interview.movie.search;

import com.google.common.collect.Sets;
import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.MovieIndex;
import com.wongnai.interview.movie.MovieRepository;
import com.wongnai.interview.movie.MovieSearchService;
import com.wongnai.interview.movie.sync.MovieDataSynchronizer;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component("invertedIndexMovieSearchService")
@DependsOn("movieDatabaseInitializer")
public class InvertedIndexMovieSearchService implements MovieSearchService {
	@Autowired
	private MovieRepository movieRepository;

    @Autowired
    private MovieDataSynchronizer movieDataSynchronizer;

    @Autowired
    private MovieIndex movieIndex;


	@Override
	public List<Movie> search(String queryText) {
		//TODO: Step 4 => Please implement in-memory inverted index to search movie by keyword.
		// You must find a way to build inverted index before you do an actual search.
		// Inverted index would looks like this:
		// -------------------------------
		// |  Term      | Movie Ids      |
		// -------------------------------
		// |  Star      |  5, 8, 1       |
		// |  War       |  5, 2          |
		// |  Trek      |  1, 8          |
		// -------------------------------
		// When you search with keyword "Star", you will know immediately, by looking at Term column, and see that
		// there are 3 movie ids contains this word -- 1,5,8. Then, you can use these ids to find full movie object from repository.
		// Another case is when you find with keyword "Star War", there are 2 terms, Star and War, then you lookup
		// from inverted index for Star and for War so that you get movie ids 1,5,8 for Star and 2,5 for War. The result that
		// you have to return can be union or intersection of those 2 sets of ids.
		// By the way, in this assignment, you must use intersection so that it left for just movie id 5.

        // never have index before (an error might occurred when forceSync() at the beginning of the application)
        if (movieIndex.isEmpty()) {
            //sync again
            movieDataSynchronizer.forceSync();
        }

        Set<Long> movieIdSet = null;
        for (String wordInQueryText : queryText.split(" ")) {
            Set<Long> idList = movieIndex.get(wordInQueryText);
            if (movieIdSet == null) {
                //initial movieIdSet
                movieIdSet = idList;
            } else {
                //find intersection
                movieIdSet = Sets.intersection(movieIdSet, idList);
            }
        }

        if (movieIdSet == null) {
            return new ArrayList<>();
        }

		return movieRepository.findAllById(Lists.newArrayList(movieIdSet));
	}
}
