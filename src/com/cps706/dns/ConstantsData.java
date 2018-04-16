package com.cps706.localdns;

/*
 * Class that holds constants inside 
 */
public class ConstantsData 
{
	public static enum DNS_RECORD_TYPE{
		
		A("A"),
		CNAME("CNAME"),
		NS("NS"),
		MX("MX"),
		R("R");
		
		String type;
		DNS_RECORD_TYPE(String type)
		{
			this.type = type;
		}
	}
}
