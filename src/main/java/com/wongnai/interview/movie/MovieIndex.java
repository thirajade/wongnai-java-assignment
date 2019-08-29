package com.wongnai.interview.movie;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component("movieIndex")
public class MovieIndex {
    private Map<String, Set<Long>> index;

    protected MovieIndex() {
        this.index = new HashMap<>();
    }

    public Set<Long> get(String word) {
        return this.index.get(word.toLowerCase());
    }

    public Set<Long> put(String word, Set<Long> index) {
        return this.index.put(word.toLowerCase(), index);
    }

    public boolean isEmpty() {
        return this.index.isEmpty();
    }
}
