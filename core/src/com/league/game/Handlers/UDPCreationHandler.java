package com.league.game.Handlers;

import com.league.game.LeagueOfHorrors;
import com.serializers.SerializableAbilityEntity;
import com.serializers.SerializableGameState;
import com.serializers.SerializableHeroEntity;
import com.serializers.SerializedHero;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UDPCreationHandler {

    private static byte[] incomingDatagramPacketBuffer;

    private static byte[] outgoingDatagramPacketBuffer;
    private static DatagramPacket outgoingDatagramPacket;
    private static DatagramPacket incomingDatagramPacket;
    private static Map<String, String> creationCommand;

    private static String playerId;

    public static final int SERVER_PORT = 8086;

    public static void handleCreation(LeagueOfHorrors gameManager) {
        playerId = gameManager.getPlayerId();
        incomingDatagramPacketBuffer = new byte[1024];
        outgoingDatagramPacketBuffer = new byte[1024];
        incomingDatagramPacket = new DatagramPacket(incomingDatagramPacketBuffer, incomingDatagramPacketBuffer.length);
        creationCommand = new HashMap<String, String>();
        creationCommand.put("createHero", "pumpkin_" + playerId);
        outgoingDatagramPacketBuffer = JSONObject.toJSONString(creationCommand).getBytes();
        try {
            gameManager.udpNetworkHandler.getClientSocket().setSoTimeout(5000);
            outgoingDatagramPacket = new DatagramPacket(outgoingDatagramPacketBuffer, outgoingDatagramPacketBuffer.length,
                    InetAddress.getByName("127.0.0.1"), SERVER_PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                gameManager.udpNetworkHandler.getClientSocket().send(outgoingDatagramPacket);
                gameManager.udpNetworkHandler.getClientSocket().receive(incomingDatagramPacket);
                SerializableGameState serializableGameState = null;
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(incomingDatagramPacket.getData());
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                Object object = objectInputStream.readObject();
                if (object instanceof SerializableGameState) {
                    serializableGameState = (SerializableGameState) object;
                }
                objectInputStream.close();
                if (serializableGameState != null) {
                    System.out.println(serializableGameState.toString());
                    if (serializableGameState.getConnectedPlayers().get(playerId) != null) {
                        gameManager.isHeroCreated = true;
                        break;
                    }
                }
            } catch (SocketTimeoutException e) {
                System.out.println("timeout");
            } catch (EOFException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
