package smktelkom_mlg.sch.id.mywallet.Beranda_screen;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.utils.Utils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import smktelkom_mlg.sch.id.mywallet.Database_sql.Controller.SaldoController;
import smktelkom_mlg.sch.id.mywallet.Database_sql.Fragment.FragmentCategory;
import smktelkom_mlg.sch.id.mywallet.Database_sql.Fragment.FragmentDailyReport;
import smktelkom_mlg.sch.id.mywallet.Database_sql.Fragment.FragmentHome;
import smktelkom_mlg.sch.id.mywallet.Database_sql.Fragment.FragmentMonthlyReport;
import smktelkom_mlg.sch.id.mywallet.Database_sql.Fragment.FragmentPromo;
import smktelkom_mlg.sch.id.mywallet.Database_sql.Utils.SPManager;
import smktelkom_mlg.sch.id.mywallet.Login_screen.LoginActivity;
import smktelkom_mlg.sch.id.mywallet.Promo_screen.PromoActivity;
import smktelkom_mlg.sch.id.mywallet.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private SharedPreferences mSharedPreferences;
    TextView personname, personemail;
    private String mUsername, mEmail;
    private ImageView Photo;
    private GoogleApiClient mGoogleApiClient;
    public static final String ANONYMOUS = "anonymous";
    public static final String EMAIL = "@email.com";

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Fragment fragment = null;
    SPManager prefManager;
    FloatingActionButton fabBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        prefManager = new SPManager(this);
        fabBtn = (FloatingActionButton) findViewById(R.id.fab);
        fabBtn.setOnClickListener(new View.OnClickListener() {
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

        fragmentManager = getFragmentManager();
        if (savedInstanceState == null) {
            fragment = new FragmentHome();
            callFragment(fragment);
        }

        //Google login
        View hView = navigationView.getHeaderView(0);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mUsername = ANONYMOUS;
        mEmail = EMAIL;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //Get nama & email
        personname = (TextView) hView.findViewById(R.id.nama);
        personemail = (TextView) hView.findViewById(R.id.email);
        Photo = (ImageView) hView.findViewById(R.id.photo);

        final FirebaseUser USER = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : USER.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();
                Uri photoUrl = profile.getPhotoUrl();

                Picasso.with(getApplicationContext())
                        .load(mAuth.getCurrentUser().getPhotoUrl())
                        .resize(120, 120)
                        .centerCrop()
                        .into(Photo, new Callback() {
                            @Override
                            public void onSuccess() {
                                Bitmap imageBitmap = ((BitmapDrawable)
                                        Photo.getDrawable()).getBitmap();
                                RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                                imageDrawable.setCircular(true);
                                imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                                Photo.setImageDrawable(imageDrawable);
                            }

                            @Override
                            public void onError() {
                                Photo.setImageResource(R.drawable.ic_user_icon);
                            }
                        });
            }

        }

        if (mUser == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;

        } else {
            mUsername = mUser.getDisplayName();
            personname.setText(mUsername);

            mEmail = mUser.getEmail();
            personemail.setText(mEmail);

        }
    }

    public void showFloatingActionButton() {
        fabBtn.show();
    }
    public void hideFloatingActionButton() {
        fabBtn.hide();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Setting saldo
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showSaldoDialog();
            return true;
        }

        /**
         * Reset button
         */
        if (id == R.id.action_reset) {

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Are you sure want to Reset Money DATA?").setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            prefManager.setFirstTimeLaunch(true);
                            getApplicationContext().deleteDatabase("uangku");
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                            return;
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).show();
        }

        /**
         * Share button
         */
        if (id == R.id.share_action) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBodyText = getResources().getString(R.string.sharebody);
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.sharesubject) );
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.sharedialog)));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Dialog setting Saldo
     */
    private void showSaldoDialog() {
        final SaldoController saldoKontrol = new SaldoController(this);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_saldo, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);

        final EditText newsaldo = (EditText) mView.findViewById(R.id.userInputDialog);
        newsaldo.setText(saldoKontrol.getSaldo());
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        String saldo = newsaldo.getText().toString();

                        if (TextUtils.isEmpty(saldo)) {
                            Toast.makeText(getApplicationContext(), "Enter amount of Balance!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        saldoKontrol.updateSaldo(Integer.parseInt(newsaldo.getText().toString()));
                        callFragment(new FragmentHome());
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();

    }

    /**
     * Navigation drawer
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragment = new FragmentHome();
            callFragment(fragment);
        } else if (id == R.id.nav_kategori) {
            fragment = new FragmentCategory();
            callFragment(fragment);
        } else if (id == R.id.nav_promo) {
            fragment = new FragmentPromo();
            callFragment(fragment);
        }
        else if (id == R.id.nav_harian) {
            fragment = new FragmentDailyReport();
            callFragment(fragment);
        } else if (id == R.id.nav_bulanan) {
            fragment = new FragmentMonthlyReport();
            callFragment(fragment);
        } else if (id == R.id.nav_backup) {
            backupRestoredialog();
        } else if (id == R.id.nav_info) {
            showAboutDialog();
        } else if (id == R.id.nav_logout) {

            logout();
            return false;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Show About Dialog
     */
    private void showAboutDialog() {
        View mView = getLayoutInflater().inflate(R.layout.dialog_about, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.txt_login);
        builder.setTitle(R.string.app_name);
        builder.setView(mView);
        builder.create();
        builder.show();
    }

    /**
     * Selec Dialog For Backup or Restore
     */
    private void backupRestoredialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Backup report money");
        builder.setItems(R.array.dialog_backup, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Toast.makeText(MainActivity.this, smktelkom_mlg.sch.id.mywallet.Database_sql.Utils.Utils.doBackup(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        builder.show();
    }

    /**
     * Start Selected Fragment
     */

    private void callFragment(Fragment fragment) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
    }


    // [START signOut]
    private void logout() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure want to exit?\nIf you exit, your money data will be lost. \n\nUnless, Backup your money data first!").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mAuth.signOut();
                        mUser = null;
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();

                        prefManager.setFirstTimeLaunch(true);
                        getApplicationContext().deleteDatabase("uangku");
                        finish();
                        return;
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }
}

