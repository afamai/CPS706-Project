package com.cps706.localdns;

import com.cps706.localdns.ConstantsData.DNS_RECORD_TYPE;

public class DNSRecord 
{
	private DNS_RECORD_TYPE dnsRecordType;
	private String name;
	private String value;
	
	public DNSRecord(String name, String value, DNS_RECORD_TYPE dnsRecordType)
	{
		this.name = name;
		this.value = value;
		this.dnsRecordType = dnsRecordType;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setValue(String value)
	{
		this.value = value;
	}
	
	public String getValue()
	{
		return value;
	}
	
	public DNS_RECORD_TYPE getDnsRecordType()
	{
		return dnsRecordType;
	}
	
	public void setDnsRecordType(DNS_RECORD_TYPE dnsRecordType)
	{
		this.dnsRecordType = dnsRecordType;
	}
	
	public String toString()
	{
		return "DNSRecord: ["
				+"name="+name
				+",value="+value
				+",dnsRecordType="+dnsRecordType
				+"]";
	}
	
	
}
