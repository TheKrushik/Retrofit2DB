package info.krushik.retrofit2db.view.fragment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import info.krushik.retrofit2db.callback.NewsFetchListener;
import info.krushik.retrofit2db.database.DatabaseHelper;
import info.krushik.retrofit2db.model.Post;
import info.krushik.retrofit2db.view.activity.MainActivity;
import info.krushik.retrofit2db.view.adapter.PostAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostListFragment extends Fragment
        implements PostAdapter.PostClickListener, NewsFetchListener {

    private static final String TAG = PostListFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RestManager mManager;
    private PostAdapter mPostAdapter;
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
        View v = inflater.inflate(R.layout.fragment_post_list, container, false);
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
        mReload = (Button) v.findViewById(R.id.btnReload);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.rvPost);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));

        mPostAdapter = new PostAdapter(this);
        mRecyclerView.setAdapter(mPostAdapter);
    }

    private void loadFlowerFeed() {
        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("Loading Post Data...");
        mDialog.setCancelable(true);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setIndeterminate(true);

        mPostAdapter.reset();

        mDialog.show();

        if (getNetworkAvailability()) {
            getFeed();
        } else {
            getFeedFromDatabase();
        }
    }

    private void getFeedFromDatabase() {
        mDatabase.fetchPosts(this);
    }

    @Override
    public void onClick(int position) {

        Post selectedPost = mPostAdapter.getSelectedPost(position);

        Fragment fragmentDetail = new PostDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Const.REFERENCE.POST, selectedPost);
        fragmentDetail.setArguments(bundle);

        MainActivity act = (MainActivity) getActivity();
        act.switchToFragment(fragmentDetail);
    }

    public void getFeed() {

        Call<List<Post>> listCall = mManager.getNewsService().getAllNews();
        listCall.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if (response.isSuccessful()) {
                    List<Post> postList = response.body();

                    for (int i = 0; i < postList.size(); i++) {
                        Post post = postList.get(i);

                        PostListFragment.SaveIntoDatabase task = new PostListFragment.SaveIntoDatabase();
                        task.execute(post);

                        mPostAdapter.addPost(post);
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
            public void onFailure(Call<List<Post>> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean getNetworkAvailability() {
        return Utils.isNetworkAvailable(getContext());
    }

    @Override
    public void onDeliverAllPosts(List<Post> posts) {

    }

    @Override
    public void onDeliverPost(Post post) {
        mPostAdapter.addPost(post);
    }

    @Override
    public void onHideDialog() {
        mDialog.dismiss();
    }

    public class SaveIntoDatabase extends AsyncTask<Post, Void, Void> {

        private final String TAG = PostListFragment.SaveIntoDatabase.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Post... params) {

            Post post = params[0];

            try {
                InputStream stream = new URL(Const.HTTP.BASE_URL + "/photos/" + post.getPhoto()).openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                post.setPicture(bitmap);
                mDatabase.addFlower(post);

            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }

            return null;
        }
    }
}