package com.lamaviedelivery.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.lamaviedelivery.R;
import com.lamaviedelivery.adapter.RequestAdapter;
import com.lamaviedelivery.databinding.FragmentInprogressBinding;
import com.lamaviedelivery.model.BookingModel;

import java.util.ArrayList;

public class CompleteFragment extends Fragment {
    FragmentInprogressBinding binding;
    ArrayList<BookingModel> arrayList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_inprogress, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        arrayList = new ArrayList<>();
        arrayList.add(new BookingModel("","complete"));
        arrayList.add(new BookingModel("","complete"));
        arrayList.add(new BookingModel("","complete"));

        binding.rvInprogress.setAdapter(new RequestAdapter(getActivity(),arrayList));

    }
}
