package gov.nih.nci.cagrid.evsgridservice.service;

import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: shanbhak
 * Date: Sep 25, 2006
 * Time: 4:44:07 PM
 * This class is used to wrap exception when connection to caCORE Service <code>ApplicationService</code> is not
 * working!
 *
 */


public class ServiceException extends RemoteException
{
    /**
     * CTOR
     */
    public ServiceException()
    {
        super();
    }

    /**
     * This CTOR constructs the {@link ServiceException} object with the passed message
     * @param message The message that describes the exception
     */
    public ServiceException(String message)
    {
        super(message);
    }

    /**
     * Constructs the {@link ServiceException} object with the passed message.
     * It also stores the actual exception that occured
     * @param message The message which is describes the exception caused
     * @param cause The actual exception that occured
     */
    public ServiceException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
}
// $Log$

