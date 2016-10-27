package com.example.asus.app.UI;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContentResolverCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.asus.app.UI.CreateAndJoinActivityHelperClass.Thread.AddClass;
import com.example.asus.app.UI.CreateAndJoinActivityHelperClass.Thread.AddTalk;
import com.example.asus.myapp.R;
import com.example.asus.pushreflash.LessonOrConversation;
import com.example.asus.pushreflash.LessonOrConversionAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.example.asus.app.UI.CreateAndJoinActivityHelperClass.Thread.AddClass.COURSENAME;
import static com.example.asus.app.UI.CreateAndJoinActivityHelperClass.Thread.AddClass.TEACHERNAME;
import static com.example.asus.app.UI.CreateAndJoinActivityHelperClass.Thread.AddTalk.GROUPNAME;
import static com.example.asus.app.UI.CreateAndJoinActivityHelperClass.Thread.AddTalk.USERNAME;
import static com.example.asus.app.UI.CreateAndJoinActivityHelperClass.Thread.CreateClass.CLASSNUMBER;
import static com.example.asus.app.UI.CreateAndJoinActivityHelperClass.Thread.CreateClass.CLASSTEACHERNAME;
import static com.example.asus.app.UI.CreateAndJoinActivityHelperClass.Thread.CreateClass.CREATECOURSENAME;
import static com.example.asus.app.UI.CreateAndJoinActivityHelperClass.Thread.CreateTalk.CREATETALKNAME;
import static com.example.asus.app.UI.CreateAndJoinActivityHelperClass.Thread.CreateTalk.TALKNUMBER;
import static com.example.asus.app.UI.CreateAndJoinActivityHelperClass.Thread.CreateTalk.TALKTEACHERNAME;

public class FunctionActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /////////////////////////////////////////////////////////////////
    public static final int CREATECOURSE = 0;
    public static final int JOINCOURSE = 1;
    public static final int CREATECONVERSATION = 2;
    public static final int JOINCONVERSATION = 3;
    public static final String ACTIVITY_TYPE = "activity_type";
//    public static final String ADDCLASS = "";
//    public static final String ADDTALK = "userName";
//    public static final String CREATECLASS = "userName";

    public static final String USER_NAME = "userName";

    private String mUserName = "测试";
    private List<LessonOrConversation> mLessonOrConversationList = new ArrayList<>();
    private LessonOrConversionAdapter mLessonOrConversationListArrayAdapter;
    /////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mLessonOrConversationListArrayAdapter =
                new LessonOrConversionAdapter(this, R.layout.lession_item, mLessonOrConversationList);
        ListView lessonConversionListVew = (ListView) findViewById(R.id.lesson_conversation_list_view);
        lessonConversionListVew.setAdapter(mLessonOrConversationListArrayAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            Bundle bundle = data.getExtras();
            String name = "";
            String creator = "";
            int number = 0;
            boolean isLesson = true;

            switch (requestCode) {
                case JOINCOURSE:
                     name = bundle.getString(COURSENAME);
                     creator = bundle.getString(TEACHERNAME);
                     number = Integer.parseInt(bundle.getString(AddClass.CLASSNUMBER));
                    isLesson = true;
                    Toast.makeText(this, name+creator+number, Toast.LENGTH_LONG).show();
                    break;
                case JOINCONVERSATION:
                    name = bundle.getString(GROUPNAME);
                     creator = bundle.getString(USERNAME);
                    number = Integer.parseInt(bundle.getString(AddTalk.TALKNUMBER));
                    isLesson = false;
                    Toast.makeText(this, name+creator+number, Toast.LENGTH_LONG).show();
                    break;
                case CREATECOURSE:
                    name = bundle.getString(CREATECOURSENAME);
                    creator = bundle.getString(CLASSTEACHERNAME);
                    number = bundle.getInt(CLASSNUMBER);
                    isLesson = true;
                    Toast.makeText(this, name+creator+number, Toast.LENGTH_LONG).show();
                    break;
                case CREATECONVERSATION:
                    name = bundle.getString(CREATETALKNAME);
                    creator = bundle.getString(TALKTEACHERNAME);
                    number = bundle.getInt(TALKNUMBER);
                    isLesson = false;
                    Toast.makeText(this, name+creator+number, Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            addmLessonOrConversationList(name,creator,number, isLesson);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.function, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent i = new Intent(this, CreateAndJoinActivity.class);
        i.putExtra(USER_NAME, mUserName);

        if (id == R.id.create_course) {
            i.putExtra(ACTIVITY_TYPE, CREATECOURSE);
            startActivityForResult(i, CREATECOURSE);

        } else if (id == R.id.join_course) {
            i.putExtra(ACTIVITY_TYPE, JOINCOURSE);
            startActivityForResult(i, JOINCOURSE);
        } else if (id == R.id.create_conversation) {
            i.putExtra(ACTIVITY_TYPE, CREATECONVERSATION);
            startActivityForResult(i, CREATECONVERSATION);

        } else if (id == R.id.join_conversation) {
            i.putExtra(ACTIVITY_TYPE, JOINCONVERSATION);
            startActivityForResult(i, JOINCONVERSATION);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void getUserInformation() {
         mUserName = getIntent().getExtras().getString(USER_NAME);
    }
    private void addmLessonOrConversationList(String name, String creator, int num, boolean isLesson) {
        LessonOrConversation lessonOrConversation =
                new LessonOrConversation(name, creator, num, R.mipmap.ic_launcher, isLesson);
        mLessonOrConversationList.add(lessonOrConversation);
        mLessonOrConversationListArrayAdapter.notifyDataSetChanged();
    }

    private void testData() {

    }
}
