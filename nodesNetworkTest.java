import org.junit.jupiter.api.Test;

public class nodesNetworkTest {

    @Test
    public void netWorkTest()
    {
        nodesNetwork network = new nodesNetwork.Builder(8).withDimensions(1,1).build();
        network.createNetwork();
        network.printInfo();
    }

    @Test
    public void initializeNetwork()
    {
        nodesNetwork network = new nodesNetwork.Builder(5).withDimensions(1,1).build();
        network.createNetwork();
        network.printInfo();

        for(SensorNode node:network.getNodesList())
        {
            node.initializeNodeWithValue(1000);
            System.out.printf("Node id:%d value:%f\n",node.getNodeNumber(),node.getMeasurements().get(0).getValue());
        }


    }

    @Test
    public void emulateNetworkForSomeTime()
    {
        nodesNetwork network = new nodesNetwork.Builder(5).withDimensions(1,1).build();
        network.createNetwork();

        for(SensorNode node:network.getNodesList())
        {
            node.initializeNodeWithValue(1000);
        }

        for(int i=1;i<10;i++)
        {
            for(SensorNode node:network.getNodesList()) {
                node.createNewMeasurement(i);
                node.broadcastMeasurement(i);
            }
        }
    }

    @Test
    public void representationAlgorithm()
    {
        nodesNetwork network = new nodesNetwork.Builder(8).withDimensions(1,1).build();
        network.createNetwork();
        network.printInfo();

        SensorNode node1 = network.getNodesList().get(0);
        node1.addToCandidateList(network.getNodesList().get(1));
        SensorNode node3 = network.getNodesList().get(2);
        node3.addToCandidateList(network.getNodesList().get(3));
        node3.addToCandidateList(network.getNodesList().get(5));
        SensorNode node4 = network.getNodesList().get(3);
        node4.addToCandidateList(network.getNodesList().get(0));
        node4.addToCandidateList(network.getNodesList().get(1));
        node4.addToCandidateList(network.getNodesList().get(2));
        node4.addToCandidateList(network.getNodesList().get(4));
        SensorNode node5 = network.getNodesList().get(4);
        node5.addToCandidateList(network.getNodesList().get(7));
        SensorNode node6 = network.getNodesList().get(5);
        node6.addToCandidateList(network.getNodesList().get(6));
        SensorNode node7 = network.getNodesList().get(6);
        node7.addToCandidateList(network.getNodesList().get(7));

        for(SensorNode temp:network.getNodesList())
            temp.informCandidates();

        for(SensorNode temp:network.getNodesList()) {
            temp.findNeighbors(network.getNodesList());
        }

       // for(SensorNode temp:network.getNodesList()) {
            //findNeighbors(network.getNodesList());
        while(undefinedExists(network)) {
            network.breakties();
            network.NoreprenentativeStayActive();
            network.recallRedundant();
            network.passiveMode();

        }

        //}
       // network.finalcleanup();
        for(SensorNode temp:network.getNodesList())
            ;
    }

    public boolean undefinedExists(nodesNetwork network)
    {
        for(SensorNode temp:network.getNodesList())
            if(temp.getStatus().equals("undefined"))
                return true;

        return false;
    }
}
