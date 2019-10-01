public class Measurement {
    private float value;
    private int time;

    public void setValue(float newValue)
    {
        this.value = newValue;
    }

    public void setTime(int curTime)
    {
        this.time = curTime;
    }

    public float getValue()
    {
        return this.value;
    }

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
