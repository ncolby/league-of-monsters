package com.league.game.Handlers;

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

    public static void handleCreation(UDPNetworkHandler udpNetworkHandler) {
        playerId = UUID.randomUUID().toString();
        incomingDatagramPacketBuffer = new byte[1024];
        outgoingDatagramPacketBuffer = new byte[1024];
        incomingDatagramPacket = new DatagramPacket(incomingDatagramPacketBuffer, incomingDatagramPacketBuffer.length);
        creationCommand = new HashMap<String, String>();
//        creationCommand.put("createHero", "pumpkin");
        creationCommand.put("test", "pumpkin_" + playerId);
        outgoingDatagramPacketBuffer = JSONObject.toJSONString(creationCommand).getBytes();
        try {
            outgoingDatagramPacket = new DatagramPacket(outgoingDatagramPacketBuffer, outgoingDatagramPacketBuffer.length,
                    InetAddress.getByName("127.0.0.1"), SERVER_PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                udpNetworkHandler.getClientSocket().send(outgoingDatagramPacket);
                udpNetworkHandler.getClientSocket().receive(incomingDatagramPacket);
                SerializableAbilityEntity serializableAbilityEntity = null;
                SerializableHeroEntity serializableHeroEntity = null;
                SerializableGameState serializableGameState = null;
                SerializedHero serializedHero = null;
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(incomingDatagramPacket.getData());
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                Object object = objectInputStream.readObject();
                objectInputStream.close();
                if (object instanceof SerializableAbilityEntity) {
                    serializableAbilityEntity = (SerializableAbilityEntity) object;
                } else if (object instanceof SerializedHero) {
                    serializedHero = (SerializedHero) object;
                } else if (object instanceof SerializableHeroEntity) {
                    serializableHeroEntity = (SerializableHeroEntity) object;
                } else if (object instanceof SerializableGameState) {
                    serializableGameState = (SerializableGameState) object;
                }
                if (serializedHero != null) {
                    System.out.println(serializedHero.getName() + " is " + serializedHero.getAge());
                } else if (serializableAbilityEntity != null) {
                    System.out.println(serializableAbilityEntity.getAbilityName() + " cooldown is " + serializableAbilityEntity.getCooldownEnd());
                } else if (serializableHeroEntity != null) {
                    System.out.println(serializableHeroEntity.getHeroName() + " has an ability of : " + serializableHeroEntity.getAbilities().get(0).getAbilityName());
                } else if (serializableGameState != null) {
                    System.out.println(serializableGameState.getConnectedPlayers().get(playerId).getHeroName() + " is the first hero with ability " +
                            serializableGameState.getConnectedPlayers().get(playerId).getAbilities().get(0).getAbilityName());
                }
            } catch (SocketTimeoutException e) {
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
