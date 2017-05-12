package de.kimrudolph.tutorials.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * 
 * @author gh
 * @description
 * 模拟发送具体的数据
 *
 */
@Component
public class RandomDataGenerator implements
    ApplicationListener<BrokerAvailabilityEvent> {

    private final MessageSendingOperations<String> messagingTemplate;

    @Autowired
    public RandomDataGenerator(
        final MessageSendingOperations<String> messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onApplicationEvent(final BrokerAvailabilityEvent event) {
    }

    @Scheduled(fixedDelay = 5000)
    public void sendDataUpdates() {

//        this.messagingTemplate.convertAndSend(
//            "/data", new Random().nextInt(100));
    	Response response = new Response();
		response.setDataList(getData());
		response.setAreaList(getDataByArea());
        this.messagingTemplate.convertAndSend(
                "/data",response );
        
//        this.messagingTemplate.convertAndSend(
//                "/dataArea",response );


    }
    
    /**
     * 需求涉及两部分 1 生产小球颜色 状态 地区信息 id等信息
     * 2 小球滑动过程如何进行交互
     * @return
     */
    //获取各个集合的数据
    private List<Data> getDataByArea(){
//    	String[] areas={"天河"};
    	String[] colors={"red","green" ,"yellow"};
    	String[] status={"new","ing" ,"finish"};
    	String[] types={"95588","app" ,"web"};
    	String[] busitypes={"投诉","查询" ,"办理业务"};
    	List<Data> listData= new ArrayList<>();
    	
    	for(int i=0;i<20;i++){
    		int index=(int)(Math.random()*colors.length);
        	String randcolors = colors[index];
        	String randstatus = status[index];
        	String randtypes = types[index];
        	String randbtypes = busitypes[index];
    		Data data = new Data();
        	data.setArea("天河");
        	data.setColor(randcolors);
        	data.setId(String.valueOf(i));//UUID.randomUUID().toString()
        	data.setStatus(randstatus);
        	data.setChannelType(randtypes);
        	data.setBusinessType(randbtypes);
        	listData.add(data);
    	}
    	return listData;
//    	return  new Random().nextInt(100);
    }
    
    
    
    private List<Data> getData(){
    	String[] areas={"天河","荔湾" ,"番禺"};
    	String[] colors={"red","green" ,"yellow"};
    	String[] status={"new","ing" ,"finish"};
    	String[] types={"95588","app" ,"web"};
    	String[] busitypes={"投诉","查询" ,"办理业务"};
    	List<Data> listData= new ArrayList<>();
    	
    	for(int i=0;i<20;i++){
    		int index=(int)(Math.random()*colors.length);
        	String randcolors = colors[index];
        	String randareas = areas[index];
        	String randstatus = status[index];
        	String randtypes = types[index];
        	String randbtypes = busitypes[index];
    		Data data = new Data();
        	data.setArea(randareas);
        	data.setColor(randcolors);
        	data.setId(String.valueOf(i));//UUID.randomUUID().toString()
        	data.setStatus(randstatus);
        	data.setChannelType(randtypes);
        	data.setBusinessType(randbtypes);
        	listData.add(data);
    	}
    	return listData;
    }
}