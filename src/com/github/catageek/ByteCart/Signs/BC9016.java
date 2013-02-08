package com.github.catageek.ByteCart.Signs;

import com.github.catageek.ByteCart.ByteCart;




public class BC9016 extends AbstractBC9000 implements Subnet,HasNetmask, Triggable {

	public BC9016(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.netmask = 0;
		this.Name = "BC9016";
		this.FriendlyName = "16-station subnet";
		this.Buildtax = ByteCart.myPlugin.getConfig().getInt("buildtax." + this.Name);
		this.Permission = "bytecart." + this.Name;
	}

}
