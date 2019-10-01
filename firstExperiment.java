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

        nodesNetwork network=new nodesNetwork.Builder(100).withDimensions(1,1).build();
        network.createNetwork();
        for (SensorNode komvos:network.getNodesList())
        {

            komvos.clearVectors();
            komvos.clearCache();
        }

        for (SensorNode komvos:network.getNodesList())
        {
            komvos.clearVectors();
        }

        network.setNumberOfClasses(numOfClasses);
        network.partitionIntoClasses();

        for (SensorNode komvos:network.getNodesList())
        {
            komvos.setrange(Math.sqrt(200));
            komvos.clearCache();
            komvos.findNeighbors(network.getNodesList());
        }

        network.initialize();

        network.training();

        for (SensorNode komvos:network.getNodesList())
        {
            komvos.modelBuild();
        }

        for (SensorNode komvos:network.getNodesList())
        {
            komvos.createEstimates();
        }

        network.createCandidateLists();

        for (SensorNode komvos:network.getNodesList())
        {
            komvos.checkCandidateList();
        }


        network.breakties();
        network.NoreprenentativeStayActive();
        network.recallRedundant();
        network.passiveMode();
        network.finalcleanup();


        SensorNode num;

        for (SensorNode komvos:network.getNodesList())
        {
            num=(SensorNode)komvos.getRepresentatives().elementAt(0);
            if(repres.contains(num)==false){
                repres.add(num);
            }
        }

        represSize = repres.size();

    }
}
