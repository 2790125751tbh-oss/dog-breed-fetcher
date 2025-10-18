package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "https://dog.ceo/api/breed/";
    private static final String STATUS_CODE = "status";
    private static final String SUCCESS_CODE = "success";

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     *
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) {
        // TODO Task 1: Complete this method based on its provided documentation
        //      and the documentation for the dog.ceo API. You may find it helpful
        //      to refer to the examples of using OkHttpClient from the last lab,
        //      as well as the code for parsing JSON responses.
        // return statement included so that the starter code can compile and run.
        final OkHttpClient client = new OkHttpClient().newBuilder().build();
        String url = BASE_URL + breed.trim().toLowerCase() + "/list";
        final Request request = new Request.Builder().url(url).build();
        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());
            if (Objects.equals(responseBody.getString(STATUS_CODE), SUCCESS_CODE)) {
                JSONArray breeds = responseBody.getJSONArray("message");
                List<String> subBreeds = new ArrayList<>();
                for (int i = 0; i < breeds.length(); i++) {
                    final String subBreed = breeds.getString(i);
                    subBreeds.add(subBreed);
                }
                return subBreeds;
            } else {
                throw new BreedNotFoundException(breed);
            }
        } catch (IOException event) {
            throw new RuntimeException(event);
        }
    }
}