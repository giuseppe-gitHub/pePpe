package it.gius.pePpe.simulator.processing;

import it.gius.pePpe.simulator.IEndListener;
import it.gius.pePpe.simulator.SimulationInfo;
import it.gius.pePpe.simulator.IEndListener.EndType;
import it.gius.processing.util.WindowClosedListener;

import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

class ClosedListenerImpl implements WindowClosedListener
{
	private List<IEndListener> endListeners = null;
	
	private SimulationInfo simInfo;
	
	private boolean notified = false;
	
	public void addEndListener(IEndListener listener) {
		if(endListeners == null)
			endListeners = new ArrayList<IEndListener>();
		
		endListeners.add(listener);

	}
	
	public void setSimInfo(SimulationInfo simInfo) {
		this.simInfo = simInfo;
	}
	
	public void alreadyNotified()
	{
		notified = true;
	}
	
	@Override
	public void windowClosed(WindowEvent e) {
		if(endListeners == null || notified)
			return;
		
		EndType endType = EndType.USER_CLOSED;
		
		for(IEndListener listener : endListeners)
			listener.notifyEnd(endType,simInfo);
	}
}

