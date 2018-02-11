package com.paulz.hhb.model.wrapper;

import com.paulz.hhb.model.SalesmanCount;
import com.paulz.hhb.model.Team;

import java.util.ArrayList;
import java.util.List;

public class SalesmanCountWraper implements BeanWraper<SalesmanCount>{
	
	/**
	 * 
	 */
    public List<SalesmanCount> list; //  当前页面所有的beans  order
    public int page_count=Integer.MAX_VALUE;//页码总数
    public int total;//条数


    @Override
    public int getItemsCount(){
    	return list==null?0:list.size();
    }
    
    @Override
    public List<SalesmanCount> getItems(){
    	if(list==null){
            list=new ArrayList<>();
    	}
    	return list;
    }
    
    @Override
    public int getTotalPage(){
    	return page_count;
    }
    
}
