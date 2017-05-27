package guajardo.budget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import static guajardo.budget.AccountActivity.*;

public class AddAcountActivity extends AppCompatActivity {
    Spinner spinner;
    EditText accountName, accountAmount;
    String name, amount, accountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_acount);
        setTitle("Agregar Cuenta");

        spinner = (Spinner) findViewById(R.id.account_type_spinner);
        accountName = (EditText) findViewById(R.id.account_name_edit);
        accountAmount = (EditText) findViewById(R.id.account_amount_edit);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.account_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void saveAccount(View view) {
        name = String.valueOf(accountName.getText());
        amount = String.valueOf(accountAmount.getText());
        accountType = spinner.getSelectedItem().toString();
        Intent intent = new Intent(getBaseContext(), AccountActivity.class);

        if (name.matches("") || amount.matches("")) {
            Toast.makeText(this, "Favor de llenar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        //Account.addAccount(accounts, new Account(name, accountType, Float.parseFloat(amount)));
        startActivity(intent);
    }
}
