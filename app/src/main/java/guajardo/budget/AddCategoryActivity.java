package guajardo.budget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddCategoryActivity extends AppCompatActivity {
    EditText categoryName, categoryAmount;
    String name, amount;
    TextView budgetAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        setTitle("Agregar Categoria");

        categoryName = (EditText) findViewById(R.id.category_name_edit);
        categoryAmount = (EditText) findViewById(R.id.category_amount_edit);
        budgetAmount = (TextView) findViewById(R.id.budget_amount);

        //budgetAmount.setText("$ " + Category.toBudget(categories, accounts));
    }

    public void saveCategory(View view) {
        name = String.valueOf(categoryName.getText());
        amount = String.valueOf(categoryAmount.getText());
        Intent intent = new Intent(getBaseContext(), CategoryActivity.class);

        if (name.matches("") || amount.matches("")) {
            Toast.makeText(this, "Favor de llenar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

       // Category.addCategory(categories, new Category(name, Float.parseFloat(amount)));
        startActivity(intent);

    }
}
