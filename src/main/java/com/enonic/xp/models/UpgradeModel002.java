package com.enonic.xp.models;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.common.io.CharSource;

public class UpgradeModel002
    extends AbstractXsltUpgradeModel
{
    public UpgradeModel002()
    {
        super( "Replace image-content property-names", "UpgradeModel002.xsl" );
    }

    @Override
    public boolean supports( final Path path )
    {
        return path.endsWith( Paths.get( "_", "node.xml" ) );
    }

    @Override
    public String upgrade( final Path path, final CharSource source )
    {
        return this.transform( path, source );
    }


}
