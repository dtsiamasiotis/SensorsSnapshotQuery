import java.util.ArrayList;

public class CacheMemory {

	private ArrayList<MemoryPair> space;
	private int maxSize;

	public CacheMemory(int maxSize)
	{
		this.space = new ArrayList<MemoryPair>();
		calculateMaxSizeOfCache(maxSize);
	}

	public ArrayList<MemoryPair> getSpace(){ return this.space;}

	public void replaceMemPair(MemoryPair newMemPair, int index)
	{
		MemoryPair existingPair = this.space.get(index);
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
