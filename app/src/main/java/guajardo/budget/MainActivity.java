package guajardo.budget;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
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
import guajardo.budget.services.RegistrationService;

import static android.R.attr.category;

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

        Intent i = new Intent(this, RegistrationService.class);
        startService(i);
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


                        for (int i = 0; i < jsonCategoriesArray.length(); i++) {
                            try {
                                Category category = new Category(jsonCategoriesArray.getJSONObject(i));
                                budgetAdapter.add(category);

                                if (Double.parseDouble(category.getTotalAmount()) < 0) {
                                    notificacionesGastos(i, category.getName(), category.getTotalAmount());
                                }


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

    public void notificacionesGastos(int notificationId, String categoryName, String categoryAmount) {
        NotificationCompat.Builder mBuilder = (android.support.v7.app.NotificationCompat.Builder)
                new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.drawable.ic_dialog_alert)
                        .setAutoCancel(true)
                        .setContentTitle("Gastos")
                        .setContentText("Te has excedido en tu presupuesto para " + categoryName + " por " + categoryAmount);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(notificationId, mBuilder.build());
    }
}
