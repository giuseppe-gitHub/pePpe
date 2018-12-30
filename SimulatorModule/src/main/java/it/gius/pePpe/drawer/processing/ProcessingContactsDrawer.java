package it.gius.pePpe.drawer.processing;

import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

import it.gius.pePpe.contact.Contact;
import it.gius.pePpe.data.physic.Bind;
import it.gius.pePpe.drawer.DrawerProperties;
import it.gius.pePpe.manifold.ContactPoint;

public class ProcessingContactsDrawer extends AbstractProcessingDrawer {


	public ProcessingContactsDrawer() {
		this.name = "Processing Contacts Drawer";
	}

	@Override
	public void setDrawerProperties(DrawerProperties drawerProperties) {

	}

	@Override
	public void draw() {

		if(!enable)
			return;

		Contact[] contacts = engine.getContacts();
		int contactsSize = engine.getContactSize();

		applet.pushMatrix();
		
		applet.fill(255,0,0);
		applet.stroke(255,0,0);

		for(int i=0; i<contactsSize; i++)
			drawContact(contacts[i]);


		applet.popMatrix();

	}


	private void drawContact(Contact contact)
	{
		Bind bindA = contact.bindA;
		Bind bindB = contact.bindB;
		Transform transformA = bindA.body.transform;
		Transform transformB = bindB.body.transform;

		assert(contact.manifold.size > 0);

		for(int i=0; i< contact.manifold.size; i++)
			drawContactPoint(contact.manifold.points[i],transformA,transformB);
	}

	private Vec2 globalPoint = new Vec2();
	private Vec2 otherGlobalPoint = new Vec2();
	private Vec2 globalMiddlePoint = new Vec2();

	private void drawContactPoint(ContactPoint point, Transform transformA, Transform transformB)
	{
		if(!point.pointOnShapeB)
		{
			Transform.mulToOut(transformA, point.localPoint, globalPoint);
			Transform.mulToOut(transformB, point.otherLocalPoint, otherGlobalPoint);
		}
		else
		{
			Transform.mulToOut(transformB, point.localPoint, globalPoint);
			Transform.mulToOut(transformA, point.otherLocalPoint, otherGlobalPoint);
		}
		
		applet.ellipse(globalPoint.x, globalPoint.y, 3, 3);
		applet.ellipse(otherGlobalPoint.x, otherGlobalPoint.y, 3, 3);
		
		drawArrow((int)globalPoint.x, (int)globalPoint.y, 
				(int)(globalPoint.x + point.normalGlobal.x*lenght), (int)(globalPoint.y + point.normalGlobal.y*lenght));
		
		globalMiddlePoint.x = 0.5f*globalPoint.x + 0.5f*otherGlobalPoint.x;
		globalMiddlePoint.y = 0.5f*globalPoint.y + 0.5f*otherGlobalPoint.y;
		
		applet.text(point.distance, globalMiddlePoint.x, globalMiddlePoint.y);
	}
	
	
	private static final int lenght = 15;
	private static final int lenght3 = lenght/3;
	
	private void drawArrow(int x1, int y1, int x2, int y2) {
		  applet.line(x1, y1, x2, y2);
		  applet.pushMatrix();
		  applet.translate(x2, y2);
		  float a = MathUtils.atan2(x1-x2, y2-y1);
		  applet.rotate(a);
		  applet.line(0, 0, -lenght3, -lenght3);
		  applet.line(0, 0, lenght3, -lenght3);
		  applet.popMatrix();
		} 
}
