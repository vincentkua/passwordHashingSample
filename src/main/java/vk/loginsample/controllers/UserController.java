package vk.loginsample.controllers;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vk.loginsample.repository.UserRepository;
import vk.loginsample.services.JWTService;
import vk.loginsample.services.UserService;

@RestController
@RequestMapping(path = "/api")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    JWTService jwtService;

    @PostMapping(value = "/createUser")
    public ResponseEntity<String> createUser(@RequestBody String requestJsonPayload) {
        try {
            // Convert JsonString to JsonObject and read it
            JsonReader reader = Json.createReader(new StringReader(requestJsonPayload));
            JsonObject jsonobj = reader.readObject();
            String newuser = jsonobj.getString("newuser");
            String newpassword = jsonobj.getString("newpassword");

            Integer insertstatus = userService.insertUserService(newuser, newpassword);

            if (insertstatus > 0) {
                JsonObject errorResponse = Json.createObjectBuilder()
                        .add("success", "New User Created")
                        .build();
                return ResponseEntity.status(HttpStatus.OK).body(errorResponse.toString());
            } else {
                JsonObject errorResponse = Json.createObjectBuilder()
                        .add("error", "0 data inserted")
                        .build();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
            }

        } catch (Exception e) {
            JsonObject errorResponse = Json.createObjectBuilder()
                    .add("error", "An unexpected error occurred")
                    .add("message", e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }

    }

    @PostMapping(value = "/login")
    public ResponseEntity<String> loginUser(@RequestBody String requestJsonPayload) {
        try {
            // Convert JsonString to JsonObject and read it
            JsonReader reader = Json.createReader(new StringReader(requestJsonPayload));
            JsonObject jsonobj = reader.readObject();
            String user = jsonobj.getString("user");
            String password = jsonobj.getString("password");

            Boolean validatedUser = userService.validateUserService(user, password);

            if (validatedUser) {
                String jwtToken = jwtService.generateJwtToken(user);
                JsonObject errorResponse = Json.createObjectBuilder()
                        .add("success", "User validated")
                        .add("jwtToken", jwtToken)
                        .build();
                return ResponseEntity.status(HttpStatus.OK).body(errorResponse.toString());
            } else {
                JsonObject errorResponse = Json.createObjectBuilder()
                        .add("error", "Invalid User or Password")
                        .build();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
            }
        } catch (Exception e) {
            JsonObject errorResponse = Json.createObjectBuilder()
                    .add("error", "An unexpected error occurred")
                    .add("message", e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }

    @PostMapping(value = "/validateJWT")
    public ResponseEntity<String> validateJWT(
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader,
            @RequestBody String requestBody) {

        try {
            // Step 1: Extract the JWT token from the Authorization header
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String jwtString = authorizationHeader.substring(7); // Remove "Bearer " prefix

                // Step 2: Parse the username from the request body
                JsonReader reader = Json.createReader(new StringReader(requestBody));
                JsonObject jsonobj = reader.readObject();
                String username = jsonobj.getString("user"); // Expect "username" field in the body

                // Step 3: Validate the JWT token (you can pass the token and username if
                // necessary)
                boolean isTokenValid = jwtService.validateJwtToken(jwtString, username);

                if (isTokenValid) {
                    JsonObject successResponse = Json.createObjectBuilder()
                            .add("success", "JWT validated")
                            .build();
                    return ResponseEntity.status(HttpStatus.OK).body(successResponse.toString());
                } else {
                    JsonObject errorResponse = Json.createObjectBuilder()
                            .add("error", "Invalid or expired token")
                            .build();
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse.toString());
                }
            } else {
                JsonObject errorResponse = Json.createObjectBuilder()
                        .add("error", "Missing or invalid Authorization header")
                        .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse.toString());
            }
        } catch (Exception e) {
            JsonObject errorResponse = Json.createObjectBuilder()
                    .add("error", "An unexpected error occurred")
                    .add("message", e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }
}
