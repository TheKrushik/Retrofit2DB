package info.krushik.retrofit2db.api;

import info.krushik.retrofit2db.Const;
import info.krushik.retrofit2db.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET(Const.HTTP.NEWS_URL)
    Call<List<Post>> getAllNews();
}
