package microservice_traceability.traceability.infrastructure.http.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import microservice_traceability.traceability.application.dto.EmployeeEfficiencyResponse;
import microservice_traceability.traceability.application.dto.OrderStatusHistoryRequest;
import microservice_traceability.traceability.application.dto.OrderStatusHistoryResponse;
import microservice_traceability.traceability.application.dto.OrderTimeResponse;
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
            description = "Retrieves the order status history for a given orderId, " +
                    "ensuring the authenticated user is the owner."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status history retrieved successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not own the order",
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

    @Operation(
            summary = "Get order processing time by orderId",
            description = "Retrieves the total time taken for an order from initiation to delivery, " +
                    "ensuring the authenticated user is the restaurant owner."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order processing time retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderTimeResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have permission to access" +
                    " this resource",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize(InfrastructureConstants.ROLE_OWNER)
    @GetMapping("/time-per-order/{orderId}")
    public ResponseEntity<OrderTimeResponse> getOrderTime(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderStatusHistoryHandler.getOrderTimeByOrderId(orderId));
    }

    @Operation(
            summary = "Get employee efficiency ranking",
            description = "Retrieves a ranking of employees based on their average order completion time, " +
                    "ensuring the authenticated user is the restaurant owner."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee efficiency ranking retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EmployeeEfficiencyResponse.class)))),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have permission to access " +
                    "this resource",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize(InfrastructureConstants.ROLE_OWNER)
    @GetMapping("/employee-ranking")
    public ResponseEntity<List<EmployeeEfficiencyResponse>> getEmployeeEfficiencyRanking() {
        return ResponseEntity.ok(orderStatusHistoryHandler.getEmployeeEfficiencyRanking());
    }
}