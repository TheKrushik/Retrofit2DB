package info.krushik.retrofit2db.api;

import info.krushik.retrofit2db.Const;
import info.krushik.retrofit2db.model.Flower;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FlowerService {

    @GET(Const.HTTP.FLOWERS_URL)
    Call<List<Flower>> getAllFlowers();
}
