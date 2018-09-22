package nweave.com.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import nweave.com.base.BaseFragment;
import nweave.com.myapplication.retrofit.GetDataService;
import nweave.com.myapplication.retrofit.RetroPhoto;
import nweave.com.myapplication.retrofit.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FeedFragment extends BaseFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private ProgressDialog progressDialog;
    private FeedRecyclerViewAdapter feedRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private List<RetroPhoto> mCachedItems;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FeedFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FeedFragment newInstance(int columnCount) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
        }


        if(mCachedItems == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading....");
            progressDialog.show();

            /*Create handle for the RetrofitInstance interface*/
            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<List<RetroPhoto>> call = service.getAllPhotos();
            call.enqueue(new Callback<List<RetroPhoto>>() {
                @Override
                public void onResponse(Call<List<RetroPhoto>> call, Response<List<RetroPhoto>> response) {
                    progressDialog.dismiss();
                    mCachedItems = response.body();
                    generateDataList(mCachedItems);
                }

                @Override
                public void onFailure(Call<List<RetroPhoto>> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            generateDataList(mCachedItems);
        }

        return view;
    }

    private void generateDataList(List<RetroPhoto> items) {
        feedRecyclerViewAdapter = new FeedRecyclerViewAdapter(getContext(), items, new OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(FeedRecyclerViewAdapter.FeedViewHolder holder, RetroPhoto item) {
                DetailsFragment kittenDetails = DetailsFragment.newInstance(item);

                // Note that we need the API version check here because the actual transition classes (e.g. Fade)
                // are not in the support library and are only available in API 21+. The methods we are calling on the Fragment
                // ARE available in the support library (though they don't do anything on API < 21)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    kittenDetails.setSharedElementEnterTransition(new DetailsTransition());
                    kittenDetails.setEnterTransition(new Fade());
                    setExitTransition(new Fade());
                    kittenDetails.setSharedElementReturnTransition(new DetailsTransition());
                }

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addSharedElement(holder.mImageProfile, "kittenImage")
                        .replace(R.id.container_layout, kittenDetails)
                        .addToBackStack(null)
                        .commit();


            }
        });
        recyclerView.setAdapter(feedRecyclerViewAdapter);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(FeedRecyclerViewAdapter.FeedViewHolder holder, RetroPhoto item);
    }
}
