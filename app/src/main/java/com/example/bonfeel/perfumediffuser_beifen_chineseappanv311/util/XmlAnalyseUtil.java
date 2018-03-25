package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util;

import android.content.res.AssetManager;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Citys;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Countys;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Provinces;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 *
 * Author:FunFun
 * Function:XML解析
 * Date:2016/4/12 9:52
 */
public class XmlAnalyseUtil {
    /**
     * 构造方法名称：	XmlAnalyseUtil
     * 创建时间：		2015-2-2下午12:08:29
     * 方法描述：		TODO
     */
    public XmlAnalyseUtil()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    private static List<Provinces> listProvinces = new ArrayList<Provinces>();
    private static List<Citys> listCitys = new ArrayList<Citys>();
    private static List<Countys> listCountys = new ArrayList<Countys>();

    private static AssetManager assetManager  = ApplicationUtil.getContext().getAssets();
    /**
     * 方法名称：	parseXMLWithSAX
     * 方法描述：	解析xml文件，获取内容
     * 参数：			@param xmlData
     * 返回值类型：	void
     * 创建时间：	2015-2-2上午10:26:05
     */

    public static void parseXMLWithSAX(String xmlData)
    {
        try
        {
            InputStream inStream = assetManager.open(xmlData);
            //解析xml的工厂对象
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            //解析xml
            try
            {
                SAXParser saxParser = saxParserFactory.newSAXParser();
                XmlHandler xmlHandler = new XmlHandler();
                //解析数据
                saxParser.parse(inStream, xmlHandler);
                inStream.close();
                if ( xmlData.equals("provinces.xml") )
                {
                    listProvinces = xmlHandler.getListProvince();
                }
                else
                {
                    if ( xmlData.equals("cities.xml") )
                    {
                        listCitys = xmlHandler.getListCity();
                    }
                    else
                    {
                        if ( xmlData.equals("districts.xml") )
                        {
                            listCountys = xmlHandler.getListCounty();
                        }
                        else
                        {}
                    }
                }
            }
            catch ( ParserConfigurationException e )
            {
                e.printStackTrace();
            }
            catch ( SAXException e )
            {
                e.printStackTrace();
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }
    /**
     * 返回值： listProvinces
     */
    public static List<Provinces> getListProvinces()
    {
        return listProvinces;
    }
    /**
     * 返回值： listCitys
     */
    public static List<Citys> getListCitys()
    {
        return listCitys;
    }
    /**
     * 返回值： listCountys
     */
    public static List<Countys> getListCountys()
    {
        return listCountys;
    }
}
