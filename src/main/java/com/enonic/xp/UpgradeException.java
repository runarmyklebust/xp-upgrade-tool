package com.enonic.xp;

public class UpgradeException extends RuntimeException
{

    public UpgradeException( final String message )
    {
        super( message );
    }

    public UpgradeException( final String message, final Throwable cause )
    {
        super( message, cause );
    }
}
