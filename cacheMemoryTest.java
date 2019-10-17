import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class cacheMemoryTest {

    private nodesNetwork network;

    @BeforeEach
    public void initializeNetwork()
    {
        network = new nodesNetwork.Builder(5).withDimensions(1,1).build();
        network.createNetwork();
        network.printInfo();

        for(SensorNode node:network.getNodesList())
        {
            node.initializeNodeWithValue(1000);
            System.out.printf("Node id:%d value:%f\n",node.getNodeNumber(),node.getMeasurements().get(0).getValue());
        }
    }

    @Test
    public void replaceMechanism()
    {
        CacheMemory cache = new CacheMemory(12);

        int Xi = 100;
        int Xj = 110;
        for(int i=0;i<4;i++) {
            for (int k = 0; k < 3; k++) {
                MemoryPair memPair = new MemoryPair();
                //memPair.setinode(i);
                memPair.setjnode(i+1);
                memPair.setXi(Xi + (k * 10));
                memPair.setXj(Xj + (k * 10));
                memPair.setTime(k);
                cache.addPair(memPair);
                //System.out.print("{" + cache.getPair(i, k).getXi() + "," + cache.getPair(i, k).getXj() + "}");
            }

        }



        for(MemoryPair temp:cache.getSpace())
            System.out.print("j:"+temp.getjnode()+","+"time:"+temp.getTime()+","+"{" + temp.getXi() + "," + temp.getXj() + "}|");

        System.out.println();

        SensorNode node = network.getNodesList().get(0);
            node.setrange(1);
            node.findNeighbors(network.getNodesList());
            node.cache = cache;
            MemoryPair memPair = new MemoryPair();
            memPair.setXi(120);
            memPair.setXj(150);
            memPair.setinode(1);
            memPair.setjnode(3);
            memPair.setTime(4);
            node.cacheReplacement(memPair);

            System.out.println();
        for(int i=0;i<4;i++) {
            for (int j = 0; j < 3; j++) {
                //System.out.print("{" + cache.getPair(i, j).getXi() + "," + cache.getPair(i, j).getXj() + "}");
            }
        }
        for(MemoryPair temp:cache.getSpace())
            System.out.print("j:"+temp.getjnode()+","+"time:"+temp.getTime()+","+"{" + temp.getXi() + "," + temp.getXj() + "}|");
            System.out.println();


        MemoryPair memPair2 = new MemoryPair();
        memPair2.setXi(120);
        memPair2.setXj(160);
        memPair2.setinode(1);
        memPair2.setjnode(3);
        memPair2.setTime(4);
        node.cacheReplacement(memPair2);

        //System.out.println();
        for(int i=0;i<4;i++) {
            for (int j = 0; j < 3; j++) {
                //System.out.print("{" + cache.getPair(i, j).getXi() + "," + cache.getPair(i, j).getXj() + "}");
            }
            //System.out.println();
        }

        for(MemoryPair temp:cache.getSpace())
            System.out.print("j:"+temp.getjnode()+","+"time:"+temp.getTime()+","+"{" + temp.getXi() + "," + temp.getXj() + "}|");


        MemoryPair memPair3 = new MemoryPair();
        memPair3.setXi(130);
        memPair3.setXj(140);
        memPair3.setinode(1);
        memPair3.setjnode(2);
        memPair3.setTime(4);
        node.cacheReplacement(memPair3);

        for(MemoryPair temp:cache.getSpace())
            System.out.print("j:"+temp.getjnode()+","+"time:"+temp.getTime()+","+"{" + temp.getXi() + "," + temp.getXj() + "}|");

        node.clearCache();
        System.out.println();
    }
}
