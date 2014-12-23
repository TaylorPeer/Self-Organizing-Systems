<<<<<<< HEAD
java -cp target/lib/jade-4.3.3.jar;target/classes jade.Boot -gui -agents "prisoner1:at.ac.tuwien.ifs.sos.PrisonAgent(tittat);prisoner2:at.ac.tuwien.ifs.sos.PrisonAgent(random);pm:at.ac.tuwien.ifs.sos.PrisonGameMasterAgent(prisoner1, prisoner2, 10)"
=======
java -cp target/lib/jade-4.3.3.jar;target/classes jade.Boot -gui -agents "p1:at.ac.tuwien.ifs.sos.PrisonerAgent(at.ac.tuwien.ifs.sos.strategies.RandomStrategy);p2:at.ac.tuwien.ifs.sos.PrisonerAgent(at.ac.tuwien.ifs.sos.strategies.RandomStrategy);pm:at.ac.tuwien.ifs.sos.GamemasterAgent(p1, p2, 500)"
>>>>>>> master
