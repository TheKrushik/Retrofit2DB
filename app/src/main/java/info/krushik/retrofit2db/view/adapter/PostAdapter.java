package info.krushik.retrofit2db.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import info.krushik.retrofit2db.R;
import info.krushik.retrofit2db.Const;
import info.krushik.retrofit2db.model.Post;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.Holder> {

    private static final String TAG = PostAdapter.class.getSimpleName();
    private final PostClickListener mListener;
    private List<Post> mPosts;

    public PostAdapter(PostClickListener listener) {
        mPosts = new ArrayList<>();
        mListener = listener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, null, false);
        return new Holder(row);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        Post currentPost = mPosts.get(position);

        holder.mName.setText(currentPost.getName());
        holder.mPrice.setText(String.format("$%.2f", currentPost.getPrice()));

        if (currentPost.isFromDatabase()) {
            holder.mPhoto.setImageBitmap(currentPost.getPicture());
        } else {
            Picasso.with(holder.itemView.getContext()).load(Const.HTTP.BASE_URL + "/photos/"
                    + currentPost.getPhoto()).into(holder.mPhoto);
        }
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public void addPost(Post post) {
        mPosts.add(post);
        notifyDataSetChanged();
    }

    public Post getSelectedPost(int position) {
        return mPosts.get(position);
    }

    public void reset() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mPhoto;
        private TextView mName, mPrice;

        public Holder(View itemView) {
            super(itemView);
            mPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
            mName = (TextView) itemView.findViewById(R.id.tvName);
            mPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(getLayoutPosition());
        }
    }

    public interface PostClickListener {

        void onClick(int position);
    }
}
