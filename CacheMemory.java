import java.util.*;

public class CacheMemory {

	private ArrayList<List<MemoryPair>> space;
	private int maxSize;
	private int NjRoundRobin = 0;

	public CacheMemory(int maxSize, int maxNumberOfNeighbors)
	{
		this.space = new ArrayList<>();
		for (int i = 0; i < maxNumberOfNeighbors; i++)
			space.add(new LinkedList<>());
		calculateMaxSizeOfCache(maxSize);
	}

	public ArrayList<List<MemoryPair>> getSpace(){ return this.space;}


	public void addPair(MemoryPair pair)
	{
		int Nj = pair.getJnode();
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

	public void cacheReplacement(MemoryPair pair, LinkedList<SensorNode> neighbors)
	{

		Map<Integer, Double> Penalty_Evict=new HashMap<>();
		int Nj,Nk,i,j,x=0;
		Nj=pair.getJnode();


		double astar,bstar,astar2,bstar2,astar3,bstar3,benefit,benefit2,benefit3,Gain_Augment;
		double nbenefit,nbenefit2;
		boolean found=false;

		List<MemoryPair> cacheLine = space.get(Nj);

			if (!cacheLine.isEmpty()) {

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

					if(benefit3>benefit2)
					{

						for(x=0;x<neighbors.size();x++)
						{
							Nk=neighbors.get(x).getNodeNumber();

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
							removeFromLineWithSmallestPenalty(Penalty_Evict, pair);
						}
						if(!found && nbenefit>nbenefit2)
						{
							space.set(Nj, cacheLineShift);
						} else
						{
							replaceMemoryInRoundRobinFashion(pair);
						}
					}

			} else {
				replaceMemoryInRoundRobinFashion(pair);
			}

	}

	private void replaceMemoryInRoundRobinFashion(MemoryPair pair) {
		while (space.get(NjRoundRobin).isEmpty()||space.get(NjRoundRobin).size()==1) {
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

	private void removeFromLineWithSmallestPenalty(Map<Integer, Double> Penalty_Evict, MemoryPair pair) {
		double smallest = 0;
		int victim_line = 0;

		Iterator<Map.Entry<Integer, Double>> it = Penalty_Evict.entrySet().iterator();

		while(it.hasNext()) {
			Map.Entry<Integer, Double> entry = it.next();
			if (entry.getValue() <= smallest) {
				victim_line = entry.getKey();
				smallest = entry.getValue();
			}
		}

		space.get(victim_line).remove(0);
		addPair(pair);
	}
}
