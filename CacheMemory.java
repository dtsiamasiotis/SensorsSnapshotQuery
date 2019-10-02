import java.util.ArrayList;

public class CacheMemory {
	int width=3;
	int height=3;

	private ArrayList<MemoryPair> space;
	private int maxSize;
	int amount=0;
	


	/*public MemoryPair getPair(int line,int place)
	{
		return this.space[line][place];
	}

	
	public void addPairTo(MemoryPair pair,int line,int column)
	{
		space[line][column]=pair;
	}*/

	public int getWidth(){return this.width;}
	public int getHeight(){return this.height;}

	public CacheMemory(int maxSize)
	{
		this.space = new ArrayList<MemoryPair>();
		calculateMaxSizeOfCache(maxSize);
	}

	public ArrayList<MemoryPair> getSpace(){ return this.space;}

	public void replaceMemPair(MemoryPair newMemPair, int index)
	{
		MemoryPair existingPair = this.space.get(index);
		System.out.println("Pair replaced:"+ existingPair.toString());
		this.space.set(index, newMemPair);
	}

	public void addPair(MemoryPair pair)
	{
		this.space.add(pair);
	}

	public void calculateMaxSizeOfCache(int size)
	{
		this.maxSize = size / (2*4); //2*4=MemoryPair size, 2 times a float
	}

	public boolean isCacheFull()
	{
		return this.space.size() == this.maxSize;
	}

	public void clearCache()
	{
		this.space.clear();
	}
}
