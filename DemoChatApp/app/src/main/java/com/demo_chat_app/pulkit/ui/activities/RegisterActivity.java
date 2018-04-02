package com.demo_chat_app.pulkit.ui.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo_chat_app.pulkit.R;
import com.demo_chat_app.pulkit.config.AppConstant;
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
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    private static String TAG = RegisterActivity.class.getName();

    @BindView(R.id.et_first_name)
    EditText etFirstName;
    @BindView(R.id.et_last_name)
    EditText etLastName;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_confirm_password)
    EditText etConfirmPassword;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.tv_already_account)
    TextView tvAlreadyAccount;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;

    private Activity activity;

    private String firstName, lastName, email, phone, password, confirmPassword;

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        activity = this;
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

    }

    @OnClick(R.id.btn_submit)
    public void onClickSubmit() {

        firstName = etFirstName.getText().toString();
        lastName = etLastName.getText().toString();
        email = etEmail.getText().toString();
        phone = etPhone.getText().toString();
        password = etPassword.getText().toString();
        confirmPassword = etConfirmPassword.getText().toString();

        if (validate()) {
            createUser(email, password);

        } else {
            Toast.makeText(this, "Invalid email or not match password", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validate() {

        if (StringUtils.isEmpty(firstName)) {
            SnackbarUtil.showWarningShortSnackbar(scrollView, getMessage(R.string.empty_first_name));
            return false;

        } else if (StringUtils.isEmpty(lastName)) {
            SnackbarUtil.showWarningShortSnackbar(scrollView, getMessage(R.string.empty_last_name));
            return false;

        } else if (StringUtils.isEmpty(email)) {
            SnackbarUtil.showWarningShortSnackbar(scrollView, getMessage(R.string.empty_email));
            return false;

        } else if (!StringUtils.isEmailMatch(email)) {
            return false;

        } else if (StringUtils.isEmpty(phone)) {
            SnackbarUtil.showWarningShortSnackbar(scrollView, getMessage(R.string.empty_phone));
            return false;

        } else if (phone.length() < 10) {
            SnackbarUtil.showWarningShortSnackbar(scrollView, getMessage(R.string.valid_phone));
            return false;

        } else if (StringUtils.isEmpty(password)) {
            SnackbarUtil.showWarningShortSnackbar(scrollView, getMessage(R.string.empty_password));
            return false;

        }
        return true;
    }

    private void createUser(String email, String password) {
        CommonUtils.showProgressDilaog(activity);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                CommonUtils.dismissProgressDilaog();

                                if (task.isSuccessful()) {

                                    initNewUserInfo(task.getResult().getUser());

                                    Toast.makeText(RegisterActivity.this, "Register and Login success", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(RegisterActivity.this, "Register Unsuccessfull", Toast.LENGTH_SHORT).show();

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

    private void initNewUserInfo(FirebaseUser user) {

        User newUser = new User();

        newUser.name = firstName + " " + lastName;
        newUser.email = user.getEmail();
        newUser.avatar = AppConstant.STR_DEFAULT_BASE64;
//        newUser.uriAvatar = user.getPhotoUrl();

        firebaseDatabase.getReference().child("user/" + user.getUid()).setValue(newUser);

    }

    private String getMessage(int id) {
        return getResources().getString(id);
    }

    @OnClick(R.id.tv_already_account)
    public void onClickAlreadyAccount() {
        finish();
    }

}
