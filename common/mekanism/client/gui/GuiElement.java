package mekanism.client.gui;

import java.util.List;

import mekanism.common.ObfuscatedNames;
import mekanism.common.util.MekanismUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public abstract class GuiElement
{
	protected static Minecraft mc = Minecraft.getMinecraft();

	protected ResourceLocation RESOURCE;

	public GuiMekanism guiObj;

	public TileEntity tileEntity;

	public ResourceLocation defaultLocation;

	public GuiElement(ResourceLocation resource, GuiMekanism gui, TileEntity tile, ResourceLocation def)
	{
		RESOURCE = resource;
		guiObj = gui;
		tileEntity = tile;
		defaultLocation = def;
	}

	protected void displayTooltip(String s, int xAxis, int yAxis)
	{
		guiObj.drawCreativeTabHoveringText(s, xAxis, yAxis);
	}

	protected void displayTooltips(List<String> list, int xAxis, int yAxis)
	{
		guiObj.func_102021_a(list, xAxis, yAxis);
	}

	protected void offsetX(int xSize)
	{
		if(guiObj instanceof GuiContainer)
		{
			try {
				int size = (Integer)MekanismUtils.getPrivateValue(guiObj, GuiContainer.class, ObfuscatedNames.GuiContainer_xSize);
				MekanismUtils.setPrivateValue(guiObj, size + xSize, GuiContainer.class, ObfuscatedNames.GuiContainer_xSize);
			} catch(Exception e) {}
		}
	}

	protected void offsetY(int ySize)
	{
		if(guiObj instanceof GuiContainer)
		{
			try {
				int size = (Integer)MekanismUtils.getPrivateValue(guiObj, GuiContainer.class, ObfuscatedNames.GuiContainer_ySize);
				MekanismUtils.setPrivateValue(guiObj, size + ySize, GuiContainer.class, ObfuscatedNames.GuiContainer_ySize);
			} catch(Exception e) {}
		}
	}

	protected FontRenderer getFontRenderer()
	{
		return guiObj.getFontRenderer();
	}

	public abstract void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight);

	public abstract void renderForeground(int xAxis, int yAxis);

	public abstract void preMouseClicked(int xAxis, int yAxis, int button);

	public abstract void mouseClicked(int xAxis, int yAxis, int button);
}
