package guajardo.budget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;


public class AddAcountActivity extends AppCompatActivity {
    Spinner spinner;
    EditText accountName, accountAmount;
    String name, amount, accountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_acount);
        setTitle("Agregar Cuenta");

        spinner = (Spinner) findViewById(R.id.account_type_spinner);
        accountName = (EditText) findViewById(R.id.account_name_edit);
        accountAmount = (EditText) findViewById(R.id.account_amount_edit);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.account_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void saveAccount(View view) {
        name = String.valueOf(accountName.getText());
        amount = String.valueOf(accountAmount.getText());
        accountType = spinner.getSelectedItem().toString();
        Intent i = new Intent(getBaseContext(), AccountActivity.class);

        if (name.matches("") || amount.matches("")) {
            Toast.makeText(this, "Favor de llenar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        } else {
            sendData(name, amount, accountType);
            startActivity(i);
        }

    }

    public void sendData(String name, String amount, String accountType) {
        String url = "https://polar-sierra-78542.herokuapp.com/accounts";
        RequestQueue queue = Volley.newRequestQueue(this);
        Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("name", name);
        jsonParams.put("amount", amount);
        jsonParams.put("acct_type", accountType);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,

                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Cuenta agregada exitosamente",
                                Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   Handle Error
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };
        queue.add(postRequest);

    }
}
