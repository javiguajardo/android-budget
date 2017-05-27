package guajardo.budget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditCategoryActivity extends AppCompatActivity {
    EditText categoryName, categoryAmount;
    TextView budgetAmount;
    String name, newName, newAmount;
    float amount;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        setTitle("Editar Categoria");

        categoryName = (EditText) findViewById(R.id.category_name_edit);
        categoryAmount = (EditText) findViewById(R.id.category_amount_edit);
        budgetAmount = (TextView) findViewById(R.id.budget_amount);
        name = getIntent().getStringExtra("name");
        amount = getIntent().getFloatExtra("amount", 0f);
        index = getIntent().getIntExtra("index", 0);

        categoryName.setText(name);
        categoryAmount.setText(String.valueOf(amount));
       // budgetAmount.setText("$ " + Category.toBudget(categories, accounts));
    }

    public void saveAccount(View view) {
        newName = categoryName.getText().toString();
        newAmount = categoryAmount.getText().toString();
        Intent intent = new Intent(getBaseContext(), CategoryActivity.class);

        if (newName.matches("") || newAmount.matches("")) {
            Toast.makeText(this, "Favor de llenar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

       // Category.updateCategory(categories, index, newName, Float.parseFloat(newAmount));
        startActivity(intent);
    }
}
