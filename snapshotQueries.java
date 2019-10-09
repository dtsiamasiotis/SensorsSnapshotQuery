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
		secondExperiment[] Tests2 = new secondExperiment[10];
		
		for(classes=1;classes<=100;classes=classes+5)
		{
			repres.clear();
			repressum =0;
			undefined=0;
			active=0;
			passive=0;


					
			for(repetitions=0;repetitions<10;repetitions++)
			{
				Tests[repetitions] = new firstExperiment(classes);
				Tests[repetitions].start();

			 }
			for(repetitions=0;repetitions<10;repetitions++)
			{

				try {
					Tests[repetitions].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
			for(repetitions=0;repetitions<10;repetitions++)
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
		
			System.out.println("\nExperiment Two");
			double rangerep;
			int g;
			int[][] exp2temp=new int[13][5];
			int[] K={1,5,10,20,100};
		

		
			for(rangerep=2;rangerep<15;rangerep++)
			{
				for(g=0;g<5;g++)
				{
					repres.clear();
					repressum = 0;

					for(repetitions=0;repetitions<10;repetitions++)
					{
						repres.clear();
						classes=K[g];


						for(repetitions=0;repetitions<10;repetitions++)
						{
							Tests2[repetitions] = new secondExperiment(classes,rangerep/10.0);
							Tests2[repetitions].start();

						}
						for(repetitions=0;repetitions<10;repetitions++)
						{

							try {
								Tests2[repetitions].join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

						}
						for(repetitions=0;repetitions<10;repetitions++)
						{
							repressum = repressum + (Tests2[repetitions].getRepresSize());
						}


						repressum = repressum +repres.size();
	
					}
			
					exp2temp[(int) (rangerep-2)][g]= repressum /10;
				}
			
			}	
			for(g=0;g<5;g++)
			{	
				System.out.println();
				System.out.println("Number of classes: "+K[g]);
				for(i =0; i <13; i++)
				{
					System.out.println("Range "+((double)(i +2)/10) +":"+ exp2temp[i][g]);
				}
			}
	
	}
}
