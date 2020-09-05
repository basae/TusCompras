package tucodigo.tuscompras;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import tucodigo.tuscompras.DB.MySQLiteHelper;
import tucodigo.tuscompras.Fragmentos.inicio;
import tucodigo.tuscompras.Fragmentos.login;
import tucodigo.tuscompras.Fragmentos.producto;
import tucodigo.tuscompras.Models.User;
import tucodigo.tuscompras.Models.UserMovil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences myPreferences;
    SharedPreferences.Editor myEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sharedpreference
        myPreferences= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        myEditor=myPreferences.edit();
        //creamos una instancia para la creacion de la base por primera vez o un adaptador para usarla en caso de existir
        MySQLiteHelper dbHelper =new MySQLiteHelper(this);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        setContentView(R.layout.activity_main);
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
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headerView = navigationView.getHeaderView(0);
        TextView tv = (TextView)headerView.findViewById(R.id.UserName);

        tv.setText("No Autenticado.");

        if(myPreferences.getString("Usuario","")!="")
        {
            User.UserInfo=new Gson().fromJson(myPreferences.getString("Usuario",""), UserMovil.class);
        }
        if(User.UserInfo!=null)
        {

            tv.setText(User.UserInfo.getUsername());
            setTitle(User.UserInfo.getUsername());
        }
        DisplayMenu(navigationView);
        //inicializar el Usuario

    }
    private void DisplayMenu(NavigationView navigationView)
    {
        Menu menu = navigationView.getMenu();
        for (int i = 0; i< menu.size();i++)
        {
            MenuItem menuItem=menu.getItem(i);
            switch (menuItem.getItemId())
            {
                case R.id.iniciar_sesion:
                    if(User.UserInfo!=null)
                    {
                        menuItem.setVisible(false);
                        menuItem.setEnabled(false);
                    }
                    else
                    {
                        menuItem.setVisible(true);
                        menuItem.setEnabled(true);
                    }
                    break;
                case R.id.logout:
                    if(User.UserInfo!=null)
                    {
                        menuItem.setVisible(true);
                        menuItem.setEnabled(true);
                    }
                    else
                    {
                        menuItem.setVisible(false);
                        menuItem.setEnabled(true);
                    }
                    break;
                case R.id.compras:
                    if(User.UserInfo!=null)
                    {
                        menuItem.setVisible(true);
                        menuItem.setEnabled(true);
                    }
                    else
                    {
                        menuItem.setVisible(false);
                        menuItem.setEnabled(true);
                    }
                    break;
            }
        }
    }
    @Override
    public void onBackPressed() {
        /*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        */
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
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

        if (id == R.id.iniciar_sesion) {
            Fragment fragment=new login();
            callFragment(fragment);
            // Handle the camera action
        } else if (id == R.id.compras) {
            Fragment fragment=new producto();
            callFragment(fragment);
        }
        else if (id == R.id.logout) {
            myPreferences= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            myEditor=myPreferences.edit();
            myEditor.clear();
            myEditor.commit();
            User.UserInfo = null;
            Intent intent=new Intent(this  , MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("logout", true);
            callFragment(new inicio());
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView tv = (TextView) headerView.findViewById(R.id.UserName);
        tv.setText("");
        setTitle(getString(R.string.app_name));
        if(!intent.hasExtra("logout")) {
            myPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            myEditor = myPreferences.edit();
            if (User.UserInfo != null) {
                myEditor.putString("Usuario", new Gson().toJson(User.UserInfo));
                myEditor.commit();
                tv.setText(User.UserInfo.getUsername());
                navigationView.setNavigationItemSelectedListener(this);
                Menu menu = navigationView.getMenu();
                Fragment inicio = new inicio();
                callFragment(inicio);
                setTitle(User.UserInfo.getUsername());
            }
        }
        DisplayMenu(navigationView);
    }


    void callFragment(Fragment fragment)
    {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contenedor_principal , fragment).commit();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        catch (Exception ex)
        {
            Toast.makeText(this,"",Toast.LENGTH_LONG);
        }
    }
}
