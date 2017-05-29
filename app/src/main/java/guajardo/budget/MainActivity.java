package guajardo.budget;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import guajardo.budget.adapters.BudgetAdapter;
import guajardo.budget.adapters.CategoryAdapter;
import guajardo.budget.models.Category;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private MenuItem selectedItem;
    private ArrayList<Category> categories = new ArrayList<Category>();
    ListView budgetList;
    TextView budgetTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Presupuesto");

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        selectedItem = bottomNavigationView.getMenu().getItem(0);
        selectedItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_account:
                                Intent intentAccount = new Intent(getBaseContext(), AccountActivity.class);
                                startActivity(intentAccount);
                                break;
                            case R.id.menu_category:
                                Intent intentCategory = new Intent(getBaseContext(), CategoryActivity.class);
                                startActivity(intentCategory);
                                break;
                            case R.id.menu_expenses:
                                Intent intentExpense = new Intent(getBaseContext(), ExpenseActivity.class);
                                startActivity(intentExpense);
                                break;
                        }
                        return true;
                    }
                });
        loadBudgets();
    }

    public void loadBudgets() {
        String url = "https://polar-sierra-78542.herokuapp.com/budgets";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    ArrayList<Category> categoryArray = new ArrayList<Category>();
                    BudgetAdapter budgetAdapter = new BudgetAdapter(MainActivity.this, categoryArray);

                    @Override
                    public void onResponse(JSONObject response) {

                        JSONArray jsonCategoriesArray = null;
                        Double jsonCashFlow = null;
                        try {
                            jsonCategoriesArray = response.getJSONArray("categories");
                            jsonCashFlow = response.getDouble("cash_flow");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        for(int i = 0; i < jsonCategoriesArray.length(); i++) {
                            try {
                                Category category = new Category(jsonCategoriesArray.getJSONObject(i));
                                budgetAdapter.add(category);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        budgetTotal = (TextView) findViewById(R.id.budget_total);
                        budgetTotal.setText("$ " + jsonCashFlow);

                        budgetList = (ListView) findViewById(R.id.budget_list);
                        budgetList.setAdapter(budgetAdapter);
                        budgetAdapter.notifyDataSetChanged();
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
