package org.cagrid.metrics.service;

import java.util.GregorianCalendar;

import org.cagrid.metrics.common.EventRecord;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class TestDriver {

	public static void main(String[] args) {
		try{
			Configuration cfg = new Configuration();
			SessionFactory factory = cfg.configure("metrics.hibernate.cfg.xml").buildSessionFactory();
			Session s = factory.openSession();
			s.beginTransaction();
			EventRecord e = new EventRecord();
			e.setReportedAt(new GregorianCalendar());
			s.save(e);
			s.getTransaction().commit();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}