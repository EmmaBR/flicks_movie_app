package me.emmabr.flicks;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import me.emmabr.flicks.models.Config;
import me.emmabr.flicks.models.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    //list of movies
    ArrayList<Movie> movies;
    //config
    Config config;
    //context
    Context context;

    //initialize list
    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    // creates and inflates a new view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //get context and create inflater
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //create view using item movie layout
        View viewMovie = inflater.inflate(R.layout.item_movie, parent, false);
        // return a new viewholder
        return new ViewHolder(viewMovie);
    }
    // associates inflated view to a new item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get the movie at the specified position
        Movie movie = movies.get(position);
        //populate the view with movie data
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());

        //determine orientation
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;


        // build url for image
        String imageUrl = null;

        // change url depending on orientation
        if(isPortrait) {
            imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
        } else {
            //load backdrop image
            imageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());
        }

        // get correct placeholder and imageView for current orientation
        int placeholderId = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView = isPortrait ? holder.ivPosterImage : holder.ivBackdropImage;



        //load image using glide
        Glide.with(context)
                .load(imageUrl)
                .apply(new RequestOptions()
                .placeholder(R.drawable.flicks_movie_placeholder)
                .error(placeholderId)
                .transforms(new RoundedCorners(25)))
                //(RequestOptions.placeholderOf(R.drawable.flicks_movie_placeholder).error(R.drawable.flicks_movie_placeholder).fitCenter())
                .into(imageView);
    }
    // returns size of list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    //create viewholder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //track view objects
        ImageView ivPosterImage;
        ImageView ivBackdropImage;
        TextView tvTitle;
        TextView tvOverview;

        public ViewHolder(View itemView) {
            super(itemView);
            //lookup view objects by id
            ivPosterImage = itemView.findViewById(R.id.ivPosterImage);
            ivBackdropImage = (ImageView) itemView.findViewById(R.id.ivBackdropImage);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }
    }
}
