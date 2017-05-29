package guajardo.budget.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import guajardo.budget.R;
import guajardo.budget.models.Category;
import guajardo.budget.models.Expense;

import static android.R.attr.category;

/**
 * Created by krierepleaermano on 5/28/17.
 */

public class ExpenseAdapter extends ArrayAdapter<Expense> {
    private static class ViewHolder {
        TextView nameCell;
        TextView amountCell;
    }

    public ExpenseAdapter(Context context, ArrayList<Expense> expenses) {
        super(context, R.layout.expense_cell, expenses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Expense expense = getItem(position);
        ExpenseAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ExpenseAdapter.ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.expense_cell, parent, false);

            viewHolder.nameCell = (TextView) convertView.findViewById(R.id.name_cell);
            viewHolder.amountCell = (TextView) convertView.findViewById(R.id.amount_cell);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ExpenseAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.nameCell.setText(expense.getStore());
        viewHolder.amountCell.setText("$ " + expense.getAmount());

        return convertView;
    }

}
