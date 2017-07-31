package com.example.ogl4jo3.accounting.setting.categorymanagement;

/**
 * 分類
 * Created by ogl4jo3 on 2017/7/13.
 */

public class Category {

	private int id;         //唯一keyID
	private int orderNum;   //順序編號
	private String name;    //分類名稱
	private int icon;       //分類圖示

	public Category() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public String toString() {
		return "id:" + id+", orderNum:" + orderNum + ", name:" + name + ", icon:" + icon;
	}
}
