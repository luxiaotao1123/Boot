package com.cool.boot;

import com.cool.boot.entity.Task;
import com.cool.boot.service.TaskServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BootApplicationTests {

	@Autowired
	private TaskServer taskServer;

	@Test
	public void contextLoads() {
		Task task = new Task();
		task.setTaskName("demo:231321");
		task.setExecuteTime("0 48 13 16 5 ? ");
		task.setParams("{'msg':'demo:231321','code':500}");
		taskServer.saveTask(task);
		System.out.println("ok");
	}

}
