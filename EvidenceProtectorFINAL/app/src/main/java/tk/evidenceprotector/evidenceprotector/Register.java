
package tk.evidenceprotector.evidenceprotector;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.HashMap;

public class Register extends AppCompatActivity implements View.OnClickListener
{

    private EditText editTextFirstname;
    private EditText editTextLastname;
    private EditText editTextUsername;
    private EditText editTextPassword;

    private Button buttonRegister;

    private static final String REGISTER_URL = "http://192.168.1.9/evidenceprotector/register.php";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextFirstname = (EditText) findViewById(R.id.editTextFirstName);
        editTextLastname = (EditText) findViewById(R.id.editTextLastName);
        editTextUsername = (EditText) findViewById(R.id.editTextUserName);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);


        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if(v == buttonRegister)
        {
            registerUser();
        }
    }

    private void registerUser()
    {
        String firstname = editTextFirstname.getText().toString().trim().toLowerCase();
        String lastname = editTextLastname .getText().toString().trim().toLowerCase();
        String username = editTextUsername.getText().toString().trim().toLowerCase();
        String password = editTextPassword.getText().toString().trim().toLowerCase();


        register(firstname,lastname,username, password);
    }

    private void register(String firstname,String lastname,String username, String password)
    {
        class RegisterUser extends AsyncTask<String, Void, String>{
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();// RegisterUserClass Object created and Intialized


            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                loading = ProgressDialog.show(Register.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params)
            {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("firstname",params[0]);
                data.put("lastname",params[1]);
                data.put("username",params[2]);
                data.put("password",params[3]);

                String result = ruc.sendPostRequest(REGISTER_URL,data);

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(firstname,lastname,username,password);
    }
}