package info.krushik.retrofit2db.view.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import info.krushik.retrofit2db.R;
import info.krushik.retrofit2db.view.fragment.PostListFragment;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        setSupportActionBar(toolbar);

        switchToFragment(new PostListFragment());
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    //переключение фрагментов
    public void switchToFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)// добавляем фрагменты в BackStack
                .commit();
    }
}
