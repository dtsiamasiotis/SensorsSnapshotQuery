import org.junit.jupiter.api.Test;

public class nodesNetworkTest {

    @Test
    public void netWorkTest()
    {
        nodesNetwork network = new nodesNetwork.Builder(5).withDimensions(1,1).build();
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
}
