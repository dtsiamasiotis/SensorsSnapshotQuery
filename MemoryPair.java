import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;

@Getter
@Setter
public class MemoryPair {
	private float Xi;
	private float Xj;
	private int inode;
	private int jnode;
	private int time;

	public static Comparator<MemoryPair> comparatorForTime = (pair1,pair2)->{return Integer.compare(pair1.time,pair2.time);};

	@Override
	public String toString()
	{
		return "j:"+this.jnode+","+"time:"+this.time+","+"{" + this.Xi + "," + this.Xj + "}";
	}
}
