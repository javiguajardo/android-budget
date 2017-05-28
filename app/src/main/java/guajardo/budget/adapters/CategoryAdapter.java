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

/**
 * Created by krierepleaermano on 5/28/17.
 */

public class CategoryAdapter extends ArrayAdapter<Category> {
    private static class ViewHolder {
        TextView nameCell;
        TextView amountCell;
    }

    public CategoryAdapter(Context context, ArrayList<Category> categories) {
        super(context, R.layout.category_cell, categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Category category = getItem(position);
        CategoryAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new CategoryAdapter.ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.category_cell, parent, false);

            viewHolder.nameCell = (TextView) convertView.findViewById(R.id.name_cell);
            viewHolder.amountCell = (TextView) convertView.findViewById(R.id.amount_cell);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CategoryAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.nameCell.setText(category.getName());
        viewHolder.amountCell.setText("$ " + category.getAmount());

        return convertView;
    }

}
