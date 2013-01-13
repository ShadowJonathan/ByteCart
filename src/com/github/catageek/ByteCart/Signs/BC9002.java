package com.github.catageek.ByteCart.Signs;


public class BC9002 extends AbstractBC9000 implements TriggeredSign {

	public BC9002(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle, "BC9002");
		this.netmask = 3;
		this.Name = "BC9002";
		this.FriendlyName = "2-station subnet";
	}

}
