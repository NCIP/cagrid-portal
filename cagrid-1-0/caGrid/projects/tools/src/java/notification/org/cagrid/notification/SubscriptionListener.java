package org.cagrid.notification;


import org.oasis.wsrf.properties.ResourcePropertyValueChangeNotificationType;

public interface SubscriptionListener {

    public void subscriptionValueChanged(ResourcePropertyValueChangeNotificationType notification);
}
