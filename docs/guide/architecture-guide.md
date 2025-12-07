# KatMall Application Architecture Guide

## Overview

This document defines the standard architecture for all modules in the KatMall application, following **Clean Architecture** and **Domain-Driven Design (DDD)** principles.

---

## Module Structure

Each module (identity, catalog, ordering, payment, etc.) MUST follow this structure:

```
module/
├── application/           # Application Layer
│   ├── dto/               # Data Transfer Objects
│   │   ├── request/       # Request DTOs
│   │   └── response/      # Response DTOs
│   ├── usecase/           # Use Cases (one per business flow)
│   │   ├── CreateXxxUseCase.java
│   │   ├── GetXxxUseCase.java
│   │   ├── UpdateXxxUseCase.java
│   │   └── DeleteXxxUseCase.java
│   └── scheduler/         # Scheduled jobs (optional)
├── domain/                # Domain Layer
│   ├── model/             # Domain entities and value objects
│   ├── repository/        # Repository interfaces
│   ├── service/           # Domain services (business rules)
│   └── event/             # Domain events (optional)
├── infrastructure/        # Infrastructure Layer
│   └── persistence/
│       ├── entity/        # JPA entities
│       ├── mapper/        # Entity-Model mappers
│       └── repository/    # Repository implementations
└── interfaces/            # Interface Layer
    └── rest/              # REST controllers
```

---

## Layer Responsibilities

### 1. Application Layer (`application/`)

#### Use Cases (`usecase/`)
- **One use case class per business flow**
- Each use case has a single `execute()` method
- Orchestrates domain services and repositories
- Handles transaction boundaries
- Returns DTOs, not domain models

```java
@Service
@RequiredArgsConstructor
public class CreateProductUseCase {
    
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    
    @Transactional
    public ProductResponse execute(CreateProductRequest request) {
        // Orchestration logic
    }
}
```

#### DTOs (`dto/`)
- Request DTOs: Input validation with Jakarta annotations
- Response DTOs: Data returned to clients
- Use Lombok `@Data`, `@Builder`

### 2. Domain Layer (`domain/`)

#### Models (`model/`)
- Aggregate roots and entities
- Value objects
- Rich domain logic (not anemic)
- Suffix with `Model` (e.g., `UserModel`, `ProductModel`)

#### Repository Interfaces (`repository/`)
- Define contracts for persistence
- Only domain models, no JPA entities

#### Domain Services (`service/`)
- Complex business rules
- Logic that spans multiple aggregates
- Pure domain logic, no infrastructure

### 3. Infrastructure Layer (`infrastructure/`)

#### Persistence
- JPA entities (suffix with `JpaEntity`)
- Mappers to convert between JPA entities and domain models
- Repository implementations using Spring Data JPA

### 4. Interface Layer (`interfaces/`)

#### REST Controllers
- Thin layer, delegates to use cases
- Request validation
- Response formatting
- Error handling

```java
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    
    private final CreateProductUseCase createProductUseCase;
    private final GetProductUseCase getProductUseCase;
    
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> create(...) {
        return createProductUseCase.execute(request);
    }
}
```

---

## Naming Conventions

### Classes

| Type | Convention | Example |
|------|------------|---------|
| Use Case | `{Action}{Entity}UseCase` | `CreateProductUseCase` |
| Domain Model | `{Entity}Model` | `ProductModel` |
| JPA Entity | `{Entity}JpaEntity` | `ProductJpaEntity` |
| Repository (Interface) | `{Entity}Repository` | `ProductRepository` |
| Repository (Impl) | `{Entity}RepositoryImpl` | `ProductRepositoryImpl` |
| Controller | `{Entity}Controller` | `ProductController` |
| Request DTO | `{Action}{Entity}Request` | `CreateProductRequest` |
| Response DTO | `{Entity}Response` | `ProductResponse` |

### Methods

- Use Cases: `execute({Request})`
- Controllers: Match HTTP verb (`create()`, `get()`, `update()`, `delete()`)
- Repositories: `save()`, `findById()`, `findByXxx()`, `deleteById()`

---

## Use Case Patterns

### CRUD Operations

```
CreateXxxUseCase.java    → POST   /api/v1/xxx
GetXxxUseCase.java       → GET    /api/v1/xxx/{id}
ListXxxUseCase.java      → GET    /api/v1/xxx
UpdateXxxUseCase.java    → PUT    /api/v1/xxx/{id}
DeleteXxxUseCase.java    → DELETE /api/v1/xxx/{id}
```

### Complex Flows

```
RegisterUseCase.java           → POST /api/v1/auth/register
VerifyRegistrationUseCase.java → GET  /api/v1/auth/verify
CheckoutUseCase.java           → POST /api/v1/orders/checkout
ProcessPaymentUseCase.java     → POST /api/v1/payments/process
```

---

## ⚠️ DO NOT

- ❌ Put business logic in controllers
- ❌ Create monolithic "service" classes with many methods
- ❌ Return domain models from controllers
- ❌ Access repositories directly from controllers
- ❌ Put infrastructure code in domain layer
- ❌ Use `application/service/` folder (use `application/usecase/` instead)

---

## ✅ DO

- ✅ One use case per business operation
- ✅ Use dependency injection
- ✅ Write Javadoc for ALL methods
- ✅ Use i18n keys for messages
- ✅ Handle errors with proper exceptions
- ✅ Validate input at DTO level
- ✅ Keep use cases focused and testable

---

## Migration Guide

To migrate existing modules from `service/` to `usecase/`:

1. Create `usecase/` folder in `application/`
2. Split each service method into a separate use case class
3. Update controllers to inject use cases instead of services
4. Delete the `service/` folder
5. Update imports and tests

Example:
```
# Before
ProductService.create()
ProductService.findById()
ProductService.update()

# After
CreateProductUseCase.execute()
GetProductUseCase.execute()
UpdateProductUseCase.execute()
```
