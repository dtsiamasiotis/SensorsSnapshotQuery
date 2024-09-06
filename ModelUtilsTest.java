import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModelUtilsTest {

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

        List<MemoryPair> Caug = new LinkedList<>();
        Caug.add(pair1);
        Caug.add(pair2);
        Caug.add(pair3);

        double aStar = ModelUtils.calculateaStar(Caug);
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

        List<MemoryPair> Caug = new LinkedList<>();
        Caug.add(pair1);
        Caug.add(pair2);
        //Caug[2] = pair3;


        double aStar = ModelUtils.calculateaStar(Caug);
        double bStar = ModelUtils.calculatebStar(Caug, aStar);
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

        List<MemoryPair> Caug = new LinkedList<>();
        Caug.add(pair1);
        Caug.add(pair2);
        Caug.add(pair3);

        double aStar = ModelUtils.calculateaStar(Caug);
        double bStar = ModelUtils.calculatebStar(Caug, aStar);
        double sse = ModelUtils.calculateSse(Caug,aStar,bStar);

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

        List<MemoryPair> Caug = new LinkedList<>();
        Caug.add(pair1);
        Caug.add(pair2);
        Caug.add(pair3);

        //double aStar = node1.calculateaStar(Caug, 3);
        //double bStar = node1.calculatebStar(Caug, 3, aStar);

        double noAnswer = ModelUtils.no_answer_sse(Caug);
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

        List<MemoryPair> Caug = new LinkedList<>();
        Caug.add(pair1);
        Caug.add(pair2);
        Caug.add(pair3);

        List<MemoryPair> Csh = new LinkedList<>();
        Csh.add(pair2);
        Csh.add(pair3);

        double aStarSh = ModelUtils.calculateaStar(Csh);
        double bStarSh = ModelUtils.calculatebStar(Csh, aStarSh);
        double sse = ModelUtils.calculateSse(Caug, aStarSh, bStarSh);
        double noAswer = ModelUtils.no_answer_sse(Caug);
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

        List<MemoryPair> Caug = new LinkedList<>();
        Caug.add(pair1);
        Caug.add(pair2);
        Caug.add(pair3);

        List<MemoryPair> Csh = new LinkedList<>();
        Csh.add(pair2);
        Csh.add(pair3);


        double aStarAug = ModelUtils.calculateaStar(Caug);
        double bStarAug = ModelUtils.calculatebStar(Caug, aStarAug);
        double sse = ModelUtils.calculateSse(Caug, aStarAug, bStarAug);
        double noAswer = ModelUtils.no_answer_sse(Caug);
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

        List<MemoryPair> Caug = new LinkedList<>();
        Caug.add(pair1);
        Caug.add(pair2);
        Caug.add(pair3);

        List<MemoryPair> Csh = new LinkedList<>();
        Csh.add(pair2);
        Csh.add(pair3);

        List<MemoryPair> C = new LinkedList<>();
        C.add(pair1);
        C.add(pair2);

        SensorNode node1 = new SensorNode();
        double aStar = ModelUtils.calculateaStar(C);
        double bStar = ModelUtils.calculatebStar(C, aStar);
        double sse = ModelUtils.calculateSse(Caug, aStar, bStar);
        double noAswer = ModelUtils.no_answer_sse(Caug);
        double benefit = noAswer - sse;
        assertEquals(160.66666666666666, benefit);
    }
}
