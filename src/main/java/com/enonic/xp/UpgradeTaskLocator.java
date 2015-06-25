package com.enonic.xp;

import java.util.List;

import com.google.common.collect.Lists;

import com.enonic.xp.models.UpgradeModel001;
import com.enonic.xp.models.UpgradeModel002;

final class UpgradeTaskLocator
{
    private final List<UpgradeModel> upgradeModels;

    public UpgradeTaskLocator()
    {
        this.upgradeModels = Lists.newArrayList();
        this.upgradeModels.add( new UpgradeModel001() );
        this.upgradeModels.add( new UpgradeModel002() );
    }

    public List<UpgradeModel> getUpgradeModels()
    {
        return upgradeModels;
    }

}
