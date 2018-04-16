package com.cps706.localdns;

import java.util.ArrayList;

public class DNSEntity 
{
	ArrayList<DNSRecord> dnsRecords;
	
	public DNSEntity()
	{
		this.dnsRecords = new ArrayList<DNSRecord>();
	}
	
	public void addDnsRecord(DNSRecord dnsRecord)
	{
		dnsRecords.add(dnsRecord);
	}
}
