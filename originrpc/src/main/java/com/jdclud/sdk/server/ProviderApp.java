package com.jdclud.sdk.server;

import com.jdclud.sdk.common.Calculator;
import com.jdclud.sdk.common.CalculatorRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ProviderApp {
    private  Calculator calculator = new CalculatorImpl();

    public static void main(String[] args) throws IOException {
        new ProviderApp().run();
    }

    private void run() throws IOException {
        ServerSocket lister = new ServerSocket(8080);

        while (true) {
            Socket socket = lister.accept();

            try {
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                Object req = inputStream.readObject();

                int result = 0;
                if (req instanceof CalculatorRequest) {
                    CalculatorRequest request = (CalculatorRequest)req;
                    result = calculator.add(request.getA(), request.getB());
                }

                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(result);


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }


    }
}
