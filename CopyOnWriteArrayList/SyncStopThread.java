/**
 * 
 */
package com.xes.effectivejava10;

import java.util.concurrent.TimeUnit;

/** 
 * 此类描述的是： 
 * @author: jiangrui
 * @version: 2014-12-1 下午03:09:45 
 */
public class SyncStopThread {
	
	private static boolean stopRequested;
	
	private static synchronized void  requestStop(){
		
		stopRequested = true;
	}
	
	public static synchronized boolean stopRequested(){
		
		return stopRequested;
		
	} 
	
	public static void main(String[] args)throws InterruptedException{
		
		Thread backgroundThread = new Thread(new Runnable(){
			public void run(){
				int i = 0 ;
				
				while(!stopRequested()){
					i++;
				}
				
				System.out.println("Thread end");
			}			
			
		});
		
		backgroundThread.start();
		
		TimeUnit.SECONDS.sleep(1);
		
		requestStop();
		
	}
}
