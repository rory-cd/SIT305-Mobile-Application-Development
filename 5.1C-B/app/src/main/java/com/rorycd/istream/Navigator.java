package com.rorycd.istream;

import androidx.fragment.app.Fragment;

public interface Navigator {
    void navigateTo(Fragment fragment, boolean addToBackStack);
}
