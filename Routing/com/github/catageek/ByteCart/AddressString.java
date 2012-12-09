package com.github.catageek.ByteCart;



// This class represents a canonical address like xx.xx.xx
public class AddressString extends AbstractAddress implements Address {
	
	private String String; // address as displayed
	
	public AddressString(String s) {

		if (AddressString.isAddress(s))
			this.String = s;
		else
			throw new IllegalArgumentException();
	}
	
	static public boolean isAddress(String s) {
		if(! (s.matches("([0-9]{1,2}\\.){2,2}[0-9]{1,2}"))) {
			return false;
		}
		
		return true;

	}
	
	public VirtualRegistry getRegion() {
/*		VirtualRegistry ret = new VirtualRegistry(5);
		ret.setAmount( this.getField(0) >> 1 );
*/
		VirtualRegistry ret = new VirtualRegistry(6);
		ret.setAmount( this.getField(0));
		return ret;
	}

	public VirtualRegistry getTrack() {
/*		VirtualRegistry ret = new VirtualRegistry(5);
		ret.setAmount( this.getField(1) >> 1 );
*/
		VirtualRegistry ret = new VirtualRegistry(6);
		ret.setAmount( this.getField(1));
		
		return ret;
	}

	public VirtualRegistry getStation() {
/*		VirtualRegistry ret = new VirtualRegistry(6);
		ret.setAmount( this.getField(2) );
*/
		VirtualRegistry ret = new VirtualRegistry(4);
		ret.setAmount( this.getField(2));
	
		return ret;
	}

	public boolean isTrain() {
		VirtualRegistry tmp = new VirtualRegistry(6);
		tmp.setAmount(this.getField(2));
		return tmp.getBit(0);
	}


	private int getField(int index) {
		String[] st = this.String.split("\\.");
		return Integer.parseInt(st[ index ].trim());
	}
	
	@Override
	public void setRegion(int region) {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public void setTrack(int track) {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public void setStation(int station) {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public void setIsTrain(boolean isTrain) {
		Registry tmp = new VirtualRegistry(6);
		tmp.setAmount(this.getField(2));
		tmp.setBit(Offsets.ISTRAIN.getOffset(), isTrain);
		this.String = this.getField(0) + "." + this.getField(1) + "." + tmp.getAmount();
		return;
	}

	@Override
	public java.lang.String toString() {
		return this.String;
	}

	@Override
	public boolean setAddress(java.lang.String s) {
		this.String = s;
		return true;
	}

	@Override
	public boolean UpdateAddress() {
		// TODO Auto-generated method stub
		return true;
	}



}
