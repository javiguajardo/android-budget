package guajardo.budget;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
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
import guajardo.budget.models.Account;
import guajardo.budget.models.Category;


public class AccountActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private MenuItem selectedItem;
    private ArrayList<Account> accounts = new ArrayList<Account>();
    ListView accountList;
    TextView incomeAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setTitle("Cuentas");


        accountList = (ListView) findViewById(R.id.account_list);
        registerForContextMenu(accountList);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        selectedItem = bottomNavigationView.getMenu().getItem(1);
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
                        }
                        return true;
                    }
                });

        loadAccounts();
    }

    public void addAccount(View view) {
        Intent intent = new Intent(getBaseContext(), AddAcountActivity.class);
        startActivity(intent);
    }

    public void setAccounts(Account account) {
        accounts.add(account);
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public void loadAccounts() {
        String url = "https://polar-sierra-78542.herokuapp.com/accounts";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    ArrayList<Account> accountArray = new ArrayList<Account>();
                    AccountAdapter accountAdapter = new AccountAdapter(AccountActivity.this, accountArray);

                    @Override
                    public void onResponse(JSONObject response) {

                        JSONArray jsonAccountsArray = null;
                        Double jsonIncome = null;
                        try {
                            jsonAccountsArray = response.getJSONArray("accounts");
                            jsonIncome = response.getDouble("total_income");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        for (int i = 0; i < jsonAccountsArray.length(); i++) {
                            try {
                                Account account = new Account(jsonAccountsArray.getJSONObject(i));
                                accountAdapter.add(account);

                                setAccounts(account);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        incomeAmount = (TextView) findViewById(R.id.income);
                        incomeAmount.setText("$ " + jsonIncome);

                        accountList = (ListView) findViewById(R.id.account_list);
                        accountList.setAdapter(accountAdapter);
                        accountAdapter.notifyDataSetChanged();
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

    public void deleteAccount(String id) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://polar-sierra-78542.herokuapp.com/accounts/" + id;

        StringRequest dr = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent i = new Intent(getBaseContext(), AccountActivity.class);
                        startActivity(i);
                        finish();

                        Toast.makeText(getApplicationContext(), "Cuenta eliminada exitosamente",
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Editar");//groupId, itemId, order, title
        menu.add(0, v.getId(), 0, "Eliminar");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;

        if (item.getTitle() == "Editar") {
            Intent intent = new Intent(getBaseContext(), EditAccountActivity.class);

            intent.putExtra("id", accounts.get(index).getId());
            intent.putExtra("name", accounts.get(index).getName());
            intent.putExtra("acctType", accounts.get(index).getAcctType());
            intent.putExtra("amount", accounts.get(index).getAmount());

            startActivity(intent);
        } else if (item.getTitle() == "Eliminar") {
            String accountId = accounts.get(index).getId();

            deleteAccount(accountId);
        } else {
            return false;
        }
        return true;
    }
}
