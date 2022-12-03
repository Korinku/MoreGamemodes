package me.korinku.moga.utils;

public class Utils {

	public static String SOUTH = "SOUTH";
	public static String EAST = "EAST";
	public static String NORTH = "NORTH";
	public static String WEST = "WEST";
	public static String UP = "UP";
	public static String DOWN = "DOWN";
	public static String UNKNOWN = "UNKNOWN";

	public static String getDirection(float yaw) {
		String returnString = "UNKNOWN";
		if (yaw > -44 && yaw < 44)
			returnString = SOUTH;
		if (yaw > 48 && yaw < 135)
			returnString = WEST;
		if (yaw < -46 && yaw > -136)
			returnString = EAST;
		if (yaw > 136 && yaw < 180 || yaw < -136 && yaw > -180)
			returnString = NORTH;

		return returnString;
	}

	public static String getUpOrDown(float pitch) {
		String returnString = "UNKNOWN";
		if (pitch > 70)
			returnString = DOWN;
		if (pitch < -70)
			returnString = UP;

		return returnString;
	}
}
