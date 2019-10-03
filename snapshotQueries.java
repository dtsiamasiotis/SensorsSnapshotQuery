import java.util.*;

public class snapshotQueries {

	/**
	 * @param args
	 */
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final SensorNode[] komvos = new SensorNode[1];
		Vector repres=new Vector(); 
		Integer represnum=new Integer(0);

		int i = 0;

		/*Experiment One*/
		long startTime = System.nanoTime();
		System.out.println("Experiment One");
		int classes;
		int repetitions;
		int repressum = 0;
		int undefined=0;
		int active;
		int passive;
		int[] average=new int[20];
		firstExperiment[] Tests = new firstExperiment[10];
		
		for(classes=10;classes<=10;classes=classes+5)
		{
			repres.clear();
			repressum =0;
			undefined=0;
			active=0;
			passive=0;


					
			for(repetitions=0;repetitions<1;repetitions++)
			{
				Tests[repetitions] = new firstExperiment(classes);
				Tests[repetitions].start();

			 }
			for(repetitions=0;repetitions<1;repetitions++)
			{

				try {
					Tests[repetitions].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
			for(repetitions=0;repetitions<1;repetitions++)
			{
				repressum = repressum + (Tests[repetitions].getRepresSize());
			}

				average[classes/5]= repressum /10;
		
		}
		
			for(i = 1; i <=100; i = i +5)
			{
				System.out.println("Number of classes: "+ i +" Representatives: "+average[i /5]);
			}
		
		long endTime = System.nanoTime();
		long elapsedTime = (endTime - startTime)/1000000;
		System.out.println("Elapsed time in ms:"+elapsedTime);
		/*Experiment Two*/
		
			/*System.out.println("\nExperiment Two");
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
					repressum[0] =0;
			
					for (i[0] =0; i[0] <networkOfNodes2.getNodesList().size(); i[0]++)
					{
						komvos[0] =networkOfNodes2.getNodesList().get(i[0]);
						komvos[0].clearVectors();
						komvos[0].clearCache();
					}
					for(repetitions=0;repetitions<10;repetitions++)
					{
						repres.clear();
						classes=K[g];
						
						for (i[0] =0; i[0] <networkOfNodes2.getNodesList().size(); i[0]++)
						{
							komvos[0] =networkOfNodes2.getNodesList().get(i[0]);
							komvos[0].clearVectors();
						}
						networkOfNodes2.setNumberOfClasses(classes);
						networkOfNodes2.partitionIntoClasses();
						for (i[0] =0; i[0] <networkOfNodes2.getNodesList().size(); i[0]++)
						{
							komvos[0] =networkOfNodes2.getNodesList().get(i[0]);
							komvos[0].setrange(rangerep/10.0);
							komvos[0].clearCache();
							komvos[0].findNeighbors(networkOfNodes2.getNodesList());
						}
					
						networkOfNodes2.initialize();
						networkOfNodes2.training();
						
						for (i[0] =0; i[0] <networkOfNodes2.getNodesList().size(); i[0]++)
						{
							komvos[0] =networkOfNodes2.getNodesList().get(i[0]);
							komvos[0].modelBuild();
						}
						for (i[0] =0; i[0] <networkOfNodes2.getNodesList().size(); i[0]++)
						{
							komvos[0] =networkOfNodes2.getNodesList().get(i[0]);
							komvos[0].createEstimates();
						}
		
						networkOfNodes2.createCandidateLists();
						for (i[0] =0; i[0] <networkOfNodes2.getNodesList().size(); i[0]++)
						{
							komvos[0] =networkOfNodes2.getNodesList().get(i[0]);
							komvos[0].checkCandidateList();
						}	
		
		
						networkOfNodes2.breakties();
						networkOfNodes2.NoreprenentativeStayActive();
						networkOfNodes2.recallRedundant();
						networkOfNodes2.passiveMode();
						networkOfNodes2.finalcleanup();
		
	
						SensorNode num;
		
						for (i[0] =0; i[0] <networkOfNodes2.getNodesList().size(); i[0]++)
						{
							komvos[0] =networkOfNodes2.getNodesList().get(i[0]);
							num=(SensorNode) komvos[0].getRepresentatives().elementAt(0);
							if(repres.contains(num)==false)
								repres.add(num);
		
						}
						repressum[0] = repressum[0] +repres.size();
	
			}
			
					exp2temp[(int) (rangerep-2)][g]= repressum[0] /10;
				}
			
			}	
			for(g=0;g<5;g++)
			{	
				System.out.println();
				System.out.println("Number of classes: "+K[g]);
				for(i[0] =0; i[0] <13; i[0]++)
				{
					System.out.println("Range "+((double)(i[0] +2)/10) +":"+ exp2temp[i[0]][g]);
				}
			}*/
	
	}
}
