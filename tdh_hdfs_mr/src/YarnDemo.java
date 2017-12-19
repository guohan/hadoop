package com.kit.hadoop;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapred.YARNRunner;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ContainerId;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.client.api.impl.YarnClientImpl;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.server.api.ResourceTracker;
import org.apache.hadoop.yarn.server.api.ServerRMProxy;
import org.apache.hadoop.yarn.server.nodemanager.Context;
import org.apache.hadoop.yarn.server.nodemanager.NodeManager;
import org.apache.hadoop.yarn.server.nodemanager.containermanager.application.Application;
import org.apache.hadoop.yarn.server.nodemanager.containermanager.application.ApplicationEvent;
import org.apache.hadoop.yarn.server.nodemanager.containermanager.application.ApplicationState;
import org.apache.hadoop.yarn.server.nodemanager.containermanager.container.Container;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class YarnDemo {
	/*
	 * get running applications
	 */
	public static List<ApplicationId> getRunningApplications() {
		List<ApplicationId> runningApplications = new ArrayList<ApplicationId>();
		NodeManager nmManager = new NodeManager();
		Context context = nmManager.getNMContext();
		runningApplications.addAll(context.getApplications().keySet());
		return runningApplications;
	}
	/*
	 * get node's cpu and mem
	 */
	public void getCpuAndMem() {
		Configuration conf = new Configuration();
		int memoryMb = conf.getInt(YarnConfiguration.NM_PMEM_MB,
				YarnConfiguration.DEFAULT_NM_PMEM_MB);

		int virtualCores = conf.getInt(YarnConfiguration.NM_VCORES,
				YarnConfiguration.DEFAULT_NM_VCORES);
	}
	/*
	 * 
	 */
	protected ResourceTracker getRMClient() throws IOException {
		Configuration conf = new Configuration();
		return ServerRMProxy.createRMProxy(conf, ResourceTracker.class);
	}

	public static void main(String[] args) throws YarnException, IOException {
		Configuration configuration = new Configuration();
		/*
		 * kerberos begin
		 */
		YARNRunner yarnRunner = new YARNRunner(configuration);
		
		try {
			org.apache.hadoop.mapreduce.JobStatus[] jsall = yarnRunner.getAllJobs();
			System.out.println("++++++++++++++++++++++++");
			
			for (org.apache.hadoop.mapreduce.JobStatus js:jsall) {
				System.out.println("++++"+js.getJobID());
				System.out.println("++++++"+js.getUsedMem());
				System.out.println("++++++"+js.getJobName());
				js.getNeededMem();
				js.getUsedMem();
				js.getFinishTime();
				js.getStartTime();
				Job job= new Job();
				job.setJobID(js.getJobID());//你要kill掉的jobid 
				job.killJob();
			}
			
		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
Application a= new Application() {
	
	@Override
	public void handle(ApplicationEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getUser() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Map<ContainerId, Container> getContainers() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ApplicationState getApplicationState() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ApplicationId getAppId() {
		// TODO Auto-generated method stub
		return null;
	}
};

		YarnClient yClient = YarnClientImpl.createYarnClient();
		yClient.init(configuration);
		yClient.start();
		System.out.println(yClient.getApplications().size());// 获取 app个数
		System.out
				.println(yClient.getYarnClusterMetrics().getNumNodeManagers());// 获取节点数
		yClient.stop();
	}
}
