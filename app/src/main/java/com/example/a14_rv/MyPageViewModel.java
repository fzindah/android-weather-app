package com.example.a14_rv;

import android.util.Pair;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class MyPageViewModel extends ViewModel {

    private MutableLiveData<Pair<Integer, String>> mv_mIndexTitle = new MutableLiveData<>();
    private LiveData<String> mv_mText = Transformations.map(
            mv_mIndexTitle, new Function< Pair<Integer,String>, String>() {
        @Override
        public String apply(Pair<Integer,String> input) {
            return "Page " + input.first + " (" + input.second + ")" + " is selected";
        }
    });

    public void mf_setIndexTitle(int index, String title) {
        mv_mIndexTitle.setValue(new Pair<>(index, title));
    }

    public LiveData<String> mf_getText() {
        return mv_mText;
    }
}