package it.gius.pePpe.simulator.endNotifiers;


import java.io.File;
import java.io.FileNotFoundException;

import it.gius.pePpe.data.tracers.PointTracer;
import it.gius.pePpe.data.tracers.PointTracerWriter;
import it.gius.pePpe.simulator.IEndListener;
import it.gius.pePpe.simulator.SimulationInfo;

public class TracersNotify implements IEndListener {

	private PointTracer[] tracers;
	private String fileName;

	public TracersNotify(PointTracer[] tracers,String fileName) {
		this.tracers = tracers;
		this.fileName = fileName;
	}

	@Override
	public void notifyEnd(EndType endType, SimulationInfo simInfo) {	

		if(endType != EndType.EXCEPTION)
		{

			File file = new File(fileName);
			PointTracerWriter writer = null;
			try {
				writer = new PointTracerWriter(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return;
			}

			for(int i=0; i< tracers.length;i++)			
				writer.writeTracer(tracers[i]);


			writer.close();

		}

	}

}
