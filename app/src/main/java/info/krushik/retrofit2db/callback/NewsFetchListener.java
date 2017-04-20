package info.krushik.retrofit2db.callback;

import info.krushik.retrofit2db.model.Post;

import java.util.List;

public interface NewsFetchListener {

    void onDeliverAllPosts(List<Post> posts);

    void onDeliverPost(Post post);

    void onHideDialog();
}
