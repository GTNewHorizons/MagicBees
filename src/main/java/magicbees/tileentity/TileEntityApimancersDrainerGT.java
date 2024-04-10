package magicbees.tileentity;

import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;

import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeModifier;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_IndustrialApiary;

public class TileEntityApimancersDrainerGT extends TileEntityApimancersDrainerCommon
        implements IGregTechDeviceInformation {

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        String aspects = essentia.aspects.entrySet().stream()
                .map(
                        e -> EnumChatFormatting.LIGHT_PURPLE + e.getKey().getName()
                                + ": "
                                + EnumChatFormatting.GREEN
                                + e.getValue())
                .collect(Collectors.joining(EnumChatFormatting.RESET + ", " + EnumChatFormatting.RESET));

        return new String[] { EnumChatFormatting.DARK_PURPLE + "Apimancer's Drainer" + EnumChatFormatting.RESET,
                aspect != null ? "Attuned: " + aspect.getName() : "Not attuned", "Stored Essentia:", aspects };
    }

    @Override
    protected IBeeHousing beeHousing(TileEntity above) {
        IBeeHousing regularCheck = super.beeHousing(above);
        if (regularCheck != null) return regularCheck;
        BaseMetaTileEntity GTMetaTileEntity = getGTTileEntity(above);
        if (GTMetaTileEntity != null) {
            IMetaTileEntity underlyingMetaTileEntity = GTMetaTileEntity.getMetaTileEntity();
            if (!(underlyingMetaTileEntity instanceof GT_MetaTileEntity_IndustrialApiary)) return null;
            return (IBeeHousing) underlyingMetaTileEntity;
        }

        return null;
    }

    @Override
    protected boolean canWork(IBeeHousing beeHousing, TileEntity te) {
        BaseMetaTileEntity GTMetaTileEntity = getGTTileEntity(te);
        return GTMetaTileEntity != null ? GTMetaTileEntity.isActive() : super.canWork(beeHousing, te);
    }

    @Override
    protected ItemStack getQueen(IBeeHousing beeHousing, TileEntity te) {
        BaseMetaTileEntity GTMetaTileEntity = getGTTileEntity(te);
        GT_MetaTileEntity_IndustrialApiary industrialApiary = getGTIndustrialApiary(GTMetaTileEntity);
        return industrialApiary != null ? industrialApiary.getUsedQueen() : super.getQueen(beeHousing, te);
    }

    @Override
    protected int getProductionMultiplier(IBeeModifier modifier, IBee queen, TileEntity te) {
        BaseMetaTileEntity GTMetaTileEntity = getGTTileEntity(te);
        GT_MetaTileEntity_IndustrialApiary industrialApiary = getGTIndustrialApiary(GTMetaTileEntity);
        int housingProductionMultiplier = super.getProductionMultiplier(modifier, queen, te);
        return industrialApiary != null
                ? housingProductionMultiplier * (int) Math.ceil(Math.sqrt(industrialApiary.mSpeed))
                : housingProductionMultiplier;
    }

    private BaseMetaTileEntity getGTTileEntity(TileEntity te) {
        boolean isGTMetaTileEntity = te instanceof BaseMetaTileEntity;
        return isGTMetaTileEntity ? (BaseMetaTileEntity) te : null;
    }

    private GT_MetaTileEntity_IndustrialApiary getGTIndustrialApiary(BaseMetaTileEntity bmte) {
        if (bmte != null) {
            IMetaTileEntity underlyingMetaTileEntity = bmte.getMetaTileEntity();
            if (!(underlyingMetaTileEntity instanceof GT_MetaTileEntity_IndustrialApiary)) return null;
            return (GT_MetaTileEntity_IndustrialApiary) underlyingMetaTileEntity;
        }
        return null;
    }
}
