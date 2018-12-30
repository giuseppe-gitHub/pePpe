package it.gius.pePpe.data.tracers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class PointTracerWriter extends PrintWriter{

	
	public PointTracerWriter(File file) throws FileNotFoundException {
		super(file);
	}
	
	public PointTracerWriter(File file, String csn)
			throws FileNotFoundException, UnsupportedEncodingException {
		super(file, csn);
	}

	
	public void writeTracer(PointTracer tracer)
	{
		DataTrace<PosVelTraceData>[] arrayTrace = tracer.d_trace;
		
		if(tracer.name != null)
		{
			write("Tracer: " + tracer.name +"\n");
		}
		else
			write("Tracer: unknown\n");
		
		
		write("time,x,y,vx,vy\n");
		for(int i=0; i< tracer.traceSize; i++)
		{
			DataTrace<PosVelTraceData> trace = arrayTrace[i];
			
			write(trace.time + ", " + trace.data.x + "," + trace.data.y + "," + trace.data.vx + "," 
					+ trace.data.vy + "\n");
		}
	}
	
	
}
