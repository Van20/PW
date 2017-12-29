package smktelkom_mlg.sch.id.mywallet.SignUp_screen;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import smktelkom_mlg.sch.id.mywallet.Beranda_screen.MainActivity;
import smktelkom_mlg.sch.id.mywallet.Login_screen.LoginActivity;
import smktelkom_mlg.sch.id.mywallet.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class SignUpActivity extends AppCompatActivity {

    private EditText inputUsername,inputEmail, inputPassword, inputConfirmPasswd;
    private TextView signInText;
    private Button   btnSignUp;
    private ProgressBar progressBar;
    private ImageView Profile;
    File file;
    Uri uri;
    Intent CamIntent, GalIntent, CropIntent ;
    public  static final int RequestPermissionCode  = 1 ;
    DisplayMetrics displayMetrics ;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseUser mmUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        getWindow().setBackgroundDrawableResource(R.drawable.background);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputUsername = (EditText) findViewById(R.id.username);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputConfirmPasswd = (EditText) findViewById(R.id.passwordconfirm);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        signInText = (TextView) findViewById(R.id.txtlogin);
        Profile = (ImageView) findViewById(R.id.profile_image);

        EnableRuntimePermission();

        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {

                                             String username = inputUsername.getText().toString();
                                             String email = inputEmail.getText().toString();
                                             final String password = inputPassword.getText().toString();
                                             final String confirm = inputConfirmPasswd.getText().toString();

                                             UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                                     .setPhotoUri(Uri.parse(email))
                                                     .setDisplayName(username)
                                                     .build();


                                             if (TextUtils.isEmpty(username)) {
                                                 Toast.makeText(getApplicationContext(), "Enter username!", Toast.LENGTH_SHORT).show();
                                                 return;
                                             }

                                             if (TextUtils.isEmpty(email)) {
                                                 Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                                                 return;
                                             }

                                             if (TextUtils.isEmpty(password)) {
                                                 Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                                                 return;
                                             }

                                             if (TextUtils.isEmpty(confirm)) {
                                                 Toast.makeText(getApplicationContext(), "Enter confirm password!", Toast.LENGTH_SHORT).show();
                                                 return;
                                             }

                                             if (password.length() < 6) {
                                                 Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                                                 return;
                                             }

                                             // TODO Auto-generated method stub
                                             if (password.equals(confirm)) {
                                                 //password and confirm passwords equal.go to next step
                                                 mAuth.getCurrentUser();
                                             }
                                             else {
                                                 //passwords not matching.please try again
                                                 Toast.makeText(getApplicationContext(), "Password & Confirm must be same characters!", Toast.LENGTH_SHORT).show();
                                                 return;
                                             }
                                             if (uri == null) {
                                                 Toast.makeText(SignUpActivity.this, "Error updating image",
                                                         Toast.LENGTH_SHORT).show();
                                             }

                                             progressBar.setVisibility(View.VISIBLE);
                                             //create user
                                             //create user
                                             mAuth.createUserWithEmailAndPassword(email, password)
                                                     .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                                         @Override
                                                         public void onComplete(@NonNull Task<AuthResult> task) {
                                                             Toast.makeText(SignUpActivity.this, "Register account was successfully processed!" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                                             progressBar.setVisibility(View.GONE);
                                                             // If sign in fails, display a message to the user. If sign in succeeds
                                                             // the auth state listener will be notified and logic to handle the
                                                             // signed in user can be handled in the listener.
                                                             if (!task.isSuccessful()) {
                                                                 Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                                                         Toast.LENGTH_SHORT).show();
                                                             } else {
                                                                 startActivity(new Intent(SignUpActivity.this, MainActivity.class)); //awalnya milik MainActivity.java
                                                                 finish();
                                                             }
                                                         }
                                                     });

                mmUser.updateProfile(profileUpdate)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (task.isSuccessful()) { //success on updating user profile
                                    Toast.makeText(SignUpActivity.this, "Update Profile Succedeed",
                                            Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                } else { //failed on updating user profile
                                    Toast.makeText(SignUpActivity.this, "Update Profile Failed",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

        });

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ClickImageFromCamera() ;
                GetImageFromGallery();

            }
        });
    }

    public void ClickImageFromCamera() {

        CamIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        file = new File(Environment.getExternalStorageDirectory(),
                "file" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        uri = Uri.fromFile(file);

        CamIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);

        CamIntent.putExtra("return-data", true);

        startActivityForResult(CamIntent, 0);

    }

    public void GetImageFromGallery(){

        GalIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(GalIntent, "Select Image From Gallery"), 2);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {

            ImageCropFunction();

        }
        else if (requestCode == 2) {

            if (data != null) {

                uri = data.getData();

                ImageCropFunction();

            }
        }
        else if (requestCode == 1) {

            if (data != null) {

                Bundle bundle = data.getExtras();

                Bitmap bitmap = bundle.getParcelable("data");

                Profile.setImageBitmap(bitmap);

            }
        }
    }

    public void ImageCropFunction() {

        // Image Crop Code
        try {
            CropIntent = new Intent("com.android.camera.action.CROP");

            CropIntent.setDataAndType(uri, "image/*");

            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 360);
            CropIntent.putExtra("outputY", 360);
            CropIntent.putExtra("aspectX", 1);
            CropIntent.putExtra("aspectY", 1);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);

            startActivityForResult(CropIntent, 1);

        } catch (ActivityNotFoundException e) {

        }
    }
    //Image Crop Code End Here

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(SignUpActivity.this,
                Manifest.permission.CAMERA))
        {

            ActivityCompat.requestPermissions(SignUpActivity.this,new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(SignUpActivity.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(SignUpActivity.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}