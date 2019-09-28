
public class MemoryPair {
	private double Xi;
	private double Xj;
	private int inode;
	private int jnode;
	
	public void setXi(double d)
	{
		this.Xi=d;
	}
	
	public void setXj(double value)
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
	
}
