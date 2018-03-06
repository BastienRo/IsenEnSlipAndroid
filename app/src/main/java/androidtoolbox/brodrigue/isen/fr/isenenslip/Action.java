package androidtoolbox.brodrigue.isen.fr.isenenslip;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by brodrigue on 06/03/2018.
 */

public class Action {

    @SerializedName("id_action")
    @Expose
    private String id_action;
    @SerializedName("activity")
    @Expose
    private String activity;

    public String getId_action() {
        return id_action;
    }

    public void setId_action(String id_action) {
        this.id_action = id_action;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}

