package org.cagrid.fqp.test.remote.steps;

import org.oasis.wsrf.properties.ResourcePropertyValueChangeNotificationType;

public interface SubscriptionListener {

    public void subscriptionValueChanged(ResourcePropertyValueChangeNotificationType notification);
}
