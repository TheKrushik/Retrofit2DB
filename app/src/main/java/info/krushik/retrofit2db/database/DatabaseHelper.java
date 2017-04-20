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

import info.krushik.retrofit2db.callback.NewsFetchListener;
import info.krushik.retrofit2db.Const;
import info.krushik.retrofit2db.Utils;
import info.krushik.retrofit2db.model.Post;

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

    public void addFlower(Post post) {

        Log.d(TAG, "Values Got " + post.getName());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Const.DATABASE.PRODUCT_ID, post.getProductId());
        values.put(Const.DATABASE.CATEGORY, post.getCategory());
        values.put(Const.DATABASE.PRICE, Double.toString(post.getPrice()));
        values.put(Const.DATABASE.INSTRUCTIONS, post.getInstructions());
        values.put(Const.DATABASE.NAME, post.getName());
        values.put(Const.DATABASE.PHOTO_URL, post.getPhoto());
        values.put(Const.DATABASE.PHOTO, Utils.getPictureByteOfArray(post.getPicture()));

        try {
            db.insert(Const.DATABASE.TABLE_NAME, null, values);
        } catch (Exception e) {

        }
        db.close();
    }

    public void fetchPosts(NewsFetchListener listener) {
        PostFetcher fetcher = new PostFetcher(listener, this.getWritableDatabase());
        fetcher.start();
    }

    public class PostFetcher extends Thread {

        private final NewsFetchListener mListener;
        private final SQLiteDatabase mDb;

        public PostFetcher(NewsFetchListener listener, SQLiteDatabase db) {
            mListener = listener;
            mDb = db;
        }

        @Override
        public void run() {
            Cursor cursor = mDb.rawQuery(Const.DATABASE.GET_NEWS_QUERY, null);

            final List<Post> postList = new ArrayList<>();

            if (cursor.getCount() > 0) {

                if (cursor.moveToFirst()) {
                    do {
                        Post post = new Post();
                        post.setFromDatabase(true);
                        post.setName(cursor.getString(cursor.getColumnIndex(Const.DATABASE.NAME)));
                        post.setPrice(Double.parseDouble(cursor.getString(cursor.getColumnIndex(Const.DATABASE.PRICE))));
                        post.setInstructions(cursor.getString(cursor.getColumnIndex(Const.DATABASE.INSTRUCTIONS)));
                        post.setCategory(cursor.getString(cursor.getColumnIndex(Const.DATABASE.CATEGORY)));
                        post.setPicture(Utils.getBitmapFromByte(cursor.getBlob(cursor.getColumnIndex(Const.DATABASE.PHOTO))));
                        post.setProductId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Const.DATABASE.PRODUCT_ID))));
                        post.setPhoto(cursor.getString(cursor.getColumnIndex(Const.DATABASE.PHOTO_URL)));

                        postList.add(post);
                        publishPost(post);

                    } while (cursor.moveToNext());
                }
            }
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onDeliverAllPosts(postList);
                    mListener.onHideDialog();
                }
            });
        }

        public void publishPost(final Post post) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onDeliverPost(post);
                }
            });
        }
    }
}
