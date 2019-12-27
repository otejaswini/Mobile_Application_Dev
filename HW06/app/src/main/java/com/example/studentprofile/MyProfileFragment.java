package com.example.studentprofile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class MyProfileFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    int ID;
    String dept = "CS";
    Profile profile=null;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RadioGroup rg_department = getActivity().findViewById(R.id.rg);
        rg_department.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.CS) {
                    dept = "CS";
                }
                if (checkedId == R.id.SIS) {
                    dept = "SIS";
                }
                if (checkedId == R.id.BIO) {
                    dept = "BIO";
                }
                if (checkedId == R.id.Other) {
                    dept = "Other";
                }
            }
        });
        if(getArguments() !=null) {
            profile = (Profile) getArguments().getSerializable(("Edit"));
            if(profile!=null){
                EditText firstName = getActivity().findViewById(R.id.fName);
                EditText lastName = getActivity().findViewById(R.id.lName);
                EditText studentID = getActivity().findViewById(R.id.stuID);
                firstName.setText(profile.firstname);
                lastName.setText(profile.lastname);
                studentID.setText(profile.studentID);
                ID = profile.imageID;
                ImageView iv = getActivity().findViewById(R.id.avatar);
                iv.setImageResource(profile.imageID);
                if (profile.Department.equals("CS")) {
                    RadioButton rb = (RadioButton) getActivity().findViewById(R.id.CS);
                    rb.setChecked(true);
                }
                if (profile.Department.equals("SIS")) {
                    RadioButton rb = (RadioButton) getActivity().findViewById(R.id.SIS);
                    rb.setChecked(true);
                }
                if (profile.Department.equals("BIO")) {
                    RadioButton rb = (RadioButton) getActivity().findViewById(R.id.BIO);
                    rb.setChecked(true);
                }
                if (profile.Department.equals("Other")) {
                    RadioButton rb = (RadioButton) getActivity().findViewById(R.id.Other);
                    rb.setChecked(true);
                }
            }

        }
        getActivity().findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText fName = getActivity().findViewById(R.id.fName);
                EditText lName = getActivity().findViewById(R.id.lName);
                EditText stuID = getActivity().findViewById(R.id.stuID);

                String firstName = fName.getText().toString();
                String lastName = lName.getText().toString();
                String studentID = stuID.getText().toString();

                if (firstName.length() != 0 && studentID.length() == 9) {
                    Profile profile = new Profile();
                    profile.firstname = firstName;
                    profile.lastname = lastName;
                    profile.studentID = studentID;
                    profile.Department = dept;
                    profile.imageID = ID;

                    Gson gson = new Gson();
                    String jsonString = gson.toJson(profile);
                    Context ctx = getActivity();
                    SharedPreferences sharedPreferences = ctx.getSharedPreferences("ProfileData", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Key", jsonString);
                    editor.commit();
                    mListener.getProfileData(profile);

                } else {
                    if (firstName.length() == 0) {
                        fName.setError("Cannot be empty");
                    }
                    if (studentID.length() == 0) {
                        stuID.setError("Student ID must be a nine digit number");
                    } else {
                        stuID.setError("Enter student ID");
                    }
                }
            }
        });


        getActivity().findViewById(R.id.avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.GoToNextFragment(profile);
            }
        });
        if (ID != 0) {
            ImageView iv = getActivity().findViewById(R.id.avatar);
            iv.setImageResource(ID);
        }
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getArguments()!=null){
            ID = getArguments().getInt("ImageID");
            profile = (Profile) getArguments().getSerializable(("Edit"));
        }
        return inflater.inflate(R.layout.fragment_my_profile, container, false);

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MyProfileFragment.OnFragmentInteractionListener) {
            mListener = (MyProfileFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    public interface OnFragmentInteractionListener {
       void GoToNextFragment(Profile profile);
       void getProfileData(Profile profile);
    }
}
