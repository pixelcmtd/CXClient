package de.chrissx.util;

public class Random {

	static final java.util.Random rand = new java.util.Random();
	
	public static int rand(int max) {
		return rand.nextInt(max);
	}
	
	public static int rand() {
		return rand.nextInt();
	}
	
	public static boolean randBool() {
		return rand.nextBoolean();
	}
	
	public static long randLong() {
		return rand.nextLong();
	}
	
	public static float randFloat() {
		return rand.nextFloat();
	}
	
	public static double randDouble() {
		return rand.nextDouble();
	}
}
