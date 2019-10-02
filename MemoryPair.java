import java.util.Comparator;

public class MemoryPair {
	private float Xi;
	private float Xj;
	private int inode;
	private int jnode;
	private int time;
	
	public void setXi(float d)
	{
		this.Xi=d;
	}
	
	public void setXj(float value)
	{
		this.Xj=value;
	}
	
	public double getXi()
	{
		return this.Xi;
	}
	
	public double getXj()
	{
		return this.Xj;
	}
	
	public int getinode()
	{
		return this.inode;
	}
	
	public int getjnode()
	{
		return this.jnode;
	}
	
	public void setinode(int value)
	{
		this.inode=value;
	}
	
	public void setjnode(int value)
	{
		this.jnode=value;
	}

	public int getTime(){ return this.time; }

	public void setTime(int time){ this.time = time;}

	public static Comparator<MemoryPair> comparatorForTime = (pair1,pair2)->{return Integer.compare(pair1.time,pair2.time);};

	@Override
	public String toString()
	{
		return "j:"+this.getjnode()+","+"time:"+this.getTime()+","+"{" + this.getXi() + "," + this.getXj() + "}";
	}
}
