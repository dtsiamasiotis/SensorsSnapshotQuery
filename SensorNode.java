import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.lang.*;
import java.math.*;

public class SensorNode {
	@Getter
	@Setter
	private int NodeNumber;
	@Getter
	@Setter
	private double range;
	@Getter
	@Setter
	private float xPosition;
	@Getter
	@Setter
	private float yPosition;
	private int numberOfClass;
	@Getter
	@Setter
	private float Pmove;
	@Getter
	@Setter
	private CacheMemory cache;
	private LinkedList<SensorNode> representatives = new LinkedList<SensorNode>();
	private LinkedList<SensorNode> neighbors;
	private LinkedList<SensorNode> candidateList=new LinkedList<SensorNode>();
	private double[][] estimatedMeasurements;
	@Getter
	@Setter
	private NodeStatus status = NodeStatus.UNDEFINED;
	private HashMap<Integer,Measurement> measurements = new HashMap<Integer, Measurement>();
	@Getter
	@Setter
	private float step;
	@Getter
	@Setter
	private Model model;

	
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
	
	//public void setReceivedMeasurements(int degrees){
	//	this.receivedMeasurements.add(degrees);
	//}

	public void addToCache(MemoryPair pair)
	{
		if(!this.cache.isCacheFull())
			this.cache.addPair(pair);
		else
			cache.cacheReplacement(pair,neighbors);
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

			distance=Math.sqrt(Math.pow((this.getXPosition()-temp.getXPosition()),2)+Math.pow((this.getYPosition()-temp.getYPosition()), 2));
			if(distance<range)
			{
				this.neighbors.add(temp);

			}
		}
		
	}
	
	//We buid a model for every node so we are able to estimate values of his neighbors.
	/*public void modelBuild(){

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
				if(temp.getJnode()==Nj)
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
		}*/
	


	public float createEstimate(Measurement Xj)
	{
		float estimate = 0;
		int time = Xj.getTime();
		int Nj = Xj.getNodeNumber();
		this.updateModel(Nj);
		float Xi = measurements.get(time).getValue();
		estimate = (model.aStar.get(Nj)*Xi) + model.bStar.get(Nj);

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
		if(this.representatives.isEmpty())
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
            if (representatives.getFirst().getCandidateList().size() < wannabeRepres.getCandidateList().size())
                representatives.set(0, wannabeRepres);
            if (representatives.getFirst().getCandidateList().size() == wannabeRepres.getCandidateList().size()) {
                if (representatives.getFirst().getNodeNumber() < wannabeRepres.getNodeNumber())
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
		pair.setJnode(received.getNodeNumber());
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
		model.updateModel(Nj, cache);
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

}
