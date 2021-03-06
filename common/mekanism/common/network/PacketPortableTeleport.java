package mekanism.common.network;

import java.io.DataOutputStream;

import mekanism.api.Coord4D;
import mekanism.common.PacketHandler;
import mekanism.common.Teleporter;
import mekanism.common.PacketHandler.Transmission;
import mekanism.common.item.ItemPortableTeleporter;
import mekanism.common.tile.TileEntityTeleporter;
import mekanism.common.util.MekanismUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.FMLCommonHandler;

public class PacketPortableTeleport implements IMekanismPacket
{
	@Override
	public String getName()
	{
		return "PortableTeleport";
	}

	@Override
	public IMekanismPacket setParams(Object... data)
	{
		return this;
	}

	@Override
	public void read(ByteArrayDataInput dataStream, EntityPlayer player, World world) throws Exception
	{
		ItemStack itemstack = player.getCurrentEquippedItem();

		if(itemstack != null && itemstack.getItem() instanceof ItemPortableTeleporter)
		{
			ItemPortableTeleporter item = (ItemPortableTeleporter)itemstack.getItem();

			if(item.getStatus(itemstack) == 1)
			{
				Coord4D coords = MekanismUtils.getClosestCoords(new Teleporter.Code(item.getDigit(itemstack, 0), item.getDigit(itemstack, 1), item.getDigit(itemstack, 2), item.getDigit(itemstack, 3)), player);

				World teleWorld = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(coords.dimensionId);
				TileEntityTeleporter teleporter = (TileEntityTeleporter)coords.getTileEntity(teleWorld);

				if(teleporter != null)
				{
					teleporter.didTeleport.add(player);
					teleporter.teleDelay = 5;

					item.setEnergy(itemstack, item.getEnergy(itemstack) - item.calculateEnergyCost(player, coords));
					
					if(player instanceof EntityPlayerMP)
					{
						((EntityPlayerMP)player).playerNetServerHandler.ticksForFloatKick = 0;
					}

					if(world.provider.dimensionId != coords.dimensionId)
					{
						((EntityPlayerMP)player).travelToDimension(coords.dimensionId);
					}

					((EntityPlayerMP)player).playerNetServerHandler.setPlayerLocation(coords.xCoord+0.5, coords.yCoord+1, coords.zCoord+0.5, player.rotationYaw, player.rotationPitch);

					world.playSoundAtEntity(player, "mob.endermen.portal", 1.0F, 1.0F);
					PacketHandler.sendPacket(Transmission.CLIENTS_RANGE, new PacketPortalFX().setParams(coords), coords, 40D);
				}
			}
		}
	}

	@Override
	public void write(DataOutputStream dataStream) throws Exception {}
}
