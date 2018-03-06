package androidtoolbox.brodrigue.isen.fr.isenenslip;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by brodrigue on 06/03/2018.
 */


public class Result {

    @SerializedName("name")
    @Expose
    private Personne personne;

    public Personne getName() {
        return personne;
    }

    public void setName(Personne personne) {
        this.personne = personne;
    }

}
