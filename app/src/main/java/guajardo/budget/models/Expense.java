package guajardo.budget.models;

import org.json.JSONException;
import org.json.JSONObject;

import static guajardo.budget.R.string.amount;

/**
 * Created by krierepleaermano on 5/28/17.
 */

public class Expense {
    private String id;
    private String date;
    private String store;
    private String amount;
    private String categoryId;

    public Expense(JSONObject object) {
        try {
            this.id = object.getString("id");
            this.date = object.getString("expense_date");
            this.store = object.getString("store");
            this.amount = object.getString("amount");
            this.categoryId = object.getString("category_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Expense(String date, String store, String amount, String categoryId) {
        this.date = date;
        this.store = store;
        this.amount = amount;
        this.categoryId = categoryId;
    }

    public String getId() {
        return id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getStore() {
        return store;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }
}
