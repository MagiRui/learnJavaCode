/**
 * 
 */
package com.xes.effectivejava10;

import java.util.concurrent.TimeUnit;

/** 
 * 此类描述的是： 
 * @author: jiangrui
 * @version: 2014-12-1 下午02:54:06 
 */
public class VolatileStopThread {
	
	
	private static volatile  boolean stopRequested;
	
	public static void main(String[] args) throws InterruptedException{
		
		Thread backgroundThread = new Thread(new Runnable(){
			
			public void run(){
				
				int i = 0 ;
				while(!stopRequested){
					i++;
				}
				
				System.out.println(" Thread End ");
			}
		});
		
		backgroundThread.start();
		
		TimeUnit.SECONDS.sleep(1);
		
		stopRequested = true;
		
	}
	
}
