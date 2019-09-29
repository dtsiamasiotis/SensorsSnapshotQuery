import java.util.Vector;

public class firstExperiment extends Thread {

    private int represSize = 0;
    private int numOfClasses;
    private Vector repres;

    public firstExperiment(int numOfClasses)
    {
        this.numOfClasses = numOfClasses;
        repres = new Vector();
    }

    public int getRepresSize(){
        return represSize;
    }

    @Override
    public void run() {

        nodesNetwork networkOfNodes2=new nodesNetwork();
        networkOfNodes2.createNetwork();
        for (SensorNode komvos:networkOfNodes2.getNodesList())
        {

            komvos.clearVectors();
            komvos.clearCache();
        }

        for (SensorNode komvos:networkOfNodes2.getNodesList())
        {
            komvos.clearVectors();
        }

        networkOfNodes2.setNumberOfClasses(numOfClasses);
        networkOfNodes2.partitionIntoClasses();

        for (SensorNode komvos:networkOfNodes2.getNodesList())
        {
            komvos.setrange(Math.sqrt(200));
            komvos.clearCache();
            komvos.findNeighbors(networkOfNodes2.getNodesList());
        }

        networkOfNodes2.initialize();

        networkOfNodes2.training();

        for (SensorNode komvos:networkOfNodes2.getNodesList())
        {
            komvos.modelBuild();
        }

        for (SensorNode komvos:networkOfNodes2.getNodesList())
        {
            komvos.createEstimates();
        }

        networkOfNodes2.createCandidateLists();

        for (SensorNode komvos:networkOfNodes2.getNodesList())
        {
            komvos.checkCandidateList();
        }


        networkOfNodes2.breakties();
        networkOfNodes2.NoreprenentativeStayActive();
        networkOfNodes2.recallRedundant();
        networkOfNodes2.passiveMode();
        networkOfNodes2.finalcleanup();


        SensorNode num;

        for (SensorNode komvos:networkOfNodes2.getNodesList())
        {
            num=(SensorNode)komvos.getRepresentatives().elementAt(0);
            if(repres.contains(num)==false){
                repres.add(num);
            }
        }

        represSize = repres.size();

    }
}
