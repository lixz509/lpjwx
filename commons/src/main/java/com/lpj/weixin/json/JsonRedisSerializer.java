package com.lpj.weixin.json;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lpj.weixin.domain.InMessage;


public class JsonRedisSerializer<T> extends Jackson2JsonRedisSerializer<T> {

	private ObjectMapper objectMapper= new ObjectMapper();
	
	@SuppressWarnings("unchecked")
	public JsonRedisSerializer() {
		super((Class<T>) InMessage.class);
	}
	public T deserialize(byte[] bytes) throws SerializationException{
		ByteArrayInputStream bis =new ByteArrayInputStream(bytes);
		DataInputStream in=new DataInputStream(bis);
		try {
			//读取类名
			int len = in.readInt();
			byte[] classNameBytes=new byte[len];//读取一个整数，这个整数是后面类名的长度
			in.readFully(classNameBytes);
			String className =new String(classNameBytes,"utf-8");
			//Class.forName("com.mysql.jdbc.Driver")   引入包com.mysql.jdbc.Driver
			@SuppressWarnings("unchecked")
			Class<T> cla=(Class<T>) Class.forName(className);
			//len+4 : len是类名的长度，4则是最开始len的长度
			T o=objectMapper.readValue(Arrays.copyOfRange(bytes, len + 4, bytes.length),cla);
			return o;
		} catch (IOException | ClassNotFoundException e) {
			throw new SerializationException(e.getLocalizedMessage(),e);
		}
	}
	
	public byte[] serialize(Object t) throws SerializationException{
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		DataOutputStream out=new DataOutputStream(bos);
		//在写数据的时候，在前面先写上一个数字，用于表示类名长度
		//紧接着写出类名
		try {
			//writerUTF本身先把长度写出去，然后在写内容
//			out.writeUTF(t.getClass().getName());
			String className=t.getClass().getName();
			byte[] classNameBytes=className.getBytes();
			out.writeInt(classNameBytes.length);
			out.write(classNameBytes);
			//最后把它序列化后的内容写出
			out.write(super.serialize(t));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}
}