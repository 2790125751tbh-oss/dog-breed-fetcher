package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    // TODO Task 2: Complete this class
    private int callsMade = 0;
    private final Map<String, List<String>> cache;
    private final BreedFetcher underlyingFetcher;
    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.cache = new HashMap<>();
        this.underlyingFetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        // return statement included so that the starter code can compile and run.
        if (cache.containsKey(breed)) {
            return new ArrayList<>(cache.get(breed));
        }
        try{
            List<String> subBreeds = underlyingFetcher.getSubBreeds(breed);
            cache.put(breed, new ArrayList<>(subBreeds));
            callsMade++;
            return new ArrayList<>(subBreeds);
        }
        catch (BreedNotFoundException e) {
            callsMade++;
            throw new BreedNotFoundException(breed);}
    }

    public int getCallsMade() {
        return callsMade;
    }
}