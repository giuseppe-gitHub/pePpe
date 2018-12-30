package it.gius.pePpe.testSuit;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.ServiceLoader;

public class SimulationLoader implements Iterable<ISimulation>{
	
	
	private static class MyClassLoader extends URLClassLoader
	{
		public MyClassLoader(URL[] urls, ClassLoader parentLoader) {
			super(urls, parentLoader);
		}
		
		public void addURL(URL url)
		{
			super.addURL(url);
		}
	}
	
	private MyClassLoader urlClassLoader;
	private ServiceLoader<ISimulation> serviceLoader;
	
	public SimulationLoader() {
	}
	
	public void init()
	{
		URL[] urls = new URL[0];
		ClassLoader parentClassLoader = this.getClass().getClassLoader();
		urlClassLoader = new MyClassLoader(urls, parentClassLoader);
		
		serviceLoader = ServiceLoader.load(ISimulation.class, urlClassLoader);
	}
	
	
	public void addJar(String filePath) throws IOException, MalformedURLException
	{
		File file = new File(filePath);
		if(!file.exists())
			throw new IOException();
		
			URI fileURI = file.toURI();
			URL fileURL = fileURI.toURL();
			URL jarUrl = new URL("jar", "", fileURL + "!/");
			
			urlClassLoader.addURL(jarUrl);

	}
	
	
	
	public void search()
	{	
			/*reload is needed, otherwise it wouldn't find the new providers
			 * added by the "addJar" method afer the last search
			 * */
			serviceLoader.reload();
	}
	
	@Override
	public Iterator<ISimulation> iterator() {
		return serviceLoader.iterator();
	}

}
