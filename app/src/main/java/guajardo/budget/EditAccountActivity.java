package guajardo.budget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static guajardo.budget.AccountActivity.*;

public class EditAccountActivity extends AppCompatActivity {
    Spinner spinner;
    EditText accountName, accountAmount;
    String name, accountType, newName, newAccountType, newAmount;
    float amount;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        setTitle("Editar Cuenta");

        spinner = (Spinner) findViewById(R.id.account_type_spinner);
        accountName = (EditText) findViewById(R.id.account_name_edit);
        accountAmount = (EditText) findViewById(R.id.account_amount_edit);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.account_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        name = getIntent().getStringExtra("name");
        amount = getIntent().getFloatExtra("amount", 0f);
        accountType = getIntent().getStringExtra("accountType");
        index = getIntent().getIntExtra("index", 0);

        accountName.setText(name);
        accountAmount.setText(String.valueOf(amount));
        spinner.setSelection(adapter.getPosition(accountType));
    }

    public void saveAccount(View view) {
        newName = accountName.getText().toString();
        newAccountType = spinner.getSelectedItem().toString();
        newAmount = accountAmount.getText().toString();
        Intent intent = new Intent(getBaseContext(), AccountActivity.class);

        if (newName.matches("") || newAmount.matches("")) {
            Toast.makeText(this, "Favor de llenar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        //Account.updateAccount(accounts, index, newName, newAccountType, Float.parseFloat(newAmount));
        startActivity(intent);
    }
}
