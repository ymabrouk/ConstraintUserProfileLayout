package nweave.com.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import nweave.com.myapplication.FeedFragment.OnListFragmentInteractionListener;
import nweave.com.myapplication.retrofit.RetroPhoto;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class FeedRecyclerViewAdapter extends RecyclerView.Adapter<FeedRecyclerViewAdapter.FeedViewHolder> {

    private final List<RetroPhoto> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final Context mContext;

    public FeedRecyclerViewAdapter(Context context, List<RetroPhoto> items, OnListFragmentInteractionListener listener) {
        this.mContext = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_feed, parent, false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FeedViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitle.setText(mValues.get(position).getTitle());


        Glide.with(mContext)
                .load(mValues.get(position).getThumbnailUrl())
                .into(holder.mImageProfile);


        Glide.with(mContext)
                .load(mValues.get(position).getUrl())
                .into(holder.mImageCover);


//        Picasso.Builder builder = new Picasso.Builder(mContext);
//        builder.downloader(new OkHttp3Downloader(mContext));
//        builder.build().load(mValues.get(position).getThumbnailUrl())
//                .placeholder((R.drawable.ic_launcher_background))
//                .error(R.drawable.ic_launcher_background)
//                .into(holder.mImageCover);

//        builder.build().load(mValues.get(position).getThumbnailUrl())
//                .placeholder((R.drawable.ic_launcher_background))
//                .error(R.drawable.ic_launcher_background)
//                .into(holder.mImageProfile);



        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder, holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageCover;
        public final ImageView mImageProfile;
        public final TextView mTitle;
        public RetroPhoto mItem;

        public FeedViewHolder(View view) {
            super(view);
            mView = view;
            mImageCover =  view.findViewById(R.id.image_cover);
            mImageProfile = view.findViewById(R.id.image_profile);
            mTitle = view.findViewById(R.id.title_tv);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItem.getTitle() + "'";
        }
    }
}
