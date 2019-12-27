package com.example.studentprofile;

import android.content.Context;
import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class DisplayProfileFragment extends Fragment {


    private OnFragmentInteractionListener2 mListener;
    Profile profile;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView tv_Name =getActivity().findViewById(R.id.PersonName);
        TextView tv_studentID= getActivity().findViewById(R.id.PersonID);
        TextView tv_department = getActivity().findViewById(R.id.PersonDept);
        ImageView iv_avtar = getActivity().findViewById(R.id.personImage);
        Button edit = getActivity().findViewById(R.id.editProfile);
        iv_avtar.setImageResource(profile.imageID);
        tv_department.setText(profile.Department);
        tv_Name.setText(profile.firstname+" "+profile.lastname);
        tv_studentID.setText(profile.studentID);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.sendProfileData(profile);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(getArguments()!=null){
            profile = (Profile) getArguments().getSerializable("Display");
        }
        return inflater.inflate(R.layout.fragment_display_profile, container, false);
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener2) {
            mListener = (OnFragmentInteractionListener2) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public interface OnFragmentInteractionListener2 {
        void sendProfileData(Profile profile);
    }
}
