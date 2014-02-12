package org.bloblines.data.world;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bloblines.data.life.LivingThing;

public class Area {

	/**
	 * List of sub areas contained inside this area.
	 */
	public List<Area> subAreas;

	/**
	 * All registered living things currently in this area.
	 */
	public Set<LivingThing> livingThings;

	/**
	 * Future living things that will get born at the end of turn.
	 */
	public Set<LivingThing> futureLivingThings;

	/**
	 * Future dead things that will disappear at the end of turn.
	 */
	public Set<LivingThing> futureDeadThings;

	/**
	 * Construct an empty area.
	 */
	public Area() {
		subAreas = new ArrayList<>();
		livingThings = new HashSet<>();
		futureLivingThings = new HashSet<>();
		futureDeadThings = new HashSet<>();
	}

	/**
	 * Register a new thing to be added in the list of managed things.
	 * 
	 * @param thing the new thing to add
	 */
	public void register(LivingThing thing) {
		futureLivingThings.add(thing);
	}

	/**
	 * Removes a living thing from the things in this area.
	 * @param thing
	 */
	public void unregister(LivingThing thing) {
		futureDeadThings.add(thing);
	}

	/**
	 * Make all things live in this area and all sub areas.
	 */
	public void live() {
		for (LivingThing livingThing : livingThings) {
			livingThing.live();
		}
		for (Area area : subAreas) {
			area.live();
		}
		// Remove dead things from living things list
		for (LivingThing deadThing : futureDeadThings) {
			livingThings.remove(deadThing);
		}
		futureDeadThings.clear();
		// Add newly born things
		for (LivingThing babyThing : futureLivingThings) {
			livingThings.add(babyThing);
		}
		futureLivingThings.clear();
	}
}
