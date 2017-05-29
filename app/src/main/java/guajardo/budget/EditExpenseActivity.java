package guajardo.budget;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import guajardo.budget.models.Category;

public class EditExpenseActivity extends AppCompatActivity {
    Spinner spinner;
    EditText expenseDate, expenseStore, expenseAmount;
    String id, store, newStore, eDate, newExpenseDate, categoryId, newCategoryId, amount, newAmount;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    private ArrayList<String> categories = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);
        setTitle("Editar Gasto");

        store = getIntent().getStringExtra("store");
        amount = getIntent().getStringExtra("amount");
        eDate = getIntent().getStringExtra("eDate");
        id = getIntent().getStringExtra("id");
        categoryId = getIntent().getStringExtra("categoryId");

        expenseStore = (EditText) findViewById(R.id.expense_store_edit);
        expenseAmount = (EditText) findViewById(R.id.expense_amount_edit);

        spinner = (Spinner) findViewById(R.id.category_spinner);
        loadCategories();

        expenseDate = (EditText) findViewById(R.id.expense_date_edit);
        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        expenseDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EditExpenseActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        expenseDate.setText(eDate);
        expenseAmount.setText(amount);
        expenseStore.setText(store);
    }

    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        expenseDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void loadSpinnerData() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(dataAdapter.getPosition(eDate));


    }

    public void saveExpense(View view) {
        newStore = expenseStore.getText().toString();
        newCategoryId = spinner.getSelectedItem().toString();
        newAmount = expenseAmount.getText().toString();
        newExpenseDate = expenseDate.getText().toString();
        Intent i = new Intent(getBaseContext(), ExpenseActivity.class);

        if (newStore.matches("") || newAmount.matches("") || newCategoryId.matches("") || newExpenseDate.matches("")) {
            Toast.makeText(this, "Favor de llenar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        } else {
            sendData(id, newStore, newCategoryId, newExpenseDate, newAmount);
            startActivity(i);
        }
    }

    public void sendData(String id, String store, String categoryId, String expenseDate, String amount) {
        String url = "https://polar-sierra-78542.herokuapp.com/expenses/" + id;
        RequestQueue queue = Volley.newRequestQueue(this);
        Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("store", store);
        jsonParams.put("category_id", categoryId);
        jsonParams.put("expense_date", expenseDate);
        jsonParams.put("amount", amount);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.PUT, url,

                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Gasto actualizado exitosamente",
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

    public void loadCategories() {
        String url = "https://polar-sierra-78542.herokuapp.com/categories";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONArray jsonCategoriesArray = null;
                        try {
                            jsonCategoriesArray = response.getJSONArray("categories");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        for (int i = 0; i < jsonCategoriesArray.length(); i++) {
                            try {
                                Category category = new Category(jsonCategoriesArray.getJSONObject(i));
                                categories.add(category.getName());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        loadSpinnerData();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

}
