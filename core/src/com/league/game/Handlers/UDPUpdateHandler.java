package com.league.game.Handlers;

import com.league.game.LeagueOfHorrors;
import com.serializers.SerializableGameState;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.SocketTimeoutException;

public class UDPUpdateHandler {

    public static void handleUpdate(LeagueOfHorrors gameManager) {
        byte[] incomingDatagramPacketBuffer = new byte[1024];
        DatagramPacket incomingDatagramPacket = new DatagramPacket(incomingDatagramPacketBuffer, incomingDatagramPacketBuffer.length);
        SerializableGameState serializableGameState = null;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(incomingDatagramPacket.getData());
        try {
            gameManager.udpNetworkHandler.getClientSocket().setSoTimeout(1000);
            gameManager.udpNetworkHandler.getClientSocket().receive(incomingDatagramPacket);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object object = objectInputStream.readObject();
            if (object instanceof SerializableGameState) {
                serializableGameState = (SerializableGameState) object;
            }
            objectInputStream.close();
            gameManager.heroStateQueue.add(UDPStateHandler.replicateServerState(serializableGameState));
        } catch (SocketTimeoutException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
