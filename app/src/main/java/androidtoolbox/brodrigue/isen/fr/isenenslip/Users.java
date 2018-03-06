package androidtoolbox.brodrigue.isen.fr.isenenslip;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by brodrigue on 06/03/2018.
 */

public class Users {

    @SerializedName("name")
    @Expose
    private List<Result> results = null;

    public List<Result> getResults() {
        return results ;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

}