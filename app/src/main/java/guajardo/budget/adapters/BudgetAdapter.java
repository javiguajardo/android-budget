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
import guajardo.budget.models.Category;

/**
 * Created by krierepleaermano on 5/28/17.
 */

public class BudgetAdapter extends ArrayAdapter<Category> {
    private static class ViewHolder {
        TextView categoryCell;
        TextView amountCell;
    }

    public BudgetAdapter(Context context, ArrayList<Category> categories) {
        super(context, R.layout.budget_cell, categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Category category = getItem(position);
        BudgetAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new BudgetAdapter.ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.budget_cell, parent, false);

            viewHolder.categoryCell = (TextView) convertView.findViewById(R.id.category_cell);
            viewHolder.amountCell = (TextView) convertView.findViewById(R.id.amount_cell);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (BudgetAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.categoryCell.setText(category.getName());
        viewHolder.amountCell.setText("$ " + category.getTotalAmount());

        return convertView;
    }

}
