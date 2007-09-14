/**
 * 
 */
package gov.nih.nci.cagrid.portal2.aggr.regsvc;

import gov.nih.nci.cagrid.portal2.aggr.ServiceUrlProvider;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.support.StaticApplicationContext;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class RegisteredServiceMockTest extends TestCase {

	/**
	 * 
	 */
	public RegisteredServiceMockTest() {

	}

	/**
	 * @param arg0
	 */
	public RegisteredServiceMockTest(String name) {
		super(name);
	}
	
	public void testEmitRegisteredServiceEvent(){
		
		String indexSvcUrl = "http://some.index.svc";
		final String svcUrl1 = "http://service1";
		final String svcUrl2 = "http://service2";
		final String svcUrl3 = "http://service3";
		
		Set<String> indexSvcUrls = new HashSet<String>();
		indexSvcUrls.add(indexSvcUrl);
		
		StaticApplicationContext ctx = new StaticApplicationContext();
		ctx.start();
		ctx.registerSingleton("listener", MockApplicationListener.class);
		ctx.refresh();
		
		
		ServiceUrlProvider dynProv = new ServiceUrlProvider(){
			public Set<String> getUrls(String indexServiceUrl) {
				Set<String> urls = new HashSet<String>();
				urls.add(svcUrl1);
				urls.add(svcUrl2);
				urls.add(svcUrl3);
				return urls;
			}
		};
		
		ServiceUrlProvider cacheProv = new ServiceUrlProvider(){
			public Set<String> getUrls(String indexServiceUrl) {
				Set<String> urls = new HashSet<String>();
				urls.add(svcUrl1);
				return urls;
			}
		};
		
		final RegisteredServiceMonitor mon = new RegisteredServiceMonitor();
		mon.setApplicationContext(ctx);
		mon.setDynamicServiceUrlProvider(dynProv);
		mon.setCachedServiceUrlProvider(cacheProv);
		mon.setIndexServiceUrls(indexSvcUrls);
		
		Thread t = new Thread(){
			public void run(){
				mon.checkForNewServices();
			}
		};
		
		t.start();
		
		try{
			t.join(5000);
		}catch(InterruptedException ex){
			fail("Thread interrupted");
		}
		
		MockApplicationListener listener = (MockApplicationListener) ctx.getBean("listener");
		Set newServiceUrls = listener.getNewServiceUrls();

		assertTrue(newServiceUrls.size() == 2);
		assertNotNull(newServiceUrls.remove(indexSvcUrl + "|" + svcUrl2));
		assertNotNull(newServiceUrls.remove(indexSvcUrl + "|" + svcUrl3));
		
	}
	
	public static class MockApplicationListener implements ApplicationListener{

		private Set<String> newServiceUrls = new HashSet<String>();
		
		public MockApplicationListener(){
			
		}
		
		public void onApplicationEvent(ApplicationEvent e) {
			if(e instanceof RegisteredServiceEvent){
				RegisteredServiceEvent evt = (RegisteredServiceEvent)e;
				this.newServiceUrls.add(evt.getIndexServiceUrl() + "|" + evt.getServiceUrl());
			}
		}
		
		public Set<String> getNewServiceUrls(){
			return this.newServiceUrls;
		}
		
	}

}
