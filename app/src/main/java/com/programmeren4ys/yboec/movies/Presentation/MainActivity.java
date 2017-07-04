package com.programmeren4ys.yboec.movies.Presentation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.programmeren4ys.yboec.movies.Domain.Movie;
import com.programmeren4ys.yboec.movies.Domain.MovieAdapter;
import com.programmeren4ys.yboec.movies.R;
import com.programmeren4ys.yboec.movies.Service.MovieRequest;

import java.util.ArrayList;

/**
 * Created by yboec on 3-7-2017.
 */

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, MovieRequest.MovieListener {

    // Logging tag
    public final String TAG = this.getClass().getSimpleName();

    // The name for communicating Intents extras
    public final static String MOVIE_DATA = "MOVIES";

    // A request code for returning data from Intent - is supposed to be unique.
    public static final int MY_REQUEST_CODE = 1234;

    // UI Elements
    private ListView listViewMovies;
    private BaseAdapter movieAdapter;
    private ArrayList<Movie> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // We kijken hier eerst of de gebruiker nog een geldig token heeft.
        // Het token is opgeslagen in SharedPreferences.
        // Mocht er geen token zijn, of het token is expired, dan moeten we
        // eerst opnieuw inloggen.
        if(tokenAvailable()){
            setContentView(R.layout.activity_main);

            listViewMovies = (ListView) findViewById(R.id.listViewMovies);
            listViewMovies.setOnItemClickListener(this);
            movieAdapter = new MovieAdapter(this, getLayoutInflater(), movies);
            listViewMovies.setAdapter(movieAdapter);
            //
            // We hebben een token. Je zou eerst nog kunnen valideren dat het token nog
            // geldig is; dat doen we nu niet.
            // Vul de lijst met ToDos
            //
            Log.d(TAG, "Token gevonden - Movies ophalen!");
            getMovies();
        } else {
            //
            // Blijkbaar was er geen token - eerst inloggen dus
            //
            Log.d(TAG, "Geen token gevonden - inloggen dus");
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(login);
            // Sluit de huidige activity. Dat voorkomt dat de gebuiker via de
            // back-button zonder inloggen terugkeert naar het homescreen.
            finish();
        }
    }

    /**
     * Aangeroepen door terugkerende Intents. Maakt het mogelijk om data
     * terug te ontvangen van een Intent.
     *
     * @param requestCode
     * @param resultCode
     * @param pData
     */

    /**
     * Check of een token in localstorage is opgeslagen. We checken niet de geldigheid -
     * alleen of het token bestaat.
     *
     * @return
     */
    private boolean tokenAvailable() {
        boolean result = false;

        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String token = sharedPref.getString(getString(R.string.saved_token), "dummy default token");
        if (token != null && !token.equals("dummy default token")) {
            result = true;
        }
        return result;
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "Position " + position + " is geselecteerd");

        Movie movie = movies.get(position);
        Intent intent = new Intent(getApplicationContext(), MovieDetailActivity.class);
        intent.putExtra(MOVIE_DATA, movie);
        startActivity(intent);
    }

    /**
     * Callback function - handle an ArrayList of ToDos
     *
     * @param movieArrayList
     */
    @Override
    public void onMoviesAvailable(ArrayList<Movie> movieArrayList) {

        Log.i(TAG, "We hebben " + movieArrayList.size() + " films in de lijst");

        movies.clear();
        for(int i = 0; i < movieArrayList.size(); i++) {
            movies.add(movieArrayList.get(i));
        }
        movieAdapter.notifyDataSetChanged();
    }

    /**
     * Callback function - handle a single ToDo
     *
     * @param movie
     */
    @Override
    public void onMovieAvailable(Movie movie) {
        movies.add(movie);
        movieAdapter.notifyDataSetChanged();
    }

    /**
     * Callback function
     *
     * @param message
     */
    @Override
    public void onMoviesError(String message) {
        Log.e(TAG, message);
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    /**
     * Start the activity to GET all ToDos from the server.
     */
    private void getMovies(){
        MovieRequest request = new MovieRequest(getApplicationContext(), this);
        request.handleGetAllMovies();
    }

    public void onRental(String message){

    }

    /**
     * Start the activity to POST a new ToDo to the server.
     */
//    private void postTodo(Movie movie){
//        ToDoRequest request = new ToDoRequest(getApplicationContext(), this);
//        request.handlePostToDo(todo);
//    }

}