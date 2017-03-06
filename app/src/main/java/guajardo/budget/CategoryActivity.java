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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import accounts.Account;
import categories.Category;

import static guajardo.budget.AccountActivity.*;

public class CategoryActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private MenuItem selectedItem;
    static List<Category> categories = new ArrayList<>();
    ListView categoryList;
    TextView budgetAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        setTitle("Categorias");

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        selectedItem = bottomNavigationView.getMenu().getItem(2);
        categoryList = (ListView) findViewById(R.id.category_list);
        budgetAmount = (TextView) findViewById(R.id.budget_amount);
        MyAdapter adapter = new MyAdapter();

        budgetAmount.setText("$ " + Category.toBudget(categories, accounts));
        categoryList.setAdapter(adapter);
        registerForContextMenu(categoryList);
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
    }

    public void addCategory(View view) {
        Intent intent = new Intent(getBaseContext(), AddCategoryActivity.class);
        startActivity(intent);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return categories.size();
        }

        @Override
        public Object getItem(int position) {
            return categories.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.category_cell, parent, false);
            }

            Category currentCategory = (Category) getItem(position);

            TextView nameCell = (TextView) convertView.findViewById(R.id.name_cell);
            TextView amountCell = (TextView) convertView.findViewById(R.id.amount_cell);

            nameCell.setText(currentCategory.getName());
            amountCell.setText("$ " + String.valueOf(currentCategory.getAmount()));

            return convertView;
        }
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
            intent.putExtra("name", categories.get(index).getName());
            intent.putExtra("amount", categories.get(index).getAmount());
            startActivity(intent);
        }
        else if(item.getTitle()=="Eliminar"){
            Intent intent = new Intent(getBaseContext(), CategoryActivity.class);
            Category.removeCategory(categories, index);
            startActivity(intent);
        }else{
            return false;
        }
        return true;
    }
}
