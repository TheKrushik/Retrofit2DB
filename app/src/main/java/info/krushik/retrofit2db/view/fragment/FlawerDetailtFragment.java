package info.krushik.retrofit2db.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import info.krushik.retrofit2db.Const;
import info.krushik.retrofit2db.R;
import info.krushik.retrofit2db.model.Flower;

public class FlawerDetailtFragment extends Fragment {

    private ImageView mPhoto;
    private TextView mName, mId, mCategory, mInstruction, mPrice;


    public FlawerDetailtFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        Bundle bundle = new Bundle();
        Flower flower = (Flower) bundle.getParcelableExtra(Const.REFERENCE.FLOWER);

        initViews(v);

        mId.setText(String.format("%d", flower.getProductId()));
        mName.setText(flower.getName());
        mCategory.setText(flower.getCategory());
        mInstruction.setText(flower.getInstructions());
        mPrice.setText(String.format("$%.2f", flower.getPrice()));

        if (flower.isFromDatabase()) {
            mPhoto.setImageBitmap(flower.getPicture());
        } else {
            Picasso.with(getContext()).load(Const.HTTP.BASE_URL + "/photos/" + flower.getPhoto()).into(mPhoto);
        }


        return v;
    }

    private void initViews(View v) {
        mPhoto = (ImageView) v.findViewById(R.id.flowerPhoto);
        mName = (TextView) v.findViewById(R.id.flowerName);
        mId = (TextView) v.findViewById(R.id.flowerId);
        mCategory = (TextView) v.findViewById(R.id.flowerCategory);
        mInstruction = (TextView) v.findViewById(R.id.flowerInstruction);
        mPrice = (TextView) v.findViewById(R.id.flowerPrice);
    }

}
