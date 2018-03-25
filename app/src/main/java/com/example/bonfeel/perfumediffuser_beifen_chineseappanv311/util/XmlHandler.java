package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Citys;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Countys;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Provinces;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Author:FunFun
 * Function:SAX的xml解析handler
 * Date:2016/4/12 9:53
 */
public class XmlHandler extends DefaultHandler
{
    private List<Provinces> listProvince;
    private Provinces province;
    private List<Citys> listCity;
    private Citys city;
    private List<Countys> listCounty;
    private Countys county;


    /**
     * 构造方法名称：	XmlHandler
     * 创建时间：		2015-2-2上午10:43:03
     * 方法描述：		TODO
     */
    public XmlHandler()
    {
        super();
        listProvince = new ArrayList<Provinces>();
        listCity = new ArrayList<Citys>();
        listCounty = new ArrayList<Countys>();
    }

    /**
     * 返回值： listProvince
     */
    public List<Provinces> getListProvince()
    {
        return listProvince;
    }

    /**
     * 参数设置： listProvince to set
     */
    public void setListProvince(List<Provinces> listProvince)
    {
        this.listProvince = listProvince;
    }

    /**
     * 返回值： listCity
     */
    public List<Citys> getListCity()
    {
        return listCity;
    }

    /**
     * 参数设置： listCity to set
     */
    public void setListCity(List<Citys> listCity)
    {
        this.listCity = listCity;
    }

    /**
     * 返回值： listCounty
     */
    public List<Countys> getListCounty()
    {
        return listCounty;
    }

    /**
     * 参数设置： listCounty to set
     */
    public void setListCounty(List<Countys> listCounty)
    {
        this.listCounty = listCounty;
    }

    /* (非Javadoc)
     * 方法名称：	startDocument
     * 方法描述：	TODO
     * 重写部分：	@see org.xml.sax.helpers.DefaultHandler#startDocument()
     */
    @Override
    public void startDocument() throws SAXException
    {

        super.startDocument();
    }

    /* (非Javadoc)
     * 方法名称：	endDocument
     * 方法描述：	TODO
     * 重写部分：	@see org.xml.sax.helpers.DefaultHandler#endDocument()
     */
    @Override
    public void endDocument() throws SAXException
    {

        super.endDocument();
    }

    /* (非Javadoc)
     * 方法名称：	startElement
     * 方法描述：	TODO
     * 重写部分：	@see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException
    {
        //解析省份数据
        if ( qName.equals("Province") )
        {
            province = new Provinces();
            province.setId(Integer.parseInt(attributes.getValue("ID")));
            province.setName(attributes.getValue("ProvinceName"));
        }
        else
        {
            //解析城市数据
            if ( qName.equals("City") )
            {
                city = new Citys();
                city.setId(Integer.parseInt(attributes.getValue("ID")));
                city.setName(attributes.getValue("CityName"));
                city.setpId(Integer.parseInt(attributes.getValue("PID")));
            }
            else
            {
                //解析区县数据
                if ( qName.equals("District") )
                {
                    county = new Countys();
                    county.setId(Integer.parseInt(attributes.getValue("ID")));
                    county.setName(attributes.getValue("DistrictName"));
                    county.setcId(Integer.parseInt(attributes.getValue("CID")));
                }
            }
        }
    }

    /* (非Javadoc)
     * 方法名称：	endElement
     * 方法描述：	TODO
     * 重写部分：	@see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException
    {
        //解析省份数据
        if ( qName.equals("Province") )
        {
            listProvince.add(province);
        }
        else
        {
            //解析城市数据
            if ( qName.equals("City") )
            {
                listCity.add(city);
            }
            else
            {
                //解析区县数据
                if ( qName.equals("District") )
                {
                    listCounty.add(county);
                }
            }
        }
    }

    /* (非Javadoc)
     * 方法名称：	characters
     * 方法描述：	TODO
     * 重写部分：	@see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
     */
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException
    {

        super.characters(ch, start, length);
    }

}
