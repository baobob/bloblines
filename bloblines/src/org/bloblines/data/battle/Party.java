package org.bloblines.data.battle;

import java.util.ArrayList;
import java.util.List;

/**
 * Group of characters in a battle
 * 
 */
public class Party {

	public String name;

	public List<Character> characters = new ArrayList<>();

	public Party(String name) {
		this.name = name;
	}
}
