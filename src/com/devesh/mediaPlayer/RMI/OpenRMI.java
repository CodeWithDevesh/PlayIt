package com.devesh.mediaPlayer.RMI;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface OpenRMI extends Remote{
	void open(String[] args) throws RemoteException;
}
