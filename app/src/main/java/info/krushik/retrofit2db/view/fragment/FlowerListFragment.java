package info.krushik.retrofit2db.view.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import info.krushik.retrofit2db.Const;
import info.krushik.retrofit2db.R;
import info.krushik.retrofit2db.Utils;
import info.krushik.retrofit2db.api.RestManager;
import info.krushik.retrofit2db.callback.FlowerFetchListener;
import info.krushik.retrofit2db.database.DatabaseHelper;
import info.krushik.retrofit2db.model.Flower;
import info.krushik.retrofit2db.view.activity.FlawerDetailActivity;
import info.krushik.retrofit2db.view.activity.FlowerListActivity;
import info.krushik.retrofit2db.view.activity.MainActivity;
import info.krushik.retrofit2db.view.adapter.FlowerAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FlowerListFragment extends Fragment
        implements FlowerAdapter.FlowerClickListener, FlowerFetchListener {

    private static final String TAG = FlowerListFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RestManager mManager;
    private FlowerAdapter mFlowerAdapter;
    private DatabaseHelper mDatabase;
    private Button mReload;
    private ProgressDialog mDialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mManager = new RestManager();
        mDatabase = new DatabaseHelper(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_flower_list, container, false);
        initViews(v);

        loadFlowerFeed();

        mReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFlowerFeed();
            }
        });

        return v;
    }

    private void initViews(View v) {
        mReload = (Button) v.findViewById(R.id.reload);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));

        mFlowerAdapter = new FlowerAdapter(this);
        mRecyclerView.setAdapter(mFlowerAdapter);
    }

    private void loadFlowerFeed() {
        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("Loading Flower Data...");
        mDialog.setCancelable(true);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setIndeterminate(true);

        mFlowerAdapter.reset();

        mDialog.show();

        if (getNetworkAvailability()) {
            getFeed();
        } else {
            getFeedFromDatabase();
        }
    }

    private void getFeedFromDatabase() {
        mDatabase.fetchFlowers(this);
    }

    @Override
    public void onClick(int position) {
        Flower selectedFlower = mFlowerAdapter.getSelectedFlower(position);
//        Intent intent = new Intent(getActivity(), FlawerDetailActivity.class);
//        intent.putExtra(Const.REFERENCE.FLOWER, selectedFlower);
//        startActivity(intent);

//        MainActivity act = (MainActivity) getActivity();
//        act.switchToFragment(new FlawerDetailtFragment());

        Fragment fragmentGet = new FlawerDetailtFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Const.REFERENCE.FLOWER, selectedFlower);

        fragmentGet.setArguments(bundle);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragmentGet);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void getFeed() {

        Call<List<Flower>> listCall = mManager.getFlowerService().getAllFlowers();
        listCall.enqueue(new Callback<List<Flower>>() {
            @Override
            public void onResponse(Call<List<Flower>> call, Response<List<Flower>> response) {

                if (response.isSuccessful()) {
                    List<Flower> flowerList = response.body();

                    for (int i = 0; i < flowerList.size(); i++) {
                        Flower flower = flowerList.get(i);

                        FlowerListFragment.SaveIntoDatabase task = new FlowerListFragment.SaveIntoDatabase();
                        task.execute(flower);

                        mFlowerAdapter.addFlower(flower);
                    }
                } else {
                    int sc = response.code();
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                    }
                }
                mDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Flower>> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean getNetworkAvailability() {
        return Utils.isNetworkAvailable(getContext());
    }

    @Override
    public void onDeliverAllFlowers(List<Flower> flowers) {

    }

    @Override
    public void onDeliverFlower(Flower flower) {
        mFlowerAdapter.addFlower(flower);
    }

    @Override
    public void onHideDialog() {
        mDialog.dismiss();
    }

    public class SaveIntoDatabase extends AsyncTask<Flower, Void, Void> {

        private final String TAG = FlowerListActivity.SaveIntoDatabase.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Flower... params) {

            Flower flower = params[0];

            try {
                InputStream stream = new URL(Const.HTTP.BASE_URL + "/photos/" + flower.getPhoto()).openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                flower.setPicture(bitmap);
                mDatabase.addFlower(flower);

            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }

            return null;
        }
    }
}