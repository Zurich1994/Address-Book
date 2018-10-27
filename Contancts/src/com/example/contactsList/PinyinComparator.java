package com.example.contactsList;

import java.util.Comparator;

import com.example.utils.ContactInfo;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<ContactInfo> {

	public int compare(ContactInfo o1, ContactInfo o2) {
		if (o1.getPinyin().equals("@")
				|| o2.getPinyin().equals("#")) {
			return -1;
		} else if (o1.getPinyin().equals("#")
				|| o2.getPinyin().equals("@")) {
			return 1;
		} else {
			return o1.getPinyin().compareTo(o2.getPinyin());
		}
	}

}
