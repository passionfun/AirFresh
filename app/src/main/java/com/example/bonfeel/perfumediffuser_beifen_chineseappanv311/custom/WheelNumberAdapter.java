package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom;

/**
 *
 * Author:FunFun
 * Function:
 * Date:2016/4/12 10:40
 */
public class WheelNumberAdapter implements WheelAdapter
{
    public static final int DEFAULT_MAX_VALUE = 9;
    public static final int DEFAULT_MIN_VALUE = 0;

    private int minValue;
    private int maxValue;

    private String format;
    /* (非Javadoc)
     * 方法名称：	getItemsCount
     * 方法描述：	TODO
     * 重写部分：	@see com.example.wheel.WheelAdapter#getItemsCount()
     */
    @Override
    public int getItemsCount()
    {
        return (maxValue - minValue + 1);
    }

    /**
     * 构造方法名称：	NumberAdapter
     * 创建时间：		2015-2-4上午9:57:24
     * 方法描述：		TODO
     */
    public WheelNumberAdapter(int minValue, int maxValue)
    {
        super();
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.format = null;
    }

    /* (非Javadoc)
     * 方法名称：	getItem
     * 方法描述：	TODO
     * 重写部分：	@see com.example.wheel.WheelAdapter#getItem(int)
     */
    @Override
    public String getItem(int index)
    {
        if ( index >= 0 && index < getItemsCount() )
        {
            int value = minValue + index;
            return format != null ? String.format(format, value):Integer.toString(value);
        }
        return null;
    }

    /* (非Javadoc)
     * 方法名称：	getMaximumLength
     * 方法描述：	TODO
     * 重写部分：	@see com.example.wheel.WheelAdapter#getMaximumLength()
     */
    @Override
    public int getMaximumLength()
    {
        int max = Math.max(Math.abs(maxValue), Math.abs(minValue));
        int maxLen = Integer.toString(max).length();
        if ( minValue < 0 )
        {
            maxLen++;
        }
        return maxLen;
    }

}
