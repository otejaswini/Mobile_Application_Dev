/*Group Number : 34
* Name : Tejaswini Oduru
* Name : Sumit Kawale
*  */

package com.example.studentprofile;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity implements MyProfileFragment.OnFragmentInteractionListener, SelectAvatarFragment.OnFragmentInteractionListener1 , DisplayProfileFragment.OnFragmentInteractionListener2{

    Fragment fragment;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context ctx = getApplicationContext();
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("ProfileData", MODE_PRIVATE);
        String jsonString = sharedPreferences.getString("Key", "");
        Gson gson = new Gson();
        Profile profile = gson.fromJson(jsonString, Profile.class);
      //  profile=null;
        if(profile==null) {
            getFragmentManager().beginTransaction().add(R.id.container, new MyProfileFragment(), "MyProfile").addToBackStack(null).commit();
        }
        else{
            getProfileData(profile);
        }
    }
    @Override
    public void GoToNextFragment(Profile profile) {
        SelectAvatarFragment selectAvatarFragment = new SelectAvatarFragment();
        Bundle args = new Bundle();
        args.putSerializable("DataProfile",profile);
        selectAvatarFragment.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.container,selectAvatarFragment,"SelectAvatarFragment").addToBackStack(null).commit();
    }
    @Override
    public void getProfileData(Profile profile) {
        DisplayProfileFragment displayProfileFragment = new DisplayProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable("Display",profile);
        displayProfileFragment.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.container,displayProfileFragment,"DisplayProfileFragment").addToBackStack(null).commit();
    }
    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
    @Override
   public void SelectedImage(int i, Profile profile) {
       MyProfileFragment profileFragment = new MyProfileFragment();
       Bundle args = new Bundle();
       args.putInt("ImageID",i);
       args.putSerializable("Edit",profile);
       profileFragment.setArguments(args);
       getFragmentManager().beginTransaction().replace(R.id.container,profileFragment,"MyProfile").addToBackStack(null).commit();

    }
    @Override
    public void sendProfileData(Profile profile) {
        MyProfileFragment profileFragment = new MyProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable("Edit",profile);
        profileFragment.setArguments(args);

        getFragmentManager().beginTransaction().replace(R.id.container,profileFragment,"MyProfile").addToBackStack(null).commit();
    }
}
