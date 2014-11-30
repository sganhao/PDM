package com.example.newsclass;

import java.io.Serializable;

public class NewsListModel implements Serializable{
	NewItem[] _newItemArr;

	public NewsListModel(NewItem[] newItem){
		_newItemArr = newItem;
	}

	public NewItem getItem(int pos){
		return _newItemArr[pos];
	}
	public NewItem[] getItems() {
		return _newItemArr;
	}
}
