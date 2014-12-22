package at.ac.tuwien.ifs.sos.strategies;

import at.ac.tuwien.ifs.sos.GameHistory;
import at.ac.tuwien.ifs.sos.ontology.Guilty;
import at.ac.tuwien.ifs.sos.ontology.Prisoner;
import jade.content.lang.Codec;
import jade.content.onto.Ontology;

public class ConstantStrategy extends AbstractStrategyBehaviour {

    private boolean comply;

    public ConstantStrategy(Codec codec, Ontology ontology, GameHistory game, boolean comply) {
        super(codec, ontology, game);
        this.comply = comply;
    }

    public ConstantStrategy(Codec codec, Ontology ontology, GameHistory game, String comply) {
        this(codec, ontology, game, Boolean.parseBoolean(comply));
    }

    @Override
    protected Guilty prepareResultNotification(Guilty areYouGuilty) {
        areYouGuilty.setPrisoner(new Prisoner(myAgent.getAID()));
        areYouGuilty.setConfession(comply);

        return areYouGuilty;
    }

    @Override
    public String toString() {
        return "ConstantStrategy{comply=" + comply + '}';
    }
}
