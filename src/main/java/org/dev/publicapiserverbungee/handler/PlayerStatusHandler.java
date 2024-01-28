package org.dev.publicapiserverbungee.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.dev.publicapiserverbungee.dto.PlayerRequestDTO;
import org.dev.publicapiserverbungee.dto.PlayerStatusDTO;
import org.dev.publicapiserverbungee.service.ValidateOnlineUserService;

public class PlayerStatusHandler implements HttpHandler {

    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
            return;
        }

        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(),
            StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder requestBuilder = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            requestBuilder.append(line);
        }
        String requestBody = requestBuilder.toString();

        // Gson으로 JSON 파싱
        PlayerRequestDTO playerRequestDTO = gson.fromJson(requestBody, PlayerRequestDTO.class);

        // 보낼 데이터
        PlayerStatusDTO validationResult = new PlayerStatusDTO(
            ValidateOnlineUserService.validate(playerRequestDTO.getPlayers()));

        // JSON으로 변환
        String jsonResponse = gson.toJson(validationResult);

        // 응답 헤더 설정 및 응답 보내기
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, jsonResponse.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(jsonResponse.getBytes(StandardCharsets.UTF_8));
        }
    }
}
