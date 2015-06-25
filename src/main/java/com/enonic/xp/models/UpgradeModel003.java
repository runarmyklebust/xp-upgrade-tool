package com.enonic.xp.models;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.common.io.CharSource;

public class UpgradeModel003
    extends AbstractXsltUpgradeModel
{
    private final static String SUPPORTED_REPO = "cms-repo";

    public UpgradeModel003()
    {
        super( "Replace moduleConfig with siteConfig", "UpgradeModel003.xsl" );
    }

    @Override
    public boolean supports( final Path path, final String repositoryName, final String branchName )
    {
        return path.endsWith( Paths.get( "_", "node.xml" ) ) && SUPPORTED_REPO.equals( repositoryName );
    }

    @Override
    public String upgrade( final Path path, final CharSource source )
    {
        return this.transform( path, source );
    }
}
