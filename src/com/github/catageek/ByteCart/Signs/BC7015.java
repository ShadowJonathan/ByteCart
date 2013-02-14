package com.github.catageek.ByteCart.Signs;

import com.github.catageek.ByteCart.Routing.AddressRouted;
import com.github.catageek.ByteCart.Routing.ReturnAddressFactory;

public final class BC7015 extends BC7011 implements Triggable {

	public BC7015(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
	}

	protected AddressRouted getTargetAddress() {
		return ReturnAddressFactory.getAddress(this.getInventory());
	}

	@Override
	public String getName() {
		return "BC7015";
	}

	@Override
	public String getFriendlyName() {
		return "Set Return";
	}
}