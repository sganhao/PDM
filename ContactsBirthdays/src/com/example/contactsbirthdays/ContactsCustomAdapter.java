package com.example.contactsbirthdays;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ContactsCustomAdapter extends BaseAdapter{
	
	private int _layout;
	private LayoutInflater _layoutInflater;
	private ContactInfo [] contacts;
	
	public ContactsCustomAdapter(Context ctx, int layout,ContactInfo [] contacts){
		_layout = layout;
		_layoutInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.contacts = contacts;
	}	
	
	@Override
	public int getCount() {
		return contacts.length;
	}

	@Override
	public Object getItem(int i) {
		return getModel(i);
	}

	private ContactInfo getModel(int i) {
		return contacts[i];
	}
	
	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup parent) {
		if(view == null){
			view = _layoutInflater.inflate(_layout, null);
			view.setTag(createViewHolderFor(view));
			bindModel(getModel(i), view.getTag());
		}else{
			bindModel(getModel(i), view.getTag());
		}
		return view;
	}

	private void bindModel(ContactInfo contact, Object viewModelObject) {
		ViewModel model = (ViewModel) viewModelObject;
		if(contact.getImage().toString().equals(""))
			model.photo.setImageResource(R.drawable.ic_launcher);
		else
			model.photo.setImageURI(contact.getImage());
		model.contactName.setText(contact.getName());
		model.contactBirthday.setText(contact.getBirthday());
		model.contactName.setTag(contact.getId());		
	}

	private Object createViewHolderFor(View view) {
		return new ViewModel(view);
	}
}
