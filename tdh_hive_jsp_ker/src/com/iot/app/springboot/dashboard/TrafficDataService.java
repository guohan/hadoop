package com.iot.app.springboot.dashboard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.iot.app.springboot.dao.TotalTrafficDataRepository;
import com.iot.app.springboot.dao.entity.TotalTrafficData;
import com.iot.app.springboot.vo.Response;

/**
 * Service class to send traffic data messages to dashboard ui at fixed interval using web-socket.
 * 
 * @author abaghel
 *
 */
@Service
public class TrafficDataService {
	private static final Logger logger = Logger.getLogger(TrafficDataService.class);
	
	@Autowired
	private SimpMessagingTemplate template;
	
	@Autowired
	private TotalTrafficDataRepository totalRepository;
	
	
	private static DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	//Method sends traffic data message in every 5 seconds.
	@Scheduled(fixedRate = 5000)
	public void trigger() {
		List<TotalTrafficData> totalTrafficList = new ArrayList<TotalTrafficData>();
		//Call dao methods
		totalRepository.findTrafficDataByDate(sdf.format(new Date())).forEach(e -> totalTrafficList.add(e));	
		//prepare response
		Response response = new Response();
		response.setTotalTraffic(totalTrafficList);
		logger.info("Sending to UI "+response);
		//send to ui
		this.template.convertAndSend("/topic/trafficData", response);
	}
	
}
