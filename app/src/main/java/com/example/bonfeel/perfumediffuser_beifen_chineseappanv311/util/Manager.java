package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util;

/**
 *
 * Author:FunFun
 * Function:管理器，使用管理器来管理程序中的多个对象，例如设备和情景
 * 					但具体使用时，需实例化子类来使用。
 * Date:2016/4/12 9:42
 */
public abstract class Manager {
    /**
     * 方法名称：	create
     * 方法描述：	创建对象
     * 参数：			@return
     * 返回值类型：	Object
     * 创建时间：	2015-1-21上午9:42:11
     */
    public abstract Object create();
    /**
     * 方法名称：	delete
     * 方法描述：	删除对象
     * 参数：			@param item
     * 返回值类型：	void
     * 创建时间：	2015-1-21上午9:42:21
     */
    public abstract void delete(Object item);
    /**
     * 方法名称：	add
     * 方法描述：	添加对象到设备列表
     * 参数：			@param item
     * 返回值类型：	void
     * 创建时间：	2015-1-21上午9:42:30
     */
    public abstract void add(Object item);
    /**
     * 方法名称：	update
     * 方法描述：	更新对象
     * 参数：			@param item
     * 返回值类型：	void
     * 创建时间：	2015-1-21上午9:42:50
     */
    public abstract void update(Object item);
    /**
     * 方法名称：	get
     * 方法描述：	获取对象
     * 参数：			@param index
     * 参数：			@return
     * 返回值类型：	Object
     * 创建时间：	2015-1-21上午9:43:00
     */
    public abstract Object get(int index);
    /************************************************
     * 方法名称：	clean
     * 说明：		清空数据
     * 创建时间：	2015-3-29
     * 作者：		jkwen
     ***********************************************/
    public abstract void clean();
}
