import java.util.*;
import java.lang.*;
import java.math.*;

public class SensorNode {
	private int NodeNumber;
	private double range;
	private float xPosition;
	private float yPosition;
	private int numberOfClass;
	private float Pmove;
	CacheMemory cache;
	private LinkedList<SensorNode> representatives = new LinkedList<SensorNode>();
	private LinkedList<SensorNode> neighbors;
	private Vector isRepresenting;
	private LinkedList<SensorNode> candidateList=new LinkedList<SensorNode>();
	private Vector receivedMeasurements;
	HashMap<Integer,Float> aStar = new HashMap<Integer,Float>();
	HashMap<Integer,Float> bStar = new HashMap<Integer,Float>();
	private double[][] estimatedMeasurements;
	private String status="undefined";
	private int test=0;
	private HashMap<Integer,Measurement> measurements = new HashMap<Integer, Measurement>();
	private int NjRoundRobin = 0;
	private float step;

	public void setStep(float step)
    {
        this.step = step;
    }

    public float getStep()
    {
        return this.step;
    }

	public void setNodeNumber(int value){
		this.NodeNumber=value;
	}
	
	
	public int getNodeNumber(){
		return this.NodeNumber;
	}
	
	public void setPmove(float pr){
		Pmove=pr;
	}
	
	public float getPmove(){
		return this.Pmove;
	}
	
	public void setrange(double rang)
	{
		this.range=rang;
	}
	
	public double getrange()
	{
		return this.range;
	}
	public void setStatus(String state){
		status=state;
	}
	
	public String getStatus(){
		return this.status;
	}
	
	public void setX(float number1){
		this.xPosition=number1;
	}
	
	public float getX(){
		return this.xPosition;
	}
	
	public void setY(float number2){
		this.yPosition=number2;
	}
	
	public double getY(){
		return this.yPosition;
	}
	
	public void setNumberOfClass(int number)
	{
		this.numberOfClass=number;
	}
	
	public int getNumberOfClass()
	{
		return this.numberOfClass;
	}
	
	public LinkedList<SensorNode> getNeighbors(){
		return this.neighbors;
	}
	
	public double getEstimation(int place,int time)
	{
		return this.estimatedMeasurements[place][time];
	}
	
	public void setRepresentatives(LinkedList<SensorNode> initialnodes){
		this.representatives=new LinkedList<SensorNode>();
		this.representatives=initialnodes;
		
	}
	public LinkedList<SensorNode> getRepresentatives()
	{
		return this.representatives;
	}
	
	public LinkedList<SensorNode> getCandidateList()
	{
		return this.candidateList;
	}

	public void addToCandidateList(SensorNode anode)
	{
		this.candidateList.add(anode);
	}
	
	
	
	public void setMyself()
	{
		this.representatives.add(this);
	}
	
	public void setReceivedMeasurements(int degrees){
		this.receivedMeasurements.add(degrees);
	}

	public void setCache(CacheMemory cache){ this.cache = cache;}
	
	public void addToCache(MemoryPair pair)
	{
		if(!this.cache.isCacheFull())
			this.cache.addPair(pair);
		else
			this.cacheReplacement(pair);
	}
	
	public void clearCache()
	{
		this.cache.clearCache();
	}

	public HashMap<Integer,Measurement> getMeasurements(){return this.measurements;}
	
	
	//For each node we find his neighbors. To do that we calculate the Eucleidian distance
	//of every neighbor.If it's smaller than node's range(root of 2 in our experiment) then 
	//we add this neighbor to the neighbors's list.
	public void findNeighbors(ArrayList<SensorNode> nodes){
		neighbors=new LinkedList<SensorNode>();
		double distance;

		for(SensorNode temp:nodes)
		{
			if(temp.getNodeNumber() == this.getNodeNumber())
				continue;

			distance=Math.sqrt(Math.pow((this.getX()-temp.getX()),2)+Math.pow((this.getY()-temp.getY()), 2));
			if(distance<range)
			{
				this.neighbors.add(temp);

			}
			else
			{
				;
			}
		}
		
	}
	
	//We buid a model for every node so we are able to estimate values of his neighbors.
	public void modelBuild(){

		float temp1sum = 0,temp2sum=0,temp3sum=0,temp4sum=0;
		int i,k,Nj,j;
		int n=0;
		MemoryPair[] cacheLine=new MemoryPair[1000];
		boolean changed=false;
		
		for(k=0;k<this.neighbors.size();k++)
		{
			Nj=(int)((SensorNode)(this.neighbors.get(k))).NodeNumber;
			
			n=0;

			for(MemoryPair temp:this.cache.getSpace())
				if(temp.getjnode()==Nj)
				{
					cacheLine[n]=temp;
					n++;
				}
			
			
			temp1sum=0;temp2sum=0;temp3sum=0;temp4sum=0;
			
			for(i=1;i<n;i++)
			{
				if(cacheLine[i-1]!=cacheLine[i])
					changed=true;
			}
			
			
		for(i=0;i<n;i++)
		{
			temp1sum=temp1sum+(cacheLine[i].getXi()*cacheLine[i].getXj());
			temp2sum=temp2sum+cacheLine[i].getXi();
			temp3sum=temp3sum+cacheLine[i].getXj();
			temp4sum=(temp4sum+(float)Math.pow(cacheLine[i].getXi(),2));
			
		}
		
		
		float aStarValue = (((n*temp1sum)-(temp2sum*temp3sum))/((n*temp4sum)-(float)Math.pow(temp2sum, 2)));
		float bStarValue =( temp3sum-(aStarValue*temp2sum))/n;
		
		if(changed==false || n==1)
		{
			aStarValue = 1;
            bStarValue=((temp3sum/n)-(aStarValue*cacheLine[0].getXi()));

		}

			aStar.put(k,aStarValue);
			bStar.put(k,bStarValue);
		}
		}
	


	public float createEstimate(Measurement Xj)
	{
		float estimate = 0;
		int time = Xj.getTime();
		int Nj = Xj.getNodeNumber();
		this.updateModel(Nj);
		float Xi = measurements.get(time).getValue();
		estimate = (aStar.get(Nj)*Xi) + bStar.get(Nj);

		return estimate;
	}


	public void compareEstimate(Measurement Xj, float estimate, float threshold)
	{
		float realValue = Xj.getValue();
		float dXjXjest = (float)Math.pow(realValue-estimate,2);
		if(dXjXjest < threshold)
		{
			for(SensorNode temp:this.neighbors)
			{
				if(temp.getNodeNumber() == Xj.getNodeNumber())
				{
					addToCandidateList(temp);
					break;
				}
			}

		}
	}

	//When we have finished finding the candidate list for each node,we choose one  node
	//from this list to be the representative. To do that, we find for each candidate node
	//the number of nodes that have him as candidate. The node with the biggest number or the
	//node with the greatest nodeNumber(e.g. mac address) if two or more have equal numbers,is 
	//selected as representative. If no representative is found, the node selects as representative
	//itself.
	public void checkCandidateList()
	{
		int i,j,max_value = 0;
		
		int[][] offer=new int[this.neighbors.size()][2];
		for(i=0;i<offer.length;i++)
		{
			
				offer[i][0]=-1;
		}
		SensorNode tempnode=new SensorNode();
		SensorNode tempnode2=new SensorNode();
		
	//	this.representatives.clear();
		
		for(i=0;i<this.neighbors.size();i++)
		{
			tempnode=(SensorNode)(this.neighbors.get(i));
			for(j=0;j<tempnode.candidateList.size();j++)
			{
				tempnode2=(SensorNode)(tempnode.candidateList.get(j));
				if(tempnode2.getNodeNumber()==this.getNodeNumber())
				{
					offer[i][0]=tempnode.getNodeNumber();
					offer[i][1]=tempnode.candidateList.size();
				}
				}
		}
		j=0;
		int k=0;
		if(offer.length!=0 && offer[0][0]!=-1)
		{
			max_value=offer[0][1];
			k=offer[0][0];
		}
		j++;
		

		
		while(j<offer.length)
		{
			if(offer[j][1]>=max_value)
			{
				max_value=offer[j][1];
				k=offer[j][0];
			}
			j++;
		}
		
		for(i=0;i<this.neighbors.size();i++)
		{
			tempnode=(SensorNode)(this.neighbors.get(i));
			if(tempnode.getNodeNumber()==k)
			{
				
				this.representatives.add(tempnode);
			}
		}
		
		for(i=0;i<offer.length && offer[i][0]!=-1;i++)
		{
			for(j=0;j<this.neighbors.size();j++){
			tempnode=(SensorNode)(this.neighbors.get(j));
			if(tempnode.getNodeNumber()==offer[i][0] && tempnode.getNodeNumber()!=k)
			tempnode.candidateList.remove(this);
			}
			}
		if(this.representatives.isEmpty()==true)
			this.representatives.add(this);
	}

	public void informCandidates()
	{
		for(SensorNode temp:candidateList)
			temp.receiveInfoFromWannabeRepresentative(this);
	}

	public void receiveInfoFromWannabeRepresentative(SensorNode wannabeRepres)
	{
		if(this.representatives.isEmpty())
			representatives.add(wannabeRepres);
		else {
            if (representatives.get(0).getCandidateList().size() < wannabeRepres.getCandidateList().size())
                representatives.set(0, wannabeRepres);
            if (representatives.get(0).getCandidateList().size() == wannabeRepres.getCandidateList().size()) {
                if (representatives.get(0).getNodeNumber() < wannabeRepres.getNodeNumber())
                    representatives.set(0, wannabeRepres);
            }
        }
	}

	public void checkForNoRepresentative()
	{
		if(this.getRepresentatives().isEmpty())
			representatives.add(this);
	}

	public void clearVectors()
	{
		this.candidateList.clear();
		//this.neighbors.clear();
		if(this.representatives!=null)
		this.representatives.clear();
	}


	public double calculateaStar(MemoryPair[] NjLine,int amount) 
	{
		double temp1sum = 0,temp2sum=0,temp3sum=0,temp4sum=0,temp5sum=0;
		double aStar=0;
		boolean changed=false;
		temp1sum=0;temp2sum=0;temp3sum=0;temp4sum=0;
		int i;


		if(amount>1)
		{
			for(i=1;i<amount;i++)
			{
			if(NjLine[i-1].getXi()!=NjLine[i].getXi())
				changed=true;
			}
		}
		
		if(changed==true)
		{
			for(i=0;i<amount;i++)
			{
			
			temp1sum=temp1sum+(NjLine[i].getXi()*NjLine[i].getXj());
			temp2sum=temp2sum+NjLine[i].getXi();
			temp3sum=temp3sum+NjLine[i].getXj();
			temp4sum=(temp4sum+Math.pow(NjLine[i].getXi(),2));
			
			}
		
			aStar=(((amount*temp1sum)-(temp2sum*temp3sum))/((amount*temp4sum)-Math.pow(temp2sum, 2)));
			
		}
		else
        {
            aStar = 1;
        }
		
		//if(aStar==0)
		   // System.out.println();
		return aStar;
	}
	
	public double calculatebStar(MemoryPair[] NjLine,int amount,double aStar) 
	{
		double temp1sum = 0,temp2sum=0,temp3sum=0,temp4sum=0,temp5sum=0;
		double bStar=0;
		boolean changed=false;
		temp1sum=0;temp2sum=0;temp3sum=0;temp4sum=0;
		int i;
		
		if(amount>1)
		{
			for(i=1;i<amount;i++)
			{
			if(NjLine[i-1].getXi()!=NjLine[i].getXi())
				changed=true;
			}
		}
		
		for(i=0;i<amount;i++)
		{
			temp3sum=temp3sum+NjLine[i].getXj();
		}
		
		if(changed==true)
		{
		for(i=0;i<amount;i++)
		{
			temp1sum=temp1sum+(NjLine[i].getXi()*NjLine[i].getXj());
			temp2sum=temp2sum+NjLine[i].getXi();
			
			temp4sum=(temp4sum+Math.pow(NjLine[i].getXi(),2));
			
		}
		
		bStar=(temp3sum-(aStar*temp2sum))/amount;
		}
		else
		{
		    //if(NjLine[0]==null)
           // {
            //    System.out.println();
           // }
			bStar=((temp3sum/amount)-(aStar*NjLine[0].getXi()));
		}
		return bStar;
		
	}
	
	public double calculateSse(MemoryPair[] c,double a,double b,int amount)
	{
		double tempsum=0,sse=0;
		int i;
		for(i=0;i<amount;i++)
		{
		tempsum=tempsum+Math.pow(c[i].getXj()-((a*c[i].getXi())+b),2);
		}
		
		sse=tempsum/amount;
		return sse;
	}
	
	public double no_answer_sse(MemoryPair[] c,int amount)
	{
		double tempsum=0,no_answer=0;
		int i;
		for(i=0;i<amount;i++)
		{
			tempsum=tempsum+Math.pow(c[i].getXj(),2);
		}
		
		no_answer=tempsum/amount;
		return no_answer;
	}

	public Measurement createNewMeasurement(int curTime)
	{
		Measurement newMeasurement = new Measurement(curTime);
		Random randomGen = new Random();
		int plusminus = 0;//randomGen.nextInt(2);
		float curValue = this.getStep();//randomGen.nextFloat();
		float previousValue = measurements.get(curTime-1).getValue();
		if(plusminus == 0)
			newMeasurement.setValue(previousValue + curValue);
		else if(plusminus == 1)
			newMeasurement.setValue(previousValue - curValue);

		newMeasurement.setTime(curTime);
		newMeasurement.setNodeNumber(this.getNodeNumber());
		measurements.put(curTime,newMeasurement);
		return newMeasurement;
	}

	public void preserveMeasurement(int time)
	{
		Measurement previousMeasurement = measurements.get(time-1);
		Measurement newMeasurement = new Measurement(time);
		newMeasurement.setNodeNumber(previousMeasurement.getNodeNumber());
		newMeasurement.setValue(previousMeasurement.getValue());
		measurements.put(time,newMeasurement);
	}

	public void initializeNodeWithValue(int upperBound)
	{
		Measurement newMeasurement = new Measurement(0);
		Random randomGen = new Random();
		float initialValue = randomGen.nextInt(1000); //* upperBound;
		newMeasurement.setValue(initialValue);
		newMeasurement.setNodeNumber(this.getNodeNumber());
		measurements.put(0,newMeasurement);
	}

	public void broadcastMeasurement(int time)
	{
		Measurement toBroadcast = measurements.get(time);

		for(SensorNode neighbor:this.getNeighbors())
		{
			neighbor.receiveMeasurementFromNetwork(toBroadcast);
		}
	}

	public void receiveMeasurementFromNetwork(Measurement received)
	{
		MemoryPair pair = new MemoryPair();
		pair.setjnode(received.getNodeNumber());
		pair.setXj(received.getValue());
		pair.setTime(received.getTime());
		if(measurements.get(received.getTime())!=null) {
			pair.setXi(measurements.get(received.getTime()).getValue());
			this.addToCache(pair);
			//this.updateModel(received.getNodeNumber());
		}
	}


	public void updateModel(int Nj)
	{
		float temp1sum = 0,temp2sum=0,temp3sum=0,temp4sum=0;
		int i,j;
		int n=0;
		MemoryPair[] cacheLine=new MemoryPair[1000];
		boolean changed=false;
        float aStarValue=0,bStarValue=0;
		n=0;

			for(MemoryPair temp:this.cache.getSpace())
				if(temp.getjnode()==Nj)
				{
					cacheLine[n]=temp;
					n++;
				}


			temp1sum=0;temp2sum=0;temp3sum=0;temp4sum=0;

			for(i=1;i<n;i++)
			{
				if(cacheLine[i-1].getXi()!=cacheLine[i].getXi()) {
                    changed = true;
                    break;
                }
			}

            if(changed==false || n==1)
            {
                aStarValue = 1;
                for(i=0;i<n;i++)
                {
                    temp3sum=temp3sum+cacheLine[i].getXj();
                }
                //bStarValue = temp3sum/n;
                bStarValue=((temp3sum/n)-(aStarValue*cacheLine[0].getXi()));
            }
            else {
                for (i = 0; i < n; i++) {
                    temp1sum = temp1sum + (cacheLine[i].getXi() * cacheLine[i].getXj());
                    temp2sum = temp2sum + cacheLine[i].getXi();
                    temp3sum = temp3sum + cacheLine[i].getXj();
                    temp4sum = (temp4sum + (float) Math.pow(cacheLine[i].getXi(), 2));
                }

                aStarValue = (((n * temp1sum) - (temp2sum * temp3sum)) / ((n * temp4sum) - (float) Math.pow(temp2sum, 2)));
                bStarValue = (temp3sum - (aStarValue * temp2sum)) / n;
            }
            //if(Float.isNaN(aStarValue)||Float.isNaN(bStarValue))
               // System.out.println();
			aStar.put(Nj,aStarValue);
			bStar.put(Nj,bStarValue);

	}

	public void sendInvitation(Measurement currentMeasurement)
	{
		for(SensorNode temp:this.neighbors)
		{
			temp.receiveInvitation(currentMeasurement);
		}
	}

	public void receiveInvitation(Measurement currentMeasurement)
	{
		float estimate = createEstimate(currentMeasurement);
		compareEstimate(currentMeasurement,estimate, 1);
	}

	public void cacheReplacement(MemoryPair pair)
	{
		MemoryPair[] cacheLine=new MemoryPair[100];
		MemoryPair[] cacheLineAug=new MemoryPair[100];
		MemoryPair[] cacheLineShift=new MemoryPair[100];
		MemoryPair[] KcacheLine=new MemoryPair[200];
		MemoryPair[] KcacheLine2=new MemoryPair[200];
		double[] Penalty_Evict=new double[100];
		int Nj,Nk,i,j,x,victim_line,position=0;
		Nj=pair.getjnode();

		int amount=0;
		double astar,bstar,astar2,bstar2,astar3,bstar3,benefit,benefit2,benefit3,Gain_Augment,smallest;
		double nbenefit,nbenefit2;
		boolean found=false;
		this.cache.getSpace().sort(MemoryPair.comparatorForTime);

		for(MemoryPair temp:this.cache.getSpace())
		{
			if(temp.getjnode()==Nj) {
                    cacheLine[amount] = temp;
                    amount++;

			}
		}

		if(amount!=0)
		{
			for(i=0;i<amount;i++)
			{
			    cacheLineAug[i] = cacheLine[i];
			}
			try {
                cacheLineAug[amount]=pair;
            }catch(Exception e)
            {
                ;
            }


			for(i=0;i<amount-1;i++)
			{
				cacheLineShift[i]=cacheLine[i+1];
			}
			cacheLineShift[amount-1]=pair;

			astar=this.calculateaStar(cacheLine, amount);
			bstar=this.calculatebStar(cacheLine, amount, astar);
			astar2=this.calculateaStar(cacheLineShift, amount);
			bstar2=this.calculatebStar(cacheLineShift, amount, astar2);
			astar3=this.calculateaStar(cacheLineAug, amount+1);
			bstar3=this.calculatebStar(cacheLineAug, amount+1, astar3);

			benefit=this.no_answer_sse(cacheLineAug,amount+1)-this.calculateSse(cacheLineAug, astar, bstar, amount+1);
			benefit2=this.no_answer_sse(cacheLineAug, amount+1)-this.calculateSse(cacheLineAug, astar2, bstar2, amount+1);
			benefit3=this.no_answer_sse(cacheLineAug, amount+1)-this.calculateSse(cacheLineAug, astar3, bstar3, amount+1);


			nbenefit=benefit2;
			nbenefit2=benefit;

			if(benefit>=benefit2 && benefit>=benefit3)
				;

			if(benefit2>=benefit3)
			{
				for(i = 0;i < this.cache.getSpace().size();i++)
				{
					if(this.cache.getSpace().get(i).getjnode() == Nj) {
						this.cache.replaceMemPair(cacheLineShift[position], i);
						position++;
					}
				}
			}

			Gain_Augment=benefit3-benefit2;
			amount=0;
			if(benefit3>benefit2)
			{
				for(i=0;i<Penalty_Evict.length;i++)
					Penalty_Evict[i]=100000;


				for(x=0;x<this.neighbors.size();x++)
				{
					Nk=((SensorNode)(this.neighbors.get(x))).getNodeNumber();

					if(Nk==Nj)
						continue;

					amount=0;

					for(MemoryPair temp:this.cache.getSpace())
						if(temp.getjnode() == Nk)
						{
							KcacheLine[amount]=temp;
							amount++;
						}

					if(amount==1)
                        continue;

					for(i=0;i<amount-1;i++)
					{
						KcacheLine2[i]=KcacheLine[i+1];
					}

					astar=this.calculateaStar(KcacheLine, amount);
					bstar=this.calculatebStar(KcacheLine, amount, astar);
					benefit=this.no_answer_sse(KcacheLine,amount)-this.calculateSse(KcacheLine, astar, bstar, amount);
					astar2=this.calculateaStar(KcacheLine2, amount-1);
					bstar2=this.calculatebStar(KcacheLine2, amount-1, astar2);
					benefit2=this.no_answer_sse(KcacheLine2,amount-1)-this.calculateSse(KcacheLine2, astar2, bstar2, amount-1);

					if((benefit-benefit2)<Gain_Augment)
					{
						Penalty_Evict[KcacheLine[0].getjnode()-1]=benefit-benefit2;
						found=true;
					}

				}

				if(found==true)
				{
					smallest=Penalty_Evict[0];
					victim_line=0;
					for(i=0;i<Penalty_Evict.length;i++)
					{
						if(Penalty_Evict[i]<=smallest)
						{
							smallest=Penalty_Evict[i];
							victim_line=i;
						}
					}

					for(i = 0;i < this.cache.getSpace().size();i++)
						if(this.cache.getSpace().get(i).getjnode() == victim_line + 1)
						{
							//System.out.println(this.cache.getSpace().get(i).getXi()+","+this.cache.getSpace().get(i).getXj());
							this.cache.replaceMemPair(pair,i);
							break;
						}


				}
				if(found==false && nbenefit>nbenefit2)
				{
					position=0;

					for(i = 0;i < this.cache.getSpace().size();i++)
					{
						if(this.cache.getSpace().get(i).getjnode() == Nj) {
							this.cache.replaceMemPair(cacheLineShift[position], i);
							position++;
						}
					}
				}
				else
                {
                    ArrayList<Integer> tempArray;
                    boolean foundVictim = false;
                    while(!foundVictim)
                    {
                        Nj = this.getNeighbors().get(NjRoundRobin).getNodeNumber();
                        tempArray = new ArrayList<>();
                        for (int y = 0; y < this.cache.getSpace().size(); y++) {
                            if (this.cache.getSpace().get(y).getjnode() == Nj) {
                                tempArray.add(y);
                                if (tempArray.size() != 1) {
                                    this.cache.getSpace().set(tempArray.get(0), pair);
                                    foundVictim = true;
                                    break;
                                }
                            }
                        }


                        NjRoundRobin++;
                        if (NjRoundRobin == this.getNeighbors().size()) {
                            NjRoundRobin = 0;
                        }
                    }
                }
			}
		}

		else if(amount==0) {

            ArrayList<Integer> tempArray;
            boolean foundVictim = false;
            while(!foundVictim)
            {
                Nj = this.getNeighbors().get(NjRoundRobin).getNodeNumber();
                tempArray = new ArrayList<>();
                for (int y = 0; y < this.cache.getSpace().size(); y++) {
                    if (this.cache.getSpace().get(y).getjnode() == Nj) {
                        tempArray.add(y);
                        if (tempArray.size() != 1) {
                            this.cache.getSpace().set(tempArray.get(0), pair);
                            foundVictim = true;
                            break;
                        }
                    }
                }


                NjRoundRobin++;
                if (NjRoundRobin == this.getNeighbors().size()) {
                    NjRoundRobin = 0;
                }
        }
        }


	}


}
