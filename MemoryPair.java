
public class MemoryPair {
	private float Xi;
	private float Xj;
	private int inode;
	private int jnode;
	
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
	
}
