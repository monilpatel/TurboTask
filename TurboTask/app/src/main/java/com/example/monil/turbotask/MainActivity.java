package com.example.monil.turbotask;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FragmentManager fragmentManager;
    private Fragment listFragment, taskFragment, calendarFragment, classesFragment, addTaskFragment ;


    private String currentFragment;
    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("auth", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("auth", "onAuthStateChanged:signed_out");
                    Intent myIntent = new Intent(MainActivity.this, SignInActivity.class);
                    MainActivity.this.startActivity(myIntent);
                    finish();
                }
        scheduleAlarm();
            }
        };

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.list:
                                changeFragment(TaskListFragment.TAG_TASK_LIST);
                                break;
//                            case R.id.calendar:
//                                changeFragment(CalendarFragment.TAG_CALENDAR);
//                                break;
                            case R.id.add:
                                changeFragment(AddTaskFragment.TAG_ADD_TASK);
                                break;
                            case R.id.classes:
                                changeFragment(ClassesFragment.TAG_CLASSES);
                                break;

                        }
//                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                        transaction.replace(R.id.frame_layout, selectedFragment);
//                        transaction.commit();
                        return true;
                    }
                });

        //TODO: it also defaults to the list view, thats why if you rotate it the screen goes blank



        taskFragment = (TaskFragment) fragmentManager.findFragmentByTag(TaskFragment.TAG_TASK_FRAGMENT);
        if(taskFragment == null){
            taskFragment = new TaskFragment();
            fragmentManager.beginTransaction().add(taskFragment, TaskFragment.TAG_TASK_FRAGMENT).commit();
        }

        if(savedInstanceState == null){
            Log.d("retain", "list view was created ");
//            listFragment = new TaskListFragment();
//            ((TaskFragment)taskFragment).setActiveTag(TaskListFragment.TAG_TASK_LIST);
//            fragmentManager.beginTransaction().add(listFragment, TaskListFragment.TAG_TASK_LIST).commit();
//            Log.d("retain", "taskFragment new tag: " +  ((TaskFragment)taskFragment).getActiveTag());
            changeFragment(TaskListFragment.TAG_TASK_LIST);

        }
        else{

            Log.d("retain", "savedInstance was not null");
            String currentTag = ((TaskFragment)taskFragment).getActiveTag();

            if(currentTag.equals(TaskListFragment.TAG_TASK_LIST)){
                Log.d("retain", "list view was retained" );
                listFragment = fragmentManager.findFragmentByTag(currentTag);
            }
            else if(currentTag.equals(AddTaskFragment.TAG_ADD_TASK)){
                Log.d("retain", "add task was retained" );
                addTaskFragment = fragmentManager.findFragmentByTag(currentTag);
            }
            else if(currentTag.equals(CalendarFragment.TAG_CALENDAR)){
                Log.d("retain", "calendar was retained" );
                calendarFragment = fragmentManager.findFragmentByTag(currentTag);
            }
            else if(currentTag.equals(ClassesFragment.TAG_CLASSES)){
                Log.d("retain", "classes was retained" );
                classesFragment = fragmentManager.findFragmentByTag(currentTag);
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                MainActivity.this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void changeFragment(String fragment_name){
        Fragment fragment;
        Class fragmentClass = null;
        if(fragment_name.equals(AddTaskFragment.TAG_ADD_TASK)){
            fragmentClass = AddTaskFragment.class;
            Log.d("retain", "add tasks fragment selected");
        }
        else if(fragment_name.equals(CalendarFragment.TAG_CALENDAR)){
            fragmentClass = CalendarFragment.class;
            Log.d("retain", "calendar fragment selected");
        }
        else if(fragment_name.equals(ClassesFragment.TAG_CLASSES)){
            fragmentClass = ClassesFragment.class;
            Log.d("retain", "classes fragment selected");
        }
        else if(fragment_name.equals(TaskListFragment.TAG_TASK_LIST)){
            fragmentClass = TaskListFragment.class;
            Log.d("retain", "task list fragment selected");
        }

        try{
            if(fragmentClass != null){
                currentFragment = fragment_name;
                fragment = (Fragment) fragmentClass.newInstance();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_layout, fragment, currentFragment);
                ft.addToBackStack(null);
                ((TaskFragment)taskFragment).setActiveTag(fragment_name);
                ft.commit();

            }

        }catch (Exception e){
            Log.d("fragment", "no fragment chosen");
            e.printStackTrace();

        }

    }

    @Override
    public void onBackPressed() {
    }

    public void scheduleAlarm() {
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every 5 seconds
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                AlarmManager.INTERVAL_DAY, pIntent);
        Log.d("timer", "Alarm timer started");
    }
}
