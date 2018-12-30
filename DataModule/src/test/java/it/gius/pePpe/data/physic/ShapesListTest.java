package it.gius.pePpe.data.physic;


import it.gius.data.structures.IdArrayListA;
import it.gius.pePpe.data.MockShape;
import it.gius.pePpe.data.aabb.AABoundaryBox;
import it.gius.pePpe.data.physic.Bind;
import it.gius.pePpe.data.physic.BindInit;
import it.gius.pePpe.data.shapes.Shape;
import junit.framework.TestCase;

@SuppressWarnings("deprecation")
public class ShapesListTest extends TestCase{

	IdArrayListA<ShapesListNode> list = new IdArrayListA<ShapesListNode>(ShapesListNode.class,20);


	public void testAddAndRemove1() {
		Shape shapeA = new MockShape();
		Shape shapeB = new MockShape();
		Shape shapeC = new MockShape();

		ShapesListNode a = new ShapesListNode();
		BindInit bi = new BindInit();
		bi.density = 1;
		bi.shape = shapeA;
		a.bind = new Bind(bi);
		//a.shape = shapeA;
		AABoundaryBox box  =new AABoundaryBox();
		shapeA.computeBox(box);
		a.box = box;
		a.otherData = null;

		int idA = list.addItem(a);

		ShapesListNode b = new ShapesListNode();
		bi = new BindInit();
		bi.density = 1;
		bi.shape = shapeB;
		b.bind = new Bind(bi);
		//b.shape = shapeB;
		box  =new AABoundaryBox();
		shapeB.computeBox(box);
		b.box = box;
		b.otherData = null;

		int idB = list.addItem(b);

		ShapesListNode c = new ShapesListNode();
		bi = new BindInit();
		bi.density = 1;
		bi.shape = shapeC;
		c.bind = new Bind(bi);
		//c.shape = shapeB;
		box  =new AABoundaryBox();
		shapeC.computeBox(box);
		c.box = box;
		c.otherData = null;

		int idC = list.addItem(c);

		if(list.rootNode == list.lastNode)
			fail();

		short curr = list.rootNode;

		for(int i=0; i< list.size(); i++)
		{
			ShapesListNode currNode = list.a_list[curr];

			if(currNode != a && currNode != b && currNode != c)
				fail();

			curr = currNode.next;
		}

		if(list.size() != 3)
			fail();

		list.removeItem(idC);

		if(list.size() != 2)
			fail();

		curr = list.rootNode;

		for(int i=0; i< list.size(); i++)
		{
			ShapesListNode currNode = list.a_list[curr];

			if(currNode != a && currNode != b && currNode != c)
				fail();

			curr = currNode.next;
		}

		list.removeItem(idA);

		if(list.size() != 1)
			fail();

		list.removeItem(idB);

		if(list.size() != 0)
			fail();


	}

	public void testAddAndRemove2() {
		Shape shapeA = new MockShape();
		Shape shapeB = new MockShape();
		Shape shapeC = new MockShape();
		Shape shapeD = new MockShape();
		Shape shapeE = new MockShape();

		ShapesListNode a = new ShapesListNode();
		BindInit bi = new BindInit();
		bi.density = 1;
		bi.shape = shapeA;
		a.bind = new Bind(bi);
		//a.shape = shapeA;
		AABoundaryBox box  =new AABoundaryBox();
		shapeA.computeBox(box);
		a.box = box;
		a.otherData = null;

		int idA = list.addItem(a);

		ShapesListNode b = new ShapesListNode();
		bi = new BindInit();
		bi.density = 1;
		bi.shape = shapeB;
		b.bind = new Bind(bi);
		//b.shape = shapeB;
		box  =new AABoundaryBox();
		shapeB.computeBox(box);
		b.box = box;
		b.otherData = null;

		int idB = list.addItem(b);

		ShapesListNode c = new ShapesListNode();
		bi = new BindInit();
		bi.density = 1;
		bi.shape = shapeC;
		c.bind = new Bind(bi);
		//c.shape = shapeB;
		box  =new AABoundaryBox();
		shapeC.computeBox(box);
		c.box = box;
		c.otherData = null;

		int idC = list.addItem(c);

		if(list.rootNode == list.lastNode)
			fail();

		if(list.size() != 3)
			fail();

		list.removeItem(idC);

		if(list.size() != 2)
			fail();



		ShapesListNode d = new ShapesListNode();
		bi = new BindInit();
		bi.density = 1;
		bi.shape = shapeD;
		d.bind = new Bind(bi);
		//d.shape = shapeD;
		box  =new AABoundaryBox();
		shapeD.computeBox(box);
		d.box = box;
		d.otherData = null;

		int idD = list.addItem(d);


		ShapesListNode e = new ShapesListNode();
		bi = new BindInit();
		bi.density = 1;
		bi.shape = shapeE;
		e.bind = new Bind(bi);
		//e.shape = shapeE;
		box  =new AABoundaryBox();
		shapeE.computeBox(box);
		e.box = box;
		e.otherData = null;

		int idE = list.addItem(e);

		if(list.size() != 4)
			fail();

		list.removeItem(idA);

		if(list.size() != 3)
			fail();


		int index = list.rootNode;
		for(int i=0; i< list.size(); i++)
		{
			if(list.a_list[index].id != idD && list.a_list[index].id != idE &&
					list.a_list[index].id != idB)
				fail();


			index = list.a_list[index].next;
		}


	}



}
