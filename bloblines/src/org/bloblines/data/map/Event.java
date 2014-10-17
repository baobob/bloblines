package org.bloblines.data.map;

import java.util.HashSet;
import java.util.Set;

public class Event {
	public Set<Requirement> requirements = new HashSet<Requirement>();
	public Action launchedAction;
}
