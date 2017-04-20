package info.krushik.retrofit2db.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import info.krushik.retrofit2db.callback.FlowerFetchListener;
import info.krushik.retrofit2db.Const;
import info.krushik.retrofit2db.Utils;
import info.krushik.retrofit2db.model.Flower;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    public DatabaseHelper(Context context) {
        super(context, Const.DATABASE.DB_NAME, null, Const.DATABASE.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(Const.DATABASE.CREATE_TABLE_QUERY);
        } catch (SQLException ex) {
            Log.d(TAG, ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Const.DATABASE.DROP_QUERY);
        this.onCreate(db);
    }

    public void addFlower(Flower flower) {

        Log.d(TAG, "Values Got " + flower.getName());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Const.DATABASE.PRODUCT_ID, flower.getProductId());
        values.put(Const.DATABASE.CATEGORY, flower.getCategory());
        values.put(Const.DATABASE.PRICE, Double.toString(flower.getPrice()));
        values.put(Const.DATABASE.INSTRUCTIONS, flower.getInstructions());
        values.put(Const.DATABASE.NAME, flower.getName());
        values.put(Const.DATABASE.PHOTO_URL, flower.getPhoto());
        values.put(Const.DATABASE.PHOTO, Utils.getPictureByteOfArray(flower.getPicture()));

        try {
            db.insert(Const.DATABASE.TABLE_NAME, null, values);
        } catch (Exception e) {

        }
        db.close();
    }

    public void fetchFlowers(FlowerFetchListener listener) {
        FlowerFetcher fetcher = new FlowerFetcher(listener, this.getWritableDatabase());
        fetcher.start();
    }

    public class FlowerFetcher extends Thread {

        private final FlowerFetchListener mListener;
        private final SQLiteDatabase mDb;

        public FlowerFetcher(FlowerFetchListener listener, SQLiteDatabase db) {
            mListener = listener;
            mDb = db;
        }

        @Override
        public void run() {
            Cursor cursor = mDb.rawQuery(Const.DATABASE.GET_FLOWERS_QUERY, null);

            final List<Flower> flowerList = new ArrayList<>();

            if (cursor.getCount() > 0) {

                if (cursor.moveToFirst()) {
                    do {
                        Flower flower = new Flower();
                        flower.setFromDatabase(true);
                        flower.setName(cursor.getString(cursor.getColumnIndex(Const.DATABASE.NAME)));
                        flower.setPrice(Double.parseDouble(cursor.getString(cursor.getColumnIndex(Const.DATABASE.PRICE))));
                        flower.setInstructions(cursor.getString(cursor.getColumnIndex(Const.DATABASE.INSTRUCTIONS)));
                        flower.setCategory(cursor.getString(cursor.getColumnIndex(Const.DATABASE.CATEGORY)));
                        flower.setPicture(Utils.getBitmapFromByte(cursor.getBlob(cursor.getColumnIndex(Const.DATABASE.PHOTO))));
                        flower.setProductId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Const.DATABASE.PRODUCT_ID))));
                        flower.setPhoto(cursor.getString(cursor.getColumnIndex(Const.DATABASE.PHOTO_URL)));

                        flowerList.add(flower);
                        publishFlower(flower);

                    } while (cursor.moveToNext());
                }
            }
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onDeliverAllFlowers(flowerList);
                    mListener.onHideDialog();
                }
            });
        }

        public void publishFlower(final Flower flower) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onDeliverFlower(flower);
                }
            });
        }
    }
}
