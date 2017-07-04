package com.programmeren4ys.yboec.movies.Domain;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yboec on 3-7-2017.
 */

public class MovieMapper {
    //public static final String MOVIE_RESULT = "result";
    public static final String MOVIE_ID = "film_id";
    public static final String MOVIE_TITLE = "title";
    public static final String MOVIE_DESCRIPTION = "description";
    public static final String MOVIE_RELEASE_YEAR = "release_year";
//    public static final String MOVIE_STATUS = "Status";

    /**
     * Map het JSON response op een arraylist en retourneer deze.
     */
    public static ArrayList<Movie> mapMovieList(JSONObject response){

        ArrayList<Movie> result = new ArrayList<>();

        try{
            JSONArray jsonArray = response.getJSONArray("result");

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonProduct = jsonArray.getJSONObject(i);

                // Convert stringdate to Date
                //String timestamp = jsonProduct.getString(MOVIE_CREATED_AT);
                //DateTime todoDateTime = ISODateTimeFormat.dateTimeParser().parseDateTime(timestamp);

//                Movie movie = new Movie(
//                        jsonProduct.getString(MOVIE_TITLE),
//                        jsonProduct.getString(MOVIE_DESCRIPTION)
//                        jsonProduct.getString(MOVIE_RESULT)
//                        //todoDateTime
//                );

                Movie movie = new Movie();

                movie.setMovieId(jsonProduct.getString(MOVIE_ID));
                movie.setDescription(jsonProduct.getString(MOVIE_DESCRIPTION));
                movie.setMovietitle(jsonProduct.getString(MOVIE_TITLE));
                movie.setReleaseYear(jsonProduct.getString(MOVIE_RELEASE_YEAR));
                Log.i("MovieMapper", "Movie: " + movie.getMovietitle());

                result.add(movie);
            }
        } catch( JSONException ex) {
            Log.e("MovieMapper", "onPostExecute JSONException " + ex.getLocalizedMessage());
        }
        return result;
    }
}
