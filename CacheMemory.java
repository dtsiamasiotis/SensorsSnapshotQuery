import java.util.*;
import java.util.stream.Collectors;

public class CacheMemory {

	private ArrayList<List<MemoryPair>> space;
	private int maxSize;
	private ModelUtils modelUtils;
	private int NjRoundRobin = 0;

	public CacheMemory(int maxSize, int maxNumberOfNeighbors)
	{
		this.space = new ArrayList<>();
		for (int i = 0; i < maxNumberOfNeighbors; i++)
			space.add(new LinkedList<>());
		calculateMaxSizeOfCache(maxSize);
	}

	public ArrayList<List<MemoryPair>> getSpace(){ return this.space;}

	//public void replaceMemPair(MemoryPair newMemPair, int index)
//	{
	//	MemoryPair existingPair = this.space.get(index);
	//	this.space.set(index, newMemPair);
	//}

	public void addPair(MemoryPair pair)
	{
		int Nj = pair.getJnode();

		if (!space.isEmpty())
			space.get(Nj).add(pair);

	}

	public void calculateMaxSizeOfCache(int size)
	{
		this.maxSize = size / (2*4); //2*4=MemoryPair size, 2 times a float
	}

	public boolean isCacheFull()
	{
		return getSize() >= this.maxSize;
	}

	public void clearCache()
	{
		//this.space.clear();
		for (List<MemoryPair> list : this.space)
			list.clear();
	}

	public int getSize() {
		return space.stream().mapToInt(List::size).sum();
	}

	public void cacheReplacement(MemoryPair pair)
	{
		//MemoryPair[] cacheLine=new MemoryPair[100];
		//MemoryPair[] cacheLineAug=new MemoryPair[100];
		//MemoryPair[] cacheLineShift=new MemoryPair[100];
		//MemoryPair[] KcacheLine=new MemoryPair[200];
		//MemoryPair[] KcacheLine2=new MemoryPair[200];
		Map<Integer, Double> Penalty_Evict=new HashMap<>();
		int Nj,Nk,i,j,x,victim_line,position=0;
		Nj=pair.getJnode();

		int amount=0;
		double astar,bstar,astar2,bstar2,astar3,bstar3,benefit,benefit2,benefit3,Gain_Augment,smallest;
		double nbenefit,nbenefit2;
		boolean found=false;
		//this.cache.getSpace().sort(MemoryPair.comparatorForTime);

		List<MemoryPair> cacheLine = space.get(Nj);
		if (cacheLine != null) {
			if (!cacheLine.isEmpty()) {
				{
					LinkedList<MemoryPair> cacheLineAug = new LinkedList<>(cacheLine);
					cacheLineAug.addLast(pair);

					LinkedList<MemoryPair> cacheLineShift = new LinkedList<>(cacheLine);
					cacheLineShift.removeFirst();
					cacheLineShift.addLast(pair);

					astar=ModelUtils.calculateaStar(cacheLine);
					bstar=ModelUtils.calculatebStar(cacheLine, astar);
					astar2=ModelUtils.calculateaStar(cacheLineShift);
					bstar2=ModelUtils.calculatebStar(cacheLineShift, astar2);
					astar3=ModelUtils.calculateaStar(cacheLineAug);
					bstar3=ModelUtils.calculatebStar(cacheLineAug,  astar3);

					benefit=ModelUtils.no_answer_sse(cacheLineAug)-ModelUtils.calculateSse(cacheLineAug, astar, bstar);
					benefit2=ModelUtils.no_answer_sse(cacheLineAug)-ModelUtils.calculateSse(cacheLineAug, astar2, bstar2);
					benefit3=ModelUtils.no_answer_sse(cacheLineAug)-ModelUtils.calculateSse(cacheLineAug, astar3, bstar3);


					nbenefit=benefit2;
					nbenefit2=benefit;

					if(benefit>=benefit2 && benefit>=benefit3)
						;

					if(benefit2>=benefit3)
					{
						space.set(Nj, cacheLineShift);
					}

					Gain_Augment=benefit3-benefit2;
					amount=0;
					if(benefit3>benefit2)
					{
						//for(i=0;i<Penalty_Evict.length;i++)
							//Penalty_Evict[i]=100000;


						for(x=0;x<space.size();x++)
						{
							if (space.get(x).isEmpty())
								continue;

							Nk = space.get(x).get(0).getJnode();

							if(Nk == Nj)
								continue;


							List<MemoryPair> KcacheLine = space.get(Nk);

							if(KcacheLine.size()==1)
								continue;

							LinkedList<MemoryPair> KcacheLine2 = new LinkedList<>(KcacheLine);
							KcacheLine2.removeFirst();

							astar=ModelUtils.calculateaStar(KcacheLine);
							bstar=ModelUtils.calculatebStar(KcacheLine, astar);
							benefit=ModelUtils.no_answer_sse(KcacheLine)-ModelUtils.calculateSse(KcacheLine, astar, bstar);
							astar2=ModelUtils.calculateaStar(KcacheLine2);
							bstar2=ModelUtils.calculatebStar(KcacheLine2, astar2);
							benefit2=ModelUtils.no_answer_sse(KcacheLine2)-ModelUtils.calculateSse(KcacheLine2, astar2, bstar2);

							if((benefit-benefit2)<Gain_Augment)
							{
								Penalty_Evict.put(KcacheLine.get(0).getJnode(), (benefit-benefit2));
								found=true;
							}

						}

						if(found)
						{
							smallest = 0;
							victim_line = 0;

							Iterator<Map.Entry<Integer, Double>> it = Penalty_Evict.entrySet().iterator();

							while(it.hasNext()) {
								Map.Entry<Integer, Double> entry = it.next();
								if (entry.getValue() <= smallest) {
									victim_line = entry.getKey();
								}
							}

							space.get(victim_line).remove(0);
							addPair(pair);

						}
						if(!found && nbenefit>nbenefit2)
						{
							space.set(Nj, cacheLineShift);
						}
						else
						{

								while (space.get(NjRoundRobin).isEmpty()) {
									NjRoundRobin++;
									if (NjRoundRobin == space.size())
										NjRoundRobin = 0;
								}

								space.get(NjRoundRobin).remove(0);
								addPair(pair);

								NjRoundRobin++;
								if (NjRoundRobin == space.size()) {
									NjRoundRobin = 0;
								}

						}
					}
				}
			}
		}
		else if(amount==0) {

			Nj = NjRoundRobin;//.getNodeNumber();
			space.get(Nj).remove(0);
			addPair(pair);

			NjRoundRobin++;
			if (NjRoundRobin == space.size()) {
				NjRoundRobin = 0;
			}
		}


	}
}
