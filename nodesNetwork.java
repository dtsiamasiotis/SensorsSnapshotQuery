import javax.xml.soap.Node;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;


public class nodesNetwork {

	
	private float dimensionX=1;
	private float dimensionY=1;
	private int numberOfNodes=20;
	private double tolerance=1.0;
	private int numberOfClasses=1;
	private ArrayList<SensorNode> NodesList;
	private float broadcastedValues[][]=new float[100][100];
	int clasum[]=new int[100];
	
	public ArrayList<SensorNode> getNodesList(){
		return this.NodesList;
	}
	
	
	public void setNumberOfClasses(int num)
	{
		this.numberOfClasses=num;
	}
	
	//For each node we assign a random number in the range [0..numberOfClasses-1]
	//and this number specifies the class it belongs to. We also create a probability
	//for each class.Nodes of the same class move upwards or downwards with the probability
	//of their class.
	public void partitionIntoClasses()
	{
		int i,number;
		float Prob[]=new float[numberOfClasses];
	
		for(i=0;i<100;i++)
		{
			this.clasum[i]=0;
		}
		
		Random randomGen3=new Random();

		for(i=0;i<numberOfClasses;i++)
		{
			Prob[i]=(float)(randomGen3.nextInt(11))/10;
			if(Prob[i]<=0.1)
			{Prob[i]=(float)(randomGen3.nextInt(11))/10;}
		}

		for(SensorNode temp:NodesList)
		{
			number=randomGen3.nextInt(numberOfClasses);
			temp.setNumberOfClass(number);
			clasum[number]++;
			temp.setPmove(Prob[temp.getNumberOfClass()]);

		}
		
	}
	
	//We create a new network with as many nodes as numberOfNodes value. For each node
	//we create two random numbers in range [0...1) as coordinates.
	public void createNetwork(){
		Random randomGen1=new Random();
		Random randomGen2=new Random();
		NodesList=new ArrayList<SensorNode>();
		float number1,number2;
		int i;
			
		for(i=0;i<numberOfNodes;i++)
		{
			number1=randomGen1.nextFloat();
			number2=randomGen2.nextFloat();
			
			SensorNode networkNode=new SensorNode();
			networkNode.setNodeNumber(i+1);
			networkNode.setX(number1);
			networkNode.setY(number2);

			NodesList.add(networkNode);
		}
		
}

	//At the beginning we assume that every node is represented by all of his neighbors
	public void initialize(){
		for(SensorNode temp:NodesList)
		{
			temp.setRepresentatives((Vector) (temp.getNeighbors()).clone());
		}
	}
	
	
	//Training includes three stages.In the first stage we create 3 measurements for every node
	//Each measurement is given a probability. If this probability is smaller than the probability
	//of node to make a step, the node makes a step upwards or downwards(we chose this creating a 
	//random number uniformly between 0 and 1).If the probability is larger we don't change node's
	//value. These measurements are stored in the cache of every node and we also have a vector
	//containing all the values that have been broadcasted to the network. When this stage is finished 
	//every node has a full cache(cache width=3).The second stage is same with the first but this
	//time we create 7 measurements and we replace existing pairs in the cache with the new ones.
	//Finally we have another stage of changing nodes' values but without storing them in cache.
	public void training(){
		int i,k,l;

		SensorNode temp2= null;
		SensorNode temp3= null;
		Random randomGen1=new Random();
		float measurement,probability;
		int trainingTimes,changeValues;
		int plusminus;
		MemoryPair cachepair=new MemoryPair();
		
		
		for(trainingTimes=0;trainingTimes<3;trainingTimes++)
		{
			
			if(trainingTimes==0)
			{
				for(i=0;i<this.numberOfNodes;i++)
				{
				measurement=randomGen1.nextInt(1001);
				this.broadcastedValues[i][0]=measurement;
				}
				
			}
			else{
				for(k=0;k<this.numberOfClasses;k++)
				{
					
					plusminus=randomGen1.nextInt(2);
					probability=(float)(randomGen1.nextInt(11))/10;
					
					for(l=0;l<this.numberOfNodes;l++)
					{
						temp3=NodesList.get(l);
						measurement=randomGen1.nextFloat();


						if(plusminus==0 && temp3.getNumberOfClass()==k && temp3.getPmove()>=probability)
						{
							this.broadcastedValues[l][trainingTimes]=this.broadcastedValues[l][trainingTimes-1]+measurement;
						}
				
						if(plusminus==1 && temp3.getNumberOfClass()==k && temp3.getPmove()>=probability)
						{
							this.broadcastedValues[l][trainingTimes]=this.broadcastedValues[l][trainingTimes-1]-measurement;
						}
						if(temp3.getPmove()<probability)
							this.broadcastedValues[l][trainingTimes]=this.broadcastedValues[l][trainingTimes-1];
				}
			}
			
			}
		
		int j;
		SensorNode temp = null;
		for(i=0;i<this.NodesList.size();i++)
		{
			temp = NodesList.get(i);
			for(j=0;j<temp.getNeighbors().size();j++)
			{
				cachepair=new MemoryPair();
				cachepair.setinode(i+1);
				temp2=((SensorNode)(temp.getNeighbors().get(j)));
				cachepair.setjnode(temp2.getNodeNumber());
				cachepair.setXi(this.broadcastedValues[temp.getNodeNumber()-1][trainingTimes]);
				cachepair.setXj(this.broadcastedValues[temp2.getNodeNumber()-1][trainingTimes]);
				temp.addToCache(cachepair);
			}
		
		}
		}
		
		
		
		for(trainingTimes=3;trainingTimes<10;trainingTimes++)
		{
		
		
			
			for(k=0;k<this.numberOfClasses;k++)
			{
				plusminus=randomGen1.nextInt(2);
				probability=randomGen1.nextFloat();
				for(l=0;l<this.numberOfNodes;l++)
				{
					temp3 = NodesList.get(l);
					measurement=randomGen1.nextFloat();

					if(plusminus==0 && temp3.getNumberOfClass()==k && temp3.getPmove()>=probability)
					{
						this.broadcastedValues[l][trainingTimes]=this.broadcastedValues[l][trainingTimes-1]+measurement;
					}
					
					if(plusminus==1 && temp3.getNumberOfClass()==k && temp3.getPmove()>=probability)
					{
						this.broadcastedValues[l][trainingTimes]=this.broadcastedValues[l][trainingTimes-1]-measurement;
					}
					if(temp3.getPmove()<probability)
						this.broadcastedValues[l][trainingTimes]=this.broadcastedValues[l][trainingTimes-1];
			}
		}
			
			

		int j;
		SensorNode temp = null;

		for(i=0;i<this.NodesList.size();i++)
		{
			temp = NodesList.get(i);
			for(j=0;j<temp.getNeighbors().size();j++)
			{
				cachepair=new MemoryPair();
				cachepair.setinode(i+1);
				temp2=((SensorNode)(temp.getNeighbors().get(j)));
				cachepair.setjnode(temp2.getNodeNumber());
				cachepair.setXi(this.broadcastedValues[temp.getNodeNumber()-1][trainingTimes]);
				cachepair.setXj(this.broadcastedValues[temp2.getNodeNumber()-1][trainingTimes]);
				temp.cacheReplacement(cachepair);
			}
		
		}
		}
		int o;
		
		
		for(changeValues=10;changeValues<100;changeValues=changeValues+11)
		{
		
				for(k=0;k<this.numberOfClasses;k++)
				{
					
					plusminus=randomGen1.nextInt(2);
					probability=randomGen1.nextFloat();
					for(l=0;l<this.numberOfNodes;l++)
					{
						temp3=NodesList.get(l);
						measurement=randomGen1.nextFloat();

						if(plusminus==0 && temp3.getNumberOfClass()==k && temp3.getPmove()>=probability)
						{
							if(changeValues==10)
								this.broadcastedValues[l][changeValues]=this.broadcastedValues[l][changeValues-1]+measurement;
							if(changeValues>10)
								this.broadcastedValues[l][changeValues]=this.broadcastedValues[l][changeValues-11]+measurement;
						}
				
						if(plusminus==1 && temp3.getNumberOfClass()==k && temp3.getPmove()>=probability)
						{
							if(changeValues==10)
								this.broadcastedValues[l][changeValues]=this.broadcastedValues[l][changeValues-1]-measurement;
							if(changeValues>10)
								this.broadcastedValues[l][changeValues]=this.broadcastedValues[l][changeValues-11]-measurement;
						}
						if(temp3.getPmove()<probability){
							if(changeValues==10)
								this.broadcastedValues[l][changeValues]=this.broadcastedValues[l][changeValues-1];
							if(changeValues>10)
								this.broadcastedValues[l][changeValues]=this.broadcastedValues[l][changeValues-11];
						}
				}
			}
		}
		
	}
	
	
	
	//Every Node has a candidate list. To add a neighbor to this list, the sse of
	//his estimated values and the real ones must be smaller or equal to the tolerance(=1)
	//Sse includes the values stored in cache but also the last broadcasted value which is
	//estimated with the model built from cached values.
	public void createCandidateLists()
	{
		SensorNode temp,temp2;
		int i,j,l,o,p,n;
		double amount=0;
		double lastEstim=0;
		MemoryPair[] cacheline=new MemoryPair[100];
		
		for(i=0;i<this.NodesList.size();i++)
		{
			temp=NodesList.get(i);
			for(j=0;j<temp.getNeighbors().size();j++)
			{
				amount=0;
				temp2=((SensorNode)(temp.getNeighbors().get(j)));
				lastEstim=((temp.getaStar(j)) * (this.broadcastedValues[temp.getNodeNumber()-1][98]))+temp.getbStar(j);
				
				
				l=0;
				n=0;
				for(o=0;o<100;o++){
					for(p=0;p<3;p++)
						if(temp.cache.getPair(o,p)!=null && temp.cache.getPair(o,p).getjnode()==temp2.getNodeNumber())
						{
							cacheline[n]=temp.cache.getPair(o,p);
							n++;
						}
					}
					while(temp.getEstimation(temp2.getNodeNumber()-1,l)!=-10000)
					{
						amount=amount+Math.pow(((temp.getEstimation(temp2.getNodeNumber()-1,l)-cacheline[l].getXj())), 2);
						l++;
					}
					amount=amount+Math.pow(lastEstim-this.broadcastedValues[temp2.getNodeNumber()-1][98], 2);
			
					if(amount<=tolerance)
					{
						temp.addToCandidateList(temp2);
					}
			
				}
		}
	}
	
	public void breakties()
	{
		SensorNode temp2;
		String state=new String();

		for(SensorNode temp:NodesList)
		{
			temp2=(SensorNode)(temp.getRepresentatives().elementAt(0));
			if((temp2.getRepresentatives().elementAt(0)).equals(temp)==true)
			{
				state=((temp.getCandidateList().size())>=(temp2.getCandidateList().size()) && temp.getNodeNumber()>temp2.getNodeNumber())?"active":"undefined";	
				temp.setStatus(state);
					
			}
		}
	}
	public void NoreprenentativeStayActive()
	{
		for(SensorNode temp:NodesList)
		{
			if(temp.getRepresentatives().elementAt(0).equals(temp))
				temp.setStatus("active");
		}
	}
	
	public void recallRedundant()
	{
		for(SensorNode temp:NodesList)
		{
			if(temp.getStatus()=="active" && temp.getRepresentatives().elementAt(0)!=temp)
			{
				temp.getRepresentatives().remove(0);
				temp.getRepresentatives().add(temp);
			}
		}
		
	}
	
	public void passiveMode()
	{
		boolean representative=false;
		SensorNode temp,temp2;
		int i,j;
		for(i=0;i<this.NodesList.size();i++)
		{
			temp=NodesList.get(i);
			if(temp.getRepresentatives().elementAt(0).equals(temp)==false)
				for(j=0;j<this.NodesList.size();j++)
				{
					temp2=NodesList.get(i);
					if(temp2.getRepresentatives().elementAt(0).equals(temp)==true)
						representative=true;
				}
				
		if(representative==false)
		{
			temp.setStatus("passive");
		}
	
		}
		}
	
	public void finalcleanup()
	{

		for(SensorNode temp:NodesList)
		{
			if(temp.getStatus()=="active" && temp.getRepresentatives().elementAt(0).equals(temp)==false)
				temp.getRepresentatives().remove(0);
			temp.getRepresentatives().add(temp);
		}
	}

	public static class Builder {

		private int numberOfNodes=20;
		private float dimensionX=1;
		private float dimensionY=1;

		public Builder(int numberOfNodes)
		{
			this.numberOfNodes = numberOfNodes;
		}

		public Builder withDimensions(float x, float y)
		{
			this.dimensionX = x;
			this.dimensionY = y;

			return this;
		}

		public nodesNetwork build()
		{
			nodesNetwork network = new nodesNetwork();
			network.numberOfNodes = this.numberOfNodes;
			network.dimensionX =  this.dimensionX;
			network.dimensionY = this.dimensionY;

			return network;
		}
	}

	private nodesNetwork()
	{

	}


	public void printInfo()
	{
		for(SensorNode node:getNodesList())
		{
			System.out.printf("Node id:%d x:%f y:%f\n",node.getNodeNumber(),node.getX(),node.getY());
		}

	}
}
	


