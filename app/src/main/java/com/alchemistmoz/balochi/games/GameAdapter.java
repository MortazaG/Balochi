package com.alchemistmoz.balochi.games;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Extension of the RecyclerView.Adapter class in order to feed data to the list.
 * The adapter object creates views for items, that is the data that will be used as
 * content for the views. It also replaces the content of the views with new data items
 * when the original item is no longer available.
 *
 * This game adapter is also customized to setVisibility of views to INVISIBLE if that
 * specific view item has been selected by the user during the game.
 */
public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {

    private ArrayList<GameItem> mData;
    private LayoutInflater mInflater;
    private Context context;
    private int itemLayout;

    /**
     * Stores and recycles views as they are scrolled off screen.
     * Provide a reference to the views for each data item.
     * Complex data items may need more than one view per item, and
     * you provide access to all the views for a data item in a view holder
     *
     * This ViewHolder implements the OnClickListener, so that we can handle click events
     * for each view.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView countItem;

        /**
         * Constructor to store inflated itemView, which can be used directly or
         * through its children as in our case.
         *
         * @param itemView - The inflated item image view
         */
        ViewHolder(ImageView itemView) {
            super(itemView);
            countItem = itemView;

        }
    }

    /**
     * Data and context is passed to the constructor, which also
     * initiates LayoutInflater from context.
     *
     * @param context - Activity context.
     * @param data - Data list to be used for the recyclerView.
     */
    public GameAdapter(Context context, ArrayList<GameItem> data, int itemLayout) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.itemLayout = itemLayout;

    }

    /**
     * Create new views (invoked by the layout manager).
     *
     * @param parent - The ViewGroup into which the new View will be added
     *               after it is bound to an adapter position.
     * @param viewType - The view type of the new View.
     * @return ViewHolder object with the inflated itemView.
     */
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // LayoutInflater instantiates a layout XML file into its corresponding View objects.
        ImageView itemView = (ImageView) mInflater.inflate(itemLayout, parent, false);

        return new ViewHolder(itemView);
    }

    /**
     * Replace the contents of a view (invoked by the layout manager).
     * Binds the data to the ImageView in each cell.
     *
     * @param holder - ViewHolder object to allow access to itemView.
     * @param position - Current adapter position.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Check if item is selected and set visibility accordingly
        if (getItem(position).isSelected()) {
            holder.countItem.setVisibility(View.INVISIBLE);
        } else {
            holder.countItem.setVisibility(View.VISIBLE);
        }

        // Load current image to countItem using Glide library
        Glide.with(context)
                .load(getItem(position).getImageResourceID())
                .into(holder.countItem);
    }

    /**
     * @return the size of your dataset (invoked by the layout manager)
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     *  Convenient method to get data at the current position.
     *
     * @param position - Current/selected position
     * @return data object
     */
    private GameItem getItem(int position) {
        return mData.get(position);
    }

}
