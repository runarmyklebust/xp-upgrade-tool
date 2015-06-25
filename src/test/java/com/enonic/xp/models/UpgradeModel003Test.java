package com.enonic.xp.models;

import java.nio.file.Paths;

import org.junit.Test;

public class UpgradeModel003Test
    extends AbstractUpgradeModelTest
{

    @Test
    public void rename_moduleConfig_to_siteConfig()
        throws Exception
    {
        final UpgradeModel003 upgradeModel = new UpgradeModel003();

        final String upgraded = upgradeModel.upgrade( Paths.get( "/test" ), getSource( "upgrademodel003.xml" ) );

        assertResult( upgraded, "upgrademodel003_result.xml" );

    }


    @Test
    public void dont_touch_other_nodes()
        throws Exception
    {
        final UpgradeModel003 upgradeModel = new UpgradeModel003();

        final String upgraded = upgradeModel.upgrade( Paths.get( "/test" ), getSource( "upgrademodel003_not_site.xml" ) );

        assertResult( upgraded, "upgrademodel003_not_site_result.xml" );

    }


}