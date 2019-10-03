public class Measurement {
    private float value;
    private int time;
    private int nodeNumber;

    public void setValue(float newValue)
    {
        this.value = newValue;
    }

    public void setTime(int curTime)
    {
        this.time = curTime;
    }

    public int getTime(){ return this.time; }

    public float getValue()
    {
        return this.value;
    }

    public void setNodeNumber(int nodeNumber){ this.nodeNumber = nodeNumber; }

    public int getNodeNumber(){ return this.nodeNumber; }

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
