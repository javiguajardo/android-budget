package guajardo.budget;

import android.app.Activity;
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

import guajardo.budget.adapters.CategoryAdapter;
import guajardo.budget.models.Category;


public class CategoryActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private MenuItem selectedItem;
    private ArrayList<String> categoryIds = new ArrayList<String>();
    ListView categoryList;
    TextView budgetAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        setTitle("Categorias");

        categoryList = (ListView) findViewById(R.id.category_list);
        registerForContextMenu(categoryList);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        selectedItem = bottomNavigationView.getMenu().getItem(2);
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
                            case R.id.menu_account:
                                Intent intentAccount = new Intent(getBaseContext(), AccountActivity.class);
                                startActivity(intentAccount);
                                break;
                        }
                        return true;
                    }
                });

        loadCategories();
    }

    public void addCategory(View view) {
        Intent intent = new Intent(getBaseContext(), AddCategoryActivity.class);
        startActivity(intent);
    }

    public void setCategoryIds(String ids) {
        categoryIds.add(ids);
    }

    public ArrayList<String> getCategoryIds() {
        return categoryIds;
    }

    public void loadCategories() {
        String url = "https://polar-sierra-78542.herokuapp.com/categories";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    ArrayList<Category> categoryArray = new ArrayList<Category>();
                    CategoryAdapter categoryAdapter = new CategoryAdapter(CategoryActivity.this, categoryArray);

                    @Override
                    public void onResponse(JSONObject response) {

                        JSONArray jsonCategoriesArray = null;
                        Double jsonLeftToBudget = null;
                        try {
                            jsonCategoriesArray = response.getJSONArray("categories");
                            jsonLeftToBudget = response.getDouble("left_to_budget");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        for(int i = 0; i < jsonCategoriesArray.length(); i++) {
                            try {
                                Category category = new Category(jsonCategoriesArray.getJSONObject(i));
                                categoryAdapter.add(category);
                                setCategoryIds(category.getId());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        budgetAmount = (TextView) findViewById(R.id.budget_amount);
                        budgetAmount.setText("$ " + jsonLeftToBudget);

                        categoryList = (ListView) findViewById(R.id.category_list);
                        categoryList.setAdapter(categoryAdapter);
                        categoryAdapter.notifyDataSetChanged();
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

    public void deleteCategory(String id) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://polar-sierra-78542.herokuapp.com/categories/" + id;

        StringRequest dr = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent i = new Intent(getBaseContext(), CategoryActivity.class);
                        startActivity(i);
                        finish();

                        Toast.makeText(getApplicationContext(), "Categoria eliminada exitosamente",
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Editar");//groupId, itemId, order, title
        menu.add(0, v.getId(), 0, "Eliminar");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;

        if(item.getTitle()=="Editar"){
            Intent intent = new Intent(getBaseContext(), EditCategoryActivity.class);
            intent.putExtra("index", index);
            //intent.putExtra("name", categories.get(index).getName());
            //intent.putExtra("amount", categories.get(index).getAmount());
            startActivity(intent);
        }
        else if(item.getTitle()=="Eliminar"){
            String categoryId = getCategoryIds().get(index);

            deleteCategory(categoryId);
        }else{
            return false;
        }
        return true;
    }
}
