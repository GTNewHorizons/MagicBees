package magicbees.client.gui;

import magicbees.main.CommonProxy;
import magicbees.tileentity.TileEntityEffectJar;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GUIEffectJar extends GuiContainer {

    public static final String BACKGROUND_FILE = "jarScreen.png";
    public static final ResourceLocation BACKGROUND_LOCATION = new ResourceLocation(
            CommonProxy.DOMAIN,
            CommonProxy.GUI_TEXTURE + "jarScreen.png");

    private static final int WIDTH = 176;
    private static final int HEIGHT = 156;

    private static final int SLOT_JAR_X = 80;
    private static final int SLOT_JAR_Y = 22;
    private static final int SLOT_INVENTORY_X = 8;
    private static final int SLOT_INVENTORY_Y = 74;
    private static final int BAR_DEST_X = 117;
    private static final int BAR_DEST_Y = 10;

    private static final int BAR_SRC_X = 176;
    private static final int BAR_SRC_Y = 0;

    private static final int BAR_WIDTH = 10;
    private static final int BAR_HEIGHT = 40;

    public GUIEffectJar(TileEntityEffectJar jar, EntityPlayer player) {
        super(new ContainerEffectJar(jar, player));
        this.xSize = WIDTH;
        this.ySize = HEIGHT;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRendererObj.drawString("Inventory", 9, 63, 0);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_SRC_ALPHA);
        this.mc.getTextureManager().bindTexture(BACKGROUND_LOCATION);

        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, xSize, ySize);

        TileEntityEffectJar jar = ((ContainerEffectJar) this.inventorySlots).jar;
        float r = ((jar.currentBeeColour >> 16) & 255) / 255f;
        float g = ((jar.currentBeeColour >> 8) & 255) / 255f;
        float b = (jar.currentBeeColour & 255) / 255f;

        GL11.glColor3f(r, g, b);

        int value = BAR_HEIGHT - (jar.currentBeeHealth * BAR_HEIGHT) / 100;
        this.drawTexturedModalRect(
                this.guiLeft + BAR_DEST_X,
                this.guiTop + value + BAR_DEST_Y,
                BAR_SRC_X,
                BAR_SRC_Y,
                BAR_WIDTH,
                BAR_HEIGHT - value);
    }
}
