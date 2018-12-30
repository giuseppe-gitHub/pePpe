package it.gius.pePpe.mains;

import java.util.Arrays;

import it.gius.pePpe.data.aabb.EndPoint;

public class TestComparingEndPoint {
	
	public static void main(String[] args) {
		EndPoint[] array = new EndPoint[3];
		
		array[0] = new EndPoint();
		array[0].idShape = 0;
		array[0].min = true;
		array[0].value = 11.1f;
		
		
		array[1] = new EndPoint();
		array[1].idShape = 4;
		array[1].min = false;
		array[1].value = 5.6f;
		
		array[2] = new EndPoint();
		array[2].idShape = 5;
		array[2].min = false;
		array[2].value = 56.6f;
		
		Arrays.sort(array);
		
		for(int i= 0;i < array.length; i++)
			System.out.println(array[i].value);
	}

}
