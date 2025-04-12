# E-Commerce Application Package Structure

## Overview

This document outlines the package structure of the e-commerce application, explaining the purpose and responsibility of each package.

## Package Organization

### Core Packages

- **controllers**: REST API endpoints and request handling
- **services**: Business logic implementation
  - **commands**: Write operations (create, update, delete)
  - **queries**: Read operations
- **repositories**: Data access layer
- **models**: Entity classes representing database tables
- **dtos**: Data Transfer Objects for API requests/responses
- **mappers**: Conversion between entities and DTOs

### Supporting Packages

- **components**: Specialized Spring components
  - **events**: Event publishers and listeners
  - **scheduling**: Scheduled tasks
  - **security**: Security-related components
  - **notifications**: Notification-related components
- **common**: Cross-cutting concerns
  - **constants**: Application constants
  - **helpers**: General helper methods
  - **utils**: Only truly cross-cutting utilities
- **exceptions**: Custom exception classes
- **configurations**: Spring configuration classes
- **filters**: HTTP request/response filters

### Domain-Specific Utilities

Domain-specific utilities are placed within their respective domain packages:

- **product/utils**: Utilities specific to product management
- **order/utils**: Utilities specific to order processing
- **user/utils**: Utilities specific to user management

## Best Practices

1. **Single Responsibility**: Each package and class should have a single, well-defined responsibility
2. **Domain-Driven Design**: Package structure should reflect the business domains
3. **Cohesion**: Related functionality should be grouped together
4. **Low Coupling**: Minimize dependencies between packages

## Naming Conventions

### Classes and Interfaces
- Use **PascalCase** (e.g., `ProductService`, `OrderController`)
- Class names should be nouns or noun phrases
- Interface names can be adjectives when they describe capabilities (e.g., `Pageable`, `Sortable`)
- Test classes should end with `Test` (e.g., `ProductServiceTest`)

### Methods
- Use **camelCase** (e.g., `createOrder`, `findByUsername`)
- Method names should be verbs or verb phrases
- Test methods should be descriptive of the test scenario (e.g., `shouldReturnUserWhenValidIdProvided`)

### Variables
- Use **camelCase** (e.g., `userName`, `orderTotal`)
- Boolean variables should use "is", "has", or "should" prefixes (e.g., `isActive`, `hasPermission`)
- Collection variables should be plural (e.g., `orders`, `userProfiles`)

### Constants
- Use **UPPER_SNAKE_CASE** (e.g., `MAX_RETRY_COUNT`, `DEFAULT_PAGE_SIZE`)

### Packages
- Use **lowercase** with dots as separators (e.g., `com.project.e_commerce.services`)
- Package names should be singular (e.g., `model`, not `models`)

### Entity Relationships
- Name fields based on their semantic meaning, not their database column names
- For relationships, use the entity name (e.g., `private Category category`, not `private Category categoryId`)

### Spring Data Repository Methods
- Follow Spring Data JPA naming conventions for query methods
- Parameter names should match the property names in the query method (e.g., `findByUsername(String username)`)

### Exceptions
- Exception class names should end with "Exception" (e.g., `InvalidDataException`)
- All custom exceptions should extend from the same base class (preferably `RuntimeException`)