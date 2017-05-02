package com.example.monil.turbotask;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FragmentManager fragmentManager;
    private Fragment listFragment, taskFragment, calendarFragment, classesFragment, addTaskFragment ;


    private String currentFragment;
    private Fragment mFragment;
    private final int PICK_IMAGE_REQUEST = 111;
    private Uri filePath;;
    private StorageReference mStorageRef;
    private StorageReference userStorage;
    private  Bitmap bitmap;


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
                    mStorageRef = FirebaseStorage.getInstance().getReference();
                    userStorage = mStorageRef.child(user.getUid() + "/profile.jpg");

                    final long ONE_MEGABYTE = 6 * 1024 * 1024;
                    userStorage.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            // Data for "images/island.jpg" is returns, use this as needed
                            Log.d("image", "side of bytes: " + bytes.length);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap, 80, 100, false);
                            BitmapDrawable bd = new BitmapDrawable(getResources(), createCircleBitmap(bitmapResized));
                            ActionBar actionBar = getSupportActionBar();
                            actionBar.setTitle("TurboTask");
                            actionBar.setDisplayShowHomeEnabled(true);
                            actionBar.setLogo(bd);
                            actionBar.setDisplayUseLogoEnabled(true);
                            getSupportActionBar().setHomeAsUpIndicator(bd);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                            Log.d("image", exception.toString());
                        }
                    });
                    scheduleAlarm();
                    Log.d("auth", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("auth", "onAuthStateChanged:signed_out");
                    Intent myIntent = new Intent(MainActivity.this, SignInActivity.class);
                    MainActivity.this.startActivity(myIntent);
                    finish();
                }

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setNavigationBarColor(getResources().getColor(R.color.darkBlue));
                }
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
                            case R.id.add:
                                changeFragment(AddTaskFragment.TAG_ADD_TASK);
                                break;
                            case R.id.classes:
                                changeFragment(ClassesFragment.TAG_CLASSES);
                                break;

                        }
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
            changeFragment(TaskListFragment.TAG_TASK_LIST);

        }
        else{
            Log.d("retain", "savedInstance was not null");
            String currentTag = ((TaskFragment)taskFragment).getActiveTag();
            if(currentTag != null){
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
            case R.id.profile:
                Intent imageIntent = new Intent();
                imageIntent.setType("image/*");
                imageIntent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(imageIntent, "Select Image"), PICK_IMAGE_REQUEST);

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

    public Bitmap createCircleBitmap(Bitmap bitmapimg){
        Bitmap output = Bitmap.createBitmap(bitmapimg.getWidth(),
                bitmapimg.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmapimg.getWidth(),
                bitmapimg.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmapimg.getWidth() / 2,
                bitmapimg.getHeight() / 2, bitmapimg.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmapimg, rect, rect, paint);
        return output;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data.getData() != null) {
            filePath = data.getData();
            File filePathString = new File(filePath.toString());
            if(filePathString.length() <  6*(1024 * 1024) ){
                try {

                   bitmap = createCircleBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), filePath));


                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] mData = baos.toByteArray();

                    UploadTask uploadTask = userStorage.putBytes(mData);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap, 80, 100, false);
                            BitmapDrawable bd = new BitmapDrawable(getResources(), createCircleBitmap(bitmapResized));
                            ActionBar actionBar = getSupportActionBar();
                            actionBar.setTitle("TurboTask");
                            actionBar.setDisplayShowHomeEnabled(true);
                            actionBar.setLogo(bd);
                            actionBar.setDisplayUseLogoEnabled(true);
                            getSupportActionBar().setHomeAsUpIndicator(bd);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(com.example.monil.turbotask.MainActivity.this, "Image size to large.",
                        Toast.LENGTH_LONG).show();            }

        }
    }
}
