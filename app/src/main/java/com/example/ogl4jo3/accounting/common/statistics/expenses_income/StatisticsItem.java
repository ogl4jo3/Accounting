package com.example.ogl4jo3.accounting.common.statistics.expenses_income;

/**
 * 支出、收入共用  統計Item
 * Created by ogl4jo3 on 2017/8/8.
 */

public class StatisticsItem {
	private int categoryId;  //分類ID
	private int icon;        //分類圖示
	private String name;     //分類名稱
	private int percentage;  //百分比
	private int price;       //金額
	private String fromDateStr; //開始日期
	private String toDateStr;   //結束日期
	private String accountName; //帳戶名稱

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getFromDateStr() {
		return fromDateStr;
	}

	public void setFromDateStr(String fromDateStr) {
		this.fromDateStr = fromDateStr;
	}

	public String getToDateStr() {
		return toDateStr;
	}

	public void setToDateStr(String toDateStr) {
		this.toDateStr = toDateStr;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
}
