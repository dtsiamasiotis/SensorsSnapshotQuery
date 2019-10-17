import java.util.Random;
import java.util.Vector;

public class secondExperiment extends Thread {

    private int represSize = 0;
    private int numOfClasses;
    private Vector repres;
    private double range;

    public secondExperiment(int numOfClasses, double range)
    {
        this.numOfClasses = numOfClasses;
        this.range = range;
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
            komvos.setrange(range);
            komvos.clearCache();
            komvos.findNeighbors(network.getNodesList());
        }

      //  network.initialize(); is it necessary?

        int time = 0;
        for(SensorNode temp:network.getNodesList())
            temp.initializeNodeWithValue(1000);

        float probability;
        Random randomGen1 = new Random();

        for(time=1;time<11;time++)
        {
            probability=(float)(randomGen1.nextInt(11))/10;
            for(SensorNode temp:network.getNodesList())
            {
                if(probability<=temp.getPmove())
                    temp.createNewMeasurement(time);
                else
                    temp.preserveMeasurement(time);
            }
            for(SensorNode temp:network.getNodesList())
            {
                temp.broadcastMeasurement(time);
               // temp.modelBuild();
            }
        }



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
            komvos.informCandidates();
        }

        for (SensorNode komvos:network.getNodesList())
        {
   //         komvos.checkForNoRepresentative();
        }

        int max_wait = 2;

            while (undefinedExists(network)) {
                if(max_wait==0)
                    break;

                network.breakties();
                network.NoreprenentativeStayActive();
                network.recallRedundant();
                network.passiveMode();
                max_wait--;
            }



        network.finalcleanup();

        SensorNode num;

        for (SensorNode komvos:network.getNodesList())
        {
            if(komvos.getStatus().equals("active")) {
                represSize++;

            }
        }

        //represSize = repres.size();
    }

    public boolean undefinedExists(nodesNetwork network)
    {
        for(SensorNode temp:network.getNodesList())
            if(temp.getStatus().equals("undefined"))
                return true;

        return false;
    }
}
