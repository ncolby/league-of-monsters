package com.league.game.Handlers;

import com.serializers.SerializedHero;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

public class UDPCreationHandler {

    private static byte[] incomingDatagramPacketBuffer;

    private static byte[] outgoingDatagramPacketBuffer;
    private static DatagramPacket outgoingDatagramPacket;
    private static DatagramPacket incomingDatagramPacket;
    private static Map<String, String> creationCommand;

    public static final int SERVER_PORT = 8086;

    public static void handleCreation(UDPNetworkHandler udpNetworkHandler) {
        incomingDatagramPacketBuffer = new byte[1024];
        outgoingDatagramPacketBuffer = new byte[1024];
        incomingDatagramPacket = new DatagramPacket(incomingDatagramPacketBuffer, incomingDatagramPacketBuffer.length);
        creationCommand = new HashMap<String, String>();
        creationCommand.put("createHero", "pumpkin");
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
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(incomingDatagramPacket.getData());
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                SerializedHero serializedHero = (SerializedHero) objectInputStream.readObject();
                objectInputStream.close();
                System.out.println(serializedHero.toString());
            } catch (SocketTimeoutException e) {
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
