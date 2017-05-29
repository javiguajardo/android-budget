package guajardo.budget;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import guajardo.budget.adapters.AccountAdapter;
import guajardo.budget.adapters.ExpenseAdapter;
import guajardo.budget.models.Account;
import guajardo.budget.models.Expense;

public class ExpenseActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private MenuItem selectedItem;
    private ArrayList<Expense> expenses = new ArrayList<Expense>();
    ListView expenseList;
    TextView totalExpenses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        setTitle("Gastos");

        expenseList = (ListView) findViewById(R.id.expense_list);
        registerForContextMenu(expenseList);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        selectedItem = bottomNavigationView.getMenu().getItem(3);
        selectedItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_home:
                                Intent intentHome = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(intentHome);
                                break;
                            case R.id.menu_category:
                                Intent intentCategory = new Intent(getBaseContext(), CategoryActivity.class);
                                startActivity(intentCategory);
                                break;
                            case R.id.menu_account:
                                Intent intentAccount = new Intent(getBaseContext(), AccountActivity.class);
                                startActivity(intentAccount);
                                break;

                        }
                        return true;
                    }
                });

        loadExpenses();
    }

    public void addExpense(View view) {
        Intent intent = new Intent(getBaseContext(), AddExpenseActivity.class);
        startActivity(intent);
    }

    public void setExpenses(Expense expense) {
        expenses.add(expense);
    }

    public ArrayList<Expense> getExpenses() {
        return expenses;
    }

    public void loadExpenses() {
        String url = "https://polar-sierra-78542.herokuapp.com/expenses";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    ArrayList<Expense> expenseArray = new ArrayList<Expense>();
                    ExpenseAdapter expenseAdapter = new ExpenseAdapter(ExpenseActivity.this, expenseArray);

                    @Override
                    public void onResponse(JSONObject response) {

                        JSONArray jsonExpensesArray = null;
                        Double jsonTotalExpenses = null;
                        try {
                            jsonExpensesArray = response.getJSONArray("expenses");
                            jsonTotalExpenses = response.getDouble("total_expenses");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        for (int i = 0; i < jsonExpensesArray.length(); i++) {
                            try {
                                Expense expense = new Expense(jsonExpensesArray.getJSONObject(i));
                                expenseAdapter.add(expense);

                                setExpenses(expense);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        totalExpenses = (TextView) findViewById(R.id.total_expenses);
                        totalExpenses.setText("$ " + jsonTotalExpenses);

                        expenseList = (ListView) findViewById(R.id.expense_list);
                        expenseList.setAdapter(expenseAdapter);
                        expenseAdapter.notifyDataSetChanged();
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


    public void deleteExpense(String id) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://polar-sierra-78542.herokuapp.com/expenses/" + id;

        StringRequest dr = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent i = new Intent(getBaseContext(), ExpenseActivity.class);
                        startActivity(i);
                        finish();

                        Toast.makeText(getApplicationContext(), "Gasto eliminado exitosamente",
                                Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );
        queue.add(dr);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Editar");//groupId, itemId, order, title
        menu.add(0, v.getId(), 0, "Eliminar");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;

        if (item.getTitle() == "Editar") {
            Intent intent = new Intent(getBaseContext(), EditExpenseActivity.class);

            intent.putExtra("id", expenses.get(index).getId());
            intent.putExtra("eDate", expenses.get(index).getDate());
            intent.putExtra("store", expenses.get(index).getStore());
            intent.putExtra("amount", expenses.get(index).getAmount());
            intent.putExtra("categoryId", expenses.get(index).getCategoryId());

            startActivity(intent);
        } else if (item.getTitle() == "Eliminar") {
            String expenseId = expenses.get(index).getId();

            deleteExpense(expenseId);
        } else {
            return false;
        }
        return true;
    }
}
