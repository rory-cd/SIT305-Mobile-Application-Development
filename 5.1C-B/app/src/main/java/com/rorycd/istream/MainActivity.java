package com.rorycd.istream;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.rorycd.istream.data.UserRepository;
import com.rorycd.istream.ui.HomeFragment;
import com.rorycd.istream.ui.LoginFragment;

public class MainActivity extends AppCompatActivity implements Navigator {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // On the first OnCreate
        if (savedInstanceState == null) {
            UserRepository repo = UserRepository.getInstance(this);

            Fragment initial = repo.isLoggedIn() ? HomeFragment.newInstance() : LoginFragment.newInstance();

            navigateTo(initial, false);
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    @Override
    public void navigateTo(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main, fragment);
        if (addToBackStack) fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
