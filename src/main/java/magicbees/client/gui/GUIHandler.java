package magicbees.client.gui;

import magicbees.tileentity.TileEntityEffectJar;
import magicbees.tileentity.TileEntityMagicApiary;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.IGuiHandler;

public class GUIHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        Object value = null;

        if (ID == UIScreens.EFFECT_JAR.ordinal()) {
            value = new ContainerEffectJar((TileEntityEffectJar) world.getTileEntity(x, y, z), player);
        } else if (ID == UIScreens.THAUMIC_APIARY.ordinal()) {
            TileEntityMagicApiary tileEntityThaumicApiary = (TileEntityMagicApiary) world.getTileEntity(x, y, z);
            value = new ContainerMagicApiary(player.inventory, tileEntityThaumicApiary);
        }

        return value;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == UIScreens.EFFECT_JAR.ordinal()) {
            return new GUIEffectJar((TileEntityEffectJar) world.getTileEntity(x, y, z), player);
        } else if (ID == UIScreens.THAUMIC_APIARY.ordinal()) {
            TileEntityMagicApiary tileEntityThaumicApiary = (TileEntityMagicApiary) world.getTileEntity(x, y, z);
            return new GuiMagicApiary(player.inventory, tileEntityThaumicApiary);
        } else {
            return null;
        }
    }
}
