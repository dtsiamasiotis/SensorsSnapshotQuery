import java.util.Random;
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

        nodesNetwork network=new nodesNetwork.Builder(100).withDimensions(1,1).withCachePerNode(2048).build();
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

      //  network.initialize(); is it necessary?

        /*network.training();

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

        represSize = repres.size();*/
        int time = 0;
        for(SensorNode temp:network.getNodesList())
            temp.initializeNodeWithValue(1000);

        for(time=1;time<11;time++)
        {
            for(SensorNode temp:network.getNodesList())
            {
                temp.createNewMeasurement(time);
            }
            for(SensorNode temp:network.getNodesList())
            {
                temp.broadcastMeasurement(time);
               // temp.modelBuild();
            }
        }

        float probability;
        Random randomGen1 = new Random();

        for(time=11;time<100;time++)
        {
            probability=(float)(randomGen1.nextInt(11))/10;

            for(SensorNode temp:network.getNodesList()) {
                if(probability<=temp.getPmove())
                    temp.createNewMeasurement(time);
                else
                    temp.preserveMeasurement(time);
            }
        }


        for(SensorNode temp:network.getNodesList())
        {
            Measurement newMeasurement = temp.createNewMeasurement(time);

        }

        for(SensorNode temp:network.getNodesList())
        {
            temp.sendInvitation(temp.getMeasurements().get(100));
        }

        for (SensorNode komvos:network.getNodesList())
        {
            System.out.print(" "+komvos.getNodeNumber()+":"+komvos.getCandidateList().size());
        }

        for (SensorNode komvos:network.getNodesList())
        {
            komvos.informCandidates();
        }

        for (SensorNode komvos:network.getNodesList())
        {
            komvos.checkForNoRepresentative();
        }


        network.breakties();
        network.NoreprenentativeStayActive();
        network.recallRedundant();
        network.passiveMode();
        network.finalcleanup();


        SensorNode num;

        for (SensorNode komvos:network.getNodesList())
        {
            num=(SensorNode)komvos.getRepresentatives().get(0);
            if(repres.contains(num)==false){
                repres.add(num);
            }
        }

        represSize = repres.size();
    }
}
