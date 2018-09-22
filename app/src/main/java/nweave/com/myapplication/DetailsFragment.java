package nweave.com.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import nweave.com.base.BaseFragment;
import nweave.com.myapplication.retrofit.RetroPhoto;


/**
 * to handle interaction events.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String RETRO_PHOTO_KEY = "RetroPhotoKey";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RetroPhoto mRetroPhoto;


    @BindView(R.id.title_profile_tv)
    TextView mTitle;


    @BindView(R.id.image_cover_profile)
    ImageView mProfile;

    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance(RetroPhoto item) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(RETRO_PHOTO_KEY, item);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRetroPhoto = getArguments().getParcelable(RETRO_PHOTO_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife .bind(this, view);
        mTitle.setText(String.valueOf(mRetroPhoto.getTitle()));
        Glide.with(getContext())
                .load(mRetroPhoto.getUrl())
                .into(mProfile);

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
