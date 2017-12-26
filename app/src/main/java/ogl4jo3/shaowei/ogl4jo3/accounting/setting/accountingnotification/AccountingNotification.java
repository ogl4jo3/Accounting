package ogl4jo3.shaowei.ogl4jo3.accounting.setting.accountingnotification;

/**
 * 記帳通知
 * Created by ogl4jo3 on 2017/12/14.
 */

public class AccountingNotification {

	private int id; //ID
	private String notificationName;    //通知名稱
	private int categoryId;             //類別ID
	private String categoryName;        //類別名稱
	private int categoryIcon;           //類別圖示
	private int notificationHour;       //通知時間,時:00-23
	private int notificationMinute;     //通知時間,分:00-59
	private boolean isOn;               //通知是否開啟

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNotificationName() {
		return notificationName;
	}

	public void setNotificationName(String notificationName) {
		this.notificationName = notificationName;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getCategoryIcon() {
		return categoryIcon;
	}

	public void setCategoryIcon(int categoryIcon) {
		this.categoryIcon = categoryIcon;
	}

	public int getNotificationHour() {
		return notificationHour;
	}

	public void setNotificationHour(int notificationHour) {
		this.notificationHour = notificationHour;
	}

	public int getNotificationMinute() {
		return notificationMinute;
	}

	public void setNotificationMinute(int notificationMinute) {
		this.notificationMinute = notificationMinute;
	}

	public boolean isOn() {
		return isOn;
	}

	public void setOn(boolean on) {
		isOn = on;
	}
}
