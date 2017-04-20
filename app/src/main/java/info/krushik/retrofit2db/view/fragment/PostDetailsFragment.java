package info.krushik.retrofit2db.view.fragment;


import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import info.krushik.retrofit2db.Const;
import info.krushik.retrofit2db.R;
import info.krushik.retrofit2db.model.Post;

public class PostDetailsFragment extends Fragment {

    private ImageView mPhoto;
    private TextView mName, mId, mCategory, mInstruction, mPrice;


    public PostDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);

        Post post = new Post(Parcel.obtain());
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            post = bundle.getParcelable(Const.REFERENCE.NEWS);
        }

        initViews(v);

        mId.setText(String.format("%d", post.getProductId()));
        mName.setText(post.getName());
        mCategory.setText(post.getCategory());
        mInstruction.setText(post.getInstructions());
        mPrice.setText(String.format("$%.2f", post.getPrice()));

        if (post.isFromDatabase()) {
            mPhoto.setImageBitmap(post.getPicture());
        } else {
            Picasso.with(getContext()).load(Const.HTTP.BASE_URL + "/photos/" + post.getPhoto()).into(mPhoto);
        }

        return v;
    }

    private void initViews(View v) {
        mPhoto = (ImageView) v.findViewById(R.id.ivPhoto);
        mName = (TextView) v.findViewById(R.id.tvName);
        mId = (TextView) v.findViewById(R.id.tvId);
        mCategory = (TextView) v.findViewById(R.id.tvCategory);
        mInstruction = (TextView) v.findViewById(R.id.tvInstruction);
        mPrice = (TextView) v.findViewById(R.id.tvPrice);
    }

}
