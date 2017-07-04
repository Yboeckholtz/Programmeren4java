package com.programmeren4ys.yboec.movies.Service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.programmeren4ys.yboec.movies.Domain.Movie;
import com.programmeren4ys.yboec.movies.Domain.MovieMapper;
import com.programmeren4ys.yboec.movies.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by yboec on 3-7-2017.
 */

public class MovieRequest {
    private Context context;
    public final String TAG = this.getClass().getSimpleName();

    // De aanroepende class implementeert deze interface.
    private MovieRequest.MovieListener listener;

    /**
     * Constructor
     *
     * @param context
     * @param listener
     */
    public MovieRequest(Context context, MovieRequest.MovieListener listener) {
        this.context = context;
        this.listener = listener;
    }

    /**
     * Verstuur een GET request om alle ToDo's op te halen.
     */
    public void handleGetAllMovies() {

        Log.i(TAG, "handleGetAllMovies");

        // Haal het token uit de prefs
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final String token = sharedPref.getString(context.getString(R.string.saved_token), "dummy default token");
//        if(token != null && !token.equals("dummy default token")) {

            Log.i(TAG, "Token gevonden, we gaan het request uitvoeren");
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, Config.URL_FILMS, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            // Succesvol response
                            Log.i(TAG, response.toString());
                            ArrayList<Movie> result = MovieMapper.mapMovieList(response);
                            listener.onMoviesAvailable(result);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // handleErrorResponse(error);
                            Log.e(TAG, error.toString());
                        }
                    }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };

            // Access the RequestQueue through your singleton class.
            VolleyRequestQueue.getInstance(context).addToRequestQueue(jsObjRequest);

    }

    /**
     * Verstuur een POST met nieuwe ToDo.
     */
    public void handleRentMovie(final Movie newRental) {

        Log.i(TAG, "handleRentMovie");

        // Haal het token uit de prefs
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final String token = sharedPref.getString(context.getString(R.string.saved_token), "dummy default token");
        final String userId = sharedPref.getString(context.getString(R.string.user_id), "customer id");
        if(token != null && !token.equals("dummy default token")) {

            //
            // Maak een JSON object met username en password. Dit object sturen we mee
            // als request body (zoals je ook met Postman hebt gedaan)
            //

            Log.i("movie", "ewo");
            //String body = "";

            //JSONObject jsonBody = new JSONObject(body);
            //Log.i(TAG, "handlePostToDo - body = " + jsonBody);
            StringRequest postRequest = new StringRequest(Request.Method.POST, Config.URL_RENTALS + "/" + userId + "/" + newRental.getMovieId(),
                    new Response.Listener<String>(){
//                        JsonObjectRequest jsObjRequest = new JsonObjectRequest
//                        (Request.Method.POST, Config.URL_RENTALS + "/" + userId + "/" + newRental.getMovieId(), new Response.Listener<JSONObject>() {

                        //@Override
                        public void onResponse(String response) {
                            Log.i(TAG, response);
                            // Het toevoegen is gelukt
                            // Hier kun je kiezen: of een refresh van de hele lijst ophalen
                            // en de ListView bijwerken ... Of alleen de ene update toevoegen
                            // aan de ArrayList. Wij doen dat laatste.
                            listener.onMovieAvailable(newRental);
                            listener.onRental(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Error - send back to caller
                            listener.onMoviesError(error.toString());
                        }
                    }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };

            // Access the RequestQueue through your singleton class.
            VolleyRequestQueue.getInstance(context).addToRequestQueue(postRequest);
        }
    }

    //
    // Callback interface - implemented by the calling class (MainActivity in our case).
    //
    public interface MovieListener {
        // Callback function to return a fresh list of ToDos
        void onMoviesAvailable(ArrayList<Movie> movies);

        // Callback function to handle a single added ToDo.
        void onMovieAvailable(Movie movie);

        // Callback to handle serverside API errors
        void onMoviesError(String message);

        void onRental(String message);

    }
}
