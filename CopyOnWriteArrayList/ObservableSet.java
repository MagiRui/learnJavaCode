/**
 * 
 */
package com.xes.effectivejava10;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.xes.effectivejava04.ForwardingSet;

interface SetObserver<E>{
	
	public void added(ObservableSet<E> e ,E element);
}





/** 
 * 此类描述的是： 
 * @author: jiangrui
 * @version: 2014-12-1 下午03:40:30 
 */
public class ObservableSet<E> extends ForwardingSet<E> {
	
	public ObservableSet(Set<E> set){
		super(set);
	}
	
	/*
	 * 要将方法的调用移出同步代码块,还用一种更好的办法。自从Java1.5发行版本
	 * 以来，Java类库就提供了一个并发集合ConpyOnWriteArrayList,只需要将，这里的
	 * ArrayList替换为CopyOnWriteArrayList即可避免main方法中的异常和死锁，而且
	 * addObserver,removeObserver发放可以去掉关键字synchronized
	 * 
	 * */
	private final List<SetObserver<E>> observers = new ArrayList<SetObserver<E>>();
	
	public void addObserver(SetObserver<E> observer){
		synchronized(observers){
			
			observers.add(observer);
		}
	}
	
	
	public boolean removeObserver(SetObserver<E> observer){
		
		synchronized(observers){
			return observers.remove(observer);
		}
	}
	
	public void notifyElementAdded(E element){
		
		synchronized(observers){
			for(SetObserver<E> observer : observers){
				
				observer.added(this,element);
			}
		}
		
		/*
		 * 要想避免下面出现的死锁和异常,可以使用一下代码
		 * List<SetObserver<E>> snapshot = null;
		 * synchorinzed(observers){
		 * 	   snapshot = new ArrayList<SetObserver<E>>(observers); 
		 * }
		 * 
		 * for(SetObserver<E> observer : snapshot){
		 * 
		 * 		observer.added(this,element);
		 * }
		 * 
		 * */
	}
	
	@Override
	public boolean add(E element){
		
		boolean added = super.add(element);
		if(added){
			notifyElementAdded(element);
		}
		
		return added;
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c){
		
		boolean result = false;
		for(E element : c){
			result = add(element);
		}
		
		return result;
	}
	
	public static void main(String[] args){
		
		ObservableSet<Integer> set = new ObservableSet<Integer>(new HashSet<Integer>());
		
		/*使用下面的代码会产生死锁：
		Exception in thread "main" java.util.ConcurrentModificationException
		at java.util.AbstractList$Itr.checkForComodification(AbstractList.java:372)
		at java.util.AbstractList$Itr.next(AbstractList.java:343)
		at com.xes.effectivejava10.ObservableSet.notifyElementAdded(ObservableSet.java:57)
		at com.xes.effectivejava10.ObservableSet.add(ObservableSet.java:69)
		at com.xes.effectivejava10.ObservableSet.main(ObservableSet.java:129)*/
		
		set.addObserver( new SetObserver<Integer>(){
			
			public void added(ObservableSet<Integer> s,Integer e){
				System.out.println(e);
				if(e == 23 ){
					s.removeObserver(this);
				}
			}
		});
		
		
//一下代码会造成死锁		
//		set.addObserver(new SetObserver<Integer>(){
//			
//			public void added(final ObservableSet<Integer> s ,Integer e ){
//				
//				System.out.println(e);
//				if(e == 23 ){
//					ExecutorService executor = Executors.newSingleThreadExecutor();
//					final SetObserver<Integer> observer = this;
//					try{
//						executor.submit(new Runnable(){
//							public void run(){
//								
//								s.removeObserver(observer);
//							}
//						}).get();
//					}catch(ExecutionException ex){
//						throw new AssertionError(ex.getCause());
//					}catch(InterruptedException ex){
//						
//						throw new AssertionError(ex.getCause());
//					}finally{
//						executor.shutdown();
//					}
//				}
//			}
//		});
		for(int i = 0 ;i< 100;i++){
			
			set.add(i);
		}
	}
}
