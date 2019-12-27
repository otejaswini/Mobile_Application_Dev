package com.example.studentprofile;

import android.content.Context;
import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;

public class SelectAvatarFragment extends Fragment {
    private OnFragmentInteractionListener1 mListener;
int imageID;
Profile profile;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(getArguments()!=null){
            profile = (Profile) getArguments().getSerializable("DataProfile");
        }
        View view = inflater.inflate(R.layout.fragment_select_avatar,container,false);
      return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().findViewById(R.id.f1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageID = R.drawable.avatar_f_1;
                if(profile!=null){
                    if(profile.imageID!=0){
                        profile.imageID = imageID;
                    }
                }
                mListener.SelectedImage(imageID, profile);
            }
        });
        getActivity().findViewById(R.id.f2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageID = R.drawable.avatar_f_2;
                if(profile!=null){
                    if(profile.imageID!=0){
                        profile.imageID = imageID;
                    }
                }
                mListener.SelectedImage(imageID, profile);
            }
        });
        getActivity().findViewById(R.id.f3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageID = R.drawable.avatar_f_3;
                if(profile!=null){
                    if(profile.imageID!=0){
                        profile.imageID = imageID;
                    }
                }
                mListener.SelectedImage(imageID, profile);
            }
        });
        getActivity().findViewById(R.id.m1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageID = R.drawable.avatar_m_1;
                if(profile!=null){
                    if(profile.imageID!=0){
                        profile.imageID = imageID;
                    }
                }
                mListener.SelectedImage(imageID, profile);
            }
        });
        getActivity().findViewById(R.id.m2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageID = R.drawable.avatar_m_2;
                if(profile!=null){
                    if(profile.imageID!=0){
                        profile.imageID = imageID;
                    }
                }
                mListener.SelectedImage(imageID, profile);
            }
        });
        getActivity().findViewById(R.id.m3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageID = R.drawable.avatar_m_3;
                if(profile!=null){
                    if(profile.imageID!=0){
                        profile.imageID = imageID;
                    }
                }
                mListener.SelectedImage(imageID, profile);
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SelectAvatarFragment.OnFragmentInteractionListener1) {
            mListener = (SelectAvatarFragment.OnFragmentInteractionListener1) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public interface OnFragmentInteractionListener1 {
        // TODO: Update argument type and name
        void SelectedImage(int i, Profile profile);
    }
}
