import java.util.LinkedList;
import java.util.List;

public class ModelUtils {
    public static double calculateaStar(List<MemoryPair> NjLine)
    {
        double temp1sum = 0,temp2sum=0,temp3sum=0,temp4sum=0,temp5sum=0;
        double aStar=0;
        boolean changed=false;
        temp1sum=0;temp2sum=0;temp3sum=0;temp4sum=0;
        int i;
        int lineSize=NjLine.size();

        if(lineSize>1)
        {
            for(i=1;i<lineSize;i++)
            {
                if(NjLine.get(i-1).getXi()!=NjLine.get(i).getXi()) {
                    changed = true;
                    break;
                }
            }
        }

        if(changed)
        {
            for(i=0;i<lineSize;i++)
            {

                temp1sum=temp1sum+(NjLine.get(i).getXi()*NjLine.get(i).getXj());
                temp2sum=temp2sum+NjLine.get(i).getXi();
                temp3sum=temp3sum+NjLine.get(i).getXj();
                temp4sum=(temp4sum+Math.pow(NjLine.get(i).getXi(),2));

            }

            aStar=(((lineSize*temp1sum)-(temp2sum*temp3sum))/((lineSize*temp4sum)-Math.pow(temp2sum, 2)));

        }
        else
        {
            aStar = 1;
        }

        //if(aStar==0)
        // System.out.println();
        return aStar;
    }
    public static double calculatebStar(List<MemoryPair> NjLine, double aStar)
    {
        double temp1sum = 0,temp2sum=0,temp3sum=0,temp4sum=0,temp5sum=0;
        double bStar=0;
        boolean changed=false;
        temp1sum=0;temp2sum=0;temp3sum=0;temp4sum=0;
        int i;
        int lineSize=NjLine.size();

        if(lineSize>1)
        {
            for(i=1;i<lineSize;i++)
            {
                if(NjLine.get(i-1).getXi()!=NjLine.get(i).getXi()) {
                    changed = true;
                    break;
                }
            }
        }

        for(i=0;i<lineSize;i++)
        {
            temp3sum=temp3sum+NjLine.get(i).getXj();
        }

        if(changed)
        {
            for(i=0;i<lineSize;i++)
            {
                temp1sum=temp1sum+(NjLine.get(i).getXi()*NjLine.get(i).getXj());
                temp2sum=temp2sum+NjLine.get(i).getXi();

                temp4sum=(temp4sum+Math.pow(NjLine.get(i).getXi(),2));

            }

            bStar=(temp3sum-(aStar*temp2sum))/lineSize;
        }
        else
        {
            //if(NjLine[0]==null)
            // {
            //    System.out.println();
            // }
            bStar=((temp3sum/lineSize)-(aStar*NjLine.get(0).getXi()));
        }
        return bStar;

    }
    public static double calculateSse(List<MemoryPair> c,double a,double b)
    {
        double tempsum=0,sse=0;
        int i;
        int lineSize=c.size();
        for(i=0;i<lineSize;i++)
        {
            tempsum=tempsum+Math.pow(c.get(i).getXj()-((a*c.get(i).getXi())+b),2);
        }

        sse=tempsum/lineSize;
        return sse;
    }
    public static double no_answer_sse(List<MemoryPair> c)
    {
        double tempsum=0,no_answer=0;
        int i;
        int lineSize=c.size();
        for(i=0;i<lineSize;i++)
        {
            tempsum=tempsum+Math.pow(c.get(i).getXj(),2);
        }

        no_answer=tempsum/lineSize;
        return no_answer;
    }
}
