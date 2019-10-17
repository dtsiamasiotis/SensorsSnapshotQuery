import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class sensorNodeTest {

    @Test
    public void testJunit(){
        SensorNode node1 = new SensorNode();
        CacheMemory cache1 = new CacheMemory(1000);
        node1.setCache(cache1);
        Measurement newMeasurement = new Measurement(0);
        newMeasurement.setNodeNumber(1);
        newMeasurement.setValue(10);
        node1.getMeasurements().put(0,newMeasurement);

        SensorNode node2 = new SensorNode();
        CacheMemory cache2 = new CacheMemory(1000);
        node2.setCache(cache2);
        Measurement newMeasurement2= new Measurement(0);
        newMeasurement2.setNodeNumber(2);
        newMeasurement2.setValue(20);
        node2.getMeasurements().put(0,newMeasurement2);

        MemoryPair memoryPair = new MemoryPair();
        memoryPair.setXi(newMeasurement.getValue());
        memoryPair.setXj(newMeasurement2.getValue());
        memoryPair.setjnode(2);
        node1.addToCache(memoryPair);
        node1.updateModel(2);
        System.out.print("a:"+ node1.aStar.get(2)+","+"b:"+node1.bStar.get(2));


    }

    @Test
    public void calculateAstartest()
    {
        MemoryPair pair1 = new MemoryPair();
        pair1.setXi(10);
        pair1.setXj(30);

        MemoryPair pair2 = new MemoryPair();
        pair2.setXi(20);
        pair2.setXj(40);

        MemoryPair pair3 = new MemoryPair();
        pair3.setXi(30);
        pair3.setXj(50);

        MemoryPair[] Caug = new MemoryPair[3];
        Caug[0] = pair1;
        Caug[1] = pair2;
        Caug[2] = pair3;

        SensorNode node1 = new SensorNode();
        double aStar = node1.calculateaStar(Caug,3);
        assertEquals(1,aStar);

    }

    @Test
    public void calculateBstartest()
    {
        MemoryPair pair1 = new MemoryPair();
        pair1.setXi(270);
        pair1.setXj(70);

        MemoryPair pair2 = new MemoryPair();
        pair2.setXi(270);
        pair2.setXj(70);

      //  MemoryPair pair3 = new MemoryPair();
       // pair3.setXi(30);
      //  pair3.setXj(50);

        MemoryPair[] Caug = new MemoryPair[3];
        Caug[0] = pair1;
        Caug[1] = pair2;
        //Caug[2] = pair3;

        SensorNode node1 = new SensorNode();
        double aStar = node1.calculateaStar(Caug,2);
        double bStar = node1.calculatebStar(Caug,2,aStar);
        assertEquals(-200,bStar);

    }

    @Test
    public void calculateSSEtest()
    {
        MemoryPair pair1 = new MemoryPair();
        pair1.setXi(10);
        pair1.setXj(30);

        MemoryPair pair2 = new MemoryPair();
        pair2.setXi(20);
        pair2.setXj(40);

        MemoryPair pair3 = new MemoryPair();
        pair3.setXi(30);
        pair3.setXj(50);

        MemoryPair[] Caug = new MemoryPair[3];
        Caug[0] = pair1;
        Caug[1] = pair2;
        Caug[2] = pair3;

        SensorNode node1 = new SensorNode();
        double aStar = node1.calculateaStar(Caug,3);
        double bStar = node1.calculatebStar(Caug,3,aStar);
        double sse = node1.calculateSse(Caug,aStar,bStar,3);

        assertEquals(0,0);

    }

    @Test
    public void calculateNoAnswertest() {
        MemoryPair pair1 = new MemoryPair();
        pair1.setXi(10);
        pair1.setXj(30);

        MemoryPair pair2 = new MemoryPair();
        pair2.setXi(20);
        pair2.setXj(40);

        MemoryPair pair3 = new MemoryPair();
        pair3.setXi(30);
        pair3.setXj(50);

        MemoryPair[] Caug = new MemoryPair[3];
        Caug[0] = pair1;
        Caug[1] = pair2;
        Caug[2] = pair3;

        SensorNode node1 = new SensorNode();
        //double aStar = node1.calculateaStar(Caug, 3);
        //double bStar = node1.calculatebStar(Caug, 3, aStar);

        double noAnswer = node1.no_answer_sse(Caug,3);
        assertEquals((double)5000/3, noAnswer);
    }

    @Test
    public void calculateBenefitCaugAshBshtest() {
        MemoryPair pair1 = new MemoryPair();
        pair1.setXi(205);
        pair1.setXj(12);

        MemoryPair pair2 = new MemoryPair();
        pair2.setXi(206);
        pair2.setXj(13);

        MemoryPair pair3 = new MemoryPair();
        pair3.setXi(206);
        pair3.setXj(13);

        MemoryPair[] Caug = new MemoryPair[3];
        Caug[0] = pair1;
        Caug[1] = pair2;
        Caug[2] = pair3;

        MemoryPair[] Csh = new MemoryPair[2];
        Csh[0] = pair2;
        Csh[1] = pair3;

        SensorNode node1 = new SensorNode();
        double aStarSh = node1.calculateaStar(Csh, 2);
        double bStarSh = node1.calculatebStar(Csh, 2, aStarSh);
        double sse = node1.calculateSse(Caug, aStarSh, bStarSh, 3);
        double noAswer = node1.no_answer_sse(Caug,3);
        double benefit = noAswer - sse;
        assertEquals(160.66666666666666, benefit);
    }

    @Test
    public void calculateBenefitCaugAaugBaugtest() {
        MemoryPair pair1 = new MemoryPair();
        pair1.setXi(205);
        pair1.setXj(12);

        MemoryPair pair2 = new MemoryPair();
        pair2.setXi(206);
        pair2.setXj(13);

        MemoryPair pair3 = new MemoryPair();
        pair3.setXi(206);
        pair3.setXj(13);

        MemoryPair[] Caug = new MemoryPair[3];
        Caug[0] = pair1;
        Caug[1] = pair2;
        Caug[2] = pair3;

        MemoryPair[] Csh = new MemoryPair[2];
        Csh[0] = pair2;
        Csh[1] = pair3;

        SensorNode node1 = new SensorNode();
        double aStarAug = node1.calculateaStar(Caug, 2);
        double bStarAug = node1.calculatebStar(Caug, 2, aStarAug);
        double sse = node1.calculateSse(Caug, aStarAug, bStarAug, 3);
        double noAswer = node1.no_answer_sse(Caug,3);
        double benefit = noAswer - sse;
        assertEquals(160.66666666666666, benefit);
    }

    @Test
    public void calculateBenefitCaugABtest() {
        MemoryPair pair1 = new MemoryPair();
        pair1.setXi(205);
        pair1.setXj(12);

        MemoryPair pair2 = new MemoryPair();
        pair2.setXi(206);
        pair2.setXj(13);

        MemoryPair pair3 = new MemoryPair();
        pair3.setXi(206);
        pair3.setXj(13);

        MemoryPair[] Caug = new MemoryPair[3];
        Caug[0] = pair1;
        Caug[1] = pair2;
        Caug[2] = pair3;

        MemoryPair[] Csh = new MemoryPair[2];
        Csh[0] = pair2;
        Csh[1] = pair3;

        MemoryPair[] C = new MemoryPair[2];
        C[0] = pair1;
        C[1] = pair2;

        SensorNode node1 = new SensorNode();
        double aStar = node1.calculateaStar(C, 2);
        double bStar = node1.calculatebStar(C, 2, aStar);
        double sse = node1.calculateSse(Caug, aStar, bStar, 3);
        double noAswer = node1.no_answer_sse(Caug,3);
        double benefit = noAswer - sse;
        assertEquals(160.66666666666666, benefit);
    }
}
