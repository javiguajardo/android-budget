package guajardo.budget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

import static android.R.attr.id;

public class EditCategoryActivity extends AppCompatActivity {
    EditText categoryName, categoryAmount;
    String amount, name, newName, newAmount, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        setTitle("Editar Categoria");

        categoryName = (EditText) findViewById(R.id.category_name_edit);
        categoryAmount = (EditText) findViewById(R.id.category_amount_edit);

        name = getIntent().getStringExtra("name");
        amount = getIntent().getStringExtra("amount");
        id = getIntent().getStringExtra("id");

        categoryName.setText(name);
        categoryAmount.setText(amount);
    }

    public void saveAccount(View view) {
        newName = categoryName.getText().toString();
        newAmount = categoryAmount.getText().toString();
        Intent i = new Intent(getBaseContext(), CategoryActivity.class);

        if (newName.matches("") || newAmount.matches("")) {
            Toast.makeText(this, "Favor de llenar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        } else {
            sendData(id, newName, newAmount);
            startActivity(i);
        }
    }

    public void sendData(String id, String name, String amount) {
        String url = "https://polar-sierra-78542.herokuapp.com/categories/" + id;
        RequestQueue queue = Volley.newRequestQueue(this);
        Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("name", name);
        jsonParams.put("amount", amount);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.PUT, url,

                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Categoria actualizada exitosamente",
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
