package com.demo_chat_app.pulkit.ui.activities;

    /* AuthStateListener is called when there is a change in the authentication state.
    * When a user is signed in,
    * When the current user is signed out,
    * When the current user changes
    * When there is a change in the current user's token, */

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.EventLogTags;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo_chat_app.pulkit.R;
import com.demo_chat_app.pulkit.config.App;
import com.demo_chat_app.pulkit.config.AppConstant;
import com.demo_chat_app.pulkit.config.AppPreferences;
import com.demo_chat_app.pulkit.models.User;
import com.demo_chat_app.pulkit.utils.CommonUtils;
import com.demo_chat_app.pulkit.utils.SnackbarUtil;
import com.demo_chat_app.pulkit.utils.StringUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private static String TAG = LoginActivity.class.getName();

    @BindView(R.id.et_email)
    public EditText etEmail;
    @BindView(R.id.et_password)
    public EditText etPassword;
    @BindView(R.id.cb_remember_me)
    public CheckBox cbRememberMe;
    @BindView(R.id.btn_submit)
    public Button btnSubmit;
    @BindView(R.id.tv_forgot_password)
    public TextView tvForgotPassword;
    @BindView(R.id.tv_new_account)
    public TextView tvNewAccount;
    @BindView(R.id.scroll_view)
    public ScrollView scrollView;

    private Activity activity;

    private String email, password;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;       //Description on top
    private FirebaseUser user;
    private boolean firstTimeAccess;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        activity = this;
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firstTimeAccess = true;

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void init() {

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    AppConstant.UID = user.getUid();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    if (firstTimeAccess) {
                        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Log.d(TAG, "onAuthStateChanged:signed_out");
                    }
                    firstTimeAccess = false;
                }

            }
        };
    }

    @OnClick(R.id.cb_remember_me)
    public void onClickRememberMe() {
        //Todo: remember me

    }

    @OnClick(R.id.btn_submit)
    public void onClickAlreadyAccount() {

        email = etEmail.getText().toString();
        password = etPassword.getText().toString();

        if (validate()) {
            signIn(email, password);

        } else {
            Toast.makeText(this, "Invalid email or empty password", Toast.LENGTH_SHORT).show();
        }
    }

    private void signIn(String email, String password) {
        CommonUtils.showProgressDilaog(activity);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        CommonUtils.dismissProgressDilaog();

                        if (task.isSuccessful()) {

                            saveUserInfo();
                            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(LoginActivity.this, "Login Unsuccessfull", Toast.LENGTH_SHORT).show();

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        CommonUtils.dismissProgressDilaog();
                    }
                });

    }

    private void saveUserInfo() {

        firebaseDatabase.getReference().child("user/" + AppConstant.UID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        HashMap hashUser = (HashMap) dataSnapshot.getValue();
                        User userInfo = new User();

                        userInfo.name = (String) hashUser.get("name");
                        userInfo.email = (String) hashUser.get("email");
                        userInfo.avatar = (String) hashUser.get("avatar");
//                        userInfo.uriAvatar = (Uri) hashUser.get("uriAvatar");

                        /*Save this Info in the SharedPreferences.*/
                        App.getPreferences().saveString(AppConstant.SHARE_KEY_NAME, userInfo.name);
                        App.getPreferences().saveString(AppConstant.SHARE_KEY_EMAIL, userInfo.email);
                        App.getPreferences().saveString(AppConstant.SHARE_KEY_AVATAR, userInfo.avatar);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private boolean validate() {

        if (StringUtils.isEmpty(email)) {
            SnackbarUtil.showWarningShortSnackbar(scrollView, getMessage(R.string.empty_email));
            return false;
        } else if (!StringUtils.isEmailMatch(email)) {
            return false;

        } else if (StringUtils.isEmpty(password)) {
            SnackbarUtil.showWarningShortSnackbar(scrollView, getMessage(R.string.empty_password));
            return false;
        }
        return true;
    }


    @OnClick(R.id.tv_forgot_password)
    public void onClickForgotPassword() {

        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_forgot_password);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        final EditText etEmail = dialog.findViewById(R.id.et_email);
        Button btnSubmit = dialog.findViewById(R.id.btn_submit);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = etEmail.getText().toString();

                if (validate()) {
                    resetPassword(email);

                } else {
                    SnackbarUtil.showSuccessLongSnackbar(scrollView, getMessage(R.string.success_send_email));

                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void resetPassword(String email) {
        CommonUtils.showProgressDilaog(activity);

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        CommonUtils.dismissProgressDilaog();

                        if (task.isSuccessful()) {
                            successDialog();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        CommonUtils.dismissProgressDilaog();
                    }
                });

    }

    private void successDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Alert")
                .setCancelable(false)
                .setMessage("Reset password is sent on your registered email")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.show();
    }

    private String getMessage(int id) {
        return getResources().getString(id);
    }

    @OnClick(R.id.tv_new_account)
    public void onClickNewAccount() {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth.removeAuthStateListener(mAuthListener);
    }

}
