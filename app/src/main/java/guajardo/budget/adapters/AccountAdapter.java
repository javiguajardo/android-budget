package guajardo.budget.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import guajardo.budget.R;
import guajardo.budget.models.Account;

/**
 * Created by krierepleaermano on 5/27/17.
 */

public class AccountAdapter extends ArrayAdapter<Account> {
    private static class ViewHolder {
        TextView nameCell;
        TextView amountCell;
    }

    public AccountAdapter(Context context, ArrayList<Account> accounts) {
        super(context, R.layout.account_cell, accounts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Account account = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.account_cell, parent, false);

            viewHolder.nameCell = (TextView) convertView.findViewById(R.id.name_cell);
            viewHolder.amountCell = (TextView) convertView.findViewById(R.id.amount_cell);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.nameCell.setText(account.getName());
        viewHolder.amountCell.setText("$ " + account.getAmount());

        return convertView;
    }
}
