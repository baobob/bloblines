package org.bloblines.data.battle;

public class Log {

	public enum Type {
		INFO, SKILL, DAMAGE, HEAL, PASSIVE
	}

	public Type type;
	public String message;

	public Log(Type type, String msg) {
		this.type = type;
		this.message = msg;
	}
}
