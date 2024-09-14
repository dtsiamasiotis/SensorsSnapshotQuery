import java.util.HashMap;
import java.util.List;

public class Model {
    HashMap<Integer,Float> aStar = new HashMap<Integer,Float>();
    HashMap<Integer,Float> bStar = new HashMap<Integer,Float>();

    public void updateModel(int Nj, CacheMemory cache)
    {
        float temp1sum = 0,temp2sum=0,temp3sum=0,temp4sum=0;
        int i,j;
        int n=0;

        boolean changed=false;
        float aStarValue=0,bStarValue=0;



        List<MemoryPair> cacheLine = cache.getSpace().get(Nj);
        n = cacheLine.size();
        temp1sum=0;temp2sum=0;temp3sum=0;temp4sum=0;

        for(i=1;i<n;i++)
        {
            if(cacheLine.get(i-1).getXi()!=cacheLine.get(i).getXi()) {
                changed = true;
                break;
            }
        }

        if((!changed && n>0) || n==1)
        {
            aStarValue = 1;
            for(i=0;i<n;i++)
            {
                temp3sum=temp3sum+cacheLine.get(i).getXj();
            }
            //bStarValue = temp3sum/n;

            bStarValue=((temp3sum/n)-(aStarValue*cacheLine.get(0).getXi()));
        }
        else {
            for (i = 0; i < n; i++) {
                temp1sum = temp1sum + (cacheLine.get(i).getXi() * cacheLine.get(i).getXj());
                temp2sum = temp2sum + cacheLine.get(i).getXi();
                temp3sum = temp3sum + cacheLine.get(i).getXj();
                temp4sum = (temp4sum + (float) Math.pow(cacheLine.get(i).getXi(), 2));
            }

            aStarValue = (((n * temp1sum) - (temp2sum * temp3sum)) / ((n * temp4sum) - (float) Math.pow(temp2sum, 2)));
            bStarValue = (temp3sum - (aStarValue * temp2sum)) / n;
        }
        //if(Float.isNaN(aStarValue)||Float.isNaN(bStarValue))
        // System.out.println();
        aStar.put(Nj,aStarValue);
        bStar.put(Nj,bStarValue);

    }
}
