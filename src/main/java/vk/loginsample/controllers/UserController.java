package vk.loginsample.controllers;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<String> validateJWT(@RequestBody String requestJwtJsonPayload) {

        try {
            // Read JWT Json into String
            JsonReader reader = Json.createReader(new StringReader(requestJwtJsonPayload));
            JsonObject jsonobj = reader.readObject();
            String user = jsonobj.getString("user");
            String jwtString = jsonobj.getString("jwtToken");

            Boolean validateJWT = jwtService.validateJwtToken(jwtString, user);

            if (validateJWT) {
                JsonObject errorResponse = Json.createObjectBuilder()
                        .add("success", "JWT validated")
                        .build();
                return ResponseEntity.status(HttpStatus.OK).body(errorResponse.toString());
            } else {
                JsonObject errorResponse = Json.createObjectBuilder()
                        .add("error", "An unexpected error occurred")
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
}
