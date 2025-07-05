package com.silveredsoul.scene_match.data;

public class Responses {

    public static class AuthResponse extends Responses {
        public String token;

        public AuthResponse(String token) {
            this.token = token;
        }
    }

    public static class ErrorResponse extends Responses {
        public String error;

        public ErrorResponse(String error) {
            this.error = error;
        }
    }

    public static class MessageResponse extends Responses {
        public String message;

        public MessageResponse(String message) {
            this.message = message;
        }
    }
}