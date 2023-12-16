package org.dev.publicapiserverbungee.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.dev.publicapiserverbungee.service.ValidateOnlineUserService;
import org.json.JSONObject;

public class StatusHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("StatusHandler called");
        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
            return;
        }

        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder requestBuilder = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            requestBuilder.append(line);
        }
        String requestBody = requestBuilder.toString();
        System.out.println(requestBody);

        try {
            // JSON 파싱
            JSONObject jsonObject = new JSONObject(requestBody);
            System.out.println(jsonObject);
            List<String> playerNames = jsonObject.getJSONArray("players").toList().stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
            System.out.println("JSON 파싱 완료!!" + playerNames);

            // 보낼 데이터 Map<String, Boolean> 형태
            Map<String, Boolean> validationResult = ValidateOnlineUserService.validate(playerNames);
            System.out.println(validationResult);

            // JSON으로 변환
            JSONObject jsonResponse = new JSONObject(validationResult);
            System.out.println("JSON 변환 완료!!");

            // 응답 헤더 설정
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, jsonResponse.toString().length());

            // 응답 보내기
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.toString().getBytes());
            os.close();
            System.out.println("응답 완료!!");
        } catch (Exception e) {
            e.printStackTrace(); // 서버 측 로그에 예외를 기록
            if (exchange.getResponseHeaders().getFirst("Content-Type") == null) {
                exchange.getResponseHeaders().set("Content-Type", "application/json");
            }
            exchange.sendResponseHeaders(500, 0); // 500 Internal Server Error
            OutputStream os = exchange.getResponseBody();
            os.write(new JSONObject().put("error", "Internal server error").toString().getBytes());
            os.close();
        }


    }
}
