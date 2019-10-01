import java.util.*;
import java.lang.*;
import java.math.*;

public class SensorNode {
	private int NodeNumber;
	private double range;
	private float xPosition;
	private float yPosition;
	private int numberOfClass;
	private double Pmove;
	CacheMemory cache=new CacheMemory(3,3);
	private Vector representatives;
	private LinkedList<SensorNode> neighbors;
	private Vector isRepresenting;
	private Vector candidateList=new Vector();
	private Vector receivedMeasurements;
	private double[] aStar;
	private double[] bStar;
	private double[][] estimatedMeasurements;
	private String status="undefined";
	private int test=0;
	private HashMap<Integer,Measurement> measurements = new HashMap<Integer, Measurement>();
	
	public void setNodeNumber(int value){
		this.NodeNumber=value;
	}
	
	
	public int getNodeNumber(){
		return this.NodeNumber;
	}
	
	public double getaStar(int pl)
	{
		return this.aStar[pl];
		                  
	}
	
	public double getbStar(int pl)
	{
		return this.bStar[pl];
		                  
	}
	
	public void setPmove(double pr){
		Pmove=pr;
	}
	
	public double getPmove(){
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
	
	public void setRepresentatives(Vector initialnodes){
		this.representatives=new Vector();
		this.representatives=initialnodes;
		
	}
	public Vector getRepresentatives()
	{
		return this.representatives;
	}
	
	public Vector getCandidateList()
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
	
	public void addToCache(MemoryPair pair)
	{
		this.cache.addPair(pair);
	}
	
	public void clearCache()
	{
		this.cache=new CacheMemory(3,3);
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
		}
		
	}
	
	//We buid a model for every node so we are able to estimate values of his neighbors.
	public void modelBuild(){
		aStar=new double[100];
		bStar=new double[100];
		double temp1sum = 0,temp2sum=0,temp3sum=0,temp4sum=0;
		int i,k,Nj,j;
		int n=0;
		MemoryPair[] cacheLine=new MemoryPair[1000];
		boolean changed=false;
		
		for(k=0;k<this.neighbors.size();k++)
		{
			Nj=(int)((SensorNode)(this.neighbors.get(k))).NodeNumber;
			
			n=0;
			for(i=0;i<100;i++)
				for(j=0;j<3;j++)
					if(this.cache.getPair(i,j)!=null && this.cache.getPair(i,j).getjnode()==Nj)
					{
						cacheLine[n]=this.cache.getPair(i,j);
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
			temp4sum=(temp4sum+Math.pow(cacheLine[i].getXi(),2));
			
		}
		
		
		aStar[k]=(((n*temp1sum)-(temp2sum*temp3sum))/((n*temp4sum)-Math.pow(temp2sum, 2)));
		bStar[k]=(temp3sum-(aStar[k]*temp2sum))/n;
		
		if(changed==false || n==1)
		{
			aStar[k]=0;
			bStar[k]=temp3sum/n;
		}
		
		}
		}
	
	public void createEstimates()
	{	
		this.estimatedMeasurements=new double[100][20];
		
		int k,i,Nj,j;
		int n;
		MemoryPair[] cacheLine=new MemoryPair[100];
		
		for(i=0;i<100;i++)
			for(j=0;j<20;j++)
				this.estimatedMeasurements[i][j]=-10000;
		
		
		for(k=0;k<this.neighbors.size();k++)
		{
		
			Nj=(int)((SensorNode)(this.neighbors.get(k))).NodeNumber;
			n=0;
			for(i=0;i<100;i++)
				for(j=0;j<3;j++)
					if(this.cache.getPair(i,j)!=null && this.cache.getPair(i,j).getjnode()==Nj)
					{
						cacheLine[n]=this.cache.getPair(i,j);
						n++;
					}
			
			for(i=0;i<n;i++)
			{
				this.estimatedMeasurements[Nj-1][i]=(aStar[k]*cacheLine[i].getXi())+bStar[k];
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
		
		this.representatives.clear();
		
		for(i=0;i<this.neighbors.size();i++)
		{
			tempnode=(SensorNode)(this.neighbors.get(i));
			for(j=0;j<tempnode.candidateList.size();j++)
			{
				tempnode2=(SensorNode)(tempnode.candidateList.elementAt(j));
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
	
	public void clearVectors()
	{
		this.candidateList.clear();
		//this.neighbors.clear();
		if(this.representatives!=null)
		this.representatives.clear();
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
		
		for(i=0;i<this.cache.getHeight();i++)
			for(j=0;j<this.cache.getWidth();j++)
				if(this.cache.getPair(i,j)!=null && this.cache.getPair(i,j).getjnode()==Nj)
				{
					cacheLine[amount]=this.cache.getPair(i,j);
					amount++;
				}
		
		if(amount!=0)
		{
		for(i=0;i<amount;i++)
		{
			cacheLineAug[i]=cacheLine[i];
		}
		cacheLineAug[amount]=pair;
		
		
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
			
			for(i=0;i<this.cache.getHeight();i++)
				for(j=0;j<this.cache.getWidth();j++)
					if(this.cache.getPair(i,j)!=null && this.cache.getPair(i,j).getjnode()==Nj)
					{
						this.cache.addPairTo(cacheLineShift[position],i,j);
						position++;
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
				for(i=0;i<this.cache.getHeight();i++)
					for(j=0;j<this.cache.getWidth();j++)
						if(this.cache.getPair(i,j)!=null && this.cache.getPair(i,j).getjnode()==Nk)
							{
							KcacheLine[amount]=this.cache.getPair(i,j);
							amount++;
							}
			
			
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
			
			search:
			for(i=0;i<this.cache.getHeight();i++)
				for(j=0;j<this.cache.getWidth();j++)
					if(this.cache.getPair(i,j)!=null && this.cache.getPair(i,j).getjnode()==victim_line+1)
						{
							
							this.cache.addPairTo(pair,i,j);
							break search;
						}
		
		}
			if(found==false && nbenefit>nbenefit2)
			{
				position=0;
			
				for(i=0;i<this.cache.getHeight();i++)
					for(j=0;j<this.cache.getWidth();j++)
						if(this.cache.getPair(i,j)!=null && this.cache.getPair(i,j).getjnode()==Nj)
						{
							this.cache.addPairTo(cacheLineShift[position],i,j);
							position++;
						}
			}
		}
		}
		
		
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
		if(changed==false)
		{
			bStar=temp3sum/amount;
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

	public void createNewMeasurement(int curTime)
	{
		Measurement newMeasurement = new Measurement(curTime);
		Random randomGen = new Random();
		int plusminus = randomGen.nextInt(2);
		float curValue = randomGen.nextFloat();
		float previousValue = measurements.get(curTime-1).getValue();
		if(plusminus == 0)
			newMeasurement.setValue(previousValue + curValue);
		else if(plusminus == 1)
			newMeasurement.setValue(previousValue - curValue);

		newMeasurement.setTime(curTime);
		measurements.put(curTime,newMeasurement);
	}

	public void initializeNodeWithValue(int upperBound)
	{
		Measurement newMeasurement = new Measurement(0);
		Random randomGen = new Random();
		float initialValue = randomGen.nextFloat() * upperBound;
		newMeasurement.setValue(initialValue);
		measurements.put(0,newMeasurement);
	}

	public void broadcastMeasurement(int time)
	{
		Measurement toBroadcast = measurements.get(time);

		for(SensorNode neighbor:this.getNeighbors())
		{
			neighbor.receiveMeasurementFromNetwork(this.getNodeNumber(),toBroadcast);
		}
	}

	public void receiveMeasurementFromNetwork(int senderId, Measurement received)
	{
		this.updateCache(received);

	}

	public void updateCache(Measurement received)
	{

	}
}
