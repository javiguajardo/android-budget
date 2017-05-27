package guajardo.budget.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by krierepleaermano on 5/27/17.
 */

public class Account {
    private String id;
    private String name;
    private String acctType;
    private String amount;

    public Account(JSONObject object) {
        try {
            this.id = object.getString("id");
            this.name = object.getString("name");
            this.acctType = object.getString("acct_type");
            this.amount = object.getString("amount");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Account(String name, String acctType, String amount) {
        this.name = name;
        this.acctType = acctType;
        this.amount = amount;
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

    public void setAcctType(String acctType) {
        this.acctType = acctType;
    }

    public String getAcctType() {
        return acctType;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }


}
