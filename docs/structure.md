src/main/java/com/en/katmall/co/
├── KatmallApplication.java
│
├── shared/                          # SHARED KERNEL (Cross-cutting)
│   ├── domain/
│   │   ├── BaseEntity.java          # Abstract entity với id, createdAt, updatedAt
│   │   ├── AggregateRoot.java       # Marker interface cho Aggregate Root
│   │   ├── DomainEvent.java         # Base domain event
│   │   └── ValueObject.java         # Base value object
│   ├── exception/
│   │   ├── DomainException.java
│   │   ├── NotFoundException.java
│   │   └── ValidationException.java
│   ├── util/
│   │   ├── IdGenerator.java
│   │   └── DateTimeUtils.java
│   └── infrastructure/
│       ├── config/
│       │   ├── SecurityConfig.java
│       │   ├── JpaConfig.java
│       │   └── WebConfig.java
│       └── persistence/
│           └── BaseRepository.java
│
├── identity/                        # IDENTITY BOUNDED CONTEXT
│   ├── domain/
│   │   ├── model/
│   │   │   ├── User.java            # Aggregate Root
│   │   │   ├── Administrator.java   # Aggregate Root
│   │   │   ├── Role.java            # Entity
│   │   │   ├── Permission.java      # Entity
│   │   │   └── valueobject/
│   │   │       ├── Email.java       # Value Object
│   │   │       ├── Password.java
│   │   │       └── AuthProvider.java
│   │   ├── repository/
│   │   │   ├── UserRepository.java           # Interface (Port)
│   │   │   └── AdministratorRepository.java
│   │   ├── service/
│   │   │   └── PasswordEncoder.java          # Domain Service Interface
│   │   └── event/
│   │       ├── UserRegisteredEvent.java
│   │       └── UserLockedEvent.java
│   ├── application/
│   │   ├── command/
│   │   │   ├── RegisterUserCommand.java
│   │   │   ├── LoginCommand.java
│   │   │   └── LockUserCommand.java
│   │   ├── query/
│   │   │   ├── GetUserByIdQuery.java
│   │   │   └── GetUserByEmailQuery.java
│   │   ├── handler/
│   │   │   ├── RegisterUserHandler.java
│   │   │   └── LoginHandler.java
│   │   ├── dto/
│   │   │   ├── UserDto.java
│   │   │   └── LoginResponseDto.java
│   │   └── mapper/
│   │       └── UserMapper.java
│   ├── infrastructure/
│   │   ├── persistence/
│   │   │   ├── entity/
│   │   │   │   └── UserJpaEntity.java
│   │   │   ├── repository/
│   │   │   │   └── UserJpaRepository.java   # Implements UserRepository
│   │   │   └── mapper/
│   │   │       └── UserPersistenceMapper.java
│   │   ├── security/
│   │   │   ├── JwtTokenProvider.java
│   │   │   └── CustomUserDetailsService.java
│   │   └── external/
│   │       └── OAuthProviderClient.java
│   └── interfaces/
│       ├── rest/
│       │   ├── AuthController.java
│       │   └── UserController.java
│       └── web/
│           └── LoginPageController.java
│
├── catalog/                         # CATALOG BOUNDED CONTEXT
│   ├── domain/
│   │   ├── model/
│   │   │   ├── Product.java         # Aggregate Root
│   │   │   ├── ProductVariant.java  # Entity (trong Product Aggregate)
│   │   │   ├── Category.java        # Aggregate Root
│   │   │   ├── Brand.java           # Entity
│   │   │   └── valueobject/
│   │   │       ├── Sku.java
│   │   │       ├── Money.java
│   │   │       └── Slug.java
│   │   ├── repository/
│   │   │   ├── ProductRepository.java
│   │   │   └── CategoryRepository.java
│   │   └── service/
│   │       └── PricingService.java
│   ├── application/
│   │   ├── command/
│   │   │   ├── CreateProductCommand.java
│   │   │   └── UpdateProductCommand.java
│   │   ├── query/
│   │   │   └── SearchProductsQuery.java
│   │   └── handler/
│   │       └── ProductCommandHandler.java
│   ├── infrastructure/
│   │   └── persistence/
│   │       └── ProductJpaRepository.java
│   └── interfaces/
│       └── rest/
│           └── ProductController.java
│
├── inventory/                       # INVENTORY BOUNDED CONTEXT
│   ├── domain/
│   │   ├── model/
│   │   │   ├── InventoryStock.java  # Aggregate Root
│   │   │   ├── InventoryLocation.java
│   │   │   └── InventoryTransaction.java
│   │   ├── repository/
│   │   │   └── InventoryRepository.java
│   │   └── service/
│   │       └── StockReservationService.java
│   ├── application/
│   │   ├── command/
│   │   │   ├── ReserveStockCommand.java
│   │   │   └── ReleaseStockCommand.java
│   │   └── handler/
│   │       └── InventoryCommandHandler.java
│   ├── infrastructure/
│   │   └── persistence/
│   └── interfaces/
│       └── rest/
│           └── InventoryController.java
│
├── ordering/                        # ORDERING BOUNDED CONTEXT
│   ├── domain/
│   │   ├── model/
│   │   │   ├── Order.java           # Aggregate Root
│   │   │   ├── OrderItem.java       # Entity (trong Order Aggregate)
│   │   │   ├── Shipment.java        # Entity
│   │   │   └── valueobject/
│   │   │       ├── OrderNumber.java
│   │   │       ├── OrderStatus.java
│   │   │       └── Address.java
│   │   ├── repository/
│   │   │   └── OrderRepository.java
│   │   ├── service/
│   │   │   └── OrderDomainService.java
│   │   └── event/
│   │       ├── OrderCreatedEvent.java
│   │       └── OrderShippedEvent.java
│   ├── application/
│   │   ├── command/
│   │   │   ├── PlaceOrderCommand.java
│   │   │   └── CancelOrderCommand.java
│   │   ├── saga/
│   │   │   └── OrderSaga.java       # Orchestration
│   │   └── handler/
│   │       └── OrderCommandHandler.java
│   ├── infrastructure/
│   │   └── persistence/
│   └── interfaces/
│       └── rest/
│           └── OrderController.java
│
├── payment/                         # PAYMENT BOUNDED CONTEXT
│   ├── domain/
│   │   ├── model/
│   │   │   ├── Payment.java
│   │   │   └── PaymentTransaction.java
│   │   └── service/
│   │       └── PaymentGateway.java  # Interface (Port)
│   ├── infrastructure/
│   │   └── gateway/
│   │       ├── MomoPaymentGateway.java
│   │       └── VnPayPaymentGateway.java
│   └── interfaces/
│       └── rest/
│           └── PaymentController.java
│
└── notification/                    # NOTIFICATION BOUNDED CONTEXT
    ├── domain/
    │   └── model/
    │       └── Notification.java
    ├── application/
    │   └── listener/
    │       └── OrderEventListener.java  # Listen to OrderCreatedEvent
    └── infrastructure/
        └── email/
            └── EmailService.java