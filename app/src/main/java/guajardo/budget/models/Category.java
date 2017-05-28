package guajardo.budget.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by krierepleaermano on 5/28/17.
 */

public class Category {
    private String id;
    private String name;
    private String amount;
    private String totalAmount;

    public Category(JSONObject object) {
        try {
            this.id = object.getString("id");
            this.name = object.getString("name");
            this.amount = object.getString("amount");
            this.totalAmount = object.getString("total_amount");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Category(String name, String amount, String totalAmount) {
        this.name = name;
        this.amount = amount;
        this.totalAmount = totalAmount;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public String getTotalAmount() {return totalAmount; }

}
