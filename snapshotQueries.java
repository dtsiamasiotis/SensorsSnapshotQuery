import java.util.*;

public class snapshotQueries {

	/**
	 * @param args
	 */
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SensorNode komvos;
		Vector repres=new Vector(); 
		Integer represnum=new Integer(0);
		
		
		int i;
		
		
		/*Experiment One*/
		
		System.out.println("Experiment One");
		int classes,repetitions,repressum=0,undefined=0,active,passive;
		int[] average=new int[20];
		nodesNetwork networkOfNodes2=new nodesNetwork();
		networkOfNodes2.createNetwork();
		
		for(classes=1;classes<=100;classes=classes+5)
		{
			repres.clear();
			repressum=0;
			undefined=0;
			active=0;
			passive=0;
			
			for (i=0;i<networkOfNodes2.getNodesList().size();i++)
			{
				komvos=(SensorNode)(networkOfNodes2.getNodesList().elementAt(i));
				komvos.clearVectors();
				komvos.clearCache();
			}
					
			for(repetitions=0;repetitions<10;repetitions++)
			{
				repres.clear();
				
		
				for (i=0;i<networkOfNodes2.getNodesList().size();i++)
				{
					komvos=(SensorNode)(networkOfNodes2.getNodesList().elementAt(i));
					komvos.clearVectors();
				}
				
				networkOfNodes2.setNumberOfClasses(classes);
				networkOfNodes2.partitionIntoClasses();
		
				for (i=0;i<networkOfNodes2.getNodesList().size();i++)
				{
					komvos=(SensorNode)(networkOfNodes2.getNodesList().elementAt(i));
					komvos.setrange(Math.sqrt(200));
					komvos.clearCache();
					komvos.findNeighbors(networkOfNodes2.getNodesList());
				}
		
				networkOfNodes2.initialize();
		
				networkOfNodes2.training();
		
				for (i=0;i<networkOfNodes2.getNodesList().size();i++)
				{
					komvos=(SensorNode)(networkOfNodes2.getNodesList().elementAt(i));
					komvos.modelBuild();
				}
	
				for (i=0;i<networkOfNodes2.getNodesList().size();i++)
				{
					komvos=(SensorNode)(networkOfNodes2.getNodesList().elementAt(i));
					komvos.createEstimates();
				}
		
				networkOfNodes2.createCandidateLists();
		
				for (i=0;i<networkOfNodes2.getNodesList().size();i++)
				{
					komvos=(SensorNode)(networkOfNodes2.getNodesList().elementAt(i));
					komvos.checkCandidateList();
				}
		
		
				networkOfNodes2.breakties();
				networkOfNodes2.NoreprenentativeStayActive();
				networkOfNodes2.recallRedundant();
				networkOfNodes2.passiveMode();
				networkOfNodes2.finalcleanup();
		
	
				SensorNode num;
		
				for (i=0;i<networkOfNodes2.getNodesList().size();i++)
				{
					komvos=(SensorNode)(networkOfNodes2.getNodesList().elementAt(i));
					num=(SensorNode)komvos.getRepresentatives().elementAt(0);
					if(repres.contains(num)==false){
						repres.add(num);
					}
				}
		
				repressum=repressum+repres.size();
		
			 } 
				average[classes/5]=repressum/10;
		
		}
		
			for(i=1;i<=100;i=i+5)
			{
				System.out.println("Number of classes: "+i+" Representatives: "+average[i/5]);
			}
		
		
		/*Experiment Two*/
		
			System.out.println("\nExperiment Two");
			double rangerep;
			int g;
			int[][] exp2temp=new int[13][5];
			int[] K={1,5,10,20,100};
		
			networkOfNodes2.createNetwork();
		
			for(rangerep=2;rangerep<15;rangerep++)
			{
				for(g=0;g<5;g++)
				{
					repres.clear();
					repressum=0;
			
					for (i=0;i<networkOfNodes2.getNodesList().size();i++)
					{
						komvos=(SensorNode)(networkOfNodes2.getNodesList().elementAt(i));
						komvos.clearVectors();
						komvos.clearCache();
					}
					for(repetitions=0;repetitions<10;repetitions++)
					{
						repres.clear();
						classes=K[g];
						
						for (i=0;i<networkOfNodes2.getNodesList().size();i++)
						{
							komvos=(SensorNode)(networkOfNodes2.getNodesList().elementAt(i));
							komvos.clearVectors();
						}
						networkOfNodes2.setNumberOfClasses(classes);
						networkOfNodes2.partitionIntoClasses();
						for (i=0;i<networkOfNodes2.getNodesList().size();i++)
						{
							komvos=(SensorNode)(networkOfNodes2.getNodesList().elementAt(i));
							komvos.setrange(rangerep/10.0);
							komvos.clearCache();
							komvos.findNeighbors(networkOfNodes2.getNodesList());
						}
					
						networkOfNodes2.initialize();
						networkOfNodes2.training();
						
						for (i=0;i<networkOfNodes2.getNodesList().size();i++)
						{
							komvos=(SensorNode)(networkOfNodes2.getNodesList().elementAt(i));
							komvos.modelBuild();
						}
						for (i=0;i<networkOfNodes2.getNodesList().size();i++)
						{
							komvos=(SensorNode)(networkOfNodes2.getNodesList().elementAt(i));
							komvos.createEstimates();
						}
		
						networkOfNodes2.createCandidateLists();
						for (i=0;i<networkOfNodes2.getNodesList().size();i++)
						{
							komvos=(SensorNode)(networkOfNodes2.getNodesList().elementAt(i));
							komvos.checkCandidateList();
						}	
		
		
						networkOfNodes2.breakties();
						networkOfNodes2.NoreprenentativeStayActive();
						networkOfNodes2.recallRedundant();
						networkOfNodes2.passiveMode();
						networkOfNodes2.finalcleanup();
		
	
						SensorNode num;
		
						for (i=0;i<networkOfNodes2.getNodesList().size();i++)
						{
							komvos=(SensorNode)(networkOfNodes2.getNodesList().elementAt(i));
							num=(SensorNode)komvos.getRepresentatives().elementAt(0);
							if(repres.contains(num)==false)
								repres.add(num);
		
						}
						repressum=repressum+repres.size();
	
			}
			
					exp2temp[(int) (rangerep-2)][g]=repressum/10;
				}
			
			}	
			for(g=0;g<5;g++)
			{	
				System.out.println();
				System.out.println("Number of classes: "+K[g]);
				for(i=0;i<13;i++)
				{
					System.out.println("Range "+((double)(i+2)/10) +":"+ exp2temp[i][g]);
				}
			}
	
	}
}
