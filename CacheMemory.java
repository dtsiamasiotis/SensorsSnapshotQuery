
public class CacheMemory {
	int width=3;
	private MemoryPair[][] space=new MemoryPair[100][width];
	int amount=0;
	
	
	
	public MemoryPair getPair(int line,int place)
	{
		return this.space[line][place];
	}
	
	public void addPair(MemoryPair pair)
	{
		int line=pair.getjnode()-1;
		int i;
	
		if(space[line][width-1]!=null)
		{
			space[line][0]=pair;
		}
		else{
		for(i=0;i<width;i++)
		{
			if(space[line][i]==null)
				break;
		}
		space[line][i]=pair;
		amount++;
		}
		}
	
	public void addPairTo(MemoryPair pair,int line,int column)
	{
		space[line][column]=pair;
	}
	
}
