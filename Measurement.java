import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Measurement {
    private float value;
    private int time;
    private int nodeNumber;

    public Measurement(int curTime)
    {
        this.time = curTime;
    }

    @Override
    public int hashCode()
    {
        return time;
    }
}
