package it.gius.pePpe.data.structures;



import it.gius.data.structures.IdArrayLinkList;
import it.gius.data.structures.IdArrayListA;
import it.gius.data.structures.IdList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PerformanceIdListsMain {

	/**
	 * @param args
	 */
	public static final int ITERATIONS = 20000;
	public static final int FIRSTS_TO_SKIP = 5000;


	public static final int CREATION_ITERATIONS = 1;
	public static final int FIRSTS_TO_SKIP_CREATION = 4;

	public static final short ARRAY_DIM = 2000;

	public static final String fileName = "src\\test\\resources\\listsPerformance.csv";

	public static void main(String[] args) {

		System.out.println(System.getProperty("user.dir"));

		double averageIdList = 0;
		long numIterations = 0;

		MockListsElement array[] = new MockListsElement[Short.MAX_VALUE-1];

		for(int i=0; i< ARRAY_DIM; i++)
		{
			array[i] = new MockListsElement(""+i);
		}



		IdArrayListA<MockListsElement> listA = null;

		double averageIdArrayListA = 0;

		for(int j= 0; j< CREATION_ITERATIONS; j++)
		{

			listA = new IdArrayListA<MockListsElement>(MockListsElement.class, 6);

			long startTime = System.currentTimeMillis();

			for(int i=0; i< ARRAY_DIM; i++)
			{
				listA.addItem(array[i]);
			}

			long stopTime = System.currentTimeMillis();

			if(j > FIRSTS_TO_SKIP_CREATION)
			{
				averageIdArrayListA += averageIdArrayListA*numIterations +  (stopTime - startTime);
				numIterations++;
				averageIdArrayListA = averageIdArrayListA / (double)numIterations;
			}

		}


		System.out.println("IdArrayListA average time to fill it: " + averageIdArrayListA);


		List<MockListsElement> list = null;
		double averageJavaList = 0;

		for(int j= 0; j< CREATION_ITERATIONS; j++)
		{

			list = new ArrayList<MockListsElement>();

			long startTime = System.currentTimeMillis();

			for(int i=0; i< ARRAY_DIM; i++)
			{
				list.add(array[i]);
			}

			long stopTime = System.currentTimeMillis();

			if(j > FIRSTS_TO_SKIP_CREATION)
			{
				averageJavaList += averageJavaList*numIterations +  (stopTime - startTime);
				numIterations++;
				averageJavaList = averageJavaList / (double)numIterations;
			}


		}



		System.out.println("java.util.List average time to fill it: " + averageJavaList);

		IdArrayLinkList<MockListsElement> idList = null;

		for(int j=0; j< CREATION_ITERATIONS; j++)
		{
			idList = new IdArrayLinkList<MockListsElement>(MockListsElement.class, 6);

			long startTime = System.currentTimeMillis();

			for(int i=0; i< ARRAY_DIM; i++)
			{
				idList.add(array[i]);
			}

			long stopTime = System.currentTimeMillis();

			if(j > FIRSTS_TO_SKIP_CREATION)
			{
				averageIdList += averageIdList*numIterations +  (stopTime - startTime);
				numIterations++;
				averageIdList = averageIdList / (double)numIterations;
			}


		}


		System.out.println("IdList average time to fill it: " + averageIdList);

	

		System.out.println();

		double averageIdArrayListImplicit = 0;
		numIterations = 0;

		for(int i= 0; i< ITERATIONS; i++)
		{

			long startTime = System.currentTimeMillis();

			for(@SuppressWarnings("unused") MockListsElement el : idList)
			{}

			long stopTime = System.currentTimeMillis();

			if(i > FIRSTS_TO_SKIP)
			{
				averageIdArrayListImplicit += averageIdArrayListImplicit*numIterations +  (stopTime - startTime);
				numIterations++;
				averageIdArrayListImplicit = averageIdArrayListImplicit / (double)numIterations;
			}
		}


		double averageIdArrayListAFor =0;
		numIterations = 0;


		for(int i= 0; i< ITERATIONS; i++)
		{

			long startTime = System.currentTimeMillis();


			short currentId = listA.rootNode;

			for(int j=0; j< listA.size();j++)
			{
				MockListsElement el = listA.a_list[currentId];

				currentId = el.next;
			}

			long stopTime = System.currentTimeMillis();

			if(i > FIRSTS_TO_SKIP)
			{
				averageIdArrayListAFor += averageIdArrayListAFor*numIterations +  (stopTime - startTime);
				numIterations++;
				averageIdArrayListAFor = averageIdArrayListAFor / (double)numIterations;
			}
		}



		System.out.println("IdList (implicit iterator) average time: " + averageIdArrayListImplicit + " ms");
		
		System.out.println("IdArrayListA (for loop) average time: " + averageIdArrayListAFor + " ms");



		double averageIdArrayListExplicit = 0;
		numIterations = 0;

		for(int i= 0; i< ITERATIONS; i++)
		{

			long startTime = System.currentTimeMillis();

			Iterator<MockListsElement> iterator = idList.iterator();
			while(iterator.hasNext())
			{
				@SuppressWarnings("unused")
				MockListsElement el = iterator.next();
			}

			long stopTime = System.currentTimeMillis();

			if(i > FIRSTS_TO_SKIP)
			{
				averageIdArrayListExplicit += averageIdArrayListExplicit*numIterations +  (stopTime - startTime);
				numIterations++;
				averageIdArrayListExplicit = averageIdArrayListExplicit / (double)numIterations;
			}
		}


		double averageIdArrayListAWhile =0;
		numIterations = 0;


		for(int i= 0; i< ITERATIONS; i++)
		{

			long startTime = System.currentTimeMillis();


			short currentId = listA.rootNode;


			while(currentId != IdArrayListA.NULL_NODE)
			{
				MockListsElement el = listA.a_list[currentId];

				currentId = el.next;
			}

			long stopTime = System.currentTimeMillis();

			if(i > FIRSTS_TO_SKIP)
			{
				averageIdArrayListAWhile += averageIdArrayListAWhile*numIterations +  (stopTime - startTime);
				numIterations++;
				averageIdArrayListAWhile = averageIdArrayListAWhile / (double)numIterations;
			}
		}

		System.out.println("IdList (explicit iterator) average time: " + averageIdArrayListExplicit + " ms");
		System.out.println("IdArrayListA (while loop) average time: " + averageIdArrayListAWhile + " ms");


		double averageIdArrayListGet = 0;
		numIterations = 0;

		for(int i= 0; i< ITERATIONS; i++)
		{

			long startTime = System.currentTimeMillis();

			short currentId = idList.getFirst();
			while(currentId != IdList.NULL_NODE)
			{
				@SuppressWarnings("unused")
				MockListsElement el = idList.get(currentId);

				currentId = idList.getNext(currentId);
			}

			long stopTime = System.currentTimeMillis();

			if(i > FIRSTS_TO_SKIP)
			{
				averageIdArrayListGet += averageIdArrayListGet*numIterations +  (stopTime - startTime);
				numIterations++;
				averageIdArrayListGet = averageIdArrayListGet / (double)numIterations;
			}
		}

		averageJavaList = 0;
		numIterations = 0;

		for(int i= 0; i< ITERATIONS; i++)
		{

			long startTime = System.currentTimeMillis();

			for(@SuppressWarnings("unused") MockListsElement el : list)
			{}

			long stopTime = System.currentTimeMillis();

			if(i > FIRSTS_TO_SKIP)
			{
				averageJavaList += averageJavaList*numIterations +  (stopTime - startTime);
				numIterations++;
				averageJavaList = averageJavaList / (double)numIterations;
			}
		}

		System.out.println("java.util.list (implicit iterator) average time: " + averageJavaList + " ms");


		System.out.println("IdList (getFirst, getNext) average time: " + averageIdArrayListGet + " ms");
		
		FileWriter outFile = null;
		File file = new File(fileName); 

		try {
			outFile = new FileWriter(file,true);
		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(1);
		}


		if(file.length() == 0)
		try {
			outFile.write("Array-Size,IdArrayList-implicit-iterator,IdArrayList-explicit-iterator," +
					"IdArrayList-getFirst-getNext-,IdArrayListA-for,IdArrayListA-while" +
					",java.util.ArrayList\n");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		try {
			outFile.append(ARRAY_DIM +","+averageIdArrayListImplicit+ "," + averageIdArrayListExplicit + "," + averageIdArrayListGet +","+
					 averageIdArrayListAFor +"," + averageIdArrayListAWhile + "," + averageJavaList +"\n");
			outFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
