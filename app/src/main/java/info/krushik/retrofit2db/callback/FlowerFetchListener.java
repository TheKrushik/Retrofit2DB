package info.krushik.retrofit2db.callback;

import info.krushik.retrofit2db.model.Flower;

import java.util.List;

public interface FlowerFetchListener {

    void onDeliverAllFlowers(List<Flower> flowers);

    void onDeliverFlower(Flower flower);

    void onHideDialog();
}
