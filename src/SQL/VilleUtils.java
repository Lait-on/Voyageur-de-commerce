package SQL;

import java.util.ArrayList;
import java.util.List;

public class VilleUtils {

    /**
     * Filtre les villes pour s'assurer qu'aucune ville n'a le même nom et le même département.
     * 
     * @param villes Le tableau de villes à filtrer.
     * @return Un tableau de villes sans doublons de nom et département.
     */
    public static Ville[] filtrerVillesUnique(Ville[] villes) {

        List<Ville> villesFiltrees = new ArrayList<>();


        for (Ville ville : villes) {
            boolean doublonTrouve = false;


            for (Ville villeExistante : villesFiltrees) {
                if (ville.getName().equals(villeExistante.getName()) && 
                    ville.getDepartement().equals(villeExistante.getDepartement())) {
                    doublonTrouve = true; 
                    break;
                }
            }

            if (!doublonTrouve) {
                villesFiltrees.add(ville);
            }
        }

        return villesFiltrees.toArray(new Ville[0]);
    }

}
