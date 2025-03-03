package microservice_traceability.traceability.infrastructure.http.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import microservice_traceability.traceability.application.dto.OrderStatusHistoryRequest;
import microservice_traceability.traceability.application.dto.OrderStatusHistoryResponse;
import microservice_traceability.traceability.application.handler.IOrderStatusHistoryHandler;
import microservice_traceability.traceability.infrastructure.util.InfrastructureConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-status-history")
@RequiredArgsConstructor
public class OrderStatusHistoryController {

    private final IOrderStatusHistoryHandler orderStatusHistoryHandler;

    @Operation(
            summary = "Create order status history",
            description = "Registers a new order status history entry when an order changes state."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order status history successfully created",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/create")
    public ResponseEntity<Void> saveOrderStatusHistory(@RequestBody @Valid OrderStatusHistoryRequest request) {
        orderStatusHistoryHandler.saveOrderStatusHistory(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Get order status history by orderId",
            description = "Retrieves the order status history for a given orderId, ensuring the authenticated user is the owner."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status history retrieved successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not own the order",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Order status history not found",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize(InfrastructureConstants.ROLE_CUSTOMER)
    @GetMapping("/list-by-order/{orderId}")
    public ResponseEntity<List<OrderStatusHistoryResponse>> getOrderStatusHistoryByOrderId(@PathVariable Long orderId) {
        List<OrderStatusHistoryResponse> response = orderStatusHistoryHandler.getOrderStatusHistoryByOrderId(orderId);
        return ResponseEntity.ok(response);
    }
}