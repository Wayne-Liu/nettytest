package com.jdcloud.sdk.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CalculatorRemoteImpl implements Calculator {
    @Override
    public int add(int a, int b) {
        String address = "127.0.0.1";
        int port = 8080;
        try {
            Socket socket = new Socket(address, port);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            CalculatorRequest request = new CalculatorRequest();
            request.setA(a);
            request.setB(b);


            out.writeObject(request);

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Object response = in.readObject();

            if (response instanceof Integer) {
                return (Integer)response;
            } else {
                throw new InternalError();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new InternalError();
        }

    }
}
