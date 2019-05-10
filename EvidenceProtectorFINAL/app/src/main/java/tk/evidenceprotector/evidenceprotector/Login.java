
package tk.evidenceprotector.evidenceprotector;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.HashMap;

public class Login extends AppCompatActivity implements View.OnClickListener
{

    private EditText editTextUsername;
    private EditText editTextPassword;

    private Button buttonRegister;
    private Button buttonLogin;

    private static final String LOGIN_URL = "http://192.168.1.9/evidenceprotector/login.php";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = (EditText) findViewById(R.id.username);
        editTextPassword = (EditText) findViewById(R.id.password);

        buttonLogin = (Button) findViewById(R.id.buttonUserLogin);
        buttonRegister = (Button) findViewById(R.id.buttonRegisterUser);

        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
    }
    @Override
    public void onClick(View v)
    {
        if(v == buttonLogin)
        {
            login();
        }
        else if(v == buttonRegister)
        {
            Intent intent = new Intent(Login.this,Register.class);
            startActivity(intent);
        }
    }


    private void login()
    {

        String username = editTextUsername.getText().toString().trim().toLowerCase();
        String password = editTextPassword.getText().toString().trim().toLowerCase();

        userLogin(username, password);
    }

    private void userLogin(final String username, final String password)
    {
        class UserLoginClass extends AsyncTask<String, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                loading = ProgressDialog.show(Login.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                loading.dismiss();
                String[] parts = s.split(" ");
                String part1 = parts[0];
                String part2 = parts[1];
                String part3 = parts[2];

                if(part1.equalsIgnoreCase("success"))
                {
                    Toast.makeText(Login.this,"Login Sucessful",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this,SelectOption.class);
                    intent.putExtra("sentFirstname",part2);
                    intent.putExtra("sentLastname",part3);
                    startActivity(intent);
                }else{
                    Toast.makeText(Login.this,s,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params)
            {
                HashMap<String, String> data = new HashMap<String,String>();
                data.put("username",params[0]);
                data.put("password",params[1]);
                RegisterUserClass ruc = new RegisterUserClass();// RegisterUserClass Object

                String result = ruc.sendPostRequest(LOGIN_URL,data);

                return  result;
            }
        }

        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(username,password);
    }

}