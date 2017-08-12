package com.example.ogl4jo3.accounting.setting.accountmanagement;

import com.example.ogl4jo3.accounting.R;

/**
 * 帳戶
 * Created by ogl4jo3 on 2017/8/10.
 */

public class Account {

	public static final int CATEGORY_CASH = 0;  //類別:現金
	public static final String CATEGORY_CASH_STR = "現金";  //類別:現金
	public static final int CATEGORY_CARD = 1;  //類別:卡片
	public static final String CATEGORY_CARD_STR = "卡片";  //類別:卡片
	public static final int CATEGORY_BANK = 2;  //類別:銀行
	public static final String CATEGORY_BANK_STR = "銀行";  //類別:銀行

	private int id;             //ID
	private String name;        //帳戶名稱
	private int startingAmount; //起始金額
	private int category;       //帳戶類別，0:現金、1:卡片、2:銀行
	private boolean defaultAccount;//是否為預設帳戶
	private Integer budgetPrice;    //預算金額
	private Integer budgetNotice;   //預算低於多少百分比提醒
	private int balance;        //帳戶餘額

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStartingAmount() {
		return startingAmount;
	}

	public void setStartingAmount(int startingAmount) {
		this.startingAmount = startingAmount;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public boolean isDefaultAccount() {
		return defaultAccount;
	}

	public void setDefaultAccount(boolean defaultAccount) {
		this.defaultAccount = defaultAccount;
	}

	public Integer getBudgetPrice() {
		return budgetPrice;
	}

	public void setBudgetPrice(Integer budgetPrice) {
		this.budgetPrice = budgetPrice;
	}

	public Integer getBudgetNotice() {
		return budgetNotice;
	}

	public void setBudgetNotice(Integer budgetNotice) {
		this.budgetNotice = budgetNotice;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public int getCategoryIcon() {
		int accountCategoryIcon = 0;
		switch (this.getCategory()) {
			case Account.CATEGORY_CASH:
				accountCategoryIcon = R.drawable.ic_account_category_cash;
				break;
			case Account.CATEGORY_CARD:
				accountCategoryIcon = R.drawable.ic_account_category_card;
				break;
			case Account.CATEGORY_BANK:
				accountCategoryIcon = R.drawable.ic_account_category_bank;
				break;
		}
		return accountCategoryIcon;
	}

	public String getCategoryName() {
		String accountCategoryName = "";
		switch (this.getCategory()) {
			case Account.CATEGORY_CASH:
				accountCategoryName = CATEGORY_CASH_STR;
				break;
			case Account.CATEGORY_CARD:
				accountCategoryName = CATEGORY_CARD_STR;
				break;
			case Account.CATEGORY_BANK:
				accountCategoryName = CATEGORY_BANK_STR;
				break;
		}
		return accountCategoryName;
	}
}
