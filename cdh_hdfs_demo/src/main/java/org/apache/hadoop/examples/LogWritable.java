package org.apache.hadoop.examples;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class LogWritable implements Writable
{
	
	private Text userIp,timestamp,request;
	

	private IntWritable respsize,status;
	public LogWritable()
	{
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public void write(DataOutput out) throws IOException
	{
		// TODO Auto-generated method stub
		
		userIp.write(out);
		
	}

	@Override
	public void readFields(DataInput in) throws IOException
	{
		// TODO Auto-generated method stub
		userIp.readFields(in);
	}
	
	public Text getUserIp()
	{
		return userIp;
	}

	public void setUserIp(Text userIp)
	{
		this.userIp = userIp;
	}

	public Text getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(Text timestamp)
	{
		this.timestamp = timestamp;
	}

	public Text getRequest()
	{
		return request;
	}

	public void setRequest(Text request)
	{
		this.request = request;
	}

	public IntWritable getRespsize()
	{
		return respsize;
	}

	public void setRespsize(IntWritable respsize)
	{
		this.respsize = respsize;
	}

	public IntWritable getStatus()
	{
		return status;
	}

	public void setStatus(IntWritable status)
	{
		this.status = status;
	}

}
