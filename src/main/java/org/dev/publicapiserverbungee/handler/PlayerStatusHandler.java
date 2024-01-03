package org.dev.publicapiserverbungee.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import org.dev.publicapiserverbungee.dto.PlayerStatusDTO;
import org.dev.publicapiserverbungee.service.ValidateOnlineUserService;
import org.json.JSONObject;

public class PlayerStatusHandler implements HttpHandler {

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

        // JSON 파싱
        JSONObject jsonObject = new JSONObject(requestBody);
        List<String> playerNames = jsonObject.getJSONArray("players").toList().stream()
            .map(Object::toString)
            .collect(Collectors.toList());

        // 보낼 데이터 Map<String, Boolean> 형태
        PlayerStatusDTO validationResult = new PlayerStatusDTO(
            ValidateOnlineUserService.validate(playerNames));

        // JSON으로 변환
        JSONObject jsonResponse = new JSONObject(validationResult);

        // 응답 헤더 설정
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, jsonResponse.toString().length());

        // 응답 보내기
        OutputStream os = exchange.getResponseBody();
        os.write(jsonResponse.toString().getBytes());
        os.close();
    }
}
