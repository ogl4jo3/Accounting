package ogl4jo3.shaowei.ogl4jo3.accounting.common.expenses;

/**
 * 支出
 * Created by ogl4jo3 on 2017/7/26.
 */

public class Expenses {

	private int id;             //ID
	private int price;          //金額
	private int categoryId;     //類別ID
	private String accountName;      //帳戶名稱
	private String description; //描述
	private String recordTime;  //紀錄時間

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public String getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}

	public String toString() {
		return "id:" + id + ", price:" + price + ", categoryId:" + categoryId + ", description:" +
				description + ", accountName:" + accountName + ", recordTime:" + recordTime;
	}
}
