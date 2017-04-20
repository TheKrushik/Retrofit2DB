package info.krushik.retrofit2db.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import info.krushik.retrofit2db.R;
import info.krushik.retrofit2db.model.Flower;
import info.krushik.retrofit2db.Const;

public class FlawerDetailActivity extends AppCompatActivity {

    private ImageView mPhoto;
    private TextView mName, mId, mCategory, mInstruction, mPrice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        Flower flower = (Flower) intent.getSerializableExtra(Const.REFERENCE.FLOWER);

        initViews();

        mId.setText(String.format("%d", flower.getProductId()));
        mName.setText(flower.getName());
        mCategory.setText(flower.getCategory());
        mInstruction.setText(flower.getInstructions());
        mPrice.setText(String.format("$%.2f", flower.getPrice()));

        if (flower.isFromDatabase()) {
            mPhoto.setImageBitmap(flower.getPicture());
        } else {
            Picasso.with(getApplicationContext()).load(Const.HTTP.BASE_URL + "/photos/" + flower.getPhoto()).into(mPhoto);
        }
    }

    private void initViews() {
        mPhoto = (ImageView) findViewById(R.id.flowerPhoto);
        mName = (TextView) findViewById(R.id.flowerName);
        mId = (TextView) findViewById(R.id.flowerId);
        mCategory = (TextView) findViewById(R.id.flowerCategory);
        mInstruction = (TextView) findViewById(R.id.flowerInstruction);
        mPrice = (TextView) findViewById(R.id.flowerPrice);

    }
}
