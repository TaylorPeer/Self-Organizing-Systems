package at.ac.tuwien.ifs.sos.strategies;

import at.ac.tuwien.ifs.sos.GameHistory;
import at.ac.tuwien.ifs.sos.ontology.Guilty;
import at.ac.tuwien.ifs.sos.ontology.Prisoner;
import jade.content.lang.Codec;
import jade.content.onto.Ontology;

public class RandomStrategy extends AbstractStrategyBehaviour {

    private double chanceForComply;

    public RandomStrategy(Codec codec, Ontology ontology, GameHistory game) { this(codec, ontology, game, 0.2); }

    public RandomStrategy(Codec codec, Ontology ontology, GameHistory game, double chanceForComply) {
        super(codec, ontology, game);
        this.chanceForComply = chanceForComply;
    }

    public RandomStrategy(Codec codec, Ontology ontology, GameHistory game, String chanceForComply) {
        this(codec, ontology, game, Double.parseDouble(chanceForComply));
    }

    @Override
    protected Guilty prepareResultNotification(Guilty areYouGuilty) {
        boolean comply = Math.random() < chanceForComply;

        areYouGuilty.setPrisoner(new Prisoner(myAgent.getAID()));
        areYouGuilty.setConfession(comply);

        return areYouGuilty;
    }

    @Override
    public String toString() {
        return "RandomStrategy{chanceForComply=" + chanceForComply + '}';
    }
}
