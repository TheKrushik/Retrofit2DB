package info.krushik.retrofit2db.api;

import info.krushik.retrofit2db.Const;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestManager {

    private static ApiService mApiService;

    public ApiService getNewsService() {

        if (mApiService == null) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Const.HTTP.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            mApiService = retrofit.create(ApiService.class);
        }
        return mApiService;
    }
}
