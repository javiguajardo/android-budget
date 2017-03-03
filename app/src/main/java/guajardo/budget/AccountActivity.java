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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import accounts.Account;

public class AccountActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Intent intent;
    private MenuItem selectedItem;
    static List<Account> accounts = new ArrayList<>();
    ListView accountList;
    TextView incomeAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setTitle("Cuentas");

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        selectedItem = bottomNavigationView.getMenu().getItem(1);
        accountList = (ListView) findViewById(R.id.account_list);
        incomeAmount = (TextView) findViewById(R.id.income_amount);
        MyAdapter adapter = new MyAdapter();

        incomeAmount.setText("$ " + Account.getIncomeSum(accounts));
        accountList.setAdapter(adapter);
        registerForContextMenu(accountList);
        selectedItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_home:
                                intent = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(intent);
                                break;
                        }
                        return true;
                    }
                });
    }

    public void addAccount(View view) {
        Intent intent = new Intent(getBaseContext(), AddAcountActivity.class);
        startActivity(intent);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return accounts.size();
        }

        @Override
        public Object getItem(int position) {
            return accounts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.account_cell, parent, false);
            }

            Account currentAccount = (Account) getItem(position);

            TextView nameCell = (TextView) convertView.findViewById(R.id.name_cell);
            TextView amountCell = (TextView) convertView.findViewById(R.id.amount_cell);

            nameCell.setText(currentAccount.getName());
            amountCell.setText("$ " + String.valueOf(currentAccount.getAmount()));

            return convertView;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
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
            Intent intent = new Intent(getBaseContext(), EditAccountActivity.class);
            intent.putExtra("index", index);
            intent.putExtra("name", accounts.get(index).getName());
            intent.putExtra("accountType", accounts.get(index).getAccountType());
            intent.putExtra("amount", accounts.get(index).getAmount());
            startActivity(intent);
        }
        else if(item.getTitle()=="Eliminar"){
            Intent intent = new Intent(getBaseContext(), AccountActivity.class);
            Account.removeAccount(accounts, index);
            startActivity(intent);
        }else{
            return false;
        }
        return true;
    }
}
