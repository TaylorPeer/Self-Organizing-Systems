package at.ac.tuwien.ifs.sos.strategies;

import at.ac.tuwien.ifs.sos.GameHistory;
import at.ac.tuwien.ifs.sos.ontology.Guilty;
import at.ac.tuwien.ifs.sos.ontology.Prisoner;
import at.ac.tuwien.ifs.sos.ontology.Round;
import jade.content.lang.Codec;
import jade.content.onto.Ontology;

public class TitForTatStrategy extends AbstractStrategyBehaviour {
    public TitForTatStrategy(Codec codec, Ontology ontology, GameHistory game) {
        super(codec, ontology, game);
    }

    @Override
    protected Guilty prepareResultNotification(Guilty areYouGuilty) {
        boolean comply = true;

        Round previousRound = game.getPreviousRound();

        if (previousRound != null) {
            if (game.getPrisoner1().getAgent().equals(myAgent.getAID()))
                comply = previousRound.getConfession2();
            else
                comply = previousRound.getConfession1();
        }

        areYouGuilty.setPrisoner(new Prisoner(myAgent.getAID()));
        areYouGuilty.setConfession(comply);

        return areYouGuilty;
    }

    @Override
    public String toString() {
        return "TitForTatStrategy";
    }
}
