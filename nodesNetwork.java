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
	private int cachePerNode;
	
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
			while(Prob[i]<=0.1)
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
		Random randomGen3=new Random();
		NodesList=new ArrayList<SensorNode>();
		float number1,number2;
		int i;
			
		for(i=0;i<numberOfNodes;i++)
		{
			number1=randomGen1.nextFloat();
			number2=randomGen2.nextFloat();
			float walkStep = 0;
			while(walkStep==0)
				walkStep = randomGen3.nextInt(11)/10;
			
			SensorNode networkNode=new SensorNode();
			networkNode.setNodeNumber(i+1);
			networkNode.setX(number1);
			networkNode.setY(number2);
			networkNode.setStep(walkStep);
			CacheMemory newCache = new CacheMemory(this.cachePerNode);
			networkNode.setCache(newCache);

			NodesList.add(networkNode);
		}
		
}

	//At the beginning we assume that every node is represented by all of his neighbors
	public void initialize(){
		for(SensorNode temp:NodesList)
		{
		//	temp.setRepresentatives(temp.getNeighbors().clone());
		}
	}


	public void breakties()
	{
		SensorNode temp2;
		String state=new String();

		for(SensorNode temp:NodesList)
		{
			if(temp.getRepresentatives().size()!=0) {
				temp2 = (SensorNode) (temp.getRepresentatives().get(0));
				if (temp2.getRepresentatives().size() != 0)
					if ((temp2.getRepresentatives().get(0)).equals(temp) == true) {
						state = ((temp.getCandidateList().size()) >= (temp2.getCandidateList().size()) && temp.getNodeNumber() > temp2.getNodeNumber()) ? "active" : "undefined";
						temp.setStatus(state);

					}
			}
		}
	}

	public void NoreprenentativeStayActive()
	{
		for(SensorNode temp:NodesList)
		{
			if(temp.getRepresentatives().isEmpty())
				temp.setStatus("active");
		}
	}
	
	public void recallRedundant()
	{
		for(SensorNode temp:NodesList)
		{
			if(temp.getStatus()=="active" && temp.getRepresentatives().size()!=0)
			{
				if(temp.getRepresentatives().get(0)!=temp)
					temp.getRepresentatives().remove(0);
				//temp.getRepresentatives().add(temp);
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
			if(!temp.getRepresentatives().isEmpty())
				for(j=0;j<this.NodesList.size();j++)
				{
					temp2=NodesList.get(j);
					if(temp2.getRepresentatives().size()!=0)
						if(temp2.getRepresentatives().get(0).equals(temp)==true)
							representative=true;
				}
				
		if(representative==false && !temp.getRepresentatives().isEmpty())
		{
			temp.setStatus("passive");
			temp.getRepresentatives().get(0).setStatus("active");
		}
			representative=false;
		}
		}
	
	public void finalcleanup()
	{
int toActive = 0;
		for(SensorNode temp:NodesList)
		{
			if(temp.getStatus()=="undefined") {
				temp.setStatus("active");
				toActive++;
			}
		}
		//System.out.println("toActive:"+toActive);
	}

	public static class Builder {

		private int numberOfNodes=20;
		private float dimensionX=1;
		private float dimensionY=1;
		private int cacheSize=1;

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

		public Builder withCachePerNode(int size)
		{
			this.cacheSize = size;

			return this;
		}

		public nodesNetwork build()
		{
			nodesNetwork network = new nodesNetwork();
			network.numberOfNodes = this.numberOfNodes;
			network.dimensionX =  this.dimensionX;
			network.dimensionY = this.dimensionY;
			network.cachePerNode = this.cacheSize;

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
	


