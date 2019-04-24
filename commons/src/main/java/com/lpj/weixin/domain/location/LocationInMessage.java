package com.lpj.weixin.domain.location;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.lpj.weixin.domain.InMessage;

@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class LocationInMessage extends InMessage{

	private static final long serialVersionUID = 1L;

	@XmlElement(name="Location_X")
	private String location_X;
	
	@XmlElement(name="Location_Y")
	private String location_Y;
	
	@XmlElement(name="Scale")
	private String scale;
	
	@XmlElement(name="Label")
	private String label;

	public String getLocation_X() {
		return location_X;
	}

	public void setLocation_X(String location_X) {
		this.location_X = location_X;
	}

	public String getLocation_Y() {
		return location_Y;
	}

	public void setLocation_Y(String location_Y) {
		this.location_Y = location_Y;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	@Override
	public String toString() {
		return "LocationInMessage [location_X=" + location_X + ", location_Y=" + location_Y + ", scale=" + scale
				+ ", label=" + label + ", getToUserName()=" + getToUserName() + ", getFromUserName()="
				+ getFromUserName() + ", getCreateTime()=" + getCreateTime() + ", getMsgType()=" + getMsgType()
				+ ", getMsgId()=" + getMsgId() + "]";
	}
}
