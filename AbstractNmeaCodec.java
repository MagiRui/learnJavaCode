package com.java.serverCoding;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public abstract class AbstractNmeaCodec extends Observable{
	
	   public Observer observer;
	   public AbstractNmeaObject abstractNmeaObject;
	   
	   //解码
	   public abstract void decode(String content);
	   
	   //编码
	   public abstract List<String> encode(AbstractNmeaObject obj);

}
