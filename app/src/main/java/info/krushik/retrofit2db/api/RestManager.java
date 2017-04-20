package info.krushik.retrofit2db.api;

import info.krushik.retrofit2db.Const;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestManager {

    private static FlowerService mFlowerService;

    public FlowerService getFlowerService() {

        if (mFlowerService == null) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Const.HTTP.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            mFlowerService = retrofit.create(FlowerService.class);
        }
        return mFlowerService;
    }
}
