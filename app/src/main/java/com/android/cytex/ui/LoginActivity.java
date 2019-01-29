package com.android.cytex.ui;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.cytex.Interface.VolleySingleton;
import com.android.cytex.acivities.Kuneyi;
import com.android.cytex.backgroundtask.DownloadCompanies;
import com.android.cytex.backgroundtask.DownloadUsers;
import com.android.cytex.db.DbContract;
import com.android.cytex.db.DbHelper;
import com.android.cytex.model.AndroidUser;
import com.android.cytex.util.SHA1;
import com.android.cytex.util.UserSessionManager;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import com.android.cytex.R;
import com.android.cytex.data.SharedPreferenceHelper;
import com.android.cytex.data.StaticConfig;
import com.android.cytex.model.User;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class LoginActivity extends AppCompatActivity {
    private static String TAG = "LoginActivity";
    FloatingActionButton fab;
    private final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private EditText editTextUsername, editTextPassword;
    private LovelyProgressDialog waitingDialog;

    private AuthUtils authUtils;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private boolean firstTimeAccess;
    private StringRequest request;
    DownloadUsers downloadUsers;
    DbHelper dbHelper;
    UserSessionManager session;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        editTextUsername = (EditText) findViewById(R.id.et_username);
        editTextPassword = (EditText) findViewById(R.id.et_password);
        firstTimeAccess = true;
        initFirebase();
        downloadUsers=new DownloadUsers(getApplicationContext());
        downloadUsers.getJSON();

        dbHelper=new DbHelper(getApplicationContext());
        session = new UserSessionManager(getApplicationContext());


        HashMap<String, String> user = session.getUserDetails();
        String username = user.get(UserSessionManager.KEY_User);

        boolean loginStatus=session.isUserLoggedIn();
        if(loginStatus){

        }
        else{
          //  Toast.makeText(getApplicationContext(),"Please login to proceed",Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * Khởi tạo các thành phần cần thiết cho việc quản lý đăng nhập
     */
    private void initFirebase() {
        //Khoi tao thanh phan de dang nhap, dang ky
        mAuth = FirebaseAuth.getInstance();
        authUtils = new AuthUtils();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    StaticConfig.UID = user.getUid();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    if (firstTimeAccess) {
                        startActivity(new Intent(LoginActivity.this, Kuneyi.class));
                        LoginActivity.this.finish();
                    }
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                firstTimeAccess = false;
            }
        };

        //Khoi tao dialog waiting khi dang nhap
        waitingDialog = new LovelyProgressDialog(this).setCancelable(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void clickRegisterLayout(View view) {
        getWindow().setExitTransition(null);
        getWindow().setEnterTransition(null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options =
                    ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
            startActivityForResult(new Intent(this, RegisterActivity.class), StaticConfig.REQUEST_CODE_REGISTER, options.toBundle());
        } else {
            startActivityForResult(new Intent(this, RegisterActivity.class), StaticConfig.REQUEST_CODE_REGISTER);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == StaticConfig.REQUEST_CODE_REGISTER && resultCode == RESULT_OK) {
            //register here
            authUtils.createUser(new AndroidUser(data.getStringExtra(StaticConfig.STR_EXTRA_NAME),data.getStringExtra(StaticConfig.STR_EXTRA_SURNAME),
                    data.getStringExtra(StaticConfig.STR_EXTRA_PHONENUMBER),data.getStringExtra(StaticConfig.STR_EXTRA_USERNAME), data.getStringExtra(StaticConfig.STR_EXTRA_PASSWORD),data.getStringExtra(StaticConfig.STR_EXTRA_SECURITY),1));
        }
    }

    public void clickLogin(View view) {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        if (validate(username, password)) {
            authUtils.signIn(username, password);
        } else {
            Toast.makeText(this, "Invalid email or empty password", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED, null);
        finish();
    }

    private boolean validate(String emailStr, String password) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return (password.length() > 0 || password.equals(";")) && matcher.find();
    }

    public void clickResetPassword(View view) {
        String username = editTextUsername.getText().toString();
        if (validate(username, ";")) {
            authUtils.resetPassword(username);
        } else {
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Dinh nghia cac ham tien ich cho quas trinhf dang nhap, dang ky,...
     */
    class AuthUtils {
        /**
         * Action register
         *
         * @param user
         * @param user
         */
        void createUser(final AndroidUser user) {
            waitingDialog.setIcon(R.drawable.ic_add_friend)
                    .setTitle("Registering...")
                    .setTopColorRes(R.color.colorPrimary)
                    .show();
            //to be removed
            saveToAppServer(user);
            session.createUserLoginSession(user.getName(),user.getUsername(),SHA1.sha(user.getUsername(),user.getPassword()));
            Toast.makeText(LoginActivity.this, "Register and Login success", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, Kuneyi.class));
            mAuth.createUserWithEmailAndPassword(user.getUsername(), SHA1.sha(user.getUsername(),user.getPassword()))
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                            waitingDialog.dismiss();
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                new LovelyInfoDialog(LoginActivity.this) {
                                    @Override
                                    public LovelyInfoDialog setConfirmButtonText(String text) {
                                        findView(com.yarolegovich.lovelydialog.R.id.ld_btn_confirm).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dismiss();
                                            }
                                        });
                                        return super.setConfirmButtonText(text);
                                    }
                                }

                                        .setTopColorRes(R.color.colorAccent)
                                        .setIcon(R.drawable.ic_add_friend)
                                        .setTitle("Register false")
                                        .setMessage("Email exist or weak password!")
                                        .setConfirmButtonText("ok")
                                        .setCancelable(false)
                                        .show();


                            } else {
                                initNewUserInfo();
                                saveToAppServer(user);
                                Toast.makeText(LoginActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                               // LoginActivity.this.finish();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            waitingDialog.dismiss();
                        }
                    })
            ;
        }


        /**
         * Action Login
         *
         * @param email
         * @param password
         */
        void signIn(String email, String password) {
            waitingDialog.setIcon(R.drawable.ic_person_low)
                    .setTitle("Login....")
                    .setTopColorRes(R.color.colorPrimary)
                    .show();

            String pass=dbHelper.getlogin(email);
            if(pass.equals(SHA1.sha(email,password))){
                session.createUserLoginSession(email,email,password);
                startActivity(new Intent(LoginActivity.this, Kuneyi.class));
                LoginActivity.this.finish();
            }
            mAuth.signInWithEmailAndPassword(email, SHA1.sha(email,password))
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            waitingDialog.dismiss();
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail:failed", task.getException());
                                new LovelyInfoDialog(LoginActivity.this) {
                                    @Override
                                    public LovelyInfoDialog setConfirmButtonText(String text) {
                                        findView(com.yarolegovich.lovelydialog.R.id.ld_btn_confirm).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dismiss();
                                            }
                                        });
                                        return super.setConfirmButtonText(text);
                                    }
                                }
                                        .setTopColorRes(R.color.colorAccent)
                                        .setIcon(R.drawable.ic_person_low)
                                        .setTitle("Login false")
                                        .setMessage("Email not exist or wrong password!")
                                        .setCancelable(false)
                                        .setConfirmButtonText("Ok")
                                        .show();
                            } else {
                                saveUserInfo();
                                startActivity(new Intent(LoginActivity.this, Kuneyi.class));
                                LoginActivity.this.finish();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                           waitingDialog.dismiss();
                        }
                    });
        }

        /**
         * Action reset password
         *
         * @param email
         */
        void resetPassword(final String email) {
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            new LovelyInfoDialog(LoginActivity.this) {
                                @Override
                                public LovelyInfoDialog setConfirmButtonText(String text) {
                                    findView(com.yarolegovich.lovelydialog.R.id.ld_btn_confirm).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dismiss();
                                        }
                                    });
                                    return super.setConfirmButtonText(text);
                                }
                            }
                                    .setTopColorRes(R.color.colorPrimary)
                                    .setIcon(R.drawable.ic_pass_reset)
                                    .setTitle("Password Recovery")
                                    .setMessage("Sent email to " + email)
                                    .setConfirmButtonText("Ok")
                                    .show();
                        }
                    })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    new LovelyInfoDialog(LoginActivity.this) {
                        @Override
                        public LovelyInfoDialog setConfirmButtonText(String text) {
                            findView(com.yarolegovich.lovelydialog.R.id.ld_btn_confirm).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dismiss();
                                }
                            });
                            return super.setConfirmButtonText(text);
                        }
                    }
                            .setTopColorRes(R.color.colorAccent)
                            .setIcon(R.drawable.ic_pass_reset)
                            .setTitle("False")
                            .setMessage("False to sent email to " + email)
                            .setConfirmButtonText("Ok")
                            .show();
                }
            });
        }

        /**
         * Luu thong tin user info cho nguoi dung dang nhap
         */
        void saveUserInfo() {
            FirebaseDatabase.getInstance().getReference().child("user/" + StaticConfig.UID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    waitingDialog.dismiss();
                    HashMap hashUser = (HashMap) dataSnapshot.getValue();
                    User userInfo = new User();
                    userInfo.name = (String) hashUser.get("name");
                    userInfo.email = (String) hashUser.get("email");
                    userInfo.avata = (String) hashUser.get("avata");
                    SharedPreferenceHelper.getInstance(LoginActivity.this).saveUserInfo(userInfo);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        /**
         * Khoi tao thong tin mac dinh cho tai khoan moi
         */
        void initNewUserInfo() {
            User newUser = new User();
            newUser.email = user.getEmail();
            newUser.name = user.getEmail().substring(0, user.getEmail().indexOf("@"));
            newUser.avata = StaticConfig.STR_DEFAULT_BASE64;
            FirebaseDatabase.getInstance().getReference().child("user/" + user.getUid()).setValue(newUser);
        }

        private void saveToAppServer(final AndroidUser user){
            DbHelper dbHelper=new DbHelper(getApplicationContext());
            SQLiteDatabase database=dbHelper.getWritableDatabase();

            if(checkNetworkConnection()){
                request=new StringRequest(Request.Method.POST, DbContract.SERVER_REGISTER_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String myresponse=jsonObject.getString("response");
                            if(myresponse.equals("success")){
                                user.setSync_status(DbContract.SYNC_STATUS_OK);
                                saveToLocalStorage(user);
                                Toast.makeText(getApplicationContext(),"Thank you for registering, you can login",Toast.LENGTH_LONG).show();
                            }
                            else {
                                user.setSync_status(DbContract.SYNC_STATUS_FAILED);
                                saveToLocalStorage(user);
                                Toast.makeText(getApplicationContext(),"Thank you for registering,something went wrong on server",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        user.setSync_status(DbContract.SYNC_STATUS_FAILED);
                        saveToLocalStorage(user);
                        Toast.makeText(getApplicationContext(),"Thank you for registering,wait for server sync",Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> params=new HashMap<>();
                        params.put("name",user.getName());
                        params.put("surname",user.getSurname());
                        params.put("phonenumber",user.getPhonenumber());
                        params.put("username",user.getUsername());
                        params.put("password",SHA1.sha(user.getUsername(),user.getPassword()));
                        params.put("security",user.getSecurity());
                        return params;
                    }
                };
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
            }

        }

        private void saveToLocalStorage(AndroidUser user){
            DbHelper dbHelper=new DbHelper(getApplicationContext());
            SQLiteDatabase database=dbHelper.getWritableDatabase();
            dbHelper.saveUserToLocalDatabase(user,database);

          //  readFromLocalStorage();   //to refresh the recyclerview
            dbHelper.close();
        }

        public boolean checkNetworkConnection(){
            ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            return (networkInfo!=null && networkInfo.isConnected());
        }
    }
}
